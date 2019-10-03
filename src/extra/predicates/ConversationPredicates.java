package extra.predicates;

import modeles.ConversationModel;

public class ConversationPredicates {

	public ConversationPredicates() {

	}

	public static Predicate<ConversationModel> findNbreLocuteurs1(final int nbreLocuteurs1Sel) {
		return new Predicate<ConversationModel>() {
			@Override
			public boolean eval(ConversationModel conversation) {
				boolean trouveNbreLocuteurs1 = false;
				int nbreLocuteurs = conversation.getNbreLocuteurs();
				if (nbreLocuteurs >= nbreLocuteurs1Sel) {
					trouveNbreLocuteurs1 = true;
					System.out.println("Conversation numéro " + conversation.getId() + " trouvé : " + nbreLocuteurs + " >= " + nbreLocuteurs1Sel);
				}
				return trouveNbreLocuteurs1;
			}
		};
	}

	public static Predicate<ConversationModel> findNbreLocuteurs2(final int nbreLocuteurs2Sel) {
		return new Predicate<ConversationModel>() {
			@Override
			public boolean eval(ConversationModel conversation) {
				boolean trouveNbreLocuteurs2 = false;
				int nbreLocuteurs = conversation.getNbreLocuteurs();
				if (nbreLocuteurs <= nbreLocuteurs2Sel) {
					trouveNbreLocuteurs2 = true;
					System.out.println("Conversation numéro " + conversation.getId() + " trouvé : " + nbreLocuteurs + " <= " + nbreLocuteurs2Sel);
				}
				return trouveNbreLocuteurs2;
			}
		};
	}

	public static Predicate<ConversationModel> findNbreMessages1(final int nbreMessages1Sel) {
		return new Predicate<ConversationModel>() {
			@Override
			public boolean eval(ConversationModel conversation) {
				boolean trouveNbreMessages1 = false;
				int nbreMessages = conversation.getNbreMessages();
				if (nbreMessages >= nbreMessages1Sel) {
					trouveNbreMessages1 = true;
					System.out.println("Conversation numéro " + conversation.getId() + " trouvé : " + nbreMessages + " >= " + nbreMessages1Sel);
				}

				return trouveNbreMessages1;
			}
		};
	}

	public static Predicate<ConversationModel> findNbreMessages2(final int nbreMessages2Sel) {
		return new Predicate<ConversationModel>() {
			@Override
			public boolean eval(ConversationModel conversation) {
				boolean trouveNbreMessages2 = false;
				int nbreMessages = conversation.getNbreMessages();
				if (nbreMessages <= nbreMessages2Sel) {
					trouveNbreMessages2 = true;
					System.out.println("Conversation numéro " + conversation.getId() + " trouvé : " + nbreMessages + " <= " + nbreMessages2Sel);
				}

				return trouveNbreMessages2;
			}
		};
	}
}
