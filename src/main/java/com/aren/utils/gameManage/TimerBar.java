package com.aren.utils.gameManage;

import com.aren.events.EventManager;
import com.aren.utils.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;

public class TimerBar {
    private int taskID = -1;
    private final JavaPlugin plugin;
    private BossBar bar;

    public TimerBar(JavaPlugin plugin, int taskID) {
        this.plugin = plugin;
        this.taskID = taskID;
    }

    public TimerBar(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void addPlayer(Player player) {
        bar.addPlayer(player);
    }

    public BossBar getBar() {
        return bar;
    }

    public void createBar(String name) {
        bar = Bukkit.createBossBar(name, BarColor.RED, BarStyle.SOLID);
        bar.setVisible(true);
    }

    public void cast() {
        int timer = (int) ConfigManager.getInstance().getConfig("GameConfig").get("Game.time");

        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {

            double progress = 1.0;
            double time = progress / timer;

            @Override
            public void run() {
                if (progress == 0) {
                    for (Player player : bar.getPlayers()) {
                        bar.removePlayer(player);
                    }
                    Bukkit.getScheduler().cancelTask(taskID);
                }
                bar.setProgress(progress);

                String title = bar.getTitle();

                if (title.contains(":")) {
                    if (title.split(" : ")[1] != "") {
                        title = title.split(" : ")[0];
                    }
                }

                bar.setTitle(format("&6" + title + " : " + Math.round(progress * timer)));

//                bossbar는 무조건 1.0 ~ 0.0의 반경에 안착해 있어야 한다.
                progress = progress - time < 0 ? 0 : progress - time;
            }
        }, 0, 20);
    }

    public void cast(Consumer<EntityDamageEvent> event) {
        int timer = (int) ConfigManager.getInstance().getConfig("GameConfig").get("Game.invincible_duration");

        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {

            double progress = 1.0;
            double time = progress / timer;

            @Override
            public void run() {
                if (progress == 0) {
                    for (Player player : bar.getPlayers()) {
                        bar.removePlayer(player);
                    }
                    Bukkit.getScheduler().cancelTask(taskID);
                }
                bar.setProgress(progress);

                String title = bar.getTitle();

                if (title.contains(":")) {
                    if (title.split(" : ")[1] != "") {
                        title = title.split(" : ")[0];
                    }
                }

                bar.setTitle(format("&6" + title + " : " + Math.round(progress * timer)));

                EventManager.getInstance().setDamageEventConsumer(event);

//                bossbar는 무조건 1.0 ~ 0.0의 반경에 안착해 있어야 한다.
                progress = progress - time < 0 ? 0 : progress - time;
            }
        }, 0, 20);
    }

    public static String format(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
