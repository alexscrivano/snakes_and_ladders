package org.example.board_components.tiles;

import java.io.Serializable;

public abstract class Tile implements Serializable {
    private final int row,col,number;
    private boolean occupied;

    public Tile(int row, int col, int number) {
        this.row = row;
        this.col = col;
        this.number = number;
        this.occupied = false;
    }
    public int getRow() {return row;}
    public int getCol() {return col;}
    public int getNumber() {return number;}
    public boolean isOccupied() {return occupied;}
    public void setOccupied(boolean occupied) {this.occupied = occupied;}

    public abstract TileType getTileType();
    public abstract Tile getDestination();
    public abstract void setDestination(Tile destination);
}
