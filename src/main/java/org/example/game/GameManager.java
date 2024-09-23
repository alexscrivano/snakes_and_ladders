package org.example.game;

import org.example.board_components.boards.GameBoard;
import org.example.board_components.builders.BoardBuilder;
import org.example.board_components.builders.StdBoardBuilder;
import org.example.board_components.tiles.Tile;
import org.example.board_components.tiles.TileType;

public class GameManager {
    private BoardBuilder builder;
    private GameBoard board;

    private int rows,cols,maxTiles;
    private int playersNumber;

    public GameManager(int rows, int cols, int playersNumber, GameType gType) {
        this.rows = rows;
        this.cols = cols;
        this.playersNumber = playersNumber;
        this.maxTiles = rows * cols;
        if(gType == GameType.Standard) this.builder = new StdBoardBuilder();
    }

    public void createGame(){
        builder.buildBoard();
        System.out.println("Board created!");
        this.board = builder.getGameBoard();
        fillTheBoard();
    }
    private void fillTheBoard(){
        int n = maxTiles;
        for(int i = 0 ; i < rows ; i++){
            for(int j = 0 ; j < cols ; j++){
                Tile t = builder.buildTile(TileType.Empty,i,j,n);
                board.addTile(t);
                n--;
            }
        }
        int m = maxTiles/8;
        builder.buildSnakes(maxTiles,rows,m);
        builder.buildLadders(maxTiles,rows,m);
    }

    public static void main(String[] args) {
        GameManager gm = new GameManager(10,10,2,GameType.Standard);
        gm.createGame();

        StringBuilder sb = new StringBuilder();
        int n = gm.maxTiles;
        Tile[][] table = new Tile[gm.rows][gm.cols];
        while(n > 0){
            Tile t = gm.board.getTile(n);
            table[t.getRow()][t.getCol()] = t;
            n--;
        }
        for(int i = 0 ; i < gm.rows ; i++){
            if(i%2==0){
                for(int j = 0 ; j < gm.cols ; j++){
                    Tile t = gm.board.getTile(i,j);
                    if(t.getDestination().getNumber() > 0) sb.append("| " + t.getNumber() + " " + t.getTileType() + " " + t.getDestination().getNumber() + " |").append("\t");
                    else sb.append(t.getNumber() + "\t");
                }
            }else{
                for(int j = gm.cols-1; j >= 0 ; j--){
                    Tile t = gm.board.getTile(i,j);
                    if(t.getDestination().getNumber() > 0) sb.append("| " + t.getNumber() + " " + t.getTileType() + " " + t.getDestination().getNumber() + " |").append("\t");
                    else sb.append(t.getNumber() + "\t");
                }
            }
            sb.append("\n");
        }
        System.out.println(sb);
    }

}
