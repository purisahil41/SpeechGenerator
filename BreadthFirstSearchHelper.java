/*
 Helper Class the performs the BFS search over the input and returns the list of all the possible sentences as per the correct grammer.
*/

package assignment1_NaturalSpeech;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class BreadthFirstSearchHelper {
	Graph inputGraph;
	String[] permittedSentenceSpec;
	String startWord;
	String startSpeechType;
	Vertice startVertice = null;
	
	/*Queue used for storing the BFS search results*/
	public static Queue<SequenceOfEdges> listOfNodes = new ArrayDeque<SequenceOfEdges>();
	
	/* List that stores all the sentences formed as per the correct grammer */ 
	List<SequenceOfEdges> allSequences = new ArrayList<SequenceOfEdges>();
	
	SequenceOfEdges duplicateSequence = null;
	int noOfNodesCompared = 1;

	/*
	 * Helper Class Constructor to initialize data regarding search Input 
	 * inputGraph: Graph representation of input.txt 
	 * permittedSentenceSpec: Sentence structure given in the problem
	 * startWord: Starting word for the search to work on
	 * startSpeechType: POS of the starting word
	 */
	 
	public BreadthFirstSearchHelper(Graph inputGraph,
			String[] permittedSentenceSpec, String startWord,
			String startSpeechType) {
		this.inputGraph = inputGraph;
		this.permittedSentenceSpec = permittedSentenceSpec;
		this.startWord = startWord;
		this.startSpeechType = startSpeechType;

		for (Vertice ver : this.inputGraph.Vertices) {
			if (ver.Name.equals(this.startWord)
					&& ver.SpeechType.equals(this.startSpeechType)) {
				this.startVertice = ver;
				break;
			}
		}
		List<Edge> edgeNew = new ArrayList<Edge>();
		edgeNew.add(new Edge(null, this.startVertice, "1.0"));
		SequenceOfEdges seq = new SequenceOfEdges(edgeNew);
		listOfNodes.add(seq);
	}

	/*
	 * Implementation of Breadth First Search Input
	 * Sequence: Sequence of edges whose last vertex in the last edge is to be processed. 
	 * Returns: The list of sequences that adhere to the sentence structure mentioned in the problem
	 */
	public List<SequenceOfEdges> performBFS(SequenceOfEdges Sequence) {

		duplicateSequence = duplicateSequence(Sequence);

		if (!(isValidSentence(Sequence))) {

			// Get Edges starting from the source node
			List<Edge> verticeEdges = getEdgesFromNode(Sequence);

			// Parse each edge
			for (int i = 0; i < verticeEdges.size(); i++) {

				// Check the ending vertex for validity
				if (mayFormValidSentence(duplicateSequence,
						verticeEdges.get(i).secondVertice)) {

					SequenceOfEdges duplicateSequenceLoop = duplicateSequence(duplicateSequence);
					duplicateSequenceLoop.edgeList.add(verticeEdges.get(i));
					noOfNodesCompared++;
					listOfNodes.add(duplicateSequenceLoop);
				}
			}
		} else {
			allSequences.add(Sequence);
		}
		if (listOfNodes.size() > 0) {
			performBFS(listOfNodes.remove());
		}
		return allSequences;
	}

	/*
	 * Checks if the provided sequence is complete and adheres to the provided
	 * sentence structure
	 */
	private boolean isValidSentence(SequenceOfEdges sequence) {
		boolean isValidSpec = true;
		List<Vertice> listOfVertices = getVerticesFromSequence(sequence);
		if (listOfVertices.size() == permittedSentenceSpec.length) {
			for (int i = 0; i < permittedSentenceSpec.length; i++) {
				if (!listOfVertices.get(i).SpeechType
						.equals(permittedSentenceSpec[i])) {
					isValidSpec = false;
					break;
				}
			}
		} else {
			isValidSpec = false;
		}
		return isValidSpec;
	}

	/*
	 * Creates a duplicate of the input sequence so that a new edge can be
	 * processed.
	 */
	private SequenceOfEdges duplicateSequence(SequenceOfEdges sequence2) {
		List<Edge> edgeListNew = new ArrayList<Edge>();
		for (Edge ed : sequence2.edgeList) {
			edgeListNew.add(ed);
		}
		SequenceOfEdges seqEdge = new SequenceOfEdges(edgeListNew);
		return seqEdge;
	}

	
	/*
	 * Checks if the input vertex is consistent with the sentence structure
	 * provided. Look ahead and check if there is an edge
	 * emanating from the input vertex which will adhere to the sentence
	 * structure provided
	 */
	public boolean mayFormValidSentence(SequenceOfEdges Sequence, Vertice vertex) {
		boolean isValidSpec = false;
		List<Vertice> listOfVertices = getVerticesFromSequence(Sequence);
		if (listOfVertices.size() > 0
				&& listOfVertices.size() < permittedSentenceSpec.length) {
			if (permittedSentenceSpec[listOfVertices.size()]
					.equals(vertex.SpeechType)) {
				isValidSpec = true;
			}

		}
		return isValidSpec;
	}

	
	/*
	 * Gets all the vertices from the sequence
	 */
	public static List<Vertice> getVerticesFromSequence(SequenceOfEdges sequence) {
		List<Vertice> listOfVertices = new ArrayList<Vertice>();
		for (int i = 0; i < sequence.edgeList.size(); i++) {

			if (i == 0 && sequence.edgeList.get(i).firstVertice != null) {
				listOfVertices.add(sequence.edgeList.get(i).firstVertice);
				listOfVertices.add(sequence.edgeList.get(i).secondVertice);
			} else {
				listOfVertices.add(sequence.edgeList.get(i).secondVertice);
			}
		}

		return listOfVertices;

	}

	
	/*
	 * Gets all the edges from the last node in the sequence
	 */
	public List<Edge> getEdgesFromNode(SequenceOfEdges Sequence) {

		Edge currentEdge = Sequence.edgeList.get(Sequence.edgeList.size() - 1);
		List<Edge> edgeList = new ArrayList<Edge>();
		for (Edge edge : this.inputGraph.Edges) {
			if (edge.firstVertice.equals(currentEdge.secondVertice)) {
				edgeList.add(edge);
			}
		}
		return edgeList;
	}

}
