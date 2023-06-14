package kr.hqservice.team.repository.impl;

import kr.hqservice.team.enums.Invite;
import kr.hqservice.team.repository.InviteRepository;

import java.util.*;

public class InviteRepositoryImpl implements InviteRepository {

    private final Map<UUID, Invite> invites = new HashMap<>();

    @Override
    public boolean isInvited(UUID uuid) {
        return invites.containsKey(uuid);
    }

    @Override
    public Invite get(UUID uuid) {
        return invites.get(uuid);
    }

    @Override
    public void set(UUID uuid, Invite invite) {
        invites.put(uuid, invite);
    }

    @Override
    public void remove(UUID uuid) {
        invites.remove(uuid);
    }
}
