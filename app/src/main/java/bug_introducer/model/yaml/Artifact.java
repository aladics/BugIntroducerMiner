package bug_introducer.model.yaml;

public class Artifact {
	private String id;
	private String reason;
	private boolean affected;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public boolean isAffected() {
		return affected;
	}
	public void setAffected(boolean affected) {
		this.affected = affected;
	}
}