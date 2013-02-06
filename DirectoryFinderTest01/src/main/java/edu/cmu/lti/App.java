package edu.cmu.lti;

import java.io.IOException;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * All the files in this project are pretty much just bad attempts to identify the 
 * faculty directory of a university and extract the directory information.
 * None work particularly well, but some provide satisfactory results.
 * 
 */
public class App {
	public static void main(String[] args) {
		int crawlDepth = 5;
		String url = "http://www.cmu.edu";
		//String url = "http://www.brown.edu/";
		System.out.print("Fetching " + url + "...");
		String[] terms = { "directory", "faculty", "people",
				"departments", "school", "college", "institute", "schools",
				"colleges", "academics", "education" };
		ArrayList<String> directories = crawl(url, crawlDepth, terms);

		System.out.println("DONE");
		for (String directory : directories) {
			System.out.println("Directory: " + directory);
		}
		System.out.println(directories.size() + " directory URLs found.");
	}

	private static void print(String msg, Object... args) {
		System.out.println(String.format(msg, args));
	}

	private static String trim(String s, int width) {
		if (s.length() > width)
			return s.substring(0, width - 1) + ".";
		else
			return s;
	}

	private static ArrayList<String> crawl(String url, int maxDepth,
			String[] termList) {
		// recursive function for looking for a URL
		return crawl(url, 0, maxDepth, termList, new ArrayList<String>());
	}

	private static ArrayList<String> crawl(String url, int depth, int maxDepth,
			String[] termList, ArrayList<String> visitedURLs) {
		// recursive function for looking through a url path to find a directory

		Document doc;
		ArrayList<String> directories = new ArrayList<String>();
		if (depth == maxDepth)
			return directories;
		try {
			if (!url.contains("http")) {
				System.err.println("error:wrong url format");
				return null;
			}
			doc = Jsoup.connect(url).get();

			Elements links = doc.select("a[href]");
			// System.out.println("\nLinks: " + links.size());
			for (Element link : links) {
				for (String term : termList)
					if (link.text().toLowerCase().contains(term)) {
						System.out.println("exploring " + link.text());

						// create new URL to explore
						String newURL = link.attr("abs:href");

						// if the link has "directory" in its name, put it in a
						// list
						if (link.text().toLowerCase().contains("directory")
								&& !directories.contains(newURL))
							directories.add(newURL);

						if (!visitedURLs.contains(newURL)) {
							// add url to list of visited urls
							visitedURLs.add(link.attr("abs:href"));
							ArrayList<String> x = crawl(newURL, depth + 1,
									maxDepth, termList, visitedURLs);
							if (x != null)
								// directories.addAll(x);

								for (String dir : x) {
									if (!directories.contains(dir))
										directories.add(dir);
									// directories.addAll(x);

								}

						}

					}
				// print(" * a: <%s>  (%s)", link.attr("abs:href"),
				// trim(link.text(), 35));
			}
			return directories;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return directories;
		} catch (java.nio.charset.IllegalCharsetNameException e) {
			e.printStackTrace();
			return directories;
		}
	}
}
