package mjchao.graph3d.ui;

import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import mjchao.graph3d.calculate.DefaultCalculate;
import mjchao.graph3d.calculate.Variable;
import mjchao.graph3d.graph.GraphPaper3D;
import mjchao.graph3d.graph.ParametricGraph;
import mjchao.graph3d.util.Text;

/**
 * panel for inputing parametrics x(t), y(t), z(t) that describe a graph
 * @author mjchao
 *
 */
public class ParametricEquationPanel extends EquationPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	final private InputField inptX = new InputField( Text.EquationPanel.INPUT_X_PARAMETRIC );
	final private InputField inptY = new InputField( Text.EquationPanel.INPUT_Y_PARAMETRIC );
	final private InputField inptZ = new InputField( Text.EquationPanel.INPUT_Z_PARAMETRIC );
	final private DimensionInputPanel pnlT = new DimensionInputPanel( Text.EquationPanel.T_DIMENSION );
	
	final private GraphPaper3D m_graphPaper;
	private ParametricGraph m_parametricGraph;
	
	private DefaultCalculate m_calcX = new DefaultCalculate();
	private DefaultCalculate m_calcY = new DefaultCalculate();
	private DefaultCalculate m_calcZ = new DefaultCalculate();
		final private Variable m_tVar = new Variable( Text.EquationPanel.T_DIMENSION );
	
	public ParametricEquationPanel( GraphPaper3D graphPaper ) {
		add( this.inptX );
		add( this.inptY );
		add( this.inptZ );
		add( this.pnlT );
		super.addColorChooser();
		super.addIgnoreOptions();
		setMaximumSize( getPreferredSize() );
		
		this.m_graphPaper = graphPaper;
		this.m_parametricGraph = new ParametricGraph( graphPaper );
		this.m_calcX.defineVariables( this.m_tVar );
		this.m_calcY.defineVariables( this.m_tVar );
		this.m_calcZ.defineVariables( this.m_tVar );
	}
	
	@Override
	public void disableModification() {
		super.disableModification();
		this.inptX.disableInput();
		this.inptY.disableInput();
		this.inptZ.disableInput();
		this.pnlT.disableInput();
	}
	
	@Override
	public void enableModification() {
		super.enableModification();
		this.inptX.enableInput();
		this.inptY.enableInput();
		this.inptZ.enableInput();
		this.pnlT.enableInput();
	}

	@Override
	public void createGraph( Thread graphThread , JProgressBar progress ) {
		if ( super.shouldGraph() ) {
			this.m_parametricGraph.clear();
			DimensionData tDimen = this.pnlT.getDimensionProperties();
			double tMin = tDimen.m_min; double tMax = tDimen.m_max; double tInc = tDimen.m_inc;
			double[] xDimenData = this.m_graphPaper.getXAxisProperties();
			double[] yDimenData = this.m_graphPaper.getYAxisProperties();
			double[] zDimenData = this.m_graphPaper.getZAxisProperties();
			double xMin = xDimenData[ 0 ]; double xMax = xDimenData[ 1 ];
			double yMin = yDimenData[ 0 ]; double yMax = yDimenData[ 1 ];
			double zMin = zDimenData[ 0 ]; double zMax = zDimenData[ 1 ];
			
			this.inptX.highlight();
			this.m_calcX.process( this.inptX.getInput() );
			this.inptX.unhighlight();
			double lastX = Double.NaN;
			
			this.inptY.highlight();
			this.m_calcY.process( this.inptY.getInput() );
			this.inptY.unhighlight();
			double lastY = Double.NaN;
			
			this.inptZ.highlight();
			this.m_calcZ.process( this.inptZ.getInput() );
			this.inptZ.unhighlight();
			double lastZ = Double.NaN;
			
			int totalCalculations = (int)( (tMax-tMin)/tInc );
			if ( progress != null ) {
				SwingUtilities.invokeLater( new Runnable() {
	
					@Override
					public void run() {
						progress.setMaximum( totalCalculations );
					}
					
				});
			}
			
			int calculations = 0;
			for ( double t = tMin ; t<tMax ; t+=tInc ) {
				this.m_tVar.set( t );
				double x = this.m_calcX.evaluate().value();
				double y = this.m_calcY.evaluate().value();
				double z = this.m_calcZ.evaluate().value();
				if ( xMin <= x && x <= xMax ) {
					if ( yMin <= y && y <= yMax ) {
						if ( zMin <= z && z <= zMax ) {
							
							//make sure the user didn't cancel the graphing yet
							if ( graphThread != null ) {
								if ( graphThread.isInterrupted() ) {
									return;
								}
							}
							
							if ( lastX == Double.NaN || lastY == Double.NaN || lastZ == Double.NaN ) {
								lastX = x;
								lastY = y;
								lastZ = z;
							}
							else {
								this.m_parametricGraph.addLineToGraphLater( lastX , lastY , lastZ , 
																			x , y , z , 
																			super.getSelectedMaterial() );
								lastX = x;
								lastY = y;
								lastZ = z;
							}
							calculations++;
							final int progressCalculations = calculations;
							if ( progress != null ) {
								SwingUtilities.invokeLater( new Runnable() {
	
									@Override
									public void run() {
											progress.setValue( progressCalculations );
									}
									
								});
							}
						}
					}
				}
			}
			this.m_parametricGraph.graphAllLines();
		}
		else {
			this.m_parametricGraph.ungraph();
		}
	}

	@Override
	public void removeGraph() {
		this.m_parametricGraph.ungraph();
	}

	@Override
	public String getEquationText() {
		return "    " + Text.EquationPanel.INPUT_X_PARAMETRIC + this.inptX.getInput() + "\n    " +
				Text.EquationPanel.INPUT_Y_PARAMETRIC + this.inptY.getInput() + "\n    " + 
				Text.EquationPanel.INPUT_Z_PARAMETRIC + this.inptY.getInput();
	}

}
