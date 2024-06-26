package net.kwzii.currencymod.screen;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.MenuProvider;
import org.jetbrains.annotations.Nullable;

/**
 * Menu Provider Class for the Wallet GUI
 * @author Travis Brown
 */
public class WalletMenuProvider implements MenuProvider {

    /**
     * Empty constructor for the Wallet Menu Provider
     */
    public WalletMenuProvider() {}

    /**
     * Method to set the displayed name of the wallet
     * @return Component that is shown on GUI
     */
    @Override
    public Component getDisplayName() {
        return Component.literal("Wallet");
    }

    /**
     * Method to create the menu
     * @param pContainerId the container ID
     * @param inv the player inventory
     * @param pPlayer the player who opened GUI
     * @return the menu instance
     */
    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory inv, Player pPlayer) {
        return new WalletMenu(pContainerId, inv);
    }
}
