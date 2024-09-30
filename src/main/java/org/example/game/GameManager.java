package org.example.game;

import org.example.application.Application;
import org.example.board_components.boards.GameBoard;
import org.example.board_components.builders.BoardBuilder;
import org.example.board_components.builders.SpecialRulesBuilder;
import org.example.board_components.builders.StdBoardBuilder;
import org.example.board_components.tiles.Tile;
import org.example.game.commands.base_command.DiceRollCommand;
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
            GameType gameType,
            Map<Player,PlayerTurnState> gturns
    )implements Serializable {}
    private final String path = ".\\src\\main\\resources\\savings\\";

    private Application app;

    private BoardBuilder builder;
    private GameBoard board;
    private GameType gameType;

    private int rows,cols,maxTiles;
    private int playersNumber,diceNumber;

    private int rollmsg;

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

    public GameManager() {}

    public void setPlayersNumber(int playersNumber) {this.playersNumber = playersNumber;}
    public void setRows(int rows) {this.rows = rows;}
    public void setCols(int cols) {this.cols = cols;}
    public void setDiceNumber(int diceNumber) {this.diceNumber = diceNumber;}
    public void setBoard(GameBoard board){
        this.board = new GameBoard();
        for(Tile t : board.getBoard().values()) {
            this.board.addTile(t);
        }
    }
    public void setTurns(Map<Player, PlayerTurnState> turns){this.turns = turns;}
    public void setBuilder(BoardBuilder builder){this.builder = builder;}
    public void setGameType(GameType type){this.gameType = type;}
    public void setApp(Application app){this.app = app;}
    public void setMaxTiles(int maxTiles) {this.maxTiles = maxTiles;}
    public void setRollmsg(int rollmsg){this.rollmsg = rollmsg;}

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
        if(board == null) {
            this.board = builder.getGameBoard();
            if(gameType == GameType.Standard) fillTheBoardStd();
            else fillTheBoard();
        }
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
        if(turns.isEmpty()){
            for (int i = 0; i < playersNumber; i++) {
                turns.put(new Player(i), new EndedTurnState());
            }
        }
        boolean done = false;
        while (!done) {
            for (Player p : turns.keySet()) {
                int t1 = p.getLastTile();
                app.setT1(t1);

                if (turns.get(p) instanceof EndedTurnState) turns.put(p, new MovingTurnState());
                turns.get(p).move(this, p, new DiceRollCommand());

                int t2 = p.getLastTile();
                app.setT2(t2);

                if (t1 < t2) {
                    app.setMsg(" rolled a " + rollmsg + " and moved from " + t1 + " to " + t2);
                }else {
                    if(turns.get(p) instanceof StoppedTurnState){
                        app.setMsg(" rolled a " + rollmsg + " but is stopped on " + t2 + " for " + ((StoppedTurnState) turns.get(p)).getStops() + " turns");
                    }
                    else {
                        app.setMsg(" rolled a " + rollmsg + " and come back to " + t2);
                    }
                }

                if (t2 == maxTiles) {
                    done = true;
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

    public void manual(int playerInd){
        for(Player p : turns.keySet()){
            if(p.getPlayerIndex() == playerInd){
                if (turns.get(p) instanceof EndedTurnState) turns.put(p, new MovingTurnState());
                int t1 = p.getLastTile();

                turns.get(p).move(this, p, new DiceRollCommand());
                int t2 = p.getLastTile();

                if (t1 < t2) {
                    app.setMsg(" rolled a " + rollmsg + " and moved from " + t1 + " to " + t2);
                }else{
                    if(turns.get(p) instanceof StoppedTurnState){
                        app.setMsg(" rolled a " + rollmsg + " but is stopped on " + t2 + " for " + ((StoppedTurnState) turns.get(p)).getStops() + " turns");
                    }else{
                        app.setMsg(" rolled a " + rollmsg + " and come back to " + t2);
                    }
                }
                if (p.getLastTile() == maxTiles) {
                    app.setMsg(" moved from " + t1 + " to " + t2);
                }

                app.setT1(t1);
                app.setT2(t2);
                app.setPlayer(p);
                app.update();
            }
        }

    }

    public void save(String name){
        try{
            File f = new File(path+name+".dat");
            if(!f.exists()){
                f.createNewFile();
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(f));
                oos.writeObject(new GameConfiguration(this.board,this.gameType,this.turns));
                oos.flush();
                oos.close();
            }
        }catch (IOException e){e.printStackTrace();}
    }

    public void load(String filename){
        try{
            File f = new File(path + filename+".dat");
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f));
            GameConfiguration config = (GameConfiguration) ois.readObject();
            this.setBoard(config.gBoard);
            this.setGameType(config.gameType);
            this.setTurns(config.gturns);

            for(Player p : turns.keySet()){
                p.setLastTile(1);
                turns.put(p, new EndedTurnState());
            }

            if(config.gameType == GameType.Standard) this.setBuilder(new StdBoardBuilder());
            else this.setBuilder(new SpecialRulesBuilder());
            ois.close();
        }catch (IOException | ClassNotFoundException e) {}
    }
}