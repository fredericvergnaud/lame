package vue.projetPanelInf;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import vue.ToolBar;

public class LamePanel extends JPanel {
	
	private static final long serialVersionUID = 1L;

	public LamePanel(){
		
	}
	
	public JPanel getPanel() {
		JPanel panel = new JPanel();
		JLabel label = new JLabel();
		label.setIcon(new ImageIcon(ToolBar.class.getResource("/images/icones/lame.jpg")));
        panel.add(label);
		return panel;
	}

}
