package tropicsnowjpr.ringexchange.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import tropicsnowjpr.ringexchange.RingExchangeMod;

public class RingExchangeConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    /** Maximum shops a non-op player may own. Ops are unlimited. */
    public int maxShopsPerPlayer = 3;

    public static RingExchangeConfig load() {
        Path path = path();
        try {
            if (Files.exists(path)) {
                RingExchangeConfig cfg = GSON.fromJson(Files.readString(path), RingExchangeConfig.class);
                if (cfg != null) {
                    cfg.save();
                    return cfg;
                }
            }
        } catch (Exception e) {
            RingExchangeMod.LOGGER.error("Failed to load config; using defaults", e);
        }
        RingExchangeConfig cfg = new RingExchangeConfig();
        cfg.save();
        return cfg;
    }

    public void save() {
        try {
            Path path = path();
            Files.createDirectories(path.getParent());
            Files.writeString(path, GSON.toJson(this));
        } catch (IOException e) {
            RingExchangeMod.LOGGER.error("Failed to save config", e);
        }
    }

    private static Path path() {
        return FabricLoader.getInstance().getConfigDir().resolve("villagershop.json");
    }
}
