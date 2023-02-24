package com.aren.commands;

import com.aren.config.ConfigManager;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MaterialWarTabCompleter implements TabCompleter {

    List<String> arguments = new ArrayList<>();
    List<String> playerable = new ArrayList<>();
    List<String> configs = new ArrayList<>();

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (arguments.isEmpty()) {
            arguments.add("start"); arguments.add("config");
            arguments.add("stop"); arguments.add("join");
            arguments.add("leave"); arguments.add("list");
            arguments.add("team"); arguments.add("load");
        }
        if (playerable.isEmpty()) {
            playerable.add("@a"); playerable.add("@p"); playerable.add("@r"); playerable.add("@s");

        }
        if (configs.isEmpty()) {
            configs.add("set"); configs.add("load");
            configs.add("get");
        }

        List<String> result = new ArrayList<String>();
        if (args.length == 1) {
            for (String a : arguments) {
                if (a.toLowerCase().startsWith(args[0].toLowerCase())) {
                    result.add(a);
                }
            }
            return result;
        }
        if (args.length == 2 && (args[0].equalsIgnoreCase("join") || args[0].equalsIgnoreCase("leave"))) {
            for(Player player : Bukkit.getOnlinePlayers()) {
                playerable.add(player.getName());
            }
            for (String a : playerable) {
                if (a.toLowerCase().startsWith(args[1].toLowerCase())) {
                    result.add(a);
                }
            }
            return result;
        }
        if (args.length == 2 && args[0].equalsIgnoreCase("config")) {
            for (String a : configs) {
                if (a.toLowerCase().startsWith(args[1].toLowerCase())) {
                    result.add(a);
                }
            }
            return result;
        }
        if (args.length >= 3 && args[0].equalsIgnoreCase("config")) {
            if (args[1].equalsIgnoreCase("set") || args[1].equalsIgnoreCase("get")) {
                List<String> configFiles = new ArrayList<>(ConfigManager.getInstance().getConfigFiles().keySet());
                for (String a : configFiles) {
                    if (a.toLowerCase().startsWith(args[2].toLowerCase())) {
                        result.add(a);
                    }
                }
                return result;
            }
        }
        if (args.length == 4 && args[0].equalsIgnoreCase("team")) {
            if (args[1].equalsIgnoreCase("create")) {
                for (String colorText : NamedTextColor.NAMES.keyToValue().keySet()) {
                    if (colorText.toLowerCase().startsWith(args[3].toLowerCase())) {
                        result.add(colorText);
                    }
                }
            }
            return result;
        }

        return null;
    }
}
