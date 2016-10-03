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

	Comparator<SequenceOfEdges> comparator = new ProbabilityComparator();
	PriorityQueue<SequenceOfEdges> listOfNodes = new PriorityQueue<SequenceOfEdges>(
			1, comparator);
	List<SequenceOfEdges> allSequences = new ArrayList<SequenceOfEdges>();
	boolean ifSequenceModified = false;
	SequenceOfEdges duplicateSequence = null;
	int noOfNodesCompared = 1;

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

	public List<SequenceOfEdges> PerformHeuristicSearch(SequenceOfEdges Sequence) {

		duplicateSequence = duplicateSequence(Sequence);

		if (!(ifSequenceModified && isValidSentence(Sequence))) {

			// Get Edges starting from the source node
			List<Edge> verticeEdges = getEdgesFromNode(Sequence);

			// Parse each edge
			for (int i = 0; i < verticeEdges.size(); i++) {

				// Check the ending vertex for validity
				if (mayFormValidSentence(duplicateSequence,
						verticeEdges.get(i).secondVertice)) {

					if (!ifSequenceModified) {
						SequenceOfEdges CopyOfSequence = duplicateSequence(Sequence);
						List<Edge> listOfEdgesSequence = new ArrayList<Edge>();
						listOfEdgesSequence.add(verticeEdges.get(i));
						CopyOfSequence = new SequenceOfEdges(
								listOfEdgesSequence);
						if (checkForProbExclusion(CopyOfSequence)) {

							noOfNodesCompared++;
							listOfNodes.add(CopyOfSequence);
							//log(listOfNodes);
						}						
					} else {
						SequenceOfEdges duplicateSequenceLoop = duplicateSequence(duplicateSequence);
						duplicateSequenceLoop.edgeList.add(verticeEdges.get(i));
						if (checkForProbExclusion(duplicateSequenceLoop)) {

						noOfNodesCompared++;
						listOfNodes.add(duplicateSequenceLoop);
						}						
					}
				}
			}
		} else {
			allSequences.add(Sequence);
			checkForMaxProbability(Sequence);
		}
		if (listOfNodes.size() > 0) {
			ifSequenceModified = true;
			PerformHeuristicSearch(listOfNodes.remove());
		}
		return allSequences;
	}

	/*private void log(PriorityQueue<SequenceOfEdges> listOfNodes2) {
		try {
			File file = new File(
					"C:\\Users\\Sahil Puri\\workspace\\CS686_Assignment1_IntroToAI\\src\\assignment1_NaturalSpeech\\log.txt");
			FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
			BufferedWriter bw = new BufferedWriter(fw);

			for (SequenceOfEdges seq : listOfNodes2) {
				for (int i = 0; i < seq.edgeList.size(); i++) {
					if (i == 0) {
						bw.append(seq.edgeList.get(i).FirstVertice.Name + "-");
					}
					bw.append(seq.edgeList.get(i).SecondVertice.Name + "-");
				}
				bw.append("\n");
			}

			bw.close();// be sure to close BufferedWriter

		} catch (Exception e) {

		}

	}*/

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

	private SequenceOfEdges duplicateSequence(SequenceOfEdges sequence2) {
		List<Edge> edgeListNew = new ArrayList<Edge>();
		for (Edge ed : sequence2.edgeList) {
			edgeListNew.add(ed);
		}
		SequenceOfEdges seqEdge = new SequenceOfEdges(edgeListNew);
		return seqEdge;
	}

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
