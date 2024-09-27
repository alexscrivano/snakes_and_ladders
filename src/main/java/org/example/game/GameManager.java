package org.example.game;

import org.example.application.Application;
import org.example.board_components.boards.GameBoard;
import org.example.board_components.builders.BoardBuilder;
import org.example.board_components.builders.SpecialRulesBuilder;
import org.example.board_components.builders.StdBoardBuilder;
import org.example.board_components.tiles.Tile;
import org.example.game.commands.PlayerMove;
import org.example.game.commands.base_command.DiceRollCommand;
import org.example.game.game_saver.FileGameSaver;
import org.example.game.turns_states.StoppedTurnState;
import org.example.support.tiles.TileType;
import org.example.game.turns_states.EndedTurnState;
import org.example.game.turns_states.MovingTurnState;
import org.example.game.turns_states.PlayerTurnState;
import org.example.support.GameType;
import org.example.support.Player;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class GameManager {

    private record GameConfiguration (
            GameBoard gBoard,
            GameType gameType
    )implements Serializable {}
    private final String path = ".\\src\\main\\resources\\savings\\";

    private Application app;

    private BoardBuilder builder;
    private GameBoard board;
    private GameType gameType;

    private int rows,cols,maxTiles;
    private int playersNumber,diceNumber;

    private Map<Player, PlayerTurnState> turns;

    public GameManager(int rows, int cols, int playersNumber, int diceNumber, GameType gType, Application app) {
        this.rows = rows;
        this.cols = cols;
        this.playersNumber = playersNumber;
        this.maxTiles = rows * cols;
        this.diceNumber = diceNumber;
        this.gameType = gType;
        this.turns = new HashMap<>();
        this.app = app;

        if(gType == GameType.Standard) this.builder = new StdBoardBuilder();
        else if(gType == GameType.MoreRules) this.builder = new SpecialRulesBuilder();
    }

    public GameManager(GameBoard board, GameType gType) {
        this.board = board;
        this.turns = new HashMap<>();

        if(gType == GameType.Standard) this.builder = new StdBoardBuilder();
        else if(gType == GameType.MoreRules) this.builder = new SpecialRulesBuilder();
    }

    public void setPlayersNumber(int playersNumber) {this.playersNumber = playersNumber;}
    public void setRows(int rows) {this.rows = rows;}
    public void setCols(int cols) {this.cols = cols;}
    public void setDiceNumber(int diceNumber) {this.diceNumber = diceNumber;}
    public void setBoard(GameBoard board){this.board = board;}
    public void setBuilder(BoardBuilder builder){this.builder = builder;}
    public void setGameType(GameType type){this.gameType = type;}

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

    public void autoplay() {
        for (int i = 0; i < playersNumber; i++) {
            turns.put(new Player(i), new EndedTurnState());
        }
        boolean done = false;
        while (!done) {
            for (Player p : turns.keySet()) {
                if (turns.get(p) instanceof EndedTurnState) turns.put(p, new MovingTurnState());
                int t1 = p.getLastTile();
                turns.get(p).move(this, p, new DiceRollCommand());
                int t2 = p.getLastTile();

                if (t1 < t2) {
                    System.out.println("Player " + p.getPlayerIndex() + " moved from " + t1 + " to " + t2);
                    app.setMsg(" moved from " + t1 + " to " + t2);
                }else {
                    if(turns.get(p) instanceof StoppedTurnState){
                        System.out.println("Player " + p.getPlayerIndex() + " still on " + t2);
                        app.setMsg(" stopped for " + ((StoppedTurnState) turns.get(p)).getStops() + " turns");
                    }
                    else {

                        System.out.println("Player " + p.getPlayerIndex() + " come back to " + t2);
                        app.setMsg(" come back to " + t2);
                    }
                }

                if (p.getLastTile() == 100) {
                    done = true;
                    System.out.println("Player " + p.getPlayerIndex() + " won!");
                    app.setMsg(" moved from " + t1 + " to " + t2);
                    app.setPlayer(p);
                    app.update();
                    break;
                }

                app.setPlayer(p);
                app.update();

            }
        }
    }

    public void manual(){
        /*
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
                String yn = app.getCommand();
                if(yn.equals("y")){
                    app.setLastTile(p.getLastTile());
                    turns.get(p).move(this,p, new DiceRollCommand());
                }else if(yn.equals("n")){
                    if(playersNumber > 2){
                        turns.keySet().remove(p);
                    }else{
                        setWinnerNext(p.getPlayerIndex());
                    }
                }

                app.setPlayer(p.getPlayerIndex());
                app.setNewTile(p.getLastTile());

            }
        }

         */
    }
    private void setWinnerNext(int playerIndex){
        System.out.println("Player "+playerIndex+" retired");
        int winner = (playerIndex + 1) % playersNumber;
        for(Player p : turns.keySet()){
            if(p.getPlayerIndex() == winner) p.setLastTile(100);
        }
    }

    public void save(String name){
        try{
            File f = new File(path+name);
            if(!f.exists()){
                f.createNewFile();
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
                oos.writeObject(new GameConfiguration(this.board,this.gameType));
                oos.flush();
                oos.close();
            }
        }catch (IOException e){e.printStackTrace();}
    }

    public void load(String filename){
        try{
            File f = new File(path + filename);
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
            GameConfiguration config = (GameConfiguration) ois.readObject();
            this.setBoard(config.gBoard);
            this.setGameType(config.gameType);
            ois.close();
        }catch (IOException | ClassNotFoundException e) {}
    }
}