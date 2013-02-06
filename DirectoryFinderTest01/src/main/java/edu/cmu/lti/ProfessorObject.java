package edu.cmu.lti;

public class ProfessorObject {

	// Professors are guaranteed to have a name, position, and department.
	// All other fields are optional.

	private String name;
	private String position;
	private String department;
	private String office;
	private String phone;
	private String fax;
	private String email;
	private String url;
	private String researchInterests;
	private String facebookID;
	private String description;

	/**
	 * Creates a new ProfessorObject with only a name.
	 * 
	 * @param name
	 *            Name of professor
	 */
	public ProfessorObject(String name) {
		this.name = name;
	}

	/**
	 * Constructs a new ProfessorObject with a name, position and department
	 * 
	 * @param name
	 *            Name of professor
	 * @param position
	 *            Position within department. ex: Associate Professor
	 * @param department
	 *            Department the professor primarily belongs to
	 */
	public ProfessorObject(String name, String position, String department) {
		this.name = name;
		this.department = department;
		this.position = position;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the position
	 */
	public String getPosition() {
		return position;
	}

	/**
	 * @param position
	 *            the position to set
	 */
	public void setPosition(String position) {
		this.position = position;
	}

	/**
	 * @return the department
	 */
	public String getDepartment() {
		return department;
	}

	/**
	 * @param department
	 *            the department to set
	 */
	public void setDepartment(String department) {
		this.department = department;
	}

	/**
	 * @return the office
	 */
	public String getOffice() {
		return office;
	}

	/**
	 * @param office
	 *            the office to set
	 */
	public void setOffice(String office) {
		this.office = office;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone
	 *            the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the fax
	 */
	public String getFax() {
		return fax;
	}

	/**
	 * @param fax
	 *            the fax to set
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the homepage
	 */
	public String getURL() {
		return url;
	}

	/**
	 * @param homepage
	 *            the homepage to set
	 */
	public void setURL(String url) {
		this.url = url;
	}

	/**
	 * @return the researchInterests
	 */
	public String getResearchInterests() {
		return researchInterests;
	}

	/**
	 * @param researchInterests
	 *            the researchInterests to set
	 */
	public void setResearchInterests(String researchInterests) {
		this.researchInterests = researchInterests;
	}

	/**
	 * 
	 * @return the facebook ID
	 */
	public String getFacebookID() {
		return facebookID;
	}

	/**
	 * @param facebookID
	 *            Facebook ID to set
	 */
	public void setFacebookID(String facebookID) {
		this.facebookID = facebookID;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		if (this.description != null)
			return description;
		String desc = this.getName() + " is a " + this.getPosition()
				+ " in the " + this.getDepartment() + " who is interested in "
				+ this.getResearchInterests();

		return desc;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}
