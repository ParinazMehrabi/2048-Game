package org.example.game.model;

public class Movements {
    private final Tile tile;
    private final int targetRow;
    private final int targetCol;
    private boolean isMerging;

    public Movements(Tile tile, int targetRow, int targetCol, boolean isMerging) {
        this.tile = tile;
        this.targetRow = targetRow;
        this.targetCol = targetCol;
        this.isMerging = isMerging;
    }

    public Tile getTile() {
        return tile;
    }

    public int getTargetRow() {
        return targetRow;
    }

    public int getTargetCol() {
        return targetCol;
    }

    public boolean isMerging() {
        return isMerging;
    }
}
