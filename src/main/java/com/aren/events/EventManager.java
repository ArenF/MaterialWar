package com.aren.events;

import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.function.Consumer;

public class EventManager {

    private static EventManager INSTANCE = new EventManager();
    private Consumer<EntityDamageEvent> damageEventConsumer = null;
    private Consumer<PlayerJoinEvent> playerJoinEventConsumer = null;


    public EventManager() {}

    public static EventManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new EventManager();
        }
        return INSTANCE;
    }

    public void setDamageEventConsumer(Consumer<EntityDamageEvent> damageByEntityEventConsumer) {
        this.damageEventConsumer = damageByEntityEventConsumer;
    }

    public void setPlayerJoinEventConsumer(Consumer<PlayerJoinEvent> playerJoinEventConsumer) {
        this.playerJoinEventConsumer = playerJoinEventConsumer;
    }

    public Consumer<EntityDamageEvent> getDamageEventConsumer() {
        return damageEventConsumer;
    }

    public Consumer<PlayerJoinEvent> getPlayerJoinEventConsumer() {
        return playerJoinEventConsumer;
    }
}
