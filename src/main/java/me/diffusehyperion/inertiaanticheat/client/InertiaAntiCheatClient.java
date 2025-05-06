package me.diffusehyperion.inertiaanticheat.client;

import com.moandjiezana.toml.Toml;
import me.diffusehyperion.inertiaanticheat.InertiaAntiCheat;
import me.diffusehyperion.inertiaanticheat.util.InertiaAntiCheatConstants;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.loading.FMLLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Mod(value = InertiaAntiCheat.MODID, dist = Dist.CLIENT)
public class InertiaAntiCheatClient {
    public static Toml clientConfig;
    public static final List<byte[]> allModData = new ArrayList<>();

    public InertiaAntiCheatClient() {
        InertiaAntiCheatClient.clientConfig = InertiaAntiCheat.initializeConfig("/config/client/InertiaAntiCheat.toml", InertiaAntiCheatConstants.CURRENT_CLIENT_CONFIG_VERSION);

        this.setupModDataList();
        ClientLoginModlistTransferHandler.init();
    }

    public void setupModDataList() {
        try {
            File modDirectory = FMLLoader.getGamePath().resolve("mods").toFile();
            for (File modFile : Objects.requireNonNull(modDirectory.listFiles())) {
                if (modFile.isDirectory()) {
                    continue;
                }
                if (!modFile.getAbsolutePath().endsWith(".jar")) {
                    continue;
                }
                InertiaAntiCheatClient.allModData.add(Files.readAllBytes(modFile.toPath()));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
