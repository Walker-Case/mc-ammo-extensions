package com.walkercase.efm;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class EFMConfig {

    static{
        final Pair<Common, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(Common::new);
        commonSpec = commonSpecPair.getRight();
        COMMON = commonSpecPair.getLeft();
    }

    public static final ForgeConfigSpec commonSpec;
    public static final EFMConfig.Common COMMON;

    public static class Common{
        public final Raiders raiders;
        public final Loot loot;
        public final AmmoExtensions ammoExtensions;

        public Common(ForgeConfigSpec.Builder builder){
            builder.push("Common");
            {
                this.raiders = new Raiders(builder);
                this.loot = new Loot(builder);
                this.ammoExtensions = new AmmoExtensions(builder);
            }
            builder.pop();
        }
    }

    public static class Loot{
        public final ForgeConfigSpec.ConfigValue<Long> refillLootTime;

        public Loot(ForgeConfigSpec.Builder builder){
            builder.comment("Control loot values.").push("loot");
            {
                this.refillLootTime = builder.comment("How long to wait before refilling loot in ms.").define("refillLootTime", 900000L);
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

    public static class Raiders
    {
        public final ForgeConfigSpec.ConfigValue<Long> raiderSpawnDelay;
        public final ForgeConfigSpec.ConfigValue<Double> raiderSpawnRange;
        public final ForgeConfigSpec.ConfigValue<Integer> maxGroupsPerSpawn;
        public final ForgeConfigSpec.ConfigValue<Integer> groupSpawnSizeMin;
        public final ForgeConfigSpec.ConfigValue<Integer> groupSpawnSize;
        public final ForgeConfigSpec.ConfigValue<Integer> spawnDistanceFromPlayer;

        public Raiders(ForgeConfigSpec.Builder builder)
        {
            builder.comment("Control Raider entity.").push("raiders");
            {
                builder.comment("Raider Spawns").push("raider-spawn");
                {
                    this.raiderSpawnDelay = builder
                            .comment("How long to wait until a new raider spawn wave will happen.")
                            .define("raiderSpawnDelay", 15000L);
                    this.raiderSpawnRange = builder.comment("How far away raiders will spawn")
                            .define("raiderSpawnRange",350d);
                    this.maxGroupsPerSpawn = builder.comment("Maximum number of raider groups that will spawn at one time.")
                            .define("maxGroupsPerSpawn", 10);
                    this.groupSpawnSizeMin = builder.comment("Minimum number of raiders to spawn in each group")
                            .define("groupSpawnSizeMin", 3);
                    this.groupSpawnSize = builder.comment("The max size of raider groups.")
                            .define("groupSpawnSize", 15);
                    this.spawnDistanceFromPlayer = builder.comment("Distance from player at which raiders can start spawning.")
                            .define("spawnDistanceFromPlayer", 60);
                }
                builder.pop();
            }
            builder.pop();
        }
    }
}
