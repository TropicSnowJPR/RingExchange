package tropicsnowjpr.ringexchange.gui.permissions;

import net.minecraft.server.level.ServerPlayer;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

public final class GuiClickActions {
    private GuiClickActions() {}

    private static final Map<Object, Map<Integer, GuiClickHandler>> ACTIONS =
            new WeakHashMap<>();

    public static synchronized void register(
            Object inventory,
            int slot,
            GuiClickHandler handler
    ) {
        ACTIONS.computeIfAbsent(inventory, ignored -> new HashMap<>())
                .put(slot, handler);
    }

    public static synchronized boolean hasAction(
            Object inventory,
            int slot
    ) {
        Map<Integer, GuiClickHandler> actions = ACTIONS.get(inventory);
        return actions != null && actions.containsKey(slot);
    }

    public static void execute(
            Object inventory,
            int slot,
            ServerPlayer player
    ) {
        GuiClickHandler handler;

        synchronized (GuiClickActions.class) {
            Map<Integer, GuiClickHandler> actions = ACTIONS.get(inventory);
            handler = actions == null ? null : actions.get(slot);
        }

        if (handler != null) {
            handler.onClick(player, slot);
        }
    }

    public static synchronized void clear(Object inventory) {
        ACTIONS.remove(inventory);
    }
}