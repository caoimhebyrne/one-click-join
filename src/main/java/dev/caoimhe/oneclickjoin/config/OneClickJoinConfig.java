package dev.caoimhe.oneclickjoin.config;

import dev.caoimhe.oneclickjoin.OneClickJoin;
import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Objects;

public class OneClickJoinConfig {
    public static final OneClickJoinConfig INSTANCE = new OneClickJoinConfig();

    private static final Path CONFIG_PATH = MinecraftClient.getInstance().runDirectory.toPath().resolve("config").resolve("one-click-join");
    private static final Logger LOGGER = LoggerFactory.getLogger(OneClickJoinConfig.class);

    private @Nullable String lastServerAddress = null;

    public void load() {
        try {
            this.lastServerAddress = Files.readString(CONFIG_PATH).trim();
        } catch (final NoSuchFileException e) {
            LOGGER.warn("No configuration file found ({}), assuming this is okay, using default values", CONFIG_PATH.toAbsolutePath());
        } catch (final IOException e) {
            LOGGER.warn("Failed to read config file, using default values", e);
        }
    }

    public @Nullable String lastServerAddress() {
        return this.lastServerAddress;
    }

    public void setLastServerAddress(final @Nullable String lastServerAddress) {
        this.lastServerAddress = lastServerAddress;

        try {
            Files.writeString(CONFIG_PATH, Objects.requireNonNullElse(this.lastServerAddress, ""));
        } catch (final IOException e) {
            LOGGER.warn("Failed to write last server address to config file", e);
        }
    }
}
