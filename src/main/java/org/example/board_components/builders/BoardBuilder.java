package org.example.board_components.builders;

import org.example.board_components.boards.GameBoard;
import org.example.support.tiles.TileType;
import org.example.board_components.tiles.Tile;

public interface BoardBuilder {
    void buildBoard();
    Tile buildTile(TileType type, int row, int col, int number);
    void buildSnakes(int n, int rows, int snakes);
    void buildLadders(int n, int rows, int ladders);
    void buildSpecials(int n);

    GameBoard getGameBoard();
}
