package modeles;

public class EdgeModel {

	int idEdge, frequence;
	VertexModel source, destination;

	public EdgeModel(int idEdge) {
		this.idEdge = idEdge;
	}

	public EdgeModel(VertexModel source, VertexModel destination) {
		this.source = source;
		this.destination = destination;
	}

	@Override
	public String toString() {
		return String.valueOf(frequence);
	}

	public int getIdEdge() {
		return idEdge;
	}

	public void setFrequence(int frequence) {
		this.frequence = frequence;
	}

	public int getFrequence() {
		return frequence;
	}

}