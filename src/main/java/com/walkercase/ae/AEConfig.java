package com.walkercase.ae;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class AEConfig {

    static{
        final Pair<Common, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(Common::new);
        commonSpec = commonSpecPair.getRight();
        COMMON = commonSpecPair.getLeft();
    }

    public static final ForgeConfigSpec commonSpec;
    public static final AEConfig.Common COMMON;

    public static class Common{
        public final AmmoExtensions ammoExtensions;

        public Common(ForgeConfigSpec.Builder builder){
            builder.push("Common");
            {
                this.ammoExtensions = new AmmoExtensions(builder);
            }
            builder.pop();
        }
    }

    public static class AmmoExtensions{
        public final ForgeConfigSpec.ConfigValue<Long> fractureTick;
        public final ForgeConfigSpec.ConfigValue<Float> fractureDamage;
        public final ForgeConfigSpec.ConfigValue<Long> fractureDuration;

        public AmmoExtensions(ForgeConfigSpec.Builder builder){
            builder.comment("Control ammo extension values.").push("loot");
            {
                this.fractureTick = builder.comment("How often to hurt the player with fracture in ms.").define("fractureTick", 500L);
                this.fractureDamage = builder.comment("How much damage to do each tick.").define("fractureDamage", 1.5f);
                this.fractureDuration = builder.comment("How long fracture lasts for (per level).").define("fractureDuration", 1500L);
            }
            builder.pop();
        }
    }
}
