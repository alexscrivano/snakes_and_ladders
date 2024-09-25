package org.example.game.commands;

import org.example.board_components.tiles.Tile;
import org.example.game.GameManager;
import org.example.game.turns_states.*;
import org.example.support.Player;
import org.example.support.tiles.TileType;

import java.util.Map;

public class PlayerMove implements Command {
    private GameManager game;
    private int roll;
    private Player player;
    private int nextNumber;

    public PlayerMove(GameManager game, int roll, Player player) {
        this.game = game;
        this.roll = roll;
        this.player = player;
    }

    @Override
    public void execute() {
        Map<Player, PlayerTurnState> turns = game.getTurns();
        System.out.println("Player " + player.getPlayerIndex() + " rolled a " + roll);

        int currentTile = player.getLastTile();
        int max = game.getMaxTiles();
        int next = currentTile + roll;
        if(next > max){
            next = max - (next - max);
        }

        Tile t = game.getBoard().getTile(next);
        if(t.getTileType() == TileType.Empty){
            player.setLastTile(t.getNumber());
            turns.put(player,new EndedTurnState());
        }else if(t.getTileType() == TileType.Snake || t.getTileType() == TileType.Ladder){
            Tile nt = game.getBoard().getTile(next).getDestination();
            player.setLastTile(nt.getNumber());
            turns.put(player,new EndedTurnState());
        }else{
            // Landed on a special tile
            TileActionCommand command = new TileActionCommand(t,game,player,next,roll);
            command.execute();
            next = command.getNextTile();
            if(next > max){
                next = max - (next - max);
            }

            Tile nt = game.getBoard().getTile(next);
            player.setLastTile(nt.getNumber());

            boolean tileCond = checkTileType(nt); // Check if is not special;
            boolean statusCond = checkState(turns,player); // Check if player is moving or stopped

            while(!tileCond && statusCond){
                command = new TileActionCommand(nt,game,player,next,roll);
                command.execute();
                next = command.getNextTile();
                if(next > max){
                    next = max - (next - max);
                }

                nt = game.getBoard().getTile(next);
                player.setLastTile(nt.getNumber());

                tileCond = checkTileType(nt);
                statusCond = checkState(turns,player);
            }
            if(checkTile(nt.getNumber())){
                player.setLastTile(nt.getNumber());
            }else{
                player.setLastTile(nt.getDestination().getNumber());
            }
            nextNumber = next;
        }
    }

    private boolean checkTileType(Tile tile){
        return tile.getTileType() == TileType.Empty || tile.getTileType() == TileType.Snake || tile.getTileType() == TileType.Ladder;
    }
    private boolean checkState(Map<Player, PlayerTurnState> turns, Player player){
        return turns.get(player) instanceof MovingTurnState;
    }

    @Override
    public int getNextTile() {
        return nextNumber;
    }

    private boolean checkTile(int nextTile) {
        Tile t = game.getBoard().getTile(nextTile);
        return t.getTileType().equals(TileType.Empty);
    }
}
