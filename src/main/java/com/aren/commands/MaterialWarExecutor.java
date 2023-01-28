package com.aren.commands;

import com.aren.materialwar.MaterialWar;
import com.aren.utils.GameManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MaterialWarExecutor implements CommandExecutor {

    private static GameManager gameManager = MaterialWar.getGameManager();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!commandOnlyPlayer(sender))
            return false;

        if (label.equalsIgnoreCase("materialgame")) {
            if (args[0].equalsIgnoreCase("start")) {
                gameManager.onStart();
            }
        }

        return false;
    }

    private boolean commandOnlyPlayer(CommandSender sender) {
        sender.sendMessage("해당 메세지는 오직 플레이어만 사용할 수 있습니다.");
        return sender instanceof Player;
    }
}
