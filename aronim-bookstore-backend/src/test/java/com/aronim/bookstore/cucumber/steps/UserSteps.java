package com.aronim.bookstore.cucumber.steps;

import com.aronim.bookstore.domain.model.Email;
import com.aronim.bookstore.domain.model.Password;
import com.aronim.bookstore.domain.model.User;
import com.aronim.bookstore.domain.repository.RoleRepository;
import com.aronim.bookstore.domain.repository.UserRepository;
import com.aronim.bookstore.e2e.api.UserApiClient;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class UserSteps {

    private final ResponseHolder responseHolder;

    private final RoleRepository roleRepository;

    private final UserApiClient userApiClient;

    private final UserRepository userRepository;

    public UserSteps(ResponseHolder responseHolder,
                     RoleRepository roleRepository,
                     UserApiClient userApiClient,
                     UserRepository userRepository) {
        this.responseHolder = responseHolder;
        this.roleRepository = roleRepository;
        this.userApiClient = userApiClient;
        this.userRepository = userRepository;
    }

    @Given("the system has an admin user with email {string} and password {string}")
    public void systemHasAdminUser(String email, String password) {
        final User user = User.create(
                new Email(email),
                new Password(password),
                "Admin",
                "User"
        );

        roleRepository.findByName("ADMIN").ifPresent(user::assignRole);

        userRepository.save(user);
    }

    @Given("a user exists with email {string} and password {string}")
    public void userExists(String email, String password) {
        responseHolder.response = userApiClient.createUser(
                email,
                password,
                "First Name",
                "Last Name"
        );
    }

    @When("I register a new user with the following details:")
    public void registerNewUser(DataTable dataTable) {
        Map<String, String> userData = dataTable.asMap();

        responseHolder.response = userApiClient.createUser(
                userData.get("email"),
                userData.get("password"),
                userData.get("firstName"),
                userData.get("lastName")
        );
    }

    @When("I login with email {string} and password {string}")
    public void loginWithCredentials(String email, String password) {
        responseHolder.response = userApiClient.login(email, password);
    }

    @Then("the user with email {string} should exist in the system")
    public void userShouldExist(String email) {
        assertThat(userRepository.findByEmail(new Email(email)).isPresent()).isTrue();
    }

    @Then("the response should contain an authentication token")
    public void responseShouldContainToken() {
        String token = responseHolder.response.jsonPath().getString("token");
        assertThat(token).isNotEmpty();
    }
}
