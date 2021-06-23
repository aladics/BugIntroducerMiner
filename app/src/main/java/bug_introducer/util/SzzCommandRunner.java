package bug_introducer.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class SzzCommandRunner {
    private final String RESULT_JSON_NAME = "fix_and_introducers_pairs.json";
    private final String RESULT_DIR = "results";
    private File workingDir;
    private File checkoutDir =  new File("repo");
    private File szzScript;


    public SzzCommandRunner(File workingDir, File szzScript){
        this.workingDir = workingDir;
        this.szzScript = szzScript;
    }

    public SzzCommandRunner(File workingDir, File szzScript, File checkoutDir){
        this.workingDir = workingDir;
        this.szzScript = szzScript;
        this.checkoutDir = checkoutDir;
    }

    public void runCommand(String command) throws IOException, InterruptedException {
        List<String> commandAsList = Arrays.asList(command.split("\\s+"));
        ProcessBuilder pb = new ProcessBuilder(commandAsList)
                            .directory(workingDir)
                            .redirectError(ProcessBuilder.Redirect.DISCARD)
                            .redirectOutput(ProcessBuilder.Redirect.DISCARD);
        Process process = pb.start();
        process.waitFor();
    }

    public void cloneRepo(String repo) throws IOException, InterruptedException {
        String command = String.format("git clone %s %s", repo, this.checkoutDir);
        runCommand(command);
    }

    public String runSZZ(String issuesJson) throws IOException, InterruptedException{
        String command = String.format("java -jar %s -i %s -r %s", this.szzScript.getCanonicalPath(), new File(issuesJson).getCanonicalPath(), this.checkoutDir);
        runCommand(command);
        return Paths.get(this.workingDir.getPath(),RESULT_DIR, RESULT_JSON_NAME).toString();
    }

}
