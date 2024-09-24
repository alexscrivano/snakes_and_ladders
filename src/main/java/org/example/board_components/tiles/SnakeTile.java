package org.example.board_components.tiles;

import org.example.support.TileType;

public class SnakeTile extends Tile {
    private final TileType type = TileType.Snake;
    private EmptyTile destination;

    public SnakeTile(int row, int col, int number) {
        super(row, col, number);
    }

    @Override public TileType getTileType() {return this.type;}
    @Override public Tile getDestination() {return destination;}
    @Override public void setDestination(Tile destination) {this.destination = (EmptyTile) destination;}
}
