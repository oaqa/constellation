package edu.cmu.lti;

public class StaffObject {

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
	private String description;

	/**
	 * Creates a new StaffObject with only a name.
	 * 
	 * @param name
	 *            Name of professor
	 */
	public StaffObject(String name) {
		this.name = name;
	}

	/**
	 * Constructs a new StaffObject with a name, position and department
	 * 
	 * @param name
	 *            Name of staff member
	 * @param position
	 *            Position within department
	 * @param department
	 *            Department the professor primarily belongs to
	 */
	public StaffObject(String name, String position, String department) {
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
	 * @return the url
	 */
	public String getURL() {
		return url;
	}

	/**
	 * @param homepage
	 *            the url to set
	 */
	public void setURL(String url) {
		this.url = url;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

}
