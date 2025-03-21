package dev.caoimhe.oneclickjoin;

// @formatter:off
//#if NEOFORGE
//$$ import net.neoforged.fml.common.Mod;
//$$ import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
//$$ import net.neoforged.neoforge.client.event.ScreenEvent;
//$$ import net.neoforged.neoforge.common.NeoForge;
//#elseif FABRIC
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.Screens;
//#endif

import com.mojang.logging.LogUtils;
import dev.caoimhe.oneclickjoin.config.OneClickJoinConfig;
import dev.caoimhe.oneclickjoin.event.ScreenEventListener;
import dev.caoimhe.oneclickjoin.event.ServerEventListener;
import org.slf4j.Logger;

//#if NEOFORGE
//$$ @Mod("one_click_join")
//$$ public class OneClickJoin {
//#elseif FABRIC
public class OneClickJoin implements ClientModInitializer {
//#endif
    public static final Logger LOGGER = LogUtils.getLogger();

    private final ScreenEventListener screenEventListener = new ScreenEventListener();
    private final ServerEventListener serverEventListener = new ServerEventListener();

    //#if NEOFORGE
    //$$ public OneClickJoin() {
    //$$     NeoForge.EVENT_BUS.addListener(ScreenEvent.Init.Post.class, (event) -> {
    //$$         this.screenEventListener.afterScreenInitialized(event.getScreen(), event::addListener);
    //$$     });
    //$$
    //$$     NeoForge.EVENT_BUS.addListener(ClientPlayerNetworkEvent.LoggingIn.class, (event) -> {
    //$$         this.serverEventListener.onServerJoin();
    //$$     });
    //#elseif FABRIC
    @Override
    public void onInitializeClient() {
        ScreenEvents.AFTER_INIT.register(((client, screen, scaledWidth, scaledHeight) -> {
            this.screenEventListener.afterScreenInitialized(screen, Screens.getButtons(screen)::add);
        }));

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            this.serverEventListener.onServerJoin();
        });
        //#endif

        OneClickJoinConfig.INSTANCE.load();
    }
}
