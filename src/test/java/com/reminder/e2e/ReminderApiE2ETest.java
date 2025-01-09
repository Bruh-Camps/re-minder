package com.reminder.e2e;
import io.restassured.RestAssured;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


public class ReminderApiE2ETest {

    // ------------------------------------- FIXTURES PARA OS TESTES

    @BeforeAll
    public static void setupClass() {

        RestAssured.baseURI = "http://localhost:8080/api";

        // Criação de usuário para os testes e2e
        given()
            .contentType("application/json")
            .body("""
                      {
                          "name": "test",
                          "username": "test",
                          "email": "test@example.com",
                          "password": "1234test"
                      }
                    """
            )
            .when()
            .post("/auth/signup")
            .then()
            .statusCode(200)
            .body(equalTo("User registered successfully"));
    }

    @AfterAll
    public static void teardownClass() {

        //Faz signin antes de remover usuário para ser autorizado a isso
        String accessToken = signInTestUser();

        if (accessToken != null) {
            given()
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType("application/json")
                    .when()
                    .delete("/user/remove/test")
                    .then()
                    .statusCode(200)
                    .body(equalTo("User removed successfully"));
        }
    }

    public static String signInTestUser() {
        // Login do usuário
        return given()
                .contentType("application/json")
                .body("""
                          {
                            "usernameOrEmail": "test",
                            "password": "1234test"
                          }
                         """
                )
                .when()
                .post("/auth/signin")
                .then()
                .statusCode(200)
                .extract()
                .path("accessToken");
    }

    public static void signOutTestUser(String accessToken) {
        // Logout do usuário
        given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .body("""
                          {
                            "usernameOrEmail": "test",
                            "password": "1234test"
                          }
                         """
                )
                .when()
                .post("/user/signout")
                .then()
                .statusCode(200)
                .body(equalTo("User signed out successfully"));
    }






    // ------------------------------------- TESTES DE SISTEMA -------------------------------------


    @Test
    public void signInAndThenOutTest() {
        String accessToken = signInTestUser();
        assert accessToken != null;
        signOutTestUser(accessToken);
    }


    @Test
    public void testCreateThenGetReminder() {
        String accessToken = signInTestUser();

        String item = """
                        {
                            "name": "Escova de dentes",
                            "dateLastChange": "01/01/2024",
                            "changeDaysInterval": 90
                          }
                      """;

        given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .body(item)
                .when()
                .post("user/item")
                .then()
                .statusCode(200)
                .body(equalTo("Item saved successfully"));

        given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .when()
                .get("/user/items")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(1))                // Verifica se pelo menos um item foi retornado
                .body("[0].name", equalTo("Escova de dentes"))
                .body("[0].dateLastChange", equalTo("01/01/2024"))
                .body("[0].changeDaysInterval", equalTo(90));

        signOutTestUser(accessToken);
    }



}
