import io.qameta.allure.*;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class OrderTest { //эндпойнт /api/orders

    private OrderMethods orderMethods;
    private UserMethods userMethods;
    private IngredientsMethods ingredientsMethods;

    @Before
    public void setUp () {
        orderMethods = new OrderMethods();
        userMethods = new UserMethods();
        ingredientsMethods = new IngredientsMethods();
    }

    @Epic(value = "API Stellar Burgers")
    @Feature(value = "Заказ")
    @Story(value = "Создание заказа")
    @Test
    @DisplayName("Попытка заказа без авторизации.")
    @Description("Тест корректности ответа при попытке создания заказа без авторизации для эндпойнта /api/orders.")
    @Owner(value = "Кидяев Александр Дмитриевич")
    @Severity(value = SeverityLevel.NORMAL)
    public void checkResponseAfterOrderPostWithoutAuthorizationTest() {

        String ingredient = ingredientsMethods.getIngredientId(3);
        Response orderResponse = orderMethods.postOrderWithoutAuthorization(ingredient);

        orderResponse.then().assertThat()
                .body("message", equalTo("You should be authorised"))
                .and()
                .body("success", equalTo(false))
                .and()
                .statusCode(SC_UNAUTHORIZED);

    }
    /*
    Этот тест падает, т.к. заказ успешно создается без хэдера с авторизационным токеном. Поле message в ответе нет.
    Но если, для проверки, его заменить на name, то в фактическом результате будет название бургера, т.е. заказ создается
     */

    @Epic(value = "API Stellar Burgers")
    @Feature(value = "Заказ")
    @Story(value = "Создание заказа")
    @Test
    @DisplayName("Успешное создание заказа.")
    @Description("Тест корректности ответа при успешном создании заказа авторизованным пользователем для эндпойнта /api/orders.")
    @Owner(value = "Кидяев Александр Дмитриевич")
    @Severity(value = SeverityLevel.BLOCKER)
    public void checkResponseAfterCorrectOrderPostTest() {

        UserRegistrationData userRegistrationData = UserRegistrationData.getRandomRegistrationData();
        userMethods.registerNewUser(userRegistrationData);
        String accessToken = userMethods.returnUserAccessToken(UserAuthorizationData.from(userRegistrationData));

        String ingredientId = ingredientsMethods.getIngredientId(1);
        Response orderResponse = orderMethods.postOrder(ingredientId, accessToken);

        orderResponse.then().assertThat()
                .body("name", notNullValue())
                .and()
                .body("success", equalTo(true))
                .and()
                .statusCode(SC_OK);

    }

    @Epic(value = "API Stellar Burgers")
    @Feature(value = "Заказ")
    @Story(value = "Создание заказа")
    @Test
    @DisplayName("Попытка заказа без ингредиентов.")
    @Description("Тест корректности ответа при попытке создания заказа авторизованным пользователем без ингредиентом для эндпойнта /api/orders.")
    @Owner(value = "Кидяев Александр Дмитриевич")
    @Severity(value = SeverityLevel.NORMAL)
    public void checkResponseAfterOrderPostWithoutIngredientsTest() {

        UserRegistrationData userRegistrationData = UserRegistrationData.getRandomRegistrationData();
        userMethods.registerNewUser(userRegistrationData);

        String accessToken = userMethods.returnUserAccessToken(UserAuthorizationData.from(userRegistrationData));
        Response orderResponse = orderMethods.postOrderWithoutIngredients(accessToken);

        orderResponse.then().assertThat()
                .body("message", equalTo("Ingredient ids must be provided"))
                .and()
                .body("success", equalTo(false))
                .and()
                .statusCode(SC_BAD_REQUEST);

    }

    @Epic(value = "API Stellar Burgers")
    @Feature(value = "Заказ")
    @Story(value = "Создание заказа")
    @Test
    @DisplayName("Попытка заказа с некорректным хешем ингредиента.")
    @Description("Тест корректности ответа при попытке создания заказа авторизованным пользователем с некорректным ингредиентом для эндпойнта /api/orders.")
    @Owner(value = "Кидяев Александр Дмитриевич")
    @Severity(value = SeverityLevel.NORMAL)
    public void checkResponseAfterOrderPostWithIncorrectIngredientsTest() {

        UserRegistrationData userRegistrationData = UserRegistrationData.getRandomRegistrationData();
        userMethods.registerNewUser(userRegistrationData);

        String ingredientId = "incorrectId";
        String accessToken = userMethods.returnUserAccessToken(UserAuthorizationData.from(userRegistrationData));
        Response orderResponse = orderMethods.postOrder(ingredientId, accessToken);

        orderResponse.then().assertThat()
                 .statusCode(SC_INTERNAL_SERVER_ERROR);

    }

    @Epic(value = "API Stellar Burgers")
    @Feature(value = "Заказ")
    @Story(value = "Получение заказов пользователя")
    @Test
    @DisplayName("Получение заказов конкретного авторизированного пользователя.")
    @Description("Тест корректности ответа при попытке получения списка заказов конкретного авторизованного пользователя для эндпойнта /api/orders.")
    @Owner(value = "Кидяев Александр Дмитриевич")
    @Severity(value = SeverityLevel.CRITICAL)
    public void checkResponseAfterOrderGetForAuthorizedUserTest() {

        UserRegistrationData userRegistrationData = UserRegistrationData.getRandomRegistrationData();
        userMethods.registerNewUser(userRegistrationData);

        String accessToken = userMethods.returnUserAccessToken(UserAuthorizationData.from(userRegistrationData));
        Response getOrderResponse = orderMethods.getOrders(accessToken);

        getOrderResponse.then().assertThat()
                .body("total", notNullValue())
                .and()
                .body("orders", notNullValue())
                .and()
                .body("success", equalTo(true))
                .and()
                .statusCode(SC_OK);

    }

    @Epic(value = "API Stellar Burgers")
    @Feature(value = "Заказ")
    @Story(value = "Получение заказов пользователя")
    @Test
    @DisplayName("Получение заказов без авторизации.")
    @Description("Тест корректности ответа при попытке получения списка заказов без авторизации для эндпойнта /api/orders.")
    @Owner(value = "Кидяев Александр Дмитриевич")
    @Severity(value = SeverityLevel.NORMAL)
    public void checkResponseAfterOrderGetWithoutAuthorizationTest() {

        Response getOrderResponse = orderMethods.getOrdersWithoutAuthorization();

        getOrderResponse.then().assertThat()
                .body("message", equalTo("You should be authorised"))
                .and()
                .body("success", equalTo(false))
                .and()
                .statusCode(SC_UNAUTHORIZED);

    }

}
