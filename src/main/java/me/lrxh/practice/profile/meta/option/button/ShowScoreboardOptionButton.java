package me.lrxh.practice.profile.meta.option.button;

import me.lrxh.practice.profile.Profile;
import me.lrxh.practice.profile.option.ProfileOptionButton;
import me.lrxh.practice.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class ShowScoreboardOptionButton extends ProfileOptionButton {

    @Override
    public String getOptionName() {
        return "&aShow Scoreboard";
    }

    @Override
    public ItemStack getEnabledItem(Player player) {
        return new ItemBuilder(Material.ITEM_FRAME).build();
    }

    @Override
    public ItemStack getDisabledItem(Player player) {
        return new ItemBuilder(Material.ITEM_FRAME).build();
    }

    @Override
    public String getDescription() {
        return "If enabled, a scoreboard will be displayed to you.";
    }

    @Override
    public String getEnabledOption() {
        return "Show you a scoreboard";
    }

    @Override
    public String getDisabledOption() {
        return "Do not show you a scoreboard";
    }

    @Override
    public boolean isEnabled(Player player) {
        return Profile.getProfiles().get(player.getUniqueId()).getOptions().showScoreboard();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        Profile profile = Profile.getProfiles().get(player.getUniqueId());
        profile.getOptions().showScoreboard(!profile.getOptions().showScoreboard());
    }

}
