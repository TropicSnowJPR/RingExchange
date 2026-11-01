package tropicsnowjpr.ringexchange.gui;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import tropicsnowjpr.ringexchange.gui.permissions.GuiClickActions;
import tropicsnowjpr.ringexchange.gui.permissions.GuiPermissions;

public final class AuctionHouse {

    private AuctionHouse() {}

    public static void open(ServerPlayer player) {
        GuiInventory inventory = new GuiInventory(54);

        for (int slot = 0; slot < 54; slot++) {
            inventory.setItem(slot, new ItemStack(Items.STAINED_GLASS_PANE.purple()));
            GuiPermissions.set(inventory, slot, false, false);
        }

        inventory.setItem(0, new ItemStack(Items.GOLDEN_SWORD));

        inventory.setItem(9, new ItemStack(Items.DIAMOND_CHESTPLATE));
        inventory.setItem(11, head("Pink custom head"));
        inventory.setItem(12, head("Pale blue custom head"));
        inventory.setItem(13, head("Yellow custom head"));
        inventory.setItem(14, head("Green cactus-like head"));
        inventory.setItem(15, head("White custom head"));
        inventory.setItem(16, head("Black custom head"));

        inventory.setItem(18, new ItemStack(Items.SLIME_BLOCK));
        inventory.setItem(20, head("Chicken-like head"));
        inventory.setItem(21, head("Brown-haired head"));
        inventory.setItem(22, head("Navy blue custom head"));
        inventory.setItem(23, head("Light blue checkered head"));
        inventory.setItem(24, head("Brown grid-patterned head"));
        inventory.setItem(25, head("Cow-like head"));

        inventory.setItem(27, new ItemStack(Items.APPLE));
        inventory.setItem(29, head("Lime-green custom head"));
        inventory.setItem(30, head("Red custom head"));
        inventory.setItem(31, head("Teal custom head"));
        inventory.setItem(32, head("Light-green custom head"));
        inventory.setItem(33, head("Enderman-like head"));
        inventory.setItem(34, head("Pale yellow custom head"));

        inventory.setItem(36, new ItemStack(Items.COBBLESTONE));
        inventory.setItem(38, head("Light blue visor head"));
        inventory.setItem(39, head("Mint-green custom head"));
        inventory.setItem(40, head("Pale gray skull"));
        inventory.setItem(41, head("Black-topped custom head"));
        inventory.setItem(42, head("Yellow-topped custom head"));
        inventory.setItem(43, head("Brown-haired head with red cap"));

        inventory.setItem(45, new ItemStack(Items.STICK));
        inventory.setItem(48, new ItemStack(Items.OAK_SIGN));
        inventory.setItem(49, new ItemStack(Items.ARROW));
        inventory.setItem(50, new ItemStack(Items.HOPPER));
        inventory.setItem(51, new ItemStack(Items.ENDER_EYE));
        inventory.setItem(52, new ItemStack(Items.POWERED_RAIL));
        inventory.setItem(53, new ItemStack(Items.ARROW));

        GuiClickActions.register(inventory, 0, (clicker, slot) -> {
            clicker.sendSystemMessage(
                    Component.literal("Diamond purchase requested")
            );

            // Call your server-side purchase service here.
        });

        GuiClickActions.register(inventory, 49, (clicker, slot) -> {
            clicker.sendSystemMessage(
                    Component.literal("Previous page")
            );

            // Change the current page here.
        });

        player.sendSystemMessage(Component.literal("Auction actions registered"));

        player.openMenu(new SimpleMenuProvider(
                (syncId, playerInventory, serverPlayer) ->
                        ChestMenu.sixRows(
                                syncId,
                                playerInventory,
                                inventory
                        ),
                Component.literal("Auction House")
        ));


    }

    private static ItemStack head(String name) {
        ItemStack stack = new ItemStack(Items.PLAYER_HEAD);
        stack.set(DataComponents.CUSTOM_NAME, Component.literal(name));
        return stack;
    }
}