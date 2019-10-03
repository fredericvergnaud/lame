package vue;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JToggleButton;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import modeles.EdgeModel;
import modeles.VertexModel;

import org.apache.commons.collections15.Predicate;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.ConstantTransformer;
import org.apache.commons.collections15.functors.MapTransformer;
import org.apache.commons.collections15.map.LazyMap;

import renderers.CustomEdgeLabelRenderer;
import controleurs.vuesabstraites.ProjetView;
import vue.dialog.DialogPanelParametreFreqsEdges;
import edu.uci.ics.jung.algorithms.cluster.EdgeBetweennessClusterer;
import edu.uci.ics.jung.algorithms.layout.AggregateLayout;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout2;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.util.Relaxer;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Context;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.io.PajekNetWriter;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse.Mode;
import edu.uci.ics.jung.visualization.decorators.AbstractVertexShapeTransformer;
import edu.uci.ics.jung.visualization.decorators.GradientEdgePaintTransformer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.picking.PickedState;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;
import extra.CustomJFileChooser;

public class NetworkGraphFrame extends JFrame implements Printable {

	private static final long serialVersionUID = 1L;
	private String title;
	// private DirectedSparseGraph<VertexModel, EdgeModel> directedGraph;
	private UndirectedSparseGraph<VertexModel, EdgeModel> undirectedGraph;
	private ProjetView activitesView;
	private ResourceBundle bundleOperationsListe;
	private String repertoire;
	private AggregateLayout<VertexModel, EdgeModel> layout;
	private int freq_inf, freq_sup;
	private Map<VertexModel, Paint> vertexPaints = LazyMap.<VertexModel, Paint> decorate(new HashMap<VertexModel, Paint>(), new ConstantTransformer(Color.white));
	private Map<EdgeModel, Paint> edgePaints = LazyMap.<EdgeModel, Paint> decorate(new HashMap<EdgeModel, Paint>(), new ConstantTransformer(Color.blue));
	private Color[] similarColors = { new Color(216, 134, 134), new Color(135, 137, 211), new Color(134, 206, 189), new Color(206, 176, 134), new Color(194, 204, 134), new Color(145, 214, 134),
			new Color(133, 178, 209), new Color(103, 148, 255), new Color(60, 220, 220), new Color(30, 250, 100) };
	private VisualizationViewer<VertexModel, EdgeModel> vv;
	private int nbre_selfloop_in_undirectedGraph;

	// private VoltageClusterer<VertexModel, EdgeModel> clusterer;

	public NetworkGraphFrame(ProjetView activitesView, ResourceBundle bundleOperationsListe, String title, String repertoire,
	// DirectedSparseGraph<VertexModel, EdgeModel> directedGraph,
			UndirectedSparseGraph<VertexModel, EdgeModel> undirectedGraph) {
		this.activitesView = activitesView;
		this.bundleOperationsListe = bundleOperationsListe;
		this.repertoire = repertoire;
		this.title = title;
		// this.directedGraph = directedGraph;
		this.undirectedGraph = undirectedGraph;

		buildNetworkGraphFrame();
	}

	public void buildNetworkGraphFrame() {
		// LAYOUT
		// Fruchterman-Rheingold (FR)
		layout = new AggregateLayout<VertexModel, EdgeModel>(new FRLayout2<VertexModel, EdgeModel>(undirectedGraph));

		// Taille du layout
		// layout.setSize(new Dimension(1500, 1500));

		// VisualizationViewer = espace de visualisation
		vv = new VisualizationViewer<VertexModel, EdgeModel>(layout);
		// Dimension de l'espace de visualisation
		// vv.setPreferredSize(new Dimension(1500, 1500));
		// Couleur de fond
		vv.setBackground(Color.white);
		// ToolTips au survol du noeud par la souris
		vv.setVertexToolTipTransformer(new ToStringLabeller<VertexModel>());

		// LABELS
		// Position des labels : AUTO : à gauche et à droite selon position du
		// graph sur le VV
		// vv.getRenderer().getVertexLabelRenderer().setPosition(Position.AUTO);
		vv.getRenderer().getVertexLabelRenderer().setPosition(Position.S);

		// VertexLabelAsShapeRenderer<VertexModel, EdgeModel> vlasr = new
		// VertexLabelAsShapeRenderer<VertexModel,
		// EdgeModel>(vv.getRenderContext());

		// // Vertex Renderer : labels en tant que vertex shape
		// vv.getRenderContext().setVertexLabelTransformer(
		// // this chains together Transformers so that the html tags
		// // are prepended to the toString method output
		// new ChainedTransformer<VertexModel, String>(new Transformer[] { new
		// ToStringLabeller<String>(), new Transformer<String, String>() {
		// public String transform(String input) {
		// return "<html><center>" + input;
		// }
		// } }));

		// ////////////////////////////////
		// PANEL ZOOM ET BOUTON RESTART //
		// ////////////////////////////////
		GridBagLayout layoutPanelZoomRestart = new GridBagLayout();
		JPanel panelZoomRestart = new JPanel();
		panelZoomRestart.setLayout(layoutPanelZoomRestart);
		panelZoomRestart.setAlignmentY(Component.LEFT_ALIGNMENT);
		GridBagConstraints c = new GridBagConstraints();

		GridBagLayout layoutPanelZoom = new GridBagLayout();
		JPanel panelZoom = new JPanel();
		panelZoom.setLayout(layoutPanelZoom);
		panelZoom.setBorder(BorderFactory.createTitledBorder("Zoom"));
		panelZoom.setAlignmentY(Component.LEFT_ALIGNMENT);
		GridBagConstraints c2 = new GridBagConstraints();
		// BOUTONS ZOOM + ET -
		JButton buttonPlus = new JButton("+");
		JButton buttonMoins = new JButton("-");
		final ScalingControl scaler = new CrossoverScalingControl();
		buttonPlus.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				scaler.scale(vv, 1.1f, vv.getCenter());
			}
		});
		buttonMoins.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				scaler.scale(vv, 1 / 1.1f, vv.getCenter());
			}
		});
		c2.fill = GridBagConstraints.HORIZONTAL;
		c2.gridx = 0;
		c2.gridy = 0;
		panelZoom.add(buttonPlus, c2);
		c2.gridx = 0;
		c2.gridy = 1;
		panelZoom.add(buttonMoins, c2);

		// BOUTON RESTART
		JPanel panelRestart = new JPanel();
		panelRestart.setLayout(new BoxLayout(panelRestart, BoxLayout.Y_AXIS));
		panelRestart.setBorder(BorderFactory.createTitledBorder(bundleOperationsListe.getString("txt_Redessiner")));
		JButton buttonRestart = new JButton(bundleOperationsListe.getString("txt_Ok"));
		buttonRestart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Layout layout = vv.getGraphLayout();
				layout.initialize();
				Relaxer relaxer = vv.getModel().getRelaxer();
				if (relaxer != null) {
					relaxer.stop();
					relaxer.prerelax();
					relaxer.relax();
				}
			}
		});
		c2.gridx = 0;
		c2.gridy = 2;
		panelRestart.add(buttonRestart, c2);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		panelZoomRestart.add(panelZoom, c);
		c.gridx = 0;
		c.gridy = 1;
		panelZoomRestart.add(panelRestart, c);

		// ////////////////////////////////
		// PANEL AFFICHAGE SELECTION //////
		// ////////////////////////////////
		GridBagLayout layoutPanelSourisAffichageSelection = new GridBagLayout();
		JPanel panelSourisAffichageSelection = new JPanel();
		panelSourisAffichageSelection.setBorder(BorderFactory.createTitledBorder(bundleOperationsListe.getString("txt_Selection")));
		panelSourisAffichageSelection.setLayout(layoutPanelSourisAffichageSelection);
		panelSourisAffichageSelection.setAlignmentY(Component.LEFT_ALIGNMENT);
		GridBagConstraints c3 = new GridBagConstraints();

		// COMPORTEMENT SOURIS
		JPanel panelSouris = new JPanel();
		panelSouris.setBorder(BorderFactory.createTitledBorder(bundleOperationsListe.getString("txt_MouseMode")));
		DefaultModalGraphMouse<VertexModel, EdgeModel> graphMouse = new DefaultModalGraphMouse<VertexModel, EdgeModel>();
		// Mode par défaut : transforming
		graphMouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);

		JComboBox mouseModeComboBox = graphMouse.getModeComboBox();
		final ToString toString = new ToString() {
			@Override
			public String toString(final Object object) {
				final Mode mode = (Mode) object;
				if (mode == Mode.TRANSFORMING)
					return bundleOperationsListe.getString("txt_MouseModeTransforming");
				else
					return bundleOperationsListe.getString("txt_MouseModePicking");
			}
		};
		mouseModeComboBox.setRenderer(new ToStringListCellRenderer(mouseModeComboBox.getRenderer(), toString));

		c3.fill = GridBagConstraints.HORIZONTAL;
		c3.gridx = 0;
		c3.gridy = 0;
		panelSouris.add(mouseModeComboBox, c3);
		c3.gridx = 0;
		c3.gridy = 1;
		panelSourisAffichageSelection.add(panelSouris, c3);

		// JTEXTAREA POUR AFFICHER SELECTION
		JPanel panelTxtSelection = new JPanel();
		final JTextArea txtSelection = new JTextArea();
		txtSelection.setEditable(false);
		txtSelection.setLineWrap(true);
		txtSelection.setWrapStyleWord(true);
		txtSelection.setBackground(new Color(214, 217, 223));
		JScrollPane scrollTxtSelection = new JScrollPane(txtSelection);
		scrollTxtSelection.setPreferredSize(new Dimension(200, 100));
		scrollTxtSelection.setAutoscrolls(true);
		panelTxtSelection.add(scrollTxtSelection);

		c3.gridx = 0;
		c3.gridy = 2;
		panelSourisAffichageSelection.add(panelTxtSelection, c3);

		// ////////////////////////////////
		// PANEL CLUSTERING ///////////////
		// ////////////////////////////////
		GridBagLayout layoutPanelClustering = new GridBagLayout();
		JPanel panelClustering = new JPanel();
		panelClustering.setBorder(BorderFactory.createTitledBorder(bundleOperationsListe.getString("txt_AnalyseStructureGraph")));
		panelClustering.setLayout(layoutPanelClustering);
		panelClustering.setAlignmentY(Component.LEFT_ALIGNMENT);
		GridBagConstraints c4 = new GridBagConstraints();

		// CHECKBOX D'ACTIVATION
		final JCheckBox boxActiveClustering = new JCheckBox(bundleOperationsListe.getString("txt_Activer"));
		boxActiveClustering.setAlignmentX(Component.LEFT_ALIGNMENT);
		boxActiveClustering.setSelected(false);

		c4.fill = GridBagConstraints.HORIZONTAL;
		c4.gridx = 0;
		c4.gridy = 0;
		panelClustering.add(boxActiveClustering, c4);

		GridBagLayout layoutPanelClusteringSlider = new GridBagLayout();
		final JPanel panelClusteringSlider = new JPanel();
		panelClusteringSlider.setAlignmentY(Component.LEFT_ALIGNMENT);
		panelClusteringSlider.setOpaque(true);
		panelClusteringSlider.setPreferredSize(new Dimension(240, 100));
		panelClusteringSlider.setMinimumSize(new Dimension(240, 100));
		panelClusteringSlider.setMaximumSize(new Dimension(240, 100));
		final String COMMANDSTRING = bundleOperationsListe.getString("txt_LiensSupprimes") + " : ";
		final JSlider edgeBetweennessSlider = new JSlider(SwingConstants.HORIZONTAL);
		String eastSize = COMMANDSTRING + edgeBetweennessSlider.getValue();
		final TitledBorder sliderBorder = BorderFactory.createTitledBorder(eastSize);
		panelClusteringSlider.setBorder(sliderBorder);

		final JToggleButton buttonGroupVertices = new JToggleButton(bundleOperationsListe.getString("txt_Clusteriser"));
		edgeBetweennessSlider.setBackground(Color.WHITE);
		edgeBetweennessSlider.setPaintTicks(true);
		// Suppression des Self-Loop dans l'undirectedGraph
		System.out.println("Nombre d'edges de undirectedGraph avant suppression self-loops = " + undirectedGraph.getEdgeCount());
		List<EdgeModel> undirectedGraphEdges = new ArrayList<EdgeModel>(undirectedGraph.getEdges());
		Iterator<EdgeModel> undirectedGraphEdgesIterator = undirectedGraphEdges.iterator();
		while (undirectedGraphEdgesIterator.hasNext()) {
			EdgeModel edge = undirectedGraphEdgesIterator.next();
			Pair<VertexModel> vertices = undirectedGraph.getEndpoints(edge);
			VertexModel v1 = vertices.getFirst();
			VertexModel v2 = vertices.getSecond();
			if (v1.equals(v2))
				undirectedGraph.removeEdge(edge);
		}
		System.out.println("Nombre d'edges de undirectedGraph après suppression self-loops = " + undirectedGraph.getEdgeCount());
		int max_edges_slider = undirectedGraph.getEdgeCount() - nbre_selfloop_in_undirectedGraph;
		edgeBetweennessSlider.setMaximum(max_edges_slider);
		edgeBetweennessSlider.setMinimum(0);
		edgeBetweennessSlider.setValue(0);
		edgeBetweennessSlider.setMajorTickSpacing(Math.round(max_edges_slider / 5));
		edgeBetweennessSlider.setPaintLabels(true);
		edgeBetweennessSlider.setPaintTicks(true);
		edgeBetweennessSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider) e.getSource();
				if (!source.getValueIsAdjusting()) {
					int numEdgesToRemove = source.getValue();
					clusterAndRecolor(numEdgesToRemove, similarColors, buttonGroupVertices.isSelected());
					sliderBorder.setTitle(COMMANDSTRING + edgeBetweennessSlider.getValue());
					panelClusteringSlider.repaint();
					vv.validate();
					vv.repaint();
				}
			}
		});
		buttonGroupVertices.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				clusterAndRecolor(edgeBetweennessSlider.getValue(), similarColors, e.getStateChange() == ItemEvent.SELECTED);
				vv.repaint();
			}
		});
		GridBagConstraints c5 = new GridBagConstraints();
		c5.gridx = 0;
		c5.gridy = 0;
		panelClusteringSlider.add(edgeBetweennessSlider, c5);

		c4.gridx = 0;
		c4.gridy = 1;
		panelClustering.add(panelClusteringSlider, c4);
		c4.gridx = 0;
		c4.gridy = 2;
		panelClustering.add(buttonGroupVertices, c4);

		// ////////////////////////////////
		// PANEL AFFICHAGE ////////////////
		// ////////////////////////////////
		GridBagLayout layoutPanelAffichage = new GridBagLayout();
		JPanel panelAffichage = new JPanel();
		panelAffichage.setBorder(BorderFactory.createTitledBorder(bundleOperationsListe.getString("txt_Affichage")));
		panelAffichage.setLayout(layoutPanelAffichage);
		panelAffichage.setAlignmentY(Component.LEFT_ALIGNMENT);
		GridBagConstraints c1 = new GridBagConstraints();
		// BOUTON FREQUENCE
		JButton buttonParamEdgesFreqs = new JButton(bundleOperationsListe.getString("txt_FrequencesArcs"));
		// Bouton de dialogue de filtre des fréquences de liens
		// Calcul des classes
		List<Integer> listFreqs = new ArrayList<Integer>();
		for (EdgeModel edge : layout.getGraph().getEdges())
			listFreqs.add(edge.getFrequence());
		Collections.sort(listFreqs);
		final int freq_max = listFreqs.get(listFreqs.size() - 1);
		// System.out.println("lastPoids = " + lastPoids);
		int dizaineSup = 10;
		if (freq_max > 10) {
			dizaineSup = Math.round(freq_max / 10) * 10;
		}
		// System.out.println("dizaineSup = " + dizaineSup);
		int pas = dizaineSup / 5;
		final int[] tabClasses = new int[5];
		int j = 0;
		for (int i = 0; i < dizaineSup; i += pas) {
			tabClasses[j] = i;
			j++;
		}
		// System.out.println("TabClasses : ");
		// for (Integer i : tabClasses)
		// System.out.println(i);
		final JTextArea txtSelectionEdgeqFreqs = new JTextArea();
		txtSelectionEdgeqFreqs.setEditable(false);
		txtSelectionEdgeqFreqs.setLineWrap(true);
		txtSelectionEdgeqFreqs.setWrapStyleWord(true);
		txtSelectionEdgeqFreqs.setBackground(new Color(214, 217, 223));
		// JPanel panelTxtSelectionEdgeqFreqs = new JPanel();
		// panelTxtSelectionEdgeqFreqs.add(txtSelectionEdgeqFreqs);
		// JScrollPane scrollLabelTxtSelectionEdgesFreqs = new
		// JScrollPane(labelTxtSelectionEdgeqFreqs);
		txtSelectionEdgeqFreqs.setPreferredSize(new Dimension(200, 100));
		txtSelectionEdgeqFreqs.setMinimumSize(new Dimension(200, 100));
		txtSelectionEdgeqFreqs.setMaximumSize(new Dimension(200, 100));
		// scrollLabelTxtSelectionEdgesFreqs.setAutoscrolls(true);
		buttonParamEdgesFreqs.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DialogPanelParametreFreqsEdges dialog = new DialogPanelParametreFreqsEdges(bundleOperationsListe, freq_max, freq_inf, freq_sup);
				int result = JOptionPane.showOptionDialog(null, dialog.getPanel(), bundleOperationsListe.getString("txt_FrequencesArcs"), JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
						null, null, null);
				if (result == JOptionPane.OK_OPTION) {
					if (!dialog.getTxtFreqInf().getText().equals("") || !dialog.getTxtFreqSup().getText().equals("")) {
						if (!dialog.getTxtFreqInf().getText().equals(""))
							try {
								freq_inf = Integer.parseInt(dialog.getTxtFreqInf().getText());
							} catch (NumberFormatException n) {
								JOptionPane.showMessageDialog(null, bundleOperationsListe.getString("txt_ErreurSaisieNonValide"), bundleOperationsListe.getString("txt_FrequencesArcs"),
										JOptionPane.ERROR_MESSAGE);
							}
						else
							freq_inf = 0 - 1;
						if (!dialog.getTxtFreqSup().getText().equals(""))
							try {
								freq_sup = Integer.parseInt(dialog.getTxtFreqSup().getText());
							} catch (NumberFormatException n) {
								JOptionPane.showMessageDialog(null, bundleOperationsListe.getString("txt_ErreurSaisieNonValide"), bundleOperationsListe.getString("txt_FrequencesArcs"),
										JOptionPane.ERROR_MESSAGE);
							}
						else
							freq_sup = freq_max + 1;
						txtSelectionEdgeqFreqs.setText(bundleOperationsListe.getString("txt_ArcFreqSupA") + " " + freq_inf + " " + bundleOperationsListe.getString("txt_ArcFreqInfA") + " "
								+ freq_sup + "\n");
						if (freq_sup > freq_inf) {
							EdgeFreqDisplayPredicate<VertexModel, EdgeModel> show_edge = new EdgeFreqDisplayPredicate<VertexModel, EdgeModel>(freq_inf, freq_sup);
							vv.getRenderContext().setEdgeIncludePredicate(show_edge);
							VertexFreqDisplayPredicate<VertexModel, EdgeModel> show_vertex = new VertexFreqDisplayPredicate<VertexModel, EdgeModel>(freq_inf, freq_sup);
							vv.getRenderContext().setVertexIncludePredicate(show_vertex);
							vv.repaint();
						} else
							JOptionPane.showMessageDialog(null, bundleOperationsListe.getString("txt_ErreurSaisieNonCoherente"), bundleOperationsListe.getString("txt_FrequencesArcs"),
									JOptionPane.ERROR_MESSAGE);

					} else
						JOptionPane.showMessageDialog(null, bundleOperationsListe.getString("txt_ErreurChampsVides"), bundleOperationsListe.getString("txt_FrequencesArcs"), JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		c1.fill = GridBagConstraints.HORIZONTAL;
		c1.gridx = 0;
		c1.gridy = 0;
		//panelAffichage.add(buttonParamEdgesFreqs, c1);
		c1.gridx = 0;
		c1.gridy = 1;
		//panelAffichage.add(txtSelectionEdgeqFreqs, c1);

		// CHECKBOX FREQUENCE EDGES
		final JCheckBox boxShowEdgesLabels = new JCheckBox(bundleOperationsListe.getString("txt_MontrerFrequenceArcs"));
		boxShowEdgesLabels.setSelected(false);
		boxShowEdgesLabels.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AbstractButton source = (AbstractButton) e.getSource();
				if (source == boxShowEdgesLabels)
					if (source.isSelected())
						vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller<EdgeModel>());
					else
						vv.getRenderContext().setEdgeLabelTransformer(new ConstantTransformer(null));
				vv.repaint();
			}
		});
		c1.gridx = 0;
		c1.gridy = 2;
		panelAffichage.add(boxShowEdgesLabels, c1);

		// CHECKBOX LABELS SOMMETS
		final JCheckBox boxShowVerticesLabels = new JCheckBox(bundleOperationsListe.getString("txt_MontrerLabelsSommets"));
		boxShowVerticesLabels.setSelected(false);
		boxShowVerticesLabels.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AbstractButton source = (AbstractButton) e.getSource();
				if (source == boxShowVerticesLabels)
					if (source.isSelected())
						vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<VertexModel>());
					else
						vv.getRenderContext().setVertexLabelTransformer(new ConstantTransformer(null));
				vv.repaint();
			}
		});
		c1.gridx = 0;
		c1.gridy = 3;
		panelAffichage.add(boxShowVerticesLabels, c1);

		// ////////////////////////////////
		// PANEL CONTROLS ////////////////
		// ////////////////////////////////

		JPanel panelControls = new JPanel();
		panelControls.setLayout(new FlowLayout());
		panelControls.setPreferredSize(new Dimension(900, 250));

		panelZoomRestart.setPreferredSize(new Dimension(100, 240));
		panelSourisAffichageSelection.setPreferredSize(new Dimension(250, 240));
		panelClustering.setPreferredSize(new Dimension(270, 240));
		panelAffichage.setPreferredSize(new Dimension(250, 240));

		panelControls.add(panelZoomRestart);
		panelControls.add(panelSourisAffichageSelection);
		panelControls.add(panelClustering);
		panelControls.add(panelAffichage);

		// ////////////////////////////////
		// BARRE DE MENUS ////////////////
		// ////////////////////////////////

		JMenu menu = new JMenu(bundleOperationsListe.getString("txt_MenuFichier"));
		menu.add(new AbstractAction(bundleOperationsListe.getString("txt_MenuExporterImage")) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				CustomJFileChooser customChooser = new CustomJFileChooser("jpg", bundleOperationsListe, repertoire, false);
				customChooser.show();
				if (customChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION && customChooser.getTxtFile() != null) {
					((NetworkGraphFrame) getJFrame()).writeJPEGImage(customChooser.getTxtFile());
				}
			}
		});
		menu.add(new AbstractAction(bundleOperationsListe.getString("txt_MenuExporterNetFile")) {
			private static final long serialVersionUID = 1L;

			@Override
			public void actionPerformed(ActionEvent e) {
				CustomJFileChooser customChooser = new CustomJFileChooser("net", bundleOperationsListe, repertoire, false);
				customChooser.show();
				if (customChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION && customChooser.getTxtFile() != null) {
					((NetworkGraphFrame) getJFrame()).writeNetFile(customChooser.getTxtFile());
				}
			}
		});
		menu.add(new AbstractAction(bundleOperationsListe.getString("txt_MenuImprimer")) {
			@Override
			public void actionPerformed(ActionEvent e) {
				PrinterJob printJob = PrinterJob.getPrinterJob();
				printJob.setPrintable((Printable) getJFrame());
				if (printJob.printDialog()) {
					try {
						printJob.print();
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		});

		JPopupMenu.setDefaultLightWeightPopupEnabled(false);
		JMenuBar menuBar = new JMenuBar();
		menuBar.add(menu);

		// Transformation du label si sélectionné
		final PickedState<VertexModel> ps = vv.getPickedVertexState();
		final Transformer<VertexModel, Font> vertexFont = new Transformer<VertexModel, Font>() {
			@Override
			public Font transform(VertexModel v) {
				if (ps.isPicked(v)) {
					return new Font("default", Font.BOLD, 13);
				}
				return new Font("default", Font.PLAIN, 12);
			}
		};
		// Couleurs des vertices
		// Dominants en rouge, petits en vert
		final Transformer<VertexModel, Paint> vertexPaint = new Transformer<VertexModel, Paint>() {
			@Override
			public Paint transform(VertexModel v) {
				if (v.getIsDominant()) {
					return new Color(156, 54, 102);
				}
				return new Color(1, 113, 188);
			}
		};
		// Couleurs du bord des vertices si sélection
		final Transformer<VertexModel, Paint> vertexBorderPaint = new Transformer<VertexModel, Paint>() {
			@Override
			public Paint transform(VertexModel v) {
				if (ps.isPicked(v)) {
					return Color.ORANGE;
				}
				return Color.BLACK;
			}
		};
		// Transformation des edges
		final EdgeWeightStrokeFunction<EdgeModel> ewcs = new EdgeWeightStrokeFunction<EdgeModel>(tabClasses);

		// TRANSFORMATION DES EDGES LORS DE LA SELECTION
		final PickedState<EdgeModel> pes = vv.getPickedEdgeState();

		final Transformer<EdgeModel, Font> edgeFont = new Transformer<EdgeModel, Font>() {
			@Override
			public Font transform(EdgeModel e) {
				if (pes.isPicked(e)) {
					return new Font("default", Font.BOLD, 13);
				}
				return new Font("default", Font.PLAIN, 12);
			}
		};

		final SelfLoopDisplayPredicate<VertexModel, EdgeModel> hide_selfloop = new SelfLoopDisplayPredicate<VertexModel, EdgeModel>(false);
		final SelfLoopDisplayPredicate<VertexModel, EdgeModel> show_selfloop = new SelfLoopDisplayPredicate<VertexModel, EdgeModel>(true);

		Transformer<VertexModel, Double> transformerVertexDegrees = new Transformer<VertexModel, Double>() {
			@Override
			public Double transform(VertexModel vertex) {
				return vertex.getDegreeScore();
			}
		};
		VertexSizeAspect<VertexModel, EdgeModel> vsa = new VertexSizeAspect<VertexModel, EdgeModel>(layout.getGraph(), transformerVertexDegrees);

		final GradientPickedEdgePaintFunction<VertexModel, EdgeModel> edgeDrawPaint = new GradientPickedEdgePaintFunction<VertexModel, EdgeModel>(vv);

		final ClusteringGradientPickedEdgePaintFunction<VertexModel, EdgeModel> clusteringEdgeDrawPaint = new ClusteringGradientPickedEdgePaintFunction<VertexModel, EdgeModel>(vv);

		final Transformer<EdgeModel, Stroke> clusteringEdgeStroke = new Transformer<EdgeModel, Stroke>() {
			@Override
			public Stroke transform(EdgeModel e) {
				return new BasicStroke(1);

			}
		};

		// MODE DEFAULT
		// MODES DE SOURIS
		vv.setGraphMouse(graphMouse);
		vv.addKeyListener(graphMouse.getModeKeyListener());
		// RENDERERS
		vv.getRenderContext().setVertexShapeTransformer(vsa);
		vv.getRenderContext().setVertexFontTransformer(vertexFont);
		vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
		vv.getRenderContext().setVertexDrawPaintTransformer(vertexBorderPaint);
		vv.getRenderContext().setEdgeStrokeTransformer(ewcs);
		vv.getRenderContext().setEdgeFontTransformer(edgeFont);
		vv.getRenderContext().setEdgeLabelRenderer(new CustomEdgeLabelRenderer(Color.black, Color.blue));
		vv.getRenderContext().setEdgeDrawPaintTransformer(edgeDrawPaint);
		// vv.getRenderContext().setVertexShapeTransformer(vlasr);
		buttonGroupVertices.setEnabled(false);
		edgeBetweennessSlider.setEnabled(false);
		// END DEFAULT
		boxActiveClustering.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AbstractButton source = (AbstractButton) e.getSource();
				if (source.isSelected()) {
					System.out.println("undirectedGraph.getEdgeCount() = " + undirectedGraph.getEdgeCount());
					if (undirectedGraph.getEdgeCount() > 600) {
						int result = JOptionPane.showOptionDialog(null,
								bundleOperationsListe.getString("txt_GrapheTropGros1") + " " + undirectedGraph.getEdgeCount() + " " + bundleOperationsListe.getString("txt_GrapheTropGros2") + " ?",
								bundleOperationsListe.getString("txt_AnalyseStructureGraph"), JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
						if (result == JOptionPane.YES_OPTION) {
							performClustering();
						} else
							source.setSelected(false);
					} else {
						performClustering();
					}
				} else {
					System.out.println("clustering désactivé");
					buttonGroupVertices.setEnabled(false);
					edgeBetweennessSlider.setEnabled(false);
					buttonGroupVertices.setSelected(false);
					edgeBetweennessSlider.setValue(0);
					vv.getGraphLayout().setGraph(undirectedGraph);
					vv.getRenderContext().setVertexFontTransformer(vertexFont);
					vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
					vv.getRenderContext().setVertexDrawPaintTransformer(vertexBorderPaint);
					vv.getRenderContext().setEdgeStrokeTransformer(ewcs);
					vv.getRenderContext().setEdgeFontTransformer(edgeFont);
					vv.getRenderContext().setEdgeDrawPaintTransformer(edgeDrawPaint);
					vv.getRenderContext().setEdgeLabelRenderer(new CustomEdgeLabelRenderer(Color.black, Color.blue));
					vv.getRenderContext().setEdgeIncludePredicate(show_selfloop);

				}
				vv.repaint();
			}

			private void performClustering() {
				System.out.println("clustering activé");
				buttonGroupVertices.setEnabled(true);
				edgeBetweennessSlider.setEnabled(true);
				vv.getGraphLayout().setGraph(undirectedGraph);
				clusterAndRecolor(0, similarColors, buttonGroupVertices.isSelected());
				vv.getRenderContext().setVertexFillPaintTransformer(MapTransformer.<VertexModel, Paint> getInstance(vertexPaints));
				vv.getRenderContext().setEdgeDrawPaintTransformer(clusteringEdgeDrawPaint);
				vv.getRenderContext().setEdgeStrokeTransformer(clusteringEdgeStroke);
				vv.getRenderContext().setEdgeIncludePredicate(hide_selfloop);
			}
		});

		// Listeners pour la sélection de vertex ou de edge
		pes.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				txtSelection.setText("");
				Object subject = e.getItem();
				if (subject instanceof EdgeModel) {
					EdgeModel edge = (EdgeModel) subject;
					if (pes.isPicked(edge)) {
						List<VertexModel> connectedVertices = new ArrayList<VertexModel>(layout.getGraph().getIncidentVertices(edge));
						String connectedVertex1 = "", connectedVertex2 = "";
						connectedVertex1 = connectedVertices.get(0).toString();
						connectedVertex2 = connectedVertices.get(1).toString();
						txtSelection.append(bundleOperationsListe.getString("txt_SelectionArc1") + " " + edge.getIdEdge() + " " + bundleOperationsListe.getString("txt_SelectionArc2") + " "
								+ connectedVertex1 + " " + bundleOperationsListe.getString("txt_SelectionArc3") + " " + connectedVertex2 + " :\n "
								+ bundleOperationsListe.getString("txt_SelectionArc4") + " = " + edge.getFrequence() + "\n");

					} else
						txtSelection.setText("");
				}
			}
		});

		// Listener sur la sélection d'un node
		ps.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				Object subject = e.getItem();
				if (subject instanceof VertexModel) {
					VertexModel vertex = (VertexModel) subject;
					if (ps.isPicked(vertex)) {
						txtSelection.append(bundleOperationsListe.getString("txt_SelectionSommet1") + " " + vertex
						// + " :\n" +
						// bundleOperationsListe.getString("txt_SelectionSommet2")
						// + " = "
						// + vertex.getOutEdgeFreqs() + "\n" +
						// bundleOperationsListe.getString("txt_SelectionSommet3")+" = "
						// + vertex.getInEdgeFreqs() + "\n"
								);
						System.out.println("In Edges = " + layout.getGraph().getInEdges(vertex) + " | " + "Out Edges = " + layout.getGraph().getOutEdges(vertex) + " | " + "Incident Edges = "
								+ layout.getGraph().getIncidentEdges(vertex));

					} else
						txtSelection.setText("");
				}
			}
		});

		// AJOUT A LA JFRAME
		setTitle(title);
		GraphZoomScrollPane gzsp = new GraphZoomScrollPane(vv);
		getContentPane().add(gzsp);
		getContentPane().add(panelControls, BorderLayout.SOUTH);
		setJMenuBar(menuBar);
		setSize(new Dimension(900, 800));
		setLocationRelativeTo(null);
		setVisible(true);

	}

	public interface ToString {
		public String toString(Object object);
	}

	public final class ToStringListCellRenderer implements ListCellRenderer<Object> {
		private final ListCellRenderer<Object> originalRenderer;
		private final ToString toString;

		public ToStringListCellRenderer(final ListCellRenderer<Object> originalRenderer, final ToString toString) {
			this.originalRenderer = originalRenderer;
			this.toString = toString;
		}

		@Override
		public Component getListCellRendererComponent(final JList list, final Object value, final int index, final boolean isSelected, final boolean cellHasFocus) {
			return originalRenderer.getListCellRendererComponent(list, toString.toString(value), index, isSelected, cellHasFocus);
		}

	}

	public void writeJPEGImage(String txtFile) {
		int width = vv.getWidth();
		int height = vv.getHeight();

		BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = bi.createGraphics();
		vv.paint(graphics);
		graphics.dispose();

		try {
			ImageIO.write(bi, "jpg", new File(txtFile));
			JOptionPane.showMessageDialog(null, bundleOperationsListe.getString("txt_GraphExportSucces") + "\n" + txtFile + ".", bundleOperationsListe.getString("txt_MenuExporterImage"),
					JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
		if (pageIndex > 0) {
			return (Printable.NO_SUCH_PAGE);
		} else {
			java.awt.Graphics2D g2d = (java.awt.Graphics2D) graphics;
			vv.setDoubleBuffered(false);
			g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
			vv.paint(g2d);
			vv.setDoubleBuffered(true);
			return (Printable.PAGE_EXISTS);
		}
	}

	public void writeNetFile(String txtFile) {
		try {
			PajekNetWriter<VertexModel, EdgeModel> writer = new PajekNetWriter<VertexModel, EdgeModel>();
			writer.save(layout.getGraph(), txtFile);
			JOptionPane.showMessageDialog(null, bundleOperationsListe.getString("txt_GraphExportSucces") + "\n" + txtFile + ".", bundleOperationsListe.getString("txt_MenuExporterNetFile"),
					JOptionPane.INFORMATION_MESSAGE);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public JFrame getJFrame() {
		return this;
	}

	// Transformer des arcs selon leur Fréquence
	private class EdgeWeightStrokeFunction<E> implements Transformer<E, Stroke> {
		Stroke stroke1 = new BasicStroke(1);
		Stroke stroke2 = new BasicStroke(2);
		Stroke stroke3 = new BasicStroke(3);
		Stroke stroke4 = new BasicStroke(4);
		Stroke stroke5 = new BasicStroke(5);

		int[] tabClasses;

		public EdgeWeightStrokeFunction(int[] tabClasses) {
			this.tabClasses = tabClasses;
		}

		@Override
		public Stroke transform(E e) {
			EdgeModel edge = (EdgeModel) e;
			int freq = edge.getFrequence();

			if (freq <= tabClasses[1]) {
				return stroke1;
			} else if (freq > tabClasses[1] && freq <= tabClasses[2]) {
				return stroke2;
			} else if (freq > tabClasses[2] && freq <= tabClasses[3]) {
				return stroke3;
			} else if (freq > tabClasses[3] && freq <= tabClasses[4]) {
				return stroke4;
			} else
				return stroke5;
		}
	}

	// Predicate de tri des arcs selon leur Fréquence
	private class EdgeFreqDisplayPredicate<V, E> implements Predicate<Context<Graph<V, E>, E>> {
		int freq_inf;
		int freq_sup;

		public EdgeFreqDisplayPredicate(int freq_inf, int freq_sup) {
			this.freq_inf = freq_inf;
			this.freq_sup = freq_sup;
		}

		@Override
		public boolean evaluate(Context<Graph<V, E>, E> context) {
			E e = context.element;
			EdgeModel edge = (EdgeModel) e;
			if (edge.getFrequence() > freq_inf && edge.getFrequence() < freq_sup) {
				return true;
			}
			return false;
		}
	}

	// Predicate de tri des Vertex selon leur (TreeSet) setEdgesFreqs
	private class VertexFreqDisplayPredicate<V, E> implements Predicate<Context<Graph<V, E>, V>> {
		int freq_inf;
		int freq_sup;

		public VertexFreqDisplayPredicate(int freq_inf, int freq_sup) {
			this.freq_inf = freq_inf;
			this.freq_sup = freq_sup;
		}

		@Override
		public boolean evaluate(Context<Graph<V, E>, V> context) {
			V v = context.element;
			VertexModel vertex = (VertexModel) v;
			TreeSet<Integer> setEdgesFreqs = vertex.getSetEdgesFreqs();
			NavigableSet<Integer> subSetEdgesFreqs = setEdgesFreqs.subSet(freq_inf, false, freq_sup, false);
			// System.out.println(vertex+" : "+setEdgesFreqs);
			if (freq_inf != -1)
				return subSetEdgesFreqs.size() > 0;
			else
				return true;
		}
	}

	public void clusterAndRecolor(int numEdgesToRemove, Color[] colors, boolean groupClusters) {
		// Now cluster the vertices by removing the top 50 edges with highest
		// betweenness
		// if (numEdgesToRemove == 0) {
		// colorCluster( g.getVertices(), colors[0] );
		// } else {

		Graph<VertexModel, EdgeModel> g = layout.getGraph();
		layout.removeAll();
		EdgeBetweennessClusterer<VertexModel, EdgeModel> clusterer = new EdgeBetweennessClusterer<VertexModel, EdgeModel>(numEdgesToRemove);
		// clusterer = new VoltageClusterer(g, 10);
		System.out.println("nombre Edges total = " + g.getEdgeCount());
		System.out.println("nombre Vertices total = " + g.getVertexCount());
		System.out.println("nombre Edges To Remove = " + numEdgesToRemove);
		System.out.println("\tnombre Edges restants sur le graph = " + g.getEdgeCount());
		Set<Set<VertexModel>> clusterSet = clusterer.transform(g);
		System.out.println("\ttaille de clusterSet = " + clusterSet.size());
		List<EdgeModel> edges = clusterer.getEdgesRemoved();
		System.out.println("\ttaille de edges (=clusterer.getEdgesRemoved()) = " + edges.size() + " :");

		int i = 0;
		// Set the colors of each node so that each cluster's vertices have the
		// same color
		for (Iterator<Set<VertexModel>> cIt = clusterSet.iterator(); cIt.hasNext();) {

			Set<VertexModel> vertices = cIt.next();
			Color c = colors[i % colors.length];

			colorCluster(vertices, c);
			if (groupClusters == true) {
				groupCluster(vertices);
			}
			i++;
		}
		for (EdgeModel e : g.getEdges()) {
			if (edges.contains(e)) {
				edgePaints.put(e, Color.lightGray);
			} else {
				edgePaints.put(e, Color.black);
			}
		}
	}

	private void colorCluster(Set<VertexModel> vertices, Color c) {
		for (VertexModel v : vertices) {
			vertexPaints.put(v, c);
		}
	}

	private void groupCluster(Set<VertexModel> vertices) {
		if (vertices.size() < layout.getGraph().getVertexCount()) {
			Point2D center = layout.transform(vertices.iterator().next());
			Graph<VertexModel, EdgeModel> subGraph = SparseMultigraph.<VertexModel, EdgeModel> getFactory().create();
			for (VertexModel v : vertices) {
				subGraph.addVertex(v);
			}
			Layout<VertexModel, EdgeModel> subLayout = new CircleLayout<VertexModel, EdgeModel>(subGraph);
			subLayout.setInitializer(vv.getGraphLayout());
			subLayout.setSize(new Dimension(100, 100));

			layout.put(subLayout, center);
			vv.repaint();
		}
	}

	private class SelfLoopDisplayPredicate<VertexModel, EdgeModel> implements Predicate<Context<Graph<VertexModel, EdgeModel>, EdgeModel>> {

		private boolean show_selfloop;

		public SelfLoopDisplayPredicate(boolean show_selfloop) {
			this.show_selfloop = show_selfloop;
		}

		@Override
		public boolean evaluate(Context<Graph<VertexModel, EdgeModel>, EdgeModel> context) {
			Graph<VertexModel, EdgeModel> graph = context.graph;
			EdgeModel edge = context.element;
			Pair<VertexModel> vertices = graph.getEndpoints(edge);
			// System.out.println("taille vertices = " + vertices.size());

			VertexModel v1 = vertices.getFirst();
			VertexModel v2 = vertices.getSecond();
			if (v1.equals(v2) && show_selfloop == false) {
				// System.out.println("\t" + v1 + " = " + v2);
				return false;
			}
			if (v1.equals(v2) && show_selfloop == true) {
				// System.out.println("\t" + v1 + " = " + v2);
				return true;
			}
			return true;
		}
	}

	private class VertexSizeAspect<VertexModel, EdgeModel> extends AbstractVertexShapeTransformer<VertexModel> {

		private Transformer<VertexModel, Double> degrees;
		private Graph<VertexModel, EdgeModel> graph;

		public VertexSizeAspect(Graph<VertexModel, EdgeModel> graph, final Transformer<VertexModel, Double> degrees) {
			this.graph = graph;
			this.degrees = degrees;
			setSizeTransformer(new Transformer<VertexModel, Integer>() {

				@Override
				public Integer transform(VertexModel vertex) {
					int degree = (int) Math.round(degrees.transform(vertex));
					if (degree == 0)
						return 10;
					if (degree > 50)
						return 50;
					return degree;
				}
			});
		}

		@Override
		public Shape transform(VertexModel vertex) {
			return factory.getEllipse(vertex);
		}
	}

	public class GradientPickedEdgePaintFunction<VertexModel, EdgeModel> extends GradientEdgePaintTransformer<VertexModel, EdgeModel> {

		public GradientPickedEdgePaintFunction(VisualizationViewer<VertexModel, EdgeModel> vv) {
			super(Color.WHITE, Color.BLACK, vv);
		}

		@Override
		public Paint transform(EdgeModel edge) {
			return super.transform(edge);
		}
	}

	public class ClusteringGradientPickedEdgePaintFunction<VertexModel, EdgeModel> extends GradientEdgePaintTransformer<VertexModel, EdgeModel> {

		public ClusteringGradientPickedEdgePaintFunction(VisualizationViewer<VertexModel, EdgeModel> vv) {
			super(Color.WHITE, Color.BLACK, vv);
		}

		@Override
		public Paint transform(EdgeModel edge) {
			Paint c = edgePaints.get(edge);
			if (c == Color.LIGHT_GRAY) {
				GradientEdgePaintTransformer<VertexModel, EdgeModel> newGradient = new GradientEdgePaintTransformer<VertexModel, EdgeModel>(Color.WHITE, Color.LIGHT_GRAY, vv);
				return newGradient.transform(edge);
			} else
				return super.transform(edge);

		}

		// public Stroke transform(EdgeModel e) {
		// Paint c = edgePaints.get(e);
		// if (edgeBetweennessSlider.getValue() != 0) {
		// if (c == Color.LIGHT_GRAY)
		// return THIN;
		// else
		// return THICK;
		// } else
		// return THIN;
		//
		// }
	}
}
