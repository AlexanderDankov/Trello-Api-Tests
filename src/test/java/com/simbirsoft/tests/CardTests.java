package com.simbirsoft.tests;

import com.simbirsoft.models.Card;
import com.simbirsoft.models.CheckItem;
import com.simbirsoft.models.CheckList;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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
    void changeNameOfCard() {
        String name = faker.funnyName().name();

        card = stepService.updateCard(card, "name", name);

        assertThat(card.getName()).isEqualTo(name);
    }

    @Test
    void changeDescriptionOfCard() {
        String desc = faker.harryPotter().quote();

        card = stepService.updateCard(card, "desc", desc);

        assertThat(card.getDesc()).isEqualTo(desc);
    }

    @Test
    void createChecklistAndGetIdInCard() {
        String name = faker.funnyName().name();

        CheckList checkList = stepService.createCheckListInCard(card, name);

        card = stepService.getCard(card);

        assertThat(card.getIdChecklists()).contains(checkList.getId());
    }

    @Test
    void createCheckItemInChecklist() {
        String name = faker.funnyName().name();

        CheckList checkList = stepService.createCheckListInCard(card, name);

        String title = faker.name().title();

        CheckItem checkItem = stepService.createCheckItemInCheckList(checkList, title);

        checkList = stepService.getChecklistInfo(checkList);

        assertThat(checkList.getCheckItems()).contains(checkItem);
    }

    @Test
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
    void deleteCheckItemFromChecklist() {
        String name = faker.funnyName().name();

        CheckList checkList = stepService.createCheckListInCard(card, name);

        CheckItem checkItem = stepService.createCheckItemInCheckList(checkList, name);

        stepService.deleteCheckItem(checkItem);

        checkList = stepService.getChecklistInfo(checkList);

        assertThat(checkList.getCheckItems()).isEmpty();

    }

    @Test
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
    @ParameterizedTest(name = "Проверка изменения статуса карточки на доски на: {0}")
    void moveCardByStandardWorkFlowOnBoard(String status) {
        String statusId = stepService.getIdOfBoardListByName(board, status);

        card = stepService.moveCardToStatus(card, statusId);

        assertThat(stepService.getCard(card).getIdList()).isEqualTo(statusId);
    }
}
