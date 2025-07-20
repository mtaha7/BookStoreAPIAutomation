# Bookstore API Test Framework

API testing framework using Java, RestAssured, TestNG, and Extent Report, integrated with Jenkins and Docker.
---

## Tech Stack

- **Java**
- **TestNG**
- **RestAssured**
- **Maven**
- **Extent Reporting**
- **Docker**
- **Jenkins**

---

## Running tests locally and generating Extent report

1. Being in test-suites dir (src/test/resources/test-suites)

## Running tests using Jenkins and Docker

### Ensure Docker engine is installed

1. Bringing Jenkins Server up using docker-compose
2. running jenkins job with the present Jenkins File to:
   - Get all the needed Jars
   - Building the app Image
   - Pushing the image to DockerHub
   - Running all the tests
   - Archiving all the artifacts (reports) after executing the job

---

## Features

1. Isolated test classes for each API endpoint
2. Happy path and edge cases scenarios
3. Logs and error handling per test