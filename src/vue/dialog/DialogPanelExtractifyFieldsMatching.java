package vue.dialog;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import controleurs.operations.liste.ajoutmessages.extractify.AddMessagesFromExtractify.LameProperty;
import controleurs.operations.liste.ajoutmessages.extractify.AddMessagesFromExtractify.LevelObject;

public class DialogPanelExtractifyFieldsMatching extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private ResourceBundle bundleOperationsListe;
	private List<LevelObject> levels;
	private Map<String, List<LameProperty>> lameTypePropertiesMap;
	private List<JComboBox<Integer>> comboList;

	public DialogPanelExtractifyFieldsMatching(ResourceBundle bundleOperationsListe, List<LevelObject> levels,
			Map<String, List<LameProperty>> lameTypePropertiesMap) {
		this.bundleOperationsListe = bundleOperationsListe;
		this.levels = levels;
		this.lameTypePropertiesMap = lameTypePropertiesMap;
		createGui();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void createGui() {
		JPanel panelFields = new JPanel();
		panelFields.setOpaque(true);
		panelFields.setLayout(new GridBagLayout());
		panelFields.setMinimumSize(new Dimension(450, 220));

		// Combobox
		GridBagConstraints grid = new GridBagConstraints();
		int y = 0;
		comboList = new ArrayList<>();
		for (LevelObject level : levels) {
			String type = level.getType();
			JLabel labType = new JLabel("*" + type);
			labType.setFont(new Font("", Font.BOLD, 12));
			grid.gridx = 0;
			grid.gridy = y;
			grid.anchor = GridBagConstraints.WEST;
			panelFields.add(labType, grid);
			y++;
			List<String> properties = (ArrayList<String>) level.getProperties();
			for (String property : properties) {
				JLabel levelPropertyLabel = new JLabel(property);
				levelPropertyLabel.setBorder(new EmptyBorder(0, 20, 0, 0));
				grid.gridx = 0;
				grid.gridy = y;
				grid.anchor = GridBagConstraints.WEST;
				panelFields.add(levelPropertyLabel, grid);
				grid.gridx = 1;
				grid.gridy = y;
				grid.anchor = GridBagConstraints.WEST;
				JComboBox combo = new JComboBox(new MyComboModel());
				combo.setName(property);
				combo.setRenderer(new LamePropertyListCellRenderer());
				combo.setPreferredSize(new Dimension(200, 25));
				combo.addItem("");
				for (Entry<String, List<LameProperty>> entry : lameTypePropertiesMap.entrySet()) {
					combo.addItem(entry.getKey());
					List<LameProperty> lameProperties = entry.getValue();
					for (LameProperty lameProperty : lameProperties) {
						combo.addItem(lameProperty);
					}
				}
				panelFields.add(combo, grid);
				comboList.add(combo);
				y++;
			}
		}
		JScrollPane scrollPane = new JScrollPane(panelFields);
		scrollPane
				.setBorder(BorderFactory.createTitledBorder(bundleOperationsListe.getString("txt_PropertiesMatching")));
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setPreferredSize(new Dimension(450, 220));
		add(scrollPane);

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setOpaque(true);
		setAlignmentY(Component.LEFT_ALIGNMENT);
		setPreferredSize(new Dimension(450, 220));
	}

	public List<JComboBox<Integer>> getComboList() {
		return comboList;
	}

	private class MyComboModel extends DefaultComboBoxModel<String> {
		public MyComboModel() {
		}

		@Override
		public void setSelectedItem(Object item) {
			if (item.toString().startsWith("*"))
				return;
			super.setSelectedItem(item);
		}
	}

	private class LamePropertyListCellRenderer extends DefaultListCellRenderer {

		private static final long serialVersionUID = 1L;

		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			if (value instanceof LameProperty) {
				LameProperty lameProperty = (LameProperty) value;
				String suffix = lameProperty.getSuffixSelectedItem();
				setText(suffix);
			}
			if (value.toString().startsWith("*"))
				setFont(new Font("", Font.BOLD, 12));
			
			return this;
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}
}