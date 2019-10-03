package extra;

import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.table.AbstractTableModel;

import modeles.tableaux.TabConversationsModel;
import modeles.tableaux.TabLocuteursModel;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class Pdf {
	private String[] headers;
	// private TabLocuteursModel modelTabL;
	// private TabConversationsModel modelTabC;
	private AbstractTableModel model;
	private String fichier, title, titleTab, txt;

	public Pdf(String fichier, String title, String txt, String titleTab,
			String[] headers, TabLocuteursModel model) {
		this.fichier = fichier;
		this.title = title;
		this.headers = headers;
		this.model = model;
		this.txt = txt;
		this.titleTab = titleTab;
	}

	public Pdf(String fichier, String title, String txt, String titleTab,
			String[] headers, TabConversationsModel model) {
		this.fichier = fichier;
		this.title = title;
		this.headers = headers;
		this.model = model;
		this.txt = txt;
		this.titleTab = titleTab;
	}

	public void createPdf() {
		Document document = new Document(PageSize.A4.rotate());
		try {
			PdfWriter.getInstance(document, new FileOutputStream(fichier));
			document.open();
			// Propriétés du document
			// Cellules
			PdfPCell cell = null;
			Font font = new Font();
			// Police
			font = FontFactory.getFont(FontFactory.COURIER, Font.DEFAULTSIZE,
					Font.NORMAL);

			// Paragraphe : Titre
			Paragraph par = new Paragraph(title);
			par.getFont().setStyle(Font.BOLD);
			document.add(par);

			// Paragraphe : stats en texte
			par = new Paragraph(txt);
			document.add(par);

			// Nouvelle page
			document.newPage();

			// Paragraphe : titre tableau
			par = new Paragraph(titleTab);
			par.getFont().setStyle(Font.BOLD);
			document.add(par);

			// Tableau
			PdfPTable table = new PdfPTable(headers.length);
			table.setSpacingBefore(20f);
			table.setWidthPercentage(100);

			for (int i = 0; i < headers.length; i++) {
				String header = headers[i];
				if (header.indexOf("<html><center>") != -1) {
					header = header.replace("<html><center>", "");
					header = header.replace("</center></html>", "");
					if (header.indexOf("<br>") != -1) {
						header = header.replace("<br>", " ");
						header = header.replace("</br>", "");
					}
				}
				cell = new PdfPCell(new Paragraph(header, font));
				cell.setBackgroundColor(new BaseColor(255, 200, 200));
				table.addCell(cell);
			}
			cell.setBackgroundColor(new BaseColor(255, 200, 200));
			for (int row = 0; row < model.getRowCount(); row++) {
				for (int col = 0; col < model.getColumnCount(); col++) {
					Object value = model.getValueAt(row, col);
					// System.out.println(value.getClass());
					if (value.getClass() == Float.class) {
						DecimalFormat df = new DecimalFormat("0.00");
						value = df.format(value);
					} else if (value.getClass() == Date.class) {
						DateFormat formatter = new SimpleDateFormat(
								"dd/MM/yyyy");
						value = formatter.format(value);
					}
					cell = new PdfPCell(new Phrase(value.toString(), font));
					table.addCell(cell);
				}
			}
			document.add(table);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (document.isOpen()) {
				document.close();
			}
		}
	}
}
