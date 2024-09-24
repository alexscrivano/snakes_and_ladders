package org.example.game.commands.special_tiles;

import org.example.board_components.tiles.Tile;
import org.example.game.GameManager;
import org.example.game.commands.Command;
import org.example.game.turns.StoppedTurnState;
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
        DrawCommand command = new DrawCommand(card,game,player);
        command.execute();

        switch (card) {
            case Panchina -> {
                System.out.printf("Player %d drawed a %s card, stopped for 1 turn", player.getPlayerIndex(),card);
                System.out.println();
                int stops = 0;
                if(player.getCard() == Cards.DivietoDiSosta) {
                    player.setCard(null);
                    System.out.printf("Player %d used a %s card, is not stopped anymore", player.getPlayerIndex(),Cards.DivietoDiSosta.name());
                    System.out.println();
                }else{
                    stops = 1;
                }
                game.getTurns().put(player,new StoppedTurnState(stops));
            }
            case Locanda -> {
                System.out.printf("Player %d drawed a %s card, stopped for 3 turns", player.getPlayerIndex(), card);
                System.out.println();
                int stops = 0;
                if(player.getCard() == Cards.DivietoDiSosta) {
                    player.setCard(null);
                    System.out.printf("Player %d used a %s card, is not stopped anymore", player.getPlayerIndex(),Cards.DivietoDiSosta.name());
                    System.out.println();
                }else{
                    stops = 3;
                }
                game.getTurns().put(player,new StoppedTurnState(stops));
            }
            case Molla -> {
                System.out.printf("Player %d drawed a %s card", player.getPlayerIndex(),card);
                System.out.println();
                PriceTileAction action = new PriceTileAction(game,player,tile,roll, PriceType.Molla);
                player.setCard(null);
                System.out.printf("Player %d used a %s card", player.getPlayerIndex(),Cards.Molla.name());
                System.out.println();
                action.execute();
            }
            case Dadi -> {
                System.out.printf("Player %d drawed a %s card", player.getPlayerIndex(),card);
                System.out.println();
                PriceTileAction action = new PriceTileAction(game,player,tile,roll, PriceType.Dadi);
                player.setCard(null);
                System.out.printf("Player %d used a %s card", player.getPlayerIndex(),Cards.Dadi.name());
                System.out.println();
                action.execute();
            }
            case DivietoDiSosta -> {
                System.out.printf("Player %d drawed a %s card", player.getPlayerIndex(),card);
                System.out.println();
                player.setCard(Cards.DivietoDiSosta);
            }
        }
    }

    @Override
    public int getNextTile() {
        return tile;
    }
}
