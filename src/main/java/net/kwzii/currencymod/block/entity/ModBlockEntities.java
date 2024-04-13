package net.kwzii.currencymod.block.entity;

import net.kwzii.currencymod.CurrencyMod;
import net.kwzii.currencymod.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Class for all the mod block entities to be registered
 * @author Travis Brown
 */
public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, CurrencyMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<BasicMoneyPrinterBlockEntity>> BASIC_MONEY_PRINTER_BE =
            BLOCK_ENTITIES.register("basic_money_printer_be", () ->
                    BlockEntityType.Builder.of(BasicMoneyPrinterBlockEntity::new, ModBlocks.BASIC_MONEY_PRINTER.get()).build(null));

    public static final RegistryObject<BlockEntityType<InkJuicerBlockEntity>> INK_JUICE_BE =
            BLOCK_ENTITIES.register("ink_juicer_be", () ->
                    BlockEntityType.Builder.of(InkJuicerBlockEntity::new, ModBlocks.INK_JUICER.get()).build(null));

    /**
     * Method to register an IEventBus with the block entities
     * @param eventBus IEventBus to be registered with the block entity deferred register
     */
    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }

}
