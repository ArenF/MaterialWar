package com.aren.game.state;

import com.aren.ability.AbilityType;
import com.aren.ability.factory.MaterialAbilityFactory;
import com.aren.config.ConfigFile;
import com.aren.config.ConfigManager;
import com.aren.utils.GamePlayer;
import com.aren.utils.TimerBar;
import com.aren.utils.WorldBarrier;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;

import java.util.List;

public class GameStartStateBuilder extends GameStateBuilder{

    private List<GamePlayer> users;
    private TimerBar timerBar;
    private ConfigFile gameConfig;

    public GameStartStateBuilder(List<GamePlayer> users) {

        this.users = users;
        gameConfig = ConfigManager.getInstance().getConfigFile("gameConfig");
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
        MaterialAbilityFactory factory = new MaterialAbilityFactory();
//        플레이어들이 사용할 스킬들을 활성화
        for (GamePlayer player: users) {
            player.addAbility(AbilityType.DIAMOND, factory.createAbility(AbilityType.DIAMOND, player));
            player.addAbility(AbilityType.IRON, factory.createAbility(AbilityType.IRON, player));
            player.addAbility(AbilityType.GOLD, factory.createAbility(AbilityType.GOLD, player));
        }
    }

    @Override
    protected void changeState() {
        setState(GameState.STARTING);
    }

    @Override
    protected void setWorldborder() {
        WorldBarrier worldBarrier = new WorldBarrier(gameConfig.getConfig().getLocation("game.location"), timerBar);
        worldBarrier.run();
    }

    @Override
    protected void activateMessage() {
        for (GamePlayer player : users) {
            player.getPlayer().sendMessage(format("무적시간이 활성화되었습니다."));
        }
    }

    @Override
    protected void deactivateMessage() {
        for (GamePlayer player : users) {
            player.getPlayer().sendMessage(format("무적시간이 끝났습니다."));
        }
    }

}
