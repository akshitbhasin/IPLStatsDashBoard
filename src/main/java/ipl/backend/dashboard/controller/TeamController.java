package ipl.backend.dashboard.controller;

import ipl.backend.dashboard.model.Match;
import ipl.backend.dashboard.model.Team;
import ipl.backend.dashboard.repository.MatchRepository;
import ipl.backend.dashboard.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class TeamController {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private MatchRepository matchRepository;

    @GetMapping("/team")
    public Iterable<Team> getAllTeam() {
        return this.teamRepository.findAll();
    }
    @GetMapping("/team/{teamName}")
    public Team getTeam(@PathVariable String teamName){
        Team team = teamRepository.findByTeamName(teamName);
        List<Match> matches = matchRepository.getMatches(teamName);
        team.setMatches(matches);
        return team;
    }

    @GetMapping("/team/{teamName}/matches")
    public List<Match> getMatchesByYear(@PathVariable String teamName, @RequestParam int year){
        List<Match> matches = matchRepository.getMatchesByTeamByYear(teamName, year);
        return matches;
    }

}
