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


/**
 * Screen rendering class for the Stamper Menu
 */
public class RecipePaperScreen extends AbstractContainerScreen<RecipePaperMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(CurrencyMod.MOD_ID, "textures/gui/recipe_paper_gui.png"); // todo put a GUI here
    private AbstractSliderButton heatSlider;
    private AbstractSliderButton aeratSlider;
    private AbstractSliderButton purifSlider;

    private int heatVal = 0;    // Current heat slider value
    private int aeratVal = 0;   // Current aeration slider value
    private int purifVal = 0;   // Current purification slider value

    /**
     * Constructor for the stamper screen
     * @param pMenu the menu to be shown
     * @param pPlayerInventory the player inventory
     * @param pTitle the title of the screen
     */
    public RecipePaperScreen(RecipePaperMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    /**
     * Method to initialize the screen
     */
    @Override
    protected void init() {
        int sliderX = 44; // Sliders X value's (all 3)
        int heatSliderY = 48;     // Slider Y location

        int aeratSliderY = 60;    // Slider Y location

        int purifSliderY = 72;    // Slider Y location

        int sliderWidth = 46; // Slider Width
        int sliderHeight = 7; // Slider Height

        int handleWidth = 4;      // Handle Width
        int handleHeight = 11;    // Handle Height

        int minValue = 0;     // Minimum value of the sliders
        int maxValue = 50;    // Maximum value of the sliders

        super.init();
//        this.inventoryLabelY = 10000;
//        this.titleLabelY = 10000;
        this.heatSlider = new AbstractSliderButton(sliderX, heatSliderY, sliderWidth, sliderHeight, Component.literal("Heat"), 0) {
            @Override
            protected void updateMessage() {

            }

            @Override
            protected void applyValue() {
//                menu.setData(,);
            }
        };
        this.aeratSlider = new AbstractSliderButton(sliderX, aeratSliderY, sliderWidth, sliderHeight, Component.literal("Aeration"), 0) {
            @Override
            protected void updateMessage() {

            }

            @Override
            protected void applyValue() {
//                menu.setData(,);
            }
        };
        this.purifSlider = new AbstractSliderButton(sliderX, purifSliderY, sliderWidth, sliderHeight, Component.literal("Purification"), 0) {
            @Override
            protected void updateMessage() {

            }

            @Override
            protected void applyValue() {
//                menu.setData(,);
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
    }

//    @Override
//    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
//        if (isMouseOverHeatSlider(pMouseX, pMouseY)) {
//            heatVal = calcValue(pMouseX);
//        } else if (isMouseOverAeratSlider(pMouseX, pMouseY)) {
//            aeratVal = calcValue(pMouseX);
//        } else if (isMouseOverPurifSlider(pMouseX, pMouseY)) {
//            purifVal = calcValue(pMouseX);
//        }
//        return super.mouseClicked(pMouseX, pMouseY, pButton);
//    }
//
//    private int calcValue(double x) {
//        return (int) ((x - sliderX) / sliderWidth) * 100;
//    }
//
//    private boolean isMouseOverAeratSlider(double x, double y) {
//        return (sliderX <= x && sliderX + width >= x) && (aeratSliderY <= y && aeratSliderY + height >= y);
//    }
//
//    private boolean isMouseOverHeatSlider(double x, double y) {
//        return (sliderX <= x && sliderX + width >= x) && (heatSliderY <= y && heatSliderY + height >= y);
//    }
//
//    private boolean isMouseOverPurifSlider(double x, double y) {
//        return (sliderX <= x && sliderX + width >= x) && (purifSliderY <= y && purifSliderY + height >= y);
//    }

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

//        int heatSliderX = heatSliderX + (int) ((float) (currentValue - minValue) / (maxValue - minValue) * (sliderWidth - handleWidth));
//        int heatHandleY = sliderY - handleHeight / 2;
//
//        int handleX = sliderX + (int) ((float) (currentValue - minValue) / (maxValue - minValue) * (sliderWidth - handleWidth));
//        int handleY = sliderY - handleHeight / 2;
//
//        int handleX = sliderX + (int) ((float) (currentValue - minValue) / (maxValue - minValue) * (sliderWidth - handleWidth));
//        int handleY = sliderY - handleHeight / 2;

        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
