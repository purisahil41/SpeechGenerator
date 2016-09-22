package assignment1_NaturalSpeech;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class BreadthFirstSearchHelper {
	Graph InputGraph;
	String[] PermittedSentenceSpec;
	String StartWord;
	String StartSpeechType;
	Vertice StartVertice = null;
	public static Queue<SequenceOfEdges> listOfNodes = new ArrayDeque<SequenceOfEdges>();
	List<SequenceOfEdges> allSequences = new ArrayList<SequenceOfEdges>();
	boolean ifSequenceModified = false;
	SequenceOfEdges duplicateSequence = null;

	public BreadthFirstSearchHelper(Graph inputGraph,
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
		listOfNodes.add(seq);
	}

	public List<SequenceOfEdges> PerformBFS(SequenceOfEdges Sequence) {
		
			duplicateSequence = duplicateSequence(Sequence);
		
		if (!(ifSequenceModified && isValidSentence(Sequence))) {

			// Get Edges starting from the source node
			List<Edge> verticeEdges = GetEdgesFromNode(Sequence);

			// Parse each edge
			for (int i = 0; i < verticeEdges.size(); i++) {

				// Check the ending vertex for validity
				if (mayFormValidSentence(duplicateSequence,
						verticeEdges.get(i).SecondVertice)) {

					if (!ifSequenceModified) {
						SequenceOfEdges CopyOfSequence = duplicateSequence(Sequence);
						List<Edge> listOfEdgesSequence = new ArrayList<Edge>();
						listOfEdgesSequence.add(verticeEdges.get(i));
						CopyOfSequence = new SequenceOfEdges(
								listOfEdgesSequence);
						listOfNodes.add(CopyOfSequence);
					} else {
						SequenceOfEdges duplicateSequenceLoop = duplicateSequence(duplicateSequence);
						duplicateSequenceLoop.edgeList.add(verticeEdges.get(i));
						listOfNodes.add(duplicateSequenceLoop);
					}
				}
			}
		} else {
			allSequences.add(Sequence);
		}
		if (listOfNodes.size() > 0) {
			ifSequenceModified = true;
			PerformBFS(listOfNodes.remove());
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
		for(Edge ed : sequence2.edgeList)
		{
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
