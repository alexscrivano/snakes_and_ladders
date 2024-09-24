package org.example.game.turns;

import org.example.game.commands.PlayerMove;
import org.example.game.GameManager;
import org.example.game.commands.DiceRoll;


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
        PlayerMove playerMove = new PlayerMove(g,total,player);
        playerMove.execute();
    }
}
