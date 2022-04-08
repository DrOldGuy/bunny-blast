/**
 * 
 */
package bunny.entity;

import java.util.LinkedList;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class is a Data Transfer Object (DTO). It is simply a Java object that contains data that
 * describes a bunny breed. This object is used in the modify breed operation. In order to work, the
 * JSON object that is passed in the modify breed request must match this object exactly. Here is a
 * sample of a JSON object that can be used for the modify breed request:
 * 
 * <pre>
 * {
    "breedId": 14,
    "breedName": "Dwarf Lop",
    "description": "Dwarf Lops are popular show rabbit breeds in the US.",
    "categoryNames": [
      "lop-eared",
      "smooth",
      "spotted"
    ],
    "alternameNames": [
      "Klein Widder",
      "Mini Lop"
    ]
  }
 * </pre>
 * 
 * Bean Validation is only performed on incoming data (i.e., the modify bunny breed operation). It
 * is not validated on outgoing data operations like retrieving bunny breed operations.
 * <p>
 * Note the Lombok annotations on the class. @Data adds getters and setters, hashCode(), equals()
 * and toString(). @NoArgsConstructor adds a second constructor with no arguments. This constructor
 * is required for Jackson.
 * <p>
 * Also note the Bean Validation annotations:
 * <table>
 * <tr>
 * <th>Annotation</th>
 * <th>Meaning</th>
 * </tr>
 * <tr>
 * <td>@NotNull</td>
 * <td>A value must be present in the JSON.</td>
 * </tr>
 * <tr>
 * <td>@Positive</td>
 * <td>The value in the JSON must not be negative.</td>
 * </tr>
 * <tr>
 * <td>@NotBlank</td>
 * <td>The value must not be null or all blank (all spaces).</td>
 * </tr>
 * <tr>
 * <td>@Length</td>
 * <td>Specify the minimum and maximum length of a String.</td>
 * </tr>
 * <tr>
 * <td>@Pattern</td>
 * <td>A Regular Expression that specifies a valid String.</td>
 * </tr>
 * </table>
 * The value in the JSON also must match the Java data type. For example, an exception is thrown if
 * "abc" is present and the Java object expects and Integer.
 * 
 * @author Promineo
 *
 */
@Data
@NoArgsConstructor
public class Breed {
  @NotNull
  @Positive
  private Integer breedId;

  @NotBlank
  @Length(min = 2, max = 64)
  @Pattern(regexp = "[\\w ]+")
  private String breedName;

  @NotBlank
  @Length(min = 2, max = 4096)
  @Pattern(regexp = "[\\w\\s.,!\"'$%@#^&*()?]+")
  private String description;

  /*
   * The constructor below creates the lisst (or Jackson will create the lists after using the
   * zero-argument constructor). Validation for each list element is contained within the Generic.
   * So, in this case, the Generic specifies a String that must not be null or blank, with a minimum
   * length of 2 and a maximum length of 32 matching the Regular Expression that accepts a
   * combination of word characters (a to z, A to Z and underscore) as well as space and dash
   * characters.
   */
  private List<@NotBlank @Length(min = 2,
      max = 32) @Pattern(regexp = "[\\w- ]+") String> categoryNames;

  private List<@NotBlank @Length(min = 2,
      max = 64) @Pattern(regexp = "[\\w- ]+") String> alternameNames;

  /**
   * This constructor is annotated with @Builder so that Lombok will only provide builder methods
   * for breed ID, breed name and description. The lists are populated using the getters (i.e.,
   * breed.getCategoryNames().add("smooth")).
   *
   * @param breedId The breed ID
   * @param breedName The breed name
   * @param description The breed description.
   */
  @Builder
  public Breed(Integer breedId, String breedName, String description) {
    this.breedId = breedId;
    this.breedName = breedName;
    this.description = description;
    this.categoryNames = new LinkedList<>();
    this.alternameNames = new LinkedList<>();
  }
}
