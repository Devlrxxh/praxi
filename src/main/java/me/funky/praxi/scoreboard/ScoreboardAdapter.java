package me.funky.praxi.scoreboard;

import me.funky.praxi.Practice;
import me.funky.praxi.match.Match;
import me.funky.praxi.match.MatchState;
import me.funky.praxi.profile.Profile;
import me.funky.praxi.profile.ProfileState;
import me.funky.praxi.queue.QueueProfile;
import me.funky.praxi.util.PlaceholderUtil;
import me.funky.praxi.util.assemble.AssembleAdapter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ScoreboardAdapter implements AssembleAdapter {
    private int index;

    public String getTitle(Player player) {
        Profile profile = Profile.getByUuid(player.getUniqueId());
        ArrayList<String> list = new ArrayList<>();
        if (!profile.getOptions().scoreboardLines()) {
            list.add("   " + getAnimation("TITLE") + "   ");
            return PlaceholderUtil.format(list, player).toString().replace("[", "").replace("]", "");
        } else {
            list.add(getAnimation("TITLE"));
            return PlaceholderUtil.format(list, player).toString().replace("[", "").replace("]", "");
        }
    }

    public List<String> getLines(Player player) {
        Profile profile = Profile.getByUuid(player.getUniqueId());

        if (profile.getState() == ProfileState.LOBBY) {

            if (profile.getParty() != null) {
                return PlaceholderUtil.format(new ArrayList<>(Practice.getInstance().getScoreboardConfig().getStringList("IN-PARTY")), player);
            }
            return PlaceholderUtil.format(new ArrayList<>(Practice.getInstance().getScoreboardConfig().getStringList("LOBBY")), player);
        }

        if (profile.getState() == ProfileState.SPECTATING) {
            return PlaceholderUtil.format(new ArrayList<>(Practice.getInstance().getScoreboardConfig().getStringList("SPECTATING")), player);
        }

        if (profile.getState() == ProfileState.QUEUEING) {
            QueueProfile queueProfile = profile.getQueueProfile();

            if (queueProfile.getQueue().isRanked()) {
                return PlaceholderUtil.format(new ArrayList<>(Practice.getInstance().getScoreboardConfig().getStringList("QUEUE.RANKED")), player);
            }
            return PlaceholderUtil.format(new ArrayList<>(Practice.getInstance().getScoreboardConfig().getStringList("QUEUE.UNRANKED")), player);
        }


        if (profile.getState() == ProfileState.FIGHTING) {
            Match match = profile.getMatch();
            if (match.getState().equals(MatchState.STARTING_ROUND)) {
                return PlaceholderUtil.format(new ArrayList<>(Practice.getInstance().getScoreboardConfig().getStringList("MATCH.STARTING")), player);
            }
            if (match.getState().equals(MatchState.ENDING_MATCH)) {
                return PlaceholderUtil.format(new ArrayList<>(Practice.getInstance().getScoreboardConfig().getStringList("MATCH.ENDING")), player);
            }
            if (match.getState().equals(MatchState.PLAYING_ROUND)) {
                if (match.getKit().getGameRules().isBoxing()) {
                    return PlaceholderUtil.format(new ArrayList<>(Practice.getInstance().getScoreboardConfig().getStringList("MATCH.IN-MATCH-BOXING")), player);
                }
                return PlaceholderUtil.format(new ArrayList<>(Practice.getInstance().getScoreboardConfig().getStringList("MATCH.IN-MATCH")), player);
            }
        }

        return null;
    }

    private String getAnimation(String configLocation) {
        List<String> footerList = Practice.getInstance().getScoreboardConfig().getStringList(configLocation);

        if (index >= footerList.size()) {
            index = 0;
        }
        return footerList.get(index++);
    }
}
