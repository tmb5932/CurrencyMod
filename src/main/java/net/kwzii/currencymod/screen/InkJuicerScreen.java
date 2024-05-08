package net.kwzii.currencymod.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.kwzii.currencymod.CurrencyMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * Ink Juicer Screen class
 * @author Travis Brown
 */
public class InkJuicerScreen extends AbstractContainerScreen<InkJuicerMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(CurrencyMod.MOD_ID, "textures/gui/ink_juicer_gui.png");

    /**
     * Constructor for the juicer screen
     * @param pMenu the menu to be shown
     * @param pPlayerInventory the player inventory
     * @param pTitle the title of the screen
     */

    public InkJuicerScreen(InkJuicerMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    /**
     * Method to initialize the screen
     */
    @Override
    protected void init() {
        super.init();
    }

    /**
     * Method to render the graphics
     * @param guiGraphics the gui graphics
     * @param pPartialTick float value partial tick
     * @param pMouseX int the mouse x value
     * @param pMouseY int the mouse y value
     */
    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        renderProgressArrow(guiGraphics, x, y);
    }

    /**
     * Method to render progress arrow's and liquid storage
     * @param guiGraphics the gui graphics
     * @param x the starting x value
     * @param y the starting y value
     */
    private void renderProgressArrow(GuiGraphics guiGraphics, int x, int y) {
        // INPUT PROGRESS ARROW RENDER
        if(menu.isJuicing()) {
            guiGraphics.blit(TEXTURE, x + 80, y + 16, 209, 0, menu.getScaledInputProgress(), 15);
        }
        // OUTPUT PROGRESS ARROW RENDER
        if(menu.isPouring()) {
            guiGraphics.blit(TEXTURE, x + 80 + (22 - menu.getScaledOutputProgress()), y + 51, 209 + (22 - menu.getScaledOutputProgress()), 15, menu.getScaledOutputProgress(), 15);
        }
        // INK STORAGE RENDER
        int arrowColor = menu.getInkColor(); // Gets color of ink
        RenderSystem.setShaderColor((float)(arrowColor >> 16 & 255) / 255.0F, (float)(arrowColor >> 8 & 255) / 255.0F, (float)(arrowColor & 255) / 255.0F, 1.0F);   // Sets liquid to ink color
        guiGraphics.blit(TEXTURE, x + 123, y + 16 + (52 - menu.getScaledLiquid()), 176, 52 - menu.getScaledLiquid(), 33, menu.getScaledLiquid());   // places liquid on screen
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);    // resets shaders to default
    }

    /**
     * Method to render the graphics
     * @param guiGraphics the graphics to render
     * @param mouseX the mouses x value
     * @param mouseY the mouses y value
     * @param delta the delta float
     */
    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
