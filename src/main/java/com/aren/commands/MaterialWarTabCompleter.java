package com.aren.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MaterialWarTabCompleter implements TabCompleter {

    List<String> arguments = new ArrayList<>();
    List<String> playerable = new ArrayList<>();

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (arguments.isEmpty()) {
            arguments.add("start"); arguments.add("config"); arguments.add("stop"); arguments.add("join"); arguments.add("leave");
        }
        if (playerable.isEmpty()) {
            playerable.add("@a"); playerable.add("@p"); playerable.add("@r"); playerable.add("@s");

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
        if (args.length == 2 && args[0].equalsIgnoreCase("join")) {
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

        return null;
    }
}