package com.example.JobManagementSystem.E2ETest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.BeforeClass;
import org.junit.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.*;

public class JobControllerE2ETest {

    @BeforeClass
    public static void setup() {
        RestAssured.port = 8080;
        RestAssured.baseURI = "http://localhost";
        RestAssured.basePath = "/v1/jobs";
    }

    // we should make sure that the delete id is there in the databsae first
    @Test
    public void testCreateAndDeleteJob() {
        // Create job payload
        String jobPayload = """
                {
                    "name": "Job1",
                    "jobType": "DataLoad"
                }
                """;

        // Step 1: Create Job
        given()
                .contentType(ContentType.JSON)
                .body(jobPayload)
                .when()
                .post()
                .then()
                .statusCode(200)
                .body(equalTo("true"));

        // Step 2: Get Job Status
        given()
                .pathParam("id", 9)
                .when()
                .get("/{id}/status")
                .then()
                .statusCode(200)
                .body(equalTo("SUCCESS"));
//        .body(not(emptyString()));
////
        // Step 3: Delete Job
        given()
                .queryParam("id", "9")
                .when()
                .delete()
                .then()
                .statusCode(200)
                .body(equalTo("true"));
    }

    @Test
    public void testScheduleJobExpectError() {
        // Invalid job scheduling payload
        String invalidJobPayload = """
                {
                    "name": "200",
                    "jobType": "DataLoad",
                    "schedule": "2025-01-25T12:10:00"
                }
                """;

        // Send the request and expect an error response
        given()
                .contentType(ContentType.JSON)
                .body(invalidJobPayload)
                .when()
                .post()
                .then()
                .statusCode(400)  // Expecting Bad Request
                .contentType(ContentType.JSON)
                .body("type", equalTo("about:blank"))
                .body("title", equalTo("Bad Request"))
                .body("status", equalTo(400))
                .body("detail", equalTo("Invalid request content."))
                .body("instance", equalTo("/v1/jobs"));
    }

    @Test
    public void testMissingFieldJobExpectError() {
        String invalidJobPayload = """
                {
                    "jobType": "DataLoad",
                    "schedule": "2025-01-25T12:10:00"
                }
                """;

        // Send the request and expect an error response
        given()
                .contentType(ContentType.JSON)
                .body(invalidJobPayload)
                .when()
                .post()
                .then()
                .statusCode(400)  // Expecting Bad Request
                .contentType(ContentType.JSON)
                .body("type", equalTo("about:blank"))
                .body("title", equalTo("Bad Request"))
                .body("status", equalTo(400))
                .body("detail", equalTo("Invalid request content."))
                .body("instance", equalTo("/v1/jobs"));
    }




}
