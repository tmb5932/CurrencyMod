package net.kwzii.hardcashmod.datagen;

import net.kwzii.hardcashmod.HardCashMod;
import net.kwzii.hardcashmod.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, HardCashMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(ModItems.DICE);
        simpleItem(ModItems.DOUBLE_DICE);

        simpleItem(ModItems.MAGNETIC_INK);

        simpleItem(ModItems.MAXWELL);

        simpleItem(ModItems.RAW_SAPPHIRE);
        simpleItem(ModItems.SAPPHIRE);

        simpleItem(ModItems.METAL_DETECTOR);

        simpleItem(ModItems.DOLLAR_BILL);

    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(HardCashMod.MOD_ID, "item/" + item.getId().getPath()));
    }
}
