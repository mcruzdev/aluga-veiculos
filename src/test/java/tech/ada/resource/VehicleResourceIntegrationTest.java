package tech.ada.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.quarkus.logging.Log;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tech.ada.dto.VehicleRequestBody;
import tech.ada.model.VehicleStatus;

@QuarkusTest
public class VehicleResourceIntegrationTest {

    static ObjectMapper objectMapper = new ObjectMapper();

    private String createVehicle() throws JsonProcessingException {
        VehicleRequestBody vehicleRequestBody = new VehicleRequestBody();
        vehicleRequestBody.setStatus(VehicleStatus.AVAILABLE);
        vehicleRequestBody.setEngine("1.0");
        vehicleRequestBody.setModel("Mobi");
        vehicleRequestBody.setYear(2022);

        String body = objectMapper.writeValueAsString(vehicleRequestBody);

        Response response = RestAssured.given()
                .contentType("application/json")
                .body(body)
                .post("api/v1/vehicles")
                .thenReturn();

        return response.getHeader("Location");
    }

    @BeforeEach
    void beforeEach() {
        Log.info("Executando antes de todos os testes");
    }

    @Test
    void shouldReturn400WhenThereAreSomeInvalidFields() throws JsonProcessingException {


        RestAssured.given()
                .contentType("application/json")
                .body("""
                       {
                         "brand": "Fiat",
                         "model": "Mobi",
                         "status": "RENTED",
                         "year": 2022,
                         "engine": "1.0"
                       }
                      """)
                .post("api/v1/vehicles")
                .then()
                .statusCode(201);
    }

    @Test
    void shouldGetVehicleByID() throws JsonProcessingException {
        String location = createVehicle();
        RestAssured.given()
                .get(location)
                .then()
                .statusCode(200);
    }

    @Test
    void shouldReceiveNotFoundWhenThereIsNoVehicleWithProvidedID() {
        RestAssured.given()
                .get("/api/v1/vehicles/1292929")
                .then()
                .statusCode(404);
    }

    @Test
    void shouldGetAll() {
        RestAssured.given()
                .get("api/v1/vehicles")
                .then()
                .statusCode(200);
    }
}
