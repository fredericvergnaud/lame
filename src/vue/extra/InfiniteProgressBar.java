package vue.extra;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.*;

public class InfiniteProgressBar extends JFrame {
	private static final long serialVersionUID = 1L;

	JLabel labAction;
	String title;

	public InfiniteProgressBar(String title) {
		this.title = title;
		createGui();
	}

	public void createGui() {
		JPanel panel = new JPanel(new GridLayout(2, 1));
		JProgressBar pb = new JProgressBar();
		pb.setIndeterminate(true);
		pb.setStringPainted(false);
		pb.setPreferredSize(new Dimension(340, 20));
		pb.setMinimumSize(new Dimension(340, 20));
		pb.setMaximumSize(new Dimension(340, 20));
		panel.setOpaque(true);
		panel.setAlignmentX(Component.CENTER_ALIGNMENT);
		labAction = new JLabel("");
		labAction.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel.add(labAction);
		panel.add(pb);
		setContentPane(panel);
		setTitle(title);
		setResizable(false);
		// setPreferredSize(new Dimension(350, 50));
		setIconImage(Toolkit.getDefaultToolkit().getImage("./images/csi.jpg"));
		pack();
		setVisible(true);
		setLocationRelativeTo(null);
	}

	public void close() {
		dispose();
	}

	public void setAction(String s) {
		labAction.setText(s);
		labAction.repaint();
		// update(getGraphics());
	}

	public void updateGraphics() {
		update(getGraphics());
	}
}