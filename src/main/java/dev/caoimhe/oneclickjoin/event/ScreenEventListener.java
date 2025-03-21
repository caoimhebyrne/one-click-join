package dev.caoimhe.oneclickjoin.event;

import dev.caoimhe.oneclickjoin.OneClickJoin;
import dev.caoimhe.oneclickjoin.config.OneClickJoinConfig;
import dev.caoimhe.oneclickjoin.gui.widget.QuickJoinButtonWidget;
import dev.caoimhe.oneclickjoin.util.ScreenUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.option.ServerList;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * Called by the platform's screen events API. Responsible for adding the quick join button to the main menu.
 *
 * @see OneClickJoin
 */
public class ScreenEventListener {
    private static final String MULTIPLAYER_I18N_KEY = "menu.multiplayer";

    /**
     * Called after a screen has been initialized.
     *
     * @param screen             The screen that was initialized.
     * @param addClickableWidget A consumer which takes a {@link ClickableWidget} to add to the current screen.
     */
    public void afterScreenInitialized(final Screen screen, final Consumer<ClickableWidget> addClickableWidget) {
        if (!(screen instanceof TitleScreen)) {
            return;
        }

        final ServerList serverList = new ServerList(MinecraftClient.getInstance());
        serverList.loadFile();

        final @Nullable String serverAddress = OneClickJoinConfig.INSTANCE.lastServerAddress();
        final @Nullable ServerInfo serverInfo = serverAddress != null ? serverList.get(serverAddress) : null;

        final @Nullable ButtonWidget multiplayerButton = ScreenUtil.findButtonWithTranslationKey(screen, MULTIPLAYER_I18N_KEY);
        addClickableWidget.accept(new QuickJoinButtonWidget(multiplayerButton, serverInfo));
    }
}
