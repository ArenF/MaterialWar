package com.aren.ability.factory;

import com.aren.ability.AbilityType;
import com.aren.ability.MaterialAbility;
import com.aren.utils.GamePlayer;

public interface AbilityFactory {
    MaterialAbility createAbility(AbilityType type, GamePlayer player);

}
