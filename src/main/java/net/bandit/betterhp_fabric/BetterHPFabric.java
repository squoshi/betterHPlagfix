package net.bandit.betterhp_fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.bandit.betterhp_fabric.client.HealthDisplayHandler;
import net.bandit.betterhp_fabric.config.ConfigManager;

public class BetterHPFabric implements ModInitializer {
    public static final String MOD_ID = "better_hp";

    @Override
    public void onInitialize() {
        // Load the configuration when the mod initializes
        ConfigManager.loadConfig();

        // Register client-specific event listeners
        HudRenderCallback.EVENT.register(new HealthDisplayHandler());

        // Ensure the config is saved when the player disconnects
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> ConfigManager.saveConfig());

        // Avoid excessive saves, and ensure the game updates correctly
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (!client.isPaused() && client.player != null) {
                // Implement additional logic if needed to trigger saves or updates
                // Example: Save configuration periodically or based on certain events
            }
        });

        System.out.println("BetterHPFabric Mod Initialized");
    }
}
