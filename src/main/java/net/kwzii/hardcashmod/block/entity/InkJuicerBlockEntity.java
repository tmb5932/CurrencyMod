package net.kwzii.hardcashmod.block.entity;

import net.kwzii.hardcashmod.item.ModItems;
import net.kwzii.hardcashmod.screen.InkJuicerMenu;
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

public class InkJuicerBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(2);

    private static final int INPUT_SLOT = 0;
    private static final int OUTPUT_SLOT = 1;

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    protected final ContainerData data;
    private int inputProgress = 0;
    private int inputMaxProgress = 39;

    private int outputProgress = 0;
    private int outputMaxProgress = 78;

    private int amtLiquidStored = 0;
    private int liquidMaxStored = 26;
    private LiquidType liquidTypeStored = LiquidType.NONE;

    // Enum of all the types of inks that can be stored in the Juicer Liquid Storage
    private enum LiquidType {
        NONE(0),
        BLACK(1),
        RED(2),
        BLUE(3),
        GREEN(4),
        PINK(5),
        MAGNETIC(6);

        private final int intVal;

        LiquidType(int intVal) {
            this.intVal = intVal;
        }

        public int getIntVal() {
            return intVal;
        }

        public static LiquidType setByInt(int intValue) {
            for (LiquidType enumValue : LiquidType.values()) {
                if (enumValue.intVal == intValue) {
                    return enumValue;
                }
            }
            throw new IllegalArgumentException("Invalid integer value: " + intValue);
        }
    }

    public InkJuicerBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.INK_JUICE_BE.get(), pPos, pBlockState);
        this.data = new ContainerData() {
            @Override
            public int get(int i) {
                return switch (i) {
                    case 0 -> InkJuicerBlockEntity.this.inputProgress;
                    case 1 -> InkJuicerBlockEntity.this.inputMaxProgress;
                    case 2 -> InkJuicerBlockEntity.this.outputProgress;
                    case 3 -> InkJuicerBlockEntity.this.outputMaxProgress;
                    case 4 -> InkJuicerBlockEntity.this.amtLiquidStored;
                    case 5 -> InkJuicerBlockEntity.this.liquidMaxStored;
                    case 6 -> InkJuicerBlockEntity.this.liquidTypeStored.getIntVal();
                    default -> 0;
                };
            }

            @Override
            public void set(int i, int i1) {
                switch (i) {
                    case 0 -> InkJuicerBlockEntity.this.inputProgress = i1;
                    case 1 -> InkJuicerBlockEntity.this.inputMaxProgress = i1;
                    case 2 -> InkJuicerBlockEntity.this.outputProgress = i1;
                    case 3 -> InkJuicerBlockEntity.this.outputMaxProgress = i1;
                    case 4 -> InkJuicerBlockEntity.this.amtLiquidStored = i1;
                    case 5 -> InkJuicerBlockEntity.this.liquidMaxStored = i1;
                    case 6 -> InkJuicerBlockEntity.this.liquidTypeStored.setByInt(i1);
                }
            }

            @Override
            public int getCount() {
                return 7;
            }
        };
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
    }

    public void drops() {
        SimpleContainer inv = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inv.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inv);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.hardcashmod.ink_juicer");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new InkJuicerMenu(i, inventory, this, this.data);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", itemHandler.serializeNBT());
        pTag.putInt("ink_juicer.input_progress", inputProgress);
        pTag.putInt("ink_juicer.output_progress", outputProgress);
        pTag.putInt("ink_juicer.amt_liquid_stored", amtLiquidStored);
        pTag.putInt("ink_juicer.liquid_type_stored", liquidTypeStored.getIntVal());

        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        itemHandler.deserializeNBT(pTag.getCompound("inventory"));
        inputProgress = pTag.getInt("ink_juicer.input_progress");
        outputProgress = pTag.getInt("ink_juicer.output_progress");
        amtLiquidStored = pTag.getInt("ink_juicer.amt_liquid_stored");
        liquidTypeStored = LiquidType.setByInt(pTag.getInt("ink_juicer.liquid_type_stored"));
    }

    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        if (hasRecipe()) {
            inputProgress++;
            setChanged(pLevel, pPos, pState);

            if (inputProgressFinished()) {
                addLiquid();
                inputProgress = 0;
            }
        } else if (this.itemHandler.getStackInSlot(INPUT_SLOT).getItem() == Items.IRON_NUGGET && liquidTypeStored == LiquidType.RED) {
            inputProgress++;
            setChanged(pLevel, pPos, pState);

            if (inputProgressFinished()) {
                this.itemHandler.extractItem(INPUT_SLOT, 1, false);
                liquidTypeStored = LiquidType.MAGNETIC;
                inputProgress = 0;
            }
        } else {
            inputProgress = 0;
        }

        if (outputPossible()) {
            outputProgress++;
            setChanged(pLevel, pPos, pState);

            if (outputProgressFinished()) {
                fillJar();
                outputProgress = 0;
            }
        } else {
            outputProgress = 0;
        }
    }

    private void fillJar() {
        ItemStack inkJar = this.itemHandler.getStackInSlot(OUTPUT_SLOT);
        if (inkJar.is(ModItems.EMPTY_JAR.get())) {
            this.itemHandler.extractItem(OUTPUT_SLOT, 1, false);
            switch(liquidTypeStored) {
                case BLACK ->
                    this.itemHandler.insertItem(OUTPUT_SLOT, new ItemStack(ModItems.BLACK_INK.get()), false);

                case RED ->
                    this.itemHandler.insertItem(OUTPUT_SLOT, new ItemStack(ModItems.RED_INK.get()), false);

                case BLUE ->
                    this.itemHandler.insertItem(OUTPUT_SLOT, new ItemStack(ModItems.BLUE_INK.get()), false);

                case GREEN ->
                        this.itemHandler.insertItem(OUTPUT_SLOT, new ItemStack(ModItems.GREEN_INK.get()), false);

                case PINK ->
                        this.itemHandler.insertItem(OUTPUT_SLOT, new ItemStack(ModItems.PINK_INK.get()), false);

                case MAGNETIC ->
                        this.itemHandler.insertItem(OUTPUT_SLOT, new ItemStack(ModItems.MAGNETIC_INK.get()), false);
            }
            inkJar = this.itemHandler.getStackInSlot(OUTPUT_SLOT);
            inkJar.setDamageValue(inkJar.getMaxDamage()-100);
            amtLiquidStored--;
        } else {
            if (amtLiquidStored > 0 && inkJar.isDamaged()) {
                if (inkJar.getDamageValue() < 100) {
                    inkJar.setDamageValue(0);
                } else {
                    inkJar.setDamageValue(inkJar.getDamageValue() - 100);
                }
                amtLiquidStored--;
            }
        }
        if (amtLiquidStored == 0) {
            liquidTypeStored = LiquidType.NONE;
        }
    }

    private boolean outputProgressFinished() {
        return outputProgress >= outputMaxProgress;
    }

    private boolean outputPossible() {
        ItemStack inkJar = this.itemHandler.getStackInSlot(OUTPUT_SLOT);
        boolean jarNeedsInk = inkJar.isDamaged();
        return (jarNeedsInk || inkJar.is(ModItems.EMPTY_JAR.get())) && itemMatchesInk(inkJar) && amtLiquidStored != 0;
    }

    private void addLiquid() {
        ItemStack inputStack = this.itemHandler.getStackInSlot(INPUT_SLOT);
        this.itemHandler.extractItem(INPUT_SLOT, 1, false);
        if (amtLiquidStored + 2 <= liquidMaxStored) {
            amtLiquidStored += 2;
        } else {
            amtLiquidStored = liquidMaxStored;
        }
        if (liquidTypeStored == LiquidType.NONE) {
            liquidTypeStored = getLiquidType(inputStack);
        }
    }

    private boolean inputProgressFinished() {
        return inputProgress >= inputMaxProgress;
    }

    private boolean hasRecipe() {
        ItemStack inputItemStack = this.itemHandler.getStackInSlot(INPUT_SLOT);
        boolean canMakeInk = inputItemStack.is(ModTags.Items.INK_CREATING_ITEMS);

        return canMakeInk && (amtLiquidStored < liquidMaxStored) && itemMatchesInk(inputItemStack);
    }

    private boolean itemMatchesInk(ItemStack itemStack) {
        if (itemStack.is(ModItems.EMPTY_JAR.get())) {
            return true;
        }

        switch(liquidTypeStored) {
            case NONE -> { return true; }
            case BLACK -> {
                if (itemStack.is(ModTags.Items.BLACK_INK_CRAFTING) || itemStack.is(ModItems.BLACK_INK.get())) {
                    return true;
                }}
            case RED -> {
                if (itemStack.is(ModTags.Items.RED_INK_CRAFTING) || itemStack.is(ModItems.RED_INK.get())) {
                    return true;
                }}
            case BLUE -> {
                if (itemStack.is(ModTags.Items.BLUE_INK_CRAFTING) || itemStack.is(ModItems.BLUE_INK.get())) {
                    return true;
                }}
            case GREEN -> {
                if (itemStack.is(ModTags.Items.GREEN_INK_CRAFTING) || itemStack.is(ModItems.GREEN_INK.get())) {
                    return true;
                }}
            case PINK -> {
                if (itemStack.is(ModTags.Items.PINK_INK_CRAFTING) || itemStack.is(ModItems.PINK_INK.get())) {
                    return true;
                }}
            case MAGNETIC -> {
                if (itemStack.is(ModItems.MAGNETIC_INK.get())) {
                return true;
            }}
        }
        return false;
    }

    private LiquidType getLiquidType(ItemStack itemStack) {
        if (itemStack.is(ModTags.Items.BLACK_INK_CRAFTING)) {
            return LiquidType.BLACK;
        } else if (itemStack.is(ModTags.Items.RED_INK_CRAFTING)) {
            return LiquidType.RED;
        } else if (itemStack.is(ModTags.Items.BLUE_INK_CRAFTING)) {
            return LiquidType.BLUE;
        } else if (itemStack.is(ModTags.Items.GREEN_INK_CRAFTING)) {
            return LiquidType.GREEN;
        } else if (itemStack.is(ModTags.Items.PINK_INK_CRAFTING)) {
            return LiquidType.PINK;
        } else {
            return LiquidType.NONE;
        }
    }
}
