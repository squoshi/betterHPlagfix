package net.bandit.better_hp.event;

import net.bandit.better_hp.config.BetterHPConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = "better_hp", value = Dist.CLIENT)
public class HealthDisplayHandler {

    @SubscribeEvent
    public static void onRenderGui(RenderGuiOverlayEvent.Pre event) {
        // Check the configuration to see if vanilla hearts should be shown
        boolean showVanillaHearts = BetterHPConfig.CLIENT.showVanillaHearts.get();
        boolean showVanillaArmor = BetterHPConfig.CLIENT.showVanillaArmor.get(); // Get armor config

        // Cancel the default health bar rendering if not showing vanilla hearts
        if (!showVanillaHearts && event.getOverlay() == VanillaGuiOverlay.PLAYER_HEALTH.type()) {
            event.setCanceled(true);
        }

        // Cancel the default armor bar rendering if not showing vanilla armor
        if (!showVanillaArmor && event.getOverlay() == VanillaGuiOverlay.ARMOR_LEVEL.type()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onRenderGui(RenderGuiOverlayEvent.Post event) {
        // Retrieve the configuration settings
        boolean showVanillaArmor = BetterHPConfig.CLIENT.showVanillaArmor.get(); // Ensure this is checked
        boolean showHPText = BetterHPConfig.CLIENT.showHPText.get();

        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;

        // Ensure the player is not null
        if (player == null) {
            return;
        }

        // Use GuiGraphics from the event to render text
        GuiGraphics guiGraphics = event.getGuiGraphics();

        // Calculate player's health, max health, absorption amount, and armor value
        int health = (int) player.getHealth();
        int maxHealth = (int) player.getMaxHealth();
        int absorption = (int) player.getAbsorptionAmount();
        int armorValue = player.getArmorValue();

        // Get the screen dimensions
        int screenWidth = minecraft.getWindow().getGuiScaledWidth();
        int screenHeight = minecraft.getWindow().getGuiScaledHeight();

        // Calculate the position based on configuration and screen size
        int healthPosX = BetterHPConfig.CLIENT.healthDisplayX.get();
        int healthPosY = BetterHPConfig.CLIENT.healthDisplayY.get();

        // Adjust for anchoring to the bottom-center
        int centeredX = (screenWidth / 2) - (healthPosX / 2);
        int bottomY = screenHeight - healthPosY;

        int foodBarY = bottomY - 3;
        int yArmor = bottomY - 15; // Position for armor
        int yHealth = bottomY; // Position for health

        // Prepare the health and armor text
        String healthText = health + "/" + maxHealth + (showHPText ? " HP" : ""); // Conditionally append "HP"
        String absorptionText = "+" + absorption; // Text for absorption hearts
        String armorText = armorValue + " Armor"; // Text for armor value
        Font font = minecraft.font;
        int healthTextWidth = font.width(healthText);
        int absorptionWidth = font.width(absorptionText);
        int armorTextWidth = font.width(armorText);

        // Define the colors
        int textColor = determineHealthColor(player); // Get health color based on status effects
        int absorptionColor = 0xFFFF00; // Yellow color for absorption
        int armorColor = 0xAAAAAA; // Gray color for armor text
        int outlineColor = 0x000000; // Black color for outline

        // Render the armor value text with a black outline
        if (!showVanillaArmor) {
            drawOutlinedText(guiGraphics, font, armorText, centeredX, yArmor, armorColor, outlineColor);
        }

        // Render the health text with a black outline
        drawOutlinedText(guiGraphics, font, healthText, centeredX, yHealth, textColor, outlineColor);

        // Render absorption hearts text if any, to the right of the health text
        if (absorption > 0) {
            drawOutlinedText(guiGraphics, font, absorptionText, centeredX + healthTextWidth + 5, yHealth, absorptionColor, outlineColor);
        }
    }

    // Method to determine health color based on player's active status effects
    private static int determineHealthColor(Player player) {
        // Check if the player is affected by the Wither effect
        MobEffectInstance witherEffect = player.getEffect(MobEffects.WITHER);
        if (witherEffect != null) {
            return 0x800080; // Dark Purple color for Wither effect
        }

        // Check if the player is affected by the Poison effect
        MobEffectInstance poisonEffect = player.getEffect(MobEffects.POISON);
        if (poisonEffect != null) {
            return 0x00FF00; // Bright Green color for Poison effect
        }

        // Default color (red) when no specific status effects
        return 0xFF0000;
    }

    private static void drawOutlinedText(GuiGraphics guiGraphics, Font font, String text, int x, int y, int color, int outlineColor) {
        // Draw text outline
        guiGraphics.drawString(font, text, x - 1, y, outlineColor, false);
        guiGraphics.drawString(font, text, x + 1, y, outlineColor, false);
        guiGraphics.drawString(font, text, x, y - 1, outlineColor, false);
        guiGraphics.drawString(font, text, x, y + 1, outlineColor, false);

        // Draw main text
        guiGraphics.drawString(font, text, x, y, color, false);
    }
}
