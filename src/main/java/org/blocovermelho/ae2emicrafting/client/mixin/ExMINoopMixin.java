package org.blocovermelho.ae2emicrafting.client.mixin;

import com.kneelawk.extramodintegrations.AbstractAE2Integration;
import com.kneelawk.extramodintegrations.ExMIMod;
import com.kneelawk.extramodintegrations.ExMIPlugin;
import dev.emi.emi.api.EmiRegistry;
import org.blocovermelho.ae2emicrafting.client.Ae2EmiMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Pseudo
@Mixin(ExMIPlugin.class)
public class ExMINoopMixin {
    @Redirect(method = "register", at = @At(value = "INVOKE", target = "Lcom/kneelawk/extramodintegrations/AbstractAE2Integration;register(Ldev/emi/emi/api/EmiRegistry;)V"), remap = false)
    public void ae2emicrafting$ExMI_unregisterAe2Plugin(EmiRegistry registry){
        if (Ae2EmiMod.cfg.exmi) {
            ExMIMod.logSkipping("Ae2 Plugin. Reason: ae2-emi-crafting was installed.");
            return;
        }

        AbstractAE2Integration.register(registry);
    }
}
