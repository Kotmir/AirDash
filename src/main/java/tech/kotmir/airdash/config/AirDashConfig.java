package tech.kotmir.airdash.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class AirDashConfig {
    private static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("airdash.json").toFile();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static double jumpPower = 0.70;
    public static double forwardPower = 0.80;
    public static int particleCount = 25;
    public static boolean disableFallDamage = true;
    public static String particleType = "FLAME";

    public static void load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                ConfigData data = GSON.fromJson(reader, ConfigData.class);
                if (data != null) {
                    jumpPower = data.jumpPower;
                    forwardPower = data.forwardPower;
                    particleCount = data.particleCount;
                    disableFallDamage = data.disableFallDamage;
                    particleType = data.particleType;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            save();
        }
    }

    public static void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            ConfigData data = new ConfigData();
            data.jumpPower = jumpPower;
            data.forwardPower = forwardPower;
            data.particleCount = particleCount;
            data.disableFallDamage = disableFallDamage;
            data.particleType = particleType;
            GSON.toJson(data, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class ConfigData {
        double jumpPower;
        double forwardPower;
        int particleCount;
        boolean disableFallDamage;
        String particleType;
    }
}