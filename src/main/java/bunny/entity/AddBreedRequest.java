/**
 * 
 */
package bunny.entity;

import java.util.LinkedList;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.Length;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This class is used to accept data for the add bunny breed operation. It is the same as the Breed
 * class but there is no breed ID. (Breed ID is generated automatically by MySQL when the bunny
 * breed is added.) It would seem expedient to have the Breed class extend this class and simply
 * supply the breed ID. Bean Validation works fine in this case but Lombok builders do not. So, all
 * the Bean Validation annotations need to be duplicated. See the {@link Breed} class for their use.
 * 
 * @author Promineo
 *
 */
@Data
@NoArgsConstructor
public class AddBreedRequest {
  @NotBlank
  @Length(min = 2, max = 64)
  @Pattern(regexp = "[\\w ]+")
  protected String breedName;

  @NotBlank
  @Length(min = 2, max = 4096)
  @Pattern(regexp = "[\\w\\s.,!\"'$%@#^&*()?]+")
  protected String description;

  /*
   * The constructor below creates the lisst (or Jackson will create the lists after using the
   * zero-argument constructor). Validation for each list element is contained within the Generic.
   * So, in this case, the Generic specifies a String that must not be null or blank, with a minimum
   * length of 2 and a maximum length of 32 matching the Regular Expression that accepts a
   * combination of word characters (a to z, A to Z and underscore) as well as space and dash
   * characters.
   */
  protected List<@NotBlank @Length(min = 2,
      max = 32) @Pattern(regexp = "[\\w- ]+") String> categoryNames;

  protected List<@NotBlank @Length(min = 2,
      max = 64) @Pattern(regexp = "[\\w- ]+") String> alternameNames;

  /**
   * This constructor is annotated with @Builder so that Lombok will only provide builder methods
   * for breed name and description. The lists are populated using the getters (i.e.,
   * addBreedRequest.getCategoryNames().add("smooth")).
   * 
   * @param breedName The breed name
   * @param description The breed description
   */
  @Builder
  public AddBreedRequest(String breedName, String description) {
    this.breedName = breedName;
    this.description = description;
    this.categoryNames = new LinkedList<>();
    this.alternameNames = new LinkedList<>();
  }
}
