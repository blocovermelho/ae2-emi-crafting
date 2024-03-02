package org.blocovermelho.ae2emicrafting.client.mixin;

import appeng.client.gui.me.common.TerminalSettingsScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(TerminalSettingsScreen.class)
public class AE2TerminalScreenMixin {
    // Thanks a lot vriska minecraft for the mixins they were tastey
    // Source: https://gitlab.com/vriska/emplied/-/blob/main/src/main/java/dev/vriska/emplied/mixin/TerminalSettingsScreenMixin.java

    @ModifyVariable(method = "<init>(Lappeng/client/gui/me/common/MEStorageScreen;)V", at = @At(value = "STORE"), name = "hasExternalSearch", remap = false)
    private boolean ae2emicrafting$set_external_search(boolean hasExternalSearch) {
        return true;
    }

    @ModifyVariable(method = "<init>(Lappeng/client/gui/me/common/MEStorageScreen;)V", at = @At("STORE"), name = "externalSearchMod", remap = false)
    private Text ae2emicrafting$set_mod_name(Text externalSearchMod) {
        return Text.literal("EMI");
    }
}
