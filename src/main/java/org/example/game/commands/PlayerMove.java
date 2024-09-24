package org.example.game.commands;

import org.example.board_components.tiles.Tile;
import org.example.game.GameManager;
import org.example.game.turns.State;
import org.example.support.Player;
import org.example.support.TileType;

import java.util.Map;

public class PlayerMove implements Command {
    private GameManager game;
    private int roll,player;

    public PlayerMove(GameManager game, int roll, int player) {
        this.game = game;
        this.roll = roll;
        this.player = player;
    }

    @Override
    public void execute() {
        int currentTile = 1;
        Map<Player, State> turns = game.getTurns();
        for(Player p : turns.keySet()) {
            if(p.getPlayerIndex() == player){
                currentTile = p.getLastTile();
                int max = game.getBoard().getBoard().size();
                int nextTile = currentTile + roll;
                if(nextTile > max){
                    nextTile = max - (nextTile - max);
                }
                boolean isEmpty = checkTile(nextTile);
                if(!isEmpty) p.setLastTile(nextTile);
                else{
                    Tile next = game.getBoard().getTile(nextTile);
                    nextTile = next.getDestination().getNumber();
                    p.setLastTile(nextTile);
                }
            }
        }
    }
    private boolean checkTile(int nextTile) {
        Tile t = game.getBoard().getTile(nextTile);
        return t.getTileType().equals(TileType.Empty);
    }
}
