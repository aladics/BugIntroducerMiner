package bug_introducer.model;

public class BugFixCommit {
    String hash;
    String creationdate;
    String resolutiondate;
    String commitdate;

    public BugFixCommit(String hash, String commitdate) {
        this.hash = hash;
        this.creationdate = commitdate;
        this.resolutiondate = commitdate;
        this.commitdate = commitdate;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getCreationdate() {
        return creationdate;
    }

    public void setCreationdate(String creationdate) {
        this.creationdate = creationdate;
    }

    public String getResolutiondate() {
        return resolutiondate;
    }

    public void setResolutiondate(String resolutiondate) {
        this.resolutiondate = resolutiondate;
    }

    public String getCommitdate() {
        return commitdate;
    }

    public void setCommitdate(String commitdate) {
        this.commitdate = commitdate;
    }
}