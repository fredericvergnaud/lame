package vue.dialog;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class DialogPanelAjoutForum extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTextField txtAdresseProxyHttp, txtPortProxyHttp, txtAdresseForum, txtSleepTime;
	private JRadioButton bProxy, bNoProxy, bMaj;
	private ResourceBundle bundleOperationsListe;
	private String httpProxyAdress, httpProxyPort, siteUrl;
	private int sleepTime;

	public DialogPanelAjoutForum(ResourceBundle bundleOperationsListe, String httpProxyAdress, String httpProxyPort, String siteUrl, int sleepTime) {
		this.bundleOperationsListe = bundleOperationsListe;
		this.httpProxyAdress = httpProxyAdress;
		this.httpProxyPort = httpProxyPort;
		this.siteUrl = siteUrl;
		this.sleepTime = sleepTime;
		createGui();
	}

	public void createGui() {		
		JPanel panelBProxy = new JPanel();
		panelBProxy.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelBProxy.setOpaque(true);
		panelBProxy.setMinimumSize(new Dimension(400, 30));
		bProxy = new JRadioButton(bundleOperationsListe.getString("txt_Proxy") + " : ");
		panelBProxy.add(bProxy);

		JPanel panelProxyHttp = new JPanel();
		panelProxyHttp.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelProxyHttp.setOpaque(true);
		JLabel labAdresseProxyHttp = new JLabel(bundleOperationsListe.getString("txt_ProxyHttp") + " : ");
		txtAdresseProxyHttp = new JTextField(httpProxyAdress);
		txtAdresseProxyHttp.setPreferredSize(new Dimension(150, 30));
		JLabel labPortProxyHttp = new JLabel(bundleOperationsListe.getString("txt_PortProxy") + " : ");
		txtPortProxyHttp = new JTextField(httpProxyPort);
		txtPortProxyHttp.setPreferredSize(new Dimension(150, 30));

		txtAdresseProxyHttp.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				bProxy.setSelected(true);
				txtAdresseProxyHttp.setEnabled(true);
				txtPortProxyHttp.setEnabled(true);
				bNoProxy.setSelected(false);

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});

		panelProxyHttp.add(labAdresseProxyHttp);
		panelProxyHttp.add(txtAdresseProxyHttp);
		panelProxyHttp.add(labPortProxyHttp);
		panelProxyHttp.add(txtPortProxyHttp);

		JPanel panelBNoProxy = new JPanel();
		panelBNoProxy.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelBNoProxy.setOpaque(true);
		panelBNoProxy.setMinimumSize(new Dimension(400, 30));
		bNoProxy = new JRadioButton(bundleOperationsListe.getString("txt_NoProxy"));
		panelBNoProxy.add(bNoProxy);

		ButtonGroup groupB = new ButtonGroup();
		groupB.add(bProxy);
		groupB.add(bNoProxy);

		bProxy.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				txtAdresseProxyHttp.setEnabled(true);
				txtPortProxyHttp.setEnabled(true);

			}
		});

		bNoProxy.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				txtAdresseProxyHttp.setEnabled(false);
				txtPortProxyHttp.setEnabled(false);
			}
		});

		JPanel panelAdresseForum = new JPanel();
		panelAdresseForum.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelAdresseForum.setOpaque(true);
		JLabel labAdresseForum = new JLabel(bundleOperationsListe.getString("txt_AdresseForum") + " : ");
		txtAdresseForum = new JTextField(siteUrl);
		txtAdresseForum.setPreferredSize(new Dimension(350, 30));
		panelAdresseForum.add(labAdresseForum);
		panelAdresseForum.add(txtAdresseForum);

		JPanel panelSleepTime = new JPanel();
		panelSleepTime.setLayout(new FlowLayout(FlowLayout.LEFT));
		panelSleepTime.setOpaque(true);
		JLabel labSleepTime = new JLabel(bundleOperationsListe.getString("txt_SleepTime") + " : ");
		txtSleepTime = new JTextField(String.valueOf(sleepTime));
		txtSleepTime.setPreferredSize(new Dimension(100, 30));
		panelSleepTime.add(labSleepTime);
		panelSleepTime.add(txtSleepTime);

		JPanel panelProxy = new JPanel();
		panelProxy.setBorder(BorderFactory.createTitledBorder("Proxy"));	
		panelProxy.setLayout(new BoxLayout(panelProxy, BoxLayout.Y_AXIS));
		panelProxy.setOpaque(true);
		panelProxy.setMaximumSize(new Dimension(550,120));
		
		panelProxy.add(panelBNoProxy);
		panelProxy.add(panelBProxy);
		panelProxy.add(panelProxyHttp);
		add(panelProxy);
		add(panelAdresseForum);
		add(panelSleepTime);

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setOpaque(true);
		setAlignmentY(Component.LEFT_ALIGNMENT);
		
		if (!httpProxyAdress.equals("")) {
			bProxy.setSelected(true);
			bNoProxy.setSelected(false);
			txtAdresseProxyHttp.setEnabled(true);
			txtPortProxyHttp.setEnabled(true);
		} else {
			bProxy.setSelected(false);
			bNoProxy.setSelected(true);
			txtAdresseProxyHttp.setEnabled(false);
			txtPortProxyHttp.setEnabled(false);
		}
		
		if (bNoProxy.isSelected()){
			txtAdresseProxyHttp.setEnabled(false);
			txtPortProxyHttp.setEnabled(false);
		} else {
			txtAdresseProxyHttp.setEnabled(true);
			txtPortProxyHttp.setEnabled(true);
		}

		
		
//		bProxy.setSelected(true);
//		txtAdresseProxyHttp.setText("www-proxy.ensmp.fr");
//		txtPortProxyHttp.setText("8080");
//		txtAdresseForum.setText("http://forum.openstreetmap.fr");
//		txtSleepTime.setText("0");
	}

	public String getHttpProxyAdress() {
		return txtAdresseProxyHttp.getText();
	}

	public String getHttpProxyPort() {
		return txtPortProxyHttp.getText();
	}

	public boolean getProxySelected() {
		return bProxy.isSelected();
	}

	public boolean getNoProxySelected() {
		return bNoProxy.isSelected();
	}

	public String getForumAdress() {
		return txtAdresseForum.getText();
	}

	public String getSleepTime() {
		if (txtSleepTime.getText().equals(""))
			return "0";
		else
			return txtSleepTime.getText();
	}
}
