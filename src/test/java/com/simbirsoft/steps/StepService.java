package com.simbirsoft.steps;

import com.github.javafaker.Faker;
import com.simbirsoft.models.*;
import io.qameta.allure.Step;
import org.json.simple.JSONObject;

import java.util.List;

import static com.simbirsoft.specs.Specs.*;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;

public class StepService {

    public static Faker faker = new Faker();

    private static final String organisationId = credentials.organisationId();

    /**
     * Создание доски со случайным названием в указанно рабочем пространстве
     * @return - Созданная доска
     */
    @Step("Создание доски в рабочем пространстве")
    public Board createBoard() {
        JSONObject requestBody = new JSONObject();
        requestBody.put("name", faker.name().title());
        requestBody.put("desc", faker.backToTheFuture().quote());
        requestBody.put("idOrganization", organisationId);

        return given()
                .spec(request)
                .body(requestBody.toJSONString())
                .when()
                .post("boards/")
                .then()
                .log().body()
                .spec(response)
                .body(matchesJsonSchemaInClasspath("schemas/createBoardSchema.json"))
                .extract().body().as(Board.class);
    }

    /**
     * Получение списка всех досок
     * @return - Список всех доступных досок
     */
    @Step("Получение списка всех досок в рабочем пространстве")
    public List<Board> getBoardsList() {
        return  given()
                .spec(request)
                .when()
                .get("organizations/" + organisationId + "/boards")
                .then()
                .spec(response)
                .extract().jsonPath().getList("", Board.class);
    }

    /**
     * Удаление доски
     * @param board - доска, которую необходимо удалить
     */
    @Step("Удаление доски из рабочего пространства")
    public void deleteBoard(Board board) {
        given()
                .spec(request)
                .when()
                .delete("boards/" + board.getId())
                .then()
                .log().body()
                .spec(response);
    }

    /**
     * Изменение цвета доски и проверка его изменения
     * @param board - доска, у которой необходимо изменить цвет
     * @param color - цвет, на котроый необходимо изменить
     */
    @Step("Изменение цвета доски и проверка обновления цвета")
    public void changeAndCheckBoardColor(Board board, String color) {
        given()
                .spec(request)
                .queryParam("prefs/background", color)
                .when()
                .put("boards/" + board.getId())
                .then()
                .log().body()
                .spec(response)
                .body("prefs.background", is(color));
    }

    /**
     * Создание лейбла на доске
     * @param board - доска, для которой необходимо создать лейбл
     * @param name - название создаваемого лейбла
     * @param color - цвет создаваемого лейбла
     * @return - созданный лейбл
     */
    @Step("Создание лейбла на доске")
    public Label createLabelOnBoard(Board board, String name, String color) {
        return given()
                .spec(request)
                .queryParam("name", name)
                .queryParam("color", color)
                .when()
                .post("boards/" + board.getId() + "/labels")
                .then()
                .log().body()
                .spec(response)
                .extract().body().as(Label.class);
    }

    /**
     * Получение информации по созданному лейблу и проверка его параметров
     * @param label - созданный на доске лейбл
     */
    @Step("Получение и проверка созданного лейбла")
    public void getAndCheckLabel(Label label) {
        given()
                .spec(request)
                .when()
                .get("labels/" + label.getId())
                .then()
                .log().body()
                .spec(response)
                .body("id", is(label.getId()))
                .body("idBoard", is(label.getIdBoard()))
                .body("name", is(label.getName()))
                .body("color", is(label.getColor()));
    }

    /**
     * Создание нового списка на доске
     * @param board - доска, для которой нужно создать список
     * @param name - название для списка
     * @return - идентификатор созданного списка
     */
    @Step("Создание нового списка на доске")
    public String createListOnBoard(Board board, String name) {
        return  given()
                .spec(request)
                .queryParam("name", name)
                .when()
                .post("boards/" + board.getId() + "/lists")
                .then()
                .log().body()
                .spec(response)
                .body("name", is(name))
                .extract()
                .path("id");
    }

    /**
     * Проверка наличия списка по его идентификатору в общем массиве списков доски
     * @param board - доска, которй принадлежит список
     * @param listId - идентификатор списка
     */
    @Step("Проверка наличия списка в массиве всех списков доски")
    public void checkListOnBoardById(Board board, String listId) {
        given()
                .spec(request)
                .when()
                .get("boards/" + board.getId() + "/lists")
                .then()
                .spec(response)
                .body("findAll{it}.id.flatten()", hasItem(listId));
    }

    /**
     * Добавление участника к доске
     * @param board - доска, для которой необходимо добавить участника
     * @param name - имя участника
     * @param email - email участника
     * @return - список участников, привязанных к доске
     */
    @Step("Добавление участника к доске")
    public List<Member> addMemberToBoard(Board board, String name, String email) {
        JSONObject requestBody = new JSONObject();
        requestBody.put("fullName", name);

        return given()
                .spec(request)
                .queryParam("email", email)
                .body(requestBody)
                .when()
                .put("boards/" + board.getId() + "/members")
                .then()
                .log().body()
                .spec(response)
                .extract().jsonPath().getList("members", Member.class);
    }

    /**
     * Получение конкретного участника из списка по имени
     * @param memberList - список участников доски
     * @param name - имя участника, которого необходимо получить
     * @return - участник
     */
    @Step("Получение участника по его имени")
    public Member getMemberFromListByName(List<Member> memberList, String name) {
        return memberList.stream()
                .filter(ml -> ml.getFullName().equals(name))
                .findFirst()
                .get();
    }

    /**
     * Проверка данных участника
     * @param member - участник
     */
    @Step("Проверка данных участника")
    public void checkCreatedMemberData(Member member) {
        given()
                .spec(request)
                .when()
                .get("members/" + member.getId())
                .then()
                .spec(response)
                .body("id", is(member.getId()))
                .body("fullName", is(member.getFullName()))
                .body("confirmed", is(false))
                .body("memberType", is("ghost"));
    }

    /**
     * Получение идентификатора списка по его названию
     * @param board - доска, в которой находится список
     * @param name - название списка
     * @return - идентификатор списка
     */
    @Step("Получение идентификатора списка по его названию")
    public String getIdOfBoardListByName(Board board, String name) {
        List<BoardList> boardsList = given()
                .spec(request)
                .when()
                .get("boards/" + board.getId() + "/lists")
                .then()
                .spec(response)
                .extract().jsonPath().getList("", BoardList.class);

        return boardsList.stream()
                .filter(l -> l.getName().equals(name))
                .findFirst()
                .get()
                .getId();
    }

    /**
     * Создание карточки
     * @param idList - идентификатор списка, в котром необходимо создать карточку
     * @param name - название карточки
     * @return - созданная карточка
     */
    @Step("Создание карточки в списке")
    public Card createCard(String idList, String name) {
        return given()
                .spec(request)
                .queryParam("idList", idList)
                .queryParam("name", name)
                .when()
                .post("cards")
                .then()
                .log().body()
                .spec(response)
                .body(matchesJsonSchemaInClasspath("schemas/createCardSchema.json"))
                .extract().body().as(Card.class);
    }

    /**
     * Получение списка всех карточек на доске
     * @param board - доска, у которой необходимо получить карточки
     * @return - список карточек
     */
    @Step("Получение списка всех карточек на доске")
    public List<Card> getCardList(Board board) {
        return given()
                .spec(request)
                .when()
                .get("boards/" + board.getId() + "/cards")
                .then()
                .log().body()
                .spec(response)
                .extract().jsonPath().getList("", Card.class);
    }

    /**
     * Получение данных карточки по ее идентификатору
     * @param card - карточка, которую необходимо получить
     * @return - данные по карточке
     */
    @Step("Получение данных по карточке")
    public Card getCard(Card card) {
        return given()
                .spec(request)
                .when()
                .get("cards/" + card.getId())
                .then()
                .log().body()
                .spec(response)
                .extract().body().as(Card.class);
    }

    /**
     * Изменение параметров карточки
     * @param card - карточка, которую необходимо изменить
     * @param parameterName - название параметра
     * @param parameterValue - значение параметра
     * @return - измененная карточка
     */
    @Step("Изменение параметров карточки")
    public Card updateCard(Card card, String parameterName, String parameterValue) {
        return given()
                .spec(request)
                .queryParam(parameterName, parameterValue)
                .when()
                .put("cards/" + card.getId())
                .then()
                .log().body()
                .spec(response)
                .extract().body().as(Card.class);
    }

    /**
     * Создание чек-листа в карточке
     * @param card - карточка, для которой необходимо создать чек-лист
     * @param name - название чек-листа
     * @return - созданный чек-лист
     */
    @Step("Создание чек-листа в карточке")
    public CheckList createCheckListInCard(Card card, String name) {
        return given()
                .spec(request)
                .queryParam("name", name)
                .when()
                .post("cards/" + card.getId() + "/checklists")
                .then()
                .log().body()
                .spec(response)
                .extract().body().as(CheckList.class);
    }

    /**
     * Добавление пункта в чек-лист
     * @param checkList - чек-лист, для которого необходимо добавить пункт
     * @param name - название пункта
     * @return - пункт чек-листа
     */
    @Step("Добавление пункта в чек-лист")
    public CheckItem createCheckItemInCheckList(CheckList checkList, String name) {
        return given()
                .spec(request)
                .queryParam("name", name)
                .when()
                .post("checklists/" + checkList.getId() + "/checkitems")
                .then()
                .log().body()
                .spec(response)
                .body("name", is(name))
                .extract().body().as(CheckItem.class);
    }

    /**
     * Получение данных по чек-листу по его идентификатору
     * @param checkList - чек-лист, который необходимо получить
     * @return - данные по чек-листу
     */
    public CheckList getChecklistInfo(CheckList checkList) {
        return given()
                .spec(request)
                .when()
                .get("checklists/" + checkList.getId())
                .then()
                .spec(response)
                .extract().body().as(CheckList.class);
    }

    /**
     * Изменение статуса пункта чек-листа на Выполнено
     * @param card - карточка, в которой находится чек-лист
     * @param checkItem - пункт чек-листа, который необходимо изменить
     * @return измененный пункт чек-листа
     */
    @Step("Изменение статуса пункта чек-листа на Выполнено")
    public CheckItem markCheckItemAsComplete(Card card, CheckItem checkItem) {
        return given()
                .spec(request)
                .queryParam("state", "complete")
                .when()
                .put("cards/" + card.getId() + "/checklist/" + checkItem.getIdChecklist() + "/checkitem/" + checkItem.getId())
                .then()
                .spec(response)
                .extract().body().as(CheckItem.class);
    }

    /**
     * Удаление пункта чек-листа
     * @param checkItem - пункт чек-листа, который необходимо удалить
     */
    @Step("Удаление пункта чек-листа")
    public void deleteCheckItem(CheckItem checkItem) {
        given()
                .spec(request)
                .when()
                .delete("checklists/" + checkItem.getIdChecklist() + "/checkitems/" + checkItem.getId())
                .then()
                .spec(response);
    }

    /**
     * Добавление комментария к карточке
     * @param card - карточка, для которой необходимо добавить комментарий
     * @param text - текст комментария
     * @return - идентификатор созданного комментария
     */
    @Step("Добавление комментария к карточке")
    public String addCommentToCard(Card card, String text) {
        return given()
                .spec(request)
                .queryParam("text", text)
                .when()
                .post("cards/" + card.getId() + "/actions/comments")
                .then()
                .log().body()
                .spec(response)
                .body("data.text", is(text))
                .extract()
                .path("id");
    }

    /**
     * Получение количества комментариев у карточки
     * @param card - карточка, у которой необходимо получить количество комментариев
     * @return - количество комментариев у карточки
     */
    @Step("Получение количества комментариев у карточки")
    public int getCommentsCountInCard(Card card) {
        return given()
                .spec(request)
                .when()
                .get("cards/" + card.getId())
                .then()
                .spec(response)
                .extract()
                .path("badges.comments");
    }

    /**
     * Изменение комментария у карточки
     * @param card - карточка, у которой необходимо изменить комментарий
     * @param commentId - идентификатор комментария
     * @param text - текст комментария
     */
    @Step("Изменение комментария у карточки")
    public void updateCommentInCard(Card card, String commentId, String text) {
        given()
                .spec(request)
                .queryParam("text", text)
                .when()
                .put("cards/" + card.getId() + "/actions/" + commentId + "/comments")
                .then()
                .spec(response)
                .body("data.text", is(text));
    }

    /**
     * Удаление комментария к карточке
     * @param card - карточка, у которой необходимо удалить комментарий
     * @param commentId - идентификатор комментария для удаления
     */
    @Step("Удаление комментария к карточке")
    public void deleteCommentInCard(Card card, String commentId) {
        given()
                .spec(request)
                .when()
                .delete("cards/" + card.getId() + "/actions/" + commentId + "/comments")
                .then()
                .spec(response);
    }

    /**
     * Изменение статуса карточки (Нужно сделать, В процессе, Выполнено и т.д.)
     * @param card - карточка, у которой необходимо изменить статус
     * @param idStatus - идентификатор статуса
     * @return - измененная карточка
     */
    @Step("Изменение статуса у карточки")
    public Card moveCardToStatus(Card card, String idStatus) {
        return updateCard(card, "idList", idStatus);
    }

    /**
     * Создание организации (рабочего пространства)
     * @param displayName - отображаемое название
     * @return - созданная организация
     */
    @Step("Создание организации")
    public Organisation createOrganisation(String displayName) {
        return given()
                .spec(request)
                .queryParam("displayName", displayName)
                .when()
                .post("organizations")
                .then()
                .log().body()
                .spec(response)
                .body(matchesJsonSchemaInClasspath("schemas/createOrganisationSchema.json"))
                .extract().body().as(Organisation.class);
    }

    /**
     * Изменение данных организации (рабочего пространства)
     * @param organisation - организация, которую необходимо изменить
     * @param parameter - название параметра
     * @param value - значение параметра
     * @return - измененная организация
     */
    @Step("Изменение данных организации")
    public Organisation updateOrganisation(Organisation organisation, String parameter, String value) {
        return given()
                .spec(request)
                .queryParam(parameter, value)
                .when()
                .put("organizations/" + organisation.getId())
                .then()
                .log().body()
                .spec(response)
                .extract().body().as(Organisation.class);
    }

    /**
     * Удаление организации (рабочего пространства)
     * @param organisation - организация, которую необходимо удалить
     */
    @Step("Удаление организации")
    public void deleteOrganisation(Organisation organisation) {
        given()
                .spec(request)
                .queryParam("prefs/permissionLevel", "public")
                .when()
                .put("organizations/" + organisation.getId())
                .then()
                .log().body()
                .spec(response);

        given()
                .spec(request)
                .when()
                .delete("organizations/" + organisation.getId())
                .then()
                .spec(response);
    }

    /**
     * Получение данных организации, в случае если организация не найдена (проверка 404 статуса)
     * @param organisation - организация
     */
    @Step("Получение данных по несуществующей организации")
    public void getOrganisationNotFound(Organisation organisation) {
        given()
                .spec(request)
                .when()
                .get("organizations/" + organisation.getId())
                .then()
                .statusCode(404);
    }

    /**
     * Добавление участника к организации
     * @param organisation - организация, для которой необходимо добавить участника
     * @param email - email участника
     * @param fullName - имя участника
     */
    @Step("Добавление участника к организации")
    public void addMemberToOrganisation(Organisation organisation, String email, String fullName) {
        given()
                .spec(request)
                .queryParam("email", email)
                .queryParam("fullName", fullName)
                .when()
                .put("organizations/" + organisation.getId() + "/members")
                .then()
                .log().body()
                .spec(response);
    }

    /**
     * Проверка нахождения участника в организации
     * @param organisation - организация, в которой должен находиться участник
     * @param fullName - имя участника
     */
    @Step("Проверка нахождения участника в организации")
    public void checkMemberInOrganisation(Organisation organisation, String fullName) {
        given()
                .spec(request)
                .when()
                .get("organizations/" + organisation.getId() + "/members")
                .then()
                .log().body()
                .spec(response)
                .body("findAll{it}.fullName.flatten()", hasItem(fullName));
    }
}
