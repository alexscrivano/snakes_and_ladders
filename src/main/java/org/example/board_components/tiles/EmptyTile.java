package org.example.board_components.tiles;

import org.example.support.TileType;

public class EmptyTile extends Tile{
    private final TileType type = TileType.Empty;
    private Tile destination;
    public EmptyTile(int row, int col, int number) {
        super(row, col, number);
    }

    @Override public TileType getTileType() {return type;}
    // An empty tile doesn't have a destination tile so i set destination to a default empty tile "-1"
    @Override public Tile getDestination() {return new EmptyTile(-1,-1,-1);}
    @Override public void setDestination(Tile destination) {this.destination = new EmptyTile(-1,-1,-1);}
}
