import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class UserMethods extends RestAssuredSpecification{

    private static final String endpointUrl = "/api/auth/";

    @Step("Регистрация нового пользователя.")
    public Response registerNewUser (UserRegistrationData registrationData) {

        return given()
                .spec(getBaseSpec())
                .body(registrationData)
                .when()
                .post(endpointUrl + "register");

    }

    @Step("Регистрация нового пользователя.")
    public Response registerNewUserWithIncorrectData (String registrationData) {

        return given()
                .spec(getBaseSpec())
                .body(registrationData)
                .when()
                .post(endpointUrl + "register");

    }

    @Step("Получение accessToken пользователя после авторизации.")
    public String returnUserAccessToken (UserAuthorizationData authorizationData) {

        return given()
                .spec(getBaseSpec())
                .body(authorizationData)
                .when()
                .post(endpointUrl + "login")
                .then()
                .extract()
                .path("accessToken");

    }

    @Step("Получение refreshToken пользователя после авторизации.")
    public String returnUserRefreshToken (UserAuthorizationData authorizationData) {

        return given()
                .spec(getBaseSpec())
                .body(authorizationData)
                .when()
                .post(endpointUrl + "login")
                .then()
                .extract()
                .path("refreshToken");

    }

    @Step("Авторизация.")
    public Response userAuthorization (UserAuthorizationData authorizationData) {

        return given()
                .spec(getBaseSpec())
                .body(authorizationData)
                .when()
                .post(endpointUrl + "login");

    }

    @Step("Выход из системы.")
    public Response userLogout (String refreshToken) {
        Map<String, String> body = new HashMap<>();
        body.put("token", refreshToken);
        return given()
                .spec(getBaseSpec())
                .body(body)
                .when()
                .post(endpointUrl + "logout");

    }

    @Step("Изменение пользователя.")
    public Response userPatch (UserRegistrationData registrationData, String accessToken) {

        return given()
                .spec(getBaseSpec())
                .headers("Authorization", accessToken)
                .body(registrationData)
                .when()
                .patch(endpointUrl + "user");

    }

    @Step("Попытка изменения пользователя без авторизации.")
    public Response userPatchWithoutAuthorization (UserRegistrationData registrationData) {

        return given()
                .spec(getBaseSpec())
                .body(registrationData)
                .when()
                .patch(endpointUrl + "user");

    }

}

