package com.ssblur.creeper_crush.fabric;

import com.ssblur.creeper_crush.CreeperCrush;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;

public class CreeperCrushFabric implements ModInitializer, ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CreeperCrush.INSTANCE.clientInit();
    }

    @Override
    public void onInitialize() {
        CreeperCrush.INSTANCE.init();
    }
}
