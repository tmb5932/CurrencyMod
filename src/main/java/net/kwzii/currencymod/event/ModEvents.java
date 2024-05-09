package net.kwzii.currencymod.event;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.kwzii.currencymod.CurrencyMod;
import net.kwzii.currencymod.item.ModItems;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = CurrencyMod.MOD_ID)
public class ModEvents {

    // IMPORTANT: Trades just have A CHANCE to appear, they aren't always going be there
    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {
        if (event.getType() == VillagerProfession.ARMORER) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

            // the level that the trade is available at (1-5)
            trades.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 2),                // Cost
                    new ItemStack(ModItems.MONEY_STAMP.get(), 1),   // Item
                    10, 8, 0.02f));                      // max trades, xp given, price multiplier

            // Level 2
            trades.get(2).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 5),                // Cost
                    new ItemStack(ModItems.MAGNETIC_INK.get(), 1),  // Item
                    6, 12, 0.035f));                     // max trades, xp given, price multiplier
        }

        if (event.getType() == VillagerProfession.TOOLSMITH) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();

            trades.get(2).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.EMERALD, 5),                // Cost
                    new ItemStack(ModItems.MAGNETIC_INK.get(), 1),  // Item
                    2, 12, 0.035f));                     // max trades, xp given, price multiplier
        }

        if (event.getType() == VillagerProfession.LIBRARIAN) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
            ItemStack fortune_5_book = EnchantedBookItem.createForEnchantment(new EnchantmentInstance(Enchantments.BLOCK_FORTUNE, 5));

            trades.get(1).add((pTrader, pRandom) -> new MerchantOffer(
                    new ItemStack(Items.NETHERITE_INGOT, 2),    // Cost 1
                    new ItemStack(Items.DIAMOND_PICKAXE, 1),    // Cost 2
                    fortune_5_book,                                    // Item
                    2, 13, 0.15f));                   // max trades, xp given, price multiplier
        }
    }

    @SubscribeEvent
    public static void addCustomWanderingTrades(WandererTradesEvent event) {
        List<VillagerTrades.ItemListing> genericTrades = event.getGenericTrades();
        List<VillagerTrades.ItemListing> rareTrades = event.getRareTrades();

        genericTrades.add((pTrader, pRandom) -> new MerchantOffer(
                new ItemStack(ModItems.ONE_DOLLAR_BILL.get(), 12), // Cost
                new ItemStack(ModItems.BLACK_PAPER.get(), 6),      // Item
                5, 8, 0.06f));                          // max trades, xp given, price multiplier

        genericTrades.add((pTrader, pRandom) -> new MerchantOffer(
                new ItemStack(ModItems.ONE_DOLLAR_BILL.get(), 12), // Cost
                new ItemStack(ModItems.RED_PAPER.get(), 6),        // Item
                5, 8, 0.06f));                          // max trades, xp given, price multiplier

        genericTrades.add((pTrader, pRandom) -> new MerchantOffer(
                new ItemStack(ModItems.ONE_DOLLAR_BILL.get(), 12), // Cost
                new ItemStack(ModItems.BLUE_PAPER.get(), 6),       // Item
                5, 8, 0.06f));                          // max trades, xp given, price multiplier

        genericTrades.add((pTrader, pRandom) -> new MerchantOffer(
                new ItemStack(ModItems.ONE_DOLLAR_BILL.get(), 12), // Cost
                new ItemStack(ModItems.GREEN_PAPER.get(), 6),      // Item
                5, 8, 0.06f));                          // max trades, xp given, price multiplier

        genericTrades.add((pTrader, pRandom) -> new MerchantOffer(
                new ItemStack(ModItems.ONE_DOLLAR_BILL.get(), 12), // Cost
                new ItemStack(ModItems.PINK_PAPER.get(), 6),       // Item
                5, 8, 0.06f));                          // max trades, xp given, price multiplier

        genericTrades.add((pTrader, pRandom) -> new MerchantOffer(
                new ItemStack(ModItems.ONE_DOLLAR_BILL.get(), 12), // Cost
                new ItemStack(ModItems.DARK_RED_PAPER.get(), 6),   // Item
                5, 8, 0.06f));                          // max trades, xp given, price multiplier

        rareTrades.add((pTrader, pRandom) -> new MerchantOffer(
                new ItemStack(ModItems.TEN_DOLLAR_BILL.get(), 20), // Cost
                new ItemStack(ModItems.MAXWELL.get(), 1),          // Item
                1, 15, 0.06f));                         // max trades, xp given, price multiplier

    }
}
