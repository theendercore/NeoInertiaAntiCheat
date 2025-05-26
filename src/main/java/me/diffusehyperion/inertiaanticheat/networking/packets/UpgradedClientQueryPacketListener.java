package me.diffusehyperion.inertiaanticheat.networking.packets;

import me.diffusehyperion.inertiaanticheat.networking.packets.S2C.*;
import net.minecraft.network.protocol.status.ClientStatusPacketListener;

public interface UpgradedClientQueryPacketListener extends ClientStatusPacketListener {
    void onReceiveAnticheatDetails(AnticheatDetailsS2CPacket var1);
}
