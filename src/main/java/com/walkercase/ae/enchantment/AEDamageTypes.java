package com.walkercase.ae.enchantment;

import com.mrcrayfish.guns.entity.ProjectileEntity;
import javax.annotation.Nullable;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public class AEDamageTypes {
    public static final ResourceKey<DamageType> FRACTURE_DAMAGE_SOURCE;

    public AEDamageTypes() {
    }

    static {
        FRACTURE_DAMAGE_SOURCE = ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("efm", "fracture"));
    }

    public static class Sources {
        public Sources() {
        }

        private static Holder.Reference<DamageType> getHolder(RegistryAccess access, ResourceKey<DamageType> damageTypeKey) {
            return access.registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(damageTypeKey);
        }

        private static DamageSource source(RegistryAccess access, ResourceKey<DamageType> damageTypeKey, @Nullable Entity directEntity, @Nullable Entity causingEntity) {
            return new DamageSource(getHolder(access, damageTypeKey), directEntity, causingEntity);
        }

        public static DamageSource fracture(RegistryAccess access, ProjectileEntity projectile, LivingEntity entity) {
            return source(access, FRACTURE_DAMAGE_SOURCE, projectile, entity);
        }
    }
}
