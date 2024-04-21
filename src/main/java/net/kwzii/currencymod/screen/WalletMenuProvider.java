package net.kwzii.currencymod.screen;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.MenuProvider;
import org.jetbrains.annotations.Nullable;

public class WalletMenuProvider implements MenuProvider {
    public WalletMenuProvider() {

    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Wallet");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory inv, Player pPlayer) {
        return new WalletMenu(pContainerId, inv);
    }
}
