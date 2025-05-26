package me.diffusehyperion.inertiaanticheat.networking.method;

import me.diffusehyperion.inertiaanticheat.util.InertiaAntiCheatConstants;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import java.security.KeyPair;

public abstract class ReceiverHandler {
    protected final KeyPair keyPair;
    protected final ResourceLocation modTransferID;

    public ReceiverHandler(KeyPair keyPair, ResourceLocation modTransferID, ServerLoginPacketListenerImpl handler) {
        this.keyPair = keyPair;
        this.modTransferID = modTransferID;

        ServerLoginNetworking.registerReceiver(handler, InertiaAntiCheatConstants.SEND_MOD, this::receiveMod);
    }

    protected abstract void receiveMod(MinecraftServer minecraftServer, ServerLoginPacketListenerImpl serverLoginNetworkHandler, boolean b, FriendlyByteBuf buf, ServerLoginNetworking.LoginSynchronizer synchronizer, PacketSender packetSender);
}
