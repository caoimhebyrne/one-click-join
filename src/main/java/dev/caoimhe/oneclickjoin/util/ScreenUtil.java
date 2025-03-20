package dev.caoimhe.oneclickjoin.util;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.TranslatableTextContent;
import org.jetbrains.annotations.Nullable;

public class ScreenUtil {
    // This is a utility class, it should not be constructed.
    private ScreenUtil() {
    }

    /**
     * Attempts to find a button with the provided translation key on a screen.
     *
     * @param screen         The screen to find the button on.
     * @param translationKey The translation key of the button's text.
     * @return The button if found, otherwise null.
     */
    public static @Nullable ButtonWidget findButtonWithTranslationKey(final Screen screen, final String translationKey) {
        return screen.children()
            .stream()
            .filter(ButtonWidget.class::isInstance)
            .map(ButtonWidget.class::cast)
            .filter(widget -> {
                if (widget.getMessage().getContent() instanceof TranslatableTextContent content) {
                    return content.getKey().equals(translationKey);
                }

                return false;
            })
            .findFirst()
            .orElse(null);
    }
}
