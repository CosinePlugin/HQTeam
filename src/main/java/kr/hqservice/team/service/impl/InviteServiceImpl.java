package kr.hqservice.team.service.impl;

import kr.hqservice.team.HQTeam;
import kr.hqservice.team.enums.Invite;
import kr.hqservice.team.enums.Result;
import kr.hqservice.team.repository.InviteRepository;
import kr.hqservice.team.service.InviteService;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;
import java.util.function.Consumer;

public class InviteServiceImpl implements InviteService {

    private final InviteRepository inviteRepository;

    public InviteServiceImpl(InviteRepository inviteRepository) {
        this.inviteRepository = inviteRepository;
    }

    @Override
    public boolean isInvited(UUID uuid) {
        return inviteRepository.isInvited(uuid);
    }

    @Override
    public void invite(UUID uuid, Consumer<Invite> consumer) {
        inviteRepository.set(uuid, Invite.NONE);
        new BukkitRunnable() {
            int time = 30;

            @Override
            public void run() {
                time--;
                Invite invite = inviteRepository.get(uuid);
                if (invite == Invite.ACCEPT || invite == Invite.REFUSE) {
                    cancel();
                    consumer.accept(invite);
                    inviteRepository.remove(uuid);
                    return;
                }
                if (time == 0) {
                    cancel();
                    consumer.accept(Invite.TIME_OUT);
                    inviteRepository.remove(uuid);
                }
            }
        }.runTaskTimerAsynchronously(HQTeam.getInstance(), 0, 20);
    }

    @Override
    public Result accept(UUID uuid) {
        if (!inviteRepository.isInvited(uuid)) {
            return Result.NOT_INVITED;
        }
        inviteRepository.set(uuid, Invite.ACCEPT);
        return Result.SUCCESSFUL;
    }

    @Override
    public Result refuse(UUID uuid) {
        if (!inviteRepository.isInvited(uuid)) {
            return Result.NOT_INVITED;
        }
        inviteRepository.set(uuid, Invite.REFUSE);
        return Result.SUCCESSFUL;
    }
}
