package com.divinitysreach.beaconbeamtoggle;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class BeaconBeamToggleClient implements ClientModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger("BeaconBeamToggle");
    private static final Path CONFIG = FabricLoader.getInstance().getConfigDir().resolve("beaconbeamtoggle.txt");

    /** Read by the mixin every frame. */
    public static volatile boolean beamsEnabled = true;

    private static KeyMapping toggleKey;

    @Override
    public void onInitializeClient() {
        load();

        toggleKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
            "key.beaconbeamtoggle.toggle",
            InputConstants.KEY_B,
            KeyMapping.Category.MISC
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (toggleKey.consumeClick()) {
                setEnabled(!beamsEnabled);
            }
        });

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) ->
            dispatcher.register(ClientCommands.literal("beaconbeams")
                .executes(ctx -> { setEnabled(!beamsEnabled); return 1; })
                .then(ClientCommands.literal("on").executes(ctx -> { setEnabled(true); return 1; }))
                .then(ClientCommands.literal("off").executes(ctx -> { setEnabled(false); return 1; }))
                .then(ClientCommands.literal("toggle").executes(ctx -> { setEnabled(!beamsEnabled); return 1; }))));

        LOGGER.info("BeaconBeamToggle loaded, beams {}", beamsEnabled ? "on" : "off");
    }

    public static void setEnabled(boolean enabled) {
        beamsEnabled = enabled;
        save();
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            mc.player.sendOverlayMessage(Component.literal("Beacon beams: ")
                .append(enabled
                    ? Component.literal("ON").withStyle(ChatFormatting.GREEN)
                    : Component.literal("OFF").withStyle(ChatFormatting.RED)));
        }
    }

    private static void load() {
        try {
            if (Files.exists(CONFIG)) {
                String s = Files.readString(CONFIG, StandardCharsets.UTF_8).trim();
                beamsEnabled = !s.equalsIgnoreCase("enabled=false");
            }
        } catch (IOException e) {
            LOGGER.warn("Could not read config", e);
        }
    }

    private static void save() {
        try {
            Files.createDirectories(CONFIG.getParent());
            Files.writeString(CONFIG, "enabled=" + beamsEnabled + System.lineSeparator(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            LOGGER.warn("Could not save config", e);
        }
    }
}
