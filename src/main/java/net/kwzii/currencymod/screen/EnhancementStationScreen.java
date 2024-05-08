package net.kwzii.currencymod.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.kwzii.currencymod.CurrencyMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class EnhancementStationScreen extends AbstractContainerScreen<EnhancementStationMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(CurrencyMod.MOD_ID, "textures/gui/enhancement_station_gui.png");
    private AbstractSliderButton heatSlider;
    private AbstractSliderButton aeratSlider;
    private AbstractSliderButton purifSlider;
    private final int sliderX = (width - imageWidth) / 2;  // 173
    private final int sliderY = ((height - imageHeight) / 2);
    private final int sliderWidth = 55;
    private final int sliderHeight = 8;


    /**
     * Constructor for the enhancement station screen
     * @param pMenu the menu to be shown
     * @param pPlayerInventory the player inventory
     * @param pTitle the title of the screen
     */
    public EnhancementStationScreen(EnhancementStationMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    /**
     * Method to initialize the screen
     */
    @Override
    protected void init() {
        super.init();
        this.inventoryLabelY = 10000;
        this.titleLabelY = 10000;
        System.out.println("ON INIT: HEAT = " + menu.getHeatValue() + "\n AERATE = " + menu.getAeratValue() + "\n PURIF = " + menu.getPurifValue());
        this.heatSlider = new AbstractSliderButton(sliderX, 15, sliderWidth, sliderHeight, Component.literal("Heat"), ((double) menu.getHeatValue()) / 100) {
            @Override
            protected void updateMessage() {
                System.out.println("Heat Changed");
            }

            @Override
            protected void applyValue() {

            }

            @Override
            public void onRelease(double pMouseX, double pMouseY) {
                super.onRelease(pMouseX, pMouseY);
                double newVal = (pMouseX-sliderX)/sliderWidth;
                menu.setData(2, (int) (newVal*100));
                System.out.println("HEAT: " + menu.getHeatValue());
            }
        };
        this.aeratSlider = new AbstractSliderButton(sliderX, sliderY - 21, sliderWidth, sliderHeight, Component.literal("Aeration"), .5) {

            @Override
            protected void updateMessage() {
                System.out.println("Aeration Changed");
            }

            @Override
            protected void applyValue() {

            }

            @Override
            public void onRelease(double pMouseX, double pMouseY) {
                super.onRelease(pMouseX, pMouseY);
                double newVal = (pMouseX-sliderX)/sliderWidth;
                menu.setData(3, (int) (newVal*100));
                System.out.println("AERAT: " + menu.getAeratValue());
            }
        };
        this.purifSlider = new AbstractSliderButton(sliderX, sliderY - 37, sliderWidth, sliderHeight, Component.literal("Purification"), (double) menu.getPurifValue() / 100) {
            @Override
            protected void updateMessage() {
                System.out.println("Purification Changed");
            }

            @Override
            protected void applyValue() {

            }

            @Override
            public void onRelease(double pMouseX, double pMouseY) {
                super.onRelease(pMouseX, pMouseY);
                double newVal = (pMouseX-sliderX)/sliderWidth;
                menu.setData(4, (int) (newVal*100));
                System.out.println("PURIF: " + menu.getPurifValue());
            }
        };
        this.addRenderableWidget(heatSlider);
        this.addRenderableWidget(aeratSlider);
        this.addRenderableWidget(purifSlider);

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
     * Method to render progress arrow
     * @param guiGraphics the gui graphics
     * @param x the starting x value
     * @param y the starting y value
     */
    private void renderProgressArrow(GuiGraphics guiGraphics, int x, int y) {
        if(menu.isCrafting()) {
            guiGraphics.blit(TEXTURE, x + 66, y + 26, 176, 0, 80, menu.getScaledProgress());
        }
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

    @Override
    public void onClose() {
        System.out.println("ON CLOSE: HEAT = " + menu.getHeatValue() + "\nAERATE = " + menu.getAeratValue() + "\nPURIF = " + menu.getPurifValue());
        super.onClose();
    }
}
