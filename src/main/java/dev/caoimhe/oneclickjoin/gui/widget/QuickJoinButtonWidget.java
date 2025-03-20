package dev.caoimhe.oneclickjoin.gui.widget;

import dev.caoimhe.oneclickjoin.OneClickJoin;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.gui.screen.world.WorldIcon;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public class QuickJoinButtonWidget extends ButtonWidget {
    private static final int WIDTH = 20;
    private static final int HEIGHT = 20;
    private static final int IMAGE_PADDING = 2;

    private final @Nullable ButtonWidget neighbor;
    private final @Nullable WorldIcon worldIcon;

    public QuickJoinButtonWidget(final @Nullable ButtonWidget neighbor, final @Nullable ServerInfo serverInfo) {
        super(
            5,
            5,
            WIDTH,
            HEIGHT,
            Text.empty(),
            (widget) -> {
                if (serverInfo == null) {
                    return;
                }

                ConnectScreen.connect(
                    new TitleScreen(),
                    MinecraftClient.getInstance(),
                    ServerAddress.parse(serverInfo.address),
                    serverInfo,
                    /* quickPlay */ false,
                    /* cookieStorage */ null
                );
            },
            ButtonWidget.DEFAULT_NARRATION_SUPPLIER
        );

        this.neighbor = neighbor;

        if (serverInfo != null) {
            this.worldIcon = WorldIcon.forServer(MinecraftClient.getInstance().getTextureManager(), serverInfo.address);
            this.setTooltip(Tooltip.of(Text.literal("Join server " + serverInfo.name + "...")));

            final byte[] bytes = serverInfo.getFavicon();
            if (bytes != null && bytes.length > 0) {
                try {
                    this.worldIcon.load(NativeImage.read(bytes));
                } catch (final Exception e) {
                    OneClickJoin.LOGGER.warn("Failed to load icon for server '{}'", serverInfo.address, e);
                }
            }
        } else {
            this.worldIcon = null;
            this.active = false;
            this.setTooltip(Tooltip.of(Text.literal("Join a server to be able to quick-join!")));
        }
    }

    // It's not enough to set the initial X and Y to the neighbor's position. In the case of the multiplayer button
    // on the main menu, it has a different Y position momentarily if the first screen is the accessibility
    // onboarding screen (I'm still unsure why).
    // The best way I can see is to always pin this button's position to its neighbor. There *might* be a better
    // way to do this, especially because I haven't considered the fact that this will conflict with other buttons
    // that are placed on the right-side of the multiplayer button right now.
    @Override
    public int getX() {
        if (this.neighbor != null) {
            return this.neighbor.getX() + this.neighbor.getWidth() + 4;
        }

        return 5;
    }

    @Override
    public int getY() {
        if (this.neighbor != null) {
            return this.neighbor.getY();
        }

        return 5;
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        super.renderWidget(context, mouseX, mouseY, delta);

        if (this.worldIcon != null) {
            context.drawTexture(
                //#if MC>=12102
                RenderLayer::getGuiTextured,
                //#endif
                this.worldIcon.getTextureId(),
                this.getX() + (IMAGE_PADDING / 2),
                this.getY() + (IMAGE_PADDING / 2),
                /* u */ 0,
                /* v */ 0,
                WIDTH - IMAGE_PADDING,
                HEIGHT - IMAGE_PADDING,
                WIDTH - IMAGE_PADDING,
                HEIGHT - IMAGE_PADDING
            );
        }
    }
}
