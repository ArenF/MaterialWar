package com.aren.ability.factory;

import com.aren.ability.*;
import com.aren.utils.GamePlayer;

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
        }

        return effect;
    }
}
