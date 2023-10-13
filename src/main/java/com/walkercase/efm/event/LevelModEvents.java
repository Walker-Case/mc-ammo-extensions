package com.walkercase.efm.event;

import com.walkercase.efm.EFMConfig;
import com.walkercase.efm.EFMMain;
import com.walkercase.efm.droptable.DropTables;
import com.walkercase.efm.entities.EFMEntities;
import com.walkercase.efm.entities.RaiderEntity;
import com.walkercase.efm.exception.UnkownTableException;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Random;

import static net.minecraftforge.fml.common.Mod.*;

@EventBusSubscriber(modid = EFMMain.MODID, bus = EventBusSubscriber.Bus.FORGE, value = Dist.DEDICATED_SERVER)
public class LevelModEvents{

        private static long lastTickContainer = 0;
        private static long lastTickRandomRaider = 0;
        private static Random random = new Random();

        /**
         * How often to refill loot.
         */
        private static long tickTimeContainer = EFMConfig.COMMON.loot.refillLootTime.get();

        private static final long tickTimeRaider = EFMConfig.COMMON.raiders.raiderSpawnDelay.get();

        private static final double raiderSpawnRange = EFMConfig.COMMON.raiders.raiderSpawnRange.get();

        private static final int raiderMaxGroupsPerSpawn = EFMConfig.COMMON.raiders.maxGroupsPerSpawn.get();

        private static final int raidersInSpot = EFMConfig.COMMON.raiders.groupSpawnSize.get();

        private static final int minRaidersInSpot = EFMConfig.COMMON.raiders.groupSpawnSizeMin.get();

        private static final int raiderDistanceFromPlayer = EFMConfig.COMMON.raiders.spawnDistanceFromPlayer.get();

        /**
         * The distance from the player to use in loot calculations.
         */
        private static int chunkLookSize = 8;
        @SubscribeEvent
        public static void onLevelTick(TickEvent.LevelTickEvent event){
            if(event.level.isClientSide())
                return;
            doRandomRaiderSpawn(event);
            doContainerDrops(event);
        }

        private static void doRandomRaiderSpawn(TickEvent.LevelTickEvent event){
            if(!(System.currentTimeMillis() > lastTickRandomRaider))
                return;

            lastTickRandomRaider = System.currentTimeMillis() + tickTimeRaider;

            for (Player player : event.level.players()) {
                if(player == null || player.isCreative())
                    continue;
                RandomSource randomsource = player.getRandom();
                RegistryObject<EntityType<RaiderEntity>> raiderEntity = EFMEntities.RAIDER_ENTITY;

                int raidersToSpawn = randomsource.nextInt(raiderMaxGroupsPerSpawn);

                if(raidersToSpawn < minRaidersInSpot)
                    raidersToSpawn += minRaidersInSpot - raidersToSpawn;


                for(int i=0;i<raidersToSpawn;i++){

                    double d0 = (randomsource.nextBoolean() ? raiderDistanceFromPlayer : -raiderDistanceFromPlayer) + (double)player.getX() + (randomsource.nextDouble() - randomsource.nextDouble()) * (double)raiderSpawnRange + 0.5D;
                    double d1 = (double)(player.getY() + randomsource.nextInt(3) - 1);
                    double d2 = (randomsource.nextBoolean() ? raiderDistanceFromPlayer : -raiderDistanceFromPlayer) + (double)player.getZ() + (randomsource.nextDouble() - randomsource.nextDouble()) * (double)raiderSpawnRange + 0.5D;

                    if (event.level.noCollision(raiderEntity.get().getAABB(d0, d1, d2))) {
                        for(int i1=0;i1<randomsource.nextInt(raidersInSpot);i1++){

                            BlockPos blockpos = BlockPos.containing(d0, d1, d2);

                            raiderEntity.get().spawn((ServerLevel) event.level, new BlockPos((int)d0, (int)d1, (int)d2), MobSpawnType.NATURAL).setTarget(player);
                        }
                    }
                }

            }
        }

        private static void doContainerDrops(TickEvent.LevelTickEvent event){
            if(!(System.currentTimeMillis() > lastTickContainer))
                return;

            lastTickContainer = System.currentTimeMillis() + tickTimeContainer;

            event.level.players().forEach(player->{
                if(player == null)
                    return;

                ChunkPos pos = player.chunkPosition();
                int minX = pos.x - chunkLookSize;
                int maxX = pos.x + chunkLookSize;
                int minZ = pos.z - chunkLookSize;
                int maxZ = pos.z + chunkLookSize;

                for(int x=minX;x<maxX;x++){
                    for(int z=minZ;z<maxZ;z++){
                        LevelChunk chunk = event.level.getChunk(x, z);
                        chunk.getBlockEntities().forEach((p,b)->{
                            if(b instanceof Container){
                                Container container = (Container)b;

                                for(int i=0;i<container.getContainerSize();i++){
                                    container.setItem(i, new ItemStack(Blocks.AIR, 1));
                                }

                                List<ItemStack> dropList = null;
                                try {
                                    dropList = DropTables.getItemStacksForContainer("minecraft.chest", container.getContainerSize());
                                } catch (UnkownTableException e) {
                                    throw new RuntimeException(e);
                                }
                                for(int i=0;i<container.getContainerSize();i++){
                                    container.setItem(i, dropList.get(i));
                                }
                            }
                        });
                    };
                }
            });
        }
    }