package ipl.backend.dashboard.csvprocessing;


import ipl.backend.dashboard.model.Match;
import org.springframework.batch.item.ItemProcessor;

public class MatchProcessor implements ItemProcessor<MatchInput, Match> {

    @Override
    public Match process(final MatchInput matchInput) throws Exception {
        Match match = new Match();
        MatchMapper.map(matchInput, match);

        return match;
    }
}
