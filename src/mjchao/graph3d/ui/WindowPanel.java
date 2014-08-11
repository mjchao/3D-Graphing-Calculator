package mjchao.graph3d.ui;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javafx.application.Platform;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

import mjchao.graph3d.graph.GraphPaper3D;
import mjchao.graph3d.util.Text;

public class WindowPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	final DimensionInputPanel pnlX = new DimensionInputPanel( Text.EquationPanel.X_DIMENSION );
	final DimensionInputPanel pnlY = new DimensionInputPanel( Text.EquationPanel.Y_DIMENSION );
	final DimensionInputPanel pnlZ = new DimensionInputPanel( Text.EquationPanel.Z_DIMENSION );

	final private JPanel pnlCommands = new JPanel( new FlowLayout( FlowLayout.CENTER ) );
		final JCheckBox chkAxes = new JCheckBox( Text.WindowPanel.SHOW_XYZ_AXES );
		final JCheckBox chkGrid = new JCheckBox( Text.WindowPanel.SHOW_COORDINATE_GRID );
		final private JButton cmdUpdate = new JButton( Text.WindowPanel.UPDATE );
		
	GraphPaper3D m_graphPaper;
	GraphInputsPanel pnlEquationInput;
	
	public WindowPanel() {
		
		setLayout( new BoxLayout( this , BoxLayout.Y_AXIS ) );
		add( this.pnlX );
		add( this.pnlY );
		add( this.pnlZ );
		
		this.chkAxes.setSelected( true );
		this.pnlCommands.add( this.chkAxes );
		
		this.chkGrid.setSelected( true );
		this.pnlCommands.add( this.chkGrid );
		
		this.pnlCommands.add( this.cmdUpdate );
		add( this.pnlCommands );
		
		addListeners();
	}
	
	public void setGraphPaper( GraphPaper3D graphPaper ) {
		this.m_graphPaper = graphPaper;
	}
	
	public void setEquationInputPanel( GraphInputsPanel p ) {
		this.pnlEquationInput = p;
	}

	private void addListeners() {
		this.cmdUpdate.addActionListener( new ActionListener() {

			@Override
			public void actionPerformed( ActionEvent e ) {
				DimensionData xData = WindowPanel.this.pnlX.getDimensionProperties();
				WindowPanel.this.m_graphPaper.setXAxisProperties( xData.m_min , xData.m_max , xData.m_inc );
				
				DimensionData yData = WindowPanel.this.pnlY.getDimensionProperties();
				WindowPanel.this.m_graphPaper.setYAxisProperties( yData.m_min , yData.m_max , yData.m_inc );
				
				DimensionData zData = WindowPanel.this.pnlZ.getDimensionProperties();
				WindowPanel.this.m_graphPaper.setZAxisProperties( zData.m_min , zData.m_max , zData.m_inc );
				
				WindowPanel.this.m_graphPaper.setAxesVisible( WindowPanel.this.chkAxes.isSelected() );
				WindowPanel.this.m_graphPaper.setGridVisible( WindowPanel.this.chkGrid.isSelected() );
				updateGraphPaper();
			}
			
		});
	}
	
	public void updateGraphPaper() {
		Platform.runLater( new Runnable() {

			@Override
			public void run() {
				WindowPanel.this.m_graphPaper.refreshDisplay();
				WindowPanel.this.pnlEquationInput.regraphEquations();
			}
			
		});
	}
	
}
