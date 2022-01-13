import io.qameta.allure.Step;
import org.apache.commons.lang3.RandomStringUtils;

public class UserAuthorizationData { //избавился от файлов, добавив классы для получения тел

    public final String email;
    public final String password;

    public UserAuthorizationData(String email, String password) {
        this.email = email;
        this.password = password;
    }
    @Step("Получение случайных данных для авторизации.")
    public static UserAuthorizationData getRandomAuthorizationData () {
        final String email = RandomStringUtils.randomAlphabetic(4) + "@" + RandomStringUtils.randomAlphabetic(4) + ".ru";
        final String password = RandomStringUtils.randomAlphabetic(10);
        return new UserAuthorizationData(email, password);
    }
    @Step("Получение логина и пароля из данных о регистрации.")
    public static UserAuthorizationData from(UserRegistrationData userRegistrationData) {
        return new UserAuthorizationData(userRegistrationData.email, userRegistrationData.password);
    }
}

