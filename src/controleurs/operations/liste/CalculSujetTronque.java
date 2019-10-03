package controleurs.operations.liste;

import java.text.Normalizer;

public class CalculSujetTronque {

	private String sujet;

	public CalculSujetTronque(String sujet) {
		this.sujet = sujet;
	}

	public String getSujetTronque() {
		String sujetTronque3 = null;
		if (sujet == null || sujet.length() == 0)
			sujetTronque3 = "";
		else {
			String sujetTronque = sujet;
			// System.out.println("sujet tronqué = "+sujetTronque);
			String sujetTronque2 = sujetTronque.replaceAll("\\p{Punct}|\\s", "");
			sujetTronque3 = Normalizer.normalize(sujetTronque2, Normalizer.Form.NFD);
			sujetTronque3 = sujetTronque3.replaceAll("[^\\p{ASCII}]", "");
			sujetTronque3 = sujetTronque3.toUpperCase();
			if (sujetTronque3.length() > 15)
				sujetTronque3 = sujetTronque3.substring(0, 15);
		}
		// System.out.println("sujet tronqué modifié = "+sujetTronque3);
		return sujetTronque3;
	}
}
