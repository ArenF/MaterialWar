package com.aren.materialwar;

import com.aren.skills.DiamondSkill;
import com.aren.skills.GoldSkill;
import com.aren.skills.IronSkill;
import com.aren.utils.GameManager;
import com.aren.utils.SkillManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class MaterialWar extends JavaPlugin {

    private static JavaPlugin plugin = null;
    private static GameManager gameManager = GameManager.getInstance();
    private static SkillManager skillManager = SkillManager.getInstance();
    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;

        if (!skillManager.getFile().isEmpty()) {
            // 혹여 모를 초기화
            skillManager.getFile().createFile();
            DiamondSkill diamondSkill = new DiamondSkill(3.0f, 2);
            IronSkill ironSkill = new IronSkill(15.0f, 5);
            GoldSkill goldSkill = new GoldSkill(12.0f, 0.5);

            skillManager.addNSave(diamondSkill);
            skillManager.addNSave(ironSkill);
            skillManager.addNSave(goldSkill);
        }


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static GameManager getGameManager() {
        return gameManager;
    }
}
