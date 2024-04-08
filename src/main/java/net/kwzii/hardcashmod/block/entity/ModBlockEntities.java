package net.kwzii.hardcashmod.block.entity;

import net.kwzii.hardcashmod.HardCashMod;
import net.kwzii.hardcashmod.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, HardCashMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<BasicMoneyPrinterBlockEntity>> BASIC_MONEY_PRINTER_BE =
            BLOCK_ENTITIES.register("basic_money_printer_be", () ->
                    BlockEntityType.Builder.of(BasicMoneyPrinterBlockEntity::new, ModBlocks.BASIC_MONEY_PRINTER.get()).build(null));

    public static final RegistryObject<BlockEntityType<InkJuicerBlockEntity>> INK_JUICE_BE =
            BLOCK_ENTITIES.register("ink_juicer_be", () ->
                    BlockEntityType.Builder.of(InkJuicerBlockEntity::new, ModBlocks.INK_JUICER.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }

}
