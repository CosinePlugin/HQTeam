package kr.hqservice.team.listener;

import kr.hqservice.team.service.TeamService;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class TeamListener implements Listener {

    private final TeamService teamService;

    public TeamListener(TeamService teamService) {
        this.teamService = teamService;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        Entity attacker = event.getDamager();
        Entity victim = event.getEntity();
        if (attacker instanceof Player && victim instanceof Player) {
            teamService.preventTeamDamage(event, (Player) attacker, (Player) victim);
        }
    }
}
