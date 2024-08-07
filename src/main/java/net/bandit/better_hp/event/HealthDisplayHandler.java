package net.bandit.better_hp.event;

import net.bandit.better_hp.config.BetterHPConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.mojang.blaze3d.systems.RenderSystem;

@Mod.EventBusSubscriber(modid = "better_hp", value = Dist.CLIENT)
public class HealthDisplayHandler {

    private static final ResourceLocation HEALTH_ICON = new ResourceLocation("better_hp", "textures/gui/health_icon.png");
    private static final ResourceLocation HUNGER_ICON = new ResourceLocation("better_hp", "textures/gui/hunger_icon.png");
    private static final ResourceLocation ARMOR_ICON = new ResourceLocation("better_hp", "textures/gui/armor_icon.png");
    private static final ResourceLocation BREATHE_ICON = new ResourceLocation("better_hp", "textures/gui/breathe_icon.png");

    @SubscribeEvent
    public static void onRenderGui(RenderGuiOverlayEvent.Pre event) {
        boolean showVanillaHearts = BetterHPConfig.CLIENT.showVanillaHearts.get();
        boolean showVanillaArmor = BetterHPConfig.CLIENT.showVanillaArmor.get();
        boolean showVanillaHunger = BetterHPConfig.CLIENT.showVanillaHunger.get();
        boolean showVanillaOxygen = BetterHPConfig.CLIENT.showVanillaOxygen.get();

        if (!showVanillaHearts && event.getOverlay() == VanillaGuiOverlay.PLAYER_HEALTH.type()) {
            event.setCanceled(true);
        }

        if (!showVanillaArmor && event.getOverlay() == VanillaGuiOverlay.ARMOR_LEVEL.type()) {
            event.setCanceled(true);
        }

        if (!showVanillaHunger && event.getOverlay() == VanillaGuiOverlay.FOOD_LEVEL.type()) {
            event.setCanceled(true);
        }

        if (!showVanillaOxygen && event.getOverlay() == VanillaGuiOverlay.AIR_LEVEL.type()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onRenderGui(RenderGuiOverlayEvent.Post event) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;

        if (player == null || minecraft.gameMode.getPlayerMode() == GameType.CREATIVE) {
            // Skip rendering if the player is null or in Creative Mode
            return;
        }

        boolean showVanillaArmor = BetterHPConfig.CLIENT.showVanillaArmor.get();
        boolean showNumericHunger = BetterHPConfig.CLIENT.showNumericHunger.get();
        boolean showBreatheIcon = BetterHPConfig.CLIENT.showOxygenIcon.get();
        boolean showNumericOxygen = BetterHPConfig.CLIENT.showNumericOxygen.get();

        GuiGraphics guiGraphics = event.getGuiGraphics();

        int health = (int) player.getHealth();
        int maxHealth = (int) player.getMaxHealth();
        int absorption = (int) player.getAbsorptionAmount();
        int armorValue = player.getArmorValue();
        int hunger = player.getFoodData().getFoodLevel();
        int saturation = (int) player.getFoodData().getSaturationLevel(); // Get saturation level
        int air = player.getAirSupply();
        int maxAir = player.getMaxAirSupply();

        int screenWidth = minecraft.getWindow().getGuiScaledWidth();
        int screenHeight = minecraft.getWindow().getGuiScaledHeight();

        int healthDisplayX = BetterHPConfig.CLIENT.healthDisplayX.get();
        int healthDisplayY = BetterHPConfig.CLIENT.healthDisplayY.get();
        int armorDisplayX = BetterHPConfig.CLIENT.armorDisplayX.get();
        int armorDisplayY = BetterHPConfig.CLIENT.armorDisplayY.get();
        int hungerDisplayX = BetterHPConfig.CLIENT.hungerDisplayX.get();
        int hungerDisplayY = BetterHPConfig.CLIENT.hungerDisplayY.get();
        int breatheDisplayX = BetterHPConfig.CLIENT.oxygenDisplayX.get();
        int breatheDisplayY = BetterHPConfig.CLIENT.oxygenDisplayY.get();

        int centeredHealthX = (screenWidth / 2) + healthDisplayX;
        int bottomHealthY = screenHeight - healthDisplayY;

        int centeredArmorX = (screenWidth / 2) + armorDisplayX;
        int bottomArmorY = screenHeight - armorDisplayY;

        int centeredHungerX = (screenWidth / 2) + hungerDisplayX;
        int bottomHungerY = screenHeight - hungerDisplayY;

        int centeredBreatheX = (screenWidth / 2) + breatheDisplayX;
        int bottomBreatheY = screenHeight - breatheDisplayY;

        // Define numeric display texts
        String healthText = health + "/" + maxHealth;
        String absorptionText = "+" + absorption;
        String armorText = armorValue + "";
        String hungerText = hunger + "/" + 20; // Max Hunger is always 20
        String saturationText = " +" + saturation; // Only display if saturation is more than 0
        String breatheText = (air / 20) + "/" + (maxAir / 20);

        Font font = minecraft.font;
        int healthTextWidth = font.width(healthText);
        int absorptionWidth = font.width(absorptionText);
        int armorTextWidth = font.width(armorText);
        int hungerTextWidth = font.width(hungerText);
        int saturationTextWidth = font.width(saturationText);
        int breatheTextWidth = font.width(breatheText);

        int textColor = determineHealthColor(player);
        int absorptionColor = 0xFFFF00; // Yellow color for absorption
        int armorColor = 0xAAAAAA; // Gray color for armor text
        int hungerColor = 0xFF7518; // Pumpkin color
        int saturationColor = 0xFFD700; // Gold color for saturation
        int breatheColor = 0x00BFFF; // DeepSkyBlue color for oxygen
        int outlineColor = 0x000000; // Black color for outline

        // Draw the icons and numeric values
        if (!showVanillaArmor) {
            drawIcon(guiGraphics, ARMOR_ICON, centeredArmorX - armorTextWidth / 2 - 18, bottomArmorY - 4, 16, 16);
            drawOutlinedText(guiGraphics, font, armorText, centeredArmorX - armorTextWidth / 2, bottomArmorY, armorColor, outlineColor);
        }

        drawIcon(guiGraphics, HEALTH_ICON, centeredHealthX - healthTextWidth / 2 - 18, bottomHealthY - 4, 16, 16);
        drawOutlinedText(guiGraphics, font, healthText, centeredHealthX - healthTextWidth / 2, bottomHealthY, textColor, outlineColor);

        if (absorption > 0) {
            drawOutlinedText(guiGraphics, font, absorptionText, centeredHealthX + healthTextWidth / 2 + 5, bottomHealthY, absorptionColor, outlineColor);
        }

        if (showNumericHunger) {
            drawIcon(guiGraphics, HUNGER_ICON, centeredHungerX - hungerTextWidth / 2 - 18, bottomHungerY - 4, 16, 16);
            drawOutlinedText(guiGraphics, font, hungerText, centeredHungerX - hungerTextWidth / 2, bottomHungerY, hungerColor, outlineColor);

            // Display saturation only if it's greater than 0
            if (saturation > 0) {
                drawOutlinedText(guiGraphics, font, saturationText, centeredHungerX + hungerTextWidth / 2 + 5, bottomHungerY, saturationColor, outlineColor);
            }
        }

        // Draw the breathe icon and numeric oxygen value if configured
        if (showBreatheIcon && player.isUnderWater()) {
            drawIcon(guiGraphics, BREATHE_ICON, centeredBreatheX - breatheTextWidth / 2 - 18, bottomBreatheY - 4, 16, 16);
            if (showNumericOxygen) {
                drawOutlinedText(guiGraphics, font, breatheText, centeredBreatheX - breatheTextWidth / 2, bottomBreatheY, breatheColor, outlineColor);
            }
        }
    }

    private static int determineHealthColor(Player player) {
        MobEffectInstance witherEffect = player.getEffect(MobEffects.WITHER);
        if (witherEffect != null) {
            return 0x800080; // Dark Purple color for Wither effect
        }

        MobEffectInstance poisonEffect = player.getEffect(MobEffects.POISON);
        if (poisonEffect != null) {
            return 0x00FF00; // Bright Green color for Poison effect
        }

        // Default color (red) when no specific status effects
        return 0xFF0000;
    }

    private static void drawIcon(GuiGraphics guiGraphics, ResourceLocation icon, int x, int y, int width, int height) {
        RenderSystem.setShaderTexture(0, icon);
        guiGraphics.blit(icon, x, y, 0, 0, width, height, width, height);
    }

    private static void drawOutlinedText(GuiGraphics guiGraphics, Font font, String text, int x, int y, int color, int outlineColor) {
        guiGraphics.drawString(font, text, x - 1, y, outlineColor, false);
        guiGraphics.drawString(font, text, x + 1, y, outlineColor, false);
        guiGraphics.drawString(font, text, x, y - 1, outlineColor, false);
        guiGraphics.drawString(font, text, x, y + 1, outlineColor, false);

        guiGraphics.drawString(font, text, x, y, color, false);
    }
}
