/**
 * 
 */
package bunny.dao;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import bunny.entity.AddBreedRequest;
import bunny.entity.Category;
import bunny.entity.Breed;
import bunny.service.BunnyService;
import lombok.extern.slf4j.Slf4j;

/**
 * This class interacts with the database directly. It uses a {@link NamedParameterJdbcTemplate} to
 * perform queries, inserts, updates, and deletes. With the NamedParameterJdbcTemplate, the SQL
 * contains placeholder names like this: ":key". A parameter map is then passed to the
 * NamedParameterJdbcTemplate with the keys and values. So, if you have a placeholder named
 * :breed_id (you must include the colon), you would add "breed_id" as a key to the map with the
 * integer breedId as the value.
 * <p>
 * Note that transactions are managed in the {@link BunnyService} class.
 * <p>
 * Spring can inject this class into the controller without an interface. Since there's only one
 * implementation class, an interface is not really needed.
 * <p>
 * This class uses text blocks, which are indicated by three double quotes ("""). Text blocks were
 * introduced as part of the Java language in Java 15. Text blocks are inherently formatted Strings.
 * If you look at the resulting String in the debugger you will see that each line in the text block
 * is separated by a linefeed. The formatted() method acts like String.format(). The same format
 * specifiers used in text blocks are used in String.format().
 * <p>
 * The only format specifier used in the text blocks in this class is %s. This takes a String
 * parameter. So, when you see """SELECT * FROM %s WHERE %s = :%s".formatted(BREED_TABLE, BREED_ID,
 * BREED_ID) it means substitute the first placeholder with the value of BREED_TABLE, and substitute
 * the second and third placeholders with the value of BREED_ID. Placeholders are replaced in the
 * same order from left to right as the parameters in the formatted() method. So, the result of the
 * SQL string in the example above is "SELECT * FROM breed WHERE breed_id = :breed_id".
 * <p>
 * Re: @SuppressWarnings
 * <ul>
 * <li>java:S1192 - Suppress the warning that there is duplicate code in the SQL strings
 * <li>java:S125 - Suppress the warning that there is code that is commented out
 * </ul>
 * 
 * @author Promineo
 *
 */
@Component // Tells Spring to manage this Bean with singleton scope (the default scope)
@Slf4j // Lombok creates an Slf4j logger with this annotation
@SuppressWarnings({"java:S1192", "java:S125"})
public class BunnyDao {

  /** These constants are the column names. They can be easily changed here if necessary. */
  private static final String ALTERNATE_NAME = "alternate_name";
  private static final String BREED_ID = "breed_id";
  private static final String BREED_NAME = "breed_name";
  private static final String CATEGORY_ID = "category_id";
  private static final String CATEGORY_NAME = "category_name";
  private static final String DESCRIPTION = "description";

  /** These are the table names. */
  private static final String ALT_NAME_TABLE = "alt_name";
  private static final String BREED_CATEGORY_TABLE = "breed_category";
  private static final String BREED_TABLE = "breed";
  private static final String CATEGORY_TABLE = "category";

  /**
   * Spring injects a NamedParameterJdbcTemplate, which manages the conversion of placeholders to
   * parameter values. The parameter values are injected into a JDBC {@link PreparedStatement} in
   * the proper order so that SQL injection is mitigated.
   */
  @Autowired
  private NamedParameterJdbcTemplate jdbcTemplate;

  /**
   * This returns a list of all breeds in the breed table.
   * 
   * @return The list of breeds.
   */
  public List<Breed> fetchAllBreeds() {
    log.info("Dao: List bunny breeds");

    /*
     * This is a text block. The parameter placeholders (%s) are replaced by the parameters in the
     * formatted() method in the same order that they occur in the text block. When formatted, this
     * will be "SELECT b.* FROM breed b ORDER BY b.breed_name". No parameter map is required.
     */
    String sql = """
        SELECT b.*
        FROM %s b
        ORDER BY b.%s
        """.formatted(BREED_TABLE, BREED_NAME);

    /*
     * Note the Lambda expression that replaces the RowMapper anonymous inner class. This could also
     * be written like this:
     */
    // return jdbcTemplate.query(sql, new RowMapper<>() {
    // @Override
    // public Breed mapRow(ResultSet rs, int rowNum) throws SQLException {
    // return Breed.builder()
    // .breedId(rs.getInt(BREED_ID))
    // .breedName(rs.getString(BREED_NAME))
    // .description(rs.getString(DESCRIPTION))
    // .build();
    // }});

    return jdbcTemplate.query(sql, (rs, rowNum) -> // @formatter:off
        Breed.builder()
            .breedId(rs.getInt(BREED_ID))
            .breedName(rs.getString(BREED_NAME))
            .description(rs.getString(DESCRIPTION))
            .build()); // @formatter:on
  }

  /**
   * Returns the alternate names for a given breed. This is done in a separate method because, if a
   * join is used, the breed information is repeated for each row with an alternate name. For
   * example, if you have the SQL query like this:
   * 
   * <pre>
   * SELECT b.*, an.*
   * FROM breed b
   * LEFT JOIN alt_name an USING (breed_id)
   * </pre>
   * 
   * you will get a bunch of nulls for breeds with no alternate names and duplicate row data for
   * breeds with multiple alternate names like this partial report:
   * 
   * <pre>
   * breed_id | breed_name      | description  | alternate_id | breed_id | alternate_name 
   *        1 | American Rabbit | American...  |       [NULL] |   [NULL] | [NULL]
   *       10 | Checked Giant   | Checkered... |            2 |       10 | Giant Papillon
   *       11 | Chinchilla      | Chinchill... |       [NULL] |   [NULL] | [NULL]
   *       14 | Dwarf Lop       | Dwarf lop... |            5 |       14 | Klein Widder
   *       14 | Dwarf Lop       | Dwarf lop... |            5 |       14 | Mini Lop
   * </pre>
   * 
   * The bottom line: it's easier to do it like this so you don't have to churn through all the data
   * in the service class to clean it up.
   * 
   * @param breedId The breed ID
   * @return A list of alternate names for the given breed.
   */
  public List<String> fetchAlternameNames(int breedId) {
    /*
     * When formatted this will be: "SELECT alternate_name FROM alt_name WHERE breed_id = :breed_id
     * ORDER BY alternate_name". The placeholder ":breed_id" means that "breed_id" must be added to
     * the parameter map.
     */
    String sql = """
        SELECT %s
        FROM %s
        WHERE %s = :%s
        ORDER BY %s
        """.formatted(ALTERNATE_NAME, ALT_NAME_TABLE, BREED_ID, BREED_ID, ALTERNATE_NAME);

    Map<String, Object> params = Map.of(BREED_ID, breedId);
    return jdbcTemplate.query(sql, params, (rs, rowNum) -> rs.getString(ALTERNATE_NAME));

    /* The above line is the same as: */
    // return jdbcTemplate.query(sql, params, new RowMapper<>() {
    // @Override
    // public String mapRow(ResultSet rs, int rowNum) throws SQLException {
    // return rs.getString(ALTERNATE_NAME);
    // }});
  }

  /**
   * Returns the breed categories associated with the given breed IDs.
   * 
   * @param breedId The breed ID.
   * @return A list of category names.
   */
  public List<String> fetchBreedCategories(int breedId) {
    /*
     * When formatted this will be: "SELECT c.category_name FROM category c JOIN breed_category bc
     * USING (category_id) WHERE bc.breed_id = :breed_id ORDER BY category_name". The parameter in
     * the SQL query ":breed_id" means that the key "breed_id" and the value must be in the
     * parameter map.
     */
    String sql = """
        SELECT c.%s
        FROM %s c
        JOIN %s bc USING (%s)
        WHERE bc.%s = :%s
        ORDER BY c.%s
        """.formatted(CATEGORY_NAME, CATEGORY_TABLE, BREED_CATEGORY_TABLE, CATEGORY_ID, BREED_ID,
        BREED_ID, CATEGORY_NAME);

    Map<String, Object> params = Map.of(BREED_ID, breedId);
    return jdbcTemplate.query(sql, params, (rs, rowNum) -> rs.getString(CATEGORY_NAME));

    /* The query in the line above can be replaced with the anonymous inner class like: */
    // return jdbcTemplate.query(sql, params, new RowMapper<>() {
    // @Override
    // public String mapRow(ResultSet rs, int rowNum) throws SQLException {
    // return rs.getString(CATEGORY_NAME);
    // }});
  }

  /**
   * Returns a specific bunny breed with the given the breed ID.
   * 
   * @param breedId The breed ID.
   * @return The Breed record if found. An empty Optional if not found.
   */
  public Optional<Breed> fetchBunny(int breedId) {
    log.info("Dao: Get bunny with ID={}", breedId);

    /*
     * When formatted, the query will be: "SELECT * FROM breed b WHERE breed_id = :breed_id". The
     * placeholder ":breed_id" means that the key "breed_id" and associated value must be put in the
     * parameter map.
     */
    String sql = """
        SELECT b.*
        FROM %s b
        WHERE %s = :%s
        """.formatted(BREED_TABLE, BREED_ID, BREED_ID);

    Map<String, Object> params = Map.of(BREED_ID, breedId);

    Breed breed = jdbcTemplate.query(sql, params, (ResultSet rs) -> {
      if (rs.next()) {
        return Breed
            .builder() // @formatter:off
            .breedId(rs.getInt(BREED_ID))
            .breedName(rs.getString(BREED_NAME))
            .description(rs.getString(DESCRIPTION))
            .build(); // @formatter:on
      }

      return null;
    });

    /* To use an anonymous inner class with a ResultSetExtractor, you would write it like this: */
    // Breed breed = jdbcTemplate.query(sql, params, new ResultSetExtractor<>() {
    // @Override
    // public Breed extractData(ResultSet rs) throws SQLException {
    // if (rs.next()) {
    // return Breed
    // .builder()
    // .breedId(rs.getInt(BREED_ID))
    // .breedName(rs.getString(BREED_NAME))
    // .description(rs.getString(DESCRIPTION))
    // .build();
    // }
    //
    // return null;
    // }
    // });

    return Optional.ofNullable(breed);
  }

  /**
   * Add a new bunny breed to the breed table. This also adds alternate breed names and categories.
   * If the category name does not exist it is created. Then the association is made in the
   * breed_category table.
   * 
   * @param breedRequest A {@link AddBreedRequest} object.
   * @return The inserted breed object.
   * @throws DuplicateKeyException Thrown if the bunny breed has the same name as an existing breed.
   */
  public Breed insertBunny(AddBreedRequest breedRequest) {
    log.info("Dao: Adding bunny {}", breedRequest);

    String sql = """
        INSERT INTO %s
        (%s, %s)
        VALUES
        (:%s, :%s)
        """.formatted(BREED_TABLE, BREED_NAME, DESCRIPTION, BREED_NAME, DESCRIPTION);

    Map<String, Object> paramMap =
        Map.of(BREED_NAME, breedRequest.getBreedName(), DESCRIPTION, breedRequest.getDescription());

    SqlParameterSource params = new MapSqlParameterSource(paramMap);
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(sql, params, keyHolder);

    /*
     * This method call retrieves the primary key value generated by MySQL. SonarLint generates
     * warning "java:S2259" because it thinks that a NullPointerException could be thrown if
     * getKeyAs() returns null. Although this may be true, it is extremely unlikely. Also, the
     * NullPointerException will definitely pinpoint the problem, so allow it to be thrown if it
     * ever is.
     */
    @SuppressWarnings("java:S2259")
    int breedId = keyHolder.getKeyAs(BigInteger.class).intValue();

    insertBreedCategories(breedId, breedRequest.getCategoryNames());
    insertBreedAlternateNames(breedId, breedRequest.getAlternameNames());

    Breed breed = Breed
        .builder() // @formatter:off
          .breedId(breedId)
          .breedName(breedRequest.getBreedName())
          .description(breedRequest.getDescription())
          .build();// @formatter:on

    breed.getAlternameNames().addAll(breedRequest.getAlternameNames());
    breed.getCategoryNames().addAll(breedRequest.getCategoryNames());

    return breed;
  }

  /**
   * Add the altername breed names to the alt_name table.
   * 
   * @param breedId The ID assigned to the breed.
   * @param alternateNames The list of alternate breed names.
   */
  private void insertBreedAlternateNames(int breedId, List<String> alternateNames) {
    /*
     * When formatted this will be:
     * "INSERT INTO alt_name (breed_id, alternate_name) VALUES (:breed_id, :alternate_name)". There
     * are two replaceable parameters, which means that the keys "breed_id" and "alternate_name"
     * along with the values, must be added to the parameter map.
     */
    String sql = """
        INSERT INTO %s
        (%s, %s)
        VALUES
        (:%s, :%s)
        """.formatted(ALT_NAME_TABLE, BREED_ID, ALTERNATE_NAME, BREED_ID, ALTERNATE_NAME);

    /*
     * Don't use Map.of() here because it returns an immutable map. We need to be able to replace
     * the "alternate_name" value for each alternate name.
     */
    Map<String, Object> params = new HashMap<>();
    params.put(BREED_ID, breedId);

    alternateNames.forEach(alternateName -> {
      params.put(ALTERNATE_NAME, alternateName);
      jdbcTemplate.update(sql, params);
    });

    /* If you don't like Lambda expressions, you can use an enhanced for loop like this: */
    // for(String alternateName : alternateNames) {
    // params.put(ALTERNATE_NAME, alternateName);
    // jdbcTemplate.update(sql, params);
    // }
  }

  /**
   * Insert the rows into the breed_category join table.
   * 
   * @param breedId The ID of the breed record.
   * @param categoryNames The list of category names. If the category name does not exist in the
   *        category table, it is added.
   */
  private void insertBreedCategories(int breedId, List<String> categoryNames) {
    /*
     * Here's the logic: 1) fetchOrCreateCategories() returns a list of Category objects. If the
     * category name was already in the category table, it is returned. If the category name is not
     * in the category table, it is added and returned in the list. 2) The forEach() method is
     * called on the returned list of Category objects. The Lambda expression inserts a
     * breed_category row for each category name.
     */
    fetchOrCreateCategories(categoryNames)
        .forEach(category -> insertBreedCategory(breedId, category.getCategoryId()));

    /* This could be written like this: */
    // List<Category> categories = fetchOrCreateCategories(categoryNames);
    //
    // for(Category category : categories) {
    // insertBreedCategory(breedId, category.getCategoryId());
    // }
  }

  /**
   * Insert a row into the breed_category join table.
   * 
   * @param breedId The breed ID
   * @param categoryId The category ID
   */
  private void insertBreedCategory(int breedId, int categoryId) {
    /*
     * When formatted, the SQL looks like this:
     * "INSERT INTO breed_category (breed_id, category_id) VALUES (:breed_id, :category_id)". The
     * two parameter placeholders ":breed_id" and ":category_id" mean that the keys "breed_id" and
     * "category_id" along with their associated values must be placed into the parameter map.
     */
    String sql = """
        INSERT INTO %s (%s, %s)
        VALUES
        (:%s, :%s)
        """.formatted(BREED_CATEGORY_TABLE, BREED_ID, CATEGORY_ID, BREED_ID, CATEGORY_ID);

    Map<String, Object> params = Map.of(BREED_ID, breedId, CATEGORY_ID, categoryId);
    jdbcTemplate.update(sql, params);
  }

  /**
   * Retrieve or create and retrieve the Category objects with the given category names.
   * 
   * @param categoryNames A list of category names that belong to the working breed object.
   * @return The list of {@link Category} objects, either fetched or inserted.
   */
  private List<Category> fetchOrCreateCategories(List<String> categoryNames) {
    List<Category> categories = new LinkedList<>();

    /* Iterate over the list of category names. This could be written as an enhanced for loop. */
    categoryNames.forEach(categoryName -> {
      /*
       * Retrieve the category with the given name. If the Optional is empty, create the category.
       */
      Optional<Category> optionalCategory = fetchCategoryByName(categoryName);

      if (optionalCategory.isPresent()) {
        /* Add the retrieved Category object to the list. */
        categories.add(optionalCategory.get());
      } else {
        /* Add the created Category to the list. */
        categories.add(insertCategory(categoryName));
      }
    });

    return categories;
  }

  /**
   * Insert a new category row with the given category name.
   * 
   * @param categoryName The category name.
   * @return A {@link Category} object that contains the contents of the category row.
   */
  private Category insertCategory(String categoryName) {
    /*
     * When formatted, the SQL statement looks like this:
     * "INSERT INTO category (category_name) VALUES (:category_name)". The placeholder
     * ":category_name" means the the key "category_name" and the name value must be added to the
     * parameter map.
     */
    String sql = """
        INSERT INTO %s
        (%s)
        VALUES
        (:%s)
        """.formatted(CATEGORY_TABLE, CATEGORY_NAME, CATEGORY_NAME);

    SqlParameterSource params = new MapSqlParameterSource(Map.of(CATEGORY_NAME, categoryName));
    KeyHolder keyHolder = new GeneratedKeyHolder();

    jdbcTemplate.update(sql, params, keyHolder);

    /*
     * This method call retrieves the primary key value generated by MySQL. SonarLint generates
     * warning "java:S2259" because it thinks that a NullPointerException could be thrown if
     * getKeyAs() returns null. Although this may be true, it is extremely unlikely. Also, the
     * NullPointerException will definitely pinpoint the problem, so allow it to be thrown if it
     * ever is.
     */
    @SuppressWarnings("java:S2259")
    int categoryId = keyHolder.getKeyAs(BigInteger.class).intValue();

    return Category
        .builder() // @formatter:off
          .categoryId(categoryId)
          .categoryName(categoryName)
          .build(); // @formatter:on
  }

  /**
   * Returns the Category object as an Optional.
   * 
   * @param categoryName The name of the category to retrieve.
   * @return If the category name exists in the table, the row is returned as a Category object in
   *         the Optional. If it does not exist, an empty Optional is returned.
   */
  private Optional<Category> fetchCategoryByName(String categoryName) {
    /*
     * When formatted, the SQL query looks like this:
     * "SELECT * FROM category WHERE category_name = :category_name". The placeholder
     * ":category_name" means that the parameter map must contain the key "category_name" with the
     * associated name value.
     */
    String sql = """
        SELECT *
        FROM %s
        WHERE %s = :%s
        """.formatted(CATEGORY_TABLE, CATEGORY_NAME, CATEGORY_NAME);

    SqlParameterSource params = new MapSqlParameterSource(Map.of(CATEGORY_NAME, categoryName));

    /*
     * The Lambda expression returns an Optional with a Category object if the category name is
     * found. It returns an empty Optional if the category name is not found. Note that (at least at
     * the time this was written) the Java compiler can't infer the parameter type for the
     * ResultSetExtractor extractData method. So the parameter type (ResultSet) must be supplied.
     */
    return Optional.ofNullable(jdbcTemplate.query(sql, params, (ResultSet rs) -> {
      if (rs.next()) {
        return Category
            .builder() // @formatter:off
            .categoryId(rs.getInt(CATEGORY_ID))
            .categoryName(rs.getString(CATEGORY_NAME))
            .build(); // @formatter:on
      }

      return null;
    }));

    /*
     * If you don't like Lambda expressions (or compound Optional assignments) you can write the
     * above like this:
     */
    // Category category = jdbcTemplate.query(sql, params, new ResultSetExtractor<>() {
    // @Override
    // public Category extractData(ResultSet rs) throws SQLException {
    // if (rs.next()) {
    // return Category
    // .builder()
    // .categoryId(rs.getInt(CATEGORY_ID))
    // .categoryName(rs.getString(CATEGORY_NAME))
    // .build();
    // }
    //
    // return null;
    // }
    // });
    //
    // return Optional.ofNullable(category);
  }

  /**
   * Modify the bunny breed row or the alternate names or categories.
   * 
   * @param breedRequest The modified data. This must contain a valid breed ID.
   * @return {@code true} if successful. Returns {@code false} if the breed ID is invalid.
   */
  public boolean modifyBunny(Breed breedRequest) {
    /*
     * When formatted, the SQL statement is this: "UPDATE breed SET breed_name = :breed_name,
     * description = :description WHERE breed_id = :breed_id". The parameter map must contain values
     * for the keys "breed_name", "description", and "breed_id".
     */
    String sql = """
        UPDATE %s
        SET %s = :%s, %s = :%s
        WHERE %s = :%s
        """.formatted(BREED_TABLE, BREED_NAME, BREED_NAME, DESCRIPTION, DESCRIPTION, BREED_ID,
        BREED_ID);

    Map<String, Object> params = Map
        .of( // @formatter:off
        BREED_NAME, breedRequest.getBreedName(), 
        DESCRIPTION, breedRequest.getDescription(), 
        BREED_ID, breedRequest.getBreedId()); // @formatter:on

    /*
     * update() returns the number of rows updated. If the primary key value is valid, the result
     * should be 1 row.
     */
    boolean updated = jdbcTemplate.update(sql, params) == 1;

    if (updated) {
      updateBreedAlternateNames(breedRequest.getBreedId(), breedRequest.getAlternameNames());
      updateBreedCategories(breedRequest.getBreedId(), breedRequest.getCategoryNames());
    }

    return updated;
  }

  /**
   * Rather than executing a complex algorithm to see what's in the database and adding changed
   * values, it's a lot easier (and probably just a fast) to simply delete all the rows in the join
   * table that match the breed ID and re-add the new ones.
   * 
   * @param breedId The breed ID
   * @param categoryNames The category names to add.
   */
  private void updateBreedCategories(Integer breedId, List<String> categoryNames) {
    deleteBreedCategories(breedId);
    insertBreedCategories(breedId, categoryNames);
  }

  /**
   * Delete the breed categories associated with the breed to be modified.
   * 
   * @param breedId The breed ID
   */
  private void deleteBreedCategories(Integer breedId) {
    /*
     * When formatted, this works out to: "DELETE FROM breed_category WHERE breed_id = :breed_id".
     * This means that we need to add "breed_id" to the parameter map.
     */
    String sql = """
        DELETE FROM %s
        WHERE %s = :%s
        """.formatted(BREED_CATEGORY_TABLE, BREED_ID, BREED_ID);

    Map<String, Object> params = Map.of(BREED_ID, breedId);
    jdbcTemplate.update(sql, params);
  }

  /**
   * Just like categories, delete all the alternate names associated with the breed ID and re-add
   * the given ones.
   * 
   * @param breedId The breed ID
   * @param alternameNames The list of alternate names
   */
  private void updateBreedAlternateNames(Integer breedId, List<String> alternameNames) {
    deleteBreedAlternateNames(breedId);
    insertBreedAlternateNames(breedId, alternameNames);
  }

  /**
   * Delete the alternate names associated with the breed.
   * 
   * @param breedId The breed ID that forms the association.
   */
  private void deleteBreedAlternateNames(Integer breedId) {
    /*
     * When formatted, the SQL statement is: "DELETE FROM alt_name WHERE breed_id = :breed_id". This
     * means we need to add "breed_id" to the parameter map.
     */
    String sql = """
        DELETE FROM %s
        WHERE %s = :%s
        """.formatted(ALT_NAME_TABLE, BREED_ID, BREED_ID);

    Map<String, Object> params = Map.of(BREED_ID, breedId);
    jdbcTemplate.update(sql, params);
  }

  /**
   * Delete the breed with the given breed ID. Because the foreign keys were created with ON DELETE
   * CASCADE we don't need to delete the child rows (alternate names and category join table rows).
   * MySQL will do that for us automatically.
   * 
   * @param breedId The breed to delete
   * @return {@code true} if the breed was deleted, {@code false} otherwise.
   */
  public boolean deleteBunnyBreed(int breedId) {
    log.info("Dao: Delete bunny with ID={}", breedId);

    /* When formatted the SQL statement is: "DELETE FROM breed WHERE breed_id = :breed_id" */
    String sql = """
        DELETE FROM %s
        WHERE %s = :%s
        """.formatted(BREED_TABLE, BREED_ID, BREED_ID);

    Map<String, Object> params = Map.of(BREED_ID, breedId);

    /* Return true if the number of rows deleted is 1. */
    return jdbcTemplate.update(sql, params) == 1;
  }
}
