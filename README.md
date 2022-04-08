# Bunny Blast project

This project can be used as a starting point to develop a Spring Boot JDBC application that connects to a MySQL database.

## How to proceed

1. Make a copy of the bunny blast project so that you can refer to the comments and documentation in each class while modifying a copy to suit your project needs.
1. Familiarize yourself with the bunny blast project. Read through the comments in each class to see what they do and why they do it. You will want to do the same basic things on your project as well.
1. Decide on a project that you want to implement.
1. Document your tables in a way that makes sense to you. See src/main/resources/bunny-blast.drawio for an example.
1. Create the schema in the database using MySQL Workbench with a custom username and password. Set the schema name, username and password in src/main/resources/application.yaml to match.
1. Take a look at pom.xml. You will probably not need to change the dependencies but you may want to change the Spring Boot version in the &lt;parent&gt; entity.
1. Change the base package name to match your project. Right-click on the bunny package and select "refactor/rename". Make sure "rename subpackages" is checked.
1. Modify the entities in the the entity subpackage to match your tables.
1. Modify the base URI in the @RequestMapping annotation in controller.BunnyOperations.java to match your project. Change the method names and OpenAPI documentation to be correct for your project. If you use the Eclipse "refactor/rename" operation it will change the method names in the controller implementation as well.
1. Change the controller, service and dao class names to match your application.
1. Change the dao class methods to do what you need it to.
1. Have fun and enjoy!

## Testing this project

Before you make any modifications to this project, you can create a schema in MySQL Workbench. The schema should have the name "bunnies" with username and password set to "bunnies". You can import data into the bunny tables by copying the contents of src/main/resources/bunny-schema.sql and pasting it into a DBeaver SQL editor. 

To do this, create a connection in DBeaver and set it to read/write from the bunnies schema. Open a SQL editor by right-clicking on the schema name in DBeaver's Database Navigator. Then select "SQL Editor" / "Recent SQL script". Once the editor is open, paste the contents of bunny-schema.sql. Press Ctrl+A (Windows) or Cmd+A (MacOS) to select everything in the editor. Then press Alt+X (Windows) or Opt+X (MacOS) to execute all the SQL statements. This will create the tables and populate them with sample data.

## Starting from scratch

You can start from scratch if you want. Create a Maven project. This will create a very minimal pom.xml. Copy the &lt;parent&gt;, &lt;properties&gt;, &lt;dependencies&gt;, and &lt;build&gt; sections from the pom.xml in this project into the pom.xml in your own project. This should be enough to get you started.

## Running the project

Run the project live by running bunny.BunnyApp as a Java or Spring Boot application. All the operations work using the /bunny URI. This is documented in bunny.controller.BunnyOperations.java.

From the running application, you can test it using the OpenAPI documentation. Navigate a browser to http://localhost:8080/swagger-ui.html.
