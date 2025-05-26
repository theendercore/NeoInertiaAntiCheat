package me.diffusehyperion.inertiaanticheat.networking.method.data.handlers;

import me.diffusehyperion.inertiaanticheat.networking.method.ReceiverHandler;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import java.security.KeyPair;

public abstract class DataReceiverHandler extends ReceiverHandler {
    protected final DataValidationHandler validator;

    public DataReceiverHandler(KeyPair keyPair, ResourceLocation modTransferID, ServerLoginPacketListenerImpl handler, DataValidationHandler validator) {
        super(keyPair, modTransferID, handler);
        this.validator = validator;
    }
}
