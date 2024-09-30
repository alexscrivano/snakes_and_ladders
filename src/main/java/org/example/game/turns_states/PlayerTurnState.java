package org.example.game.turns_states;

import org.example.game.GameManager;
import org.example.game.commands.Command;
import org.example.support.Player;
import java.io.Serializable;

public interface PlayerTurnState extends Serializable {
    void move(GameManager game, Player player, Command command);
}
