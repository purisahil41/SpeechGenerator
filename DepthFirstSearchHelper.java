package assignment1_NaturalSpeech;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import assignment1_NaturalSpeech.Vertice.NodeStatus;

public class DepthFirstSearchHelper {
	Graph inputGraph;
	String[] permittedSentenceSpec;
	String startWord;
	String startSpeechType;
	Vertice startVertice = null;
	public static Stack<SequenceOfEdges> stackOfNodes = new Stack<SequenceOfEdges>();
	List<SequenceOfEdges> allSequences = new ArrayList<SequenceOfEdges>();
	int noOfNodesCompared = 1;

	public DepthFirstSearchHelper(Graph inputGraph,
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
		stackOfNodes.push(seq);
	}

	public List<SequenceOfEdges> PerformDFS(SequenceOfEdges Sequence) {
		List<Vertice> allVertices = getVerticesFromSequence(Sequence);
		allVertices.get(allVertices.size() - 1).nodeStatus = NodeStatus.VISITED;
		if (Sequence.edgeList.get(Sequence.edgeList.size() - 1).firstVertice != null) {
			Sequence.edgeList.get(Sequence.edgeList.size() - 1).firstVertice.nodeStatus = NodeStatus.VISITED;
		}
		if (!(isValidSentence(Sequence))) {
			// Get Edges starting from the source node
			List<Edge> verticeEdges = getEdgesFromNode(Sequence);
			for (Edge currEdge : verticeEdges) {
				
				// Marking Vertex of edges to visited
				currEdge.firstVertice.nodeStatus = NodeStatus.VISITED;
				// Check the ending vertex for validity
				if (mayFormValidSentence(Sequence, currEdge.secondVertice)) {
					SequenceOfEdges duplicateSequence = duplicateSequence(Sequence);
					duplicateSequence.edgeList.add(currEdge);
					noOfNodesCompared++;
					stackOfNodes.push(duplicateSequence);
					PerformDFS(stackOfNodes.peek());
				}
			}
		} else {
			allSequences.add(Sequence);
			if (stackOfNodes.size() > 0) {
				stackOfNodes.pop();
			}
		}
		return allSequences;
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
				isValidSpec = true;
			}

		}
		return isValidSpec;
	}

	public List<Vertice> getVerticesFromSequence(SequenceOfEdges sequence) {
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
