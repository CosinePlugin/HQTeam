package kr.hqservice.team.service;

import kr.hqservice.team.enums.Result;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import java.util.UUID;

public interface TeamService {

    Result createTeam(UUID uuid, String teamName);

    Result invitePlayer(Player player, String targetName) throws NullPointerException;

    Result acceptInvite(UUID uuid);

    Result refuseInvite(UUID uuid);

    Result showTeamInfo(Player player, String teamName);

    Result leaveTeam(Player player);

    Result deleteTeam(Player player);

    void preventTeamDamage(Cancellable event, Player player, Player target);
}
