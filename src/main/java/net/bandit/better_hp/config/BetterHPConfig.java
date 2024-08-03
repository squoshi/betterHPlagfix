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
        public final ForgeConfigSpec.BooleanValue showHPText;
        public final ForgeConfigSpec.IntValue healthDisplayX;
        public final ForgeConfigSpec.IntValue healthDisplayY;

        public Client(ForgeConfigSpec.Builder builder) {
            builder.push("Display Settings");

            showVanillaHearts = builder
                    .comment("Show Vanilla Hearts")
                    .define("showVanillaHearts", false);

            showVanillaArmor = builder
                    .comment("Show Vanilla Armor")
                    .define("showVanillaArmor", false);

            showHPText = builder
                    .comment("Show HP Text")
                    .define("showHPText", true);

            healthDisplayX = builder
                    .comment("Health Display X Position")
                    .defineInRange("healthDisplayX", 140, 0, Integer.MAX_VALUE);

            healthDisplayY = builder
                    .comment("Health Display Y Position")
                    .defineInRange("healthDisplayY", 43, 0, Integer.MAX_VALUE);

            builder.pop();
        }
    }
}
