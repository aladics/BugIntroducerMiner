package bug_introducer.util;

import bug_introducer.model.yaml.Vulnerability;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.*;
import java.util.List;

public class Parser {

    private static final Logger logger = LogManager.getLogger(Parser.class);

    public static Vulnerability parseYaml(String path) {
        Yaml yaml = new Yaml(new Constructor(Vulnerability.class));
        InputStream input;
        Vulnerability vulnerability = null;
        try {
            input = new FileInputStream(path);
            vulnerability = yaml.load(input);
        } catch (FileNotFoundException e) {
            logger.error("Yaml file not found: {}", path, e);
            e.printStackTrace();
        } catch (org.yaml.snakeyaml.error.YAMLException e) {
            logger.error("Couldn't parse YAML file: {}", path, e);
            vulnerability = null;
        }

        return vulnerability;
    }

    public static String parseRepository(String repo_url) throws IOException {
        String repo = "";

        repo_url = repo_url.replaceAll("\\.git", "");
        if(repo_url.contains("https://github.com/")) {
            repo = repo_url.replaceFirst("https://github.com/", "");
        }
        else if(repo_url.contains("git-wip-us.apache.org") || repo_url.contains("git.apache.org")) {
            repo =  "apache";
            repo += repo_url.substring(repo_url.lastIndexOf('/'));
        }
        else {
            throw new IOException(String.format("Unknown repository: {}", repo_url));
        }


        return repo;
    }

    public static List<List<String>> parseResultJson(String jsonPath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<List<String>> results = mapper.readValue(new File(jsonPath), new TypeReference<List<List<String>>>() {});
        return results;
    }
}
