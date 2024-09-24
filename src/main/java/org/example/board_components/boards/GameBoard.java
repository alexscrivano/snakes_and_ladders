package org.example.board_components.boards;

import org.example.board_components.tiles.Tile;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class GameBoard implements Serializable {
    private Map<Integer,Tile> board;

    public GameBoard() {board = new HashMap<>();}

    public void addTile(Tile t){board.put(t.getNumber(),t);}

    public Tile getTile(int number){return board.get(number);}

    public Tile getTile(int row,int col){
        for(Tile t : board.values()){
            if(t.getRow() == row && t.getCol() == col){return t;}
        }
        return null;
    }

    public Map<Integer,Tile> getBoard(){return board;}
}
