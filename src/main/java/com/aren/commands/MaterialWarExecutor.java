package com.aren.commands;

import com.aren.materialwar.MaterialWar;
import com.aren.utils.IF.ConfigDisplayInventory;
import com.aren.utils.gameManage.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class MaterialWarExecutor implements CommandExecutor {

    private static GameManager gameManager = MaterialWar.getGameManager();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player))
            return false;

        Player player = (Player) sender;

        if (label.equalsIgnoreCase("materialgame")) {
            if (args[0].equalsIgnoreCase("start")) {
                sender.sendMessage("게임이 시작되었습니다.");
                gameManager.onStart();
                return true;
            }

            if (args[0].equalsIgnoreCase("stop")) {
                sender.sendMessage("게임이 중단되었습니다.");
                gameManager.onStop();
                return false;
            }

            if (args[0].equalsIgnoreCase("config")) {
                sender.sendMessage("설정할 ui를 불러옵니다.");
                ConfigDisplayInventory inventory = new ConfigDisplayInventory();

                inventory.show(inventory.create(), player);
                return true;
            }

            if ((args[0].equalsIgnoreCase("join") || args[0].equalsIgnoreCase("leave")) && args.length == 2) {
                boolean isJoin = args[0].equalsIgnoreCase("join");
                if (args[1].equalsIgnoreCase("@a")) {
                    for (Player p : Bukkit.getOnlinePlayers()) {
                        gameManager.joinPlayer(p);
                        player.sendMessage("모든 플레이어가 참가했습니다.");
                        p.sendMessage("모든 플레이어가 참가했습니다.");

                        return true;
                    }
                }
                if (args[1].equalsIgnoreCase("@r")) {
                    Random rand = new Random();
                    int randomCount = rand.nextInt(Bukkit.getOnlinePlayers().size());
                    Player target = (Player) Bukkit.getOnlinePlayers().toArray()[randomCount];

                    gameManager.joinPlayer((Player) Bukkit.getOnlinePlayers().toArray()[randomCount]);
                    player.sendMessage("랜덤한 플레이어 " + target.getName() + "님이 참가했습니다.");
                    target.sendMessage("랜덤한 플레이어 " + target.getName() + "님이 참가했습니다.");
                    return true;
                }
                if (args[1].equalsIgnoreCase("@p") && args[1].equalsIgnoreCase("@s")) {
                    gameManager.joinPlayer(player);
                    player.sendMessage(player.getName() + "님이 참가하였습니다.");
                    return true;
                }
                if (Bukkit.getPlayer(args[1]) != null) {
                    gameManager.joinPlayer(Bukkit.getPlayer(args[1]));
                    player.sendMessage(Bukkit.getPlayer(args[1]).getName() + "님이 참가하였습니다.");

                    return true;
                }

            } else {
                sender.sendMessage(ChatColor.RED + "불완전한 명령어입니다.");
            }

        }

        return false;
    }
}
