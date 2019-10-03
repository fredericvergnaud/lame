/*
 * L@ME Logiciel d'Analyse de Messages Electroniques
 * 
 * Copyright (c) 2013 Fr�d�ric Vergnaud
 * 
 * frederic.vergnaud@mines-paristech.fr
 * 
 * Licence GNU GENERAL PUBLIC LICENCE Version 3 
 *
 * This program is free software: you can redistribute it and/or modify it 
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or 
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
 * See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License 
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 * 
 */

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Insets;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Painter;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import modeles.ProjetModel;
import vue.MainFrame;
import controleurs.ProjetController;

public class Lame {
	public static void main(String[] args) {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			// If Nimbus is not available, you can set the GUI to another look
			// and feel.
			e.printStackTrace();
		}

		// UI

		// bottom insets is 1 because the tabs are bottom aligned
		UIManager.getLookAndFeelDefaults().put("TabbedPane.contentBorderInsets", new Insets(5, 5, 5, 5));
		UIManager.getLookAndFeelDefaults().put("Panel.background", Color.WHITE);
		UIManager.getLookAndFeelDefaults().put("background", Color.WHITE);
		UIManager.getLookAndFeelDefaults().put("control", Color.WHITE);
		UIManager.getLookAndFeelDefaults().put("FileChooser.background", Color.WHITE);
		UIManager.getLookAndFeelDefaults().put("OptionPane.background", Color.WHITE);
		UIManager.getLookAndFeelDefaults().put("OptionPane.messagebackground", Color.WHITE);
		UIManager.getLookAndFeelDefaults().put("Panel.background", Color.WHITE);
		UIManager.getLookAndFeelDefaults().put("nimbusOrange", new Color(57, 105, 138));
		// UIManager.getLookAndFeelDefaults().put("FileChooser.background",
		// Color.WHITE);
		// UIManager.getLookAndFeelDefaults().put("InternalFrame.background",
		// Color.WHITE);
		// UIManager.getLookAndFeelDefaults().put("FileChooser.disabled",
		// Color.WHITE);
		// UIManager.getLookAndFeelDefaults().put("FileChooser.background",
		// Color.WHITE);

		UIManager.getLookAndFeelDefaults().put("FileChooser[Enabled].backgroundPainter", new Painter<JFileChooser>() {
			@Override
			public void paint(Graphics2D g, JFileChooser object, int width, int height) {
				g.setColor(Color.WHITE);
				g.draw(object.getBounds());

			}
		});
		// UIManager.put("FileChooser[Enabled].backgroundPainter",
		// new Painter<JFileChooser>()
		// {
		// @Override
		// public void paint(Graphics2D g, JFileChooser object, int width, int
		// height)
		// {
		// g.setColor(Color.WHITE);
		// g.draw(object.getBounds());
		//
		// }
		// });

		// UIManager.put("TabbedPane.tabsOverlapBorder", true);

		// UIManager.put("TabbedPane.focus", new Color(0, 0, 0, 0));
		// final Color couleurFoncee = new Color(57, 105, 138);
		// Painter<JComponent> painter = new Painter<JComponent>() {
		//
		// Color color = couleurFoncee;
		//
		// @Override
		// public void paint(Graphics2D g, JComponent c, int width, int height)
		// {
		// g.setColor(color == null ? Color.WHITE : color);
		// g.fillRect(0, 0, width, height);
		//
		// }
		// };

		// Color couleurClaire = new Color(239, 235, 231);
		// UIManager.put("nimbusOrange", couleurFoncee);
		// UIManager.put("TabbedPane.selected", Color.white);
		// UIManager.put("control", couleurClaire);
		// UIManager.put("controlShadow", couleurClaire);
		// UIManager.put("nimbusBase", couleurFoncee);
		// UIManager.put("nimbusFocus", couleurFoncee);
		// UIManager.put("nimbusSelectionBackground", couleurFoncee);
		// UIManager.put("nimbusSelection", couleurFoncee);
		// UIManager.put("textBackground", couleurFoncee);
		// UIManager.put("textHighLight", couleurFoncee);
		// UIManager.put("Menu.disabled", couleurFoncee);
		// UIManager.put("menu", couleurFoncee);
		// UIManager.put("Table.alternateRowColor", Color.white);
		// UIManager.put("Table.showGrid", false);
		// UIManager.put("TableHeader.background", couleurFoncee);
		UIManager.getLookAndFeelDefaults().put("Tree.leafIcon", new ImageIcon(Lame.class.getResource("/images/icones/liste-15.png")));
		UIManager.getLookAndFeelDefaults().put("Tree.openIcon", new ImageIcon(Lame.class.getResource("/images/icones/projet-15.png")));
		UIManager.getLookAndFeelDefaults().put("Tree.closedIcon", new ImageIcon(Lame.class.getResource("/images/icones/projet-ferme-15.png")));

		UIManager.getLookAndFeelDefaults().put("Tree.collapsedIcon", new ImageIcon(Lame.class.getResource("/images/icones/tree-collapsed.png")));
		UIManager.getLookAndFeelDefaults().put("Tree.expandedIcon", new ImageIcon(Lame.class.getResource("/images/icones/tree-expanded.png")));
		
		// choix de la langue
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setOpaque(true);
		panel.setAlignmentY(Component.LEFT_ALIGNMENT);
		panel.setPreferredSize(new Dimension(200, 50));
		String[] tabLang = { "Français", "English" };
		JComboBox<String> listLang = new JComboBox<String>(tabLang);
		listLang.setPreferredSize(new Dimension(150, 30));
		listLang.setMinimumSize(new Dimension(150, 30));
		listLang.setMaximumSize(new Dimension(150, 30));
		listLang.setSelectedIndex(0);
		panel.add(listLang);
		int result = JOptionPane.showOptionDialog(null, panel, "L@ME", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
		if (result == JOptionPane.OK_OPTION) {
			int choixLang = listLang.getSelectedIndex();
			// Creation de la fenetre principale
			MainFrame mainFrame = new MainFrame();
			ProjetModel projet = new modeles.ProjetModel();
			final ProjetController controller = new ProjetController(mainFrame, choixLang, projet);
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					controller.addViews();
				}
			});

		}

	}

}
