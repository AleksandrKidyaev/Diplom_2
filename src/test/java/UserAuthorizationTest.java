import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;

public class UserAuthorizationTest { //эндпойнт /api/auth/login

    private UserMethods userMethods;

    @Before
    public void setUp () {
        userMethods = new UserMethods();
    }

    @Epic(value = "API Stellar Burgers")
    @Feature(value = "Пользователь")
    @Story(value = "Авторизация пользователя")
    @Test
    @DisplayName("Успешная авторизация пользователя.")
    @Description("Тест корректности ответа при успешной авторизации нового пользователя и его последующего выхода из системы для эндпойнта /api/auth/login.")
    @Owner(value = "Кидяев Александр Дмитриевич")
    @Severity(value = SeverityLevel.BLOCKER)
    public void checkResponseAfterCorrectUserAuthorizationAndLogoutTest() {
        UserRegistrationData userRegistrationData = UserRegistrationData.getRandomRegistrationData();
        userMethods.registerNewUser(userRegistrationData);
        Response loginResponse = userMethods.userAuthorization(UserAuthorizationData.from(userRegistrationData));
        String accessToken = userMethods.returnUserAccessToken(UserAuthorizationData.from(userRegistrationData));
        loginResponse.then().assertThat()
                .body("accessToken", equalTo(accessToken))
                .and()
                .body("success", equalTo(true))
                .and()
                .statusCode(SC_OK);
        String refreshToken = userMethods.returnUserRefreshToken(UserAuthorizationData.from(userRegistrationData));
        Response logoutResponse = userMethods.userLogout(refreshToken);
        logoutResponse.then().assertThat()
                .body("message", equalTo("Successful logout"))
                .and()
                .body("success", equalTo(true))
                .and()
                .statusCode(SC_OK);
    }

    @Epic(value = "API Stellar Burgers")
    @Feature(value = "Пользователь")
    @Story(value = "Авторизация пользователя")
    @Test
    @DisplayName("Авторизация пользователя с некорректными данными.")
    @Description("Тест корректности ответа при авторизации не зарегистрированного пользователя для эндпойнта /api/auth/login.")
    @Owner(value = "Кидяев Александр Дмитриевич")
    @Severity(value = SeverityLevel.NORMAL)
    public void checkResponseAfterIncorrectUserAuthorizationTest() {
        UserAuthorizationData userAuthorizationData = UserAuthorizationData.getRandomAuthorizationData();
        Response loginResponse = userMethods.userAuthorization(userAuthorizationData);
        loginResponse.then().assertThat()
                .body("message", equalTo("email or password are incorrect"))
                .and()
                .body("success", equalTo(false))
                .and()
                .statusCode(SC_UNAUTHORIZED);
    }

}
