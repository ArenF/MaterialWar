package com.aren.commands;

import com.aren.ability.AbilityType;
import com.aren.config.ConfigFile;
import com.aren.config.ConfigManager;
import com.aren.game.GameManager;
import com.aren.game.state.GameState;
import com.aren.game.team.GameTeam;
import com.aren.utils.player.GamePlayer;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Random;
import java.util.regex.Pattern;

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
//            게임 시작 명령어
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

                if (!configFile.getConfig().isLocation("game.startLocation")) {
                    configFile.getConfig().set("game.startLocation", ((Player) sender).getLocation());
                    configFile.save();
                }

                if (isInvulnerable) {
                    gameManager.start(GameState.INVULNERABLE);
                } else {
                    gameManager.start(GameState.STARTING);
                }
                return true;
//                게임 참여 명령어
//
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
                } else if (args[1].equalsIgnoreCase("@s") || args[1].equalsIgnoreCase("@p")) {
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
//                게임 참여 나가기 명령어
//
            } else if (args[0].equalsIgnoreCase("leave")) {
                if (args.length == 1) {
                    sender.sendMessage(ChatColor.RED + "해당 메세지는 완성되지 않았습니다.");
                    return false;
                }
                if (args[1].equalsIgnoreCase("@a")) {
                    for (Player player : Bukkit.getOnlinePlayers())
                        gameManager.leavePlayer(player);
                } else if (args[1].equalsIgnoreCase("@r")) {
                    Random rand = new Random();
                    int i = rand.nextInt(Bukkit.getOnlinePlayers().toArray().length);
                    Player player = (Player) Bukkit.getOnlinePlayers().toArray()[i];
                    gameManager.leavePlayer(player);
                    return true;
                } else if (args[1].equalsIgnoreCase("@s") || args[1].equalsIgnoreCase("@p")) {
                    Player player = (Player) sender;
                    gameManager.leavePlayer(player);
                    return true;
                } else if (Bukkit.getPlayer(args[1]) != null) {
                    Player player = Bukkit.getPlayer(args[1]);
                    gameManager.leavePlayer(player);
                    return true;
                } else {
                    gameManager.sendMessage("해당 플레이어를 찾을 수 없습니다.");
                }
//                게임 멈추기
//
            } else if (args[0].equalsIgnoreCase("stop")) {
                gameManager.stop();
//                게임 콘피그 설정
//
            } else if (args[0].equalsIgnoreCase("config")) {
                if (args.length == 1) {
                    sender.sendMessage(ChatColor.RED + "해당 메세지는 완성되지 않았습니다.");
                    return false;
                }
                if (args[1].equalsIgnoreCase("load")) {
                    sender.sendMessage("모든 콘피그가 초기화되었습니다.");
                    configManager.load();
                    for (GamePlayer player : gameManager.getParticipants().values()) {
                        for (AbilityType type : AbilityType.values()) {
                            player.getAbility(type).load();
                        }
                    }
                }
                if (args[1].equalsIgnoreCase("set")) {
                    if (args.length != 5) {
                        sender.sendMessage(ChatColor.RED + "해당 메세지는 완성되지 않았습니다.");
                        sender.sendMessage(ChatColor.YELLOW + "/MaterialWar config set {gameConfig|skillConfig} [key] [value]");
                        return false;
                    }
                    String filename = args[2];
                    String key = args[3];
                    String value = args[4];
                    Object resultValue = null;

                    if (Pattern.matches("^(true)$", value) || Pattern.matches("^(false)$", value))
                        resultValue = Boolean.parseBoolean(value);
                    else if (Pattern.matches("^[0-9]*$", value))
                        resultValue = Integer.parseInt(value);
                    else if (Pattern.matches("^[0-9]+\\.?[0-9]+$", value))
                        resultValue = Double.parseDouble(value);
                    else if (Pattern.matches("^[a-zA-Z]*$", value))
                        resultValue = value;

                    ConfigFile configFile = configManager.getConfigFile(filename);
                    configFile.getConfig().set(key, resultValue);
                    configFile.save();
                }
                if (args[1].equalsIgnoreCase("get")) {
                    if (args.length != 4) {
                        sender.sendMessage(ChatColor.RED + "해당 메세지는 완성되지 않았습니다.");
                        sender.sendMessage(ChatColor.YELLOW + "/MaterialWar config get {gameConfig|skillConfig} [key]");
                        return false;
                    }
                    String filename = args[2];
                    String key = args[3];
                    ConfigFile configFile = configManager.getConfigFile(filename);
                    if (!configFile.getConfig().isSet(key)) {
                        sender.sendMessage(ChatColor.RED + "해당 파일에 데이터가 존재하지 않습니다.");
                        return false;
                    }

                    sender.sendMessage(Objects.requireNonNull(configFile.getConfig().getString(key)));
                }
            } else if (args[0].equalsIgnoreCase("team")) {
                GameTeam team = GameTeam.getInstance();
                if (args.length == 1) {
                    sender.sendMessage(ChatColor.RED + "해당 메세지는 완성되지 않았습니다.");
                    return false;
                }
                if (args[1].equalsIgnoreCase("create")) {
                    if (args.length != 4) {
                        sender.sendMessage(ChatColor.RED + "해당 메세지는 완성되지 않았습니다.");
                        sender.sendMessage(ChatColor.YELLOW + "/MaterialWar team create (Name) (Color)");
                        return false;
                    }
                    String name = args[2];
                    NamedTextColor color = NamedTextColor.NAMES.value(args[3]);

                    team.create(name, color);
                }
                if (args[1].equalsIgnoreCase("join")) {
                    String name = args[2];
                    Player player = Bukkit.getPlayer(args[3]);

                    team.join(name, player);
                }
                if (args[1].equalsIgnoreCase("leave")) {
                    String name = args[2];
                    Player player = Bukkit.getPlayer(args[3]);

                    team.leave(name, player);
                }
            }
        }

        return false;
    }
}
