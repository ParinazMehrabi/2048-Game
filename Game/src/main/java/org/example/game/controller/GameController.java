package org.example.game.controller;

import org.example.game.model.GameBoard;

public class GameController
{
    private static GameController gameController;

    private GameBoard gameBoard;

    private GameController() {
    }
    public static GameController getGameController() {
        if (gameController == null)
            gameController = new GameController();
        return gameController;
    }


}
