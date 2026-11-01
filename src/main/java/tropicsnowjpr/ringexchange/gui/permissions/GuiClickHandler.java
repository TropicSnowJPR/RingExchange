package tropicsnowjpr.ringexchange.gui.permissions;

import net.minecraft.server.level.ServerPlayer;

@FunctionalInterface
public interface GuiClickHandler {
    void onClick(ServerPlayer player, int slot);
}