package com.aren.game.state;

import com.aren.ability.AbilityType;
import com.aren.ability.factory.MaterialAbilityFactory;
import com.aren.config.ConfigFile;
import com.aren.config.ConfigManager;
import com.aren.game.GameManager;
import com.aren.utils.player.GamePlayer;
import com.aren.utils.TimerBar;
import com.aren.utils.WorldBarrier;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class GameInvulnerableStateBuilder extends GameStateBuilder {

    private HashMap<UUID, GamePlayer> players;
    private TimerBar bar;
    private ConfigFile gameConfig;
    private GameManager gameManager = GameManager.getInstance();

    public GameInvulnerableStateBuilder(HashMap<UUID, GamePlayer> gamePlayers) {
        players = gamePlayers;
        gameConfig = ConfigManager.getInstance().getConfigFile("gameConfig");
    }

    @Override
    protected void castTimer() {
        String timeString = gameConfig.getConfig().getString("game.invulnerable_time");

        String str = timeString.replaceAll("m", "").replaceAll("s", "");

        int minute = Integer.parseInt(str.split(" ")[0]) * 60;
        int second = Integer.parseInt(str.split(" ")[1]);

        bar = new TimerBar("무적시간이 끝나기까지 time 가 남아있습니다.", minute + second, BarColor.WHITE, players);
        bar.createBar();
    }

    @Override
    protected void closeTimer() {
        bar.close();
        gameManager.setInvulnerableEventConsumer(null);
    }

    @Override
    protected void managePlayers() {
        MaterialAbilityFactory factory = new MaterialAbilityFactory();
        for (GamePlayer player : players.values()) {
            player.addAbility(AbilityType.DIAMOND, factory.createAbility(AbilityType.DIAMOND, player));
            player.addAbility(AbilityType.IRON, factory.createAbility(AbilityType.IRON, player));
            player.addAbility(AbilityType.GOLD, factory.createAbility(AbilityType.GOLD, player));
        }
    }

    @Override
    protected void changeState() {
        setState(GameState.INVULNERABLE);

        gameManager.setInvulnerableEventConsumer(event -> {
            if (!(event.getEntity() instanceof Player))
                return;
            for (GamePlayer player : players.values()) {
                if (!player.getUniqueId().equals(event.getEntity().getUniqueId())) {
                    continue;
                }
                event.setCancelled(true);
                player.getPlayer().sendMessage("플레이어의 데미지가 차단되었습니다.");
            }
        });
    }

    @Override
    protected void activateWorldBorder() {
        if (!gameConfig.getConfig().isLocation("game.startLocation")) {
            return;
        }

        worldBarrier.setTimer(null);
        worldBarrier.run();
    }

    @Override
    protected void deactivateWorldBorder() {
        worldBarrier.stop();
    }

    @Override
    protected void activateMessage() {
        for (GamePlayer player : players.values()) {
            player.getPlayer().sendMessage(format("무적시간이 정해졌습니다."));
        }
    }

    @Override
    protected void deactivateMessage() {
        for (GamePlayer player : players.values()) {
            player.getPlayer().sendMessage(format("무적시간이 끝났습니다."));
        }
    }
}
