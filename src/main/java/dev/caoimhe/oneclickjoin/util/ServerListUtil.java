package dev.caoimhe.oneclickjoin.util;

import dev.caoimhe.oneclickjoin.mixin.ServerListAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.ServerList;

public class ServerListUtil {
    private ServerListUtil() {
    }

    public static ServerListAccessor getServerListAccessor() {
        final ServerList serverList = new ServerList(MinecraftClient.getInstance());
        serverList.loadFile();

        return (ServerListAccessor) serverList;
    }
}
