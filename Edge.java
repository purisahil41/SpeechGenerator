package assignment1_NaturalSpeech;

public class Edge {
	Vertice firstVertice;
	Vertice secondVertice;
	double probability = 0.0;

	public Edge(Vertice FirstVertice, Vertice SecondVertice, String Prob) {
		this.firstVertice = FirstVertice;
		this.secondVertice = SecondVertice;
		this.probability = Double.parseDouble(Prob);
	}

	@Override
	public boolean equals(Object object) {
		boolean sameSame = false;

		if (object != null) {
			if (this.firstVertice.equals(((Edge) object).firstVertice)
					&& this.secondVertice.equals(((Edge) object).secondVertice)
					&& this.probability == ((Edge) object).probability) {
				sameSame = true;
			}
		}
		return sameSame;
	}
}