package org.example.game.commands;

import org.example.board_components.tiles.standard.EmptyTile;
import org.example.board_components.tiles.Tile;
import org.example.board_components.tiles.standard.LadderTile;
import org.example.board_components.tiles.standard.SnakeTile;
import org.example.game.GameManager;
import org.example.game.commands.standard_tiles.DiceRollCommand;
import org.example.game.turns.*;
import org.example.support.GameType;
import org.example.support.Player;
import org.example.support.tiles.TileType;

import java.util.Map;

public class PlayerMove implements Command {
    private GameManager game;
    private int roll;
    private Player player;
    private DiceRollCommand rollCommand = new DiceRollCommand();
    private boolean doubleSix;
    private int nextNumber;

    public PlayerMove(GameManager game, int roll, Player player, boolean doubleSix) {
        this.game = game;
        this.roll = roll;
        this.player = player;
        this.doubleSix = doubleSix;
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
            if(doubleSix && game.getGameType() == GameType.MoreRules && game.getDiceNumber() > 1) turns.get(player).move(game,player);
            else {
                player.setLastTile(t.getNumber());
                turns.put(player,new EndedTurnState());
            }
        }else if(t.getTileType() == TileType.Snake || t.getTileType() == TileType.Ladder){
            Tile nt = game.getBoard().getTile(next).getDestination();
            if(doubleSix && game.getGameType() == GameType.MoreRules && game.getDiceNumber() > 1) turns.get(player).move(game,player);
            else {
                player.setLastTile(nt.getNumber());
                turns.put(player,new EndedTurnState());
            }
        }else{
            System.out.println(next);
            TileActionCommand command = new TileActionCommand(t,game,player,next,roll);
            command.execute();
            next = command.getNextTile();
            if(next > max){
                next = max - (next - max);
            }
            if(next == 0){
                next = 1;
            }
            System.out.println(next);
            Tile nt = game.getBoard().getTile(next);
            if(nt.getTileType() == TileType.Empty){
                if(doubleSix && game.getGameType() == GameType.MoreRules && game.getDiceNumber() == 2) turns.get(player).move(game,player);
                else {
                    player.setLastTile(nt.getNumber());
                    turns.put(player,new EndedTurnState());
                }
            }else{
                turns.get(player).move(game,player);
            }
            nextNumber = next;
        }
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
