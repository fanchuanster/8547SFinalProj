package assignment4;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;


public class Task3 {
	public static final String INPUT_PAGES_DIR = "resources\\W3C Web Pages\\Pages\\";
	public static final String output = "output/";
	
    private static class ExtractTextNodeVisitor implements NodeVisitor {
        private StringBuilder nodeText = new StringBuilder(); // holds the text in the node.

        // hit when the node is first seen
        public void head(Node node, int depth) {
            String name = node.nodeName();
            if (node instanceof TextNode)
                append(((TextNode) node).text()); 
            else if (name.equals("li"))
                append("\n * ");
            else if (name.equals("dt"))
                append("  ");
            else if (StringUtil.in(name, "p", "h1", "h2", "h3", "h4", "h5", "tr"))
                append("\n");
        }

        public void tail(Node node, int depth) {
            String name = node.nodeName();
            if (StringUtil.in(name, "br", "dd", "dt", "p", "h1", "h2", "h3", "h4", "h5"))
                append("\n");
        }

        private void append(String text) {
        	assert text != null : "appending a null text is not allowed.";
        	nodeText.append(text);
        }

        @Override
        public String toString() {
            return nodeText.toString();
        }
    }
	
	static void HTMLtoText(File file, String outTextFile) {
		Document doc = null;
		try {
			doc = Jsoup.parse(file, "UTF-8");			
		} catch (IOException e) {
			e.printStackTrace();
		}

		ExtractTextNodeVisitor formatter = new ExtractTextNodeVisitor();
        NodeTraversor.traverse(formatter, doc);
        
		try {
			FileWriter fw = new FileWriter(outTextFile);
			fw.write(formatter.toString());
	        fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
		
	public static void main(String[] args) {
		
		File directory = new File(output);
	    if (! directory.exists()){
	        directory.mkdir();
	    }
		
		directory = new File(INPUT_PAGES_DIR);
		File[] contents = directory.listFiles();
		for ( File f : contents) {
			String outputFile = output + f.getName().replaceAll(" ", "") + ".txt";
			System.out.println("HTML converting " + f.getName() + "\t==>\t" + outputFile);
			HTMLtoText(f, outputFile);
		}
	}
}
