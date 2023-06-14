package kr.hqservice.team.data;

import org.bukkit.Server;
import org.bukkit.entity.Player;

import java.util.*;

public class Team {

    private final String teamName;

    private final UUID owner;
    private final Set<UUID> members = new HashSet<>();

    public Team(String teamName, UUID owner) {
        this.teamName = teamName;
        this.owner = owner;
    }

    public String getTeamName() {
        return teamName;
    }

    public UUID getOwner() {
        return owner;
    }

    public boolean contains(UUID uuid) {
        return getMembersWithOwner().contains(uuid);
    }

    public List<UUID> getMembersWithOwner() {
        List<UUID> allMembers = new ArrayList<>(members);
        allMembers.add(owner);
        return allMembers;
    }

    public List<UUID> getMembers() {
        return new ArrayList<>(members);
    }

    public void addMember(UUID uuid) {
        members.add(uuid);
    }

    public void removeMember(UUID uuid) {
        members.remove(uuid);
    }

    public void sendMessage(Server server, String message) {
        getMembersWithOwner().forEach((memberUUID) -> {
            Player member = server.getPlayer(memberUUID);
            if (member != null) {
                member.sendMessage(message);
            }
        });
    }
}
