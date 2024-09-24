package org.example.game.game_saver;

import org.example.board_components.boards.GameBoard;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;

public class FileGameSaver implements GameSaver{
    private GameBoard gameBoard;
    private final String path = ".\\src\\main\\java\\org\\example\\support\\savings\\";

    @Override
    public void save(GameBoard board, String saveName) {
        try{
            File file = new File(path + saveName + ".dat");
            if(!file.exists()){
                file.createNewFile();
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));
                out.writeObject(board);
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
    public GameBoard load(String saveName) {
        try{
            File f = new File(path + saveName + ".dat");
            ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
            this.gameBoard = (GameBoard) in.readObject();
            in.close();
        }catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return this.gameBoard;
    }
}
