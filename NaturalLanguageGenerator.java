package assignment1_NaturalSpeech;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

public class NaturalLanguageGenerator {

	public static void main(String[] args) {

		String inputFilePath = "C:\\Users\\Sahil Puri\\workspace\\CS686_Assignment1_IntroToAI\\src\\assignment1_NaturalSpeech\\input.txt";

		String StartWord = "a";
		String StartSpeechType = "DT";
		String[] PermittedSentenceSpec = { "DT", "NN", "VBD", "NNP" };

		// Reading Input Text File
		Graph InputGraph = new Graph(inputFilePath);

		// Run BFS and return sentences with permitted specification and
		// probabilities
		String[] Sentence = RunBFSOnGraph(InputGraph, PermittedSentenceSpec,
				StartWord, StartSpeechType);
		
		

	}

	public static String[] RunBFSOnGraph(Graph InputGraph,
			String[] PermittedSentenceSpec, String StartWord,
			String StartSpeechType) {
		String[] Sentence = new String[PermittedSentenceSpec.length];
		
		// Initialize BFSHelper
		BreadthFirstSearchHelper BfsSearchHelper = new BreadthFirstSearchHelper(
				InputGraph, PermittedSentenceSpec, StartWord, StartSpeechType);

		// PerformBFS
		List<SequenceOfEdges> Sequence = BfsSearchHelper
				.PerformBFS(BfsSearchHelper.listOfNodes.remove());
		
		// Print the Output
		double MaxProb = 0.00;
		String sentenceBest = "Best Sentence : ";
		String SpeechSentenceCorrect = ", Correct Speech : ";
		boolean maxExceeded = false;
		for (SequenceOfEdges seqEdge : Sequence) {

			double prob = 1.00;
			String sentence = "Sentence : ";
			String SpeechSentence = ", Speech : ";
			for (Edge edgeInSequence : seqEdge.edgeList) {
				prob *= edgeInSequence.Probability;
			}
			if (prob > MaxProb) {
				MaxProb = prob;
				maxExceeded = true;

			}

			List<Vertice> allVertices = BfsSearchHelper
					.getVerticesFromSequence(seqEdge);

			if (maxExceeded) {
				sentenceBest = "Best Sentence : ";
				SpeechSentenceCorrect = ", Correct Speech : ";
			}
			for (Vertice ver : allVertices) {
				sentence += ver.Name + " ";
				SpeechSentence += ver.SpeechType + " ";

				if (maxExceeded) {

					sentenceBest += ver.Name + " ";
					SpeechSentenceCorrect += ver.SpeechType + " ";

				}
			}
			maxExceeded = false;
			System.out.println(sentence + SpeechSentence + ", Probability : "
					+ prob);
		}
		System.out.println(sentenceBest + SpeechSentenceCorrect
				+ ", Probability : " + MaxProb);
		return Sentence;
	}

}
