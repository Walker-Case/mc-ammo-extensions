package com.walkercase.efm.systems;

import com.mrcrayfish.guns.event.GunFireEvent;
import com.mrcrayfish.guns.event.GunProjectileHitEvent;
import com.mrcrayfish.guns.util.math.ExtendedEntityRayTraceResult;
import com.walkercase.efm.EFMConfig;
import com.walkercase.efm.EFMMain;
import com.walkercase.efm.enchantment.EFMDamageTypes;
import com.walkercase.efm.enchantment.EFMEnchantments;
import com.walkercase.efm.item.EFMAmmoEnchant;
import com.walkercase.efm.item.EFMAmmoWrapper;
import com.walkercase.efm.item.Magazine;
import com.walkercase.efm.systems.data.FractureHitInfo;
import com.walkercase.efm.util.EFMNBTHelper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = EFMMain.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.DEDICATED_SERVER)
public class EFMAmmoExtensionSystem {

    public static final HashMap<UUID, FractureHitInfo> playerFractureHit = new HashMap<UUID, FractureHitInfo>();

    @SubscribeEvent
    public static void preFire(GunFireEvent.Pre event) {
        Player player = event.getEntity();
        ItemStack is = event.getStack();

        ItemStack currentMagazine = Magazine.getCurrentMagazineForGun(is);
        if(currentMagazine != null){
            Item bullet = Magazine.takeTopBullet(currentMagazine);
            if(bullet instanceof EFMAmmoWrapper wrapped){
                wrapped.applyAmmoEnchants(is);
            }
        }

    }



    @SubscribeEvent
    public static void weaponHit(GunProjectileHitEvent event){
        if (event.getRayTrace() instanceof ExtendedEntityRayTraceResult) {
            ExtendedEntityRayTraceResult entityHitResult = (ExtendedEntityRayTraceResult)event.getRayTrace();
            Entity entity = entityHitResult.getEntity();

            if (entity.getId() == event.getProjectile().getShooterId()) {
                return;
            }

            LivingEntity shooter = event.getProjectile().getShooter();
            if (shooter instanceof Player) {
                Player player = (Player)shooter;
                if (entity.hasIndirectPassenger(player)) {
                    return;
                }
            }

            if(!(entity instanceof LivingEntity))
                return;


            int fractureLevel = EnchantmentHelper.getItemEnchantmentLevel(EFMEnchantments.FRACTURE.get(), event.getProjectile().getWeapon());
            if (fractureLevel > 0) {
                synchronized (playerFractureHit){
                    playerFractureHit.put(((LivingEntity)entity).getUUID(),
                            new FractureHitInfo(System.currentTimeMillis() +
                                    (EFMConfig.COMMON.ammoExtensions.fractureDuration.get() * fractureLevel), event.getProjectile(), shooter));
                }
            }
        }
    }

    @SubscribeEvent
    public static void weaponHitExtension(LivingEvent.LivingTickEvent event){
        synchronized (playerFractureHit){
            if(playerFractureHit.containsKey(event.getEntity().getUUID())){
                FractureHitInfo value = playerFractureHit.get(event.getEntity().getUUID());

                if(System.currentTimeMillis() > value.duration){
                    playerFractureHit.remove(event.getEntity().getUUID());
                    return;
                }

                if(System.currentTimeMillis() > value.nextHit){
                    event.getEntity().hurt(EFMDamageTypes.Sources.fracture(event.getEntity().level.registryAccess(), value.projectile, value.shooter),
                            EFMConfig.COMMON.ammoExtensions.fractureDamage.get());

                    value.nextHit = System.currentTimeMillis() + EFMConfig.COMMON.ammoExtensions.fractureTick.get();
                }
            }
        }
    }

    @SubscribeEvent
    public static void postFire(GunFireEvent.Post event){
        Player player = event.getEntity();
        ItemStack is = event.getStack();

        EnchantmentHelper.setEnchantments(new HashMap<Enchantment, Integer>() {}, is);
    }
}
