package com.aren.utils.gameManage;

import com.aren.materialwar.MaterialWar;
import com.aren.utils.ConfigManager;
import com.aren.utils.GameState;
import com.aren.utils.data.ConfigFile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameManager {

    private static GameManager instance;
    private static ConfigManager configManager = ConfigManager.getInstance();

    private TimerBar invincible_bar;
    private TimerBar bar;
    private List<Player> participants = new ArrayList<Player>();

    GameState state = GameState.WAITING;

    public GameManager() {}

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public void onStart() {
        state = GameState.STARTING;

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(ChatColor.GREEN + "현재 참가한 플레이어들");
            StringBuilder builder = new StringBuilder();
            for (Player participant : participants) {
                builder.append(participant.getName());

                if (participant != participants.get(participants.size() - 1)) {
                    builder.append(", ");
                }
            }
            p.sendMessage(ChatColor.YELLOW + builder.toString());

            p.sendMessage(ChatColor.GREEN + "게임을 시작합니다.");
        }

//        빌더 패턴을 이용해 순서를 무작위로 설정할 수 있을 듯으로 보임.
        giveStartItem();
        castGameTimer(participants);
        if ((int) configManager.getConfig("GameConfig").get("Game.invincible_duration") > 0) {
            castInvincibleTimer(participants);
        }

    }

    public void joinPlayer(Player player) {
        participants.add(player);
    }

    private void giveStartItem() {
        ConfigFile configFile = configManager.getConfig("GameConfig");
        List<ItemStack> itemStacks = (List<ItemStack>) configFile.getConfiguration().getList("game.default_item");
        for (Player player : participants) {
            for (ItemStack item : itemStacks) {
                player.getInventory().addItem(item);
            }
        }
    }

    private void castInvincibleTimer(List<Player> players) {
        invincible_bar = new TimerBar(MaterialWar.getPlugin());
        invincible_bar.createBar(TimerBar.format("무적시간이 끝나기까지"));

        for (Player player : players) {
            invincible_bar.addPlayer(player);
        }

        invincible_bar.cast(event -> {
            if (!(event.getEntity() instanceof Player)) {
                return;
            }
            Player target = (Player) event.getEntity();

            for (Player player : invincible_bar.getBar().getPlayers()) {
                if (target != player)
                    continue;
                event.setCancelled(true);
            }
        });

    }

    private void castGameTimer(List<Player> players) {
        bar = new TimerBar(MaterialWar.getPlugin());
        bar.createBar(TimerBar.format("게임이 끝나기까지"));
        for (Player player : players) {
            bar.addPlayer(player);
        }
        bar.cast();
    }

    public void stopTimerBar(TimerBar timerBar) {
        timerBar.getBar().removeAll();
    }

    public void onStop() {
        state = GameState.END_OF;

        stopTimerBar(bar);
        stopTimerBar(invincible_bar);

        for (Player player : participants) {
            participants.remove(player);
        }
    }

}
