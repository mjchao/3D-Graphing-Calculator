package mjchao.graph3d.ui;

import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import mjchao.graph3d.calculate.DefaultCalculate;
import mjchao.graph3d.calculate.Variable;
import mjchao.graph3d.graph.FunctionGraph;
import mjchao.graph3d.graph.GraphPaper3D;
import mjchao.graph3d.util.Text;

/**
 * allows the user to input an equation z(x, y) that descibes one graph
 * @author mjchao
 *
 */
public class FunctionEquationPanel extends EquationPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	final private InputField inptFunction = new InputField( Text.EquationPanel.INPUT_FUNCTION );
	final private DimensionInputPanel pnlX = new DimensionInputPanel( Text.EquationPanel.X_DIMENSION );
	final private DimensionInputPanel pnlY = new DimensionInputPanel( Text.EquationPanel.Y_DIMENSION );

	final private GraphPaper3D m_graphPaper;
	private FunctionGraph m_functionGraph;
	
	private DefaultCalculate m_calculateEnvironment = new DefaultCalculate();
		final private Variable m_xVar = new Variable( Text.EquationPanel.X_DIMENSION );
		final private Variable m_yVar = new Variable( Text.EquationPanel.Y_DIMENSION );
		
	public FunctionEquationPanel( GraphPaper3D graphPaper ) {
		add( this.inptFunction );
		add( this.pnlX );
		add( this.pnlY );
		super.addColorChooser();
		super.addIgnoreOptions();
		setMaximumSize( getPreferredSize() );
	
		this.m_graphPaper = graphPaper;
		this.m_functionGraph = new FunctionGraph( graphPaper );
		this.m_calculateEnvironment.defineVariables( this.m_xVar , this.m_yVar );
	}
	
	private String m_input = null;
	private DimensionData m_xDimen = null;
	private DimensionData m_yDimen = null;
	private void cacheInputs() {
		this.m_input = this.inptFunction.getInput();
		this.m_xDimen = this.pnlX.getDimensionProperties();
		this.m_yDimen = this.pnlY.getDimensionProperties();
	}
	
	private boolean changed() {
		if ( this.m_input == null || this.m_xDimen == null || this.m_yDimen == null ) {
			return true;
		}
		else {
			return !( this.m_input.equals( this.inptFunction.getInput() ) && this.m_xDimen.equals( this.pnlX.getDimensionProperties() ) && this.m_yDimen.equals( this.pnlY.getDimensionProperties() ) );
		}
	}
	
	@Override
	public void disableModification() {
		super.disableModification();
		this.inptFunction.disableInput();
		this.pnlX.disableInput();
		this.pnlY.disableInput();
	}
	
	@Override
	public void enableModification() {
		super.enableModification();
		this.inptFunction.enableInput();
		this.pnlX.enableInput();
		this.pnlY.enableInput();
	}
	
	@Override
	public void createGraph( Thread graphThread , JProgressBar progress ) {
		if ( super.shouldGraph() ) {
			if ( changed() ) {
				cacheInputs();
				this.m_functionGraph.clear();
				DimensionData xDimen = this.pnlX.getDimensionProperties();
				DimensionData yDimen = this.pnlY.getDimensionProperties();
				double[] zAxisData = this.m_graphPaper.getZAxisProperties();
				
				this.inptFunction.highlight();
				this.m_calculateEnvironment.process( this.inptFunction.getInput() );
				this.inptFunction.unhighlight();
				
				double xMin = xDimen.m_min; double xMax = xDimen.m_max; double xInc = xDimen.m_inc;
				double yMin = yDimen.m_min; double yMax = yDimen.m_max; double yInc = yDimen.m_inc;
				double zMin = zAxisData[ 0 ]; double zMax = zAxisData[ 1 ];
				
				int totalCalculations = (int)(( (xMax-xMin)/xInc )*( (yMax-yMin)/yInc ));
				if ( progress != null ) {
					SwingUtilities.invokeLater( new Runnable() {
	
						@Override
						public void run() {
							progress.setMaximum( totalCalculations );
						}
						
					});
				}
				
				int calculationsPerformed = 0;
				for ( double x=xMin ; x<xMax ; x+=xInc ) {
					for ( double y=yMin ; y<yMax ; y+=yInc ) {
						this.m_xVar.set( x );
						this.m_yVar.set( y );
						double z = this.m_calculateEnvironment.evaluate().value();
						
						if ( zMin<=z && z<=zMax ) {
							double nextX = x+xInc;
							this.m_xVar.set( nextX );
							this.m_yVar.set( y );
							double zNextXCurrY = this.m_calculateEnvironment.evaluate().value();
							
							if ( zMin<=zNextXCurrY && zNextXCurrY<=zMax ) {
								
								double nextY = y+yInc;
								this.m_xVar.set( nextX );
								this.m_yVar.set( nextY );
								double zNextXNextY = this.m_calculateEnvironment.evaluate().value();
								if ( zMin<=zNextXNextY && zNextXNextY<=zMax ) {
									
									//make sure the user did not cancel the graphing
									if ( graphThread != null ) {
										if ( graphThread.isInterrupted() ) {
											return;
										}
									}
									
									this.m_xVar.set( x );
									this.m_yVar.set( nextY );
									double zCurrXNextY = this.m_calculateEnvironment.evaluate().value();
									
									if ( zMin<=zCurrXNextY && zCurrXNextY<=zMax ) {
										this.m_functionGraph.addQuadrilateralToGraphLater( x , y , z , 
																				nextX , y , zNextXCurrY ,
																				nextX , nextY , zNextXNextY ,
																				x , nextY , zCurrXNextY , 
																				super.getSelectedMaterial() );
									}
									
									try {
										Thread.sleep( 10 );
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
								}
							}
						}
						
						//update the user on the graphing progress
						calculationsPerformed++;
						if ( progress != null ) {
							final int progressVal = calculationsPerformed;
							SwingUtilities.invokeLater(
								new Runnable() {
	
									@Override
									public void run() {
										progress.setValue( progressVal );
									}
									
								}	
							);
						}
					}
				}
			}
			this.m_functionGraph.graphAllQuadrilaterals();
		}
		else {
			this.m_functionGraph.ungraph();
		}
	}
	
	@Override
	public void removeGraph() {
		this.m_functionGraph.ungraph();
	}
	
	@Override
	public String getEquationText() {
		return "    " + Text.EquationPanel.INPUT_FUNCTION + this.inptFunction.getInput();
	}

}
