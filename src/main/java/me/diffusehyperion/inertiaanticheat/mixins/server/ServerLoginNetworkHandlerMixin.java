package me.diffusehyperion.inertiaanticheat.mixins.server;

import com.mojang.authlib.GameProfile;
import me.diffusehyperion.inertiaanticheat.interfaces.ServerLoginNetworkHandlerInterface;
import net.minecraft.network.Connection;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ServerLoginPacketListenerImpl.class)
public abstract class ServerLoginNetworkHandlerMixin implements ServerLoginNetworkHandlerInterface {
    @Shadow @Final
    Connection connection;

    @Shadow private @Nullable GameProfile authenticatedProfile;

    @Override
    public Connection inertiaAntiCheat$getConnection() {
        return this.connection;
    }

    @Override
    public GameProfile inertiaAntiCheat$getGameProfile() {
        return this.authenticatedProfile;
    }
}
