package vue;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;
import modeles.evenements.ProjetChangedEvent;
import modeles.evenements.ProjetListeAddedEvent;
import controleurs.ProjetController;
import controleurs.vuesabstraites.ProjetView;

public class Activites extends ProjetView {

	private double progressStart = 0;
	private double progressEnd = 100;
	private double step;
	private JLabel labelAction;
	private JTextArea txtArea;
	private JProgressBar pb;
	private JFrame frame;
	private JPanel panel;
	private File logFile;
	private FileWriter sortie;
	private DateFormat format = new SimpleDateFormat("dd/MM/yyyy H:mm");
	private ResourceBundle activites;

	public Activites(ProjetController controller, ResourceBundle activites) {
		super(controller);
		this.activites = activites;
		create();
	}

	public JTextArea getTxtArea() {
		return txtArea;
	}

	public JPanel getPanel() {
		return panel;
	}

	@Override
	public JProgressBar getProgressBar() {
		return pb;
	}

	public void create() {
		frame = new JFrame();
		frame.setTitle(activites.getString("txt_Activites"));
		JPanel panel = new JPanel(new FlowLayout());
		panel.setOpaque(true);
		panel.setAlignmentY(Component.LEFT_ALIGNMENT);

		GridBagLayout layout = new GridBagLayout();
		JPanel panelAction = new JPanel();
		panelAction.setPreferredSize(new Dimension(440, 20));
		panelAction.setLayout(layout);
		panelAction.setAlignmentY(Component.LEFT_ALIGNMENT);
		GridBagConstraints c = new GridBagConstraints();

		labelAction = new JLabel(" ");
		labelAction.setPreferredSize(new Dimension(438, 20));

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		panelAction.add(labelAction, c);

		GridBagLayout layout1 = new GridBagLayout();
		JPanel panelProgress = new JPanel();
		panelProgress.setLayout(layout1);
		GridBagConstraints c1 = new GridBagConstraints();

		JLabel labelAvancement = new JLabel(activites.getString("txt_Etat")
				+ " : ");

		c1.fill = GridBagConstraints.HORIZONTAL;
		c1.gridx = 0;
		c1.gridy = 1;
		panelProgress.add(labelAvancement, c1);

		pb = new JProgressBar(0, 100);
		pb.setPreferredSize(new Dimension(395, 25));
		pb.setStringPainted(true);
		pb.setForeground(Color.WHITE);
		pb.setValue(0);
		
		c1.fill = GridBagConstraints.HORIZONTAL;
		c1.gridx = 1;
		c1.gridy = 1;
		panelProgress.add(pb, c1);

		txtArea = new JTextArea();
		txtArea.setEditable(false);
		txtArea.setLineWrap(true);
		txtArea.setWrapStyleWord(true);
		txtArea.setForeground(Color.LIGHT_GRAY);
		// txtAppend.setPreferredSize(new Dimension(440, 210));

		JScrollPane scrollTxtAppend = new JScrollPane(txtArea);
		scrollTxtAppend.setAutoscrolls(true);
		scrollTxtAppend.setBorder(new TitledBorder(activites
				.getString("txt_Details")));
		scrollTxtAppend.setPreferredSize(new Dimension(440, 210));

		GridBagLayout layout2 = new GridBagLayout();
		JPanel panelDetails = new JPanel();
		panelDetails.setPreferredSize(new Dimension(440, 210));
		panelDetails.setLayout(layout2);
		GridBagConstraints c2 = new GridBagConstraints();

		c2.fill = GridBagConstraints.HORIZONTAL;
		c2.gridx = 0;
		c2.gridy = 0;
		panelDetails.add(scrollTxtAppend, c);

		// BOUTONS
		int largeurBouton = 100;
		int hauteurBouton = 25;

		GridBagLayout l8 = new GridBagLayout();
		JPanel panel8 = new JPanel();
		panel8.setLayout(l8);
		GridBagConstraints c8 = new GridBagConstraints();

		JButton bOk = new JButton(activites.getString("txt_Fermer"));
		bOk.setVisible(true);
		bOk.setPreferredSize(new Dimension(largeurBouton, hauteurBouton));
		bOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});

		JButton bEffacer = new JButton(activites.getString("txt_Effacer"));
		bEffacer.setVisible(true);
		bEffacer.setPreferredSize(new Dimension(largeurBouton, hauteurBouton));
		bEffacer.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getTxtArea().setText("");
			}
		});

		c8.gridx = 0;
		c8.gridy = 0;
		panel8.add(bEffacer, c8);

		c8.gridx = 1;
		c8.gridy = 0;
		panel8.add(bOk, c8);

		panel.add(panelAction);
		panel.add(panelProgress);
		panel.add(panelDetails);
		panel.add(panel8);

		frame.setContentPane(panel);
		frame.pack();
		frame.setSize(450, 330);
		frame.setResizable(false);
		frame.setLocation(0, 0);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(
				"./images/csi.jpg"));
		// frame.setVisible(true);
	}

	@Override
	public void appendTxtArea(String txt) {
		txtArea.append(txt);
		txtArea.scrollRectToVisible(new Rectangle(0, txtArea.getHeight() - 2,
				1, 1));
	}

	@Override
	public void updateProgress() {
		progressStart += step;
		pb.setValue((int) progressStart);
		// pb.setValue(i);
	}

	public void changeActionTitle(String newActionTitle) {
		// frame.setTitle(newActionTitle);
	}

	@Override
	public void resetProgress() {
		pb.setValue(0);
	}

	@Override
	public void setLabelProgress(String txtLabelAction) {
		labelAction.setText(txtLabelAction);

	}

	@Override
	public void setStepProgress(int valueDepart) {
		progressStart = 0;
		step = (progressEnd - progressStart) / valueDepart;
	}
	
	@Override
	public void close() {
		frame.setVisible(false);

	}

	@Override
	public JPanel getInfosProjetPanel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JPanel getListesListePanel() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public JMenuBar getMenuBar() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void showActivites() {
		frame.setVisible(true);
	}

	public void logAppendText(String text) {
		try {
			sortie = new FileWriter(logFile, true);
			sortie.append(format.format(new Date()) + " : " + text + " \n");
			sortie.close();
		} catch (IOException e) {
			System.out.println("Log/appendText : erreur d'I/O " + e);
		}
	}

	public File getLogFile() {
		return logFile;
	}

	public void setLogFile(File logFile) {
		this.logFile = logFile;
	}

	public void createLog(ProjetChangedEvent event) {
		logFile = new File(event.getNewRepertoire() + "/" + event.getNewNom()
				+ ".log");
		if (logFile.exists())
			logAppendText(activites.getString("txt_Projet") + " \""
					+ event.getNewNom() + "\" : "
					+ activites.getString("txt_NbreListes") + " = "
					+ event.getNewNbreListes() + " | "
					+ activites.getString("txt_CumulMessages") + " = "
					+ event.getNewCumulMessages() + "\n");
	}

	@Override
	public void projetChanged(ProjetChangedEvent event) {
		resetProgress();
		setLabelProgress(activites.getString("txt_InformationProjet"));
		setStepProgress(1);
		createLog(event);
		appendTxtArea(activites.getString("txt_Projet") + " \""
				+ event.getNewNom() + "\" : "
				+ activites.getString("txt_NbreListes") + " = "
				+ event.getNewNbreListes() + " | "
				+ activites.getString("txt_CumulMessages") + " = "
				+ event.getNewCumulMessages() + "\n");
		updateProgress();

	}

	@Override
	public void projetListeAdded(ProjetListeAddedEvent event) {
		// TODO Auto-generated method stub
		
	}

}
