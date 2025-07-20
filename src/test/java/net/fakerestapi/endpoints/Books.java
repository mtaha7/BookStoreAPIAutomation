package net.fakerestapi.endpoints;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.fakerestapi.constants.GeneralConstants;
import net.fakerestapi.dataModels.AuthorDM;
import net.fakerestapi.dataModels.BookDM;
import net.fakerestapi.utils.Log;
import net.fakerestapi.utils.PropertiesFilesHandler;

import java.util.Properties;

import static io.restassured.RestAssured.given;

public class Books {
    static PropertiesFilesHandler propHandler = new PropertiesFilesHandler();
    static Properties generalConfigsProps = propHandler.loadPropertiesFile(GeneralConstants.GENERAL_CONFIG_FILE_NAME);
    public static String endpoint = generalConfigsProps.getProperty(GeneralConstants.BOOKS_ENDPOINT);

    public static Response getAllBooks() {
        
        Log.info("Getting All Books");

        return given()
                .accept(ContentType.JSON)
                .when()
                .get(endpoint)
                .then()
                .extract().response();
    }

    public static Response getBookByIntegerId(int id) {

        Log.info("Getting book by ID: " + id);

        String url = endpoint + "/" + id;

        return given()
                .accept(ContentType.JSON)
                .when()
                .get(url)
                .then()
                .extract().response();
    }

    public static Response getBookByStringId(String id) {
        
        Log.info("Getting book by ID (string): " + id);

        String url = endpoint + "/" + id;

        return given()
                .accept(ContentType.JSON)
                .when()
                .get(url)
                .then()
                .extract().response();
    }


    public static Response createBook(BookDM bookDM) {
        
        Log.info("Creating book: " + bookDM.getTitle());

        return given()
                .contentType(ContentType.JSON)
                .body(bookDM)
                .when()
                .post(endpoint)
                .then()
                .extract().response();
    }

    public static Response updateBook(BookDM bookDM) {
        
        Log.info("Updating book with ID: " + bookDM.getId());

        return given()
                .contentType(ContentType.JSON)
                .body(bookDM)
                .when()
                .put(endpoint + "/" + bookDM.getId())
                .then()
                .extract().response();
    }

    public static Response deleteBook(BookDM bookDM) {
        
        Log.info("Deleting book with ID: " + bookDM.getId());

        return given()
                .when()
                .delete(endpoint + "/" + bookDM.getId())
                .then()
                .extract().response();
    }

    public static Response deleteBook(String id) {
        
        Log.info("Deleting book with ID: " + id);

        return given()
                .when()
                .delete(endpoint + "/" + id)
                .then()
                .extract().response();
    }
}