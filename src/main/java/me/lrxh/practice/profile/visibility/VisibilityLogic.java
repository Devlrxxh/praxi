package me.lrxh.practice.profile.visibility;

import me.lrxh.practice.match.participant.MatchGamePlayer;
import me.lrxh.practice.profile.Profile;
import me.lrxh.practice.profile.ProfileState;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class VisibilityLogic {

    public static void handle(Player viewer) {
        for (Player target : Bukkit.getOnlinePlayers()) {
            handle(viewer, target);
        }
    }

    public static void handle(Player viewer, Player target) {
        if (viewer == null || target == null) {
            return;
        }

        Profile viewerProfile = Profile.getByUuid(viewer.getUniqueId());
        Profile targetProfile = Profile.getByUuid(target.getUniqueId());

        if (viewerProfile.getState() == ProfileState.LOBBY || viewerProfile.getState() == ProfileState.QUEUEING) {
            if (viewer.equals(target)) {
                return;
            }

            if (viewerProfile.getParty() != null && viewerProfile.getParty().containsPlayer(target.getUniqueId())) {
                viewer.showPlayer(target);
            } else {
                viewer.hidePlayer(target);
            }
            if (viewerProfile.getOptions().showPlayers()) {
                for (Player players : Bukkit.getOnlinePlayers()) {
                    viewer.showPlayer(players);
                }
            }
        } else if (viewerProfile.getState() == ProfileState.FIGHTING) {
            if (viewer.equals(target)) {
                return;
            }

            MatchGamePlayer targetGamePlayer = viewerProfile.getMatch().getGamePlayer(target);

            if (targetGamePlayer != null) {
                if (!targetGamePlayer.isDead()) {
                    viewer.showPlayer(target);
                } else {
                    viewer.hidePlayer(target);
                }
            } else {
                viewer.hidePlayer(target);
            }

        } else if (viewerProfile.getState() == ProfileState.EVENT) {
            if (targetProfile.getState() == ProfileState.EVENT) {
                viewer.showPlayer(target);
            } else {
                viewer.hidePlayer(target);
            }
        } else if (viewerProfile.getState() == ProfileState.SPECTATING) {
            MatchGamePlayer targetGamePlayer = viewerProfile.getMatch().getGamePlayer(target);

            if (targetGamePlayer != null) {
                if (!targetGamePlayer.isDead() && !targetGamePlayer.isDisconnected()) {
                    viewer.showPlayer(target);
                } else {
                    viewer.hidePlayer(target);
                }
            } else {
                viewer.hidePlayer(target);
            }
        }
    }
}
