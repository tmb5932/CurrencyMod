package net.kwzii.currencymod.block;

import net.kwzii.currencymod.CurrencyMod;
import net.kwzii.currencymod.block.custom.BasicMoneyPrinterBlock;
import net.kwzii.currencymod.block.custom.EnhancementStationBlock;
import net.kwzii.currencymod.block.custom.InkJuicerBlock;
import net.kwzii.currencymod.block.custom.StamperBlock;
import net.kwzii.currencymod.item.ModItems;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

/**
 * Class for all the mod blocks to be registered in
 * @author Travis Brown
 */
public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CurrencyMod.MOD_ID);

    public static final RegistryObject<Block> SAPPHIRE_BLOCK = registerBlock("sapphire_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.AMETHYST)));

    public static final RegistryObject<Block> RAW_SAPPHIRE_BLOCK = registerBlock("raw_sapphire_block",
            () -> new Block(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).sound(SoundType.AMETHYST)));

    public static final RegistryObject<Block> SAPPHIRE_ORE = registerBlock("sapphire_ore",
            () -> new DropExperienceBlock(BlockBehaviour.Properties.copy(Blocks.STONE).strength(3.5f)
                    .requiresCorrectToolForDrops(), UniformInt.of(3, 6)));

    public static final RegistryObject<Block> BASIC_MONEY_PRINTER = registerBlock("basic_money_printer",
            () -> new BasicMoneyPrinterBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion()));

    public static final RegistryObject<Block> INK_JUICER = registerBlock("ink_juicer",
            () -> new InkJuicerBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion()));

    public static final RegistryObject<Block> STAMPER = registerBlock("stamper",
            () -> new StamperBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK))); // todo make model and gui

    public static final RegistryObject<Block> ENHANCEMENT_STATION = registerBlock("enhancement_station",
            () -> new EnhancementStationBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BLOCK).noOcclusion()));

    private static <T extends Block>RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    /**
     * Function to register block a block
     * @param name the name of the block
     * @param block the block that is being registered
     * @return Registry object of the registered block
     * @param <T> Register object type
     */
    private static <T extends Block>RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    /**
     * Registers an IEventBus with the mod blocks
     * @param eventBus the IEventBus to be registered
     */
    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
