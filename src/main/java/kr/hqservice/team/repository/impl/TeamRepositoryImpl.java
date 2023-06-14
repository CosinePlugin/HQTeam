package kr.hqservice.team.repository.impl;

import kr.hqservice.team.data.Team;
import kr.hqservice.team.repository.TeamRepository;

import java.util.*;

public class TeamRepositoryImpl implements TeamRepository {

    private final Map<String, Team> teams = new HashMap<>();

    @Override
    public boolean contains(String teamName) {
        return teams.containsKey(teamName);
    }

    @Override
    public List<String> getTeamKey() {
        return new ArrayList<>(teams.keySet());
    }

    @Override
    public List<Team> getTeamValue() {
        return new ArrayList<>(teams.values());
    }

    @Override
    public Team getTeam(UUID uuid) {
        return teams.values().stream()
                .filter(guild -> guild.contains(uuid))
                .findFirst()
                .orElse(null);
    }

    @Override
    public Team getTeam(String teamName) {
        return teams.get(teamName);
    }

    @Override
    public void setTeam(String teamName, Team team) {
        teams.put(teamName, team);
    }

    @Override
    public void removeTeam(String teamName) {
        teams.remove(teamName);
    }
}
