package bug_introducer;

import bug_introducer.model.BugFixCommit;
import bug_introducer.model.yaml.Commit;
import bug_introducer.model.yaml.Vulnerability;
import bug_introducer.util.Parser;
import bug_introducer.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import picocli.CommandLine;

import java.io.IOException;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.*;

public class Miner {
    static Logger logger = LogManager.getLogger(Miner.class);

    static private GHCommit getGHCommit(Commit commit) throws IOException {
        GitHub github = GitHubBuilder.fromPropertyFile().build();
        String repo = Parser.parseRepository(commit.getRepository());
        GHCommit ghCommit;
        try {
            ghCommit = github.getRepository(repo).getCommit(commit.getId());
        }
        catch (org.kohsuke.github.HttpException e) {
            logger.warn("Commit '{}' not found in repository '{}'", commit.getId(), repo);
            throw e;
        }

        return ghCommit;
    }

    static private GHCommit getOldestCommit(List<Commit> commits) throws IOException {
        GHCommit oldestCommit = null;
        for(Commit commit : commits){
            GHCommit currentGHCommit = getGHCommit(commit);

            if(oldestCommit == null || currentGHCommit.getCommitDate().before(oldestCommit.getCommitDate())){
                oldestCommit = currentGHCommit;
            }
        }

        return oldestCommit;
    }

    static Map<String, BugFixCommit> getBugfixes(List<Commit> commits, String cveId) throws  IOException{
        Map<String, BugFixCommit> bugFixes = new HashMap<>();

        GHCommit ghCommit = getOldestCommit(commits);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
        BugFixCommit bugFix = new BugFixCommit(ghCommit.getSHA1(), dateFormat.format(ghCommit.getCommitDate()));

        bugFixes.put(cveId, bugFix);
        return bugFixes;
    }

    public static Vulnerability processYaml(Path yamlFile, String resultPath)  {
        Vulnerability vulnerability = Parser.parseYaml(yamlFile.toAbsolutePath().toString());

        if(vulnerability.getFixes() == null){
            logger.warn("Skipping vulnerability '{}': no fixes. ", vulnerability.getVulnerability_id());
            return null;
        }

        List<Commit> commits = vulnerability.getFixes().get(0).getCommits();
        try {
            Map<String, BugFixCommit> bugFixes = getBugfixes(commits, vulnerability.getVulnerability_id());
            Util.writeToJson(bugFixes, resultPath);
        }
        catch (IOException e){
            logger.warn("Skipping vulnerability '{}': IOException.", vulnerability.getVulnerability_id(), e);
            return null;
        }

        return vulnerability;
    }

    public static Map<String, Set<String>> getIntroducingHashes(List<List<String>> commitIntroducerPairs){
        Map<String, Set<String>> introducerFixerPairs = new HashMap<>();
        for(List<String> commitIntroducerPair : commitIntroducerPairs){
            String fixingSha = commitIntroducerPair.get(0);
            String introducingSha = commitIntroducerPair.get(1);

            if(!introducerFixerPairs.containsKey(fixingSha)){
                introducerFixerPairs.put(fixingSha, new HashSet<>());
            }

            introducerFixerPairs.get(fixingSha).add(introducingSha);
        }

        return introducerFixerPairs;
    }
}
