package me.diffusehyperion.inertiaanticheat.mixins.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import me.diffusehyperion.inertiaanticheat.interfaces.ClientConnectionMixinInterface;
import me.diffusehyperion.inertiaanticheat.interfaces.ServerInfoInterface;
import me.diffusehyperion.inertiaanticheat.packets.UpgradedClientQueryPacketListener;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerStatusPinger;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.status.ClientStatusPacketListener;
import me.diffusehyperion.inertiaanticheat.packets.UpgradedClientQueryNetworkHandler;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.InetSocketAddress;

@Mixin(ServerStatusPinger.class)
public abstract class MultiplayerServerListPingerMixin {
    @Shadow
    void onPingFailed(Component error, ServerData info) {}
    @Shadow
    void pingLegacyServer(InetSocketAddress socketAddress, final ServerAddress address, final ServerData serverInfo) {}

    @Inject(method = "pingServer",
            at = @At(value = "HEAD"))
    private void pingServer(ServerData entry, Runnable saver, Runnable pingCallback, CallbackInfo ci,
                            @Share("serverInfo") LocalRef<ServerData> serverDataLocalRef,
                            @Share("saver") LocalRef<Runnable> saverLocalRef,
                            @Share("pingCallback") LocalRef<Runnable> pingCallbackLocalRef) {
        serverDataLocalRef.set(entry);
        saverLocalRef.set(saver);
        pingCallbackLocalRef.set(pingCallback);
    }

    @Redirect(method = "pingServer",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/network/Connection;initiateServerboundStatusConnection(Ljava/lang/String;ILnet/minecraft/network/protocol/status/ClientStatusPacketListener;)V"))
    private void initiateServerboundUpgradedStatusConnection(
            Connection connection, String host, int port, ClientStatusPacketListener clientQueryPacketListener,
            @Share("serverInfo") LocalRef<ServerData> serverDataLocalRef,
            @Share("saver") LocalRef<Runnable> runnableLocalRef,
            @Share("pingCallback") LocalRef<Runnable> pingCallbackLocalRef,
            @Local InetSocketAddress inetSocketAddress,
            @Local ServerAddress serverAddress) {

        ServerData serverInfo = serverDataLocalRef.get();
        Runnable saver = runnableLocalRef.get();
        Runnable pingCallback = pingCallbackLocalRef.get();

        UpgradedClientQueryPacketListener listener =
                new UpgradedClientQueryNetworkHandler(serverInfo, saver, pingCallback,
                        connection, inetSocketAddress, serverAddress,
                this::onPingFailed,
                this::pingLegacyServer);

        ((ServerInfoInterface) serverInfo).inertiaAntiCheat$setInertiaInstalled(null);
        ((ServerInfoInterface) serverInfo).inertiaAntiCheat$setAnticheatDetails(null);
        ((ClientConnectionMixinInterface) connection).inertiaAntiCheat$connect(host, port, listener);
    }
}