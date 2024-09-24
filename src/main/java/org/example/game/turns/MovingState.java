package org.example.game.turns;

import org.example.board_components.tiles.Tile;
import org.example.support.TileType;
import org.example.game.GameManager;
import org.example.support.Player;
import org.example.game.commands.DiceRoll;

import java.util.Map;

public class MovingState implements State{
    private GameManager g;

    @Override
    public void move(GameManager game, int player) {
        this.g = game;
        DiceRoll command = new DiceRoll();
        command.execute();
        int total = command.getTotal();
        moveTo(player,total);
        g.setState(player,new EndedState());
    }
    private void moveTo(int player, int total) {
        int current = 1;
        Map<Player,State> turns = g.getTurns();
        for(Player p : turns.keySet()) {
            if(p.getPlayerIndex() == player){
                current = p.getLastTile();
                int maxTiles = g.getBoard().getBoard().size();
                int newTile = current + total;
                if((current + total) > maxTiles){
                    newTile = maxTiles - (newTile - maxTiles);
                }
                boolean isEmpty = check(newTile);
                if(isEmpty) p.setLastTile(newTile);
                else {
                    //Need to add a check for special tiles TODO
                    newTile = g.getBoard().getTile(newTile).getDestination().getNumber();
                    p.setLastTile(newTile);
                }
            }
        }
    }
    private boolean check(int tile) {
        Tile t = g.getBoard().getTile(tile);
        return t.getTileType().equals(TileType.Empty);
    }
}
