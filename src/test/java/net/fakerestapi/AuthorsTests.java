package net.fakerestapi;

import io.restassured.response.Response;
import net.fakerestapi.constants.GeneralConstants;
import net.fakerestapi.dataModels.AuthorDM;
import net.fakerestapi.endpoints.Authors;
import net.fakerestapi.util.JsonUtil;
import net.fakerestapi.utils.Log;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;

public class AuthorsTests extends BaseTest {

    int validIntegerAuthorId = 10;
    int invalidIntegerAuthorId = -1;
    String stringAuthorId = "test";

    @Test
    public void getAllAuthors() {

        test = extent.createTest("Retrieve All Authors");
        Log.test = test;
        Log.startTestCase("Retrieve All Authors");

        Response response = Authors.getAllAuthors();

        Log.info("Response status: " + response.getStatusCode());
        Log.info("Response body: " + response.asPrettyString());

        assertEquals(200, response.getStatusCode());

        List<Map<String, Object>> Authors = response.jsonPath().getList("$");
        assertFalse(Authors.isEmpty(), "Authors list should not be empty");

        Map<String, Object> Author = Authors.getFirst();

        assertNotNull(Author.get("id"), "Author ID should not be null");

        assertNotNull(Author.get("idBook"), "Author idBook should not be null");

        assertNotNull(Author.get("firstName"), "Author firstName should not be null");

        assertNotNull(Author.get("lastName"), "Author lastName should not be null");
    }

    @Test
    public void getAuthorWithValidIntegerId() {

        test = extent.createTest("Retrieve Certain Author By using valid ID");
        Log.test = test;
        Log.startTestCase("Retrieve Certain Author By using valid ID");

        Response response = Authors.getAuthorById(validIntegerAuthorId);

        Log.info("Response body: " + response.asPrettyString());

        assertEquals(validIntegerAuthorId, response.jsonPath().getInt("id"));
    }

    @Test
    public void getAuthorWithInvalidIntegerId() {

        test = extent.createTest("Retrieve Certain Author By using Invalid ID");
        Log.test = test;
        Log.startTestCase("Retrieve Certain Author By using Invalid ID");

        Response response = Authors.getAuthorById(invalidIntegerAuthorId);

        Log.info("Response body: " + response.asPrettyString());

        assertEquals(404, response.getStatusCode(), "Expected 404 for Invalid Author ID");

        String body = response.getBody().asString();
        assertTrue(body.contains("Not Found"),
                "Expected error message to contain 'Not found'");
    }

    @Test
    public void getAuthorWithStringId() {

        test = extent.createTest("Retrieve Certain Author By using String ID");
        Log.test = test;
        Log.startTestCase("Retrieve Certain Author By using String ID");

        Response response = Authors.getAuthorById(stringAuthorId);

        Log.info("Response body: " + response.asPrettyString());

        assertEquals(400, response.getStatusCode(), "Expected status code 400 for String ID format");

        String errorMessage = response.jsonPath().getList("errors.id").getFirst().toString();
        assertTrue(errorMessage.contains("not valid"), "Expected validation error message for invalid ID format");
    }

    @Test
    public void createNewAuthor() {

        test = extent.createTest("Creating New Author");
        Log.test = test;
        Log.startTestCase("Creating New Author");

        AuthorDM authorDM = JsonUtil.getTestData(GeneralConstants.VALID_AUTHORS_TEST_DATA_FILEPATH, AuthorDM.class);
        Response response = Authors.createAuthor(authorDM);

        Log.info("Response: " + response.asPrettyString());

        assertEquals(200, response.getStatusCode(), "Expected status code 200 for created author");
        assertEquals(authorDM.getId(), response.jsonPath().getInt("id"), "Returned author ID should be " + authorDM.getId());
    }

    @Test
    public void createNewAuthorWithInvalidData() {

        test = extent.createTest("Creating New Author With Invalid Data");
        Log.test = test;
        Log.startTestCase("Creating New Author With Invalid Data");

        AuthorDM authorDM = JsonUtil.getTestData(GeneralConstants.INVALID_AUTHORS_TEST_DATA_FILEPATH, AuthorDM.class);
        Response response = Authors.createAuthor(authorDM);

        Log.info("Response: " + response.asPrettyString());

        assertEquals(400, response.getStatusCode(),
                "Expected status code 400 for invalid request body");
    }

    @Test
    public void updateAuthorWithValidData() {

        test = extent.createTest("Update Existing Author With Valid Data");
        Log.test = test;
        Log.startTestCase("Update Existing Author With Valid Data");

        AuthorDM authorDM = JsonUtil.getTestData(GeneralConstants.VALID_AUTHORS_TEST_DATA_FILEPATH, AuthorDM.class);
        authorDM.setFirstName("Modified First Name");
        authorDM.setLastName("Modified Last Name");

        Response response = Authors.updateAuthor(authorDM);

        Log.info("Response: " + response.asPrettyString());

        assertEquals(200, response.getStatusCode(), "Expected status code 200 after successful update");

        assertEquals("Modified First Name", response.jsonPath().getString("firstName"), "lastName in response should match updated value");
        assertEquals("Modified Last Name", response.jsonPath().getString("lastName"), "lastName in response should match updated value");
    }

    @Test
    public void updateAuthorWithInvalidData() {

        test = extent.createTest("Update Existing Author With Invalid Data");
        Log.test = test;
        Log.startTestCase("Update Existing Author With Invalid Data");

        AuthorDM authorDM = JsonUtil.getTestData(GeneralConstants.VALID_AUTHORS_TEST_DATA_FILEPATH, AuthorDM.class);
        authorDM.setId(invalidIntegerAuthorId);

        Response response = Authors.updateAuthor(authorDM);

        Log.info("Response body: " + response.asPrettyString());

        assertEquals(404, response.getStatusCode(), "Expected 404 for updating a non-existent Author");

        String body = response.getBody().asString();
        assertTrue(body.contains("Not Found"),
                "Expected error message to contain 'Not found'");
    }

    @Test
    public void updateAuthorWithEmptyData() {

        test = extent.createTest("Update Existing Author With Empty Data");
        Log.test = test;
        Log.startTestCase("Update Existing Author With Empty Data");

        AuthorDM authorDM = JsonUtil.getTestData(GeneralConstants.VALID_AUTHORS_TEST_DATA_FILEPATH, AuthorDM.class);
        authorDM.setFirstName("");

        Response response = Authors.updateAuthor(authorDM);

        Log.info("Response body: " + response.asPrettyString());

        assertEquals(400, response.getStatusCode(), "Expected status code 400 for invalid request body");

        String body = response.getBody().asString();
        assertTrue(body.contains("firstName"), "Invalid request, 'firstName' is missing");
    }

    @Test
    public void deleteAuthorWithValidData() {

        test = extent.createTest("Delete Existing Author With Valid Data");
        Log.test = test;
        Log.startTestCase("Delete Existing Author With Valid Data");

        AuthorDM authorDM = JsonUtil.getTestData(GeneralConstants.VALID_AUTHORS_TEST_DATA_FILEPATH, AuthorDM.class);
        authorDM.setId(validIntegerAuthorId);

        Response response = Authors.deleteAuthor(authorDM);

        Log.info("Response: " + response.asPrettyString());

        assertEquals(200, response.getStatusCode(), "Expected status code 200 after successful delete");

        Response getResponse = Authors.getAuthorById(validIntegerAuthorId);

        Log.info("Response: " + getResponse.asPrettyString());

        assertEquals(404, getResponse.getStatusCode(), "Expected 404 for deleted Author");
    }

    @Test
    public void deleteAuthorWithInvalidData() {

        test = extent.createTest("Delete Existing Author With Invalid Data");
        Log.test = test;
        Log.startTestCase("Delete Existing Author With Invalid Data");

        AuthorDM authorDM = JsonUtil.getTestData(GeneralConstants.VALID_AUTHORS_TEST_DATA_FILEPATH, AuthorDM.class);
        authorDM.setId(invalidIntegerAuthorId);

        Response response = Authors.deleteAuthor(authorDM);

        Log.info("Response: " + response.asPrettyString());

        assertEquals(404, response.getStatusCode(), "Expected 404 for deletion of non-existent Author");

        String body = response.getBody().asString();
        assertTrue(body.contains("Not Found"),
                "Expected error message to contain 'Not found'");
    }

    @Test
    public void deleteAuthorWithStringId() {

        test = extent.createTest("Delete Existing Author With String ID");
        Log.test = test;
        Log.startTestCase("Delete Existing Author With String ID");

        Response response = Authors.deleteAuthor(stringAuthorId);

        Log.info("Response: " + response.asPrettyString());

        assertEquals(400, response.getStatusCode(), "Expected 400 for invalid ID format");

        String body = response.getBody().asString();
        assertTrue(body.contains("One or more validation errors occurred."),
                "Expected validation error message for invalid ID format");
    }
}