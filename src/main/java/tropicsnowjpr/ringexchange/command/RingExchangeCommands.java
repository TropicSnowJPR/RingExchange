package tropicsnowjpr.ringexchange.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import tropicsnowjpr.ringexchange.gui.AuctionHouse;
import tropicsnowjpr.ringexchange.text.Lang;

public class RingExchangeCommands {
    private RingExchangeCommands() {}

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("shop")
                        .then(Commands.literal("create")).executes(ctx -> createShop(ctx.getSource()))
                        .then(Commands.literal("remove")).executes(ctx -> removeShop(ctx.getSource()))
                        .then(Commands.literal("list")).executes(ctx -> listShops(ctx.getSource()))
                        .then(Commands.literal("rename")
                                .then(Commands.argument("name", StringArgumentType.greedyString())
                                        .executes(ctx -> renameShop(ctx.getSource(), StringArgumentType.getString(ctx, "name")))))
                        .then(Commands.literal("admin")
                                .requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS))
                                .then(Commands.literal("list").executes(ctx -> adminListShops(ctx.getSource())))
                                .then(Commands.literal("remove").executes(ctx -> adminRemoveShop(ctx.getSource()))))
        );
        dispatcher.register(
                Commands.literal("test")
                        .executes(ctx -> test(ctx.getSource()))
        );
    }

    private static int createShop(CommandSourceStack source) {
        return 1;
    }

    private static int removeShop(CommandSourceStack source) {
        return 1;
    }

    private static int listShops(CommandSourceStack source) {
        return 1;
    }

    private static int renameShop(CommandSourceStack source, String name) {
        return 1;
    }

    private static int adminListShops(CommandSourceStack source) {
        return 1;
    }

    private static int adminRemoveShop(CommandSourceStack source) {
        return 1;
    }

    private static ServerPlayer requirePlayer(CommandSourceStack source) {
        return source.getEntity() instanceof ServerPlayer sp ? sp : null;
    }

    private static int info(CommandSourceStack source) {
        ServerPlayer sp = requirePlayer(source);
        if (sp == null) {
            source.sendFailure(Component.literal(Lang.tr(sp, "ringexchange.cmd.player_only", "This command must be run by a player.")));
            return 0;
        }
        source.sendSuccess(() -> Component.literal(Lang.tr(sp, "ringexchange.cmd.info", "Mod Version 1.0."))
                .withStyle(ChatFormatting.GREEN), false);
        return 1;
    }

    private static int test(CommandSourceStack source) {
        ServerPlayer player = requirePlayer(source);
        if (player == null) {
            source.sendFailure(Component.literal(Lang.tr(player, "ringexchange.cmd.player_only", "This command must be run by a player.")));
            return 0;
        }
        try {
            AuctionHouse.open(player);
        } catch (Exception e) {
            source.sendFailure(Component.literal(Lang.tr(player, "ringexchange.cmd.error", e.toString())));
        }
        return 0;
    }
}
