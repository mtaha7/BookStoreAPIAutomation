package net.fakerestapi;

import io.restassured.response.Response;
import net.fakerestapi.constants.GeneralConstants;
import net.fakerestapi.dataModels.AuthorDM;
import net.fakerestapi.dataModels.BookDM;
import net.fakerestapi.endpoints.Authors;
import net.fakerestapi.endpoints.Books;
import net.fakerestapi.util.JsonUtil;
import net.fakerestapi.utils.Log;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static org.testng.Assert.*;

public class BooksTests extends BaseTest {

    int validIntegerBookId = 10;
    int invalidIntegerBookId = -1;
    String stringBookId = "test";

    @Test
    public void getAllBooks() {

        test = extent.createTest("Retrieve All Books");
        Log.test = test;
        Log.startTestCase("Retrieve All Books");

        Response response = Books.getAllBooks();

        Log.info("Response body: " + response.asPrettyString());

        assertEquals(200, response.getStatusCode());

        List<Map<String, Object>> Books = response.jsonPath().getList("$");
        assertFalse(Books.isEmpty(), "Books list should not be empty");

        Map<String, Object> Book = Books.getFirst();

        assertNotNull(Book.get("id"), "Book ID should not be null");

        assertNotNull(Book.get("title"), "Book Title should not be null");

        assertNotNull(Book.get("description"), "Book Description should not be null");

        assertNotNull(Book.get("pageCount"), "Book Page Count should not be null");
    }

    @Test
    public void getBookWithValidIntegerId() {

        test = extent.createTest("Retrieve Certain Book By using valid ID");
        Log.test = test;
        Log.startTestCase("Retrieve Certain Book By using valid ID");

        Response response = Books.getBookByIntegerId(validIntegerBookId);

        Log.info("Response body: " + response.asPrettyString());

        assertEquals(validIntegerBookId, response.jsonPath().getInt("id"));
    }

    @Test
    public void getBookWithInvalidIntegerId() {

        test = extent.createTest("Retrieve Certain Book By using Invalid ID");
        Log.test = test;
        Log.startTestCase("Retrieve Certain Book By using Invalid ID");

        Response response = Books.getBookByIntegerId(invalidIntegerBookId);

        Log.info("Response body: " + response.asPrettyString());

        assertEquals(404, response.getStatusCode(), "Expected 404 for Invalid Book ID");

        String body = response.getBody().asString();
        assertTrue(body.contains("Not Found"),
                "Expected error message to contain 'Not found'");
    }

    @Test
    public void getBookWithStringId() {

        test = extent.createTest("Retrieve Certain Book By using String ID");
        Log.test = test;
        Log.startTestCase("Retrieve Certain Book By using String ID");

        Response response = Books.getBookByStringId(stringBookId);

        Log.info("Response body: " + response.asPrettyString());

        assertEquals(400, response.getStatusCode(), "Expected status code 400 for String ID format");

        String errorMessage = response.jsonPath().getList("errors.id").getFirst().toString();
        assertTrue(errorMessage.contains("not valid"), "Expected validation error message for invalid ID format");
    }

    @Test
    public void createNewBook() {

        test = extent.createTest("Creating New Book");
        Log.test = test;
        Log.startTestCase("Creating New Book");

        BookDM bookDM = JsonUtil.getTestData(GeneralConstants.VALID_BOOKS_TEST_DATA_FILEPATH, BookDM.class);
        Response response = Books.createBook(bookDM);

        Log.info("Response: " + response.asPrettyString());

        assertEquals(200, response.getStatusCode(), "Expected status code 200 for created book");
        assertEquals(bookDM.getId(), response.jsonPath().getInt("id"), "Returned Book ID should be " + bookDM.getId());
    }

    @Test
    public void createNewBookWithInvalidData() {

        test = extent.createTest("Creating New Book With Invalid Data");
        Log.test = test;
        Log.startTestCase("Creating New Book With Invalid Data");

        BookDM bookDM = JsonUtil.getTestData(GeneralConstants.INVALID_BOOKS_TEST_DATA_FILEPATH, BookDM.class);
        Response response = Books.createBook(bookDM);

        Log.info("Response: " + response.asPrettyString());

        assertEquals(400, response.getStatusCode(),
                "Expected status code 400 for invalid request body");
    }

    @Test
    public void updateBookWithValidData() {

        test = extent.createTest("Update Existing Book With Valid Data");
        Log.test = test;
        Log.startTestCase("Update Existing Book With Valid Data");

        BookDM bookDM = JsonUtil.getTestData(GeneralConstants.VALID_BOOKS_TEST_DATA_FILEPATH, BookDM.class);
        bookDM.setTitle("Modified Title");
        bookDM.setDescription("Modified Description");

        Response response = Books.updateBook(bookDM);

        Log.info("Response: " + response.asPrettyString());

        assertEquals(200, response.getStatusCode(), "Expected status code 200 after successful update");

        assertEquals("Modified Title", response.jsonPath().getString("title"), "title in response should match updated value");
        assertEquals("Modified Description", response.jsonPath().getString("description"), "description in response should match updated value");
    }

    @Test
    public void updateBookWithInvalidData() {

        test = extent.createTest("Update Existing Book With Invalid Data");
        Log.test = test;
        Log.startTestCase("Update Existing Book With Invalid Data");

        BookDM bookDM = JsonUtil.getTestData(GeneralConstants.VALID_BOOKS_TEST_DATA_FILEPATH, BookDM.class);
        bookDM.setId(invalidIntegerBookId);

        Response response = Books.updateBook(bookDM);

        Log.info("Response body: " + response.asPrettyString());

        assertEquals(404, response.getStatusCode(), "Expected 404 for updating a non-existent Book");

        String body = response.getBody().asString();
        assertTrue(body.contains("Not Found"),
                "Expected error message to contain 'Not found'");
    }

    @Test
    public void updateBookWithEmptyData() {

        test = extent.createTest("Update Existing Book With Empty Data");
        Log.test = test;
        Log.startTestCase("Update Existing Book With Empty Data");

        BookDM bookDM = JsonUtil.getTestData(GeneralConstants.VALID_BOOKS_TEST_DATA_FILEPATH, BookDM.class);
        bookDM.setTitle("");

        Response response = Books.updateBook(bookDM);

        Log.info("Response body: " + response.asPrettyString());

        assertEquals(400, response.getStatusCode(), "Expected status code 400 for invalid request body");

        String body = response.getBody().asString();
        assertTrue(body.contains("title"), "Invalid request, 'title' is missing");
    }

    @Test
    public void deleteBookWithValidData() {

        test = extent.createTest("Delete Existing Book With Valid Data");
        Log.test = test;
        Log.startTestCase("Delete Existing Book With Valid Data");

        BookDM bookDM = JsonUtil.getTestData(GeneralConstants.VALID_BOOKS_TEST_DATA_FILEPATH, BookDM.class);
        bookDM.setId(validIntegerBookId);

        Response response = Books.deleteBook(bookDM);

        Log.info("Response: " + response.asPrettyString());

        assertEquals(200, response.getStatusCode(), "Expected status code 200 after successful delete");

        Response getResponse = Books.getBookByIntegerId(validIntegerBookId);

        Log.info("Response: " + getResponse.asPrettyString());

        assertEquals(404, getResponse.getStatusCode(), "Expected 404 for deleted Book");
    }

    @Test
    public void deleteBookWithInvalidData() {

        test = extent.createTest("Delete Existing Book With Invalid Data");
        Log.test = test;
        Log.startTestCase("Delete Existing Book With Invalid Data");

        BookDM bookDM = JsonUtil.getTestData(GeneralConstants.VALID_BOOKS_TEST_DATA_FILEPATH, BookDM.class);
        bookDM.setId(invalidIntegerBookId);

        Response response = Books.deleteBook(bookDM);

        Log.info("Response: " + response.asPrettyString());

        assertEquals(404, response.getStatusCode(), "Expected 404 for deletion of non-existent Book");

        String body = response.getBody().asString();
        assertTrue(body.contains("Not Found"),
                "Expected error message to contain 'Not found'");
    }

    @Test
    public void deleteBookWithStringId() {

        test = extent.createTest("Delete Existing Book With String ID");
        Log.test = test;
        Log.startTestCase("Delete Existing Book With String ID");

        Response response = Books.deleteBook(stringBookId);

        Log.info("Response: " + response.asPrettyString());

        assertEquals(400, response.getStatusCode(), "Expected 400 for invalid ID format");

        String body = response.getBody().asString();
        assertTrue(body.contains("One or more validation errors occurred."),
                "Expected validation error message for invalid ID format");
    }
}