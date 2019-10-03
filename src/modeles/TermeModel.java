package modeles;

import java.util.HashMap;
import java.util.Map;

public class TermeModel {

	private String mot;
	private Map<String, Integer> mapNumMessageFrequence = new HashMap<String, Integer>();
	private Map<String, Integer> mapIdentifiantMessageFrequence = new HashMap<String, Integer>();
	private String lemme;
	private int frequence = 0;
	private Map<String, Integer> mapCoocFrequence = new HashMap<String, Integer>();
	
	public TermeModel(String mot) {	
		this.mot = mot;
	}
	
	public void addNumMessage(String numMessage) {
		if (!mapNumMessageFrequence.containsKey(numMessage)) {
			mapNumMessageFrequence.put(numMessage, new Integer(1));
		} else {
			int existingFrequence = mapNumMessageFrequence.get(numMessage);
			existingFrequence += 1;
			mapNumMessageFrequence.put(numMessage, existingFrequence);
		}		
	}
	
	public void addIdentifiantMessage(String identifiantMessage) {
		if (!mapIdentifiantMessageFrequence.containsKey(identifiantMessage)) {
			mapIdentifiantMessageFrequence.put(identifiantMessage, new Integer(1));
		} else {
			int existingFrequence = mapIdentifiantMessageFrequence.get(identifiantMessage);
			existingFrequence += 1;
			mapIdentifiantMessageFrequence.put(identifiantMessage, existingFrequence);
		}		
	}

	public String getMot() {
		return mot;
	}

	public void setMot(String mot) {
		this.mot = mot;
	}

	public String getLemme() {
		return lemme;
	}

	public void setLemme(String lemme) {
		this.lemme = lemme;
	}

	public int getFrequence() {
		return frequence;
	}

	public void setFrequence(int frequence) {
		this.frequence = frequence;
	}

	public Map<String, Integer> getMapNumMessageFrequence() {
		return mapNumMessageFrequence;
	}

	public void setMapNumMessageFrequence(
			Map<String, Integer> mapNumMessageFrequence) {
		this.mapNumMessageFrequence = mapNumMessageFrequence;
	}
	
	
	
}
