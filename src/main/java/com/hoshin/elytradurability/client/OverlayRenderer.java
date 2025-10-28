package com.hoshin.elytradurability.client;


import com.hoshin.elytradurability.core.EDIConfig;
import com.hoshin.elytradurability.core.ElytraDurabilityIndicator;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ElytraDurabilityIndicator.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class OverlayRenderer {
    private static final ResourceLocation ELYTRA_ICON = new ResourceLocation(ElytraDurabilityIndicator.MOD_ID, "textures/gui/elytra_icon.png");

    private static final Minecraft minecraft = Minecraft.getInstance();
    private static Window window = minecraft.getWindow();

    public static int x_center() {
        return window.getGuiScaledWidth() / 2;
    }
    public static int y_center() {
        return window.getGuiScaledHeight() / 2;
    }

    public static int bar_vertical_offset() {
        return EDIConfig.CLIENT.BAR_VERTICAL_OFFSET.get();
    }
    public static int bar_height() {
        return EDIConfig.CLIENT.BAR_HEIGHT.get();
    }
    public static int bar_width() {
        return EDIConfig.CLIENT.BAR_WIDTH.get();
    }
    public static int icon_offset() {
        return EDIConfig.CLIENT.ICON_OFFSET.get();
    }
    public static int full_bar_color() {
        return EDIConfig.CLIENT.FULL_BAR_COLOR.get();
    }

    public static int damaged_bar_color() {
        return EDIConfig.CLIENT.DAMAGED_BAR_COLOR.get();
    }

    public static boolean disappears_while_full() {
        return EDIConfig.CLIENT.DISAPPEARS_WHILE_FULL.get();
    }

    public static boolean only_while_flying() {
        return EDIConfig.CLIENT.ONLY_WHILE_FLYING.get();
    }

    public static boolean always_render_when_broken() {
        return EDIConfig.CLIENT.ALWAYS_RENDER_WHEN_BROKEN.get();
    }

    @SubscribeEvent
    public static void onRenderCrosshair(RenderGuiEvent event) {
        GuiGraphics guiGraphics = event.getGuiGraphics();
        PoseStack matrix = guiGraphics.pose();
        matrix.pushPose();
        try {
            Player player = Minecraft.getInstance().player;
            if (player == null) return;

            ItemStack chestItem = player.getItemBySlot(EquipmentSlot.CHEST);

            if (player.isSpectator()) return;
            if (chestItem.getItem() != Items.ELYTRA) return;
            if ((disappears_while_full() && !chestItem.isDamaged()) && !player.isFallFlying()) return;
            if ((only_while_flying() && !player.isFallFlying()) && !(always_render_when_broken() && computeBarMultiplier(chestItem) == 0)) return;

            int barW1 = bar_width();
            int barW2 = computeRenderedBarWidth(chestItem);
            int barWHalf = (barW2 / 2);

            int barH = bar_height();
            int iconSize = 16;

            //Don't ask why there are is +1 then -1... The minecraft crosshair is uncentered
            int centerX = (window.getGuiScaledWidth() / 2) - 1;
            int centerY = (window.getGuiScaledHeight() / 2);

            int barXFull = (centerX - (barW1 / 2));
            int barXDamagedLeft = (centerX - barWHalf);
            int barXDamagedRight = barWHalf != 0 ? (centerX + barWHalf + 1) : centerX;
            int barY1 = (centerY + bar_vertical_offset());
            int barY2 = barY1 + barH;

            //Semi transparent gray background
            guiGraphics.fill(barXFull, barY1, barXFull + barW1, barY2, full_bar_color());
            // White overlay
            guiGraphics.fill(barXDamagedLeft, barY1, barXDamagedRight, barY2, damaged_bar_color());

            int iconX = (centerX - (iconSize / 2) + 1);
            int iconY = centerY + icon_offset();

            guiGraphics.blit(
                    ELYTRA_ICON,
                    iconX, iconY,
                    0, 0,
                    16, 16,
                    16, 16
            );

        } finally {
            matrix.popPose();
        }
    }

    public static int computeRenderedBarWidth(ItemStack itemStack) {
        return Math.round(bar_width() * computeBarMultiplier(itemStack));
    }

    public static float computeBarMultiplier(ItemStack itemStack) {
        int damage = itemStack.getDamageValue();
        int max = itemStack.getMaxDamage();
        float remaining = max - damage; // durability left
        if (remaining == 1) {return 0;}
        return clamp(remaining / (float) max, 0.0F, 1.0F);
    }

    public static float clamp(float value, float min, float max) {
        return Math.min(Math.max(value, min), max);
    }

}
