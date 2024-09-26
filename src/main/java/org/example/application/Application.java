package org.example.application;

import org.example.board_components.tiles.Tile;
import org.example.game.GameManager;
import org.example.game.turns_states.EndedTurnState;
import org.example.game.turns_states.MovingTurnState;
import org.example.game.turns_states.PlayerTurnState;
import org.example.game.turns_states.StoppedTurnState;
import org.example.support.GameType;
import org.example.support.Player;
import org.example.support.tiles.TileType;

import javax.swing.*;
import java.awt.*;
import java.util.List;

class WhitePanel extends JPanel {
    private int width;
    private int height;
    public WhitePanel(int width, int height) {
        this.width = width;
        this.height = height;
        setPreferredSize(new Dimension(width, height));
    }
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);
    }
}

public class Application extends JFrame {
    GameManager gm;

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
    private void initUi() {

        setLayout(new BorderLayout());

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(800, 800));

        JPanel turnsPanel =  new JPanel();
        turnsPanel.setLayout(new BoxLayout(turnsPanel, BoxLayout.Y_AXIS));

        JScrollPane turnsScrollPane = new JScrollPane(turnsPanel);
        turnsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        turnsScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        turnsScrollPane.setPreferredSize(new Dimension(500, 800));
        turnsScrollPane.setBounds(1050,0,500,800);

        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new FlowLayout());
        btnPanel.setPreferredSize(new Dimension(800, 50));
        btnPanel.setAlignmentY(Component.CENTER_ALIGNMENT);
        btnPanel.setAlignmentX(Component.CENTER_ALIGNMENT);


        final JPanel[] tablePanel = new JPanel[1];
        final JPanel[] tablePanel1 = new JPanel[1];

        JButton btnNewGame = new JButton("New Standard Game");
        btnNewGame.addActionListener(e -> {

            if(gm == null){
                gm = new GameManager(10,10,2,2, GameType.Standard);
                gm.createGame();
            }
            String message = "New game table Created: \n";

            tablePanel[0] = createTable();
            tablePanel[0].setBounds(0,0,800,800);

            if(tablePanel1[0] == null){
                layeredPane.add(tablePanel[0], JLayeredPane.DEFAULT_LAYER);
                JOptionPane.showMessageDialog(this, message);
            }


        });

        JButton btnNewGame1 = new JButton("New Special Game");
        btnNewGame1.addActionListener(e -> {

            if(gm == null){
                gm = new GameManager(10,10,2,2, GameType.MoreRules);
                gm.createGame();
            }

            String message = "New game table Created: \n";

            tablePanel1[0] = createTable();
            tablePanel1[0].setBounds(0,0,800,800);

            if(tablePanel[0] == null){
                layeredPane.add(tablePanel1[0], JLayeredPane.DEFAULT_LAYER);
                JOptionPane.showMessageDialog(this, message);
            }

        });

        btnPanel.add(btnNewGame);
        btnPanel.add(btnNewGame1);

        final int[] simType = new int[1];
        JCheckBox cb = new JCheckBox("Manual advancement");
        cb.addActionListener(e -> {
            if(cb.isSelected()) {
                simType[0] = 1;
            }else{
                simType[0] = 0;
            }
        });




        JButton startGame = new JButton("Start simulation");
        startGame.addActionListener(e -> {

            SwingWorker<Void,String> autoworker = new SwingWorker<>() {

                @Override
                protected Void doInBackground() throws Exception {
                    for (int i = 0; i < gm.getPlayersNumber(); i++) {
                        gm.getTurns().put(new Player(i), new EndedTurnState());
                    }
                    boolean done = false;
                    while (!done) {
                        for (Player p : gm.getTurns().keySet()) {
                            if (p.getLastTile() == 100) {
                                done = true;
                                System.out.println("Player " + p.getPlayerIndex() + " won!");
                                JOptionPane.showMessageDialog(layeredPane,"Player " + p.getPlayerIndex() + " won!");

                                if(tablePanel[0] != null){
                                    layeredPane.remove(tablePanel[0]);
                                    tablePanel[0] = null;
                                }
                                if(tablePanel1[0] != null){
                                    layeredPane.remove(tablePanel1[0]);
                                    tablePanel1[0] = null;
                                }
                                gm = null;
                                break;
                            }

                            if (gm.getTurns().get(p) instanceof EndedTurnState) gm.getTurns().put(p, new MovingTurnState());
                            int t1 = p.getLastTile();
                            gm.getTurns().get(p).move(gm, p);
                            int t2 = p.getLastTile();
                            if (t1 < t2) {
                                System.out.println("Player " + p.getPlayerIndex() + " moved from " + t1 + " to " + t2);
                                publish( "\tP" + p.getPlayerIndex() + ">>  moved from " + t1 + " to " + t2);
                            } else {
                                System.out.println("Player " + p.getPlayerIndex() + " still on " + t1);
                                PlayerTurnState state = gm.getTurns().get(p);
                                if(state instanceof StoppedTurnState) publish("\tP" + p.getPlayerIndex() + ">>  on " + t2 + ", stopped for " + ((StoppedTurnState) state).getStops() + " turns");
                                else publish("\tP" + p.getPlayerIndex() + ">>  come back to " + t2);
                            }
                            Thread.sleep(1000);
                        }
                    }
                    return null;
                }

                @Override
                protected void process(List<String> chunks) {
                    for(String chunk : chunks) {
                        JLabel label = new JLabel(chunk);
                        //label.setAlignmentX(Component.LEFT_ALIGNMENT);
                        turnsPanel.add(label);
                        turnsPanel.revalidate();
                        turnsPanel.repaint();
                        turnsScrollPane.revalidate();
                        turnsScrollPane.repaint();


                        JScrollBar vertical = turnsScrollPane.getVerticalScrollBar();
                        vertical.setValue(vertical.getMaximum());
                    }
                }
            };

            final int[] player = new int[1];

            if(turnsPanel != null){
                if(turnsPanel.getComponentCount() > 0) {
                    turnsPanel.removeAll();
                    turnsPanel.revalidate();
                    turnsPanel.repaint();
                }
            }

            if(simType[0] == 0) {
                autoworker.execute();
            }else {
                JButton goOn = new JButton("Roll");
                goOn.addActionListener(action -> {
                    if(gm != null) {
                        SwingWorker<Void,String> manualworker = new SwingWorker<>() {

                            @Override
                            protected Void doInBackground() throws Exception {
                                for (int i = 0; i < gm.getPlayersNumber(); i++) {
                                    gm.getTurns().put(new Player(i), new EndedTurnState());
                                }

                                player[0] = (player[0] + 1)%gm.getPlayersNumber();
                                Player plr = null;
                                for(Player p : gm.getTurns().keySet()) {
                                    if(p.getPlayerIndex() == player[0]){plr = p;}
                                }
                                if (gm.getTurns().get(plr) instanceof EndedTurnState) gm.getTurns().put(plr, new MovingTurnState());
                                assert plr != null;
                                int pt = plr.getLastTile();
                                gm.getTurns().get(plr).move(gm,plr);
                                int nt = plr.getLastTile();

                                if(nt == 100){
                                    System.out.println("Player " + plr.getPlayerIndex() + " won!");
                                    JOptionPane.showMessageDialog(layeredPane,"Player " + plr.getPlayerIndex() + " won!");

                                    if(tablePanel[0] != null){
                                        layeredPane.remove(tablePanel[0]);
                                        tablePanel[0] = null;
                                    }
                                    if(tablePanel1[0] != null){
                                        layeredPane.remove(tablePanel1[0]);
                                        tablePanel1[0] = null;
                                    }
                                    gm = null;
                                }else{
                                    if (pt < nt) {
                                        System.out.println("Player " + plr.getPlayerIndex() + " moved from " + pt + " to " + nt);
                                        publish( "\tP" + plr.getPlayerIndex() + ">>  moved from " + pt + " to " + nt);
                                    } else {
                                        System.out.println("Player " + plr.getPlayerIndex() + " still on " + pt);
                                        PlayerTurnState state = gm.getTurns().get(plr);
                                        if(state instanceof StoppedTurnState) publish("\tP" + plr.getPlayerIndex() + ">>  on " + nt + ", stopped for " + ((StoppedTurnState) state).getStops() + " turns");
                                        else publish("\tP" + plr.getPlayerIndex() + ">>  come back to " + nt);
                                    }
                                }

                                Thread.sleep(1000);

                                return null;
                            }

                            @Override
                            protected void process(List<String> chunks) {
                                for(String chunk : chunks) {
                                    JLabel label = new JLabel(chunk);
                                    //label.setAlignmentX(Component.LEFT_ALIGNMENT);
                                    turnsPanel.add(label);
                                    turnsPanel.revalidate();
                                    turnsPanel.repaint();
                                    turnsScrollPane.revalidate();
                                    turnsScrollPane.repaint();


                                    JScrollBar vertical = turnsScrollPane.getVerticalScrollBar();
                                    vertical.setValue(vertical.getMaximum());
                                }
                            }
                        };
                        manualworker.execute();
                    }
                });
                btnPanel.add(goOn);
                btnPanel.revalidate();
                btnPanel.repaint();
            }

            for(Player p : gm.getTurns().keySet()){
                if(p.getLastTile() == 100) JOptionPane.showMessageDialog(this, "Player " + p.getPlayerIndex() + " won!");
            }

            // TODO implementing an observer for this
        });

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
