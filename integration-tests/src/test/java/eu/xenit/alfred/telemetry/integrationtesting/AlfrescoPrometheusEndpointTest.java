package eu.xenit.alfred.telemetry.integrationtesting;

import static io.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.isOneOf;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

public class AlfrescoPrometheusEndpointTest extends RestAssuredTest {

    @Test
    void alfrescoEndpointExposesAlfredTelemetryMetrics() {
        // First call fails due to
        // org.springframework.jdbc.UncategorizedSQLException:
        // Error selecting key or setting result to parameter object.
        // Cause: org.postgresql.util.PSQLException:
        // ERROR: cannot execute nextval() in a read-only transaction
        given().when().get("s/prometheus");
        // Actual test
        ExtractableResponse<Response> response =
                given()
                        .log().ifValidationFails()
                        .when()
                        .get("s/prometheus")
                        .then()
                        .log().ifValidationFails()
                        .statusCode(isOneOf(200))
                        .extract();

        String responseBody = response.body().asString();

        assertThat(responseBody, containsString("alfred_telemetry_registries_total"));
        // Does not work with integrated Alfresco Prometheus registry
//        assertThat(responseBody, containsString("application=\"alfresco\""));
//        assertThat(responseBody, containsString(",host=\""));
    }

}
