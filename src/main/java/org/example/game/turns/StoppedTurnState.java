package org.example.game.turns;

import org.example.game.GameManager;
import org.example.support.Player;

import java.util.Map;

public class StoppedTurnState implements PlayerTurnState{
    private int stops;

    public StoppedTurnState(int stops) {
        this.stops = stops - 1;
    }

    @Override
    public void move(GameManager game, Player player) {
        if(stops <= 0){
            Map<Player, PlayerTurnState> turns = game.getTurns();
            game.getTurns().put(player,new EndedTurnState());
        }else{
            stop();
        }
    }
    public void stop(){stops--;}

}
