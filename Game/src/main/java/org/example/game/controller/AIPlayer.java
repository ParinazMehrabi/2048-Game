package org.example.game.controller;

import org.example.game.model.GameBoard;
import org.example.game.model.Tile;

public class AIPlayer
{
    private static AIPlayer AIPlayer;
    private GameBoard board;
    public boolean won = false;

    public AIPlayer() {
    }
    public static AIPlayer getAIPlayer() {
        if (AIPlayer == null)
            AIPlayer = new AIPlayer();
        return AIPlayer;
    }
    public void setBoard(GameBoard gameBoard)
    {
        this.board = gameBoard.cloneBoard();
    }
    public int getEmptyTiles(GameBoard board)
    {
        int emptyTiles = 0;
        for(int row = 0; row < board.getSize(); ++row)
        {
            for(int col=0;col < board.getSize();++col)
            {
                if(!board.isFilled(row,col))
                    ++emptyTiles;
            }
        }
        return emptyTiles;
    }
    public int evaluate(GameBoard board)
    {
        return getEmptyTiles(board)*10 + mergeProb(board) * 5 + board.getScore();
    }
    public GameBoard checkMovements(GameBoard board, char move)
    {
        GameBoard copiedBoard = board.cloneBoard();
        MovementsController movements = new MovementsController(copiedBoard);
        switch (move)
        {
            case 'U' -> movements.moveUp();
            case 'D' -> movements.moveDown();
            case 'L' -> movements.moveLeft();
            case 'R' -> movements.moveRight();
        }
        return copiedBoard;
    }
    public char findBestMove(GameBoard board,int count)
    {
        this.board = board.cloneBoard();
        char[] moves = {'U', 'D', 'L', 'R'};
        char bestMove = 'n';
        int bestScore = Integer.MIN_VALUE;
        for (char move : moves) {
            GameBoard simulatedBoard = checkMovements(this.board, move);
            if (simulatedBoard != null) {
                int score = algorithm(simulatedBoard, count - 1, false);
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = move;
                }
            }
        }
        System.out.println(bestMove);
        return bestMove;
    }
    private int mergeProb(GameBoard board) {
        int mergeProb = 0;
        Tile current = board.getHead();
        while (current != null) {
            Tile rightNeighbor = board.findTile(current.getRow(), current.getColumn() + 1);
            if (rightNeighbor != null) {
                mergeProb -= Math.abs(current.getValue() - rightNeighbor.getValue());
            }
            Tile bottomNeighbor = board.findTile(current.getRow() + 1, current.getColumn());
            if (bottomNeighbor != null) {
                mergeProb -= Math.abs(current.getValue() - bottomNeighbor.getValue());
            }

            current = current.getNext();
        }

        return mergeProb;
    }
    public int algorithm(GameBoard board, int depth, boolean isBest) {
        if (depth == 0 || board.getScore() >= 2048) {
            won = true;
            return evaluate(board);
        }

        if (isBest) {
            int maxEval = Integer.MIN_VALUE;
            for (char move : new char[] {'U', 'D', 'L', 'R'}) {
                GameBoard simulatedBoard = checkMovements(board, move);
                if (simulatedBoard != null) {
                    int eval = algorithm(simulatedBoard, depth - 1, false);
                    maxEval = Math.max(maxEval, eval);
                }
            }
            return maxEval;
        } else {
            int minEval = 0;
            for (int row = 0; row < board.getSize(); row++) {
                for (int col = 0; col < board.getSize(); col++) {
                    if (!board.isFilled(row, col)) {
                        GameBoard secBoard = board.cloneBoard();
                        secBoard.addTile(2, row, col);
                        int evalTwo = algorithm(secBoard, depth - 1, true);
                        GameBoard fourTileBoard = board.cloneBoard();
                        fourTileBoard.addTile(4, row, col);
                        int evalFour = algorithm(fourTileBoard, depth - 1, true);
                        int weightedEval = (int) (0.7 * evalTwo + 0.3 * evalFour);
                        minEval = Math.min(minEval, weightedEval);
                    }
                }
            }
            return minEval;
        }
    }


}
