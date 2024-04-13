package net.kwzii.currencymod.item.custom;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import org.jetbrains.annotations.Nullable;

/**
 * Class to make a custom fuel type item
 * @author Travis Brown
 */
public class FuelItem extends Item {
    private int burnTime;

    /**
     * Constructor for fuel item
     * @param pProperties the properties of the item
     * @param burnTime the burn time of the item
     */
    public FuelItem(Properties pProperties, int burnTime) {
        super(pProperties);
        this.burnTime = burnTime;
    }

    /**
     * Method to return the burn time of the item
     * @param itemStack the item that is being checked
     * @param recipeType the recipe that is being used
     * @return the int burn time value
     */
    @Override
    public int getBurnTime(ItemStack itemStack, @Nullable RecipeType<?> recipeType) {
        return this.burnTime;
    }
}
