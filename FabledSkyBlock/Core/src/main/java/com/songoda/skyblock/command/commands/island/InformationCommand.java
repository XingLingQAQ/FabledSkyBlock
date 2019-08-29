package com.songoda.skyblock.command.commands.island;

import com.songoda.skyblock.command.SubCommand;
import com.songoda.skyblock.config.FileManager.Config;
import com.songoda.skyblock.island.IslandManager;
import com.songoda.skyblock.menus.Information;
import com.songoda.skyblock.message.MessageManager;
import com.songoda.skyblock.playerdata.PlayerData;
import com.songoda.skyblock.playerdata.PlayerDataManager;
import com.songoda.skyblock.sound.SoundManager;
import com.songoda.skyblock.utils.player.OfflinePlayer;
import com.songoda.skyblock.utils.version.Sounds;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.UUID;

public class InformationCommand extends SubCommand {

    @Override
    public void onCommandByPlayer(Player player, String[] args) {
        PlayerDataManager playerDataManager = skyblock.getPlayerDataManager();
        MessageManager messageManager = skyblock.getMessageManager();
        IslandManager islandManager = skyblock.getIslandManager();
        SoundManager soundManager = skyblock.getSoundManager();

        if (playerDataManager.hasPlayerData(player)) {
            Config config = skyblock.getFileManager().getConfig(new File(skyblock.getDataFolder(), "language.yml"));
            FileConfiguration configLoad = config.getFileConfiguration();

            UUID islandOwnerUUID = null;

            if (args.length == 1) {
                Player targetPlayer = Bukkit.getServer().getPlayer(args[0]);

                if (targetPlayer == null) {
                    OfflinePlayer targetOfflinePlayer = new OfflinePlayer(args[0]);
                    islandOwnerUUID = targetOfflinePlayer.getOwner();
                } else {
                    islandOwnerUUID = playerDataManager.getPlayerData(targetPlayer).getOwner();
                }

                if (islandOwnerUUID == null) {
                    messageManager.sendMessage(player,
                            configLoad.getString("Command.Island.Information.Island.Message"));
                    soundManager.playSound(player, Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);

                    return;
                }
            } else if (args.length != 0) {
                messageManager.sendMessage(player, configLoad.getString("Command.Island.Information.Invalid.Message"));
                soundManager.playSound(player, Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);

                return;
            }

            PlayerData playerData = skyblock.getPlayerDataManager().getPlayerData(player);

            if (islandOwnerUUID == null) {
                if (islandManager.getIsland(player) == null) {
                    messageManager.sendMessage(player,
                            configLoad.getString("Command.Island.Information.Owner.Message"));
                    soundManager.playSound(player, Sounds.ANVIL_LAND.bukkitSound(), 1.0F, 1.0F);

                    return;
                } else {
                    islandOwnerUUID = playerData.getOwner();
                }
            }

            playerData.setViewer(new Information.Viewer(islandOwnerUUID, Information.Viewer.Type.Categories));
            Information.getInstance().open(player);
            soundManager.playSound(player, Sounds.CHEST_OPEN.bukkitSound(), 1.0F, 1.0F);
        }
    }

    @Override
    public void onCommandByConsole(ConsoleCommandSender sender, String[] args) {
        sender.sendMessage("SkyBlock | Error: You must be a player to perform that command.");
    }

    @Override
    public String getName() {
        return "information";
    }

    @Override
    public String getInfoMessagePath() {
        return "Command.Island.Information.Info.Message";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"info"};
    }

    @Override
    public String[] getArguments() {
        return new String[0];
    }
}
