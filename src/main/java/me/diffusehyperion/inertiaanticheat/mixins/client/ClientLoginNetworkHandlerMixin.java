package me.diffusehyperion.inertiaanticheat.mixins.client;

import me.diffusehyperion.inertiaanticheat.interfaces.ClientLoginNetworkHandlerInterface;
import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;
import net.minecraft.client.multiplayer.ServerData;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ClientHandshakePacketListenerImpl.class)
public abstract class ClientLoginNetworkHandlerMixin implements ClientLoginNetworkHandlerInterface {
    @Shadow @Final private @Nullable ServerData serverData;

    @Override
    public ServerData inertiaAntiCheat$getServerInfo() {
        return this.serverData;
    }
}
