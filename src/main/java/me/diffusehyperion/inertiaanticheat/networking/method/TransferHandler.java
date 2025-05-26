package me.diffusehyperion.inertiaanticheat.networking.method;

import me.diffusehyperion.inertiaanticheat.util.InertiaAntiCheatConstants;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginNetworking;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketSendListener;
import net.minecraft.resources.ResourceLocation;
import java.security.PublicKey;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public abstract class TransferHandler {
    protected final PublicKey publicKey;
    protected final ResourceLocation modTransferID;

    public TransferHandler(PublicKey publicKey, ResourceLocation modTransferID) {
        this.publicKey = publicKey;
        this.modTransferID = modTransferID;

        ClientLoginNetworking.registerReceiver(InertiaAntiCheatConstants.SEND_MOD, this::transferMod);
    }

    protected abstract CompletableFuture<FriendlyByteBuf> transferMod(Minecraft client, ClientHandshakePacketListenerImpl handler, FriendlyByteBuf buf, Consumer<PacketSendListener> callbacksConsumer);

    public void onDisconnect(ClientHandshakePacketListenerImpl ignored1, Minecraft ignored2) {
        ClientLoginNetworking.unregisterReceiver(this.modTransferID);
    }
}
