package com.hoshin.elytradurability.client;


import com.hoshin.elytradurability.core.EDIConfig;
import com.hoshin.elytradurability.core.ElytraDurabilityIndicator;
import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ElytraDurabilityIndicator.MOD_ID, value = Dist.CLIENT)
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

    @SubscribeEvent
    public static void onRenderGui(RenderGuiOverlayEvent event) {

        GuiGraphics guiGraphics = event.getGuiGraphics();
        Player player = Minecraft.getInstance().player;
        if (player == null) return;

        ItemStack chestItem = player.getItemBySlot(EquipmentSlot.CHEST);
        if (!((chestItem.getItem() == Items.ELYTRA) && chestItem.isDamaged()) || (player.isSpectator())) return;

        int barW1 = bar_width();
        int barW2 = computeRenderedBarWidth(chestItem);
        int barH = bar_height();
        int iconSize = 16;

        //Don't ask why there are is +1 then -1... The minecraft crosshair is uncentered
        int centerX = (window.getGuiScaledWidth() / 2) - 1;
        int centerY = (window.getGuiScaledHeight() / 2);

        int iconX = (centerX - (iconSize / 2)) + 1;
        int iconY = centerY + icon_offset();

        int barXFull = centerX - (barW1 / 2);
        int barXDamaged = centerX - (barW2 / 2);
        int barY1 = centerY + bar_vertical_offset();
        int barY2 = barY1 + barH;

        //Semi transparent gray background
        guiGraphics.fill(barXFull, barY1, barXFull + barW1, barY2, full_bar_color());
        // White overlay
        guiGraphics.fill(barXDamaged, barY1, barXDamaged + barW2, barY2, damaged_bar_color());

        guiGraphics.blit(
                ELYTRA_ICON,
                iconX, iconY,     // x, y
                0, 0,       // u, v (start of texture)
                16, 16,     // width, height to draw
                16, 16      // texture sheet size
        );
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
