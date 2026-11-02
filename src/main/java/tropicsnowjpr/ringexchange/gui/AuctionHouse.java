package tropicsnowjpr.ringexchange.gui;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundOpenSignEditorPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import tropicsnowjpr.ringexchange.gui.permissions.GuiClickActions;
import tropicsnowjpr.ringexchange.gui.permissions.GuiPermissions;

public final class AuctionHouse {

    private AuctionHouse() {}

    private static GuiInventory inventory;

    // Menu Border Items
    private static final ItemStack WEAPONS = new ItemStack(Items.STAINED_GLASS_PANE.yellow()); // 0
    private static final ItemStack ARMOR = new ItemStack(Items.STAINED_GLASS_PANE.blue()); // 1
    private static final ItemStack ACCESSORIES = new ItemStack(Items.STAINED_GLASS_PANE.green()); // 2
    private static final ItemStack CONSUMABLES = new ItemStack(Items.STAINED_GLASS_PANE.red()); // 3
    private static final ItemStack COSMETICS = new ItemStack(Items.STAINED_GLASS_PANE.orange()); // 4
    private static final ItemStack TOOLS_AND_MISC = new ItemStack(Items.STAINED_GLASS_PANE.purple()); // 5

    private static final ItemStack[] CATEGORIES = {
            WEAPONS, // 0
            ARMOR, // 1
            ACCESSORIES, // 2
            CONSUMABLES, // 3
            COSMETICS, // 4
            TOOLS_AND_MISC // 5
    };

    private static int CurrentMenu = 0;

    public static void open(ServerPlayer player, Level level) {
        inventory = new GuiInventory(54);

        refresh();

//        for (int slot = 0; slot < 54; slot++) {
//            inventory.setItem(slot, new ItemStack(Items.STAINED_GLASS_PANE.purple()));
//            GuiPermissions.set(inventory, slot, false, false);
//        }
//
//        inventory.setItem(0, new ItemStack(Items.GOLDEN_SWORD));
//
//        inventory.setItem(9, new ItemStack(Items.DIAMOND_CHESTPLATE));
//        inventory.setItem(11, head("Pink custom head"));
//        inventory.setItem(12, head("Pale blue custom head"));
//        inventory.setItem(13, head("Yellow custom head"));
//        inventory.setItem(14, head("Green cactus-like head"));
//        inventory.setItem(15, head("White custom head"));
//        inventory.setItem(16, head("Black custom head"));
//
//        inventory.setItem(18, new ItemStack(Items.SLIME_BLOCK));
//        inventory.setItem(20, head("Chicken-like head"));
//        inventory.setItem(21, head("Brown-haired head"));
//        inventory.setItem(22, head("Navy blue custom head"));
//        inventory.setItem(23, head("Light blue checkered head"));
//        inventory.setItem(24, head("Brown grid-patterned head"));
//        inventory.setItem(25, head("Cow-like head"));
//
//        inventory.setItem(27, new ItemStack(Items.APPLE));
//        inventory.setItem(29, head("Lime-green custom head"));
//        inventory.setItem(30, head("Red custom head"));
//        inventory.setItem(31, head("Teal custom head"));
//        inventory.setItem(32, head("Light-green custom head"));
//        inventory.setItem(33, head("Enderman-like head"));
//        inventory.setItem(34, head("Pale yellow custom head"));
//
//        inventory.setItem(36, new ItemStack(Items.COBBLESTONE));
//        inventory.setItem(38, head("Light blue visor head"));
//        inventory.setItem(39, head("Mint-green custom head"));
//        inventory.setItem(40, head("Pale gray skull"));
//        inventory.setItem(41, head("Black-topped custom head"));
//        inventory.setItem(42, head("Yellow-topped custom head"));
//        inventory.setItem(43, head("Brown-haired head with red cap"));
//
//        inventory.setItem(45, new ItemStack(Items.STICK));
//        inventory.setItem(48, new ItemStack(Items.OAK_SIGN));
//        inventory.setItem(49, new ItemStack(Items.ARROW));
//        inventory.setItem(50, new ItemStack(Items.HOPPER));
//        inventory.setItem(51, new ItemStack(Items.ENDER_EYE));
//        inventory.setItem(52, new ItemStack(Items.POWERED_RAIL));
//        inventory.setItem(53, new ItemStack(Items.ARROW));

        GuiClickActions.register(inventory, 0, (clicker, slot) -> {
            CurrentMenu = 0;
            refresh();
        });

        GuiClickActions.register(inventory, 9, (clicker, slot) -> {
            CurrentMenu = 1;
            refresh();
        });

        GuiClickActions.register(inventory, 18, (clicker, slot) -> {
            CurrentMenu = 2;
            refresh();
        });

        GuiClickActions.register(inventory, 27, (clicker, slot) -> {
            CurrentMenu = 3;
            refresh();
        });

        GuiClickActions.register(inventory, 36, (clicker, slot) -> {
            CurrentMenu = 4;
            refresh();
        });

        GuiClickActions.register(inventory, 45, (clicker, slot) -> {
            CurrentMenu = 5;
            refresh();
        });

        GuiClickActions.register(inventory, 48, (clicker, slot) -> {
            clicker.sendSystemMessage(
                    Component.literal("Opening sign menu")
            );

            BlockPos signPos = player.blockPosition().above(2);
            BlockState originalState = level.getBlockState(signPos); // Later needed for putting back the block the client thought was there
            player.closeContainer(); // Needed else client or server thinks inventory is still open and tries to update slots that don't exist causing a client crash
            player.connection.send(
                    new ClientboundBlockUpdatePacket(
                            signPos,
                            Blocks.OAK_SIGN.defaultBlockState()
                    )
            );
            player.connection.send(
                    new ClientboundOpenSignEditorPacket(
                            signPos,
                            true
                    )
            );

            // Change the current page here.
        });

        player.sendSystemMessage(Component.literal("Auction actions registered"));

        player.openMenu(new SimpleMenuProvider(
                (syncId, playerInventory, serverPlayer) -> {
                    ChestMenu menu = ChestMenu.sixRows(
                            syncId,
                            playerInventory,
                            inventory
                    );

                    System.out.println("=== GUI DEBUG ===");
                    System.out.println("Container size: " + inventory.getContainerSize());
                    System.out.println("Menu slots: " + menu.slots.size());
                    System.out.println("Menu container ID: " + menu.containerId);
                    System.out.println("=================");

                    return menu;
                },
                Component.literal("Auction House")
        ));

    }

    private static ItemStack head(String name) {
        ItemStack stack = new ItemStack(Items.PLAYER_HEAD);
        stack.set(DataComponents.CUSTOM_NAME, Component.literal(name));
        return stack;
    }


    private static void refresh() {
        for (int slot = 0; slot < 54; slot++) {
            if (
                    slot <= 8 || slot == 10 || slot == 17 || slot == 19 ||
                    slot == 26 || slot == 28 || slot == 35 || slot == 37 ||
                    slot == 44 || slot == 46 || slot == 47
            ) {
                inventory.setItem(slot, CATEGORIES[CurrentMenu]);
            }

            GuiPermissions.set(inventory, slot, false, false);
        }


        inventory.setItem(0, new ItemStack(Items.GOLDEN_SWORD));
        inventory.setItem(9, new ItemStack(Items.DIAMOND_CHESTPLATE));
        inventory.setItem(18, new ItemStack(Items.SLIME_BLOCK));
        inventory.setItem(27, new ItemStack(Items.APPLE));
        inventory.setItem(36, new ItemStack(Items.COBBLESTONE));

        inventory.setItem(45, new ItemStack(Items.STICK));
        inventory.setItem(48, new ItemStack(Items.OAK_SIGN));
        inventory.setItem(49, new ItemStack(Items.ARROW));
        inventory.setItem(50, new ItemStack(Items.HOPPER));
        inventory.setItem(51, new ItemStack(Items.ENDER_EYE));
        inventory.setItem(52, new ItemStack(Items.POWERED_RAIL));
        inventory.setItem(53, new ItemStack(Items.ARROW));
    }
}