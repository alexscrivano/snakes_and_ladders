package org.example.game.commands.special_tiles_actions;

import org.example.game.GameManager;
import org.example.game.commands.Command;
import org.example.game.commands.base_command.DiceRollCommand;
import org.example.support.Player;
import org.example.support.tiles.PriceType;

public class PriceTileAction implements Command {
    private GameManager game;
    private Player player;
    private int nextTile, roll;
    private PriceType priceType;

    public PriceTileAction(GameManager game, Player player, int nextTile, int roll, PriceType price) {
        this.game = game;
        this.player = player;
        this.nextTile = nextTile;
        this.priceType = price;
        this.roll = roll;
    }

    @Override
    public void execute() {
        DiceRollCommand rollCommand = new DiceRollCommand();
        int num = game.getDiceNumber();
        rollCommand.execute();
        System.out.printf("Player %d landed on a %s tile", player.getPlayerIndex(), priceType);
        System.out.println();
        switch (priceType) {
            case Molla -> {
                nextTile += roll;
                if(nextTile > game.getMaxTiles()) nextTile = game.getMaxTiles() - (nextTile - game.getMaxTiles());
            }
            case Dadi -> {
                int newRoll;
                if (num == 2) newRoll = rollCommand.getResult();
                else newRoll = rollCommand.getDice1();

                nextTile += newRoll;
                if(nextTile > game.getMaxTiles()) {
                    nextTile = game.getMaxTiles() - (nextTile - game.getMaxTiles());
                }
            }
        }
    }

    @Override
    public int getNextTile() {
        return player.getLastTile();
    }

}
