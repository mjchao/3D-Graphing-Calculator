package mjchao.graph3d.ui;

import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.ListCellRenderer;

import mjchao.graph3d.util.Materials;
import mjchao.graph3d.util.Materials.MaterialData;
import mjchao.graph3d.util.Text;

/**
 * allows the user to input equation(s) describing one graph
 * 
 * @author mjchao
 *
 */
abstract public class EquationPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	final private JPanel pnlIgnoreOptions = new JPanel( new FlowLayout( FlowLayout.CENTER ) );
	final private JCheckBox chkIgnore = new JCheckBox( Text.EquationPanel.IGNORE_EQUATION );
	final private JButton cmdRemove = new JButton( Text.EquationPanel.REMOVE_EQUATION );
	
	final private JPanel pnlColor = new JPanel();
	final private JLabel lblColor = new JLabel( Text.EquationPanel.SELECT_COLOR );
	final private ColorChooser cboColor = new ColorChooser();
	
	public EquationPanel() {
		setLayout( new BoxLayout( this , BoxLayout.Y_AXIS ) );
		setBorder( BorderFactory.createEtchedBorder() );
	}
	
	/**
	 * prevents the user from making modifications to this equation
	 */
	public void disableModification() {
		this.cboColor.setEnabled( false );
		this.chkIgnore.setEnabled( false );
		this.cmdRemove.setEnabled( false );
	}
	
	/**
	 * allows the user to make modifications to this equation once again
	 */
	public void enableModification() {
		this.cboColor.setEnabled( true );
		this.chkIgnore.setEnabled( true );
		this.cmdRemove.setEnabled( true );
	}
	
	protected void addColorChooser() {
		this.pnlColor.setLayout( new BoxLayout( this.pnlColor , BoxLayout.X_AXIS ) );
		this.pnlColor.add( this.lblColor );
		this.pnlColor.add( this.cboColor );
		add( this.pnlColor );
	}
	
	protected Color getSelectedColor() {
		return ( (ColorData) this.cboColor.getSelectedItem() ).m_color;
	}
	
	protected PhongMaterial getSelectedMaterial() {
		return ( (ColorData) this.cboColor.getSelectedItem() ).m_material;
	}
	
	protected void addIgnoreOptions() {
		this.pnlIgnoreOptions.add( this.chkIgnore );
		this.pnlIgnoreOptions.add( this.cmdRemove );
		add( this.pnlIgnoreOptions );
	}
	
	/**
	 * adds the given listener to the remove button
	 * 
	 * @param l
	 */
	public void addRemoveListener( ActionListener l ) {
		this.cmdRemove.addActionListener( l );
	}
	
	public boolean shouldGraph() {
		return !this.chkIgnore.isSelected();
	}
	
	abstract public String getEquationText();
	
	public void createGraph() {
		createGraph( null , null );
	}
	
	/**
	 * graphs the graph defined by the data the user inputed into this equation panel
	 * 
	 * @param graphThread		an optional thread in which the graphing takes place. this can be null if
	 * 							graphing is not occuring in a thread
	 * @param progress			an optional progressbar to update the user of the graphing progress. this can be
	 * 							null if you do not need to update the user on the graphing progress
	 */
	abstract public void createGraph( Thread graphThread , JProgressBar progress );
	
	abstract public void removeGraph();
	
	final public static ArrayList< ColorData > GRAPH_COLORS = loadColorData();
			
	private static ArrayList< ColorData > loadColorData() {
		ArrayList< ColorData > rtn = new ArrayList< ColorData >();
		for ( Materials.MaterialData d : Materials.getMaterialsList() ) {
			rtn.add( ColorData.createFromMaterialData( d ) );
		}
		return rtn;
	}
	
	protected class ColorChooser extends JComboBox< ColorData > {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public ColorChooser() {
			this.setRenderer( new ColorDisplay() );
			for ( ColorData c : GRAPH_COLORS ) {
				this.addItem( c );
			}
		}
	}
	
	private static class ColorData {
		
		final public PhongMaterial m_material;
		final public Color m_color;
		final public String m_name;
		
		public ColorData( PhongMaterial m , Color c , String name ) {
			this.m_material = m;
			this.m_color = c;
			this.m_name = name;
		}
		
		public static ColorData createFromMaterialData( MaterialData m ) {
			return new ColorData( m.m_material , m.m_color , m.m_name );
		}
	}
	
	private class ColorDisplay extends JLabel implements ListCellRenderer< ColorData > {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		final public static int LINE_WIDTH = 50;
		final public static int PANEL_WIDTH = 50 + LINE_WIDTH;
		final public static int PANEL_HEIGHT = 25;
		final public static int THICKNESS = 20;
		final public static int START_Y = 10;
		
		private java.awt.Color m_awtColor;
		
		public ColorDisplay() {

		}
		
		public void setColor( Color c ) {
			this.m_awtColor = new java.awt.Color( (int)(c.getRed()*255) , (int)(c.getGreen()*255) , (int)(c.getBlue()*255) );
		}
		
		@Override
		public void paintComponent( Graphics g ) {
			Graphics2D g2d = ( Graphics2D ) g;
			g2d.setColor( this.m_awtColor );
			g2d.setStroke( new BasicStroke( THICKNESS ) );
			g2d.drawLine( this.getWidth()-LINE_WIDTH , START_Y , this.getWidth() , START_Y );
			super.paintComponent( g );
		}
		
		@Override
		public Dimension getPreferredSize() {
			return new Dimension( PANEL_WIDTH , PANEL_HEIGHT );
		}
		
		@Override
		public Component getListCellRendererComponent( JList< ? extends ColorData > list , ColorData value , int index , boolean isSelected , boolean cellHasFocus ) {
			this.setColor( value.m_color );
			this.setText( value.m_name );
			return this;
		}
	}
}
