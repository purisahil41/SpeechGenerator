package assignment1_NaturalSpeech;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

public class HeuristicSearchHelper {
	Graph inputGraph;
	String[] permittedSentenceSpec;
	String startWord;
	String startSpeechType;
	Vertice startVertice = null;
	double maxProb = 0.0;

	// Comparator for logic while adding nodes to priority queue
	Comparator<SequenceOfEdges> comparator = new ProbabilityComparator();
	PriorityQueue<SequenceOfEdges> listOfNodes = new PriorityQueue<SequenceOfEdges>(
			1, comparator);
	List<SequenceOfEdges> allSequences = new ArrayList<SequenceOfEdges>();
	SequenceOfEdges duplicateSequence = null;
	int noOfNodesCompared = 1;

	/*
	 * Helper Class Constructor to initialize data regarding search Input -
	 * inputGraph - Graph representation of input.txt permittedSentenceSpec -
	 * Sentence structure startWord - Starting word for the search to work on
	 * startSpeechType - POS of the starting word
	 */
	public HeuristicSearchHelper(Graph inputGraph,
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
	 * Implementation of Heuristic search Input - Sequence - Sequence of edges
	 * whose last vertex in the last edge is to be processed. Returns - The list
	 * of sequences that adhere to the sentence structure
	 */
	public List<SequenceOfEdges> PerformHeuristicSearch(SequenceOfEdges Sequence) {

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
					if (checkForProbExclusion(duplicateSequenceLoop)) {
						noOfNodesCompared++;
						listOfNodes.add(duplicateSequenceLoop);
					}
				}
			}
		} else {
			allSequences.add(Sequence);
			checkForMaxProbability(Sequence);
		}
		if (listOfNodes.size() > 0) {
			PerformHeuristicSearch(listOfNodes.remove());
		}
		return allSequences;
	}

	/*
	 * HEURISTIC LOGIC 1.0 - Method for implementing of heuristic logic - Ignore
	 * a sequence if the probability is less than the maximum probability found
	 * for any other sequence
	 */
	private boolean checkForProbExclusion(SequenceOfEdges Sequence) {
		// Check if Probability of Current Sequence is less than max, then
		// IGNORE
		double prob = getProbability(Sequence);
		if (prob < maxProb) {
			return false;
		}
		return true;
	}

	private void checkForMaxProbability(SequenceOfEdges Sequence) {
		double prob = getProbability(Sequence);
		if (prob > maxProb) {
			maxProb = prob;
		}
	}

	private double getProbability(SequenceOfEdges Sequence) {
		double prob = 1.0;
		for (Edge edge : Sequence.edgeList) {
			prob *= edge.probability;
		}
		return prob;
	}

	/*
	 * Probability comparison comparator
	 */
	public class ProbabilityComparator implements Comparator<SequenceOfEdges> {
		@Override
		public int compare(SequenceOfEdges x, SequenceOfEdges y) {
			double probX = calculateProbForSequence(x);
			double probY = calculateProbForSequence(y);
			if (probX < probY) {
				return -1;
			}
			if (probX > probY) {
				return 1;
			}
			return 0;
		}

		private double calculateProbForSequence(SequenceOfEdges x) {
			return x.edgeList.get(x.edgeList.size() - 1).probability;
		}
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
	 * provided. HEURISTIC LOGIC 1.2 - Look ahead and check if there is an edge
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
				if (listOfVertices.size() == permittedSentenceSpec.length - 1) {
					isValidSpec = true;
				} else if (listOfVertices.size() + 1 <= permittedSentenceSpec.length) {
					if (isValidPropogableEdgePresent(vertex,
							permittedSentenceSpec[listOfVertices.size() + 1])) {
						isValidSpec = true;
					} else {
						isValidSpec = false;
					}

				}
			}

		}
		return isValidSpec;
	}

	private boolean isValidPropogableEdgePresent(Vertice vertex, String string) {
		for (Edge e : inputGraph.Edges) {
			if (e.firstVertice.equals(vertex)) {
				if (e.secondVertice.SpeechType.equals(string)) {
					return true;
				}
			}
		}
		return false;
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
