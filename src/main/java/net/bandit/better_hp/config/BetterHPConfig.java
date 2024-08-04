package net.bandit.better_hp.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class BetterHPConfig {
    public static final ForgeConfigSpec CLIENT_SPEC;
    public static final Client CLIENT;

    static {
        Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
        CLIENT_SPEC = specPair.getRight();
        CLIENT = specPair.getLeft();
    }

    public static class Client {
        public final ForgeConfigSpec.BooleanValue showVanillaHearts;
        public final ForgeConfigSpec.BooleanValue showVanillaArmor;
        public final ForgeConfigSpec.BooleanValue showVanillaHunger; // Added this line
        public final ForgeConfigSpec.BooleanValue showNumericHunger;
        public final ForgeConfigSpec.BooleanValue showHPText;
        public final ForgeConfigSpec.BooleanValue showHungerText; // New config for "Hunger" text
        public final ForgeConfigSpec.IntValue healthDisplayX;
        public final ForgeConfigSpec.IntValue healthDisplayY;
        public final ForgeConfigSpec.IntValue hungerDisplayX; // Added this line
        public final ForgeConfigSpec.IntValue hungerDisplayY; // Added this line

        public Client(ForgeConfigSpec.Builder builder) {
            builder.push("Display Settings");

            showVanillaHearts = builder
                    .comment("Show vanilla hearts")
                    .define("showVanillaHearts", true);

            showVanillaArmor = builder
                    .comment("Show vanilla armor bar")
                    .define("showVanillaArmor", false);

            showVanillaHunger = builder
                    .comment("Show vanilla hunger bar")
                    .define("showVanillaHunger", true);

            showNumericHunger = builder
                    .comment("Show numeric hunger instead of vanilla hunger bar")
                    .define("showNumericHunger", true);

            showHPText = builder
                    .comment("Show 'HP' text with health numbers")
                    .define("showHPText", true);

            showHungerText = builder
                    .comment("Show 'Hunger' text with hunger numbers")
                    .define("showHungerText", true);

            healthDisplayX = builder
                    .comment("Horizontal position of the health display")
                    .defineInRange("healthDisplayX", -48, -1000, 1000);

            healthDisplayY = builder
                    .comment("Vertical position of the health display")
                    .defineInRange("healthDisplayY", 43, 0, 1000);

            hungerDisplayX = builder
                    .comment("Horizontal position of the hunger display")
                    .defineInRange("hungerDisplayX", 66, -1000, 1000);

            hungerDisplayY = builder
                    .comment("Vertical position of the hunger display")
                    .defineInRange("hungerDisplayY", 43, 0, 1000);

            builder.pop();
        }
    }
}
