import io.qameta.allure.Step;
import static io.restassured.RestAssured.given;

public class IngredientsMethods extends RestAssuredSpecification {

    private static final String endpointUrl = "/api/ingredients";

    @Step("Получение id ингредиента.")
    public String getIngredientId (int massiveId) { //параметр добавил, чтобы была вариативность, чтобы можно было выбирать, при необходимости, разные ингредиенты/элементы массива
        return given()
                .spec(getBaseSpec())
                .get(endpointUrl)
                .then()
                .extract()
                .path("data["+ massiveId + "]._id");
    }
}
