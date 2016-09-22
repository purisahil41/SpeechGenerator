package assignment1_NaturalSpeech;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.omg.IOP.Encoding;

public class Graph {
	List<Vertice> Vertices = new ArrayList<Vertice>();
	List<Edge> Edges = new ArrayList<Edge>();

	public final Charset ENCODING = StandardCharsets.UTF_8;

	public Graph(String inputFilePath) {
		try {
			// Read Input.txt file
			List<String> inputFile = readInput(inputFilePath);

			// Parse input.txt file
			for (String str : inputFile) {
				String[] arrSplits = new String[3];
				arrSplits = str.split("//", 3);

				// Parse for Vertices, Edges
				Vertice FirstVertice = new Vertice(arrSplits[0]);
				Vertice SecondVertice = new Vertice(arrSplits[1]);
				Edge edge = new Edge(FirstVertice, SecondVertice, arrSplits[2]);

				// Check If Vertice and Edge to be added to graph
				if (!AlreadyHas(this.Vertices, FirstVertice)) {
					this.Vertices.add(FirstVertice);
				}
				if (!AlreadyHas(this.Vertices, SecondVertice)) {
					this.Vertices.add(SecondVertice);
				}
				if (!AlreadyHas(this.Edges, edge)) {
					this.Edges.add(edge);
				}
			}
		} catch (Exception ex) {
			System.out
					.println("Exception : " + ex.getMessage() + ex.toString());
		}

	}

	public boolean AlreadyHas(List<Vertice> Vertices, Vertice CurrentVertice) {
		for (Object ver : Vertices) {
			if (ver.equals(CurrentVertice)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean AlreadyHas(List<Edge> Edges, Edge CurrentEdge) {
		for (Object ver : Edges) {
			if (ver.equals(CurrentEdge)) {
				return true;
			}
		}
		return false;
	}

	List<String> readInput(String aFileName) throws IOException {
		List<String> inputFile = new ArrayList<String>();
		Path path = Paths.get(aFileName);
		try (BufferedReader reader = Files.newBufferedReader(path, ENCODING)) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				// process each line in some way
				inputFile.add(line);
			}
		}
		return inputFile;
	}
}
