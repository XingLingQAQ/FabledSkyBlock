package com.songoda.skyblock.world.generator;

import com.songoda.skyblock.SkyBlock;
import com.songoda.skyblock.config.FileManager.Config;
import com.songoda.skyblock.island.IslandWorld;
import com.songoda.skyblock.utils.version.Materials;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class VoidGenerator extends ChunkGenerator {

    @Override
    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
        ChunkData chunkData = createChunkData(world);

        SkyBlock skyblock = SkyBlock.getInstance();

        Config config = skyblock.getFileManager().getConfig(new File(skyblock.getDataFolder(), "config.yml"));
        FileConfiguration configLoad = config.getFileConfiguration();

        for (IslandWorld worldList : IslandWorld.values()) {
            if (world.getEnvironment() == worldList.getUncheckedEnvironment()) {
                if (configLoad.getBoolean("Island.World." + worldList.name() + ".Liquid.Enable")) {
                    if (configLoad.getBoolean("Island.World." + worldList.name() + ".Liquid.Lava")) {
                        setBlock(chunkData, Materials.LEGACY_STATIONARY_LAVA.parseMaterial(),
                                configLoad.getInt("Island.World." + worldList.name() + ".Liquid.Height"));
                    } else {
                        setBlock(chunkData, Materials.LEGACY_STATIONARY_WATER.parseMaterial(),
                                configLoad.getInt("Island.World." + worldList.name() + ".Liquid.Height"));
                    }
                }

                break;
            }
        }

        return chunkData;
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(final World world) {
        return Arrays.asList(new BlockPopulator[0]);
    }

    @Override
    public boolean canSpawn(World world, int x, int z) {
        return true;
    }

    public byte[][] generateBlockSections(World world, Random random, int chunkX, int chunkZ, BiomeGrid biomeGrid) {
        return new byte[world.getMaxHeight() / 16][];
    }

    private void setBlock(ChunkData chunkData, Material material, int height) {
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 0; y < height; y++) {
                    chunkData.setBlock(x, y, z, material);
                }
            }
        }
    }
}
