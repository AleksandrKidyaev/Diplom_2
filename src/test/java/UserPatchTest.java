import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class UserPatchTest { //эндпойнт /api/auth/user

    private UserMethods userMethods;

    @Before
    public void setUp () {
        userMethods = new UserMethods();
    }

    @Epic(value = "API Stellar Burgers")
    @Feature(value = "Пользователь")
    @Story(value = "Изменение пользователя")
    @Test
    @DisplayName("Успешное изменение авторизованного пользователя.")
    @Description("Тест корректности ответа при успешном изменении авторизованного нового пользователя с проверкой возможности последующей авторизацией с новыми данными + выход из системы. Для эндпойнта /api/auth/user.")
    @Owner(value = "Кидяев Александр Дмитриевич")
    @Severity(value = SeverityLevel.CRITICAL)
    public void checkResponseAfterCorrectUserPatchTest() {
        UserRegistrationData userRegistrationData = UserRegistrationData.getRandomRegistrationData();
        userMethods.registerNewUser(userRegistrationData);
        String accessToken = userMethods.returnUserAccessToken(UserAuthorizationData.from(userRegistrationData));
        UserRegistrationData newUserData = UserRegistrationData.getRandomRegistrationData();
        Response patchResponse = userMethods.userPatch(newUserData, accessToken);
        patchResponse.then().assertThat()
                .body("success", equalTo(true))
                .and()
                .statusCode(SC_OK);
        Response loginResponse = userMethods.userAuthorization(UserAuthorizationData.from(newUserData));
        loginResponse.then().assertThat()
                .body("success", equalTo(true))
                .and()
                .statusCode(SC_OK);
        String refreshToken = userMethods.returnUserRefreshToken(UserAuthorizationData.from(newUserData));
        userMethods.userLogout(refreshToken);
    }

    @Epic(value = "API Stellar Burgers")
    @Feature(value = "Пользователь")
    @Story(value = "Изменение пользователя")
    @Test
    @DisplayName("Попытка изменения не авторизованного пользователя.")
    @Description("Тест корректности ответа при попытке изменении пользователя без авторизации для эндпойнта /api/auth/user.")
    @Owner(value = "Кидяев Александр Дмитриевич")
    @Severity(value = SeverityLevel.CRITICAL)
    public void checkResponseAfterUserWithoutAuthorizationPatchTest() {
        UserRegistrationData userRegistrationData = UserRegistrationData.getRandomRegistrationData();
        userMethods.registerNewUser(userRegistrationData);
        UserRegistrationData newUserData = UserRegistrationData.getRandomRegistrationData();
        Response patchResponse = userMethods.userPatchWithoutAuthorization(newUserData);
        patchResponse.then().assertThat()
                .body("message", equalTo("You should be authorised"))
                .and()
                .body("success", equalTo(false))
                .and()
                .statusCode(SC_UNAUTHORIZED);
        Response loginResponse = userMethods.userAuthorization(UserAuthorizationData.from(newUserData));
        loginResponse.then().assertThat()
                .body("message", equalTo("email or password are incorrect"))
                .and()
                .body("success", equalTo(false))
                .and()
                .statusCode(SC_UNAUTHORIZED);
    }


}
