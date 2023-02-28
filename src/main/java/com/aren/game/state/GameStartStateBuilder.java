package com.aren.game.state;

import com.aren.ability.AbilityType;
import com.aren.ability.factory.MaterialAbilityFactory;
import com.aren.config.ConfigFile;
import com.aren.config.ConfigManager;
import com.aren.game.team.GameTeam;
import com.aren.utils.player.GamePlayer;
import com.aren.utils.TimerBar;
import com.aren.utils.WorldBarrier;
import com.aren.utils.player.PlayerState;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.json.simple.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class GameStartStateBuilder extends GameStateBuilder{

    private HashMap<UUID, GamePlayer> users;
    private TimerBar timerBar;
    private ConfigFile gameConfig;
    private ConfigFile playerConfig;

    public GameStartStateBuilder(HashMap<UUID, GamePlayer> users) {
        this.users = users;
        gameConfig = ConfigManager.getInstance().getConfigFile("gameConfig");
        playerConfig = ConfigManager.getInstance().getConfigFile("playerConfig");
    }

    @Override
    public TimerBar getTimerBar() {
        return timerBar;
    }

    @Override
    protected void castTimer() {

        String timeString = gameConfig.getConfig().getString("game.time");

        String str = timeString.replaceAll("m", "").replaceAll("s", "");

        int minute = Integer.parseInt(str.split(" ")[0]) * 60;
        int second = Integer.parseInt(str.split(" ")[1]);

        timerBar = new TimerBar("게임이 끝나기까지 time 남았습니다.", minute + second, BarColor.RED, users);
        timerBar.createBar();

    }

    @Override
    protected void closeTimer() {
        timerBar.close();

    }

    @Override
    protected void managePlayers() {
        FileConfiguration config = playerConfig.getConfig();
        MaterialAbilityFactory factory = new MaterialAbilityFactory();
        List<String> names = new ArrayList<>();
//        플레이어들이 사용할 스킬들을 활성화
        for (GamePlayer player: users.values()) {
            for (AbilityType type : AbilityType.values()) {
                player.addAbility(type, factory.createAbility(type, player));
            }

            names.add(player.getName());
        }

        config.set(PlayerState.ALIVE.name(), names);
        playerConfig.save();

    }

    @Override
    protected void changeState() {
        setState(GameState.STARTING);
    }

    @Override
    protected void activateWorldBorder() {
        worldBarrier.setTimer(timerBar);
        worldBarrier.run();
    }

    @Override
    protected void deactivateWorldBorder() {
        worldBarrier.stop();
    }

    @Override
    protected void activateMessage() {
        for (GamePlayer player : users.values()) {
            player.getPlayer().sendMessage(format("게임이 시작되었습니다."));
        }
    }

    @Override
    protected void deactivateMessage() {
        for (GamePlayer player : users.values()) {
            player.getPlayer().sendMessage(format("게임이 끝났습니다."));
        }
    }

}
