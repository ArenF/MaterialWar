package com.aren.commands;

import com.aren.config.ConfigFile;
import com.aren.config.ConfigManager;
import com.aren.game.GameManager;
import com.aren.game.state.GameState;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class MaterialWarExecutor implements CommandExecutor {

    GameManager gameManager = GameManager.getInstance();
    ConfigManager configManager = ConfigManager.getInstance();
    ConfigFile configFile = configManager.getConfigFile("gameConfig");

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player))
            return false;

        if (label.equalsIgnoreCase("MaterialWar")) {
            if (args.length == 0) {
                sender.sendMessage(ChatColor.RED + "해당 메세지는 완성되지 않았습니다.");
                return false;
            }
            if (args[0].equalsIgnoreCase("start")) {
                if (gameManager.getParticipants().size() == 0) {
                    gameManager.sendMessage("현재 플레이어가 모여있지 않습니다.");
                    return false;
                }
                if (configFile == null || configFile.isEmpty()) {
                    sender.sendMessage("게임 설정 파일이 존재하지 않습니다.");
                    return false;
                }

                boolean isInvulnerable = configFile.getConfig().getBoolean("game.invulnerable");

                if (isInvulnerable) {
                    gameManager.start(GameState.INVULNERABLE);
                } else {
                    gameManager.start(GameState.STARTING);
                }
                configFile.getConfig().set("game.location", ((Player) sender).getLocation());
                configFile.save();
                return true;
            } else if (args[0].equalsIgnoreCase("join")) {
                if (args.length == 1) {
                    sender.sendMessage(ChatColor.RED + "해당 메세지는 완성되지 않았습니다.");
                    return false;
                }
                if (args[1].equalsIgnoreCase("@a")) {
                    for (Player player : Bukkit.getOnlinePlayers())
                        gameManager.joinPlayer(player);
                } else if (args[1].equalsIgnoreCase("@r")) {
                    Random rand = new Random();
                    int i = rand.nextInt(Bukkit.getOnlinePlayers().toArray().length);
                    Player player = (Player) Bukkit.getOnlinePlayers().toArray()[i];
                    gameManager.joinPlayer(player);
                    return true;
                } else if (args[1].equalsIgnoreCase("@s")) {
                    Player player = (Player) sender;
                    gameManager.joinPlayer(player);
                    return true;
                } else if (Bukkit.getPlayer(args[1]) != null) {
                    Player player = Bukkit.getPlayer(args[1]);
                    gameManager.joinPlayer(player);
                    return true;
                } else {
                    gameManager.sendMessage("해당 플레이어를 찾을 수 없습니다.");
                }
            } else if (args[0].equalsIgnoreCase("stop")) {
                gameManager.stop();
            }
        }

        return false;
    }
}
