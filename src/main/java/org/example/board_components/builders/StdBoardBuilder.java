package org.example.board_components.builders;

import org.example.board_components.boards.GameBoard;
import org.example.board_components.tiles.TileType;
import org.example.board_components.tiles.EmptyTile;
import org.example.board_components.tiles.LadderTile;
import org.example.board_components.tiles.SnakeTile;
import org.example.board_components.tiles.Tile;

import java.util.ArrayList;
import java.util.Random;

public class StdBoardBuilder implements BoardBuilder { // Build the game board with standard rules
    private GameBoard board;

    @Override
    public void buildBoard() {
        board = new GameBoard();
    }

    @Override
    public Tile buildTile(TileType type, int row, int col, int number) {
        Tile t;
        switch (type) {
            case Snake -> t = new SnakeTile(row,col, number);
            case Ladder -> t = new LadderTile(row,col, number);
            default -> t = new EmptyTile(row,col, number);
        }
        board.addTile(t);
        return t;
    }

    @Override
    public void buildSnakes(int n, int rows, int snakes) {
        Random rand = new Random();
        ArrayList<EmptyTile> emptyTiles = new ArrayList<>();

        while (snakes > 0){
            int i = rand.nextInt(n-1)+1;
            Tile t = board.getTile(i);
            if(t instanceof EmptyTile && t.getRow() < rows-1){
                SnakeTile st = (SnakeTile) buildTile(TileType.Snake, t.getRow(),t.getCol(),t.getNumber());
                board.addTile(st);
                snakes--;
            }
        }
        for(Tile t : board.getBoard().values()){
            if(t instanceof SnakeTile){
                for(Tile tUp : board.getBoard().values()){
                    if(tUp instanceof EmptyTile && tUp.getRow() > t.getRow() && !tUp.isOccupied()) emptyTiles.add((EmptyTile) tUp);
                }

                int x = rand.nextInt(emptyTiles.size());
                EmptyTile emptyTile = emptyTiles.get(x);
                t.setDestination(emptyTile);
                emptyTile.setOccupied(true);
            }
        }

    }

    @Override
    public void buildLadders(int n, int rows, int ladders) {
        Random rand = new Random();
        ArrayList<EmptyTile> emptyTiles = new ArrayList<>();

        while (ladders > 0){
            int i = rand.nextInt(n-1)+1;
            Tile t = board.getTile(i);
            if(t.getTileType() == TileType.Empty && t.getRow() > 0){
                LadderTile lt = (LadderTile) buildTile(TileType.Ladder, t.getRow(),t.getCol(),t.getNumber());
                board.addTile(lt);
                ladders--;
            }
        }

        for(Tile t : board.getBoard().values()){
            if(t instanceof LadderTile){
                for(Tile tDwn : board.getBoard().values()){
                    if(tDwn instanceof EmptyTile && tDwn.getRow() < t.getRow() && tDwn.isOccupied()) emptyTiles.add((EmptyTile) tDwn);
                }

                int x = rand.nextInt(emptyTiles.size());
                EmptyTile emptyTile = emptyTiles.get(x);
                t.setDestination(emptyTile);
                emptyTile.setOccupied(true);
            }
        }
    }

    @Override
    public GameBoard getGameBoard() {
        return board;
    }
}
