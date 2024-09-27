package org.example.game.game_saver;

import org.example.board_components.boards.GameBoard;
import org.example.game.GameManager;
import org.example.support.GameType;

import java.io.*;

public class FileGameSaver extends GameSaver{

    private record GameConfiguration (
            GameBoard gBoard,
            GameType gameType
    )implements Serializable {}


    private GameConfiguration gameConfiguration;
    private final String path = ".\\src\\main\\resources\\savings\\";

    @Override
    public void save(GameManager game, String saveName) {
        try{
            File file = new File(path + saveName + ".dat");
            if(!file.exists()){
                file.createNewFile();
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
                gameConfiguration = new GameConfiguration(game.getBoard(), game.getGameType());
                out.writeObject(gameConfiguration);
                out.flush();
                out.close();
            }else{
                System.out.println("File already exists");
                //TODO add exception management
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public GameManager load(String saveName) {
        GameManager game = null;
        try{
            File f = new File(path + saveName + ".dat");
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
            this.gameConfiguration = (GameConfiguration) in.readObject();
            game = new GameManager(gameConfiguration.gBoard, gameConfiguration.gameType);
            in.close();
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return game;
    }
}
