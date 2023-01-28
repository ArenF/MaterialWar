package com.aren.utils;

import com.aren.skills.DiamondSkill;
import com.aren.skills.MaterialSkills;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class GameManager {

    private static GameManager instance;
    private static SkillManager skillManager = SkillManager.getInstance();

    GameState state = GameState.WAITING;
    List<GamePlayer> players = new ArrayList<>();

    public GameManager() {}

    public static GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public GamePlayer getGamePlayer(UUID uuid) {
        GamePlayer gamePlayer = null;
        for (GamePlayer player : players) {
            if (player.getUuid() != uuid) {
                continue;
            }
            gamePlayer = player;
        }
        return gamePlayer;
    }

    public void onStart() {
        state = GameState.STARTING;
        for (Player player : Bukkit.getOnlinePlayers()) {
            players.add(new GamePlayer(player.getUniqueId(), player.getName()));
        }

        for (GamePlayer user : players) {
//          내용란 : 스킬 추가
            user.addAllSkill(skillManager.getSkills());
        }
    }
}
