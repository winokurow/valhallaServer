package dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class User {

	private String id;
	private String name;
	private String email;
	private String created_at;
	private int points;

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public User() {
		super();
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public User(String id, String name, String email, String created_at, int points) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.created_at = created_at;
		this.points = points;
	}

}
