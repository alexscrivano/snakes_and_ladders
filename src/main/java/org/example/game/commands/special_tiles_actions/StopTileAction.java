package org.example.game.commands.special_tiles_actions;

import org.example.game.GameManager;
import org.example.game.commands.Command;
import org.example.game.turns_states.EndedTurnState;
import org.example.game.turns_states.StoppedTurnState;
import org.example.support.Player;
import org.example.support.tiles.Cards;
import org.example.support.tiles.StopType;

public class StopTileAction implements Command {
    private GameManager game;
    private Player player;
    private StopType stop;

    public StopTileAction(GameManager game, Player player, StopType stop) {
        this.game = game;
        this.player = player;
        this.stop = stop;
    }

    @Override
    public void execute() {

        int stops = 0;
        System.out.printf("Player %d landed on %s tile", player.getPlayerIndex(), stop.name());
        switch (stop) {
            case Panchina -> stops = 1;
            case Locanda -> stops = 3;
        }
        if(player.getCard() == Cards.DivietoDiSosta) {
            System.out.printf("Player %d used a %s card, is not stopped anymore", player.getPlayerIndex(),Cards.DivietoDiSosta.name());
            System.out.println();
            player.setCard(null);
            game.getTurns().put(player, new EndedTurnState());
        }else{
            System.out.println();
            System.out.printf("Player %d stopped for %d turns", player.getPlayerIndex(), stops);
            System.out.println();

            game.getTurns().put(player,new StoppedTurnState(stops));
        }

    }

    @Override
    public int getNextTile() {
        return player.getLastTile();
    }
}
