package com.walkercase.efm.framework;

import com.mrcrayfish.framework.api.FrameworkAPI;
import com.mrcrayfish.framework.api.sync.Serializers;
import com.mrcrayfish.framework.api.sync.SyncedClassKey;
import com.mrcrayfish.framework.api.sync.SyncedDataKey;
import com.walkercase.efm.EFMMain;
import com.walkercase.efm.entities.RaiderEntity;
import net.minecraft.resources.ResourceLocation;

public class SyncedKeys {

    public static final SyncedClassKey<RaiderEntity> RAIDER_ENTITY = new SyncedClassKey(RaiderEntity.class, new ResourceLocation(EFMMain.MODID, "raider_entity"));
    public static final SyncedDataKey<RaiderEntity, String> RAIDER_SKIN = SyncedDataKey.builder(RAIDER_ENTITY, Serializers.STRING)
            .id(new ResourceLocation(EFMMain.MODID, "raider_skin"))
            .defaultValueSupplier(() -> "raider_skin")
            .saveToFile()
            .syncMode(SyncedDataKey.SyncMode.TRACKING_ONLY)
            .build();

    // Register the key using the Framework register event
    public static void registerKeys()
    {
        EFMMain.LOGGER.info("Registering synced data keys!");
        FrameworkAPI.registerSyncedDataKey(RAIDER_SKIN);
    }
}
