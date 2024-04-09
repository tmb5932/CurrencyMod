package net.kwzii.hardcashmod.item;

import net.kwzii.hardcashmod.HardCashMod;
import net.kwzii.hardcashmod.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * Class to create custom creative tab
 */
public class ModCreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, HardCashMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> HARDCASHMOD_TAB =
            CREATIVE_MODE_TABS.register("hardcashmod_tab", () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.MAXWELL.get()))
                    .title(Component.translatable("creativetab.hardcashmod_tab"))
            .displayItems((itemDisplayParameters, output) -> {
                // Add blocks to mod tab
                for(RegistryObject<Block> block : ModBlocks.BLOCKS.getEntries()) {
                    output.accept(block.get());
                }

                    // Add items to mod tab
                for(RegistryObject<Item> item : ModItems.ITEMS.getEntries()) {
                    output.accept(item.get());
                }
            }).build());

    /**
     * Registers an IEventBus with the mod creative tab
     * @param eventBus the IEventBus to be registered
     */
    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }

}
