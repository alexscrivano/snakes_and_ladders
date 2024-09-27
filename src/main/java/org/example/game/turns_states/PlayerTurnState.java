package org.example.game.turns_states;

import org.example.game.GameManager;
import org.example.game.commands.Command;
import org.example.support.Player;

public interface PlayerTurnState {
    void move(GameManager game, Player player, Command command);
}
