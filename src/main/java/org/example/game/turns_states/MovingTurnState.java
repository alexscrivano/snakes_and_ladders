package org.example.game.turns_states;

import org.example.game.commands.PlayerMove;
import org.example.game.GameManager;
import org.example.game.commands.base_command.DiceRollCommand;
import org.example.support.GameType;
import org.example.support.Player;


public class MovingTurnState implements PlayerTurnState {
    private GameManager g;

    @Override
    public void move(GameManager game, Player player) {
        this.g = game;
        int total;
        DiceRollCommand command = new DiceRollCommand();
        command.execute();
        if(game.getDiceNumber() == 2){
            if(game.getGameType() == GameType.MoreRules){
                int max = game.getMaxTiles();
                if(player.getLastTile() <= max - 1 && player.getLastTile() >= max - 6){
                    total = command.getDice1();
                    moveTo(player,total);
                }else{
                    total = command.getResult();
                    int d1 = command.getDice1();
                    int d2 = command.getDice2();
                    moveTo(player,total);
                    if(d1 == d2 && d1 == 6) {
                        moveTo(player,total);
                    }
                }
            }else{
                total = command.getResult();
                moveTo(player,total);
            }
        }
        else {
            total = command.getDice1();
            moveTo(player,total);
        }

    }
    private void moveTo(Player player, int total) {
        PlayerMove playerMove = new PlayerMove(g,total,player);
        playerMove.execute();
    }
}
