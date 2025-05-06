package me.diffusehyperion.inertiaanticheat.interfaces;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.Connection;

public interface ServerLoginNetworkHandlerInterface {
    Connection inertiaAntiCheat$getConnection();
    GameProfile inertiaAntiCheat$getGameProfile();
}
