package kr.hqservice.team.repository;

import kr.hqservice.team.enums.Invite;

import java.util.UUID;

public interface InviteRepository {

    boolean isInvited(UUID uuid);

    Invite get(UUID uuid);

    void set(UUID uuid, Invite invite);

    void remove(UUID uuid);
}
