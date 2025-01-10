package com.reminder.e2e;

import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext
public class ReminderApiE2ETest {

    // ------------------------------------- FIXTURES PARA OS TESTES

    @BeforeAll
    public static void setupClass() {

        RestAssured.baseURI = "http://localhost:8080/api";
        RestAssured.port = 8080;

        // Criação de usuário para os testes e2e
        given()
            .contentType("application/json")
            .body("""
                      {
                          "name": "testUserName",
                          "username": "testUserUsername",
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
                    .delete("/user/remove/" + "testUserUsername")
                    .then()
                    .statusCode(200)
                    .body(equalTo("User removed successfully"));
        }
    }

    public static String signInTestUser(String username, String password) {
        return given()
                .contentType("application/json")
                .body("""
                          {
                            "usernameOrEmail": "%s",
                            "password": "%s"
                          }
                         """.formatted(username, password)
                )
                .when()
                .post("/auth/signin")
                .then()
                .statusCode(200)
                .extract()
                .path("accessToken");
    }
    private static String signInTestUser() {
        return given()
                .contentType("application/json")
                .body("""
                          {
                            "usernameOrEmail": "testUserUsername",
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
    public void testSignInAndThenOutTest() {
        String accessToken = signInTestUser();
        assert accessToken != null;
        signOutTestUser(accessToken);
    }

    @Test
    public void testTryToSignOutWhileNotSignedIn() {
        given()
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
            .statusCode(401)
            .body("error", equalTo("Unauthorized"))
            .body("message", equalTo("User is not authenticated. Please log in to access this resource."));
    }


    @Test
    public void testCreateThenGetOneReminder() {
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

    @Test
    public void testCreateThenGetMultipleReminders() {
        String accessToken = signInTestUser();

        String item1 = """
                        {
                            "name": "Escova de dentes",
                            "dateLastChange": "01/01/2024",
                            "changeDaysInterval": 90
                          }
                      """;
        String item2 = """
                        {
                            "name": "Óleo do motor",
                            "dateLastChange": "10/01/2024",
                            "changeDaysInterval": 120
                          }
                      """;
        String item3 = """
                        {
                            "name": "Travesseiro",
                            "dateLastChange": "12/01/2024",
                            "changeDaysInterval": 100
                          }
                      """;


        given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .body(item1)
                .when()
                .post("user/item")
                .then()
                .statusCode(200)
                .body(equalTo("Item saved successfully"));

        given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .body(item2)
                .when()
                .post("user/item")
                .then()
                .statusCode(200)
                .body(equalTo("Item saved successfully"));

        given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .body(item3)
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
                .body("size()", greaterThanOrEqualTo(3))                // Verifica se pelo menos um item foi retornado
                .body("[0].name", equalTo("Escova de dentes"))
                .body("[0].dateLastChange", equalTo("01/01/2024"))
                .body("[0].changeDaysInterval", equalTo(90))
                .body("[1].name", equalTo("Óleo do motor"))
                .body("[1].dateLastChange", equalTo("10/01/2024"))
                .body("[1].changeDaysInterval", equalTo(120))
                .body("[2].name", equalTo("Travesseiro"))
                .body("[2].dateLastChange", equalTo("12/01/2024"))
                .body("[2].changeDaysInterval", equalTo(100));


        signOutTestUser(accessToken);
    }

    @Test
    public void testTryToCreateReminderWhileNotSignedIn() {
        String item = """
                        {
                            "name": "Escova de dentes",
                            "dateLastChange": "01/01/2024",
                            "changeDaysInterval": 90
                          }
                      """;

        given()
                .contentType("application/json")
                .body(item)
                .when()
                .post("user/item")
                .then()
                .statusCode(401)
                .body("error", equalTo("Unauthorized"))
                .body("message", equalTo("User is not authenticated. Please log in to access this resource."));
    }

    @Test
    public void testTryToGetReminderAfterSignedOut() {
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

        signOutTestUser(accessToken);

        given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .when()
                .get("/user/items")
                .then()
                .statusCode(401)
                .body("error", equalTo("Unauthorized"))
                .body("message", equalTo("User is not authenticated. Please log in to access this resource."));
    }

    @Test
    public void testTryCreatingReminderWithWrongDateFormat() {
        String accessToken = signInTestUser();

        String itemWhithInvalidDate = """
                        {
                            "name": "Escova de dentes",
                            "dateLastChange": "40/13/2024",
                            "changeDaysInterval": 90
                          }
                      """;

        String itemWhithInvalidDateFormat = """
                        {
                            "name": "Escova de dentes",
                            "dateLastChange": "10-10-2024",
                            "changeDaysInterval": 90
                          }
                      """;

        given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .body(itemWhithInvalidDate)
                .when()
                .post("user/item")
                .then()
                .statusCode(400)
                .body("error", equalTo("Invalid Date"))
                .body("message", equalTo("Date must be in the format dd/MM/yyyy and valid."));

        given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .body(itemWhithInvalidDateFormat)
                .when()
                .post("user/item")
                .then()
                .statusCode(400)
                .body("dateLastChange", equalTo("Date must be in the format dd/MM/yyyy."));

        signOutTestUser(accessToken);
    }

    @Test
    public void testTryCreatingReminderWithMissingData() {
        String accessToken = signInTestUser();

        String invalidItemNoName = """
                                    {
                                        "dateLastChange": "01/01/2024",
                                        "changeDaysInterval": 10
                                    }
                                  """;
        String invalidItemNoDateLastChange =  """
                                              {
                                                  "name": "Travesseiro",
                                                  "changeDaysInterval": 10
                                              }
                                              """;
        String invalidItemNoChangeDaysInterval =  """
                                                  {
                                                      "name": "Travesseiro",
                                                      "dateLastChange": "01/01/2024"
                                                  }
                                                  """;

        given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .body(invalidItemNoName)
                .when()
                .post("user/item")
                .then()
                .statusCode(400)
                .body("name", equalTo("Name is required"));

        given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .body(invalidItemNoDateLastChange)
                .when()
                .post("user/item")
                .then()
                .statusCode(400)
                .body("dateLastChange", equalTo("Date of last change is required"));

        given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .body(invalidItemNoChangeDaysInterval)
                .when()
                .post("user/item")
                .then()
                .statusCode(400)
                .body("changeDaysInterval", equalTo("Change days interval is required"));

        signOutTestUser(accessToken);
    }

    @Test
    public void testCreateReminderWithNonPositiveChangeDaysInterval() {
        String accessToken = signInTestUser();

        String item = """
                        {
                            "name": "Escova de dentes",
                            "dateLastChange": "01/01/2024",
                            "changeDaysInterval": -90
                          }
                      """;

        given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .body(item)
                .when()
                .post("user/item")
                .then()
                .statusCode(400)
                .body("error", equalTo("Bad Request"))
                .body("message", equalTo("Invalid data provided. Change interval must be positive."));

        signOutTestUser(accessToken);
    }

    @Test
    public void testDeleteUserThenCreateAgain() {
        String accessToken = signInTestUser();

        given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .when()
                .delete("user/remove/" + "testUserUsername")
                .then()
                .statusCode(200)
                .body(equalTo("User removed successfully"));

        given()
                .contentType("application/json")
                .body("""
                      {
                          "name": "testUserName",
                          "username": "testUserUsername",
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

    @Test
    public void testTryToDeleteUserWhileNotSignedIn() {
        given()
                .contentType("application/json")
                .when()
                .delete("user/remove/" + "testUserUsername")
                .then()
                .statusCode(401)
                .body("error", equalTo("Unauthorized"))
                .body("message", equalTo("User is not authenticated. Please log in to access this resource."));
    }

    @Test
    public void testTryToDeleteUserWithInvalidUsername() {
        String accessToken = signInTestUser();
        given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .when()
                .delete("user/remove/" + "invalidUsername")
                .then()
                .statusCode(404)
                .body(equalTo("User not found!"));
    }

    @Test
    public void testTryToAccessAdminEndpointWithNormalUser(){
        String accessToken = signInTestUser();
        given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType("application/json")
                .when()
                .get("/admin/info")
                .then()
                .statusCode(403)
                .body("error", equalTo("Access Denied"))
                .body("message", equalTo("You are not authorized to access this resource."));
    }

    @Test
    public void testTryToCreateUserWithDuplicateUsername() {
        // TODO
    }

    @Test
    public void testTryToCreateUserWithDuplicateMail() {
        // TODO
    }

    @Test
    public void testTryToCreateUserWithInvalidData() {
        // TODO
    }

    @Test
    public void testTryToDeleteAnotherUserBeingAdmin() {
        // TODO
    }

    @Test
    public void testTryToDeleteAnotherUserNotBeingAdmin() {
        // Cria um segundo usuário
        given()
                .contentType("application/json")
                .body("""
                      {
                          "name": "anotherTestUserName",
                          "username": "anotherTestUserName",
                          "email": "anotherTestMail@example.com",
                          "password": "1234test"
                      }
                    """
                )
                .when()
                .post("/auth/signup")
                .then()
                .statusCode(200)
                .body(equalTo("User registered successfully"));

        // Faz login com o primeiro usuário
        String accessToken = signInTestUser();

        // Tenta remover o segundo usuário (estando logado com o primeiro)
        if (accessToken != null) {
            given()
                    .header("Authorization", "Bearer " + accessToken)
                    .contentType("application/json")
                    .when()
                    .delete("/user/remove/" + "anotherTestUserName")
                    .then()
                    .statusCode(403)
                    .body(equalTo("You are not authorized to remove this user!"));
        }

        // Remove o segundo usuário após o teste ser completo
        String accessTokenAnotherUser = signInTestUser("anotherTestUserName", "1234test");
        if (accessToken != null) {
            given()
                    .header("Authorization", "Bearer " + accessTokenAnotherUser)
                    .contentType("application/json")
                    .when()
                    .delete("/user/remove/" + "anotherTestUserName")
                    .then()
                    .statusCode(200)
                    .body(equalTo("User removed successfully"));
        }



    }

}
