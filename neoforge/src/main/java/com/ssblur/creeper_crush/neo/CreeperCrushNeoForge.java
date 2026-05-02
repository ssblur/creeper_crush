package com.ssblur.creeper_crush.neo;

import com.ssblur.creeper_crush.CreeperCrush;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLEnvironment;

@Mod(CreeperCrush.MODID)
public final class CreeperCrushNeoForge {
    public CreeperCrushNeoForge() {
        CreeperCrush.INSTANCE.init();
        if (FMLEnvironment.dist == Dist.CLIENT) {
            CreeperCrush.INSTANCE.clientInit();
        }
    }
}
