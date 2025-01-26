package com.example.JobManagementSystem.E2ETest;


import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class JobTypeControllerTest {

    @BeforeClass
    public static void setup() {
        RestAssured.port = 8080;  // Ensure this matches your app's port
        RestAssured.baseURI = "http://localhost";
        RestAssured.basePath = "/v1/jobtype";
    }

    @Test
    public void testGetAllJobTypes() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get()
                .then()
                .statusCode(200)  // Expecting HTTP 200 OK
                .contentType(ContentType.JSON)
                .body("jobTypeName", not(empty()))  // Ensure jobTypeName list is not empty
                .body("jobTypeName", hasItems("DataLoad", "Email")); // Adjust expected job types
    }
}
