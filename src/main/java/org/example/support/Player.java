package org.example.support;

public class Player {
    private int playerIndex;
    private int lastTile;
    public Player(int playerIndex) {
        this.playerIndex = playerIndex;
        this.lastTile = 1;
    }
    public int getPlayerIndex() {return playerIndex;}
    public int getLastTile() {return lastTile;}
    public void setLastTile(int lastTile) {this.lastTile = lastTile;}
}
