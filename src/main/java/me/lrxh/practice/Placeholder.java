package me.lrxh.practice;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.lrxh.practice.leaderboards.Leaderboard;
import me.lrxh.practice.leaderboards.PlayerElo;
import me.lrxh.practice.profile.Profile;
import me.lrxh.practice.util.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class Placeholder extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "practice";
    }

    @Override
    public @NotNull String getAuthor() {
        return "lrxh";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public boolean canRegister() {
        return Bukkit.getPluginManager().isPluginEnabled(Practice.getInstance());
    }

    @Override
    public String onRequest(OfflinePlayer player, @NotNull String identifier) {
        if (player == null) return "";
        if (!player.isOnline()) return "Offline Player";

        String[] parts = identifier.split("_");
        if (parts.length == 4 && parts[0].equalsIgnoreCase("lb")) {
            String queue = parts[1];
            int position = Integer.parseInt(parts[2]);
            PlayerElo playerElo = Leaderboard.getEloLeaderboards().get(queue).getTopPlayers().get(position - 1);

            switch (parts[3]) {
                case "name":
                    return playerElo.getPlayerName();
                case "elo":
                    return String.valueOf(playerElo.getElo());
            }
        } else if (identifier.equalsIgnoreCase("leaderboards_update")) {
            return TimeUtil.millisToTimer(Leaderboard.getRefreshTime());
        } else if (identifier.equalsIgnoreCase("player_theme")) {
            return String.valueOf(Profile.getByUuid(player.getUniqueId()).getOptions().theme().getColor().getChar());
        }
        return "test";
    }
}
