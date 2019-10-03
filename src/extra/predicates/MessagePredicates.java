package extra.predicates;

import java.text.Collator;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Pattern;

import modeles.MessageModel;

public class MessagePredicates {

	private static DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

	public MessagePredicates() {
	}

	public static Predicate<MessageModel> supDate1(final Date date1Sel) {
		return new Predicate<MessageModel>() {
			@Override
			public boolean eval(MessageModel message) {
				boolean trouveDate1Sel = false;
				String sDateM = formatter.format(message.getDateUS());
				Date dateM = null;
				try {
					dateM = formatter.parse(sDateM);
					// System.out.println("date message = "+date);
					if (dateM.equals(date1Sel) || dateM.after(date1Sel)) {
						trouveDate1Sel = true;
						System.out.println("message " + message.getNumero() + " trouve (" + dateM + " egal ou apres " + date1Sel + ")");
					}
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				return trouveDate1Sel;
			}
		};
	}

	public static Predicate<MessageModel> infDate2(final Date date2Sel) {
		return new Predicate<MessageModel>() {
			@Override
			public boolean eval(MessageModel message) {
				boolean trouveDate2Sel = false;
				String sDateM = formatter.format(message.getDateUS());
				Date dateM = null;
				try {
					dateM = formatter.parse(sDateM);
					// System.out.println("date message = "+date);
					if (dateM.equals(date2Sel) || dateM.after(date2Sel)) {
						trouveDate2Sel = true;
						System.out.println("message " + message.getNumero() + " trouve (" + dateM + " egal ou avant " + date2Sel + ")");
					}
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				return trouveDate2Sel;
			}
		};
	}
	
	public static Predicate<MessageModel> supDate1EtInfDate2(final Date date1Sel, final Date date2Sel) {
		return new Predicate<MessageModel>() {
			@Override
			public boolean eval(MessageModel message) {
				boolean trouveDate1Date2Sel = false;
				String sDateM = formatter.format(message.getDateUS());
				Date dateM = null;
				try {
					dateM = formatter.parse(sDateM);
					// System.out.println("date message = "+date);
					if ((dateM.equals(date1Sel) || dateM.after(date1Sel)) && (dateM.equals(date2Sel) || dateM.before(date2Sel))) {
						trouveDate1Date2Sel = true;
						System.out.println("message " + message.getNumero() + " trouve (" + dateM + " comprise entre " + date1Sel + " et " + date2Sel + ")");
					}
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				return trouveDate1Date2Sel;
			}
		};
	}

	public static Predicate<MessageModel> findNumero(final String numerosMessagesSel, final int firstNumM, final int lastNumM) {
		return new Predicate<MessageModel>() {
			@Override
			public boolean eval(MessageModel message) {
				boolean trouveNumMessage = false;
				String numM = message.getNumero();
				int iNumM = Integer.parseInt(numM);
				String[] tabNumerosSelectionnes = null;
				if (numerosMessagesSel.indexOf(";") != -1)
					tabNumerosSelectionnes = numerosMessagesSel.split(";");
				else {
					tabNumerosSelectionnes = new String[1];
					tabNumerosSelectionnes[0] = numerosMessagesSel;
				}

				for (int j = 0; j < tabNumerosSelectionnes.length; j++) {
					String numMSel = tabNumerosSelectionnes[j];
					// PLAGE
					if (numMSel.indexOf("-") != -1) {
						String[] tabPlageSel = numMSel.split("-");
						String borneNumInfSel = tabPlageSel[0].trim();
						int iBorneNumInfSel;
						if (borneNumInfSel.equals("*")) {
							iBorneNumInfSel = firstNumM;
						} else {
							iBorneNumInfSel = Integer.parseInt(borneNumInfSel);
						}
						String borneNumSupSel = tabPlageSel[1].trim();
						int iBorneNumSupSel;
						if (borneNumSupSel.equals("*")) {
							iBorneNumSupSel = lastNumM;
						} else {
							iBorneNumSupSel = Integer.parseInt(borneNumSupSel);
						}
						if (iNumM >= iBorneNumInfSel && iNumM <= iBorneNumSupSel) {
							trouveNumMessage = true;
							System.out.println("message " + message.getNumero() + " trouve (" + numM + " est dans la plage " + iBorneNumInfSel + "-" + iBorneNumSupSel + ")");
						}
					} else {
						int iNumMSel = Integer.parseInt(numMSel);
						if (iNumM == iNumMSel) {
							trouveNumMessage = true;
							System.out.println("message " + message.getNumero() + " trouve (" + numM + " = " + numMSel + ")");
						}
					}
				}
				return trouveNumMessage;
			}
		};
	}

	public static Predicate<MessageModel> findMotDansSujet(final String motsSujetSel, final boolean etOuMotsSujetSel) {
		return new Predicate<MessageModel>() {
			@Override
			public boolean eval(MessageModel message) {
				Collator compareOperator = Collator.getInstance(Locale.FRENCH);
				compareOperator.setStrength(Collator.PRIMARY);
				boolean trouveMotsDansSujet = false;
				String sujetM = message.getSujet();
				String[] tabMotsSujet = Pattern.compile("\\W+").split(sujetM);
				String[] tabMotsSujetSelectionnes = null;
				if (motsSujetSel.indexOf(";") != -1)
					tabMotsSujetSelectionnes = motsSujetSel.split(";");
				else {
					tabMotsSujetSelectionnes = new String[1];
					tabMotsSujetSelectionnes[0] = motsSujetSel;
				}
				// int nbreMotsSujetSelectionnes =
				// tabMotsSujetSelectionnes.length;
				// System.out
				// .println("nbre de mots dans messages selectionnes = "
				// + nbreMotsSujetSelectionnes);
				// SI OU
				if (etOuMotsSujetSel == false) {
					for (int k = 0; k < tabMotsSujetSelectionnes.length; k++) {
						String motSujetSel = tabMotsSujetSelectionnes[k];
						boolean ajout = false;
						System.out.println("motSujetSel = " + motSujetSel);
						for (int j = 0; j < tabMotsSujet.length; j++) {
							String motSujet = tabMotsSujet[j];
							int result = compareOperator.compare(motSujetSel, motSujet);
							if (result == 0) {
								trouveMotsDansSujet = true;
								ajout = true;
								System.out.println("message " + message.getNumero() + " trouve (" + motSujetSel + " present dans " + sujetM + ")");
								break;
							}
						}
						if (ajout == true)
							break;
					}
				} else {
					// SI ET
					int l = 0;
					for (int m = 0; m < tabMotsSujetSelectionnes.length; m++) {
						String motSujetSel = tabMotsSujetSelectionnes[m];
						// System.out.println("motSujetSel = "+motSujetSel);
						for (int k = 0; k < tabMotsSujet.length; k++) {
							String motSujet = tabMotsSujet[k];
							// System.out.println("	motSujet = "+motSujet);
							int result = compareOperator.compare(motSujetSel, motSujet);
							// System.out.println("	result = "+result);
							if (result == 0) {
								l++;
								break;
							}
						}
					}
					// System.out.println("l = " + l);
					// System.out
					// .println("taille de tabMotsSujetSelectionnes = "
					// + tabMotsSujetSelectionnes.length);
					if (l == tabMotsSujetSelectionnes.length) {
						trouveMotsDansSujet = true;
						System.out.println("message " + message.getNumero() + " trouve (" + motsSujetSel + " presents dans " + sujetM + ")");

					} else {
						if (sujetM.indexOf(motsSujetSel) != -1) {
							trouveMotsDansSujet = true;
							System.out.println("message " + message.getNumero() + " trouve (" + motsSujetSel + " present dans " + sujetM + ")");
						}
					}
				}
				return trouveMotsDansSujet;
			}
		};
	}

	public static Predicate<MessageModel> findMotDansCorps(final String motsCorpsSel, final boolean etOuMotsCorpsSel, final boolean nonIntegrationMessageOriginalMotsCorpsSel) {
		return new Predicate<MessageModel>() {
			@Override
			public boolean eval(MessageModel message) {
				Collator compareOperator = Collator.getInstance(Locale.FRENCH);
				compareOperator.setStrength(Collator.PRIMARY);
				boolean trouveMotsDansCorps = false;
				String corpsM = message.getCorps();
				if (nonIntegrationMessageOriginalMotsCorpsSel == true)
					corpsM = message.getSsOriginalMessage(corpsM);
				String[] tabMotsCorps = Pattern.compile("\\W+").split(corpsM);

				if (motsCorpsSel.indexOf(";") != -1) {
					String[] tabMotsCorpsSelectionnes = motsCorpsSel.split(";");
					// int nbreMotsSujetSelectionnes =
					// tabMotsSujetSelectionnes.length;
					// System.out
					//
					// .println("nbre de mots dans messages selectionnes = "
					// + nbreMotsSujetSelectionnes);
					// SI OU
					if (etOuMotsCorpsSel == false) {
						for (int p = 0; p < tabMotsCorpsSelectionnes.length; p++) {
							String motCorpsSel = tabMotsCorpsSelectionnes[p];
							boolean ajout = false;
							for (int q = 0; q < tabMotsCorps.length; q++) {
								String motCorps = tabMotsCorps[q];
								int result = compareOperator.compare(motCorpsSel, motCorps);
								if (result == 0) {
									trouveMotsDansCorps = true;
									ajout = true;
									System.out.println("####################################################################");
									System.out.println("message " + message.getNumero() + " trouve (" + motCorpsSel + " present dans " + corpsM + ")");
									System.out.println("####################################################################");
									break;
								}
							}
							if (ajout == true)
								break;
						}
					} else {
						// SI ET
						int r = 0;
						for (int s = 0; s < tabMotsCorpsSelectionnes.length; s++) {
							String motCorpsSel = tabMotsCorpsSelectionnes[s];
							// System.out.println("motSujetSel = "+motSujetSel);
							for (int t = 0; t < tabMotsCorps.length; t++) {
								String motCorps = tabMotsCorps[t];
								// System.out.println("	motSujet = "+motSujet);
								int result = compareOperator.compare(motCorpsSel, motCorps);
								// System.out.println("	result = "+result);
								if (result == 0) {
									r++;
									break;
								}
							}
						}
						// System.out.println("r = " + r);
						// System.out
						// .println("taille de tabMotsSujetSelectionnes = "
						// + tabMotsCorpsSelectionnes.length);
						if (r == tabMotsCorpsSelectionnes.length) {
							trouveMotsDansCorps = true;
							// System.out.println("message " +
							// message.getNumero()
							// + " trouve (" + motsCorpsSel
							// + " presents dans " + corpsM + ")");
						}
					}
				} else {
					if (corpsM.indexOf(motsCorpsSel) != -1) {
						trouveMotsDansCorps = true;
						System.out.println("####################################################################");
						System.out.println("message " + message.getNumero() + " trouve (" + motsCorpsSel + " present dans " + corpsM + ")");
						System.out.println("####################################################################");
					}
				}
				return trouveMotsDansCorps;
			}
		};
	}

}
