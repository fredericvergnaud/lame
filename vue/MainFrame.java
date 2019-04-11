package vue;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicTabbedPaneUI;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel mainPanel, panelToolBar, panelSup;
	private JTabbedPane mainOnglet;

	public MainFrame() {
		buildFrame();
	}

	private void buildFrame() {

		// PANEL TOOLBAR
		panelToolBar = new JPanel();
		GridLayout toolBarLayout = new GridLayout(1, 1);
		panelToolBar.setLayout(toolBarLayout);
		panelToolBar.setPreferredSize(new Dimension(1330, 30));
		panelToolBar.setBackground(Color.WHITE);

		// PANEL SUPERIEUR
		panelSup = new JPanel();
		panelSup.setOpaque(true);
		GridLayout panelSupLayout = new GridLayout(1, 3);
		panelSup.setLayout(panelSupLayout);
		panelSup.setPreferredSize(new Dimension(1330, 100));
		panelSup.setBackground(Color.WHITE);

		// PANEL INFERIEUR + ONGLET
		JPanel panelInf = new JPanel(new GridLayout(1, 1));
		panelInf.setOpaque(true);
		panelInf.setBackground(Color.WHITE);
		panelInf.setAlignmentY(Component.CENTER_ALIGNMENT);
		mainOnglet = new JTabbedPane();
		mainOnglet.setOpaque(true);
		mainOnglet.setName("mainOnglet");
		mainOnglet.setBackground(Color.WHITE);
		// mainOnglet.setTabPlacement(JTabbedPane.LEFT);
		mainOnglet.setUI(new BasicTabbedPaneUI() {
			@Override
			protected void installDefaults() {
				super.installDefaults();
				// highlight = Color.pink;
				lightHighlight = Color.LIGHT_GRAY;
				// shadow = Color.red;
				darkShadow = Color.LIGHT_GRAY;
				focus = Color.WHITE;
			}
		});
		panelInf.add(mainOnglet);

		// SPLITPANE PRINCIPAL
		JSplitPane mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, panelSup, panelInf);
		mainSplitPane.setBackground(Color.WHITE);
		mainSplitPane.setDividerLocation(130);
		mainSplitPane.setDividerSize(0);

		// Main Panel
		mainPanel = new JPanel();
		mainPanel.setPreferredSize(new Dimension(1330, 1000));
		mainPanel.setBackground(Color.WHITE);
		mainPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.WHITE));
		BorderLayout mainPanelLayout = new BorderLayout();
		mainPanel.setLayout(mainPanelLayout);

		mainPanel.add(panelToolBar, BorderLayout.PAGE_START);
		mainPanel.add(mainSplitPane, BorderLayout.CENTER);

		// INTEGRATION DANS LA FRAME
		// add(panelToolBar);
		setContentPane(mainPanel);

		setPreferredSize(new Dimension(1330, 1000));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
//		setResizable(false);
//		addComponentListener(new ComponentAdapter() {
//
//			@Override
//			public void componentResized(ComponentEvent e) {
//				setSize(new Dimension(1300, getHeight()));
//				super.componentResized(e);
//			}
//
//		});
		setVisible(true);
	}

	public JPanel getPanelSup() {
		return panelSup;
	}

	public JTabbedPane getOnglet() {
		return mainOnglet;
	}

	public JPanel getPanelToolBar() {
		return panelToolBar;
	}

	public JPanel getMainPanel() {
		return mainPanel;
	}
}
