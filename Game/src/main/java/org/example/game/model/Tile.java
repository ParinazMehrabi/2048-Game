package org.example.game.model;

public class Tile
{
    private int value;
    private int column;
    private int row;
    private Tile next;

    public Tile(int value, int column, int row) {
        this.value = value;
        this.column = column;
        this.row = row;
        this.next = null;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public Tile getNext() {
        return next;
    }

    public void setNext(Tile next) {
        this.next = next;
    }
}
