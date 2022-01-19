import io.qameta.allure.Step;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class OrderMethods extends RestAssuredSpecification {

    private static final String endpointUrl = "/api/orders";
    private static final Map<String, String> body = new HashMap<>();

    @Step("Попытка заказа без авторизации.")
    public Response postOrderWithoutAuthorization (String ingredientId) {

        body.put("ingredients", ingredientId);

        return given()
                .spec(getBaseSpec())
                .body(body)
                .when()
                .post(endpointUrl);

    }

    @Step("Создание заказа.")
    public Response postOrder (String ingredientId, String accessToken) {

        body.put("ingredients", ingredientId);

        return given()
                .spec(getBaseSpec())
                .headers("Authorization", accessToken)
                .body(body)
                //.body("{\"ingredients\": \"" + ingredientId + "\"}")
                .when()
                .post(endpointUrl);

    }

    @Step("Попытка заказа без ингредиентов.")
    public Response postOrderWithoutIngredients (String accessToken) {

        return given()
                .spec(getBaseSpec())
                .headers("Authorization", accessToken)
                .when()
                .post(endpointUrl);

    }

    @Step("Получение заказов.")
    public Response getOrders (String accessToken) {

        return given()
                .spec(getBaseSpec())
                .headers("Authorization", accessToken)
                .when()
                .get(endpointUrl);

    }

    @Step("Получение заказов без авторизации.")
    public Response getOrdersWithoutAuthorization () {

        return given()
                .spec(getBaseSpec())
                .when()
                .get(endpointUrl);

    }

}
