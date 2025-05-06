package me.diffusehyperion.inertiaanticheat.packets;

import me.diffusehyperion.inertiaanticheat.packets.S2C.AnticheatDetailsS2CPacket;
import me.diffusehyperion.inertiaanticheat.util.InertiaAntiCheatConstants;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.PacketType;

public class AnticheatPackets {
    public static final PacketType<AnticheatDetailsS2CPacket> DETAILS_RESPONSE = new PacketType<>(PacketFlow.CLIENTBOUND, InertiaAntiCheatConstants.ANTICHEAT_DETAILS_ID);
}
