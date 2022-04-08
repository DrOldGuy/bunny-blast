/**
 * 
 */
package bunny.controller;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import bunny.entity.AddBreedRequest;
import bunny.entity.Breed;
import bunny.service.BunnyService;
import lombok.extern.slf4j.Slf4j;

/**
 * This class implements the {@link BunnyOperations} interface. Note the very simple code without
 * the OpenAPI documentation. Error handling is hidden away in the {@link ErrorHandler} class.
 * 
 * @author Promineo
 *
 */
@RestController
@Slf4j
public class BunnyController implements BunnyOperations {

  /*
   * Using @Autowired tells Spring to inject the singleton RenameMeService object. RenameMeService
   * is a class and not an interface. Spring can handle both but there's no reason to use an
   * interface if there is only one implementing class and if there is no messy code (like OpenAPI)
   * to mess up the code.
   */
  @Autowired
  private BunnyService service;

  /**
   * @see BunnyOperations#listBunnyBreeds()
   * @see BunnyService#listBunnyBreeds()
   */
  @Override
  public List<Breed> listBunnyBreeds() {
    log.info("Controller: List bunny breeds");
    return service.listBunnyBreeds();
  }

  /**
   * A {@link NoSuchElementException} is thrown by the {@link BunnyService service} if the breed ID
   * is invalid.
   * 
   * @see BunnyOperations#getBreed(int)
   * @see BunnyService#getBunnyBreed(int)
   */
  @Override
  public Breed getBreed(int breedId) {
    log.info("Controller: Get bunny with ID={}", breedId);
    return service.getBunnyBreed(breedId);
  }

  /**
   *
   * @see BunnyOperations#addBreed(AddBreedRequest)
   * @see BunnyService#addBunny(AddBreedRequest)
   */
  @Override
  public Breed addBreed(AddBreedRequest breedRequest) {
    log.info("Controller: Adding bunny {}", breedRequest);
    return service.addBunny(breedRequest);
  }

  /**
   * A {@link NoSuchElementException} is thrown by the {@link BunnyService service} if the breed ID
   * is invalid.
   * 
   * @see BunnyOperations#modifyBreed(Breed)
   * @see BunnyService#modifyBunny(Breed)
   */
  @Override
  public Breed modifyBreed(Breed breedRequest) {
    log.info("Controller: Modify bunny {}", breedRequest);
    return service.modifyBunny(breedRequest);
  }

  /**
   * A {@link NoSuchElementException} is thrown by the {@link BunnyService service} if the breed ID
   * is invalid.
   * 
   * @see BunnyOperations#deleteBreed(int)
   * @see BunnyService#deleteBunny(int)
   */
  @Override
  public void deleteBreed(int breedId) {
    log.info("Controller: Delete bunny with ID={}", breedId);
    service.deleteBunny(breedId);
  }

}
