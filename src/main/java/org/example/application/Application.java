package org.example.application;

import org.example.board_components.tiles.Tile;
import org.example.game.GameManager;
import org.example.game.turns_states.StoppedTurnState;
import org.example.support.GameType;
import org.example.support.Player;
import org.example.support.tiles.TileType;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class Application extends JFrame {
    private GameManager gm;
    private Player player;
    private String msg;

    JLayeredPane layeredPane;
    JPanel turnsPanel;
    JScrollPane turnsScrollPane;
    JPanel btnPanel;
    JButton resetbtn;
    JButton btnNewGame, btnNewGame1, startGame;
    JCheckBox cb;

    final JPanel[] tablePanel = new JPanel[1];
    final JPanel[] tablePanel1 = new JPanel[1];

    public Application() {
        setTitle("Snakes and Ladders");
        setSize(1000,1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);

        initUi();
        pack();
        setVisible(true);
    }

    public void update(){
        // TODO modify the interface state and repaint

        JLabel label = new JLabel("P" + player.getPlayerIndex() + ">>" + msg);
        turnsPanel.add(label);
        turnsPanel.revalidate();
        turnsPanel.repaint();

        if(player.getLastTile() == 100) {
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

    private void initUi() {

        JFrame frame = new JFrame("Game configuration");

        setLayout(new BorderLayout());

        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(800, 800));

        turnsPanel =  new JPanel();
        turnsPanel.setLayout(new BoxLayout(turnsPanel, BoxLayout.Y_AXIS));

        turnsScrollPane = new JScrollPane(turnsPanel);
        turnsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        turnsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        turnsScrollPane.setPreferredSize(new Dimension(500, 800));
        turnsScrollPane.setBounds(1050,0,500,800);

        btnPanel = new JPanel();
        btnPanel.setLayout(new FlowLayout());
        btnPanel.setPreferredSize(new Dimension(800, 50));
        btnPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        btnPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnNewGame = new JButton("New Standard Game");
        btnNewGame.addActionListener(e -> {
            String message;
            if(gm == null){
                gm = new GameManager(10,10,2,2, GameType.Standard, this);
                gm.createGame();
                message = "New game table Created: \n";

                tablePanel[0] = createTable();
                tablePanel[0].setBounds(0,0,800,800);

                if(tablePanel1[0] == null){
                    layeredPane.add(tablePanel[0], JLayeredPane.DEFAULT_LAYER);
                    JOptionPane.showMessageDialog(this, message);
                }
            }
        });

        btnNewGame1 = new JButton("New Special Game");
        btnNewGame1.addActionListener(e -> {
            if(gm == null){
                gm = new GameManager(10,10,2,2, GameType.MoreRules,  this);
                gm.createGame();

                String message = "New game table Created: \n";
                tablePanel1[0] = createTable();
                tablePanel1[0].setBounds(0,0,800,800);
                if(tablePanel[0] == null){
                    layeredPane.add(tablePanel1[0], JLayeredPane.DEFAULT_LAYER);
                    JOptionPane.showMessageDialog(this, message);
                }
            }

        });



        final int[] simType = new int[1];
        cb = new JCheckBox("Manual advancement");
        cb.addActionListener(e -> {
            if(cb.isSelected()) {
                simType[0] = 1;
            }else{
                simType[0] = 0;
            }
        });

        startGame = new JButton("Start simulation");
        startGame.addActionListener(e -> {
            if(turnsPanel != null){
                if(turnsPanel.getComponentCount() > 0) {
                    turnsPanel.removeAll();
                    turnsPanel.revalidate();
                    turnsPanel.repaint();
                }
            }

            if(simType[0] == 0) {
                gm.autoplay();
            }else {
                JButton goOn = new JButton("Roll");
                goOn.addActionListener(action -> {
                    if(gm != null) {gm.manual();}
                });
                btnPanel.add(goOn);
                btnPanel.revalidate();
                btnPanel.repaint();
            }
        });
        btnPanel.add(btnNewGame);
        btnPanel.add(btnNewGame1);
        btnPanel.add(startGame);

        btnPanel.add(cb);
        layeredPane.setBorder(BorderFactory.createEmptyBorder(0,0,0,50));
        layeredPane.revalidate();
        layeredPane.repaint();
        add(btnPanel, BorderLayout.SOUTH);
        add(layeredPane, BorderLayout.WEST);
        add(turnsScrollPane, BorderLayout.EAST);

    }

    private JPanel createTable() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(gm.getRows(),gm.getCols()));
        String content = "";
        for (int i = 0; i < gm.getRows(); i++) {
            if(i%2 == 0){
                for (int j = 0; j < gm.getCols(); j++) {
                    Tile t = gm.getBoard().getTile(i,j);
                    if(t.getTileType() == TileType.Empty) content = "" + t.getNumber();
                    else{
                        if(t.getDestination().getNumber() > 0) content = t.getNumber() + " - " + t.getTileType();
                        else content = t.getNumber() + " - " + t.getTileType();
                    }
                    JLabel tile = new JLabel(content,SwingConstants.CENTER);
                    tile.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                    panel.add(tile);
                }
            }else{
                for (int j = gm.getCols()-1; j >=0; j--) {
                    Tile t = gm.getBoard().getTile(i,j);
                    if(t.getTileType() == TileType.Empty) content = "" + t.getNumber();
                    else{
                        if(t.getDestination().getNumber() > 0) content = t.getNumber() + " - " + t.getTileType();
                        else content = t.getNumber() + " - " + t.getTileType();
                    }
                    JLabel tile = new JLabel(content,SwingConstants.CENTER);
                    tile.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                    panel.add(tile);
                }
            }
        }
        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Application::new);
    }
}
