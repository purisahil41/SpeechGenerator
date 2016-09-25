package assignment1_NaturalSpeech;

public class Vertice {
	String Name = "";
	String SpeechType = "";
	NodeStatus nodeStatus = NodeStatus.UNVISITED;

	public Vertice(String edge) {
		String[] edgeSplit = edge.split("/", 2);
		this.Name = edgeSplit[0];
		this.SpeechType = edgeSplit[1];
	}

	@Override
	public boolean equals(Object object) {
		boolean sameSame = false;
		if (object != null) {
			if (this.Name.equals(((Vertice) object).Name)
					&& this.SpeechType.equals(((Vertice) object).SpeechType)) {
				sameSame = true;
			}
		}
		return sameSame;
	}

	public enum NodeStatus {
		UNVISITED, VISITED, COMPLETE
	}
}
