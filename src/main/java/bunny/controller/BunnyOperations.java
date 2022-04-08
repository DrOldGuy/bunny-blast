/**
 * 
 */
package bunny.controller;

import java.util.List;
import javax.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import bunny.entity.AddBreedRequest;
import bunny.entity.Breed;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.servers.Server;

/**
 * This interface declares the methods that are implemented in the {@link BunnyController}. This
 * serves the following purposes: 1) All OpenAPI documentation is declared in the interface. This
 * allows the controller to have much cleaner code. 2) HTTP verb types are mapped to methods (GET,
 * PUT, etc.). 3) Bean validation is enabled. 4) Default result status codes are declared here,
 * meaning that they don't need to be declared in the implementing class.
 * <p>
 * All URIs supported by the class methods are mapped to the /bunny URI. Some methods take URL
 * parameters and some do not.
 * 
 * @author Promineo
 *
 */
@Validated // Turn on Bean Validation
@RequestMapping("/bunny") // Maps to http://localhost:8080/bunny
@OpenAPIDefinition(info = @Info(title = "Bunny Operations"),
    servers = {@Server(url = "http://localhost:8080", description = "Local server.")})
public interface BunnyOperations {

  /**
   * This method is routed to when the GET method at http://localhost:8080/bunny is invoked. It
   * returns a list of all bunny breeds, along with categories and alternate breed names.
   * 
   * @return The list of breeds.
   */
  @Operation( // @formatter:off
      summary = "List all bunny breeds",
      description = "List all the bunny breeds with alternate names if they exist",
      responses = {
          @ApiResponse(responseCode = "200", description = "Returns a list of all bunny breeds", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Breed.class))),
          @ApiResponse(responseCode = "500", description = "An unplanned error occurred", content = @Content(mediaType = "application/json"))
      }
  ) // @formatter:on
  @GetMapping
  @ResponseStatus(code = HttpStatus.OK)
  List<Breed> listBunnyBreeds();

  /**
   * This method returns a specific bunny breed when the GET method at
   * http://localhost:8080/bunny/{breedId} is invoked (i.e., http://localhost:8080/bunny/29).
   * 
   * @param breedId
   * @return
   */
  @Operation( // @formatter:off
      summary = "Return a specified bunny breed",
      description = "Return a specified bunny breed with category and alternate names",
      responses = {
          @ApiResponse(responseCode = "200", description = "Returns a bunny breed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Breed.class))),
          @ApiResponse(responseCode = "400", description = "Invalid breed ID", content = @Content(mediaType = "application/json")),
          @ApiResponse(responseCode = "404", description = "Breed not found", content = @Content(mediaType = "application/json")),
          @ApiResponse(responseCode = "500", description = "An unplanned error occurred", content = @Content(mediaType = "application/json"))
      },
      parameters = {
          @Parameter(
              name = "breedId", 
              allowEmptyValue = false, 
              required = true, 
              description = "The breed ID of the breed to return.", 
              in = ParameterIn.PATH
          )
      }  
  ) // @formatter:on
  @GetMapping("/{breedId}")
  @ResponseStatus(code = HttpStatus.OK)
  Breed getBreed(@PathVariable int breedId);

  /**
   * This method adds a new bunny breed when the HTTP POST verb is invoked at
   * http://localhost:8080/bunny. The input object is of type {@link AddBreedRequest}. It is passed
   * in the request body as a JSON object.
   * 
   * @param breedRequest The {@link AddBreedRequest} object. Jackson must be able to convert the
   *        JSON object to an {@link AddBreedRequest} object or a validation exception is thrown.
   * @return The added breed.
   */
  @Operation( // @formatter:off
      summary = "Add a new bunny breed",
      description = "Add a new bunny breed and return the breed with breed ID",
      responses = {
          @ApiResponse(responseCode = "201", description = "Add a bunny breed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Breed.class))),
          @ApiResponse(responseCode = "400", description = "Invalid breed data", content = @Content(mediaType = "application/json")),
          @ApiResponse(responseCode = "409", description = "Duplicate breed", content = @Content(mediaType = "application/json")),
          @ApiResponse(responseCode = "500", description = "An unplanned error occurred", content = @Content(mediaType = "application/json"))
      }
  ) // @formatter:on
  @PostMapping
  @ResponseStatus(code = HttpStatus.CREATED)
  Breed addBreed(@Valid @RequestBody AddBreedRequest breedRequest);

  /**
   * This method modifies an existing bunny breed including categories and alternate names. It is
   * invoked when a PUT verb is sent to http://localhost:8080/bunny. The input must be in the
   * request body as a JSON object. Jackson must be able to properly convert the JSON object to a
   * {@link Breed} object.
   * 
   * @param breedRequest The input object as described in the method overview. It must contain the
   *        breed ID.
   * @return The modified breed object.
   */
  @Operation( // @formatter:off
      summary = "Modify an existing bunny breed",
      description = "Modify an existing bunny breed and return the breed with breed ID",
      responses = {
          @ApiResponse(responseCode = "200", description = "Modify a bunny breed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Breed.class))),
          @ApiResponse(responseCode = "400", description = "Invalid breed data", content = @Content(mediaType = "application/json")),
          @ApiResponse(responseCode = "404", description = "Breed not found", content = @Content(mediaType = "application/json")),
          @ApiResponse(responseCode = "500", description = "An unplanned error occurred", content = @Content(mediaType = "application/json"))
      }
  ) // @formatter:on
  @PutMapping
  @ResponseStatus(code = HttpStatus.OK)
  Breed modifyBreed(@Valid @RequestBody Breed breedRequest);

  /**
   * This method deletes a bunny breed when a DELETE verb is invoked at
   * http://localhost:8080/bunny/{breedId} (i.e., http://localhost:8080/bunny/5).
   * 
   * @param breedId The breed ID of the bunny to delete.
   */
  @Operation( // @formatter:off
      summary = "Delete an existing bunny breed",
      description = "Delete an existing bunny breed",
      responses = {
          @ApiResponse(responseCode = "200", description = "Delete a bunny breed", content = @Content(mediaType = "application/json")),
          @ApiResponse(responseCode = "400", description = "Invalid breed ID", content = @Content(mediaType = "application/json")),
          @ApiResponse(responseCode = "404", description = "Breed not found", content = @Content(mediaType = "application/json")),
          @ApiResponse(responseCode = "500", description = "An unplanned error occurred", content = @Content(mediaType = "application/json"))
      },
      parameters = {
          @Parameter(
              name = "breedId", 
              allowEmptyValue = false, 
              required = true, 
              description = "The breed ID of the breed to delete.", 
              in = ParameterIn.PATH
          )
      }
  ) // @formatter:on
  @DeleteMapping("/{breedId}")
  @ResponseStatus(code = HttpStatus.OK)
  void deleteBreed(@PathVariable int breedId);
}
