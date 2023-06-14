package kr.hqservice.team.service;

import kr.hqservice.team.enums.Invite;
import kr.hqservice.team.enums.Result;

import java.util.UUID;
import java.util.function.Consumer;

public interface InviteService {

    boolean isInvited(UUID uuid);

    void invite(UUID uuid, Consumer<Invite> consumer);

    Result accept(UUID uuid);

    Result refuse(UUID uuid);
}
