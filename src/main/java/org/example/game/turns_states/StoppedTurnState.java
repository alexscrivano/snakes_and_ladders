package org.example.game.turns_states;

import org.example.game.GameManager;
import org.example.game.commands.Command;
import org.example.support.Player;


public class StoppedTurnState implements PlayerTurnState{
    private int stops;

    public StoppedTurnState(int stops) {
        this.stops = stops - 1;
    }

    @Override
    public void move(GameManager game, Player player, Command command) {
        if(stops <= 0){
            game.getTurns().put(player,new EndedTurnState());
        }else{
            stop();
        }
    }
    public int getStops() {return stops;}
    public void stop(){stops--;}

}
