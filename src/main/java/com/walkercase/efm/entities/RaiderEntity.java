package com.walkercase.efm.entities;

import com.mrcrayfish.framework.api.network.LevelLocation;
import com.mrcrayfish.guns.Config;
import com.mrcrayfish.guns.common.ProjectileManager;
import com.mrcrayfish.guns.entity.ProjectileEntity;
import com.mrcrayfish.guns.interfaces.IProjectileFactory;
import com.mrcrayfish.guns.item.GunItem;
import com.mrcrayfish.guns.network.PacketHandler;
import com.mrcrayfish.guns.network.message.S2CMessageGunSound;
import com.mrcrayfish.guns.util.GunModifierHelper;
import com.walkercase.efm.EFMMain;
import com.walkercase.efm.droptable.DropTables;
import com.walkercase.efm.entities.ai.goal.RangedGunGoal;
import com.walkercase.efm.exception.UnkownTableException;
import com.walkercase.efm.framework.SyncedKeys;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import com.mrcrayfish.guns.common.Gun;
import net.minecraft.world.entity.monster.Skeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

import javax.annotation.Nullable;

public class RaiderEntity extends PathfinderMob implements RangedAttackMob {
    public RaiderEntity(EntityType<? extends PathfinderMob> p_21368_, Level p_21369_) {
        super(p_21368_, p_21369_);
    }

    public static AttributeSupplier.Builder getRaiderAttributes(){
        return Mob.createMobAttributes().add(Attributes.ARMOR, 10).add(Attributes.ARMOR_TOUGHNESS, 3)
                .add(Attributes.MOVEMENT_SPEED, 0.15f).add(Attributes.MAX_HEALTH, 40).add(Attributes.FOLLOW_RANGE, 200);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 300.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.addBehaviourGoals();
    }

    protected void addBehaviourGoals() {
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers(RaiderEntity.class));
        this.goalSelector.addGoal(2, new RangedGunGoal(this, 1.0D));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 0.7D));
    }

    @Override
    public void tick(){
        super.tick();
    }

    @Override
    public void performRangedAttack(LivingEntity targetEntity, float p_33318_) {
        ItemStack heldItem = this.getMainHandItem();
        if(heldItem != null && this.getMainHandItem().getItem() instanceof GunItem gunItem){

            Gun modifiedGun = gunItem.getModifiedGun(heldItem);

            if(modifiedGun != null){
                int count = modifiedGun.getGeneral().getProjectileAmount();
                Gun.Projectile projectileProps = modifiedGun.getProjectile();
                ProjectileEntity[] spawnedProjectiles = new ProjectileEntity[count];
                for(int i = 0; i < count; i++)
                {

                    IProjectileFactory factory = ProjectileManager.getInstance().getFactory(projectileProps.getItem());
                    ProjectileEntity projectileEntity = factory.create(this.level, this, heldItem, gunItem, modifiedGun);
                    projectileEntity.setWeapon(heldItem);
                    projectileEntity.setAdditionalDamage(Gun.getAdditionalDamage(heldItem));
                    this.level.addFreshEntity(projectileEntity);
                    spawnedProjectiles[i] = projectileEntity;
                    projectileEntity.tick();
                }

                ResourceLocation fireSound = getFireSound(heldItem, modifiedGun);
                if(fireSound != null)
                {
                    double posX = this.getX();
                    double posY = this.getY() + this.getEyeHeight();
                    double posZ = this.getZ();
                    float volume = GunModifierHelper.getFireSoundVolume(heldItem);
                    float pitch = 0.9F + this.level.random.nextFloat() * 0.2F;
                    double radius = GunModifierHelper.getModifiedFireSoundRadius(heldItem, Config.SERVER.gunShotMaxDistance.get());
                    boolean muzzle = modifiedGun.getDisplay().getFlash() != null;
                    S2CMessageGunSound messageSound = new S2CMessageGunSound(fireSound, SoundSource.HOSTILE, (float) posX, (float) posY, (float) posZ, volume, pitch, this.getId(), muzzle, false);
                    PacketHandler.getPlayChannel().sendToNearbyPlayers(() -> LevelLocation.create(this.level, posX, posY, posZ, radius), messageSound);
                }
            }
        }
    }

    private static ResourceLocation getFireSound(ItemStack stack, Gun modifiedGun)
    {
        ResourceLocation fireSound = null;
        if(GunModifierHelper.isSilencedFire(stack))
        {
            fireSound = modifiedGun.getSounds().getSilencedFire();
        }
        else if(stack.isEnchanted())
        {
            fireSound = modifiedGun.getSounds().getEnchantedFire();
        }
        if(fireSound != null)
        {
            return fireSound;
        }
        return modifiedGun.getSounds().getFire();
    }


    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor p_32146_, DifficultyInstance p_32147_, MobSpawnType p_32148_, @Nullable SpawnGroupData p_32149_, @Nullable CompoundTag p_32150_) {
        p_32149_ = super.finalizeSpawn(p_32146_, p_32147_, p_32148_, p_32149_, p_32150_);
        RandomSource randomsource = p_32146_.getRandom();

        SyncedKeys.RAIDER_SKIN.setValue(this, RaiderRenderer.skinList[randomsource.nextInt(RaiderRenderer.skinList.length)]);

        //this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.DIAMOND_HELMET));

        ItemStack raiderWeapon;
        try {
            raiderWeapon = DropTables.getGuaranteedItemStack("raider-weapons");
        } catch (UnkownTableException e) {
            EFMMain.LOGGER.error("Error", e);
            throw new RuntimeException(e);
        }

        this.setItemInHand(InteractionHand.MAIN_HAND, raiderWeapon);


        return p_32149_;
    }

    public static boolean canSpawn(EntityType<RaiderEntity> RaiderEntityType, ServerLevelAccessor serverLevelAccessor, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource randomSource) {
        return (mobSpawnType == MobSpawnType.SPAWNER) || (serverLevelAccessor.getBlockState(blockPos).isValidSpawn(serverLevelAccessor, blockPos, RaiderEntityType));
    }
}
