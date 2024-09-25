package org.example.game.commands.special_tiles_actions;

import org.example.game.GameManager;
import org.example.game.commands.Command;
import org.example.support.Player;
import org.example.support.tiles.Cards;

public class DrawAction implements Command {
    private Cards card;
    private GameManager game;
    private Player player;

    public DrawAction(Cards card, GameManager game, Player player) {
        this.card = card;
        this.game = game;
        this.player = player;
    }

    @Override
    public void execute() {
        if(player.getCard() == null){
            switch (card){
                case Dadi -> player.setCard(Cards.Dadi);
                case Molla -> player.setCard(Cards.Molla);
                case Panchina -> player.setCard(Cards.Panchina);
                case Locanda -> player.setCard(Cards.Locanda);
                case DivietoDiSosta -> player.setCard(Cards.DivietoDiSosta);
            }
        }
    }

    @Override
    public int getNextTile() {
        return player.getLastTile();
    }
}

