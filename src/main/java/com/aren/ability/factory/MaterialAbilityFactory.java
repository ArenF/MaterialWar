package com.aren.ability.factory;

import com.aren.ability.*;
import com.aren.utils.player.GamePlayer;

public class MaterialAbilityFactory implements AbilityFactory {
    @Override
    public MaterialAbility createAbility(AbilityType type, GamePlayer player) {

        MaterialAbility effect = null;

        switch (type) {
            case DIAMOND:
                effect = new DiamondAbility(player);
                break;
            case IRON:
                effect = new IronAbility(player);
                break;
            case GOLD:
                effect = new GoldAbility(player);
                break;
            case COAL:
                effect = new CoalAbility(player);
                break;
            case STONE:
                effect = new StoneAbility(player);
                break;
            case EMERALD:
                effect = new EmeraldAbility(player);
                break;
            case REDSTONE:
                effect = new RedstoneAbility(player);
                break;
            case LAPIS:
                effect = new LapisAbility(player);
                break;
        }

        return effect;
    }
}
