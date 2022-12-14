package ipl.backend.dashboard.csvprocessing;

import ipl.backend.dashboard.model.Team;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {



    @Autowired
    private final EntityManager em;

    @Autowired
    public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate, EntityManager em) {
        this.em = em;
    }

    @Override
    @Transactional
    public void afterJob(JobExecution jobExecution) {
        if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
            System.out.println("!!! JOB FINISHED! Time to verify the results");
            Map<String, Team> teamNamesToTeamEntityMap = new HashMap<>();
            em.createQuery("select distinct m.team1, count(*) from Match m group by m.team1", Object[].class)
                    .getResultList()
                    .stream()
                    .map(e -> new Team((String) e[0], (long) e[1]))
                    .forEach(team -> teamNamesToTeamEntityMap.put(team.getTeamName(), team));

            em.createQuery("select distinct m.team2, count(*) from Match m group by m.team2", Object[].class)
                    .getResultList()
                    .stream()
                    .forEach(e -> {
                        Team team = teamNamesToTeamEntityMap.get((String)e[0]);
                        team.setTotalMatches(team.getTotalMatches() + (long) e[1]);
                    });

            em.createQuery("select m.winner, count(*) from Match m group by m.winner", Object[].class)
                    .getResultList()
                    .forEach(e ->{
                        Team team = teamNamesToTeamEntityMap.get((String) e[0]);
                        if(team!=null) team.setTotalWins((long) e[1]);
                    });
            teamNamesToTeamEntityMap.values().forEach(team -> em.persist(team));
            teamNamesToTeamEntityMap.values().forEach(team -> System.out.println(team));






        }
    }
}