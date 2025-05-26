package me.diffusehyperion.inertiaanticheat.mixins.client;

import me.diffusehyperion.inertiaanticheat.interfaces.ClientConnectionMixinInterface;
import me.diffusehyperion.inertiaanticheat.networking.packets.UpgradedClientQueryPacketListener;
import net.minecraft.network.ClientboundPacketListener;
import net.minecraft.network.Connection;
import net.minecraft.network.ProtocolInfo;
import net.minecraft.network.ServerboundPacketListener;
import net.minecraft.network.protocol.handshake.ClientIntent;
import net.minecraft.network.protocol.status.StatusProtocols;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Connection.class)
public abstract class ClientConnectionMixin implements ClientConnectionMixinInterface {
    @Unique
    @Override
    public void inertiaAntiCheat$connect(String address, int i, UpgradedClientQueryPacketListener upgradedClientQueryPacketListener) {
        this.initiateServerboundConnection(address, i, StatusProtocols.SERVERBOUND, StatusProtocols.CLIENTBOUND, upgradedClientQueryPacketListener, ClientIntent.STATUS);
    }

    @Shadow
    private <S extends ServerboundPacketListener, C extends ClientboundPacketListener> void initiateServerboundConnection(String address, int port, ProtocolInfo<S> outboundState, ProtocolInfo<C> inboundState, C prePlayStateListener, ClientIntent intent) {
    }
}
