package me.diffusehyperion.inertiaanticheat.packets;

import com.mojang.authlib.GameProfile;
import me.diffusehyperion.inertiaanticheat.interfaces.ServerInfoInterface;
import me.diffusehyperion.inertiaanticheat.packets.S2C.AnticheatDetailsS2CPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerStatusPinger;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.network.Connection;
import net.minecraft.network.DisconnectionDetails;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.ping.ClientboundPongResponsePacket;
import net.minecraft.network.protocol.ping.ServerboundPingRequestPacket;
import net.minecraft.network.protocol.status.ClientboundStatusResponsePacket;
import net.minecraft.network.protocol.status.ServerStatus;
import org.apache.logging.log4j.util.TriConsumer;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiConsumer;

public class UpgradedClientQueryNetworkHandler implements UpgradedClientQueryPacketListener {
    /* ---------- vanilla fields ----------*/

    private final ServerData serverInfo;
    private final Runnable saver;
    private final Runnable pingCallback;

    private final Connection connection;

    private final InetSocketAddress inetSocketAddress;
    private final ServerAddress serverAddress;

    private final BiConsumer<Component, ServerData> showErrorMethod;
    private final TriConsumer<InetSocketAddress, ServerAddress, ServerData> pingMethod;

    private boolean sentQuery;
    private boolean received;
    private long startTime;

    public UpgradedClientQueryNetworkHandler(ServerData serverInfo, Runnable saver, Runnable pingCallback, Connection connection,
                                             InetSocketAddress inetSocketAddress, ServerAddress serverAddress,
                                             BiConsumer<Component, ServerData> showErrorMethod,
                                             TriConsumer<InetSocketAddress, ServerAddress, ServerData> pingMethod) {
        /* ---------- vanilla fields ----------*/

        this.serverInfo = serverInfo;
        this.saver = saver;
        this.pingCallback = pingCallback;

        this.connection = connection;

        this.inetSocketAddress = inetSocketAddress;
        this.serverAddress = serverAddress;

        this.showErrorMethod = showErrorMethod;
        this.pingMethod = pingMethod;
    }


    @Override
    public void onReceiveAnticheatDetails(AnticheatDetailsS2CPacket var1) {
        ((ServerInfoInterface) serverInfo).inertiaAntiCheat$setInertiaInstalled(true);
        ((ServerInfoInterface) serverInfo).inertiaAntiCheat$setAnticheatDetails(var1.details());
    }


    /* ---------- (Mostly) vanilla stuff below ----------*/

    @Override
    public void handleStatusResponse(ClientboundStatusResponsePacket packet) {
        if (this.received) {
            connection.disconnect(Component.translatable("multiplayer.status.unrequested"));
            return;
        }
        this.received = true;
        ServerStatus serverMetadata = packet.status();
        serverInfo.motd = serverMetadata.description();
        serverMetadata.version().ifPresentOrElse(version -> {
            serverInfo.version = Component.literal(version.name());
            serverInfo.protocol = version.protocol();
        }, () -> {
            serverInfo.version = Component.translatable("multiplayer.status.old");
            serverInfo.protocol = 0;
        });
        serverMetadata.players().ifPresentOrElse(players -> {
            serverInfo.status = ServerStatusPinger.formatPlayerCount(players.online(), players.max());
            serverInfo.players = players;
            if (!players.sample().isEmpty()) {
                ArrayList<Component> list = new ArrayList<>(players.sample().size());
                for (GameProfile gameProfile : players.sample()) {
                    list.add(Component.literal(gameProfile.getName()));
                }
                if (players.sample().size() < players.online()) {
                    list.add(Component.translatable("multiplayer.status.and_more", players.online() - players.sample().size()));
                }
                serverInfo.playerList = list;
            } else {
                serverInfo.playerList = List.of();
            }
        }, () -> serverInfo.status = Component.translatable("multiplayer.status.unknown").withStyle(ChatFormatting.DARK_GRAY));
        serverMetadata.favicon().ifPresent(favicon -> {
            if (!Arrays.equals(favicon.iconBytes(), serverInfo.getIconBytes())) {
                serverInfo.setIconBytes(ServerData.validateIcon(favicon.iconBytes()));
                saver.run();
            }
        });
        this.startTime = Util.getMillis();
        this.connection.send(new ServerboundPingRequestPacket(this.startTime));
        this.sentQuery = true;
    }

    @Override
    public void handlePongResponse(ClientboundPongResponsePacket packet) {
        long l = this.startTime;
        long m = Util.getMillis();
        serverInfo.ping = m - l;
        this.connection.disconnect(Component.translatable("multiplayer.status.finished"));
        this.pingCallback.run();
    }

    @Override
    public void onDisconnect(DisconnectionDetails info) {
        if (!this.sentQuery) {
            showErrorMethod.accept(info.reason(), serverInfo);
            pingMethod.accept(inetSocketAddress, serverAddress, serverInfo);
        }
    }

    @Override
    public boolean isAcceptingMessages() {
        return this.connection.isConnected();
    }
}
