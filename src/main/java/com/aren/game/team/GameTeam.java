package com.aren.game.team;

import com.aren.config.ConfigFile;
import com.aren.config.ConfigManager;
import com.aren.utils.player.GamePlayer;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class GameTeam {

    private static GameTeam instance = new GameTeam();
    private ConfigFile configFile = ConfigManager.getInstance().getConfigFile("gameConfig");
    private List<GamePlayer> gamePlayers = new ArrayList<>();
    private ScoreboardManager scoreboardManager = Bukkit.getScoreboardManager();
    private List<Team> teamList = new ArrayList<>();

    public static GameTeam getInstance() {
        if (instance == null) {
            instance = new GameTeam();
        }
        return instance;
    }

    public GameTeam() {
        load();
    }

    public Team create(String name, NamedTextColor color) {
        Scoreboard scoreboard = scoreboardManager.getMainScoreboard();

        Team team = scoreboard.getTeam(name) == null ? scoreboard.registerNewTeam(name) : scoreboard.getTeam(name);
        team.color(color);
        team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.ALWAYS);

        teamList.add(team);
        configFile.getConfig().set("game.team", team);

        return team;
    }

    public void join(String name, Player player) {
        for (Team team : teamList) {
            if (!team.getName().equals(name))
                continue;
            team.addEntry(player.getName());
        }
    }

    public void leave(String name, Player player) {
        for (Team team : teamList) {
            if (!team.getName().equals(name))
                continue;
            team.removeEntry(player.getName());
        }
    }

    public void load() {
        if (configFile.getConfig().isSet("game.team")) {

            Set<String> keys = configFile.getConfig().getConfigurationSection("game.team").getKeys(false);

            for (String teamName : keys) {
                String a = configFile.getConfig().getString("game.team." + teamName);
                teamList.add(create(teamName, NamedTextColor.NAMES.value(a)));
            }
        }
    }

}
