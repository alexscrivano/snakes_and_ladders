package org.example.game;

import org.example.board_components.boards.GameBoard;
import org.example.board_components.builders.BoardBuilder;
import org.example.board_components.builders.SpecialRulesBuilder;
import org.example.board_components.builders.StdBoardBuilder;
import org.example.board_components.tiles.Tile;
import org.example.game.game_saver.FileGameSaver;
import org.example.support.tiles.TileType;
import org.example.game.turns_states.EndedTurnState;
import org.example.game.turns_states.MovingTurnState;
import org.example.game.turns_states.PlayerTurnState;
import org.example.support.GameType;
import org.example.support.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class GameManager {
    private BoardBuilder builder;
    private GameBoard board;
    private GameType gameType;

    private int rows,cols,maxTiles;
    private int playersNumber,diceNumber;

    private Map<Player, PlayerTurnState> turns;

    public GameManager(int rows, int cols, int playersNumber, int diceNumber, GameType gType) {
        this.rows = rows;
        this.cols = cols;
        this.playersNumber = playersNumber;
        this.maxTiles = rows * cols;
        this.diceNumber = diceNumber;
        this.gameType = gType;
        this.turns = new HashMap<>();

        if(gType == GameType.Standard) this.builder = new StdBoardBuilder();
        else if(gType == GameType.MoreRules) this.builder = new SpecialRulesBuilder();
    }
    public int getPlayersNumber(){return playersNumber;}
    public int getRows(){return rows;}
    public int getCols(){return cols;}
    public GameType getGameType() {return gameType;}
    public GameBoard getBoard(){return board;}
    public Map<Player, PlayerTurnState> getTurns(){return turns;}

    public int getMaxTiles(){return maxTiles;}
    public int getDiceNumber(){return diceNumber;}

    public void createGame(){
        builder.buildBoard();
        System.out.println("Board created!");
        this.board = builder.getGameBoard();
        if(gameType == GameType.Standard) fillTheBoardStd();
        else fillTheBoard();
    }

    private void fillTheBoard(){
        fillTheBoardStd();
        builder.buildSpecials(maxTiles);
    }

    private void fillTheBoardStd(){
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

    public void autoplay(){
        for(int i = 0 ; i < playersNumber ; i++){
            turns.put(new Player(i),new EndedTurnState());
        }
        boolean done = false;
        while(!done){
            for(Player p : turns.keySet()){
                if(p.getLastTile() == 100) {
                    done = true;
                    System.out.println("Player "+p.getPlayerIndex()+" won!");
                    break;
                }

                if(turns.get(p) instanceof EndedTurnState) turns.put(p,new MovingTurnState());
                int t1 = p.getLastTile();
                turns.get(p).move(this,p);
                int t2 = p.getLastTile();
                if(t1 != t2) System.out.println("Player " + p.getPlayerIndex() + " moved from " + t1 + " to " + t2);
                else System.out.println("Player " + p.getPlayerIndex() + " still on " + t1);


            }
        }
    }
    public void manual(){
        for(int i = 0 ; i < playersNumber ; i++){
            System.out.println("Player " + i );
            turns.put(new Player(i),new EndedTurnState());
        }

        Scanner sc = new Scanner(System.in);
        boolean done = false;
        while(!done){
            for(Player p : turns.keySet()){
                if(p.getLastTile() == 100){
                    done = true;
                    System.out.println("Player "+p.getPlayerIndex()+" won!");
                    break;
                }

                PlayerTurnState state = turns.get(p);
                if(state instanceof EndedTurnState) turns.put(p,new MovingTurnState());
                System.out.printf("Player %d want to continue?", p.getPlayerIndex());
                String yn = sc.nextLine();
                if(yn.equals("y")){
                    turns.get(p).move(this,p);
                }else if(yn.equals("n")){
                    if(playersNumber > 2){
                        turns.keySet().remove(p);
                    }else{
                        setWinnerNext(p.getPlayerIndex());
                    }
                }
            }
        }
    }
    private void setWinnerNext(int playerIndex){
        System.out.println("Player "+playerIndex+" retired");
        int winner = (playerIndex + 1) % playersNumber;
        for(Player p : turns.keySet()){
            if(p.getPlayerIndex() == winner) p.setLastTile(100);
        }
    }

    public void save(String name){
        FileGameSaver saver = new FileGameSaver();
        saver.save(board,name);
    }


    public static void main(String[] args) {
        GameManager gm = new GameManager(10,10,2,2,GameType.MoreRules);
        gm.createGame();
        //gm.save("save1");

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
                    if(t.getTileType() == TileType.Snake || t.getTileType() == TileType.Ladder) sb.append("| " + t.getNumber() + " " + t.getTileType() + " " + t.getDestination().getNumber() + " |").append("\t");
                    else if(t.getTileType() != TileType.Empty && t.getTileType() != TileType.Snake && t.getTileType() != TileType.Ladder) sb.append("| " + t.getNumber() + " " + t.getTileType() + " |").append("\t");
                    else sb.append(t.getNumber() + "\t");
                }
            }else{
                for(int j = gm.cols-1; j >= 0 ; j--){
                    Tile t = gm.board.getTile(i,j);
                    if(t.getTileType() == TileType.Snake || t.getTileType() == TileType.Ladder) sb.append("| " + t.getNumber() + " " + t.getTileType() + " " + t.getDestination().getNumber() + " |").append("\t");
                    else if(t.getTileType() != TileType.Empty && t.getTileType() != TileType.Snake && t.getTileType() != TileType.Ladder) sb.append("| " + t.getNumber() + " " + t.getTileType() + " |").append("\t");
                    else sb.append(t.getNumber() + "\t");
                }
            }
            sb.append("\n");
        }
        System.out.println(sb);

        gm.manual();

    }


}
