package bug_introducer.model.yaml;

import java.util.List;

public class Fix {
	private String id;
	private List<Commit> commits;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public List<Commit> getCommits() {
		return commits;
	}
	public void setCommits(List<Commit> commits) {
		this.commits = commits;
	}
	
	
}
