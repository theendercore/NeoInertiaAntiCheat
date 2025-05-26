package me.diffusehyperion.inertiaanticheat.networking.method.name;

import me.diffusehyperion.inertiaanticheat.InertiaAntiCheat;
import me.diffusehyperion.inertiaanticheat.client.InertiaAntiCheatClient;
import me.diffusehyperion.inertiaanticheat.networking.method.TransferHandler;
import me.diffusehyperion.inertiaanticheat.util.InertiaAntiCheatConstants;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketSendListener;
import net.minecraft.resources.ResourceLocation;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.PublicKey;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ClientNameTransferHandler extends TransferHandler {
    private final int maxIndex;
    private int currentIndex;

    public ClientNameTransferHandler(PublicKey publicKey, ResourceLocation modTransferID) {
        super(publicKey, modTransferID);
        this.maxIndex = InertiaAntiCheatClient.allModNames.size();
        this.currentIndex = 0;
    }

    @Override
    public CompletableFuture<FriendlyByteBuf> transferMod(Minecraft client, ClientHandshakePacketListenerImpl handler, FriendlyByteBuf buf, Consumer<PacketSendListener> callbacksConsumer) {
        InertiaAntiCheat.debugInfo("Sending mod " + this.currentIndex);

        if (this.currentIndex >= this.maxIndex) {
            // All files have been sent, returning null to signify goodbye
            InertiaAntiCheat.debugInfo("Sending final packet");
            InertiaAntiCheat.debugLine();

            ClientLoginNetworking.unregisterGlobalReceiver(InertiaAntiCheatConstants.SEND_MOD);
            return CompletableFuture.completedFuture(null);
        }
        SecretKey secretKey = InertiaAntiCheat.createAESKey();
        FriendlyByteBuf responseBuf = PacketByteBufs.create();

        byte[] encryptedAESNameData = InertiaAntiCheat.encryptAESBytes(
                InertiaAntiCheatClient.allModNames.get(currentIndex).getBytes(StandardCharsets.UTF_8), secretKey);
        byte[] encryptedRSASecretKey = InertiaAntiCheat.encryptRSABytes(secretKey.getEncoded(), this.publicKey);
        responseBuf.writeInt(encryptedRSASecretKey.length);
        responseBuf.writeBytes(encryptedRSASecretKey);
        responseBuf.writeBytes(encryptedAESNameData);

        this.currentIndex++;

        InertiaAntiCheat.debugLine();
        return CompletableFuture.completedFuture(responseBuf);
    }
}
