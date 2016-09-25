package assignment1_NaturalSpeech;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import assignment1_NaturalSpeech.Vertice.NodeStatus;

public class DepthFirstSearchHelper {
	Graph InputGraph;
	String[] PermittedSentenceSpec;
	String StartWord;
	String StartSpeechType;
	Vertice StartVertice = null;
	public static Stack<SequenceOfEdges> stackOfNodes = new Stack<SequenceOfEdges>();
	List<SequenceOfEdges> allSequences = new ArrayList<SequenceOfEdges>();
	int noOfNodesCompared = 0;

	public DepthFirstSearchHelper(Graph inputGraph,
			String[] permittedSentenceSpec, String startWord,
			String startSpeechType) {
		this.InputGraph = inputGraph;
		this.PermittedSentenceSpec = permittedSentenceSpec;
		this.StartWord = startWord;
		this.StartSpeechType = startSpeechType;

		for (Vertice ver : this.InputGraph.Vertices) {
			if (ver.Name.equals(this.StartWord)
					&& ver.SpeechType.equals(this.StartSpeechType)) {
				this.StartVertice = ver;
				break;
			}
		}
		List<Edge> edgeNew = new ArrayList<Edge>();
		edgeNew.add(new Edge(null, this.StartVertice, "1.0"));
		SequenceOfEdges seq = new SequenceOfEdges(edgeNew);
		stackOfNodes.push(seq);
	}

	public List<SequenceOfEdges> PerformDFS(SequenceOfEdges Sequence) {
		// Mark last vertex of the sequence as VISITED
		List<Vertice> allVertices = getVerticesFromSequence(Sequence);
		allVertices.get(allVertices.size() - 1).nodeStatus = NodeStatus.VISITED;
		if (Sequence.edgeList.get(Sequence.edgeList.size() - 1).FirstVertice != null) {
			Sequence.edgeList.get(Sequence.edgeList.size() - 1).FirstVertice.nodeStatus = NodeStatus.VISITED;
		}
		if (!(isValidSentence(Sequence))) {
			// Get Edges starting from the source node
			List<Edge> verticeEdges = GetEdgesFromNode(Sequence);
			for (Edge currEdge : verticeEdges) {
				
				// Marking Vertex of edges to visited
				currEdge.FirstVertice.nodeStatus = NodeStatus.VISITED;
				// Check the ending vertex for validity
				if (mayFormValidSentence(Sequence, currEdge.SecondVertice)) {
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
		if (listOfVertices.size() == PermittedSentenceSpec.length) {
			for (int i = 0; i < PermittedSentenceSpec.length; i++) {
				if (!listOfVertices.get(i).SpeechType
						.equals(PermittedSentenceSpec[i])) {
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
				&& listOfVertices.size() < PermittedSentenceSpec.length) {
			if (PermittedSentenceSpec[listOfVertices.size()]
					.equals(vertex.SpeechType)) {
				isValidSpec = true;
			}

		}
		return isValidSpec;
	}

	public List<Vertice> getVerticesFromSequence(SequenceOfEdges sequence) {
		List<Vertice> listOfVertices = new ArrayList<Vertice>();
		for (int i = 0; i < sequence.edgeList.size(); i++) {

			if (i == 0 && sequence.edgeList.get(i).FirstVertice != null) {
				listOfVertices.add(sequence.edgeList.get(i).FirstVertice);
				listOfVertices.add(sequence.edgeList.get(i).SecondVertice);
			} else {
				listOfVertices.add(sequence.edgeList.get(i).SecondVertice);
			}
		}

		return listOfVertices;

	}

	public List<Edge> GetEdgesFromNode(SequenceOfEdges Sequence) {

		Edge currentEdge = Sequence.edgeList.get(Sequence.edgeList.size() - 1);
		List<Edge> edgeList = new ArrayList<Edge>();
		for (Edge edge : this.InputGraph.Edges) {
			if (edge.FirstVertice.equals(currentEdge.SecondVertice)) {
				edgeList.add(edge);
			}
		}
		return edgeList;
	}

}
