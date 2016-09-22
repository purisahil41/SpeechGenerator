package assignment1_NaturalSpeech;

public class Edge {
	Vertice FirstVertice;
	Vertice SecondVertice;
	double Probability = 0.0;

	public Edge(Vertice FirstVertice, Vertice SecondVertice, String Prob) {
		this.FirstVertice = FirstVertice;
		this.SecondVertice = SecondVertice;
		this.Probability = Double.parseDouble(Prob);
	}

	@Override
	public boolean equals(Object object) {
		boolean sameSame = false;

		if (object != null) {
			if (this.FirstVertice.equals(((Edge) object).FirstVertice)
					&& this.SecondVertice.equals(((Edge) object).SecondVertice)
					&& this.Probability == ((Edge) object).Probability) {
				sameSame = true;
			}
		}
		return sameSame;
	}
}