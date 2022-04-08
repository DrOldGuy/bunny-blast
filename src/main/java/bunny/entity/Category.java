/**
 * 
 */
package bunny.entity;

import lombok.Builder;
import lombok.Value;

/**
 * This class is a Data Transfer Object (DTO) that stores data that matches a category row. It is
 * not used with validation so there are no Bean Validation annotations. The Lombok @Value
 * annotation provides getters, hashCode(), equals() and toString() methods. There are no setters so
 * this class is immutable. The Lombok annotation @Buider provides an all-argument constructor and
 * an inner builder class that implements the Builder Design Pattern. To create a Category object
 * you would do something like this:
 * <pre>
 * Category category = Category.builder()
 *      .categoryId(5)
 *      .categoryName("wooly")
 *      .build();
 * </pre>
 * 
 * @author Promineo
 *
 */
@Value
@Builder
public class Category {
  private int categoryId;
  private String categoryName;
}
