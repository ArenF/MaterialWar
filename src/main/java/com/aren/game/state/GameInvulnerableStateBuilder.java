package com.aren.game.state;

import com.aren.ability.AbilityType;
import com.aren.ability.factory.MaterialAbilityFactory;
import com.aren.config.ConfigFile;
import com.aren.config.ConfigManager;
import com.aren.game.GameManager;
import com.aren.utils.GamePlayer;
import com.aren.utils.TimerBar;
import org.bukkit.boss.BarColor;
import org.bukkit.entity.Player;

import java.util.List;

public class GameInvulnerableStateBuilder extends GameStateBuilder {

    private List<GamePlayer> players;
    private TimerBar bar;
    private ConfigFile gameConfig;
    private GameManager gameManager = GameManager.getInstance();

    public GameInvulnerableStateBuilder(List<GamePlayer> gamePlayers) {
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
        for (GamePlayer player : players) {
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
            for (GamePlayer player : players) {
                if (!player.getUniqueId().equals(event.getEntity().getUniqueId())) {
                    continue;
                }
                event.setCancelled(true);
                player.getPlayer().sendMessage("플레이어의 데미지가 차단되었습니다.");
            }
        });
    }

    @Override
    protected void setWorldborder() {

    }

    @Override
    protected void activateMessage() {
        for (GamePlayer player : players) {
            player.getPlayer().sendMessage(format("무적시간이 정해졌습니다."));
        }
    }

    @Override
    protected void deactivateMessage() {
        for (GamePlayer player : players) {
            player.getPlayer().sendMessage(format("게임이 중단되었습니다."));
        }
    }
}
