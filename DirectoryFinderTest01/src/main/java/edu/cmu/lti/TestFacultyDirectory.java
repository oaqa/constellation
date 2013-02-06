package edu.cmu.lti;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class TestFacultyDirectory {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String url = "http://www.cmu.edu/academics/schools.shtml"; // seed page for all departments and schools
		
		String department = "English";
		int crawlDepth = 3;
		
		System.out.println("Loading seed page...");
		try {
			Document doc = Jsoup.connect(url).get();
			Elements links = doc.select("a[href]");
			//System.out.println("\nLinks: " + links.size());
			
			for (Element link : links) {
				// is this our department?
				if(link.text().equalsIgnoreCase(department)) {
					String newURL = link.attr("abs:href");
					ArrayList<String> urls = findDirectories(newURL, crawlDepth);
					System.out.println("Department(supplied): " + department);
					System.out.println("Found the following department faculty directories:\n");
					for(String dir:urls) {
						System.out.println(dir);
						System.out.println();
					}
					if(urls.size() == 0)
						System.out.println("No Directories Found!");
				}
			}
		
		} catch (IOException e) {
			System.err.println("ERROR PROCESSING SEED PAGE!");
			e.printStackTrace();
		}
		
		
		System.out.println("\nDONE!");
		
	}
	
	public static ArrayList<String> findDirectories(String url, int crawlDepth) {
		crawlDepth--;
		ArrayList<String> directories = new ArrayList<String>();
		if(crawlDepth<=0) return directories;
		
		String[] terms = { "directory", "faculty", "people" };
		// ArrayList<String> directories = crawl(url, crawlDepth,terms);
		Document doc;
		try {
			doc = Jsoup.connect(url).get();
			//System.out.println(doc.text());
			//Elements tags = doc.select("ul");
			Elements links = doc.select("a[href]");
			//System.out.println("\nLinks: " + links.size());
			for (Element link : links) {
				// for each link
				for (String term : terms) {
					// for eack keyterm
					String linkURL = link.attr("abs:href");
					if (link.text().toLowerCase().contains(term) || linkURL.contains(term)) {
						// for each link matching a keyterm
						// investigate link.
						String newURL = link.attr("abs:href");
						System.out.println("Investigating Link: " + link.text() + "  \t"
								+ newURL);
						// is it a directory?
						int conf = checkDirectory(newURL);
						System.out.println("Confidence: " + conf);
						if(conf >= 80) { // it is a directory
							directories.add(newURL);
						} else {
							//crawl that location and grab the directories.
							ArrayList<String> dirs = findDirectories(newURL,crawlDepth);
							for(String dir : dirs)
								if(!directories.contains(dir))
									directories.add(dir);
							//directories.addAll(findDirectories(newURL,crawlDepth));
						}
						
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return directories;
		
	}
	
	
	
	
	
	public static int checkDirectory(String url) {
		// gives a confidence measure as to how likely it is that the supplied url is a faculty directory page
		Document doc;
		int confidence = 0;
		try {
			System.out.println("Fetching " + url + "...");
			doc = Jsoup.connect(url).get();
			String text = doc.text();
			//System.out.println("Length: " + text.length());
			int length = text.length();
			int emails = count(text,"@",false) + count(text, "email",false) + count(text,"e-mail",false);
			int faxes = count(text,"fax",false);
			int research = count(text,"research",false);
			int publications = count(text,"publications",false);
			int offices = count(text,"office",false);
			int education = count(text,"education",false);
			int professors = count(text,"Professor",false);
			int phones = count(text, "Phone", false);
			
			//System.out.println("Email Addresses: " + emails);
			//System.out.println("Professors: " + professors);
			//System.out.println("Phones: " + phones);
			//System.out.println("Offices: " + offices);
			int pageConfidence = (professors + emails + phones + education + offices + publications + research + faxes);
			int linkConfidence = 0;
			
			Elements links = doc.select("a[href]");
			//System.out.println("\nLinks: " + links.size());
			int goodLinks = 0;
			for (Element link : links) {
				// test if link is a faculty memeber's page
				String newURL = link.attr("abs:href");
				if(!newURL.contains("cmu.edu") || newURL.contains("#"))
					continue; // skip this url.
				goodLinks ++;
				int facultyPageConfidence = checkFacultyPage(newURL);
				//System.out.println("Confidence for Faculty Page: " + facultyPageConfidence);
				linkConfidence += facultyPageConfidence;
			}
			//System.out.println("Total Page Confidence: " + pageConfidence);
			//System.out.println("Total Link Confidence: " + linkConfidence);
			//System.out.println("Good Links: " + goodLinks);
			confidence = pageConfidence + (linkConfidence)/((goodLinks/10)+1);
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return confidence;
		
	}
	
	public static int checkFacultyPage(String url) {
		// gives a confidence measure as to how likely it is that the supplied url is a faculty page
				Document doc;
				int confidence = 0;
				if(!url.contains("cmu.edu")) {
					System.out.println("Skipping " + url);
					return 0;
				}
				try {
					System.out.println("Fetching " + url + "...");
					doc = Jsoup.connect(url).get();
					String text = doc.text();
					//System.out.println("Length: " + text.length());
					int length = text.length();
					
					// Positive Indicators
					
					int emails = count(text,"@",false) + count(text, "email",false) + count(text,"e-mail",false);
					if(emails != 0)
						confidence += 3/emails;
					int professors = count(text,"Professor",false);
					if(professors != 0)
						confidence += 5/professors;
					int phones = count(text, "Phone", false);
					int faxes = count(text,"fax",false);
					if(phones + faxes != 0) 
						confidence += 3/(phones + faxes);
					int research = count(text,"research",false);
					int publications = count(text,"publications",false);
					if(research + publications != 0)
						confidence += 6/(research + publications);
					int offices = count(text,"office",false);
					if(offices != 0)
						confidence += 3/offices;
					int education = count(text,"education",false);
					if(education != 0)
						confidence += 3/education;
					
					
					// Negative indicators
					
					confidence -= phones/2;
					confidence -= faxes/2;
					confidence -= offices/2;
					confidence -= emails/2;
					confidence -= research/4;
					confidence -= education/4;
					confidence -= professors/5;
					
					
					/*System.out.print("Emails: " + emails);
					System.out.print("\tProfs: " + professors);
					System.out.println("\tPhones: " + phones);
					System.out.println("\tOffices: " + offices);
					System.out.println("\tresearch: " + research);
					System.out.println("\teducation: " + education);
					*/
					// analysis complete.
				} catch (IOException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (OutOfMemoryError e) {
					e.printStackTrace();
				}
				if(confidence > 0)
					return confidence;
				else return 0;
				
	}
	
	public static int count(String str, String sub, boolean caseSensitive) {
		// counts the occurances of substring sub within str.
		if(!caseSensitive) {
			str = str.toLowerCase();
			sub = sub.toLowerCase();
		}
		
		int x = str.indexOf(sub);
		int count = 1;
		while(x>=0) {
			x = str.indexOf(sub,x+1);
			count++;
		}
		return --count;
	}

}
