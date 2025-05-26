package me.diffusehyperion.inertiaanticheat.networking.method.name.handlers;

import me.diffusehyperion.inertiaanticheat.networking.method.ReceiverHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import java.security.KeyPair;

public abstract class NameReceiverHandler extends ReceiverHandler {
    protected final NameValidationHandler validator;

    public NameReceiverHandler(KeyPair keyPair, ResourceLocation modTransferID, ServerLoginPacketListenerImpl handler, NameValidationHandler validator) {
        super(keyPair, modTransferID, handler);
        this.validator = validator;
    }
}
