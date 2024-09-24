package org.example.board_components.tiles.standard;

import org.example.board_components.tiles.Tile;
import org.example.support.tiles.TileType;

public class LadderTile extends Tile {
    private final TileType type = TileType.Ladder;
    private EmptyTile destination;

    public LadderTile(int row, int col, int number) {
        super(row, col, number);
    }

    @Override public TileType getTileType() {return this.type;}
    @Override public Tile getDestination() {return this.destination;}
    @Override public void setDestination(Tile destination) {this.destination = (EmptyTile) destination;}
}
