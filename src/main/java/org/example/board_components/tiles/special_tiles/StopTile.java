package org.example.board_components.tiles.special_tiles;

import org.example.board_components.tiles.Tile;
import org.example.board_components.tiles.standard_tiles.EmptyTile;
import org.example.support.tiles.StopType;
import org.example.support.tiles.TileType;

public class StopTile extends Tile {
    private final TileType type = TileType.Stop;
    private StopType stop;
    private Tile destination;

    public StopTile(int row, int col, int number) {
        super(row, col, number);
        setDestination(new EmptyTile(-1,-1,-1));
    }

    @Override public TileType getTileType() {return type;}
    @Override public Tile getDestination() {return destination;}
    @Override public void setDestination(Tile destination) {this.destination = new EmptyTile(-1,-1,-1);}

    public void setStop(StopType stop) {this.stop = stop;}
    public StopType getStop() {return stop;}

}
