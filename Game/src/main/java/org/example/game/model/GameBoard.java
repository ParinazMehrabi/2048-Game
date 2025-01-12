package org.example.game.model;

import org.example.game.view.BoardController;

import java.util.Random;
import java.util.Stack;

public class GameBoard {
    private Tile head;
    private int score;
    private static final int BOARD_SIZE = 4;
    private final int stackLimit = 5;
    private Stack<BoardState> undoStack = new Stack<>();
    private Stack<BoardState> redoStack = new Stack<>();
    private static BoardController boardController;
    private int emptyTiles;

    public GameBoard(BoardController boardController)
    {
        this.head = null;
        this.score = 0;
        GameBoard.boardController = boardController;
    }

    public Tile getHead() {
        return head;
    }

    public void setHead(Tile head) {
        this.head = head;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getSize() {
        return BOARD_SIZE;
    }
    public void resetBoard() {
        head = null;
        score = 0;
    }

    public void addTile(int value, int row, int col) {
        Tile newTile = new Tile(value, col, row);
        if (head == null) {
            head = newTile;
        } else {
            Tile current = head;
            while (current.getNext() != null) {
                current = current.getNext();
            }
            current.setNext(newTile);
        }
    }
    public void removeTile(Tile tileToRemove) {
        if (head == null || tileToRemove == null) {
            return;
        }

        if (head == tileToRemove) {
            head = head.getNext();
            return;
        }

        Tile current = head;
        while (current.getNext() != null) {
            if (current.getNext() == tileToRemove) {
                current.setNext(current.getNext().getNext());
                return;
            }
            current = current.getNext();
        }
    }
    public boolean isFilled(int row, int col) {
        return findTile(row, col) != null;
    }

    public Tile findTile(int row, int col) {
        Tile current = head;
        while (current != null) {
            if (current.getRow() == row && current.getColumn() == col) {
                return current;
            }
            current = current.getNext();
        }
        return null;
    }

    public void addRandomTile() {
        Random random = new Random();
        int value = random.nextInt(10) < 7 ? 2 : 4; // 70% chance for 2, 30% for 4
        int row, col;

        do {
            row = random.nextInt(BOARD_SIZE);
            col = random.nextInt(BOARD_SIZE);
        } while (isFilled(row, col));

        addTile(value, row, col);
    }
    public void saveState() {
        if (undoStack.size() == stackLimit) {
            undoStack.remove(0);
        }
        undoStack.push(new BoardState(copyLinkedList(head), score));
        redoStack.clear();
        System.out.println("saved state: Head = " + head + ", Score = " + score);
    }
    private Tile copyLinkedList(Tile head) {
        if (head == null) return null;
        Tile copiedHead = new Tile(head.getValue(), head.getColumn(), head.getRow());
        Tile currentOriginal = head.getNext();
        Tile currentCopy = copiedHead;
        while (currentOriginal != null) {
            Tile newTile = new Tile(currentOriginal.getValue(), currentOriginal.getColumn(), currentOriginal.getRow());
            currentCopy.setNext(newTile);
            currentCopy = newTile;
            currentOriginal = currentOriginal.getNext();
        }

        return copiedHead;
    }

    private void restoreState(BoardState state) {
        head = state.getList(state.getHead());
        score = state.getScore();
        System.out.println("Restoring state: Head = " + head + ", Score = " + score);
        boardController.updateUI();
    }


    public boolean undo() {
        if (undoStack.isEmpty()) {
            return false;
        }
        redoStack.push(new BoardState(head, score));
        BoardState lastState = undoStack.pop();
        restoreState(lastState);
        return true;
    }
    public boolean redo()
    {
        if(redoStack.isEmpty())
            return false;
        undoStack.push(new BoardState(head, score));
        BoardState nextState = redoStack.pop();
        restoreState(nextState);
        return true;
    }
    public boolean hasWon()
    {
        return score >= 2048;
    }
    public boolean canMove()
    {
        for(int row=0;row<BOARD_SIZE;++row)
        {
            for(int col=0;col<BOARD_SIZE;++col)
            {
                Tile cur = this.findTile(row, col);
                if(cur != null)
                    if((col < this.getSize() - 1 && cur.getValue() == this.findTile(row, col + 1).getValue()) || ((row > 0 && cur.getValue() == this.findTile(row - 1, col).getValue()) ||
                            (row < this.getSize() - 1 && cur.getValue() == this.findTile(row + 1, col).getValue()) ||
                            (col > 0 && cur.getValue() == this.findTile(row, col - 1).getValue())))
                        return true;
            }
        }
        return false;
    }
    private boolean hasEmptyTile() {
        for (int row = 0; row < this.getSize(); row++) {
            for (int col = 0; col < this.getSize(); col++) {
                if (!this.isFilled(row, col)) {
                    return true;
                }
            }
        }
        return false;
    }
    public boolean GameOver() {
        return !hasEmptyTile() && !canMove();
    }
    public GameBoard cloneBoard() {
        GameBoard clonedBoard = new GameBoard(null);
        clonedBoard.head = copyLinkedList(this.head);
        clonedBoard.score = this.score;
        return clonedBoard;
    }


}
