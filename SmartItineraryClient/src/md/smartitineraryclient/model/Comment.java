package md.smartitineraryclient.model;

import java.util.Date;

public class Comment {
	private String comment;
	private Date date;
	
	public Comment(String comment, Date date) {
		this.comment = comment;
		this.date = date;
	}

	public String getComment() {
		return comment;
	}

	public Date getDate() {
		return date;
	}
}
