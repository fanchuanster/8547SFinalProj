package finalproj;

import textprocessing.StdOut;
import textprocessing.TST;
import textprocessing.TrieST;

public class App {

	public static void main(String[] args) {
		String[] keys = {"she","sells","sea","shells","by","the","sea","shore"}; 
    	
    	// build symbol table from standard input
        TrieST<Integer> st = new TrieST<Integer>();
        for (int i = 0; i < keys.length; i++) {
            //String key = In.readString();
            st.put(keys[i], i);
        }

        // print results
        if (st.size() < 100) {
            StdOut.println("keys(\"\"):");
            for (String key : st.keys()) {
                StdOut.println(key + " " + st.get(key));
            }
            StdOut.println();
        }

        StdOut.println("longestPrefixOf(\"shellsort\"):");
        StdOut.println(st.longestPrefixOf("shellsort"));
        StdOut.println();

        StdOut.println("keysWithPrefix(\"shor\"):");
        for (String s : st.keysWithPrefix("shor"))
            StdOut.println(s);
        StdOut.println();

        StdOut.println("keysThatMatch(\"sea\"):");
        for (String s : st.keysThatMatch("sea"))
            StdOut.println(s);

	}

}
