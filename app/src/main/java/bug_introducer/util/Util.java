package bug_introducer.util;

import bug_introducer.model.BugFixCommit;
import bug_introducer.model.VulnerabilityResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Util {
    static Logger logger = LogManager.getLogger(Util.class);
    public static String TMP_DIR = "tmp";

    public static List<Path> listYamlFiles(Path rootDir) throws IOException {
        List<Path> result;
        try (Stream<Path> walk = Files.walk(rootDir)) {
            result = walk.filter(file -> file.getFileName().toString().endsWith(".yaml"))
                    .collect(Collectors.toList());
        }
        return result;
    }

    public static void writeToJson(Map<String, BugFixCommit> bugFixes, String resultPath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File(resultPath), bugFixes);
    }

    public static String getAsYaml(VulnerabilityResult vulnerabilityResult, String cve){
        Representer representer = new Representer();
        representer.addClassTag(VulnerabilityResult.class, Tag.MAP);
        Yaml yaml = new Yaml(representer);
        Map<String, VulnerabilityResult> vulnerabilityResults = new HashMap<>();
        vulnerabilityResults.put(cve, vulnerabilityResult);
        String yamlString = yaml.dumpAs(vulnerabilityResults, Tag.MAP, null);
        return yamlString;
    }



    private static void deleteDir(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                if (! Files.isSymbolicLink(f.toPath())) {
                    deleteDir(f);
                }
            }
        }
        file.delete();
    }

    public static void deleteTmp() throws IOException, InterruptedException {
        Path tmpPath = Paths.get(TMP_DIR);
        if(!Files.exists(tmpPath)) return;

        deleteDir(tmpPath.toFile());
    }

    public static void resetTmp() throws IOException, InterruptedException {
        deleteTmp();
        Files.createDirectories(Paths.get(TMP_DIR));
    }


    public static void appendToResults(String yamlString) throws IOException {
        Files.write(Paths.get("results.yaml"), yamlString.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }
}
