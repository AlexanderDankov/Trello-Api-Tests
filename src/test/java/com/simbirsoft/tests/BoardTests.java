package com.simbirsoft.tests;

import com.simbirsoft.models.Label;
import com.simbirsoft.models.Member;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

@Story("Добавление, изменение, удаление элементов доски в рабочем пространстве")
@Tag("Board")
public class BoardTests extends TestBase {

    @ValueSource(strings = {"blue", "orange", "green", "red", "purple", "pink", "lime", "sky", "grey"})
    @ParameterizedTest(name = "{0}")
    @DisplayName("Проверка изменения цвета доски на: ")
    @Feature("Изменение параметров доски")
    @Owner("Alexander Dankov")
    @Severity(SeverityLevel.NORMAL)
    void changeDefaultColorOfBoard(String color) {
        stepService.changeAndCheckBoardColor(board, color);
    }

    @CsvSource({
            "firstLabel, blue",
            "secondLabel, green",
            "thirdLabel, red"
    })
    @ParameterizedTest(name = "{0}")
    @DisplayName("Проверка создания лэйбла для доски с названием и цветом: ")
    @Feature("Изменение параметров доски")
    @Owner("Alexander Dankov")
    @Severity(SeverityLevel.NORMAL)
    void createAndGetLabelOnBoard(String title, String color) {
        Label label = stepService.createLabelOnBoard(board, title, color);

        stepService.getAndCheckLabel(label);
    }

    @Test
    @DisplayName("Создание списка на доске и получение его данных в общем списке")
    @Feature("Добавление элементов доски")
    @Owner("Alexander Dankov")
    @Severity(SeverityLevel.NORMAL)
    void createAndGetListOnBoard() {
        String name = faker.name().title();

        String listId = stepService.createListOnBoard(board, name);

        stepService.checkListOnBoardById(board, listId);
    }

    @Test
    @DisplayName("Добавление пользователя к доске и получение его данных в общем списке")
    @Feature("Добавление пользователей доски")
    @Owner("Alexander Dankov")
    @Severity(SeverityLevel.CRITICAL)
    void addAndGetMemberOfBoard() {
        String email = faker.internet().emailAddress();
        String name = faker.name().fullName();

        List<Member> members = stepService.addMemberToBoard(board, name, email);

        Member createdMemberFromList = stepService.getMemberFromListByName(members, name);

        stepService.checkCreatedMemberData(createdMemberFromList);
    }
}
