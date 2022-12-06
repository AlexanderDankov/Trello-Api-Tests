package com.simbirsoft.tests;

import com.simbirsoft.models.Card;
import com.simbirsoft.models.CheckItem;
import com.simbirsoft.models.CheckList;
import io.qameta.allure.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Story("Добавление, изменение данных карточки и ее элементов")
@Tag("Card")
public class CardTests extends TestBase {

    @BeforeAll
    static void createCard() {
        String name = faker.name().title();

        String listId = stepService.getIdOfBoardListByName(board, "Нужно сделать");

        card = stepService.createCard(listId, name);

        List<Card> cardList = stepService.getCardList(board);

        assertThat(cardList).contains(card);
    }

    @Test
    @DisplayName("Изменение названия карточки")
    @Feature("Добавление и изменение карточек в доске")
    @Owner("Alexander Dankov")
    @Severity(SeverityLevel.CRITICAL)
    void changeNameOfCard() {
        String name = faker.funnyName().name();

        card = stepService.updateCard(card, "name", name);

        assertThat(card.getName()).isEqualTo(name);
    }

    @Test
    @DisplayName("Изменение описания карточки")
    @Feature("Добавление и изменение карточек в доске")
    @Owner("Alexander Dankov")
    @Severity(SeverityLevel.NORMAL)
    void changeDescriptionOfCard() {
        String desc = faker.harryPotter().quote();

        card = stepService.updateCard(card, "desc", desc);

        assertThat(card.getDesc()).isEqualTo(desc);
    }

    @Test
    @DisplayName("Создание чек-листа и проверка его наличия в общем списке")
    @Feature("Добавление и изменение чек-листов и его элементов в карточке")
    @Owner("Alexander Dankov")
    @Severity(SeverityLevel.CRITICAL)
    void createChecklistAndGetIdInCard() {
        String name = faker.funnyName().name();

        CheckList checkList = stepService.createCheckListInCard(card, name);

        card = stepService.getCard(card);

        assertThat(card.getIdChecklists()).contains(checkList.getId());
    }

    @Test
    @DisplayName("Создание пункта в чек-листе и проверка его наличия в чек-листе")
    @Feature("Добавление и изменение чек-листов и его элементов в карточке")
    @Owner("Alexander Dankov")
    @Severity(SeverityLevel.CRITICAL)
    void createCheckItemInChecklist() {
        String name = faker.funnyName().name();

        CheckList checkList = stepService.createCheckListInCard(card, name);

        String title = faker.name().title();

        CheckItem checkItem = stepService.createCheckItemInCheckList(checkList, title);

        checkList = stepService.getChecklistInfo(checkList);

        assertThat(checkList.getCheckItems()).contains(checkItem);
    }

    @Test
    @DisplayName("Перевод всех элементов чек-листа в статус Выполнено")
    @Feature("Добавление и изменение чек-листов и его элементов в карточке")
    @Owner("Alexander Dankov")
    @Severity(SeverityLevel.NORMAL)
    void markAllCheckItemsAsCompleteInChecklist() {
        String name = faker.funnyName().name();
        List<CheckItem> checkItems = new ArrayList<>();

        CheckList checkList = stepService.createCheckListInCard(card, name);

        for (int i = 0; i < 10; i++) {
            String title = faker.name().title();
            CheckItem checkItem = stepService.createCheckItemInCheckList(checkList, title);
            checkItem = stepService.markCheckItemAsComplete(card, checkItem);
            checkItems.add(checkItem);
        }

        assertThat(checkItems).allMatch(checkItem ->
                checkItem.getState().equals("complete"));
    }

    @Test
    @DisplayName("Удаление элемента из чек-листа")
    @Feature("Добавление и изменение чек-листов и его элементов в карточке")
    @Owner("Alexander Dankov")
    @Severity(SeverityLevel.CRITICAL)
    void deleteCheckItemFromChecklist() {
        String name = faker.funnyName().name();

        CheckList checkList = stepService.createCheckListInCard(card, name);

        CheckItem checkItem = stepService.createCheckItemInCheckList(checkList, name);

        stepService.deleteCheckItem(checkItem);

        checkList = stepService.getChecklistInfo(checkList);

        assertThat(checkList.getCheckItems()).isEmpty();

    }

    @Test
    @DisplayName("Добавление, изменение, удаление комментария в карточке")
    @Feature("Добавление и изменение карточек в доске")
    @Owner("Alexander Dankov")
    @Severity(SeverityLevel.MINOR)
    void addNewCommentToCardThenUpdateAndDelete() {
        String text = faker.backToTheFuture().quote();

        String commentId = stepService.addCommentToCard(card, text);

        assertThat(stepService.getCommentsCountInCard(card)).isEqualTo(1);

        text = faker.harryPotter().quote();

        stepService.updateCommentInCard(card, commentId, text);

        stepService.deleteCommentInCard(card, commentId);

        assertThat(stepService.getCommentsCountInCard(card)).isZero();
    }

    @ValueSource(strings = {"Нужно сделать", "В процессе", "Готово"})
    @ParameterizedTest(name = "{0}")
    @DisplayName("Проверка изменения статуса карточки на доски на: ")
    @Feature("Добавление и изменение карточек в доске")
    @Owner("Alexander Dankov")
    @Severity(SeverityLevel.CRITICAL)
    void moveCardByStandardWorkFlowOnBoard(String status) {
        String statusId = stepService.getIdOfBoardListByName(board, status);

        card = stepService.moveCardToStatus(card, statusId);

        assertThat(stepService.getCard(card).getIdList()).isEqualTo(statusId);
    }
}
