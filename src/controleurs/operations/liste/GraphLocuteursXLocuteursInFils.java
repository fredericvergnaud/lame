package controleurs.operations.liste;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.SortedSet;
import java.util.TreeMap;

import modeles.EdgeModel;
import modeles.LocuteurModel;
import modeles.MessageModel;
import modeles.VertexModel;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;

public class GraphLocuteursXLocuteursInFils {

	private Map<String, MessageModel> mapIdMessages;
	private SortedSet<LocuteurModel> setLocuteurs;

	private UndirectedSparseGraph<VertexModel, EdgeModel> undirectedGraph;

	// private DirectedSparseGraph<VertexModel, EdgeModel> directedGraph;

	public GraphLocuteursXLocuteursInFils(Map<String, MessageModel> mapIdMessages, SortedSet<LocuteurModel> setLocuteurs) {
		this.mapIdMessages = mapIdMessages;
		this.setLocuteurs = setLocuteurs;
	}

	public void createGraphs() {

		// Création des Graphes
		// directedGraph = new DirectedSparseGraph<VertexModel, EdgeModel>();
		undirectedGraph = new UndirectedSparseGraph<VertexModel, EdgeModel>();
		// Création d'une mapNomLocuteursVertex pour avoir des Vertex uniques
		Map<String, VertexModel> mapNomLocuteurVertex = new TreeMap<String, VertexModel>();
		for (LocuteurModel locuteur : setLocuteurs) {
			VertexModel vertex = new VertexModel(locuteur.getId(), locuteur.getNom(), locuteur.isLd());
			mapNomLocuteurVertex.put(locuteur.getNom(), vertex);
		}
		// System.out.println(mapNomLocuteurVertex);
		for (Entry<String, MessageModel> entry : mapIdMessages.entrySet()) {
			MessageModel messageSource = entry.getValue();
			String nomLocuteurSource = messageSource.getExpediteur();
			String idMessageDestination = messageSource.getInReplyTo();
			MessageModel messageDestination = mapIdMessages.get(idMessageDestination);
			String nomLocuteurDestination = null;
			if (messageDestination != null) {
				nomLocuteurDestination = messageDestination.getExpediteur();
			}
			if (nomLocuteurDestination != null && !nomLocuteurSource.equals(nomLocuteurDestination)) {
				if (mapNomLocuteurVertex.containsKey(nomLocuteurSource) && mapNomLocuteurVertex.containsKey(nomLocuteurDestination)) {
					VertexModel vertexSource = mapNomLocuteurVertex.get(nomLocuteurSource);
					VertexModel vertexDestination = mapNomLocuteurVertex.get(nomLocuteurDestination);
					Map<VertexModel, Integer> mapSourceLinkedVertices = vertexSource.getMapLinkedVertices();
					Map<VertexModel, Integer> mapDestinationLinkedVertices = vertexDestination.getMapLinkedVertices();
					if (!mapSourceLinkedVertices.containsKey(vertexDestination)) {
						mapSourceLinkedVertices.put(vertexDestination, 1);
					} else {
						int oldFreq = mapSourceLinkedVertices.get(vertexDestination);
						oldFreq += 1;
						mapSourceLinkedVertices.put(vertexDestination, oldFreq);
					}
					if (!mapDestinationLinkedVertices.containsKey(vertexSource)) {
						mapDestinationLinkedVertices.put(vertexSource, 1);
					} else {
						int oldFreq = mapDestinationLinkedVertices.get(vertexSource);
						oldFreq += 1;
						mapDestinationLinkedVertices.put(vertexSource, oldFreq);
					}
				}
			}
		}

		for (Entry<String, VertexModel> entry : mapNomLocuteurVertex.entrySet()) {
			VertexModel vertex = entry.getValue();
			System.out.println(vertex + " : " + vertex.getMapLinkedVertices().size() + " relations : " + vertex.getMapLinkedVertices());
		}

		// // CREATION mapLinkFreq
		// // + ajout des liens entrants et sortants pour chaque vertex
		// Map<String, Integer> mapLinkFreq = new TreeMap<String, Integer>();
		// for (Entry<String, MessageModel> entry : mapIdMessages.entrySet()) {
		// MessageModel messageSource = entry.getValue();
		// String nomLocuteurSource = messageSource.getExpediteur();
		// String idMessageDestination = messageSource.getInReplyTo();
		// MessageModel messageDestination =
		// mapIdMessages.get(idMessageDestination);
		// String nomLocuteurDestination = null;
		// if (messageDestination != null) {
		// nomLocuteurDestination = messageDestination.getExpediteur();
		// }
		// // Création d'une String lien unique
		// if (nomLocuteurDestination != null &&
		// !nomLocuteurSource.equals(nomLocuteurDestination)) {
		// String link = nomLocuteurSource + " %%% " + nomLocuteurDestination;
		// if (!mapLinkFreq.containsKey(link)) {
		// mapLinkFreq.put(link, 1);
		// } else {
		// int oldLinkFreq = mapLinkFreq.get(link);
		// oldLinkFreq += 1;
		// mapLinkFreq.put(link, oldLinkFreq);
		// }
		// }
		// }
		//
		// // Affichage : somme_freq doit être egal a nbreMessages
		// int sommeFreq = 0;
		// System.out.println("SOURCE %%% DESTINATION : FREQUENCE");
		// for (Entry<String, Integer> entry : mapLinkFreq.entrySet()) {
		// System.out.println(entry.getKey() + " : " + entry.getValue());
		// sommeFreq += entry.getValue();
		// }
		// System.out.println("Somme freq =  " + sommeFreq);
		//
		// System.out.println();
		//
		// // Doublement de la MAP
		// Map<String, Integer> mapLinkFreq2 = new TreeMap<String,
		// Integer>(mapLinkFreq);
		// // Création de la MAP finale
		// Map<String, Integer> mapLinkFreqFinale = new TreeMap<String,
		// Integer>();
		// Map<String, Integer> mapLinkFreqInverseToRemove = new TreeMap<String,
		// Integer>();
		// Iterator<Entry<String, Integer>> mapLinkFreqIterator =
		// mapLinkFreq.entrySet().iterator();
		// while (mapLinkFreqIterator.hasNext()) {
		// Entry<String, Integer> entry = mapLinkFreqIterator.next();
		// String relation1 = entry.getKey();
		// int frequence1 = entry.getValue();
		// String[] tabRelation1 = relation1.split("%%%");
		// String a1 = tabRelation1[0].trim();
		// String b1 = null;
		// if (tabRelation1.length > 1)
		// b1 = tabRelation1[1].trim();
		// else
		// b1 = a1;
		// // System.out.println("a1 = "+a1+" | b1 = "+b1);
		// Iterator<Entry<String, Integer>> mapLinkFreq2Iterator =
		// mapLinkFreq2.entrySet().iterator();
		// while (mapLinkFreq2Iterator.hasNext()) {
		// Entry<String, Integer> entry2 = mapLinkFreq2Iterator.next();
		// String relation2 = entry2.getKey();
		// int frequence2 = entry2.getValue();
		// String[] tabRelation2 = relation2.split("%%%");
		// String a2 = tabRelation2[0].trim();
		// String b2 = null;
		// if (tabRelation2.length > 1)
		// b2 = tabRelation2[1].trim();
		// else
		// b2 = a2;
		// // System.out.println("\ta2 = "+a2+" | b2 = "+b2);
		// if (a1.equals(b2) && a2.equals(b1)) {
		// int frequence12 = frequence1 + frequence2;
		// //
		// System.out.println(a1+" = "+b2+" | "+a2+" = "+b1+" | somme freq = "+frequence12);
		// mapLinkFreqFinale.put(relation1, frequence12);
		// mapLinkFreqIterator.remove();
		// mapLinkFreq2Iterator.remove();
		// if (mapLinkFreq.containsKey(relation2)) {
		// mapLinkFreqInverseToRemove.put(relation2,
		// mapLinkFreq.get(relation2));
		// }
		// break;
		// } else {
		// mapLinkFreqFinale.put(relation1, frequence1);
		// }
		// }
		// }
		// for (Entry<String, Integer> entry :
		// mapLinkFreqInverseToRemove.entrySet())
		// mapLinkFreqFinale.remove(entry.getKey());
		// System.out.println();
		// sommeFreq = 0;
		// System.out.println("MAP FINALE LIEN : FREQUENCE");
		// for (Entry<String, Integer> entry : mapLinkFreqFinale.entrySet()) {
		// System.out.println(entry.getKey() + " : " + entry.getValue());
		// sommeFreq += entry.getValue();
		// }
		// System.out.println("Somme freq =  " + sommeFreq);
		//
		// System.out.println();

		// Ajout des Vertex et Edge au graph
		// + Création d'une mapEdgeFrequence
		Map<EdgeModel, Number> mapEdgeFrequenciesUndirected = new HashMap<EdgeModel, Number>();
		int idEdge = 0;
		for (Entry<String, VertexModel> entry : mapNomLocuteurVertex.entrySet()) {
			VertexModel vertex = entry.getValue();
			Map<VertexModel, Integer> mapLinkedVertices = vertex.getMapLinkedVertices();
			if (vertex.getMapLinkedVertices().size() > 0)
				for (Entry<VertexModel, Integer> entry2 : mapLinkedVertices.entrySet()) {
					int linkedFreq = entry2.getValue();
					VertexModel linkedVertex = entry2.getKey();
					EdgeModel newEdge = new EdgeModel(idEdge);
					newEdge.setFrequence(linkedFreq);	
					mapEdgeFrequenciesUndirected.put(newEdge, linkedFreq);
					for (int i = 0; i < linkedFreq; i++) {
						undirectedGraph.addEdge(newEdge, vertex, linkedVertex, EdgeType.UNDIRECTED);
						idEdge++;
					}
				}
			else
				undirectedGraph.addVertex(vertex);
		}

		// // Ajout des Vertex et Edge au graph
		// // + Création d'une mapEdgeFrequence
		// Map<EdgeModel, Number> mapEdgeFrequencies = new HashMap<EdgeModel,
		// Number>();
		// Map<EdgeModel, Number> mapEdgeFrequenciesUndirected = new
		// HashMap<EdgeModel, Number>();
		// int idEdge = 0;
		// for (Entry<String, Integer> entry : mapLinkFreqFinale.entrySet()) {
		// String[] tabLink = entry.getKey().split(" %%% ");
		// String nomLocuteurSource = tabLink[0].trim();
		// //
		// System.out.println("nomLocuteurSource évalué = "+nomLocuteurSource);
		// VertexModel source = mapNomLocuteurVertex.get(nomLocuteurSource);
		// // System.out.println("Source évaluée = "+source);
		// String nomLocuteurDestination = "";
		// VertexModel destination = null;
		// if (tabLink.length > 1) {
		// nomLocuteurDestination = tabLink[1].trim();
		// destination = mapNomLocuteurVertex.get(nomLocuteurDestination);
		// }
		//
		// if (destination != null) {
		// int edgeFreq = entry.getValue();
		// for (int i = 0; i < edgeFreq; i++) {
		// EdgeModel newEdge = new EdgeModel(idEdge);
		// newEdge.setFrequence(edgeFreq);
		// mapEdgeFrequencies.put(newEdge, edgeFreq);
		// source.getSetEdgesFreqs().add(edgeFreq);
		// destination.getSetEdgesFreqs().add(edgeFreq);
		// // directedGraph.addEdge(newEdge, source, destination,
		// // EdgeType.DIRECTED);
		// undirectedGraph.addEdge(newEdge, source, destination,
		// EdgeType.UNDIRECTED);
		// source.setOutEdgeFreqs(source.getOutEdgeFreqs() + 1);
		// destination.setInEdgeFreqs(destination.getInEdgeFreqs() + 1);
		// mapEdgeFrequenciesUndirected.put(newEdge, source.getOutEdgeFreqs() +
		// destination.getInEdgeFreqs());
		// // Transformer<EdgeModel, Integer> wtTransformer = new
		// // Transformer<EdgeModel, Integer>() {
		// // public Integer transform(EdgeModel edge) {
		// // return edge.getFrequence();
		// // }
		// // };
		// // DijkstraShortestPath<VertexModel, EdgeModel> alg = new
		// // DijkstraShortestPath<VertexModel, EdgeModel>(graph,
		// // wtTransformer);
		// // List<EdgeModel> l = alg.getPath(source, destination);
		// // Number dist = alg.getDistance(source, destination);
		// // System.out
		// // .println("\tThe shortest path from " + source + " to " +
		// // destination + " is:");
		// // System.out.println("\t"+l.toString());
		// // System.out.println("\tand the length of the path is: " +
		// // dist);
		// idEdge++;
		// }
		// } else {
		// // directedGraph.addVertex(source);
		// undirectedGraph.addVertex(source);
		// source.setOutEdgeFreqs(source.getOutEdgeFreqs() + entry.getValue());
		// }
		// //
		// System.out.println("Lien "+entry.getKey()+" : vertex "+source.getNomLocuteur()+" passe à "+source.getOutEdges()+" outEdges");
		// }

		// System.out.println("mapEdgeFrequence : "+mapEdgeFrequence);

		// // ALGORITHMES DE CENTRALITE
		// // See http://en.wikipedia.org/wiki/Centrality
		//
		// // PAGERANK
		// // Transformer pour les edge_weights
		// Transformer<EdgeModel, Number> transformerEdgeFrequencies = new
		// Transformer<EdgeModel, Number>() {
		// @Override
		// public Number transform(EdgeModel edge) {
		// return edge.getFrequence();
		// }
		// };
		// Transformer<EdgeModel, Number> transformerEdgeFrequenciesUndirected =
		// new Transformer<EdgeModel, Number>() {
		// @Override
		// public Number transform(EdgeModel edge) {
		// Pair<VertexModel> vertices = undirectedGraph.getEndpoints(edge);
		// if (vertices != null) {
		// VertexModel v1 = vertices.getFirst();
		// return (v1.getInEdgeFreqs() + v1.getOutEdgeFreqs());
		// } else
		// return 0;
		// }
		// };
		// double alpha = 0.1;
		// // PageRank<VertexModel, EdgeModel> pageRanker = new
		// // PageRank<VertexModel, EdgeModel>(directedGraph,
		// // transformerEdgeFrequencies, alpha);
		// PageRank<VertexModel, EdgeModel> pageRankerUndirected = new
		// PageRank<VertexModel, EdgeModel>(undirectedGraph,
		// transformerEdgeFrequenciesUndirected, alpha);
		// // ranker.setTolerance(this.tolerance) ;
		// // ranker.setMaxIterations(this.maxIterations);
		// // pageRanker.evaluate();
		// pageRankerUndirected.evaluate();
		//
		// // DEGREE SCORER
		// // DegreeScorer<VertexModel> degreeScorer = new
		// // DegreeScorer<VertexModel>(directedGraph);
		// DegreeScorer<VertexModel> degreeScorerUndirected = new
		// DegreeScorer<VertexModel>(undirectedGraph);
		//
		// // CLOSENESS CENTRALITY = mesure du temps pris pour diffuser une
		// // information de v vers tous les autres sommets
		// // Plus un sommet est central, plus sa distance avec tous les autres
		// // sommets est faible
		// // ClosenessCentrality<VertexModel, EdgeModel> closenessCentrality =
		// new
		// // ClosenessCentrality<VertexModel, EdgeModel>(directedGraph,
		// // transformerEdgeFrequencies);
		// ClosenessCentrality<VertexModel, EdgeModel>
		// closenessCentralityUndirected = new ClosenessCentrality<VertexModel,
		// EdgeModel>(undirectedGraph, transformerEdgeFrequenciesUndirected);
		//
		// // BETWEENNESS CENTRALITY = Mesure de la centralité d'un node
		// // BetweennessCentrality<VertexModel, EdgeModel>
		// betweennessCentrality =
		// // new BetweennessCentrality<VertexModel, EdgeModel>(directedGraph,
		// // true, true);
		// // betweennessCentrality.setEdgeWeights(mapEdgeFrequencies);
		// // betweennessCentrality.setRemoveRankScoresOnFinalize(false);
		// // betweennessCentrality.evaluate();
		//
		// BetweennessCentrality<VertexModel, EdgeModel>
		// betweennessCentralityUndirected = new
		// BetweennessCentrality<VertexModel, EdgeModel>(undirectedGraph, true,
		// true);
		// betweennessCentralityUndirected.setEdgeWeights(mapEdgeFrequenciesUndirected);
		// betweennessCentralityUndirected.setRemoveRankScoresOnFinalize(false);
		// betweennessCentralityUndirected.evaluate();
		//
		// // EIGENVECTOR CENTRALITY = Influence d'un node dans un réseau
		// // Pour les graphs très connectés
		// // EigenvectorCentrality<VertexModel, EdgeModel>
		// eigenvectorCentrality =
		// // new EigenvectorCentrality<VertexModel, EdgeModel>(directedGraph,
		// // transformerEdgeFrequenciesUndirected);
		// // eigenvectorCentrality.acceptDisconnectedGraph(true);
		// // eigenvectorCentrality.evaluate();
		//
		// EigenvectorCentrality<VertexModel, EdgeModel>
		// eigenvectorCentralityUndirected = new
		// EigenvectorCentrality<VertexModel, EdgeModel>(undirectedGraph,
		// transformerEdgeFrequenciesUndirected);
		// eigenvectorCentralityUndirected.acceptDisconnectedGraph(true);
		// eigenvectorCentralityUndirected.evaluate();
		//
		// // Map<Double, Set<VertexModel>> listPageRank = new TreeMap<Double,
		// // Set<VertexModel>>();
		// // Map<Integer, Set<VertexModel>> listDegree = new TreeMap<Integer,
		// // Set<VertexModel>>();
		// // Map<Double, Set<VertexModel>> listCloseness = new TreeMap<Double,
		// // Set<VertexModel>>();
		// // Map<Double, Set<VertexModel>> listBetweenness = new
		// TreeMap<Double,
		// // Set<VertexModel>>();
		// // Map<Double, Set<VertexModel>> listEigenvector = new
		// TreeMap<Double,
		// // Set<VertexModel>>();
		// Map<Double, Set<VertexModel>> listPageRankUndirected = new
		// TreeMap<Double, Set<VertexModel>>();
		// Map<Integer, Set<VertexModel>> listDegreeUndirected = new
		// TreeMap<Integer, Set<VertexModel>>();
		// Map<Double, Set<VertexModel>> listClosenessUndirected = new
		// TreeMap<Double, Set<VertexModel>>();
		// Map<Double, Set<VertexModel>> listBetweennessUndirected = new
		// TreeMap<Double, Set<VertexModel>>();
		// Map<Double, Set<VertexModel>> listEigenvectorUndirected = new
		// TreeMap<Double, Set<VertexModel>>();
		//
		// // // System.out.println("DIRECTED NETWORK");
		// // for (Entry<String, VertexModel> entry :
		// // mapNomLocuteurVertex.entrySet()) {
		// // VertexModel vertex = entry.getValue();
		// // // System.out.println(vertex);
		// // // System.out.println("\tPAGE RANKER : \t\t\t" +
		// // // pageRanker.getVertexScore(vertex));
		// // if (!listPageRank.containsKey(pageRanker.getVertexScore(vertex)))
		// {
		// // Set<VertexModel> newSet = new HashSet<VertexModel>();
		// // newSet.add(vertex);
		// // listPageRank.put(pageRanker.getVertexScore(vertex), newSet);
		// // } else {
		// // Set<VertexModel> oldSet =
		// // listPageRank.get(pageRanker.getVertexScore(vertex));
		// // oldSet.add(vertex);
		// // listPageRank.put(pageRanker.getVertexScore(vertex), oldSet);
		// // }
		// //
		// // // System.out.println("\tDEGREE SCORER : \t\t" +
		// // // degreeScorer.getVertexScore(vertex));
		// // if (!listDegree.containsKey(degreeScorer.getVertexScore(vertex)))
		// {
		// // Set<VertexModel> newSet = new HashSet<VertexModel>();
		// // newSet.add(vertex);
		// // listDegree.put(degreeScorer.getVertexScore(vertex), newSet);
		// // } else {
		// // Set<VertexModel> oldSet =
		// // listDegree.get(degreeScorer.getVertexScore(vertex));
		// // oldSet.add(vertex);
		// // listDegree.put(degreeScorer.getVertexScore(vertex), oldSet);
		// // }
		// //
		// // // System.out.println("\tCLOSENESS CENTRALITY : \t\t" +
		// // // closenessCentrality.getVertexScore(vertex));
		// // if
		// //
		// (!listCloseness.containsKey(closenessCentrality.getVertexScore(vertex)))
		// // {
		// // Set<VertexModel> newSet = new HashSet<VertexModel>();
		// // newSet.add(vertex);
		// // listCloseness.put(closenessCentrality.getVertexScore(vertex),
		// // newSet);
		// // } else {
		// // Set<VertexModel> oldSet =
		// // listCloseness.get(closenessCentrality.getVertexScore(vertex));
		// // oldSet.add(vertex);
		// // listCloseness.put(closenessCentrality.getVertexScore(vertex),
		// // oldSet);
		// // }
		// // // System.out.println("\tVERTEX BETWEENNESS CENTRALITY : " +
		// // // betweennessCentrality.getVertexRankScore(vertex));
		// // if
		// //
		// (!listBetweenness.containsKey(betweennessCentrality.getVertexRankScore(vertex)))
		// // {
		// // Set<VertexModel> newSet = new HashSet<VertexModel>();
		// // newSet.add(vertex);
		// //
		// listBetweenness.put(betweennessCentrality.getVertexRankScore(vertex),
		// // newSet);
		// // } else {
		// // Set<VertexModel> oldSet =
		// //
		// listBetweenness.get(betweennessCentrality.getVertexRankScore(vertex));
		// // oldSet.add(vertex);
		// //
		// listBetweenness.put(betweennessCentrality.getVertexRankScore(vertex),
		// // oldSet);
		// // }
		// // // System.out.println("\tEIGENVECTOR CENTRALITY : \t" +
		// // // eigenvectorCentrality.getVertexScore(vertex));
		// // if
		// //
		// (!listEigenvector.containsKey(eigenvectorCentrality.getVertexScore(vertex)))
		// // {
		// // Set<VertexModel> newSet = new HashSet<VertexModel>();
		// // newSet.add(vertex);
		// // listEigenvector.put(eigenvectorCentrality.getVertexScore(vertex),
		// // newSet);
		// // } else {
		// // Set<VertexModel> oldSet =
		// // listEigenvector.get(eigenvectorCentrality.getVertexScore(vertex));
		// // oldSet.add(vertex);
		// // listEigenvector.put(eigenvectorCentrality.getVertexScore(vertex),
		// // oldSet);
		// // }
		// //
		// // // vertex.setPageRankScore(pageRanker.getVertexScore(vertex));
		// // // vertex.setDegreeScore(degreeScorer.getVertexScore(vertex));
		// // //
		// //
		// vertex.setClosenessCentrality(closenessCentrality.getVertexScore(vertex));
		// // //
		// //
		// vertex.setBetweennessCentrality(betweennessCentrality.getVertexRankScore(vertex));
		// // //
		// //
		// vertex.setEigenvectorCentrality(eigenvectorCentrality.getVertexScore(vertex));
		// // // System.out.println();
		// // }
		//
		// // System.out.println("UNDIRECTED NETWORK");
		// for (Entry<String, VertexModel> entry :
		// mapNomLocuteurVertex.entrySet()) {
		// VertexModel vertex = entry.getValue();
		// // System.out.println(vertex);
		// // System.out.println("\tPAGE RANKER : \t\t\t" +
		// // pageRankerUndirected.getVertexScore(vertex));
		// if
		// (!listPageRankUndirected.containsKey(pageRankerUndirected.getVertexScore(vertex)))
		// {
		// Set<VertexModel> newSet = new HashSet<VertexModel>();
		// newSet.add(vertex);
		// listPageRankUndirected.put(pageRankerUndirected.getVertexScore(vertex),
		// newSet);
		// } else {
		// Set<VertexModel> oldSet =
		// listPageRankUndirected.get(pageRankerUndirected.getVertexScore(vertex));
		// oldSet.add(vertex);
		// listPageRankUndirected.put(pageRankerUndirected.getVertexScore(vertex),
		// oldSet);
		// }
		//
		// // System.out.println("\tDEGREE SCORER : \t\t" +
		// // degreeScorer.getVertexScore(vertex));
		// if
		// (!listDegreeUndirected.containsKey(degreeScorerUndirected.getVertexScore(vertex)))
		// {
		// Set<VertexModel> newSet = new HashSet<VertexModel>();
		// newSet.add(vertex);
		// listDegreeUndirected.put(degreeScorerUndirected.getVertexScore(vertex),
		// newSet);
		// } else {
		// Set<VertexModel> oldSet =
		// listDegreeUndirected.get(degreeScorerUndirected.getVertexScore(vertex));
		// oldSet.add(vertex);
		// listDegreeUndirected.put(degreeScorerUndirected.getVertexScore(vertex),
		// oldSet);
		// }
		//
		// // System.out.println("\tCLOSENESS CENTRALITY : \t\t" +
		// // closenessCentrality.getVertexScore(vertex));
		// if
		// (!listClosenessUndirected.containsKey(closenessCentralityUndirected.getVertexScore(vertex)))
		// {
		// Set<VertexModel> newSet = new HashSet<VertexModel>();
		// newSet.add(vertex);
		// listClosenessUndirected.put(closenessCentralityUndirected.getVertexScore(vertex),
		// newSet);
		// } else {
		// Set<VertexModel> oldSet =
		// listClosenessUndirected.get(closenessCentralityUndirected.getVertexScore(vertex));
		// oldSet.add(vertex);
		// listClosenessUndirected.put(closenessCentralityUndirected.getVertexScore(vertex),
		// oldSet);
		// }
		// // System.out.println("\tVERTEX BETWEENNESS CENTRALITY : " +
		// // betweennessCentrality.getVertexRankScore(vertex));
		// if
		// (!listBetweennessUndirected.containsKey(betweennessCentralityUndirected.getVertexRankScore(vertex)))
		// {
		// Set<VertexModel> newSet = new HashSet<VertexModel>();
		// newSet.add(vertex);
		// listBetweennessUndirected.put(betweennessCentralityUndirected.getVertexRankScore(vertex),
		// newSet);
		// } else {
		// Set<VertexModel> oldSet =
		// listBetweennessUndirected.get(betweennessCentralityUndirected.getVertexRankScore(vertex));
		// oldSet.add(vertex);
		// listBetweennessUndirected.put(betweennessCentralityUndirected.getVertexRankScore(vertex),
		// oldSet);
		// }
		// // System.out.println("\tEIGENVECTOR CENTRALITY : \t" +
		// // eigenvectorCentrality.getVertexScore(vertex));
		// if
		// (!listEigenvectorUndirected.containsKey(eigenvectorCentralityUndirected.getVertexScore(vertex)))
		// {
		// Set<VertexModel> newSet = new HashSet<VertexModel>();
		// newSet.add(vertex);
		// listEigenvectorUndirected.put(eigenvectorCentralityUndirected.getVertexScore(vertex),
		// newSet);
		// } else {
		// Set<VertexModel> oldSet =
		// listEigenvectorUndirected.get(eigenvectorCentralityUndirected.getVertexScore(vertex));
		// oldSet.add(vertex);
		// listEigenvectorUndirected.put(eigenvectorCentralityUndirected.getVertexScore(vertex),
		// oldSet);
		// }
		// }
		// // System.out.println();
		// // System.out.println("DIRECTED GRAPH");
		// // System.out.println("PAGE RANK");
		// // for (Entry<Double, Set<VertexModel>> entry :
		// listPageRank.entrySet())
		// // System.out.println(entry.getValue() + " (" + entry.getKey() +
		// ")");
		// // System.out.println();
		// // System.out.println("DEGREE");
		// // for (Entry<Integer, Set<VertexModel>> entry :
		// listDegree.entrySet())
		// // System.out.println(entry.getValue() + " (" + entry.getKey() +
		// ")");
		// // System.out.println();
		// // System.out.println("CLOSENESS");
		// // for (Entry<Double, Set<VertexModel>> entry :
		// // listCloseness.entrySet())
		// // System.out.println(entry.getValue() + " (" + entry.getKey() +
		// ")");
		// // System.out.println();
		// // System.out.println("BETWEENNESS");
		// // for (Entry<Double, Set<VertexModel>> entry :
		// // listBetweenness.entrySet())
		// // System.out.println(entry.getValue() + " (" + entry.getKey() +
		// ")");
		// // System.out.println();
		// // System.out.println("EIGENVECTOR");
		// // for (Entry<Double, Set<VertexModel>> entry :
		// // listEigenvector.entrySet())
		// // System.out.println(entry.getValue() + " (" + entry.getKey() +
		// ")");
		// // System.out.println();
		//
		// System.out.println("UNDIRECTED GRAPH");
		// System.out.println("PAGE RANK");
		// for (Entry<Double, Set<VertexModel>> entry :
		// listPageRankUndirected.entrySet())
		// System.out.println(entry.getValue() + " (" + entry.getKey() + ")");
		// System.out.println();
		// System.out.println("DEGREE");
		// for (Entry<Integer, Set<VertexModel>> entry :
		// listDegreeUndirected.entrySet())
		// System.out.println(entry.getValue() + " (" + entry.getKey() + ")");
		// System.out.println();
		// System.out.println("CLOSENESS");
		// for (Entry<Double, Set<VertexModel>> entry :
		// listClosenessUndirected.entrySet())
		// System.out.println(entry.getValue() + " (" + entry.getKey() + ")");
		// System.out.println();
		// System.out.println("BETWEENNESS");
		// for (Entry<Double, Set<VertexModel>> entry :
		// listBetweennessUndirected.entrySet())
		// System.out.println(entry.getValue() + " (" + entry.getKey() + ")");
		// System.out.println();
		// System.out.println("EIGENVECTOR");
		// for (Entry<Double, Set<VertexModel>> entry :
		// listEigenvectorUndirected.entrySet())
		// System.out.println(entry.getValue() + " (" + entry.getKey() + ")");
		// System.out.println();
		//
		// // for (EdgeModel edge : directedGraph.getEdges())
		// // System.out.println("EDGES BETWEENNESS CENTRALITY : " + edge +
		// " = " +
		// // betweennessCentrality.getEdgeRankScore(edge));

	}

	public UndirectedSparseGraph<VertexModel, EdgeModel> getUndirectedGraph() {
		return undirectedGraph;
	}

	// public DirectedSparseGraph<VertexModel, EdgeModel> getDirectedGraph() {
	// return directedGraph;
	// }
}
