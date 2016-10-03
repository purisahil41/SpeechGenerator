package assignment1_NaturalSpeech;

import java.util.List;

public class NaturalLanguageGenerator {

	public static void main(String[] args) {

		// File Path for Input.txt
		String inputFilePath = "C:\\Users\\Sahil Puri\\workspace\\CS686_Assignment1_IntroToAI\\src\\assignment1_NaturalSpeech\\input.txt";
		// Starting Word of the sentence to be constructed
		String startWord = "hans";
		// Permitted Sentence Structure
		String[] permittedSentenceSpec = { "NNP", "VBD", "DT", "NN" };
		// Part of the speech of the starting word -- Ideally can be different
		// but same as the first POS of sentence in this case
		String startSpeechType = permittedSentenceSpec[0];
		// Reading Input Text File into local graph data structure
		Graph inputGraph = new Graph(inputFilePath);
		// Search strategy to be employed
		String searchStrategy = "HeuristicSearch";

		generate(inputGraph, permittedSentenceSpec, startWord, startSpeechType,
				searchStrategy);

	}
	/*
	 * This method calls the search algorithm.
	 * Input - 
	 * 	InputGraph : Graph - of input.txt file
	 * 	PermittedSentenceSpec : String array - for the permitted sentence structure.
	 * 	StartWord : String - starting word of the sentence to be searched.
	 * 	StartSpeechType : String - Speech type of the starting word : Same as the fist POS of the provided sentence structure.
	 * 	searchStrategy : String - THe search algorithm to be employed.
	 */
	public static String[] generate(Graph inputGraph,
			String[] permittedSentenceSpec, String startWord,
			String startSpeechType, String searchStrategy) {
		String[] sentenceHeuristic = null;
		if (searchStrategy.equals("BreadthFirstSearch")) {
			// Run BFS and return sentences with permitted specification and
			// probabilities
			sentenceHeuristic = runBFSOnGraph(inputGraph,
					permittedSentenceSpec, startWord, startSpeechType);
		} else if (searchStrategy.equals("DepthFirstSearch")) {
			// Run DFS and return sentences with permitted specification and
			// probabilities
			sentenceHeuristic = runDFSOnGraph(inputGraph,
					permittedSentenceSpec, startWord, startSpeechType);
		} else if (searchStrategy.equals("HeuristicSearch")) {
			// Run Heuristic and return sentences with permitted specification
			// and
			// probabilities
			sentenceHeuristic = runHeuristicSearchOnGraph(inputGraph,
					permittedSentenceSpec, startWord, startSpeechType);
		}
		return sentenceHeuristic;

	}
	/*
	 * This method calls the implementer class of Heuristic algorithm.
	 * Input - 
	 * 	InputGraph : Graph - of input.txt file
	 * 	PermittedSentenceSpec : String array - for the permitted sentence structure
	 * 	StartWord : String - starting word of the sentence to be searched
	 * 	StartSpeechType : String - Speech type of the starting word : Same as the fist POS of the provided sentence structure
	 */
	public static String[] runHeuristicSearchOnGraph(Graph InputGraph,
			String[] PermittedSentenceSpec, String StartWord,
			String StartSpeechType) {
		String[] Sentence = new String[PermittedSentenceSpec.length];

		// Initialize HeuristicSearchHelper Class
		HeuristicSearchHelper heuristicSearchHelper = new HeuristicSearchHelper(
				InputGraph, PermittedSentenceSpec, StartWord, StartSpeechType);

		// PerformHeuristic Search
		List<SequenceOfEdges> Sequence = heuristicSearchHelper
				.PerformHeuristicSearch(heuristicSearchHelper.listOfNodes
						.remove());
		// Print the Best sentence as per the maximum probability
		printOutput(Sequence);
		// Print the number of comparisons made
		System.out.println("No of comparisons "
				+ heuristicSearchHelper.noOfNodesCompared);
		return Sentence;
	}
	/*
	 * This method calls the implementer class of BFS algorithm.
	 * Input - 
	 * 	InputGraph : Graph - of input.txt file
	 * 	PermittedSentenceSpec : String array - for the permitted sentence structure
	 * 	StartWord : String - starting word of the sentence to be searched
	 * 	StartSpeechType : String - Speech type of the starting word : Same as the fist POS of the provided sentence structure
	 */
	public static String[] runBFSOnGraph(Graph InputGraph,
			String[] PermittedSentenceSpec, String StartWord,
			String StartSpeechType) {
		String[] Sentence = new String[PermittedSentenceSpec.length];

		// Initialize BFSHelper Class
		BreadthFirstSearchHelper BfsSearchHelper = new BreadthFirstSearchHelper(
				InputGraph, PermittedSentenceSpec, StartWord, StartSpeechType);

		// Perform BFS Search
		List<SequenceOfEdges> Sequence = BfsSearchHelper
				.performBFS(BfsSearchHelper.listOfNodes.remove());
		// Print the best sentence as per the maximum probability
		printOutput(Sequence);
		// Print the number of comparisons made
		System.out.println("No of comparisons "
				+ BfsSearchHelper.noOfNodesCompared);
		return Sentence;
	}
	/*
	 * This method calls the implementer class of DFS algorithm.
	 * Input - 
	 * 	InputGraph : Graph - of input.txt file
	 * 	PermittedSentenceSpec : String array - for the permitted sentence structure
	 * 	StartWord : String - starting word of the sentence to be searched
	 * 	StartSpeechType : String - Speech type of the starting word : Same as the fist POS of the provided sentence structure
	 */
	public static String[] runDFSOnGraph(Graph InputGraph,
			String[] PermittedSentenceSpec, String StartWord,
			String StartSpeechType) {
		String[] Sentence = new String[PermittedSentenceSpec.length];

		// Initialize DFSHelper Class
		DepthFirstSearchHelper DfsSearchHelper = new DepthFirstSearchHelper(
				InputGraph, PermittedSentenceSpec, StartWord, StartSpeechType);

		// PerformDFS Search
		List<SequenceOfEdges> Sequence = DfsSearchHelper
				.PerformDFS(DfsSearchHelper.stackOfNodes.peek());
		// Print the best sentence as per the maximum probability
		printOutput(Sequence);
		// Print the number of comparisons made
		System.out.println("Total nodes considered "
				+ DfsSearchHelper.noOfNodesCompared);
		return Sentence;
	}

	/*
	 * This method prints the best sentence as per the maximum probability
	 * Input - sequence : List of all the sentences that adhere to the provided sentence structure
	 */
	public static void printOutput(List<SequenceOfEdges> sequence) {

		// Print the Output
		double MaxProb = 0.00;
		String sentenceBest = "";
		boolean maxExceeded = false;
		for (SequenceOfEdges seqEdge : sequence) {

			double prob = 1.00;
			String sentence = "Sentence : ";
			String SpeechSentence = ", Speech : ";
			for (Edge edgeInSequence : seqEdge.edgeList) {
				prob *= edgeInSequence.probability;
			}
			if (prob > MaxProb) {
				MaxProb = prob;
				maxExceeded = true;
			}
			List<Vertice> allVertices = BreadthFirstSearchHelper
					.getVerticesFromSequence(seqEdge);
			if (maxExceeded) {
				sentenceBest = "";
			}
			for (Vertice ver : allVertices) {
				sentence += ver.Name + " ";
				SpeechSentence += ver.SpeechType + " ";
				if (maxExceeded) {
					sentenceBest += ver.Name + " ";
				}
			}
			maxExceeded = false;
			System.out.println(sentence + SpeechSentence + ", Probability : "
					+ prob);

		}
		System.out.println(sentenceBest + " with a probability of " + MaxProb);

	}
}
