package com.walkercase.efm.systems.data;

import com.mrcrayfish.guns.entity.ProjectileEntity;
import net.minecraft.world.entity.LivingEntity;

public class FractureHitInfo{
        public long duration;
        public long nextHit;
        public ProjectileEntity projectile;
        public LivingEntity shooter;

        public FractureHitInfo(long duration, ProjectileEntity projectile, LivingEntity shooter) {
            this.duration = duration;
            this.projectile = projectile;
            this.shooter = shooter;
        }
    }