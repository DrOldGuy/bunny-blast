/**
 * 
 */
package bunny.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import bunny.dao.BunnyDao;
import bunny.entity.AddBreedRequest;
import bunny.entity.Breed;
import bunny.exception.DeleteBreedException;
import lombok.extern.slf4j.Slf4j;

/**
 * The bunny service implements the service layer in the bunny breed application. It acts as a
 * go-between for the controller (input/output) layer and the data (dao) layer.
 * <p>
 * Transactions are typically managed by the service layer so they are managed in this class. The
 * at-transactional annotation starts a transaction in the public service methods. If an exception
 * is thrown in the execution of the method, Spring JDBC rolls back the transaction. If no exception
 * is thrown, Spring JDBC commits the transaction.
 * <p>
 * A transaction manager is automatically created by Spring Boot when Spring JDBC is detected on the
 * classpath. Each method annotated with @transactional is wrapped with AOP advice that starts the
 * transaction, calls the method and commits or rolls back the transaction after the method has
 * completed.
 * <p>
 * A word of advice about the AOP advice: The wrapped methods really mess with the debugger. If you
 * need to debug into this class, comment out all lines with @transactional on them and uncomment
 * the lines when you are finished debugging.
 * 
 * @author Promineo
 *
 */
@Service // Tell Spring this is a singleton-scope managed Bean
@Slf4j // Add an Slf4j logger (Lombok)
public class BunnyService {

  /** Tell Spring to create and inject a DAO object. */
  @Autowired
  private BunnyDao dao;

  /**
   * Returns a list of bunny breeds. This method first gets the list of breeds, then it gets the
   * alternate breed names and category names if they exist. It is possible to do joins in such a
   * way as to get all the information in one query but this causes a lot of data to be duplicated
   * as well as returning a lot of {@code null}s. By fetching the breed list and then adding in
   * categories and alternate names, it makes the Java code a lot easier and more understandable.
   * 
   * @return The list of breeds.
   */
  @Transactional(readOnly = true)
  public List<Breed> listBunnyBreeds() {
    log.info("Service: List bunny breeds");

    List<Breed> breeds = dao.fetchAllBreeds();

    breeds.forEach(breed -> {
      breed.getAlternameNames().addAll(dao.fetchAlternameNames(breed.getBreedId()));
      breed.getCategoryNames().addAll(dao.fetchBreedCategories(breed.getBreedId()));
    });

    return breeds;
  }

  /**
   * Return a specific breed that has the given breed ID.
   * 
   * @param breedId The breed ID
   * @return A bunny breed object with all category names and alternate breed names
   * @throws NoSuchElementException Thrown if the breed ID does not exist in the database.
   */
  @Transactional(readOnly = true)
  public Breed getBunnyBreed(int breedId) {
    log.info("Service: Get bunny with ID={}", breedId);

    Breed breed = dao.fetchBunny(breedId)
        .orElseThrow(() -> new NoSuchElementException("Unknown bunny with breed ID=" + breedId));

    breed.getAlternameNames().addAll(dao.fetchAlternameNames(breedId));
    breed.getCategoryNames().addAll(dao.fetchBreedCategories(breedId));

    return breed;
  }

  /**
   * Add a new bunny breed.
   * 
   * @param breedRequest The breed request object
   * @return The bunny breed with the breed ID created by MySQL
   */
  @Transactional(readOnly = false)
  public Breed addBunny(AddBreedRequest breedRequest) {
    log.info("Service: Adding bunny {}", breedRequest);
    return dao.insertBunny(breedRequest);
  }

  /**
   * Modify a bunny breed including the alternate breed names and the category names.
   * 
   * @param breed The breed object
   * @return The breed object if successful
   * @throws NoSuchElementException Thrown if the breed with the given ID does not exist.
   */
  @Transactional(readOnly = false)
  public Breed modifyBunny(Breed breed) {
    if (!dao.modifyBunny(breed)) {
      throw new NoSuchElementException("Unknown bunny with breed ID=" + breed.getBreedId());
    }

    return breed;
  }

  /**
   * Delete a bunny breed and all associated breed categories and alternate breed names.
   * 
   * @param breedId The ID of the breed to delete
   * @throws DeleteBreedException Thrown if an error occurs deleting the breed.
   */
  @Transactional(readOnly = false)
  public void deleteBunny(int breedId) {
    log.info("Service: Delete bunny with ID={}", breedId);

    /* Fetch the breed first. This will throw an exception if the breed does not exist. */
    getBunnyBreed(breedId);

    if (!dao.deleteBunnyBreed(breedId)) {
      throw new DeleteBreedException("Unable to delete breed with ID=" + breedId);
    }
  }
}
