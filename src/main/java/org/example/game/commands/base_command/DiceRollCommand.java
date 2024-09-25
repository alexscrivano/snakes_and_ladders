package org.example.game.commands.base_command;

import org.example.game.commands.Command;

import java.util.Random;

public class DiceRollCommand implements Command {
    private int dice1, dice2;
    private int total;
    private Random roll = new Random();

    @Override
    public void execute() {
        this.dice1 = roll.nextInt(1,7);
        this.dice2 = roll.nextInt(1,7);
        this.total = dice1 + dice2;
    }

    @Override
    public int getNextTile() {
        return 0;
    }

    public int getResult() {return total;}
    public int getDice1() {return dice1;}
    public int getDice2() {return dice2;}
}
