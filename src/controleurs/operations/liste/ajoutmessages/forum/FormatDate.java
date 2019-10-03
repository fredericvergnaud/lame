package controleurs.operations.liste.ajoutmessages.forum;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.joda.time.DateTime;

public class FormatDate {

	private String dateToFormat;
	
	public FormatDate(String dateToFormat) {
		this.dateToFormat = dateToFormat;
	}

	public Date getDateFormatted() {
		Date dateFormatted = null;

		DateFormat inputFormatEn1 = new SimpleDateFormat("dd MMM yyyy HH:mm", Locale.US);
		DateFormat inputFormatEn2 = new SimpleDateFormat("EEE MMM dd yyyy h:mm a", Locale.US);
		DateFormat inputFormatEn3 = new SimpleDateFormat("MMM dd yyyy hh:mm a", Locale.US);
		DateFormat inputFormatEn4 = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
		DateFormat inputFormatEn5 = new SimpleDateFormat("MMM dd, yyyy, hh:mm:ss a", Locale.US);
		DateFormat inputFormatEn6 = new SimpleDateFormat("EEE MMM dd, yyyy hh:mm a", Locale.US);
		DateFormat inputFormatEn7 = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.US);

		DateFormat inputFormatFr1 = new SimpleDateFormat("dd MMM yyyy HH:mm", Locale.FRENCH);
		DateFormat inputFormatFr2 = new SimpleDateFormat("EEE MMM dd yyyy h:mm a", Locale.FRENCH);
		DateFormat inputFormatFr3 = new SimpleDateFormat("EEE dd MMM yyyy HH:mm", Locale.FRENCH);
		DateFormat inputFormatFr4 = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.FRENCH);
		DateFormat inputFormatFr5 = new SimpleDateFormat("EEE MMM dd, yyyy hh:mm a", Locale.FRENCH);
		DateFormat inputFormatFr6 = new SimpleDateFormat("EEE dd MMM - HH:mm (yyyy)", Locale.FRENCH);
		DateFormat inputFormatFr7 = new SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.FRENCH);
		DateFormat inputFormatFr8 = new SimpleDateFormat("dd MMM yyyy", Locale.FRENCH);
		DateFormat inputFormatFr9 = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.FRENCH);
		DateFormat inputFormatFr10 = new SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH);
		DateFormat inputFormat;
		// System.out.println("Date avant format = " + date);
		inputFormat = inputFormatEn1;
		try {
			dateFormatted = inputFormat.parse(dateToFormat);
		} catch (ParseException e) {
			// e.printStackTrace();
			inputFormat = inputFormatEn2;
			try {
				dateFormatted = inputFormat.parse(dateToFormat);
			} catch (ParseException e4) {
				// e4.printStackTrace();
				inputFormat = inputFormatFr1;
				try {
					String newDate = getDateFormatMois(dateToFormat);
					// System.out.println("formatFr1 : newDate = " +
					// newDate);
					dateFormatted = inputFormat.parse(newDate);
				} catch (ParseException e2) {
					// e2.printStackTrace();
					inputFormat = inputFormatFr2;
					try {
						String newDate = getDateFormatJour(dateToFormat);
						newDate = getDateFormatMois(newDate);
						// System.out.println("formatFr2 : newDate = " +
						// newDate);
						dateFormatted = inputFormat.parse(newDate);
					} catch (ParseException e3) {
						// e3.printStackTrace();
						inputFormat = inputFormatFr3;
						try {
							String newDate = getDateFormatJour(dateToFormat);
							newDate = getDateFormatMois(newDate);
							// System.out.println("formatFr3 : newDate = "
							// + newDate);
							dateFormatted = inputFormat.parse(newDate);
						} catch (ParseException e5) {
							// e5.printStackTrace();
							inputFormat = inputFormatEn3;
							try {
								dateFormatted = inputFormat.parse(dateToFormat);
							} catch (ParseException e6) {
								// e6.printStackTrace();
								inputFormat = inputFormatEn4;
								try {
									dateFormatted = inputFormat.parse(dateToFormat);
								} catch (ParseException e7) {
									// e6.printStackTrace();
									inputFormat = inputFormatFr4;
									try {
										dateFormatted = inputFormat.parse(dateToFormat);
									} catch (ParseException e8) {
										inputFormat = inputFormatEn5;
										try {
											dateFormatted = inputFormat.parse(dateToFormat);
										} catch (ParseException e9) {
											inputFormat = inputFormatFr5;
											try {
												String newDate = getDateFormatJour(dateToFormat);
												newDate = getDateFormatMois(newDate);
												// System.out.println("formatFr2 : newDate = "
												// +
												// newDate);
												dateFormatted = inputFormat.parse(newDate);
											} catch (ParseException e10) {
												inputFormat = inputFormatEn6;
												try {
													dateFormatted = inputFormat.parse(dateToFormat);
												} catch (ParseException e11) {
													inputFormat = inputFormatEn7;
													try {
														dateFormatted = inputFormat.parse(dateToFormat);
													} catch (ParseException e12) {
														inputFormat = inputFormatFr6;
														try {
															String newDate = getDateFormatJour(dateToFormat);
															newDate = getDateFormatMois(newDate);
															// System.out.println("formatFr2 : newDate = "
															// +
															// newDate);
															dateFormatted = inputFormat.parse(newDate);
														} catch (ParseException e13) {
															inputFormat = inputFormatFr7;
															try {
																String newDate = getDateFormatJour(dateToFormat);
																newDate = getDateFormatMois(newDate);
																// System.out.println("formatFr2 : newDate = "
																// +
																// newDate);
																dateFormatted = inputFormat.parse(newDate);
															} catch (ParseException e14) {
																inputFormat = inputFormatFr8;
																try {
																	String newDate = getDateFormatMois(dateToFormat);
																	dateFormatted = inputFormat.parse(newDate);
																} catch (ParseException e15) {
																	inputFormat = inputFormatFr9;
																	try {
																		String newDate = getDateFormatMois(
																				dateToFormat);
																		dateFormatted = inputFormat.parse(newDate);
																	} catch (ParseException e16) {
																		inputFormat = inputFormatFr10;
																		try {
																			String newDate = getDateFormatMois(
																					dateToFormat);
																			dateFormatted = inputFormat.parse(newDate);
																		} catch (ParseException e17) {
																			try {
																				// cas de 1 semaine, 2 mois, 3 ans
																				// ...etc
																				// => batiactu
																				String[] tab = dateToFormat.split(" ");
																				int x = 0;
																				try {
																					x = Integer.parseInt(tab[0].trim());
																				} catch (NumberFormatException nfe) {
																					
																				} finally {
																					if (x != 0) {
																						String s = tab[1].trim();
																						Date today = new Date();
																						// System.out.println("x = " + x
																						// + "
																						// | s = " + s + " | today = " +
																						// today);
																						Calendar c = Calendar
																								.getInstance();
																						c.setTime(today);
																						if (s.equals("jour")
																								|| s.equals("jours"))
																							c.add(Calendar.DATE, -x);
																						else if (s.equals("semaine")
																								|| s.equals("semaines"))
																							c.add(Calendar.DATE,
																									-x * 7);
																						else if (s.equals("mois"))
																							c.add(Calendar.MONTH, -x);
																						else if (s.equals("an")
																								|| s.equals("ans"))
																							c.add(Calendar.YEAR, -x);
																						// System.out.println("c = " +
																						// c.getTime());
																						dateFormatted = c.getTime();
																					}
																				}
																			} catch (Exception e18) {
																				
																			}
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		return dateFormatted;
	}

	private String getDateFormatJour(String date) {
		String[] givenDays = { "Lun", "Mar", "Mer", "Jeu", "Ven", "Sam", "Dim", "Mon", "Tue", "Wed", "Thu", "Fri",
				"Sat", "Sun" };
		String[] realDays = { "Lun.", "Mar.", "Mer.", "Jeu.", "Ven.", "Sam.", "Dim.", "Mon.", "Tue.", "Wed.", "Thu.",
				"Fri.", "Sat.", "Sun." };
		// System.out.println("hello");
		String[] tabDate = date.split(" ");
		if (tabDate.length > 0)
			for (int i = 0; i < givenDays.length; i++) {
				tabDate[0] = tabDate[0].replaceAll(givenDays[i], realDays[i]);
			}
		String newDate = "";
		for (String s : tabDate) {
			// System.out.println(s);
			newDate += s + " ";
		}
		newDate = newDate.trim();
		return newDate;
	}

	private String getDateFormatMois(String date) {
		String[] givenMonths = { "Jan", "Fév", "Mars", "Avr", "Mai", "Juin", "Juil", "Août", "Sep", "Oct", "Nov", "Déc",
				"Mar", "Aoû", "Feb", "Apr", "May", "Jun", "Jul", "Aug", "Dec", "F&eacute;v", "D&eacute;c", "Ao&ucirc;",
				"Ao&ucirc;t" };
		String[] realMonths = { "Janv.", "Févr.", "Mars", "Avr.", "Mai", "Juin", "Juil.", "Août", "Sept.", "Oct.",
				"Nov.", "Déc.", "Mars", "Août", "Feb.", "Apr.", "May.", "Jun.", "Jul.", "Aug.", "Dec.", "Févr.", "Déc.",
				"Août", "Août" };
		String[] tabDate = date.split(" ");
		// System.out.println("tabDate.lenght = " + tabDate.length);
		if (tabDate.length > 2)
			for (int i = 0; i < givenMonths.length; i++) {
				if (tabDate[1].equals(givenMonths[i]))
					tabDate[1] = tabDate[1].replaceAll(givenMonths[i], realMonths[i]);
				else if (tabDate.length > 1)
					if (tabDate[2].equals(givenMonths[i]))
						tabDate[2] = tabDate[2].replaceAll(givenMonths[i], realMonths[i]);
			}
		String newDate = "";
		for (String s : tabDate) {
			// System.out.println(s);
			newDate += s + " ";
		}
		newDate = newDate.trim();
		return newDate;
	}

}
