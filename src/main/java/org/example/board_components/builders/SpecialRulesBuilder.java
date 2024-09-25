package org.example.board_components.builders;


import org.example.board_components.boards.GameBoard;
import org.example.board_components.tiles.Tile;
import org.example.board_components.tiles.special_tiles.DrawCardTile;
import org.example.board_components.tiles.special_tiles.PriceTile;
import org.example.board_components.tiles.special_tiles.StopTile;
import org.example.board_components.tiles.standard_tiles.EmptyTile;
import org.example.board_components.tiles.standard_tiles.LadderTile;
import org.example.board_components.tiles.standard_tiles.SnakeTile;
import org.example.support.tiles.PriceType;
import org.example.support.tiles.StopType;
import org.example.support.tiles.TileType;

import java.util.ArrayList;
import java.util.Random;

public class SpecialRulesBuilder implements BoardBuilder {
    private GameBoard gameBoard;

    @Override
    public void buildBoard() {this.gameBoard = new GameBoard();}

    @Override
    public Tile buildTile(TileType type, int row, int col, int number){
        Tile t;
        switch (type){
            case Snake -> t = new SnakeTile(row,col, number);
            case Ladder -> t = new LadderTile(row,col, number);
            case Stop -> t = new StopTile(row,col, number);
            case Price -> t = new PriceTile(row,col, number);
            case Card -> t = new DrawCardTile(row,col,number);
            default -> t = new EmptyTile(row,col,number);
        }
        return t;
    }

    @Override
    public void buildSnakes(int n, int rows, int snakes) {
        Random rand = new Random();
        ArrayList<EmptyTile> emptyTiles = new ArrayList<>();

        while (snakes > 0){
            int i = rand.nextInt(n-1)+1;
            Tile t = gameBoard.getTile(i);
            if(t instanceof EmptyTile && t.getRow() < rows-1){
                SnakeTile st = (SnakeTile) buildTile(TileType.Snake, t.getRow(),t.getCol(),t.getNumber());
                gameBoard.addTile(st);
                snakes--;
            }
        }
        for(Tile t : gameBoard.getBoard().values()){
            if(t instanceof SnakeTile){
                for(Tile tUp : gameBoard.getBoard().values()){
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
            Tile t = gameBoard.getTile(i);
            if(t.getTileType() == TileType.Empty && t.getRow() > 0){
                LadderTile lt = (LadderTile) buildTile(TileType.Ladder, t.getRow(),t.getCol(),t.getNumber());
                gameBoard.addTile(lt);
                ladders--;
            }
        }

        for(Tile t : gameBoard.getBoard().values()){
            if(t instanceof LadderTile){
                for(Tile tDwn : gameBoard.getBoard().values()){
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
    public GameBoard getGameBoard() {return gameBoard;}

    @Override
    public void buildSpecials(int n){
        Random rand = new Random();
        int cards = n/10;
        int stops = n/10;
        int prices = n/10;

        while (cards > 0){
            int tileN = rand.nextInt(n-1)+1;
            Tile t = gameBoard.getTile(tileN);
            if(t.getTileType() == TileType.Empty && !t.isOccupied()){
                gameBoard.getBoard().put(t.getNumber(),buildTile(TileType.Card,t.getRow(),t.getCol(),t.getNumber()));
                cards--;
            }
        }
        while (stops > 0){
            int tileN = rand.nextInt(n-1)+1;
            Tile t = gameBoard.getTile(tileN);
            if(t.getTileType() == TileType.Empty && !t.isOccupied()){
                gameBoard.getBoard().put(t.getNumber(),buildTile(TileType.Stop,t.getRow(),t.getCol(),t.getNumber()));
                int type = rand.nextInt(2);
                if(type == 0){
                    ((StopTile) gameBoard.getBoard().get(tileN)).setStop(StopType.Panchina);
                }else{
                    ((StopTile) gameBoard.getBoard().get(tileN)).setStop(StopType.Locanda);
                }
                stops--;
            }
        }
        while (prices > 0){
            int tileN = rand.nextInt(n-1)+1;
            Tile t = gameBoard.getTile(tileN);
            if(t.getTileType() == TileType.Empty && !t.isOccupied()){
                gameBoard.getBoard().put(t.getNumber(),buildTile(TileType.Price,t.getRow(),t.getCol(),t.getNumber()));
                int price = rand.nextInt(2);
                if(price == 0){
                    ((PriceTile) gameBoard.getBoard().get(tileN)).setPrice(PriceType.Molla);
                }else{
                    ((PriceTile) gameBoard.getBoard().get(tileN)).setPrice(PriceType.Dadi);
                }
                prices--;
            }
        }
    }

}
