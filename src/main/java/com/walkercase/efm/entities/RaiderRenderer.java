package com.walkercase.efm.entities;

import com.walkercase.efm.framework.SyncedKeys;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.walkercase.efm.EFMMain.MODID;

@OnlyIn(Dist.CLIENT)
public class RaiderRenderer extends HumanoidMobRenderer<RaiderEntity, HumanoidModel<RaiderEntity>> {

    public static final String[] skinList = new String[]{
            "raider_skin",
            "raider_skin_1",
            "raider_skin_3"
    };

    public RaiderRenderer(EntityRendererProvider.Context p_174380_) {
        this(p_174380_, ModelLayers.PLAYER, ModelLayers.PLAYER_INNER_ARMOR, ModelLayers.PLAYER_OUTER_ARMOR);
    }

    public RaiderRenderer(EntityRendererProvider.Context ctx, ModelLayerLocation loc1, ModelLayerLocation loc2, ModelLayerLocation loc3) {
        super(ctx, new HumanoidModel<>(ctx.bakeLayer(loc1)), 0.5F);
        this.addLayer(new HumanoidArmorLayer<>(this, new HumanoidModel(ctx.bakeLayer(loc2)), new HumanoidModel(ctx.bakeLayer(loc3)), ctx.getModelManager()));
    }

    public ResourceLocation getTextureLocation(RaiderEntity entity) {
        return new ResourceLocation(MODID, "textures/entity/" + SyncedKeys.RAIDER_SKIN.getValue(entity) + ".png");
    }


}