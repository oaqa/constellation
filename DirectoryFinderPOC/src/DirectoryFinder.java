import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class DirectoryFinder {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String url = "http://www.cmu.edu";
        System.out.print("Fetching " + url + "...");

        Document doc = Jsoup.connect(url).get();
		Elements links = doc.select("a[href]");
		print("\nLinks: (%d)", links.size());
        for (Element link : links) {
            print(" * a: <%s>  (%s)", link.attr("abs:href"), trim(link.text(), 35));
        }
	}

}
