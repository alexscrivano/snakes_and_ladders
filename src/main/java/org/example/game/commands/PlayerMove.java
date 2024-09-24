package org.example.game.commands;

import org.example.board_components.tiles.EmptyTile;
import org.example.board_components.tiles.LadderTile;
import org.example.board_components.tiles.SnakeTile;
import org.example.board_components.tiles.Tile;
import org.example.game.GameManager;
import org.example.game.turns.PlayerTurnState;
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
        Map<Player, PlayerTurnState> turns = game.getTurns();
        for(Player p : turns.keySet()) {
            if(p.getPlayerIndex() == player){
                System.out.println("Player " + p.getPlayerIndex() + " rolled a " + roll);
                int currentTile = p.getLastTile();
                int max = game.getMaxTiles();
                int next = currentTile + roll;
                if(next > max){
                    next = max - (next - max);
                }
                if(!(game.getBoard().getTile(next) instanceof EmptyTile)){
                    Tile nt = game.getBoard().getTile(next).getDestination();
                    if(nt.getNumber() > 0){
                        Tile pt = game.getBoard().getTile(next);
                        next = nt.getNumber();
                        System.out.println("Player " + p.getPlayerIndex() + " moved on " + pt.getNumber() + " and is on a " + pt.getTileType() + " and is moving to " + nt.getNumber());

                    }
                }
                p.setLastTile(next);
            }
        }
    }

    private boolean checkTile(int nextTile) {
        Tile t = game.getBoard().getTile(nextTile);
        return t.getTileType().equals(TileType.Empty);
    }
}
