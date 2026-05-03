package com.ssblur.creeper_crush.mixin;

import org.spongepowered.asm.mixin.gen.Accessor;

import java.nio.file.Path;

@org.spongepowered.asm.mixin.Mixin(net.minecraft.world.level.storage.SavedDataStorage.class)
public interface SavedDataStorageAccessor {
    @Accessor
    Path getDataFolder();
}
