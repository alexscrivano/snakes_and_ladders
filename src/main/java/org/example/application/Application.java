package org.example.application;

import org.example.board_components.builders.SpecialRulesBuilder;
import org.example.board_components.builders.StdBoardBuilder;
import org.example.board_components.tiles.Tile;
import org.example.game.GameManager;
import org.example.game.turns_states.EndedTurnState;
import org.example.support.GameType;
import org.example.support.Player;
import org.example.support.tiles.TileType;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class Application extends JFrame {
    private GameManager gm;
    private Player player;
    private String msg;
    private int t1,t2;

    JPanel table;
    JPanel turnsPanel;
    JScrollPane turnsScrollPane;
    JPanel btnPanel;
    JButton resetbtn;
    int[] i = new int[1];

    public Application() {
        setTitle("Snakes and Ladders");
        setSize(1550,1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        initUi();
        pack();
        setVisible(true);
    }

    public void update(){
        JLabel label = new JLabel("P" + player.getPlayerIndex() + ">>" + msg);
        label.setFont(new Font("Arial",Font.HANGING_BASELINE,13));
        label.setPreferredSize(new Dimension(200,20));
        turnsPanel.add(label);
        turnsPanel.revalidate();
        turnsPanel.repaint();

        Tile ft = gm.getBoard().getTile(t1);
        Tile dt = gm.getBoard().getTile(t2);

        int index1,index2;

        if(ft.getRow() % 2 == 0){
            index2 = (ft.getRow()*gm.getCols()) + ft.getCol();
        }else{
            index2 = (ft.getRow()*gm.getCols()) + (gm.getCols() - 1 - ft.getCol());
        }
        if(dt.getRow() % 2 == 0){
            index1 = (dt.getRow()*gm.getCols()) + dt.getCol();
        }
        else{
            index1 = (dt.getRow()*gm.getCols()) + (gm.getCols() - 1 - dt.getCol());
        }

        JLabel label2 = (JLabel) table.getComponent(index2);
        JLabel label1 = (JLabel) table.getComponent(index1);

        String content = ft.getNumber() + ", " + ft.getTileType();
        if(ft.getTileType() == TileType.Snake || ft.getTileType() == TileType.Ladder){
            content += " -> " + ft.getDestination().getNumber();
        }

        label2.setText(content);
        label1.setText(dt.getNumber() + " - P" + player.getPlayerIndex());

        if(player.getLastTile() == gm.getMaxTiles()) {
            JOptionPane.showMessageDialog(this,"Player " + player.getPlayerIndex() + " won!");
            gm = null;
            player = null;
            resetbtn = new JButton("Reset");
            resetbtn.addActionListener(e -> reset());
            btnPanel.add(resetbtn);
            btnPanel.revalidate();
            btnPanel.repaint();
        }
    }

    private void reset(){
        removeAll();
        SwingUtilities.invokeLater(Application::new);
        dispose();
    }

    public void setMsg(String msg){this.msg = msg;}
    public Player getPlayer() {return this.player;}
    public void setPlayer(Player player) {this.player = player;}
    public void setT1(int t1) {this.t1 = t1;}
    public void setT2(int t2) {this.t2 = t2;}

    private void setPanels(JPanel panel, JPanel btns, JFrame parent){
        int[] dice = {-1};
        GameType[] type = new GameType[1];

        JPanel players = new JPanel();
        JPanel diceNumber = new JPanel();
        JPanel gameType = new JPanel();
        JPanel rows = new JPanel();
        JPanel cols = new JPanel();

        JLabel playersL = new JLabel("Select players number: ");
        JLabel diceNumberL = new JLabel("Select dice number: ");
        JLabel gameTypeL = new JLabel("Select game type: ");
        JLabel rowsL = new JLabel("Select rows for game table: ");
        JLabel colsL = new JLabel("Select cols for game table: ");

        JButton incr = new JButton("+");
        JButton decr = new JButton("-");
        JButton incr1 = new JButton("+");
        JButton decr1 = new JButton("-");
        JButton incr2 = new JButton("+");
        JButton decr2 = new JButton("-");
        JCheckBox check1 = new JCheckBox();
        JCheckBox check2 = new JCheckBox();// setText 1 o 2
        JCheckBox type1 = new JCheckBox();
        JCheckBox type2 = new JCheckBox();// setText special o standard
        JTextField playersT = new JTextField(2);
        JTextField dimTR = new JTextField(2);
        JTextField dimTC = new JTextField(2);

        playersT.setText(""+2);
        dimTR.setText(""+10);
        dimTC.setText(""+10);

        players.setLayout(new GridLayout(1,4));
        rows.setLayout(new GridLayout(1,4));
        cols.setLayout(new GridLayout(1,4));
        diceNumber.setLayout(new GridLayout(1,3));
        gameType.setLayout(new GridLayout(1,3));

        incr.setPreferredSize(new Dimension(50,50));
        decr.setPreferredSize(new Dimension(50,50));
        incr1.setPreferredSize(new Dimension(50,50));
        decr1.setPreferredSize(new Dimension(50,50));
        incr2.setPreferredSize(new Dimension(50,50));
        decr2.setPreferredSize(new Dimension(50,50));

        incr.addActionListener(e -> {
                int num = Integer.parseInt(playersT.getText());
                num += 1;
                playersT.setText(""+num);
        });
        decr.addActionListener(e -> {
                int num = Integer.parseInt(playersT.getText());
                num -= 1;
                playersT.setText(""+num);
        });
        incr1.addActionListener(e -> {
                int num = Integer.parseInt(dimTR.getText());
                num += 1;
                dimTR.setText(""+num);
        });
        decr1.addActionListener(e -> {
                int num = Integer.parseInt(dimTR.getText());
                num -= 1;
                dimTR.setText(""+num);
        });
        incr2.addActionListener(e -> {
                int num = Integer.parseInt(dimTC.getText());
                num += 1;
                dimTC.setText(""+num);
        });
        decr2.addActionListener(e -> {
                int num = Integer.parseInt(dimTC.getText());
                num -= 1;
                dimTC.setText(""+num);
        });

        type1.addActionListener(e -> {
            if(type1.isSelected() && !type2.isSelected()) type[0] = GameType.Standard;
            else if(type1.isSelected() && type2.isSelected()) JOptionPane.showMessageDialog(this, "You have to choose only one game type");
        });
        type2.addActionListener(e -> {
            if(!type1.isSelected() && type2.isSelected()) type[0] = GameType.MoreRules;
            else if(type1.isSelected() && type2.isSelected()) JOptionPane.showMessageDialog(this, "You have to choose only one game type");
        });
        check1.addActionListener(e -> {
            if(check1.isSelected() && check2.isSelected()) JOptionPane.showMessageDialog(this, "You have to choose only one number");
            else if(check1.isSelected() && !check2.isSelected()) dice[0] = 1;
        });
        check2.addActionListener(e -> {
            if(check1.isSelected() && check2.isSelected()) JOptionPane.showMessageDialog(this, "You have to choose only one number");
            else if(!check1.isSelected() && check2.isSelected()) dice[0] = 2;
        });

        rows.add(rowsL); rows.add(decr1); rows.add(dimTR); rows.add(incr1);
        cols.add(colsL); cols.add(decr2); cols.add(dimTC); cols.add(incr2);
        players.add(playersL); players.add(decr); players.add(playersT); players.add(incr);

        diceNumber.add(diceNumberL); check1.setText("1"); diceNumber.add(check1); check2.setText("2"); diceNumber.add(check2);
        gameType.add(gameTypeL); type1.setText("standard rules"); gameType.add(type1); type2.setText("special rules"); gameType.add(type2);

        panel.add(players);
        panel.add(diceNumber);
        panel.add(gameType);
        panel.add(rows);
        panel.add(cols);

        JButton next = new JButton("Next");
        JButton cancel = new JButton("Cancel");
        JButton loadConfig = new JButton("Load");

        next.addActionListener(e -> {
            int playersN = Integer.parseInt(playersT.getText());
            int diceN = dice[0];
            GameType gameTypeS = type[0];
            int rowsN = Integer.parseInt(dimTR.getText());
            int colsN = Integer.parseInt(dimTC.getText());

            if(gameTypeS == null || diceN < 0){
                JOptionPane.showMessageDialog(this, "Please choose dice number and game type");
            }else{
                this.gm = new GameManager(rowsN,colsN,playersN,diceN,gameTypeS, this);
                this.gm.createGame();
                parent.dispose();
                visualizeGame(rowsN,colsN);
            }


            // TODO visualizeTable() to show the game board;
        });
        cancel.addActionListener(e -> System.exit(0));
        loadConfig.addActionListener(e -> {
            String name = JOptionPane.showInputDialog("Insert config name: ");
            this.gm = new GameManager();
            this.gm.load(name);

            int playersN = Integer.parseInt(playersT.getText());
            int diceN = dice[0];
            int rowsN = Integer.parseInt(dimTR.getText());
            int colsN = Integer.parseInt(dimTC.getText());

            this.gm.setApp(this);
            this.gm.setRows(rowsN);
            this.gm.setCols(colsN);
            this.gm.setDiceNumber(diceN);
            this.gm.setPlayersNumber(playersN);
            this.gm.setMaxTiles(rowsN*colsN);

            if(gm.getGameType() == GameType.Standard) this.gm.setBuilder(new StdBoardBuilder());
            else if(gm.getGameType() == GameType.MoreRules) this.gm.setBuilder(new SpecialRulesBuilder());

            this.gm.createGame();
            // TODO visualizeTable() to show the game board;
            parent.dispose();
            visualizeGame(rowsN,colsN);
        });

        btns.add(cancel); btns.add(next); btns.add(loadConfig);
    }

    private void visualizeGame(int rows, int cols){
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1270,900));
        table = new JPanel(new GridLayout(rows,cols));

        table.setPreferredSize(new Dimension(950,800));
        table.setBounds(0,0,950,800);

        String content;
        for(int i = 0; i < rows; i++){
            if(i % 2 == 0){
                for(int j = 0; j < cols; j++){
                    Tile t = gm.getBoard().getTile(i,j);
                    content = t.getNumber() + ", " + t.getTileType();
                    if(t.getTileType() == TileType.Snake || t.getTileType() == TileType.Ladder){
                        content += " -> " + t.getDestination().getNumber();
                    }
                    JLabel tileLabel = new JLabel(content);
                    tileLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                    table.add(tileLabel);
                }
            }else{
                for(int j = cols-1; j >=0; j--){
                    Tile t = gm.getBoard().getTile(i,j);
                    content = t.getNumber() + ", " + t.getTileType();
                    if(t.getTileType() == TileType.Snake || t.getTileType() == TileType.Ladder){
                        content += " -> " + t.getDestination().getNumber();
                    }
                    JLabel tileLabel = new JLabel(content);
                    tileLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                    table.add(tileLabel);
                }
            }
        }
        this.add(table, BorderLayout.WEST);
        revalidate();
        repaint();

        turnsPanel = new JPanel();
        turnsPanel.setLayout(new BoxLayout(turnsPanel, BoxLayout.Y_AXIS));
        turnsScrollPane = new JScrollPane(turnsPanel);

        turnsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        turnsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        turnsScrollPane.setPreferredSize(new Dimension(300, 800));
        turnsScrollPane.setBounds(1040,0,300,800);
        turnsScrollPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        this.add(turnsScrollPane, BorderLayout.EAST);
        revalidate();
        repaint();

        btnPanel = new JPanel();
        btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        btnPanel.setPreferredSize(new Dimension(1200,100));
        btnPanel.setBounds(0,0,1200,50);

        JButton start = new JButton("Start simulation");
        JCheckBox simType = new JCheckBox("Manual advancement");
        JButton roll = new JButton("Roll");
        JButton saveConfig = new JButton("Save");

        saveConfig.addActionListener(e -> {
            String name = JOptionPane.showInputDialog("Insert save name: ");
            gm.save(name);
        });

        start.addActionListener(e -> {
            if(simType.isSelected()) {

                if(gm.getTurns() == null || gm.getTurns().isEmpty()){
                    for (int i = 0; i < gm.getPlayersNumber(); i++) {
                        gm.getTurns().put(new Player(i), new EndedTurnState());
                    }
                }

                btnPanel.add(roll);
                btnPanel.revalidate();
                btnPanel.repaint();
                JOptionPane.showMessageDialog(this, "Press roll to play");
            }
            else gm.autoplay();
        });
        roll.addActionListener(e -> {
            i[0] += 1;
            int indx = (i[0]) % gm.getPlayersNumber();
            gm.manual(indx);
        });

        btnPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnPanel.setAlignmentY(CENTER_ALIGNMENT);
        btnPanel.add(start);
        btnPanel.add(simType);
        btnPanel.add(saveConfig);
        this.add(btnPanel, BorderLayout.SOUTH);
        revalidate();
        repaint();
        pack();
        setVisible(true);
    }

    private void initUi() {
        JFrame frame = new JFrame("Snakes and Ladders - game setup");
        JPanel pframe = new JPanel();

        JPanel settingsPanel = new JPanel();
        JPanel buttonsPanel = new JPanel();

        frame.setLayout(new BorderLayout());
        pframe.setLayout(new GridLayout(2,1));
        settingsPanel.setLayout(new GridLayout(5,1));
        buttonsPanel.setLayout(new GridLayout(1,3));

        frame.setPreferredSize(new Dimension(600,250));
        settingsPanel.setPreferredSize(new Dimension(600,230));
        buttonsPanel.setPreferredSize(new Dimension(600,20));

        setPanels(settingsPanel, buttonsPanel, frame);
        pframe.add(settingsPanel, BorderLayout.NORTH);
        pframe.add(buttonsPanel, BorderLayout.SOUTH);

        frame.add(pframe);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Application::new);
    }
}