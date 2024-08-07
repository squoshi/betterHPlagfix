package net.bandit.betterhp_fabric.client;

import net.bandit.betterhp_fabric.config.ConfigManager;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.world.GameMode;

public class HealthDisplayHandler implements HudRenderCallback {

    private static final Identifier HEALTH_ICON = new Identifier("betterhp_fabric", "textures/gui/health_icon.png");
    private static final Identifier HUNGER_ICON = new Identifier("betterhp_fabric", "textures/gui/hunger_icon.png");
    private static final Identifier ARMOR_ICON = new Identifier("betterhp_fabric", "textures/gui/armor_icon.png");
    private static final Identifier BREATHE_ICON = new Identifier("betterhp_fabric", "textures/gui/breathe_icon.png");

    @Override
    public void onHudRender(DrawContext context, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        PlayerEntity player = client.player;

        // Exit if player is null or in creative mode
        if (player == null || client.interactionManager.getCurrentGameMode() == GameMode.CREATIVE) {
            return;
        }

        int health = MathHelper.ceil(player.getHealth());
        int maxHealth = MathHelper.ceil(player.getMaxHealth());
        int absorption = MathHelper.ceil(player.getAbsorptionAmount());
        int armorValue = player.getArmor();
        int hunger = player.getHungerManager().getFoodLevel();
        int maxHunger = 20;
        int air = player.getAir();
        int maxAir = player.getMaxAir();

        // Define colors
        int healthColor = determineHealthColor(player);
        int hungerColor = determineHungerColor(hunger, maxHunger);
        int breatheColor = 0x00BFFF; // DeepSkyBlue color for oxygen

        // Get screen dimensions
        int screenWidth = client.getWindow().getScaledWidth();
        int screenHeight = client.getWindow().getScaledHeight();

        // Calculate positions based on screen dimensions and configuration offsets
        int healthPosX = screenWidth / 2 + ConfigManager.healthDisplayX();
        int healthPosY = screenHeight - ConfigManager.healthDisplayY();

        int hungerPosX = screenWidth / 2 + ConfigManager.hungerDisplayX();
        int hungerPosY = screenHeight - ConfigManager.hungerDisplayY();

        int armorPosX = screenWidth / 2 + ConfigManager.armorDisplayX();
        int armorPosY = screenHeight - ConfigManager.armorDisplayY();

        int breathePosX = screenWidth / 2 + ConfigManager.breatheDisplayX();
        int breathePosY = screenHeight - ConfigManager.breatheDisplayY();

        // Render health icon and value
        if (ConfigManager.showHealthIcon()) {
            renderIcon(context, HEALTH_ICON, healthPosX, healthPosY);
            context.drawText(client.textRenderer, health + "/" + maxHealth, healthPosX + 18, healthPosY + 4, healthColor, false);
        }

        // Render hunger icon and value
        if (ConfigManager.showHungerIcon()) {
            renderIcon(context, HUNGER_ICON, hungerPosX, hungerPosY);
            context.drawText(client.textRenderer, hunger + "/" + maxHunger, hungerPosX + 18, hungerPosY + 4, hungerColor, false);
        }

        // Render armor icon and value
        if (ConfigManager.showArmorIcon()) {
            renderIcon(context, ARMOR_ICON, armorPosX, armorPosY);
            context.drawText(client.textRenderer, String.valueOf(armorValue), armorPosX + 18, armorPosY + 4, 0xFFFFFF, false);
        }

        // Render oxygen icon and value
        if (ConfigManager.showBreatheIcon() && player.isSubmergedInWater()) {
            renderIcon(context, BREATHE_ICON, breathePosX, breathePosY);
            context.drawText(client.textRenderer, (air / 20) + "/" + (maxAir / 20), breathePosX + 18, breathePosY + 4, breatheColor, false);
        }
    }

    private void renderIcon(DrawContext context, Identifier icon, int x, int y) {
        RenderSystem.setShaderTexture(0, icon);
        context.drawTexture(icon, x, y, 0, 0, 16, 16, 16, 16);
    }

    private int determineHealthColor(PlayerEntity player) {
        int health = MathHelper.ceil(player.getHealth());
        int maxHealth = MathHelper.ceil(player.getMaxHealth());

        if (player.hasStatusEffect(StatusEffects.POISON)) {
            return 0x00FF00; // Green for poison
        } else if (player.hasStatusEffect(StatusEffects.WITHER)) {
            return 0x707070; // Gray for wither
        }

        if (health > maxHealth * 0.75) {
            return 0x00FF00; // Green
        } else if (health > maxHealth * 0.25) {
            return 0xFFFF00; // Yellow
        } else {
            return 0xFF0000; // Red
        }
    }

    private int determineHungerColor(int hunger, int maxHunger) {
        if (hunger > maxHunger * 0.75) {
            return 0xFF8C00; // Dark Orange when full
        } else if (hunger > maxHunger * 0.25) {
            return 0xFFFF00; // Yellow when medium
        } else {
            return 0xFF4500; // Orange Red when low
        }
    }
}
