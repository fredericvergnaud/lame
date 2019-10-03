package extra.predicates;

import modeles.LocuteurModel;

public class LocuteurPredicates {

	public LocuteurPredicates() {

	}

	public static Predicate<LocuteurModel> findAuteur(final String[] tabLocuteursSelectionnes) {
		return new Predicate<LocuteurModel>() {
			@Override
			public boolean eval(LocuteurModel locuteur) {
				boolean trouveLocuteurSel = false;
				for (int i = 0; i < tabLocuteursSelectionnes.length; i++) {
					String nomLSel = tabLocuteursSelectionnes[i];
					if (locuteur.getNom().equals(nomLSel)) {
						trouveLocuteurSel = true;
						break;
					}
				}
				return trouveLocuteurSel;
			}
		};
	}
}
