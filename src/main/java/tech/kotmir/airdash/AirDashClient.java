package tech.kotmir.airdash;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import tech.kotmir.airdash.config.AirDashConfigScreen;

import java.net.URI;

public class AirDashClient implements ClientModInitializer {
    public static KeyBinding openConfigKey;

    @Override
    public void onInitializeClient() {
        // Категория объявляется через KeyBinding.Category в 1.21.11+
        openConfigKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.airdash.open_config",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_O,
            KeyBinding.Category.create(Identifier.of("airdash", "general"))
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openConfigKey.wasPressed()) {
                if (client.player != null) {
                    client.setScreen(AirDashConfigScreen.createScreen(client.currentScreen));
                }
            }
        });

        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            if (client.player != null) {
                Text siteMsg = Text.literal("[AirDash] ").formatted(Formatting.GREEN, Formatting.BOLD)
                        .append(Text.literal("Сайт: ").formatted(Formatting.WHITE))
                        .append(Text.literal("https://kotmir.tech")
                                .formatted(Formatting.DARK_AQUA, Formatting.UNDERLINE)
                                .styled(s -> s.withClickEvent(new ClickEvent.OpenUrl(URI.create("https://kotmir.tech")))));

                Text discordMsg = Text.literal("[AirDash] ").formatted(Formatting.GREEN, Formatting.BOLD)
                        .append(Text.literal("Discord: ").formatted(Formatting.WHITE))
                        .append(Text.literal("https://discord.gg/kotmiks")
                                .formatted(Formatting.LIGHT_PURPLE, Formatting.UNDERLINE)
                                .styled(s -> s.withClickEvent(new ClickEvent.OpenUrl(URI.create("https://discord.gg/kotmiks")))));

                client.player.sendMessage(siteMsg, false);
                client.player.sendMessage(discordMsg, false);
            }
        });
    }
}