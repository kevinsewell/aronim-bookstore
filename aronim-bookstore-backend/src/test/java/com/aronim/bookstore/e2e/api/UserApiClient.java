package com.aronim.bookstore.e2e.api;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

import static io.restassured.RestAssured.given;

@Component
public class UserApiClient {

    @Autowired
    private Environment environment;

    private String getBaseUrl() {
        return "http://localhost:" + environment.getProperty("local.server.port") + "/api";
    }

    public Response login(String username, String password) {
        return given()
                .contentType(ContentType.JSON)
                .body(Map.of(
                        "username", username,
                        "password", password
                ))
                .when()
                .post(getBaseUrl() + "/auth/login");
    }

    /**
     * Creates a new user.
     *
     * @param email     User's email address
     * @param password  User's password
     * @param firstName User's first name
     * @param lastName  User's last name
     * @return Response object containing the API response
     */
    public Response createUser(String email, String password, String firstName, String lastName) {
        return given()
                .contentType(ContentType.JSON)
                .body(Map.of(
                        "email", email,
                        "password", password,
                        "firstName", firstName,
                        "lastName", lastName
                ))
                .when()
                .post(getBaseUrl() + "/users");
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id The UUID of the user to retrieve
     * @return Response object containing the API response
     */
    public Response getUserById(UUID id) {
        return given()
                .when()
                .get(getBaseUrl() + "/users/" + id);
    }

    /**
     * Retrieves a user by their email address.
     *
     * @param email The email address of the user to retrieve
     * @return Response object containing the API response
     */
    public Response getUserByEmail(String email) {
        return given()
                .when()
                .get(getBaseUrl() + "/users/email/" + email);
    }

    /**
     * Changes a user's password.
     *
     * @param id          The UUID of the user whose password will be changed
     * @param newPassword The new password
     * @return Response object containing the API response
     */
    public Response changePassword(UUID id, String newPassword) {
        return given()
                .contentType(ContentType.JSON)
                .body(Map.of("newPassword", newPassword))
                .when()
                .patch(getBaseUrl() + "/users/" + id + "/password");
    }

    /**
     * Creates a user and returns the response. If the creation is successful,
     * extracts the user ID from the response.
     *
     * @param email     User's email address
     * @param password  User's password
     * @param firstName User's first name
     * @param lastName  User's last name
     * @return UUID of the created user, or null if creation failed
     */
    public UUID createUserAndGetId(String email, String password, String firstName, String lastName) {
        Response response = createUser(email, password, firstName, lastName);

        if (response.getStatusCode() == 201) {
            return UUID.fromString(response.jsonPath().getString("id"));
        }
        return null;
    }

    /**
     * Checks if a user with the given email exists.
     *
     * @param email The email address to check
     * @return true if a user with the given email exists, false otherwise
     */
    public boolean userExistsByEmail(String email) {
        Response response = getUserByEmail(email);
        return response.getStatusCode() == 200;
    }

    /**
     * Creates a test user with random values.
     * Useful for setting up test data.
     *
     * @return UUID of the created test user
     */
    public UUID createTestUser() {
        String randomSuffix = String.valueOf(System.currentTimeMillis());
        String email = "test.user." + randomSuffix + "@example.com";
        String password = "Password123!";
        String firstName = "Test";
        String lastName = "User" + randomSuffix;

        return createUserAndGetId(email, password, firstName, lastName);
    }
}
