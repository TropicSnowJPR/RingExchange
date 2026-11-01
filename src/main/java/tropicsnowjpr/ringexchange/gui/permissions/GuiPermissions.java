package tropicsnowjpr.ringexchange.gui.permissions;

import java.util.HashMap;
import java.util.Map;

public final class GuiPermissions {
    private GuiPermissions() {}

    private static final Map<Object, Map<Integer, Rule>> RULES =
            new java.util.WeakHashMap<>();

    private record Rule(boolean canPlace, boolean canTake) {}

    public static synchronized void set(
            Object inventory,
            int slot,
            boolean canPlace,
            boolean canTake
    ) {
        RULES.computeIfAbsent(inventory, ignored -> new HashMap<>())
                .put(slot, new Rule(canPlace, canTake));
    }

    public static synchronized boolean canPlace(Object inventory, int slot) {
        Rule rule = get(inventory, slot);
        return rule == null || rule.canPlace();
    }

    public static synchronized boolean canTake(Object inventory, int slot) {
        Rule rule = get(inventory, slot);
        return rule == null || rule.canTake();
    }

    private static Rule get(Object inventory, int slot) {
        Map<Integer, Rule> slots = RULES.get(inventory);
        return slots == null ? null : slots.get(slot);
    }

    public static synchronized void clear(Object inventory) {
        RULES.remove(inventory);
    }
}