package org.blocovermelho.ae2emicrafting.client.mixin;

import appeng.util.ExternalSearch;
import dev.emi.emi.api.EmiApi;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ExternalSearch.class)
public class AE2SearchMixin {
    // Thanks a lot vriska minecraft for the mixins they were tastey
    // Source: https://gitlab.com/vriska/emplied/-/blob/main/src/main/java/dev/vriska/emplied/mixin/ExternalSearchMixin.java

    @Inject(method = "isExternalSearchAvailable", at= @At("HEAD"), cancellable = true, remap = false)
    private static void ae2emicrafting$fix_external_search(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }

    @Inject(method = "getExternalSearchText",at= @At("HEAD"), cancellable = true, remap = false)
    private static void ae2emicrafting$fix_text_sync_from(CallbackInfoReturnable<String> cir){
        cir.setReturnValue(EmiApi.getSearchText());
    }

    @Inject(method = "setExternalSearchText",at= @At("HEAD"), cancellable = true, remap = false)
    private static void ae2emicrafting$fix_text_sync_to(String text, CallbackInfo ci){
        EmiApi.setSearchText(text);
        ci.cancel();
    }

    @Inject(method = "isExternalSearchFocused",at= @At("HEAD"), cancellable = true, remap = false)
    private static void ae2emicrafting$fix_text_sync_to(CallbackInfoReturnable<Boolean> cir){
        cir.setReturnValue(EmiApi.isSearchFocused());
    }
}
