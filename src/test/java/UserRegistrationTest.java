import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.*;

public class UserRegistrationTest { //эндпойнт /api/auth/register

    private UserMethods userMethods;

    @Before
    public void setUp () {
        userMethods = new UserMethods();
    }

    @Epic(value = "API Stellar Burgers")
    @Feature(value = "Пользователь")
    @Story(value = "Регистрация пользователя")
    @Test
    @DisplayName("Успешная регистрация нового пользователя.")
    @Description("Тест корректности ответа при регистрации нового пользователя с правильными данными для эндпойнта /api/auth/register.")
    @Owner(value = "Кидяев Александр Дмитриевич")
    @Severity(value = SeverityLevel.BLOCKER)
    public void checkResponseForRegisteringANewUserTest() {
        UserRegistrationData userRegistrationData = UserRegistrationData.getRandomRegistrationData();
        Response registrationResponse = userMethods.registerNewUser(userRegistrationData);
        registrationResponse.then().assertThat()
                .body("success", equalTo(true))
                .and()
                .statusCode(SC_OK);

    }

    @Epic(value = "API Stellar Burgers")
    @Feature(value = "Пользователь")
    @Story(value = "Регистрация пользователя")
    @Test
    @DisplayName("Попытка регистрации нового пользователя по уже существующим данным.")
    @Description("Тест корректности ответа при регистрации нового пользователя по уже существующим данным для эндпойнта /api/auth/register.")
    @Owner(value = "Кидяев Александр Дмитриевич")
    @Severity(value = SeverityLevel.CRITICAL)
    public void checkResponseAfterRegistrationOfSecondUserWithSameParametersTest() {
        UserRegistrationData userRegistrationData = UserRegistrationData.getRandomRegistrationData();
        userMethods.registerNewUser(userRegistrationData);
        Response registrationResponse = userMethods.registerNewUser(userRegistrationData);
        registrationResponse.then().assertThat()
                .body("message", equalTo("User already exists"))
                .and()
                .body("success", equalTo(false))
                .and()
                .statusCode(SC_FORBIDDEN);

    }

    @Epic(value = "API Stellar Burgers")
    @Feature(value = "Пользователь")
    @Story(value = "Регистрация пользователя")
    @Test
    @DisplayName("Попытка регистрация пользователя без email.")
    @Description("Тест корректности ответа при регистрации нового пользователя без использования поля почты для эндпойнта /api/auth/register.")
    @Owner(value = "Кидяев Александр Дмитриевич")
    @Severity(value = SeverityLevel.MINOR)
    public void checkUserRegistrationWithoutLoginTest() {
        String bodyWithoutEmail = "{\"password\":\"somepassword\",\"name\":\"somename\"}";
        Response registrationResponse = userMethods.registerNewUserWithIncorrectData(bodyWithoutEmail);
        registrationResponse.then().assertThat()
                .body("message", equalTo("Email, password and name are required fields"))
                .and()
                .body("success", equalTo(false))
                .and()
                .statusCode(SC_FORBIDDEN);
    }

    @Epic(value = "API Stellar Burgers")
    @Feature(value = "Пользователь")
    @Story(value = "Регистрация пользователя")
    @Test
    @DisplayName("Попытка регистрация пользователя без пароля.")
    @Description("Тест корректности ответа при регистрации нового пользователя без использования поля пароля для эндпойнта /api/auth/register.")
    @Owner(value = "Кидяев Александр Дмитриевич")
    @Severity(value = SeverityLevel.MINOR)
    public void checkUserRegistrationWithoutPasswordTest() {
        String bodyWithoutEmail = "{\"email\":\"email@test.ru\",\"name\":\"somename\"}";
        Response registrationResponse = userMethods.registerNewUserWithIncorrectData(bodyWithoutEmail);
        registrationResponse.then().assertThat()
                .body("message", equalTo("Email, password and name are required fields"))
                .and()
                .body("success", equalTo(false))
                .and()
                .statusCode(SC_FORBIDDEN);
    }

    @Epic(value = "API Stellar Burgers")
    @Feature(value = "Пользователь")
    @Story(value = "Регистрация пользователя")
    @Test
    @DisplayName("Попытка регистрация пользователя без имени.")
    @Description("Тест корректности ответа при регистрации нового пользователя без использования поля имени для эндпойнта /api/auth/register.")
    @Owner(value = "Кидяев Александр Дмитриевич")
    @Severity(value = SeverityLevel.MINOR)
    public void checkUserRegistrationWithoutNameTest() {
        String bodyWithoutEmail = "{\"email\":\"email@test.ru\",\"password\":\"somepassword\"}";
        Response registrationResponse = userMethods.registerNewUserWithIncorrectData(bodyWithoutEmail);
        registrationResponse.then().assertThat()
                .body("message", equalTo("Email, password and name are required fields"))
                .and()
                .body("success", equalTo(false))
                .and()
                .statusCode(SC_FORBIDDEN);
    }

}