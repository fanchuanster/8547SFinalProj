package finalproj;


public class SearchResult implements Comparable<SearchResult> {
	String keyword;
	int occurrences;
	String pageUrl;
	
	public SearchResult(String keyword, int occurrences, String url) {
		this.keyword = keyword;
		this.occurrences = occurrences;
		this.pageUrl = url;
	}

	@Override
	public int compareTo(SearchResult o) {
		return Integer.compare(this.occurrences, o.occurrences);
	}
}
