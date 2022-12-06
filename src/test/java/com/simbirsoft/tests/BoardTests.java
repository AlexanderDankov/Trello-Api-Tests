package com.simbirsoft.tests;

import com.github.javafaker.Faker;
import com.simbirsoft.models.Board;
import com.simbirsoft.models.Label;
import com.simbirsoft.models.Member;
import com.simbirsoft.steps.StepService;
import org.json.simple.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static com.simbirsoft.specs.Specs.request;
import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.request;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

public class BoardTests extends TestBase {

    @ValueSource(strings = {"blue", "orange", "green", "red", "purple", "pink", "lime", "sky", "grey"})
    @ParameterizedTest(name = "Проверка изменения цвета доски на {0}")
    void changeDefaultColorOfBoard(String color) {
        stepService.changeAndCheckBoardColor(board, color);
    }

    @CsvSource({
            "firstLabel, blue",
            "secondLabel, green",
            "thirdLabel, red"
    })
    @ParameterizedTest(name = "Проверка создания лэйбла для доски с названием и цветом: {0}")
    void createAndGetLabelOnBoard(String title, String color) {
        Label label = stepService.createLabelOnBoard(board, title, color);

        stepService.getAndCheckLabel(label);
    }

    @Test
    void createAndGetListOnBoard() {
        String name = faker.name().title();

        String listId = stepService.createListOnBoard(board, name);

        stepService.checkListOnBoardById(board, listId);
    }

    @Test
    void addAndGetMemberOfBoard() {
        String email = faker.internet().emailAddress();
        String name = faker.name().fullName();

        List<Member> members = stepService.addMemberToBoard(board, name, email);

        Member createdMemberFromList = stepService.getMemberFromListByName(members, name);

        stepService.checkCreatedMemberData(createdMemberFromList);
    }
}
