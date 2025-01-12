package org.example.game.controller;

import org.example.game.model.GameBoard;
import org.example.game.model.Movements;
import org.example.game.model.Tile;

import java.util.ArrayList;
import java.util.List;

public class MovementsController {
    private final GameBoard gameBoard;


    public MovementsController(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    public List<Movements> moveUp() {
        List<Movements> movements = new ArrayList<>();
        for (int col = 0; col < gameBoard.getSize(); col++) {
            Tile lastMerged = null;
            for (int row = 1; row < gameBoard.getSize(); row++) {
                Tile tile = gameBoard.findTile(row, col);
                if (tile != null) {
                    int targetRow = row;
                    while (targetRow > 0 && !gameBoard.isFilled(targetRow - 1, col)) {
                        targetRow--;
                    }

                    Tile mergeCandidate = gameBoard.findTile(targetRow - 1, col);
                    if (mergeCandidate != null && mergeCandidate.getValue() == tile.getValue() && mergeCandidate != lastMerged) {
                        mergeCandidate.setValue(mergeCandidate.getValue() * 2);
                        gameBoard.setScore(gameBoard.getScore() + mergeCandidate.getValue());
                        gameBoard.removeTile(tile);
                        lastMerged = mergeCandidate;
                        movements.add(new Movements(tile, targetRow - 1, col,true));
                    } else if (targetRow != row) {
                        tile.setRow(targetRow);
                        movements.add(new Movements(tile, targetRow, col,false));
                    }
                }
            }
        }
        return movements;
    }

    public List<Movements> moveDown() {
        List<Movements> movements = new ArrayList<>();
        for (int col = 0; col < gameBoard.getSize(); col++) {
            Tile lastMerged = null;
            for (int row = gameBoard.getSize() - 2; row >= 0; row--) {
                Tile tile = gameBoard.findTile(row, col);
                if (tile != null) {
                    int targetRow = row;
                    while (targetRow < gameBoard.getSize() - 1 && !gameBoard.isFilled(targetRow + 1, col)) {
                        targetRow++;
                    }

                    Tile mergeCandidate = gameBoard.findTile(targetRow + 1, col);
                    if (mergeCandidate != null && mergeCandidate.getValue() == tile.getValue() && mergeCandidate != lastMerged) {
                        mergeCandidate.setValue(mergeCandidate.getValue() * 2);
                        gameBoard.setScore(gameBoard.getScore() + mergeCandidate.getValue());
                        gameBoard.removeTile(tile);
                        lastMerged = mergeCandidate;
                        movements.add(new Movements(tile, targetRow + 1, col,true));
                    } else if (targetRow != row) {
                        tile.setRow(targetRow);
                        movements.add(new Movements(tile, targetRow, col,false));
                    }
                }
            }
        }

        return movements;
    }

    public List<Movements> moveLeft() {
        List<Movements> movements = new ArrayList<>();
        for (int row = 0; row < gameBoard.getSize(); row++) {
            Tile lastMerged = null;
            for (int col = 1; col < gameBoard.getSize(); col++) {
                Tile tile = gameBoard.findTile(row, col);
                if (tile != null) {
                    int targetCol = col;
                    while (targetCol > 0 && !gameBoard.isFilled(row, targetCol - 1)) {
                        targetCol--;
                    }

                    Tile mergeCandidate = gameBoard.findTile(row, targetCol - 1);
                    if (mergeCandidate != null && mergeCandidate.getValue() == tile.getValue() && mergeCandidate != lastMerged) {
                        mergeCandidate.setValue(mergeCandidate.getValue() * 2);
                        gameBoard.setScore(gameBoard.getScore() + mergeCandidate.getValue());
                        gameBoard.removeTile(tile);
                        lastMerged = mergeCandidate;
                        movements.add(new Movements(tile, row, targetCol - 1,true));
                    } else if (targetCol != col) {
                        tile.setColumn(targetCol);
                        movements.add(new Movements(tile, row, targetCol,false));
                    }
                }
            }
        }

        return movements;
    }

    public List<Movements> moveRight() {
        List<Movements> movements = new ArrayList<>();
        for (int row = 0; row < gameBoard.getSize(); row++) {
            Tile lastMerged = null;
            for (int col = gameBoard.getSize() - 2; col >= 0; col--) {
                Tile tile = gameBoard.findTile(row, col);
                if (tile != null) {
                    int targetCol = col;
                    while (targetCol < gameBoard.getSize() - 1 && !gameBoard.isFilled(row, targetCol + 1)) {
                        targetCol++;
                    }

                    Tile mergeCandidate = gameBoard.findTile(row, targetCol + 1);
                    if (mergeCandidate != null && mergeCandidate.getValue() == tile.getValue() && mergeCandidate != lastMerged) {
                        mergeCandidate.setValue(mergeCandidate.getValue() * 2);
                        gameBoard.setScore(gameBoard.getScore() + mergeCandidate.getValue());
                        gameBoard.removeTile(tile);
                        lastMerged = mergeCandidate;
                        movements.add(new Movements(tile, row, targetCol + 1,true));
                    } else if (targetCol != col) {
                        tile.setColumn(targetCol);
                        movements.add(new Movements(tile, row, targetCol,false));
                    }
                }
            }
        }

        return movements;
    }
}
