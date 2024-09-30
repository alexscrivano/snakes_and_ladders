package org.example.game.commands.special_tiles_actions;

import org.example.game.GameManager;
import org.example.game.commands.Command;
import org.example.game.turns_states.EndedTurnState;
import org.example.game.turns_states.StoppedTurnState;
import org.example.support.Player;
import org.example.support.tiles.Cards;
import org.example.support.tiles.PriceType;

public class CardTileAction implements Command {
    private GameManager game;
    private Player player;
    private Cards card;
    private int tile,roll;

    public CardTileAction(GameManager game, Player player, int nextTile, int roll, Cards card) {
        this.game = game;
        this.player = player;
        this.tile = nextTile;
        this.roll = roll;
        this.card = card;
    }

    @Override
    public void execute() {
        DrawAction command = new DrawAction(card,game,player);
        command.execute();

        switch (card) {
            case Panchina -> {
                int stops = 1;
                game.getTurns().put(player,new StoppedTurnState(stops));
            }

            case Locanda -> {
                int stops = 3;
                game.getTurns().put(player,new StoppedTurnState(stops));
            }

            case Molla -> {
                PriceTileAction action = new PriceTileAction(game,player,tile,roll, PriceType.Molla);
                player.setCard(null);
                action.execute();
            }

            case Dadi -> {
                PriceTileAction action = new PriceTileAction(game,player,tile,roll, PriceType.Dadi);
                player.setCard(null);
                action.execute();
            }

            case DivietoDiSosta -> {
                player.setCard(Cards.DivietoDiSosta);
                if(game.getTurns().get(player) instanceof StoppedTurnState){
                    player.setCard(null);
                    game.getTurns().put(player, new EndedTurnState());
                }
            }
        }
    }

    @Override
    public int getNextTile() {
        return player.getLastTile();
    }
}
