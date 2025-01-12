package org.example.game.model;

public class BoardState
{
    private Tile head;
    private int score;

    public BoardState(Tile head, int score) {
        this.head = head;
        this.score = score;
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
    public Tile getList(Tile head)
    {
        if(head == null)
            return null;
        Tile newHead = new Tile(head.getValue(),head.getColumn(),head.getRow());
        Tile cur = head.getNext();
        Tile traverser = newHead;
        while(cur != null)
        {
            Tile next = new Tile(cur.getValue(),cur.getColumn(),cur.getRow());
            traverser.setNext(next);
            cur = cur.getNext();
            traverser = next;
        }
        return newHead;
    }
}
