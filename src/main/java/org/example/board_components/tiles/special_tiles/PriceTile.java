package org.example.board_components.tiles.special_tiles;

import org.example.board_components.tiles.Tile;
import org.example.board_components.tiles.standard_tiles.EmptyTile;
import org.example.support.tiles.PriceType;
import org.example.support.tiles.TileType;

public class PriceTile extends Tile {
    private final TileType type = TileType.Price;
    private Tile destination;
    private PriceType price;

    public PriceTile(int row, int col, int number) {
        super(row, col, number);
        setDestination(new EmptyTile(-1,-1,-1));
    }

    @Override public TileType getTileType() {return type;}
    @Override public Tile getDestination() {return this.destination;}
    @Override public void setDestination(Tile destination) {this.destination = new EmptyTile(-1,-1,-1);}

    public void setPrice(PriceType price) {this.price = price;}
    public PriceType getPrice() {return this.price;}

}
