package net.kwzii.currencymod.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.kwzii.currencymod.CurrencyMod;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.Arrays;


/**
 * Screen rendering class for the Stamper Menu
 */
public class RecipePaperScreen extends AbstractContainerScreen<RecipePaperMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(CurrencyMod.MOD_ID, "textures/gui/recipe_paper_gui.png"); // todo put a GUI here
    private AbstractSliderButton heatSlider;
    private AbstractSliderButton aeratSlider;
    private AbstractSliderButton purifSlider;

    private double heatVal = 0;    // Current heat slider value
    private double aeratVal = 0;   // Current aeration slider value
    private double purifVal = 0;   // Current purification slider value
    private final int sliderX = 44; // Sliders X value's (all 3)
    private final int heatSliderY = 48;     // Slider Y location

    private final int aeratSliderY = 90;    // Slider Y location

    private final int purifSliderY = 140;    // Slider Y location

    private final int sliderWidth = 100; // Slider Width
    private final int sliderHeight = 15; // Slider Height

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
        super.init();
        this.inventoryLabelY = 10000;

        heatVal = menu.getSliderData(0);
        aeratVal = menu.getSliderData(1);
        purifVal = menu.getSliderData(2);

        this.heatSlider = new AbstractSliderButton(sliderX, heatSliderY, sliderWidth, sliderHeight, Component.literal("Heat"), heatVal) {
            @Override
            protected void updateMessage() {}

            @Override
            protected void applyValue() {}

            @Override
            public void onClick(double pMouseX, double pMouseY) {
                super.onClick(pMouseX, pMouseY);
                heatVal = (pMouseX - sliderX) / sliderWidth;
                System.out.println("HEATCLICK:" + pMouseX + "; =>" + heatVal);
                menu.setSliderData(0, heatVal);
            }

            @Override
            public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
                boolean result = super.keyPressed(pKeyCode, pScanCode, pModifiers);
                if (result) {
                    if (pKeyCode == 262) {
                        if (heatVal != 1) {
                            heatVal += 1.0 / (float) (this.width - 8);
                        }
                    } else if (pKeyCode == 263) {
                        if (heatVal != 0) {
                            heatVal -= 1.0 / (float) (this.width - 8);
                        }
                    }
                    if (heatVal < 0) {
                        heatVal = 0;
                    } else if (heatVal > 1) {
                        heatVal = 1;
                    }
                }
                return result;
            }
        };
        this.aeratSlider = new AbstractSliderButton(sliderX, aeratSliderY, sliderWidth, sliderHeight, Component.literal("Aeration"), aeratVal) {
            @Override
            protected void updateMessage() {}

            @Override
            protected void applyValue() {}

            @Override
            public void onClick(double pMouseX, double pMouseY) {
                super.onClick(pMouseX, pMouseY);
                aeratVal = (pMouseX - sliderX) / sliderWidth;
                System.out.println("AERATVAL:" + pMouseX + "; =>" + aeratVal);
                menu.setSliderData(1, aeratVal);
            }

            @Override
            public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
                boolean result = super.keyPressed(pKeyCode, pScanCode, pModifiers);
                if (result) {
                    if (pKeyCode == 262) {
                        if (aeratVal != 1) {
                            aeratVal += 1.0 / (float) (this.width - 8);
                        }
                    } else if (pKeyCode == 263) {
                        if (aeratVal != 0) {
                            aeratVal -= 1.0 / (float) (this.width - 8);
                        }
                    }
                    if (aeratVal < 0) {
                        aeratVal = 0;
                    } else if (aeratVal > 1) {
                        aeratVal = 1;
                    }
                }
                return result;
            }
        };
        this.purifSlider = new AbstractSliderButton(sliderX, purifSliderY, sliderWidth, sliderHeight, Component.literal("Purification"), purifVal) {
            @Override
            protected void updateMessage() {}

            @Override
            protected void applyValue() {}

            @Override
            public void onClick(double pMouseX, double pMouseY) {
                super.onClick(pMouseX, pMouseY);
                purifVal = (pMouseX - sliderX) / sliderWidth;
                System.out.println("PERSIVAL :" + pMouseX + "; =>" + purifVal);
                menu.setSliderData(2, purifVal);
            }

            @Override
            public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
                boolean result = super.keyPressed(pKeyCode, pScanCode, pModifiers);
                if (result) {
                    if (pKeyCode == 262) {
                        if (purifVal != 1) {
                            purifVal += 1.0 / (float) (this.width - 8);
                        }
                    } else if (pKeyCode == 263) {
                        if (purifVal != 0) {
                            purifVal -= 1.0 / (float) (this.width - 8);
                        }
                    }
                    if (purifVal < 0) {
                        purifVal = 0;
                    } else if (purifVal > 1) {
                        purifVal = 1;
                    }
                }
                return result;
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
