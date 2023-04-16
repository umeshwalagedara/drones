Overview
This is a Spring Boot project that provides a RESTful API for managing drones and medications. The API includes endpoints for registering drones, loading medications onto drones, checking battery levels, and getting a list of available drones.

Technologies Used
Java 16
Spring Boot 3.0.5
Maven 4.0
JUnit 4
H2 in memory DB

Building the Project
To build the project, follow these steps:

Clone the repository from the remote location
Open a terminal/command prompt window and navigate to the project root directory
Run the following command to build the project:
Copy code
mvn clean install
This will compile the code, run the tests, and create a JAR file in the target directory.

Running the Project
To run the project, follow these steps:

Open a terminal/command prompt window and navigate to the project's  target directory
Run the following command:

java -jar <project-name>.jar
Replace <project-name> with the actual name of your project. ( java -jar Drones-0.0.1-SNAPSHOT.jar)

This will start the Spring Boot application and make it available at http://localhost:8080.

Testing the Project
To run the tests for the project, follow these steps:

Open a terminal/command prompt window and navigate to the project root directory
Run the following command:
mvn test
This will run all the unit tests in the project and produce a report on the results.

API Documentation
The following endpoints are available in the API:

POST /drones - Register a new drone
POST /drones/{droneId}/medications - Load medications onto a drone
GET /drones/{droneId}/battery - Get the battery level of a drone
GET /drones/available - Get a list of available drones
GET /drones/{droneId}/medications - Get a list of medications loaded onto a drone