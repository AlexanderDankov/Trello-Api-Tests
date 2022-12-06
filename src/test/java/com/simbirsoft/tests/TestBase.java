package com.simbirsoft.tests;

import com.github.javafaker.Faker;
import com.simbirsoft.models.Board;
import com.simbirsoft.models.Card;
import com.simbirsoft.steps.StepService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TestBase {
    public static Faker faker = new Faker();
    protected static StepService stepService = new StepService();
    protected static Board board = new Board();
    protected static Card card = new Card();

    @BeforeAll
    static void createBoard() {
        board = stepService.createBoard();

        List<Board> boardsList = stepService.getBoardsList();

        assertThat(boardsList).contains(board);
    }

    @AfterAll
    static void deleteBoard() {
        stepService.deleteBoard(board);

        List<Board> boardsList = stepService.getBoardsList();

        assertThat(boardsList).doesNotContain(board);
    }
}
