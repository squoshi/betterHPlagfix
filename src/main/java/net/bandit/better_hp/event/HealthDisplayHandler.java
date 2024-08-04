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
        boolean showVanillaHearts = BetterHPConfig.CLIENT.showVanillaHearts.get();
        boolean showVanillaArmor = BetterHPConfig.CLIENT.showVanillaArmor.get();
        boolean showVanillaHunger = BetterHPConfig.CLIENT.showVanillaHunger.get();


        if (!showVanillaHearts && event.getOverlay() == VanillaGuiOverlay.PLAYER_HEALTH.type()) {
            event.setCanceled(true);
        }

        if (!showVanillaArmor && event.getOverlay() == VanillaGuiOverlay.ARMOR_LEVEL.type()) {
            event.setCanceled(true);
        }
        if (!showVanillaHunger && event.getOverlay() == VanillaGuiOverlay.FOOD_LEVEL.type()) {
            event.setCanceled(true);
        }
    }
    @SubscribeEvent
    public static void onRenderGui(RenderGuiOverlayEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;


        if (player == null) {
            return;
        }

        boolean showVanillaArmor = BetterHPConfig.CLIENT.showVanillaArmor.get();
        boolean showNumericHunger = BetterHPConfig.CLIENT.showNumericHunger.get();
        boolean showHPText = BetterHPConfig.CLIENT.showHPText.get();
        boolean showHungerText = BetterHPConfig.CLIENT.showHungerText.get();

        GuiGraphics guiGraphics = event.getGuiGraphics();


        int health = (int) player.getHealth();
        int maxHealth = (int) player.getMaxHealth();
        int absorption = (int) player.getAbsorptionAmount();
        int armorValue = player.getArmorValue();
        int hunger = player.getFoodData().getFoodLevel();
        int maxHunger = 20;


        if (player.getFoodData().getSaturationLevel() > 20) {
            maxHunger = (int) player.getFoodData().getSaturationLevel();
        }


        int screenWidth = minecraft.getWindow().getGuiScaledWidth();
        int screenHeight = minecraft.getWindow().getGuiScaledHeight();


        int healthDisplayX = BetterHPConfig.CLIENT.healthDisplayX.get();
        int healthDisplayY = BetterHPConfig.CLIENT.healthDisplayY.get();
        int centeredHealthX = (screenWidth / 2) + healthDisplayX;
        int bottomHealthY = screenHeight - healthDisplayY;

        // Calculate the position for hunger display
        int hungerDisplayX = BetterHPConfig.CLIENT.hungerDisplayX.get();
        int hungerDisplayY = BetterHPConfig.CLIENT.hungerDisplayY.get();
        int centeredHungerX = (screenWidth / 2) + hungerDisplayX;
        int bottomHungerY = screenHeight - hungerDisplayY;

        int yArmor = bottomHealthY - 10; // Position for armor
        int yHealth = bottomHealthY; // Position for health
        int yHunger = bottomHungerY; // Position for hunger


        String healthText = health + "/" + maxHealth + (showHPText ? " HP" : ""); // Conditionally append "HP"
        String absorptionText = "+" + absorption; // Text for absorption hearts
        String armorText = armorValue + " Armor"; // Text for armor value
        String hungerText = hunger + "/" + maxHunger + (showHungerText ? " Hunger" : ""); // Conditionally append "Hunger"
        Font font = minecraft.font;
        int healthTextWidth = font.width(healthText);
        int absorptionWidth = font.width(absorptionText);
        int armorTextWidth = font.width(armorText);
        int hungerTextWidth = font.width(hungerText);


        int textColor = determineHealthColor(player);
        int absorptionColor = 0xFFFF00; // Yellow color for absorption
        int armorColor = 0xAAAAAA; // Gray color for armor text
        int hungerColor = 0xFF7518; // Pumpkin color
        int outlineColor = 0x000000; // Black color for outline


        if (!showVanillaArmor) {
            drawOutlinedText(guiGraphics, font, armorText, centeredHealthX - armorTextWidth / 2, yArmor, armorColor, outlineColor);
        }

        // Render the health text with a black outline
        drawOutlinedText(guiGraphics, font, healthText, centeredHealthX - healthTextWidth / 2, yHealth, textColor, outlineColor);

        // Render absorption hearts text if any, to the right of the health text
        if (absorption > 0) {
            drawOutlinedText(guiGraphics, font, absorptionText, centeredHealthX + healthTextWidth / 2 + 5, yHealth, absorptionColor, outlineColor);
        }

        // Render the hunger text with a black outline
        if (showNumericHunger) {
            drawOutlinedText(guiGraphics, font, hungerText, centeredHungerX - hungerTextWidth / 2, yHunger, hungerColor, outlineColor);
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
