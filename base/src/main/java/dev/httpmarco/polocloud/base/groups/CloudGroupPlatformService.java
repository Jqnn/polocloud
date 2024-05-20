package dev.httpmarco.polocloud.base.groups;

import dev.httpmarco.osgan.files.Files;
import dev.httpmarco.polocloud.api.services.CloudService;
import dev.httpmarco.polocloud.base.groups.platforms.PaperMCPlatform;
import dev.httpmarco.polocloud.base.groups.platforms.Platform;
import dev.httpmarco.polocloud.base.services.LocalCloudService;
import lombok.SneakyThrows;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class CloudGroupPlatformService {

    private static final Path PLATFORM_FOLDER = Path.of("local/platforms");
    private final Set<Platform> platforms = new HashSet<>();

    public CloudGroupPlatformService() {

        Files.createDirectoryIfNotExists(PLATFORM_FOLDER);

        this.platforms.add(new PaperMCPlatform("velocity"));
        this.platforms.add(new PaperMCPlatform("paper"));
    }


    public boolean isValidPlatform(String platform) {
        return platforms.stream().anyMatch(it -> it.possibleVersions().stream().anyMatch(v -> v.equalsIgnoreCase(platform)));
    }

    private Platform find(String platform) {
        return platforms.stream().filter(it -> it.possibleVersions().stream().anyMatch(v -> v.equalsIgnoreCase(platform))).findFirst().orElse(null);
    }

    private boolean isPlatformAvailableForRuntime(String platform) {
        return java.nio.file.Files.exists(PLATFORM_FOLDER.resolve(platform + ".jar"));
    }

    @SneakyThrows
    public void preparePlatform(LocalCloudService cloudService) {
        var platform = cloudService.group().platform();
        if (!isPlatformAvailableForRuntime(platform)) {
            find(platform).download(platform);
        }

        var platformFile = cloudService.group().platform() + ".jar";
        java.nio.file.Files.copy(PLATFORM_FOLDER.resolve(cloudService.group().platform() + ".jar"), cloudService.runningFolder().resolve(platformFile));
    }
}