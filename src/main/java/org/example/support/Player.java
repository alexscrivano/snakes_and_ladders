package org.example.support;

import org.example.support.tiles.Cards;

public class Player {
    private int playerIndex;
    private int lastTile;
    private Cards card;

    public Player(int playerIndex) {
        this.playerIndex = playerIndex;
        this.lastTile = 1;
    }
    public int getPlayerIndex() {return playerIndex;}
    public int getLastTile() {return lastTile;}
    public void setLastTile(int lastTile) {this.lastTile = lastTile;}
    public Cards getCard() {return card;}
    public void setCard(Cards card) {this.card = card;}
}
