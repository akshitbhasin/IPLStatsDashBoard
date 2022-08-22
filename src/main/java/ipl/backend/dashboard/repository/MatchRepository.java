package ipl.backend.dashboard.repository;

import ipl.backend.dashboard.model.Match;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;


import java.time.LocalDate;
import java.util.List;

public interface MatchRepository extends CrudRepository<Match, Long> {

    default List<Match> getMatches(String teamName){
        Pageable pageable = PageRequest.of(0,4);
        return getByTeam1OrTeam2OrderByDateDesc(teamName,teamName, pageable) ;
    }
    default List<Match> getMatchesByTeamByYear(String teamName, int year){
        LocalDate startDate = LocalDate.of(year,1,1);
        LocalDate endDate = LocalDate.of(year + 1,1,1);
        return getByTeam1AndDateBetweenOrTeam2AndDateBetweenOrderByDateDesc(teamName, startDate, endDate, teamName,startDate,endDate);
    }

//    select m from Match m where (m.team1 = :teamName or m.team2 = :teamName) and m.date between :startDate and :endDate order by date desc
    List<Match> getByTeam1AndDateBetweenOrTeam2AndDateBetweenOrderByDateDesc(String teamName1,  LocalDate date1, LocalDate date2, String teamName2, LocalDate date3, LocalDate date4);
    List<Match> getByTeam1OrTeam2OrderByDateDesc(String teamName1, String teamName2, Pageable pageable);



}
