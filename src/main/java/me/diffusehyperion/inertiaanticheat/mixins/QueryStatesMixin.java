package me.diffusehyperion.inertiaanticheat.mixins;

import me.diffusehyperion.inertiaanticheat.packets.AnticheatPackets;
import me.diffusehyperion.inertiaanticheat.packets.S2C.AnticheatDetailsS2CPacket;
import net.minecraft.network.protocol.ProtocolInfoBuilder;
import net.minecraft.network.protocol.status.StatusProtocols;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/*
 * Purpose of this is to register the custom query response packet, otherwise io.netty.handler.codec.EncoderException will be thrown
 */
@Mixin(StatusProtocols.class)
public class QueryStatesMixin {

    @Inject(method = "lambda$static$2", at = @At(value = "TAIL"))
    private static void registerClientbound(ProtocolInfoBuilder builder, CallbackInfo ci) {
        builder.addPacket(AnticheatPackets.DETAILS_RESPONSE, AnticheatDetailsS2CPacket.CODEC);
    }
}
