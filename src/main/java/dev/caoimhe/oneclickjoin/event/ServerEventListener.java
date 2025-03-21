package dev.caoimhe.oneclickjoin.event;

import dev.caoimhe.oneclickjoin.config.OneClickJoinConfig;
import dev.caoimhe.oneclickjoin.mixin.ServerListAccessor;
import dev.caoimhe.oneclickjoin.util.ServerListUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerInfo;
import org.jetbrains.annotations.Nullable;

public class ServerEventListener {
    /**
     * Fired when the client has connected to a server.
     */
    public void onServerJoin() {
        if (MinecraftClient.getInstance().isInSingleplayer()) {
            return;
        }

        final @Nullable ServerInfo connectedServer = MinecraftClient.getInstance().getCurrentServerEntry();
        if (connectedServer == null) {
            return;
        }

        // If the connected server is not in the "visible" servers of the server list (not a direct connection!), then
        // we can store it as the last connected server. `serverList.get` will still return the server if it was a recent
        // direct-connection attempt, hence the reason for the accessor.
        final ServerListAccessor serverListAccessor = ServerListUtil.getServerListAccessor();
        if (serverListAccessor.getServers().stream().noneMatch(it -> it.address.equals(connectedServer.address))) {
            return;
        }

        OneClickJoinConfig.INSTANCE.setLastServerAddress(connectedServer.address);
    }
}
