import io.qameta.allure.Step;
import org.apache.commons.lang3.RandomStringUtils;

public class UserRegistrationData {
    public final String email;
    public final String password;
    public final String name;


    public UserRegistrationData(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
    @Step("Получение случайных данных для регистрации.")
    public static UserRegistrationData getRandomRegistrationData () {
        final String email = RandomStringUtils.randomAlphabetic(4) + "@" + RandomStringUtils.randomAlphabetic(4) + ".ru";
        final String password = RandomStringUtils.randomAlphabetic(10);
        final String name = RandomStringUtils.randomAlphabetic(10);
        return new UserRegistrationData(email, password, name);
    }
}

