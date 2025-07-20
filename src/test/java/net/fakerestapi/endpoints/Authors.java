package net.fakerestapi.endpoints;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import net.fakerestapi.constants.GeneralConstants;
import net.fakerestapi.dataModels.AuthorDM;
import net.fakerestapi.utils.Log;
import net.fakerestapi.utils.PropertiesFilesHandler;

import java.util.Properties;

import static io.restassured.RestAssured.given;

public class Authors {
    static PropertiesFilesHandler propHandler = new PropertiesFilesHandler();
    static Properties generalConfigsProps = propHandler.loadPropertiesFile(GeneralConstants.GENERAL_CONFIG_FILE_NAME);
    public static String endpoint = generalConfigsProps.getProperty(GeneralConstants.AUTHORS_ENDPOINT);
    public static Response getAllAuthors() {

        Log.info("Getting All Authors");

        return given()
                .accept(ContentType.JSON)
                .when()
                .get(endpoint)
                .then()
                .extract().response();
    }

    public static Response getAuthorById(int id) {

        Log.info("Getting Author by ID: " + id);

        return given()
                .accept(ContentType.JSON)
                .when()
                .get(endpoint + "/" + id)
                .then()
                .extract().response();
    }
    public static Response getAuthorById(String id) {

        Log.info("Getting Author by ID (string): " + id);

        return given()
                .accept(ContentType.JSON)
                .when()
                .get(endpoint + "/" + id)
                .then()
                .extract().response();
    }

    public static Response createAuthor(AuthorDM authorDM) {

        Log.info("Creating Author: " + authorDM.getFirstName());

        return given()
                .contentType(ContentType.JSON)
                .body(authorDM)
                .when()
                .post(endpoint)
                .then()
                .extract().response();
    }

    public static Response updateAuthor(AuthorDM authorDM) {

        Log.info("Updating Author with ID: " + authorDM.getId());

        return given()
                .contentType(ContentType.JSON)
                .body(authorDM)
                .when()
                .put(endpoint + "/" + authorDM.getId())
                .then()
                .extract().response();
    }

    public static Response deleteAuthor(AuthorDM authorDM) {

        Log.info("Deleting Author with ID: " + authorDM.getId());

        return given()
                .when()
                .delete(endpoint + "/" + authorDM.getId())
                .then()
                .extract().response();
    }

    public static Response deleteAuthor(String id) {

        Log.info("Deleting book with ID (string): " + id);

        return given()
                .when()
                .delete(endpoint + "/" + id)
                .then()
                .extract().response();
    }
}