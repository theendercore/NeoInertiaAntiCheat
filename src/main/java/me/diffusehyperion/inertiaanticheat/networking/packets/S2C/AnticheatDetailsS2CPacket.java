package me.diffusehyperion.inertiaanticheat.networking.packets.S2C;

import me.diffusehyperion.inertiaanticheat.networking.packets.AnticheatPackets;
import me.diffusehyperion.inertiaanticheat.networking.packets.UpgradedClientQueryPacketListener;
import me.diffusehyperion.inertiaanticheat.util.AnticheatDetails;
import me.diffusehyperion.inertiaanticheat.util.GroupAnticheatDetails;
import me.diffusehyperion.inertiaanticheat.util.IndividualAnticheatDetails;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketType;
import java.util.ArrayList;
import java.util.Arrays;

public record AnticheatDetailsS2CPacket(AnticheatDetails details) implements Packet<UpgradedClientQueryPacketListener> {
    public static final StreamCodec<FriendlyByteBuf, AnticheatDetailsS2CPacket> CODEC = Packet.codec(AnticheatDetailsS2CPacket::write, AnticheatDetailsS2CPacket::new);

    private AnticheatDetailsS2CPacket(FriendlyByteBuf packetByteBuf) {
        this(bufToDetails(packetByteBuf));
    }

    private static AnticheatDetails bufToDetails(FriendlyByteBuf buf) {
        int ordinal = buf.readInt();
        if (ordinal == 0) {
            return new IndividualAnticheatDetails(
                    buf.readBoolean(),
                    new ArrayList<>(Arrays.asList(buf.readUtf().split(","))),
                    new ArrayList<>(Arrays.asList(buf.readUtf().split(","))));
        } else if (ordinal == 1) {
            return new GroupAnticheatDetails(
                    buf.readBoolean(),
                    new ArrayList<>(Arrays.asList(buf.readUtf().split(",")))
            );
        } else {
            throw new RuntimeException("Unknown ordinal given");
        }
    }

    public void write(FriendlyByteBuf buf) {
        buf.writeInt(this.details.getCheckMethod().ordinal());
        if (this.details instanceof IndividualAnticheatDetails individualDetails) {
            buf.writeBoolean(individualDetails.showInstalled());
            buf.writeUtf(String.join(",", individualDetails.getBlacklistedMods()));
            buf.writeUtf(String.join(",", individualDetails.getWhitelistedMods()));
        } else if (this.details instanceof GroupAnticheatDetails groupDetails) {
            buf.writeBoolean(groupDetails.showInstalled());
            buf.writeUtf(String.join(",", groupDetails.getModpackDetails()));
        }
    }

    @Override
    public PacketType<? extends Packet<UpgradedClientQueryPacketListener>> type() {
        return AnticheatPackets.DETAILS_RESPONSE;
    }


    public void handle(UpgradedClientQueryPacketListener listener) {
        listener.onReceiveAnticheatDetails(this);
    }
}
