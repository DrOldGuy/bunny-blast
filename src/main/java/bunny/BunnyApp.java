package bunny;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Run this class as a Java application (or a Spring Boot application) to start Spring Boot. Then
 * use a browser to navigate to http://localhost:8080/bunny to list all the bunny breeds.
 * <p>
 * 
 * @SpringBootApplication with no attributes will cause a component scan to be generated starting
 *                        with the current package and all subpackages.
 * 
 * @author Promineo
 *
 */
@SpringBootApplication
public class BunnyApp {

  /**
   * The main() method simply starts Spring Boot. Spring Boot starts an embedded Tomcat Web server,
   * which calls Spring's Dispatcher Servlet to route HTTP requests to the application controller
   * methods.
   * 
   * @param args
   */
  public static void main(String[] args) {
    SpringApplication.run(BunnyApp.class, args);
  }
}
