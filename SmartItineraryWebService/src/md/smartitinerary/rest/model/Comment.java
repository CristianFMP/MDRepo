package md.smartitinerary.rest.model;

import java.sql.Timestamp;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class Comment {
	private String comment;
	private Timestamp timestamp;
	
	public Comment(String comment, Timestamp timestamp) {
		this.comment = comment;
		this.timestamp = timestamp;
	}
	
	public Comment() {
		this.comment = "";
		this.timestamp = null;
	}

	public String getComment() {
		return comment;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}
}
