package modeles;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

public class VertexModel {
	private int idLocuteur;
	private String nomLocuteur;
	private boolean isDominant;
	private int inEdgeFreqs, outEdgeFreqs;
	private TreeSet<Integer> setEdgesFreqs;
	private double degreeScore, pageRankScore, closenessCentrality, betweennessCentrality, eigenvectorCentrality, voltageScore;
	private Map<VertexModel, Integer> mapLinkedVertices = new HashMap<VertexModel, Integer>();

	public VertexModel(int id, String name, boolean isDominant) {
		this.idLocuteur = id;
		this.nomLocuteur = name;
		this.isDominant = isDominant;
		this.setEdgesFreqs = new TreeSet<Integer>();
	}

	@Override
	public String toString() {
		return nomLocuteur;
	}

	public int getIdLocuteur() {
		return idLocuteur;
	}

	public String getNomLocuteur() {
		return nomLocuteur;
	}
	
	public boolean getIsDominant() {
		return isDominant;
	}

	public int getInEdgeFreqs() {
		return inEdgeFreqs;
	}

	public void setInEdgeFreqs(int inEdgeFreqs) {
		this.inEdgeFreqs = inEdgeFreqs;
	}

	public int getOutEdgeFreqs() {
		return outEdgeFreqs;
	}

	public void setOutEdgeFreqs(int outEdgeFreqs) {
		this.outEdgeFreqs = outEdgeFreqs;
	}

	public TreeSet<Integer> getSetEdgesFreqs() {
		return setEdgesFreqs;
	}

	public double getDegreeScore() {
		return degreeScore;
	}

	public void setDegreeScore(double degreeScore) {
		this.degreeScore = degreeScore;
	}

	public double getPageRankScore() {
		return pageRankScore;
	}

	public void setPageRankScore(double pageRankScore) {
		this.pageRankScore = pageRankScore;
	}

	public double getClosenessCentrality() {
		return closenessCentrality;
	}

	public void setClosenessCentrality(double closenessCentrality) {
		this.closenessCentrality = closenessCentrality;
	}

	public double getBetweennessCentrality() {
		return betweennessCentrality;
	}

	public void setBetweennessCentrality(double betweennessCentrality) {
		this.betweennessCentrality = betweennessCentrality;
	}

	public double getEigenvectorCentrality() {
		return eigenvectorCentrality;
	}

	public void setEigenvectorCentrality(double eigenvectorCentrality) {
		this.eigenvectorCentrality = eigenvectorCentrality;
	}

	public double getVoltageScore() {
		return voltageScore;
	}

	public void setVoltageScore(double voltageScore) {
		this.voltageScore = voltageScore;
	}

	public Map<VertexModel, Integer> getMapLinkedVertices() {
		return mapLinkedVertices;
	}

	public void putInMapLinkedVertices(VertexModel vertexToPut, int freq) {
		this.mapLinkedVertices.put(vertexToPut, freq);
	}
	
}