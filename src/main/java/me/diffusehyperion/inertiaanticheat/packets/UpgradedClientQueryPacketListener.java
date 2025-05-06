package me.diffusehyperion.inertiaanticheat.packets;

import me.diffusehyperion.inertiaanticheat.packets.S2C.*;
import net.minecraft.network.protocol.status.ClientStatusPacketListener;

public interface UpgradedClientQueryPacketListener extends ClientStatusPacketListener {
    void onReceiveAnticheatDetails(AnticheatDetailsS2CPacket var1);
}
