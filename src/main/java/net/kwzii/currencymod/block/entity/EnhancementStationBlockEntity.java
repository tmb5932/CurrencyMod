package net.kwzii.currencymod.block.entity;

import net.kwzii.currencymod.screen.EnhancementStationMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EnhancementStationBlockEntity extends BlockEntity implements MenuProvider {

    // Enum of all the types of enhancements that can be created in the enhancement station
    private enum RecipeType {
        NONE(0),
        STRENGTH(1),
        IRON_SKIN(2),
        COLD_BLOODED(3);

        private final int intVal;

        /**
         * Creator for Recipe type enum
         * @param intVal the int value associated with that Recipe type
         */
        RecipeType(int intVal) {
            this.intVal = intVal;
        }

        /**
         * Getter method for Recipe type
         * @return int intVal
         */
        public int getIntVal() {
            return intVal;
        }

        /**
         * Returns the liquidType that has the associated int value
         * @param intValue the int value of the liquid we want to get
         * @return LiquidType associated with the given number
         */
        public static EnhancementStationBlockEntity.RecipeType setByInt(int intValue) {
            for (EnhancementStationBlockEntity.RecipeType enumValue : EnhancementStationBlockEntity.RecipeType.values()) {
                if (enumValue.intVal == intValue) {
                    return enumValue;
                }
            }
            throw new IllegalArgumentException("Invalid integer value: " + intValue);
        }
    }

    private final ItemStackHandler itemHandler = new ItemStackHandler(5);

    private static final int inputSlots = 4;
    private static final int OUTPUT_SLOT = 4;

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 78;

    private int heatSlider = 0;
    private int aerateSlider = 0;
    private int purifSlider = 0;

    private RecipeType currentRecipe = RecipeType.NONE;

    /**
     * Constructor for Enhancement Station Block Entity
     * @param pPos the position is it placed in the world
     * @param pBlockState the state of the block when it is placed
     */
    public EnhancementStationBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.ENHANCEMENT_STATION_BE.get(), pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> EnhancementStationBlockEntity.this.progress;
                    case 1 -> EnhancementStationBlockEntity.this.maxProgress;
                    case 2 -> EnhancementStationBlockEntity.this.heatSlider;
                    case 3 -> EnhancementStationBlockEntity.this.aerateSlider;
                    case 4 -> EnhancementStationBlockEntity.this.purifSlider;
                    case 5 -> EnhancementStationBlockEntity.this.currentRecipe.getIntVal();
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int i1) {
                switch (i) {
                    case 0 -> EnhancementStationBlockEntity.this.progress = i1;
                    case 1 -> EnhancementStationBlockEntity.this.maxProgress = i1;
                    case 2 -> EnhancementStationBlockEntity.this.heatSlider = i1;
                    case 3 -> EnhancementStationBlockEntity.this.aerateSlider = i1;
                    case 4 -> EnhancementStationBlockEntity.this.purifSlider = i1;
                    case 5 -> EnhancementStationBlockEntity.this.currentRecipe = RecipeType.setByInt(i1);
                }
            }

            @Override
            public int getCount() {
                return 6;
            }
        };
    }

    /**
     * Adds the lazyItemHandler to the getCapability() call
     * @param cap the capability
     * @param side the direction side
     * @return the LazyItemHandler
     * @param <T> capability type
     */
    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    /**
     * Adds the lazyItemHandler to the onLoad() call
     */
    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    /**
     * Adds the lazyItemHandler to the invalidateCaps() method call
     */
    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    /**
     * Method to make the inventory contents drop when block broken
     */
    public void drops() {
        SimpleContainer inv = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inv.setItem(i, itemHandler.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inv);
    }

    /**
     * Sets the display name of the block entity
     * @return Component of what the name of the block entity is
     */
    @Override
    public Component getDisplayName() {
        return Component.translatable("block.currencymod.enhancement_station");
    }

    /**
     * Creates GUI menu for the enhancement station block
     * @param i the int container ID
     * @param inv the player inventory
     * @param player the player
     * @return new Enhancement Station Menu instance
     */
    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inv, Player player) {
        return new EnhancementStationMenu(i, inv, this, this.data);
    }

    /**
     * Method to save additional information to server files
     * @param pTag data structure used to store information
     */
    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("enhancement_station.inventory", itemHandler.serializeNBT());
        pTag.putInt("enhancement_station.progress", progress);
        pTag.putInt("enhancement_station.heat", heatSlider);
        pTag.putInt("enhancement_station.aerate", aerateSlider);
        pTag.putInt("enhancement_station.purif", purifSlider);
        pTag.putInt("enhancement_station.recipe", currentRecipe.getIntVal());
        super.saveAdditional(pTag);
    }

    /**
     * Loads saved information about block
     * @param pTag data structure used to store information
     */
    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        itemHandler.deserializeNBT(pTag.getCompound("enhancement_station.inventory"));
        progress = pTag.getInt("enhancement_station.progress");
        heatSlider = pTag.getInt("enhancement_station.heat");
        aerateSlider = pTag.getInt("enhancement_station.aerate");
        purifSlider = pTag.getInt("enhancement_station.purif");
        currentRecipe = RecipeType.setByInt(pTag.getInt("enhancement_station.recipe"));
    }

    /**
     * Method that runs every tick
     * Checks if crafting is possible, and if so, starts process of crafting
     * @param pLevel the world instance the tick is happening on
     * @param pPos the block's position
     * @param pState the state of the block
     */
    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        if (hasRecipe()) {
            progress++;
            setChanged(pLevel, pPos, pState);

            if (hasProgressFinished()) {
                if (itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty()) {
                    craftItem();
                    resetProgress();
                } else {
                    progress = maxProgress;
                }
            }
        } else {
            resetProgress();
        }
    }

    private void craftItem() {
        for (int i = 0; i < inputSlots; i++) {
            itemHandler.extractItem(i, 1, false);
        }
        switch (currentRecipe.getIntVal()) {
            case 1 -> itemHandler.setStackInSlot(OUTPUT_SLOT, new ItemStack(Items.CREEPER_SPAWN_EGG, 1)); // todo: ModItems.STRENGTH_ENHANCEMENT
            case 2 -> itemHandler.setStackInSlot(OUTPUT_SLOT, new ItemStack(Items.OBSIDIAN, 1)); // ModItems.IRON_SKIN_ENHANCEMENT
            case 3 -> itemHandler.setStackInSlot(OUTPUT_SLOT, new ItemStack(Items.CLOCK, 1)); // ModItems.COLD_BLOODED_ENHANCEMENT
            default -> throw new IllegalArgumentException("Invalid Recipe Found: " + currentRecipe);
        }
    }

    private boolean hasProgressFinished() {
        return progress >= maxProgress;
    }

    private void resetProgress() {
        progress = 0;
        currentRecipe = RecipeType.NONE;
    }

    private boolean hasRecipe() {
            // Strength Enhancement
        if (inputHas(Items.IRON_INGOT) && inputHas(Items.NETHER_WART) && inputHas(Items.BLAZE_POWDER) && inputHas(Items.AIR)) {
            currentRecipe = RecipeType.STRENGTH;
            return true;
        } else
            // Iron Skin Enhancement
        if (inputHas(Items.TURTLE_HELMET) && inputHas(Items.NETHER_WART) && inputHas(Items.QUARTZ) && inputHas(Items.AIR)) {
            currentRecipe = RecipeType.IRON_SKIN;
            return true;
        } else
            // Cold Blooded Enhancement
        if (inputHas(Items.PHANTOM_MEMBRANE) && inputHas(Items.NETHER_WART) && inputHas(Items.FERMENTED_SPIDER_EYE)) {
            currentRecipe = RecipeType.COLD_BLOODED;
            return true;
        }

        return false;
    }

    private boolean inputHas(Item item) {
        for (int i = 0; i < inputSlots; i++) {
            if (itemHandler.getStackInSlot(i).is(item)) {
                return true;
            }
        }
        return false;
    }
}
