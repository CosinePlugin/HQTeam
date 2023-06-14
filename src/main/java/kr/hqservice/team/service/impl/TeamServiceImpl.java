package kr.hqservice.team.service.impl;

import kr.hqservice.team.HQTeam;
import kr.hqservice.team.data.Team;
import kr.hqservice.team.enums.Result;
import kr.hqservice.team.repository.TeamRepository;
import kr.hqservice.team.service.InviteService;
import kr.hqservice.team.service.TeamService;
import org.bukkit.OfflinePlayer;
import org.bukkit.Server;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static kr.hqservice.team.HQTeam.prefix;

public class TeamServiceImpl implements TeamService {

    private final InviteService inviteService;
    private final TeamRepository teamRepository;

    public TeamServiceImpl(InviteService inviteService, TeamRepository teamRepository) {
        this.inviteService = inviteService;
        this.teamRepository = teamRepository;
    }

    @Override
    public Result createTeam(UUID uuid, String teamName) {
        if (teamRepository.getTeam(uuid) != null) {
            return Result.HAS_TEAM;
        }
        if (teamRepository.contains(teamName)) {
            return Result.IS_CREATED_TEAM;
        }
        Team team = new Team(teamName, uuid);
        teamRepository.setTeam(teamName, team);
        return Result.SUCCESSFUL;
    }

    @Override
    public Result invitePlayer(Player player, String targetName) throws NullPointerException {
        Team team = teamRepository.getTeam(player.getUniqueId());
        if (team == null) {
            return Result.NOT_JOINED_TEAM;
        }
        if (player.getName().equals(targetName)) {
            return Result.IS_SELF;
        }
        Server server = player.getServer();
        Player target = server.getPlayer(targetName);
        if (target == null) {
            return Result.IS_INVALID_PLAYER;
        }
        UUID targetUUID = target.getUniqueId();
        Team targetTeam = teamRepository.getTeam(targetUUID);
        if (targetTeam != null) {
            return Result.HAS_TEAM;
        }
        if (inviteService.isInvited(targetUUID)) {
            return Result.IS_INVITED;
        }
        String teamName = team.getTeamName();

        player.sendMessage(prefix + " " + targetName + "님에게 팀 초대를 보냈습니다.");
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);

        target.sendMessage(prefix + " " + teamName + " 팀에서 초대가 왔습니다.");
        target.sendMessage("§7└ 수락하시려면 §a/팀 수락§7, 거절하시려면 §c/팀 거절");
        target.playSound(target.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1f, 1f);

        inviteService.invite(targetUUID, (invite) -> {
            switch (invite) {
                case ACCEPT: {
                    team.addMember(targetUUID);
                    team.sendMessage(server, prefix + " " + targetName + "님이 팀에 참가하였습니다.");
                    break;
                }
                case REFUSE: case TIME_OUT: {
                    player.sendMessage(prefix + " " + targetName + "님이 초대를 거절하였습니다.");
                    break;
                }
            }
        });
        return Result.NONE;
    }

    @Override
    public Result acceptInvite(UUID uuid) {
        return inviteService.accept(uuid);
    }

    @Override
    public Result refuseInvite(UUID uuid) {
        return inviteService.refuse(uuid);
    }

    @Override
    public Result showTeamInfo(Player player, String teamName) {
        Team team = teamRepository.getTeam(teamName);

        if (team == null) {
            return Result.NOT_EXIST_TEAM;
        }

        Server server = player.getServer();
        server.getScheduler().runTaskAsynchronously(HQTeam.getInstance(), () -> {
            OfflinePlayer offlineLeader = server.getOfflinePlayer(team.getOwner());
            String leaderName = offlineLeader.getName();
            List<String> offlineMembers = team.getMembers().stream().map(server::getOfflinePlayer).map(OfflinePlayer::getName).collect(Collectors.toList());
            String membersName = offlineMembers.toString().replace("[", "").replace("]", "");

            player.sendMessage("§6§l[ " + teamName + " ] §f§l팀 정보");
            player.sendMessage("§7└ 팀장: §f" + leaderName);
            player.sendMessage("§7└ 팀원: §f" + membersName);
        });

        return Result.NONE;
    }

    @Override
    public Result leaveTeam(Player player) {
        UUID uuid = player.getUniqueId();
        Team team = teamRepository.getTeam(uuid);

        if (team == null) {
            return Result.NOT_JOINED_TEAM;
        }
        if (team.getOwner() == uuid) {
            return Result.IS_LEADER;
        }

        team.removeMember(uuid);
        team.sendMessage(player.getServer(), prefix + " " + player.getName() + "님이 팀에서 탈퇴하였습니다.");

        return Result.SUCCESSFUL;
    }

    @Override
    public Result deleteTeam(Player player) {
        UUID uuid = player.getUniqueId();
        Team team = teamRepository.getTeam(uuid);

        if (team == null) {
            return Result.NOT_JOINED_TEAM;
        }
        if (team.getOwner() != uuid) {
            return Result.NOT_LEADER;
        }

        String teamName = team.getTeamName();
        teamRepository.removeTeam(teamName);

        return Result.SUCCESSFUL;
    }

    @Override
    public void preventTeamDamage(Cancellable event, Player player, Player target) {
        Team team = teamRepository.getTeam(player.getUniqueId());
        if (team == null) return;
        if (team.contains(target.getUniqueId())) {
            event.setCancelled(true);
        }
    }
}
