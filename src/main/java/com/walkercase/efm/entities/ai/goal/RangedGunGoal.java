package com.walkercase.efm.entities.ai.goal;

import java.util.EnumSet;
import javax.annotation.Nullable;

import com.mrcrayfish.guns.common.Gun;
import com.mrcrayfish.guns.item.GunItem;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.item.ItemStack;

public class RangedGunGoal extends Goal {
   private final Mob mob;
   private final RangedAttackMob rangedAttackMob;
   @Nullable
   private LivingEntity target;
   private int attackTime = -1;
   private final double speedModifier;
   private int seeTime;

   public RangedGunGoal(RangedAttackMob p_25773_, double p_25774_) {
      if (!(p_25773_ instanceof LivingEntity)) {
         throw new IllegalArgumentException("RangedGunGoal requires Mob implements RangedAttackMob");
      } else {
         this.rangedAttackMob = p_25773_;
         this.mob = (Mob)p_25773_;
         this.speedModifier = p_25774_;

         this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
      }
   }

   public boolean canUse() {
      LivingEntity livingentity = this.mob.getTarget();
      if (livingentity != null && livingentity.isAlive()) {
         this.target = livingentity;
         return true;
      } else {
         return false;
      }
   }

   public boolean canContinueToUse() {
      return this.canUse() || this.target.isAlive() && !this.mob.getNavigation().isDone();
   }

   public void stop() {
      this.target = null;
      this.seeTime = 0;
      this.attackTime = -1;
   }

   public boolean requiresUpdateEveryTick() {
      return true;
   }

   public void tick() {
      double d0 = this.mob.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
      boolean flag = this.mob.getSensing().hasLineOfSight(this.target);
      if (flag) {
         ++this.seeTime;
      } else {
         this.seeTime = 0;
      }

      ItemStack heldItem = this.mob.getMainHandItem();
      if(heldItem != null && heldItem.getItem() instanceof GunItem gunItem) {
         Gun modifiedGun = gunItem.getModifiedGun(heldItem);
         double attackRadius = (modifiedGun.getProjectile().getSpeed() * modifiedGun.getProjectile().getSpeed()) * 2.5d;

         if (!(d0 > attackRadius) && this.seeTime >= 1) {
            this.mob.getNavigation().stop();
         } else {
            this.mob.getNavigation().moveTo(this.target, this.speedModifier);
         }

         this.mob.getLookControl().setLookAt(this.target, 30.0F, 30.0F);
         if (!(d0 > attackRadius) && --this.attackTime == 0) {
            if (!flag) {
               return;
            }

            float f = (float) Math.sqrt(d0) / (float)attackRadius;
            float f1 = Mth.clamp(f, 0.1F, 1.0F);
            this.rangedAttackMob.performRangedAttack(this.target, f1);
            this.attackTime = Mth.floor(f * modifiedGun.getGeneral().getRate());
         } else if (this.attackTime < 0) {
            this.attackTime = modifiedGun.getGeneral().getRate();
         }
      }

   }
}