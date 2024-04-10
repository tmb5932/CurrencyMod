package net.kwzii.hardcashmod.block.entity;

import net.kwzii.hardcashmod.item.ModItems;
import net.kwzii.hardcashmod.screen.BasicMoneyPrinterMenu;
import net.kwzii.hardcashmod.util.ModTags;
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

/**
 * Basic Printer Custom Block Entity
 * @author Travis Brown
 */
public class BasicMoneyPrinterBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(3);
    private static int cookSlower = 0;

    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;
    private static final int INK_SLOT = 2;

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 78;

    /**
     * Constructor for Basic Printer Block Entity
     * @param pPos the position is it placed in the world
     * @param pBlockState the state of the block when it is placed
     */
    public BasicMoneyPrinterBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.BASIC_MONEY_PRINTER_BE.get(), pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> BasicMoneyPrinterBlockEntity.this.progress;
                    case 1 -> BasicMoneyPrinterBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int i1) {
                switch (i) {
                    case 0 -> BasicMoneyPrinterBlockEntity.this.progress = i1;
                    case 1 -> BasicMoneyPrinterBlockEntity.this.maxProgress = i1;
                }
            }

            @Override
            public int getCount() {
                return 2;
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
        return Component.translatable("block.hardcashmod.basic_money_printer");
    }

    /**
     * Creates GUI menu for the printer block
     * @param pContainerId the int container ID
     * @param inv the player inventory
     * @param player the player
     * @return new Basic Money Printer Menu instance
     */
    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory inv, Player player) {
        return new BasicMoneyPrinterMenu(pContainerId, inv, this, this.data);
    }

    /**
     * Method to save additional information to server files
     * @param pTag data structure used to store information
     */
    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", itemHandler.serializeNBT());
        pTag.putInt("basic_money_printer.progress", progress);
        super.saveAdditional(pTag);
    }

    /**
     * Loads saved information about block
     * @param pTag data structure used to store information
     */
    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        itemHandler.deserializeNBT(pTag.getCompound("inventory"));
        progress = pTag.getInt("basic_money_printer.progress");
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
            increaseCraftingProgress();
            setChanged(pLevel, pPos, pState);

            if (hasProgressFinished()) {
                craftItem();
                resetProgress();
            }
        } else {
            resetProgress();
        }
    }

    /**
     * Resets the crafting progress to 0. Function used to increase readability of tick()
     */
    private void resetProgress() {
        progress = 0;
    }

    /**
     * Crafts the item and removes one of the items that it was crafted from
     */
    private void craftItem() {
//        ItemStack result = new ItemStack(ModItems.DOLLAR_BILL.get(), 8); // todo ADD ANOTHER BLOCK ENTITY TO STAMP/CUT PAPER TO CREATE DOLLARS
        ItemStack result = getOutputItem();
        this.itemHandler.extractItem(INPUT_SLOT, 1, false);

        this.itemHandler.setStackInSlot(OUTPUT_SLOT, new ItemStack(result.getItem(),
                this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + result.getCount()));
    }

    /**
     * Checks if the current items in the slots can produce an item
     * @return boolean true if item creation is possible with the current items in slots, and false otherwise
     */
    private boolean hasRecipe() {
        boolean hasPaper = this.itemHandler.getStackInSlot(INPUT_SLOT).getItem() == Items.PAPER;
        boolean hasInk = this.itemHandler.getStackInSlot(INK_SLOT).is(ModTags.Items.JARS) && !this.itemHandler.getStackInSlot(INK_SLOT).is(ModItems.EMPTY_JAR.get());
        ItemStack result = getOutputItem();
        return hasPaper && hasInk && compatibleOutputSlot(result.getItem()) && outputStackHasSpace(result.getCount());
    }

    /**
     * Method to determine what item should be created when each ink is placed in the printer
     * @return an ItemStack of whatever color paper should be printed
     */
    private ItemStack getOutputItem() {
        Item ink = this.itemHandler.getStackInSlot(INK_SLOT).getItem();
        Item result;
        if (ink == ModItems.BLACK_INK.get()) {
            result = ModItems.BLACK_PAPER.get();
        } else if (ink == ModItems.RED_INK.get()) {
            result = ModItems.RED_PAPER.get();
        } else if (ink == ModItems.BLUE_INK.get()) {
            result = ModItems.BLUE_PAPER.get();
        } else if (ink == ModItems.GREEN_INK.get()) {
            result = ModItems.GREEN_PAPER.get();
        } else if (ink == ModItems.PINK_INK.get()) {
            result = ModItems.PINK_PAPER.get();
        } else if (ink == ModItems.MAGNETIC_INK.get()) {
            result = ModItems.DARK_RED_PAPER.get();
        } else {
            return new ItemStack(Items.PAPER, 70); // Purposefully breaks the outputStackHasSpace(result.getCount()) boolean, making hasRecipe() return false
        }
        return new ItemStack(result);
    }

    /**
     * Method to check if there is room to add the created items to the output slot
     * @param count number of items getting added to the stack
     * @return boolean true if the stack in the output slot can hold the parameters without going over the limit, false otherwise
     */
    private boolean outputStackHasSpace(int count) {
        return this.itemHandler.getStackInSlot(OUTPUT_SLOT).getCount() + count <= this.itemHandler.getStackInSlot(OUTPUT_SLOT).getMaxStackSize();
    }

    /**
     * Method to check if what is in the output slot is the same as what is trying to be crafted
     * @param item the item that is trying to be placed in the output slot
     * @return boolean true if the item can go into the output slot, and false otherwise
     */
    private boolean compatibleOutputSlot(Item item) {
        return this.itemHandler.getStackInSlot(OUTPUT_SLOT).isEmpty() || this.itemHandler.getStackInSlot(OUTPUT_SLOT).is(item);
    }

    /**
     * Method to see if progress to create an item has finished. Used for readability of tick() method
     * @return boolean true if progress is greater or equal to maxProgress, false otherwise
     */
    private boolean hasProgressFinished() {
        return progress >= maxProgress;
    }

    /**
     * Method to increase crafting progress.
     * Increases progress every other tick, but lowers the ink durability every tick
     */
    private void increaseCraftingProgress() {
        // Only progresses craft every other tick, while ink is damaged every tick (aka halves ink lifespan)
        if (cookSlower == 1) {
            progress++;
            cookSlower = 0;
        } else {
            cookSlower++;
        }
        ItemStack ink = this.itemHandler.getStackInSlot(INK_SLOT);
        ink.setDamageValue(ink.getDamageValue() + 1);

        if (ink.getDamageValue() >= ink.getMaxDamage()) {
            ink.shrink(1);
            ItemStack emptyJar = new ItemStack(ModItems.EMPTY_JAR.get(), 1);
            this.itemHandler.setStackInSlot(INK_SLOT, new ItemStack(emptyJar.getItem(), emptyJar.getCount()));
        }
    }
}
