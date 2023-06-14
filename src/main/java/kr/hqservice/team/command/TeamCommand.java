package kr.hqservice.team.command;

import kr.hqservice.team.enums.Result;
import kr.hqservice.team.service.TeamService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.function.BiConsumer;

import static kr.hqservice.team.HQTeam.prefix;

public class TeamCommand implements CommandExecutor {

    private final String[] commandHelp = {
            prefix + " 팀 명령어 도움말",
            "",
            prefix + " /팀 생성 [이름] : 팀을 생성합니다.",
            prefix + " /팀 정보 [팀]: 팀 정보를 확인합니다.",
            prefix + " /팀 초대 [닉네임] : 팀에 플레이어를 초대합니다.",
            prefix + " /팀 수락 : 팀 초대를 수락합니다.",
            prefix + " /팀 거절 : 팀 초대를 거절합니다.",
            prefix + " /팀 탈퇴 : 팀을 탈퇴합니다.",
            prefix + " /팀 제거 : 팀을 제거합니다."
    };

    private final TeamService teamService;

    public TeamCommand(TeamService teamService) {
        this.teamService = teamService;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(prefix + " 콘솔에서 사용할 수 없는 명령어입니다.");
            return true;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            printHelp(player);
            return true;
        }
        checker(player, args);
        return true;
    }

    private void printHelp(Player player) {
        player.sendMessage(commandHelp);
    }

    private void checker(Player player, String[] args) {
        String label;
        BiConsumer<Player, String> function;

        switch (args[0]) {
            case "생성": {
                label = "이름";
                function = this::createTeam;
                break;
            }

            case "정보": {
                label = "팀";
                function = this::showTeamInfo;
                break;
            }

            case "초대": {
                label = "닉네임";
                function = this::invitePlayer;
                break;
            }

            case "수락": {
                acceptInvite(player);
                return;
            }

            case "거절": {
                refuseInvite(player);
                return;
            }

            case "탈퇴": {
                leaveTeam(player);
                return;
            }

            case "제거": {
                deleteTeam(player);
                return;
            }

            default: {
                printHelp(player);
                return;
            }
        }
        if (args.length == 1) {
            player.sendMessage(prefix + " " + label + "을 적어주세요.");
        } else {
            function.accept(player, args[1]);
        }
    }

    private void createTeam(Player player, String teamName) {
        switch (teamService.createTeam(player.getUniqueId(), teamName)) {
            case HAS_TEAM: {
                player.sendMessage(prefix + " 이미 팀에 소속되어 있습니다.");
                return;
            }
            case IS_CREATED_TEAM: {
                player.sendMessage(prefix + " 이미 존재하는 팀입니다.");
                return;
            }
            case SUCCESSFUL: {
                player.sendMessage(prefix + " " + teamName + " 팀을 생성하였습니다.");
            }
        }
    }

    private void showTeamInfo(Player player, String teamName) {
        if (teamService.showTeamInfo(player, teamName) == Result.NOT_EXIST_TEAM) {
            player.sendMessage(prefix + " 존재하지 않는 팀입니다.");
        }
    }

    private void invitePlayer(Player player, String targetName) {
        switch (teamService.invitePlayer(player, targetName)) {
            case NOT_JOINED_TEAM: {
                player.sendMessage(prefix + " 팀이 없습니다.");
                return;
            }
            case IS_SELF: {
                player.sendMessage(prefix + " 자신을 초대할 수 없습니다.");
                return;
            }
            case IS_INVALID_PLAYER: {
                player.sendMessage(prefix + " 해당 플레이어가 오프라인입니다.");
                return;
            }
            case HAS_TEAM: {
                player.sendMessage(prefix + " 해당 플레어이가 이미 팀에 소속되어 있습니다.");
                return;
            }
            case IS_INVITED: {
                player.sendMessage(prefix + " 해당 플레이어가 다른 초대를 보유 중입니다.");
            }
        }
    }

    private void acceptInvite(Player player) {
        switch (teamService.acceptInvite(player.getUniqueId())) {
            case NOT_INVITED: {
                player.sendMessage(prefix + " 받은 초대가 없습니다.");
                return;
            }
            case SUCCESSFUL: {
                player.sendMessage(prefix + " 초대를 수락했습니다.");
            }
        }
    }

    private void refuseInvite(Player player) {
        switch (teamService.refuseInvite(player.getUniqueId())) {
            case NOT_INVITED: {
                player.sendMessage(prefix + " 받은 초대가 없습니다.");
                return;
            }
            case SUCCESSFUL: {
                player.sendMessage(prefix + " 초대를 거절했습니다.");
            }
        }
    }

    private void leaveTeam(Player player) {
        switch (teamService.leaveTeam(player)) {
            case NOT_JOINED_TEAM: {
                player.sendMessage(prefix + " 팀이 없습니다.");
                return;
            }
            case IS_LEADER: {
                player.sendMessage(prefix + " 팀장은 탈퇴할 수 없습니다.");
                return;
            }
            case SUCCESSFUL: {
                player.sendMessage(prefix + " 팀에서 탈퇴하였습니다.");
            }
        }
    }

    private void deleteTeam(Player player) {
        switch (teamService.deleteTeam(player)) {
            case NOT_JOINED_TEAM: {
                player.sendMessage(prefix + " 팀이 없습니다.");
                return;
            }
            case NOT_LEADER: {
                player.sendMessage(prefix + " 권한이 없습니다.");
                return;
            }
            case SUCCESSFUL: {
                player.sendMessage(prefix + " 팀이 제거되었습니다.");
            }
        }
    }
}
