package kr.hqservice.team.repository;

import kr.hqservice.team.data.Team;

import java.util.List;
import java.util.UUID;

public interface TeamRepository {

    boolean contains(String teamName);

    List<String> getTeamKey();

    List<Team> getTeamValue();

    Team getTeam(UUID uuid);

    Team getTeam(String teamName);

    void setTeam(String teamName, Team team);

    void removeTeam(String teamName);
}
