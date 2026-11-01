package tropicsnowjpr.ringexchange.mixin;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import tropicsnowjpr.ringexchange.gui.permissions.GuiPermissions;

@Mixin(Slot.class)
public abstract class SlotPermissionMixin {

    @Shadow @Final public Container container;
    @Shadow @Final public int slot;

    @Inject(method = "mayPlace", at = @At("HEAD"), cancellable = true)
    private void ringexchange$checkPlacement(
            ItemStack stack,
            CallbackInfoReturnable<Boolean> cir
    ) {
        if (!GuiPermissions.canPlace(container, slot)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "mayPickup", at = @At("HEAD"), cancellable = true)
    private void ringexchange$checkTaking(
            Player player,
            CallbackInfoReturnable<Boolean> cir
    ) {
        if (!GuiPermissions.canTake(container, slot)) {
            cir.setReturnValue(false);
        }
    }
}