package com.aren.utils;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;

public class DamageTeritory {
//        월드에서 받아온 모든 엔티티를 감지해서 해당 거리에 속해 있으면 리스트에 추가하여 넣는다.
    public static List<Entity> nearbyEntity(Location location, double range) {
        List<Entity> entity = new ArrayList<>();

        /*
        *
        * 해당 코드를 사용하는 예시를 적어두기
        *
        * for (Entity e : nearbyEntity(loc, 3.5)) {
            if (!(e instanceof LivingEntity))
                continue;
            if (e == owner) {
                continue;
            }
            if (e instanceof Player) {
                ((Player) e).damage(6, owner);
            } else {
                ((LivingEntity) e).damage(6, owner);
            }
        }
        *
        */

        for (Entity e: location.getWorld().getEntities()) {
            if (location.distance(e.getLocation()) <= range) {
                entity.add(e);
            }
        }

        return entity;
    }
}
