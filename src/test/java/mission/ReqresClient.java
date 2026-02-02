package mission;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class ReqresClient {

    private static final String BASE_URL = "https://reqres.in/api";
   
    private static final String API_KEY = "reqres_2a3e235444ff4c85b00fb22718e184b9"; 

    private static RequestSpecification getRequestSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .addHeader("x-api-key", API_KEY)
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) Chrome/120.0.0.0 Safari/537.36")
                .setContentType(ContentType.JSON)
                .build();
    }

    public static Response get(String endpoint) {
        return RestAssured.given().spec(getRequestSpec()).get(endpoint);
    }

    public static Response post(String endpoint, Object body) {
        return RestAssured.given().spec(getRequestSpec()).body(body).post(endpoint);
    }

    public static Response put(String endpoint, Object body) {
        return RestAssured.given()
                .spec(getRequestSpec()) // THIS IS CRITICAL: It carries your API Key and JSON Content-Type
                .body(body)
                .when()
                .put(endpoint)
                .then()
                .extract()
                .response();
    }

public static Response patch(String endpoint, Object body) {
    return RestAssured.given().spec(getRequestSpec()).body(body).patch(endpoint);
}

public static Response delete(String endpoint) {
    return RestAssured.given().spec(getRequestSpec()).delete(endpoint);
}
}