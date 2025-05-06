package me.diffusehyperion.inertiaanticheat.mixins.server;

import me.diffusehyperion.inertiaanticheat.packets.S2C.AnticheatDetailsS2CPacket;
import me.diffusehyperion.inertiaanticheat.server.InertiaAntiCheatServer;
import me.diffusehyperion.inertiaanticheat.util.GroupAnticheatDetails;
import me.diffusehyperion.inertiaanticheat.util.IndividualAnticheatDetails;
import me.diffusehyperion.inertiaanticheat.util.ModlistCheckMethod;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.ping.ServerboundPingRequestPacket;
import net.minecraft.server.network.ServerStatusPacketListenerImpl;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerStatusPacketListenerImpl.class)
public abstract class ServerQueryNetworkHandlerMixin {
    @Shadow @Final
    private Connection connection;

    @Inject(method = "handlePingRequest",
    at = @At(value = "HEAD"))
    private void injectSendAnticheatDetails(ServerboundPingRequestPacket packet, CallbackInfo ci) {
        if (InertiaAntiCheatServer.modlistCheckMethod == ModlistCheckMethod.INDIVIDUAL) {
            IndividualAnticheatDetails details =
                    new IndividualAnticheatDetails(
                            InertiaAntiCheatServer.serverConfig.getBoolean("motd.showInstalled"),
                            InertiaAntiCheatServer.serverConfig.getList("motd.blacklist"),
                            InertiaAntiCheatServer.serverConfig.getList("motd.whitelist"));
            this.connection.send(new AnticheatDetailsS2CPacket(details));
        } else {
            GroupAnticheatDetails details =
                    new GroupAnticheatDetails(
                            InertiaAntiCheatServer.serverConfig.getBoolean("motd.showInstalled"),
                            InertiaAntiCheatServer.serverConfig.getList("motd.hash"));
            this.connection.send(new AnticheatDetailsS2CPacket(details));
        }
    }
}
