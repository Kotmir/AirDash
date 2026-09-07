package tech.kotmir.airdash.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import tech.kotmir.airdash.AirDashConstants;

import java.net.URI;
import java.util.List;

public class AirDashConfigScreen {

    public static Screen createScreen(Screen parent) {
        // При открытии экрана загружаем актуальные значения из файла
        AirDashConfig.load();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.literal("AirDash — Настройки"))
                .setSavingRunnable(AirDashConfig::save); // Фиксируем сохранение в airdash.json при клике "Save & Quit"

        ConfigEntryBuilder entryBuilder = builder.getEntryBuilder();

        // --- КАТЕГОРИЯ: Настройки прыжка ---
        ConfigCategory movement = builder.getOrCreateCategory(Text.literal("Физика & Механика"));

        movement.addEntry(entryBuilder.startDoubleField(Text.literal("Сила прыжка (Высота)"), AirDashConfig.jumpPower)
                .setDefaultValue(0.70)
                .setMin(0.30)
                .setMax(2.00)
                .setTooltip(Text.literal("Отвечает за вертикальный импульс при рывке."))
                .setSaveConsumer(newValue -> AirDashConfig.jumpPower = newValue)
                .build());

        movement.addEntry(entryBuilder.startDoubleField(Text.literal("Сила рывка (Вперед)"), AirDashConfig.forwardPower)
                .setDefaultValue(0.80)
                .setMin(0.20)
                .setMax(3.00)
                .setTooltip(Text.literal("Ускорение по направлению взгляда игрока."))
                .setSaveConsumer(newValue -> AirDashConfig.forwardPower = newValue)
                .build());

        movement.addEntry(entryBuilder.startBooleanToggle(Text.literal("Защита от урона при падении"), AirDashConfig.disableFallDamage)
                .setDefaultValue(true)
                .setTooltip(Text.literal("Обнуляет дистанцию падения после применения двойного прыжка."))
                .setSaveConsumer(newValue -> AirDashConfig.disableFallDamage = newValue)
                .build());

        // --- КАТЕГОРИЯ: Партиклы и визуализация ---
        ConfigCategory visual = builder.getOrCreateCategory(Text.literal("Эффекты & Партиклы"));

        visual.addEntry(entryBuilder.startIntSlider(Text.literal("Количество партиклов"), AirDashConfig.particleCount, 0, 100)
                .setDefaultValue(25)
                .setTooltip(Text.literal("Плотность шлейфа партиклов во время полёта."))
                .setSaveConsumer(newValue -> AirDashConfig.particleCount = newValue)
                .build());

        visual.addEntry(entryBuilder.startStringDropdownMenu(Text.literal("Тип партиклов"), AirDashConfig.particleType)
                .setDefaultValue("FLAME")
                .setSelections(List.of("FLAME", "SOUL_FIRE_FLAME", "END_ROD", "HEART", "SMOKE"))
                .setTooltip(Text.literal("Выбери визуал шлейфа за спиной."))
                .setSaveConsumer(newValue -> AirDashConfig.particleType = newValue)
                .build());

        // --- КАТЕГОРИЯ: Информация ---
        ConfigCategory info = builder.getOrCreateCategory(Text.literal("Об авторе"));

        info.addEntry(entryBuilder.startTextDescription(
                Text.literal("Автор мода: ").formatted(Formatting.GRAY)
                        .append(Text.literal(AirDashConstants.AUTHOR).formatted(Formatting.GOLD, Formatting.BOLD))
        ).build());

        info.addEntry(entryBuilder.startTextDescription(
                Text.literal("Официальный сайт: ").formatted(Formatting.GRAY)
                        .append(Text.literal(AirDashConstants.WEBSITE_URL)
                                .formatted(Formatting.AQUA, Formatting.UNDERLINE)
                                .styled(s -> s.withClickEvent(new ClickEvent.OpenUrl(URI.create(AirDashConstants.WEBSITE_URL)))))
        ).build());

        info.addEntry(entryBuilder.startTextDescription(
                Text.literal("Discord сообщество: ").formatted(Formatting.GRAY)
                        .append(Text.literal(AirDashConstants.DISCORD_URL)
                                .formatted(Formatting.LIGHT_PURPLE, Formatting.UNDERLINE)
                                .styled(s -> s.withClickEvent(new ClickEvent.OpenUrl(URI.create(AirDashConstants.DISCORD_URL)))))
        ).build());

        return builder.build();
    }
}