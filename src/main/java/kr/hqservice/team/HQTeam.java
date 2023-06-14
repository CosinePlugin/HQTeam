package kr.hqservice.team;

import kr.hqservice.team.command.TeamCommand;
import kr.hqservice.team.listener.TeamListener;
import kr.hqservice.team.repository.InviteRepository;
import kr.hqservice.team.repository.TeamRepository;
import kr.hqservice.team.repository.impl.InviteRepositoryImpl;
import kr.hqservice.team.repository.impl.TeamRepositoryImpl;
import kr.hqservice.team.service.InviteService;
import kr.hqservice.team.service.TeamService;
import kr.hqservice.team.service.impl.InviteServiceImpl;
import kr.hqservice.team.service.impl.TeamServiceImpl;
import org.bukkit.plugin.java.JavaPlugin;

public class HQTeam extends JavaPlugin {

    private static HQTeam plugin;

    public static String prefix = "§b[ 팀 ]§f";

    @Override
    public void onLoad() {
        plugin = this;
    }

    @Override
    public void onEnable() {
        InviteRepository inviteRepository = new InviteRepositoryImpl();
        TeamRepository teamRepository = new TeamRepositoryImpl();

        InviteService inviteService = new InviteServiceImpl(inviteRepository);
        TeamService teamService = new TeamServiceImpl(inviteService, teamRepository);

        getServer().getPluginManager().registerEvents(new TeamListener(teamService), this);

        getCommand("팀").setExecutor(new TeamCommand(teamService));
    }

    @Override
    public void onDisable() {}

    public static HQTeam getInstance() { return plugin; }
}
