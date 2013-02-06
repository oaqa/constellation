package edu.cmu.lti;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CSDirectoryExtractor {

	/**
	 * Extracts data pertaining to individuals listed in the CS directory.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		// Test the Professor collection code:
		ArrayList<ProfessorObject> profs = getProfessors();
		
		//for(ProfessorObject prof : profs) {
		//	System.out.println(prof.getDescription());
		//}
		
		// Test the Staff collection code:
		//getStaff();
	}

	/**
	 * Extracts the professors from the directory and returns a complete
	 * ArrayList of ProfessorObject types.
	 * 
	 * @return ArrayList of ProfessorObject instances that contain the data for each professor.
	 */
	public static ArrayList<ProfessorObject> getProfessors() {
		String[] baseURLs = { "http://people.cs.cmu.edu/Faculty/",
				"http://people.cs.cmu.edu/Adjunct/" };
		String[] URLExtentions = { "", "B", "C", "D", "E", "F", "G", "H", "I",
				"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
				"V", "W", "X", "Y", "Z" };

		ArrayList<ProfessorObject> totalFaculty = new ArrayList<ProfessorObject>();

		for (String baseURL : baseURLs)
			for (String url : URLExtentions)
				totalFaculty.addAll(extractFaculty(baseURL + url));

		System.out.println("Total Faculty Added: " + totalFaculty.size());
		System.out.println();
		System.out.println("DONE");
		return totalFaculty;
	}

	/**
	 * Takes in a URL and extracts all available information from the directory
	 * into an ArrayList of type ProfessorObject
	 * 
	 * @param url
	 *            The URL of the webpage to extract directory information from
	 * @return an ArrayList of type ProfessorObject that contains all data
	 *         extracted from the page
	 */
	private static ArrayList<ProfessorObject> extractFaculty(String url) {
		ArrayList<ProfessorObject> facultyList = new ArrayList<ProfessorObject>();

		try {
			Document doc = Jsoup.connect(url).get();

			// get staff table
			Elements tables = doc.select("table.faculty");
			Element staffTable = tables.get(0);

			Elements faculty = staffTable.select("tr");
			// System.out.println("Staff found: " + (faculty.size() - 1) +
			// "\n\n");
			// for each professor on the page...
			for (Element prof : faculty) {
				// get the three columns
				Elements columns = prof.select("td");
				if (columns.size() != 3)
					continue; // skip table header

				// Process column number 1
				Element col = columns.get(0);

				String name = col.select("div.name").text();
				System.out.println(name);
				//System.out.println("Name:\t\t" + name);

				String position = col.select("div").get(1).text();
				//System.out.println("Position:\t" + position);

				String department = col.select("div").get(2).text();
				//System.out.println("Department:\t" + department);

				ProfessorObject newProf = new ProfessorObject(name, position,
						department);

				// Process data in column number 2
				col = columns.get(1);

				for (Element div : col.select("div")) {
					String data = div.text();
					if (data.contains(":")) {
						String tag = data.substring(0, data.indexOf(":"))
								.trim();
						String value = data.substring(data.indexOf(":") + 1)
								.trim();
						if (tag.equalsIgnoreCase("Phone")) {
							value = value.replaceAll("-", "");
							if (!value.equals("0000000000")
									&& value.length() == 10) {
								//System.out.println("Phone:\t\t" + value);
								newProf.setPhone(value);
							}
						} else if (tag.equalsIgnoreCase("Fax")) {
							value = value.replaceAll("-", "");
							if (!value.equals("0000000000")
									&& value.length() == 10) {
								//System.out.println("Fax:\t\t" + value);
								newProf.setFax(value);
							}
						} else if (tag.equalsIgnoreCase("Office")) {
							//System.out.println("Office:\t\t" + value);
							newProf.setOffice(value);
						} else if (tag.equalsIgnoreCase("Email")) {
							//System.out.println("Email:\t\t" + value);
							newProf.setEmail(value);
						}
					}
				}
				// grab personal homepage link
				String link = col.select("a[href]").attr("abs:href");
				if (!link.equals("")) {
					//System.out.println("Homepage:\t" + link);
					newProf.setURL(link);
				}

				// Process data in column number 3
				col = columns.get(2);
				String researchInterests = col.select("div").get(0).text()
						.trim();
				if (!researchInterests.equals("")) {
					//System.out.println("Research:\t" + researchInterests);
					newProf.setResearchInterests(researchInterests);
				}

				// extract research interests

				facultyList.add(newProf);

				//System.out.println();
			}

		} catch (IOException e) {
			// bad webpage format
			System.err.println("ERROR LOADING WEBPAGE AT " + url);
		}
		return facultyList;
	}
	
	public static ArrayList<StaffObject> getStaff() {
		String baseURL = "http://people.cs.cmu.edu/Staff/";
		String[] URLExtentions = { "", "B", "C", "D", "E", "F", "G", "H", "I",
				"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
				"V", "W", "X", "Y", "Z" };

		ArrayList<StaffObject> totalStaff = new ArrayList<StaffObject>();

		for (String url : URLExtentions)
			totalStaff.addAll(extractStaff(baseURL + url));

		System.out.println("Total Staff Added: " + totalStaff.size());
		System.out.println();
		System.out.println("DONE");
		return totalStaff;
	}
	
	private static ArrayList<StaffObject> extractStaff(String url) {
		
		ArrayList<StaffObject> totalStaff = new ArrayList<StaffObject>();
		
		try {
			Document doc = Jsoup.connect(url).get();

			// get staff table
			Element staffTable = doc.select("ul#personList").get(0);
			Elements staff = staffTable.select("li");
			for(Element i : staff) {
				String name = i.select("span.name").text().trim();
				String link = i.select("span.name").select("a[href]").attr("abs:href").trim();
				String email = i.select("span.email").text().trim();
				String jobTitle = i.select("span.jobtitle").text().trim();
				String department = i.select("span.department").text().trim();
				System.out.println(name);
				
				// make staffMember object to add to arraylist
				StaffObject staffMember = new StaffObject(name,jobTitle,department);
				staffMember.setEmail(email);
				staffMember.setURL(link);
				
				// attempt to extract additional info from homepage
				try {
					Document doc2 = Jsoup.connect(link).get();
					Element info = doc2.select("div#person").get(0);
					String data = info.select("div.office").get(0).html();
					data = data.substring(data.indexOf("</div>") + 6);
					//System.out.println(data);
					//System.out.println();
					String office = data.substring(0,data.indexOf("<br />")).trim();
					staffMember.setOffice(office);
					//data = data.substring(data.indexOf("<br />") + 6);
					String phone = data.substring(data.indexOf("Phone:") + 6).trim();
					if(phone.contains("Fax:")) {
						String fax = phone.substring(phone.indexOf("Fax:") + 4).replaceAll("-","").trim();
						//System.out.println(fax);
						staffMember.setFax(fax);
						phone = phone.substring(0,phone.indexOf("<br />")).trim();
					}
					phone = phone.replaceAll("-", "").trim();
					staffMember.setPhone(phone);
					//System.out.println(office);
					//System.out.println(phone);
					//System.out.println();
					
				} catch (IOException e) {
					System.err.println("Error in getting extra info from staff page.");
				}
				
				// add staffMember to directory listing
				totalStaff.add(staffMember);
				
			}
			
		} catch (IOException e) {
			System.err.println("Error in staff directory page :(");
		}
		
		return totalStaff;
	}

}
