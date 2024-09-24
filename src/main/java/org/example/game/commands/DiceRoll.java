package org.example.game.commands;

import java.util.Random;

public class DiceRoll implements Command {
    private int dice1, dice2;
    private int total;
    private Random roll = new Random();

    @Override
    public void execute() {
        this.dice1 = roll.nextInt(1,7);
        this.dice2 = roll.nextInt(1,7);
        this.total = dice1 + dice2;
    }

    public int getTotal() {return total;}
    public int getDice1() {return dice1;}
    public int getDice2() {return dice2;}
}
