package com.aronim.bookstore.cucumber;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com.aronim.bookstore.cucumber"},
        plugin = {"pretty", "html:target/cucumber-reports"}
)
public class CucumberIntegrationTest {
    // This class is the entry point for running Cucumber tests
}
