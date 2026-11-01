
package tropicsnowjpr.ringexchange.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tropicsnowjpr.ringexchange.gui.GuiInventory;
import tropicsnowjpr.ringexchange.gui.permissions.GuiClickActions;
import tropicsnowjpr.ringexchange.gui.permissions.GuiPermissions;

@Mixin(AbstractContainerMenu.class)
public abstract class AbstractContainerMenuClickMixin {

    @Inject(method = "clicked", at = @At("HEAD"), cancellable = true)
    private void ringexchange$handleProtectedClicks(
            int slotIndex,
            int buttonNum,
            ContainerInput containerInput,
            Player player,
            CallbackInfo ci
    ) {
        if (!(player instanceof ServerPlayer serverPlayer)) {
            return;
        }

        AbstractContainerMenu menu = (AbstractContainerMenu) (Object) this;

        if (slotIndex < 0 || slotIndex >= menu.slots.size()) {
            return;
        }

        Slot clickedSlot = menu.getSlot(slotIndex);
        Container container = clickedSlot.container;

        if (!(container instanceof GuiInventory)) {
            return;
        }

        int inventorySlot = clickedSlot.getContainerSlot();

        // GUI actions must never perform ordinary item movement.
        if (GuiClickActions.hasAction(container, inventorySlot)) {
            if (containerInput == ContainerInput.PICKUP
                    && (buttonNum == 0 || buttonNum == 1)) {
                GuiClickActions.execute(
                        container,
                        inventorySlot,
                        serverPlayer
                );
            }

            ci.cancel();
            menu.broadcastFullState();
            return;
        }

        boolean canPlace =
                GuiPermissions.canPlace(container, inventorySlot);
        boolean canTake =
                GuiPermissions.canTake(container, inventorySlot);

        // Never allow a quick-move operation to originate from
        // a slot configured as non-removable.
        if (containerInput == ContainerInput.QUICK_MOVE && !canTake) {
            ci.cancel();
            menu.broadcastFullState();
            return;
        }

        // Fully locked display slots cannot be manipulated at all.
        if (!canPlace && !canTake) {
            ci.cancel();
            menu.broadcastFullState();
        }
    }
}
