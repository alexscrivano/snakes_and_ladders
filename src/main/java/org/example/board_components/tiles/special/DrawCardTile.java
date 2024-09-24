package org.example.board_components.tiles.special;

import org.example.board_components.tiles.Tile;
import org.example.board_components.tiles.standard.EmptyTile;
import org.example.support.tiles.Cards;
import org.example.support.tiles.TileType;

import java.util.Random;

public class DrawCardTile extends Tile {
    private final TileType tileType = TileType.Card;
    private Tile destination;

    public DrawCardTile(int row, int col, int number) {
        super(row, col, number);
        setDestination(new EmptyTile(-1,-1,-1));
    }

    @Override public TileType getTileType() {return this.tileType;}
    @Override public Tile getDestination() {return this.destination;}
    @Override public void setDestination(Tile destination) {this.destination = new EmptyTile(-1,-1,-1);}

    public Cards drawCard() {
        Random rand = new Random();
        int card = rand.nextInt(Cards.values().length);
        return Cards.values()[card];
    }
}
