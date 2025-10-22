package com.hoshin.elytradurability.core;


import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

public class EDIConfig {
    public static final ForgeConfigSpec CLIENT_SPEC;
    public static final Client CLIENT;

    static {
        Pair<Client, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder().configure(Client::new);
        CLIENT = pair.getLeft();
        CLIENT_SPEC = pair.getRight();
    }

    public static class Client {
        public final ForgeConfigSpec.IntValue BAR_VERTICAL_OFFSET;
        public final ForgeConfigSpec.IntValue BAR_HEIGHT;
        public final ForgeConfigSpec.IntValue BAR_WIDTH;
        public final ForgeConfigSpec.IntValue ICON_OFFSET;
        public final ForgeConfigSpec.IntValue DAMAGED_BAR_COLOR;
        public final ForgeConfigSpec.IntValue FULL_BAR_COLOR;
        public final ForgeConfigSpec.BooleanValue DISAPPEARS_WHILE_FULL;
        public final ForgeConfigSpec.BooleanValue ONLY_WHILE_FLYING;
        public final ForgeConfigSpec.BooleanValue ALWAYS_RENDER_WHEN_BROKEN;


        public Client(ForgeConfigSpec.Builder builder) {
            builder.push("GUI");
            BAR_VERTICAL_OFFSET = builder.comment("Distance between center of the screen and the center of the bar")
                    .defineInRange("barVerticalOffset", 7, 1, 100);

            BAR_HEIGHT = builder.comment("Pixel width of the durability progress bar")
                    .defineInRange("barHeight", 2, 1, 30);

            BAR_WIDTH = builder.comment("Pixel width of the durability progress bar")
                    .defineInRange("barWidth", 25, 1, 500);

            FULL_BAR_COLOR = builder.comment("ARGB integer determining the color and opacity of the bar rendered behind the durability bar.")
                    .defineInRange("fullBarColor", 0x3808080, Integer.MIN_VALUE, Integer.MAX_VALUE);

            DAMAGED_BAR_COLOR = builder.comment("ARGB integer determining the color and opacity of the durability bar")
                    .defineInRange("damagedBarColor", 0xFFFFFFFF, Integer.MIN_VALUE, Integer.MAX_VALUE);

            ICON_OFFSET = builder.comment("Distance between the center of the screen and the top of the icon")
                    .defineInRange("iconOffset", 7, 1, 100);

            DISAPPEARS_WHILE_FULL = builder.comment("Does the GUI element disappear when your elytra is fully repaired?")
                    .define("disappearWhileFull", true);

            ONLY_WHILE_FLYING = builder.comment("Does the GUI element only appear while flying with your elytra?")
                    .define("onlyWhileFlying", true);

            ALWAYS_RENDER_WHEN_BROKEN = builder.comment("Does the GUI element always render when your elytra is broken?")
                    .define("alwaysRenderWhenBroken", true);

            builder.pop();
        }
    }
}
