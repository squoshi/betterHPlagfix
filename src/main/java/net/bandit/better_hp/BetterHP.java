package net.bandit.better_hp;

import net.bandit.better_hp.config.BetterHPConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;

@Mod(BetterHP.MOD_ID)
public class BetterHP {
    public static final String MOD_ID = "better_hp";

    public BetterHP() {
        // Register the configuration
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, BetterHPConfig.CLIENT_SPEC);

        // Add listeners for setup events
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        // Client-specific setup
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
        });
    }

    private void setup(final FMLCommonSetupEvent event) {
        // Common setup tasks can be done here
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        // Client-specific setup tasks, like registering renderers, can be done here
    }
}
