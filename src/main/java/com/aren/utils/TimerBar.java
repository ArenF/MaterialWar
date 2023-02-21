package com.aren.utils;

import com.aren.game.GameManager;
import com.aren.game.state.GameState;
import com.aren.materialwar.MaterialWar;
import com.aren.utils.player.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class TimerBar {

    private GameManager gameManager = GameManager.getInstance();

    private int time;
    private String title;
    private BarColor color;
    private HashMap<UUID, GamePlayer> players;
    private int taskId = -1;
    private boolean closed = false;
    private BossBar bar;

    public TimerBar(String title, int time, BarColor color, HashMap<UUID, GamePlayer> players) {
        this.time = time;
        this.title = title;
        this.color = color;
        this.players = players;
    }

    public BossBar getBar() {
        return bar;
    }

    public void createBar() {
        bar = Bukkit.createBossBar(title, color, BarStyle.SOLID);
        bar.setVisible(true);
        cast();
    }

    public void cast() {

        for (GamePlayer player : players.values()) {
            bar.addPlayer(player.getPlayer());
        }

        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(MaterialWar.getPlugin(), new Runnable() {

            double progress = 1.0;
            double timer = 1.0/time;

            @Override
            public void run() {
                bar.setProgress(progress);

                if (progress == 0) {
                    close();
                    Bukkit.getScheduler().cancelTask(taskId);
                }
                String title = bar.getTitle();

                double title_progress = progress * time;
                int minute = (int) title_progress / 60;
                int second = (int) title_progress % 60;
                String display_time = minute + "분 " + second + "초";

                String display_title = "";
                if (title.contains("time")) {
                    display_title = title.replaceAll("time", display_time);
                } else {
                    int check_minute = (int) (title_progress + 1) / 60;
                    int check_second = (int) (title_progress + 1) % 60;
                    display_title = title.replaceAll(check_minute + "분 " + check_second + "초", display_time);
                }

                bar.setTitle(display_title);

                progress = progress - timer < 0 ? 0 : progress - timer;
            }
        }, 0, 1);
    }

    public void close() {
        if (isClosed()) {
            return;
        }

        setClosed(true);
        bar.removeAll();

        if (gameManager.getState().equals(GameState.INVULNERABLE)) {
            gameManager.start(GameState.STARTING);
        }
    }

    private boolean isClosed() {
        return closed;
    }

    private void setClosed(boolean isTrue) {
        closed = isTrue;
    }
}
