package org.example.board_components.tiles.standard_tiles;

import org.example.board_components.tiles.Tile;
import org.example.support.tiles.TileType;

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
