package com.aronim.bookstore.cucumber.steps;

import io.restassured.response.Response;
import org.springframework.stereotype.Component;

@Component
public class ResponseHolder {
    public Response response;
}
