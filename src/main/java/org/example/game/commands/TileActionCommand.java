package org.example.game.commands;

import org.example.board_components.tiles.Tile;
import org.example.board_components.tiles.special_tiles.DrawCardTile;
import org.example.board_components.tiles.special_tiles.PriceTile;
import org.example.board_components.tiles.special_tiles.StopTile;
import org.example.game.GameManager;
import org.example.game.commands.special_tiles_actions.CardTileAction;
import org.example.game.commands.special_tiles_actions.PriceTileAction;
import org.example.game.commands.special_tiles_actions.StopTileAction;
import org.example.support.Player;

public class TileActionCommand implements Command {
    private Tile tile;
    private Command action;
    private GameManager game;
    private Player player;
    private int nextTile;

    public TileActionCommand(Tile tile, GameManager game, Player player, int nextTile, int roll) {
        this.tile = tile;
        this.game = game;
        this.player = player;
        this.nextTile = nextTile;
        switch (tile.getTileType()){
            case Card -> action = new CardTileAction(game,player,nextTile, roll, ((DrawCardTile) tile).drawCard());
            case Stop -> action = new StopTileAction(game,player, ((StopTile)tile).getStop());
            case Price -> action = new PriceTileAction(game,player,nextTile, roll, ((PriceTile) tile).getPrice());
        }
    }

    public int getNextTile() {
        return action.getNextTile();
    }

    @Override
    public void execute() {
        action.execute();
    }

}
