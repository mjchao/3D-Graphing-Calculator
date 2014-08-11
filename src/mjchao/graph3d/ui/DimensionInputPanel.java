package mjchao.graph3d.ui;

import java.awt.FlowLayout;

import javax.swing.JPanel;

import mjchao.graph3d.calculate.DefaultCalculate;
import mjchao.graph3d.util.Text;

/**
 * allows the user to input properties of a dimension for a graph
 * 
 * @author mjchao
 *
 */
public class DimensionInputPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	final public static  int DEFAULT_INPUT_COLS = 3;
	final private String m_dimensionName;
	final private InputField inptMin = new InputField( DEFAULT_INPUT_COLS );
	final private InputField inptMax = new InputField( DEFAULT_INPUT_COLS );
	final private InputField inptInc = new InputField( DEFAULT_INPUT_COLS );
	final private DefaultCalculate m_calculate = new DefaultCalculate();
	
	public DimensionInputPanel( String dimensionName ) {
		setLayout( new FlowLayout( FlowLayout.CENTER ) );
		this.m_dimensionName = dimensionName;
		this.inptMin.setDescription( dimensionName + " " + Text.EquationPanel.DIMENSION_MIN );
		add( this.inptMin );
		this.inptMax.setDescription( dimensionName + " " + Text.EquationPanel.DIMENSION_MAX );
		add( this.inptMax );
		this.inptInc.setDescription( dimensionName + " " + Text.EquationPanel.DIMENSION_INC );
		add( this.inptInc );
		setDefaultDimensionProperties();
	}
	
	public void disableInput() {
		this.inptMin.disableInput();
		this.inptMax.disableInput();
		this.inptInc.disableInput();
	}
	
	public void enableInput() {
		this.inptMin.enableInput();
		this.inptMax.enableInput();
		this.inptInc.enableInput();
	}
	
	public void setDefaultDimensionProperties() {
		setDimensionProperties( -1 , 1 , 0.1 );
	}
	
	public void setDimensionProperties( double min , double max , double inc ) {
		this.inptMin.setInput( String.valueOf( min ) );
		this.inptMax.setInput( String.valueOf( max ) );
		this.inptInc.setInput( String.valueOf( inc ) );
	}
	
	public DimensionData getDimensionProperties() {
		this.inptMin.highlight();
		double min = this.m_calculate.evaluate( this.inptMin.getInput() ).value();
		this.inptMin.unhighlight();
		
		this.inptMax.highlight();
		double max = this.m_calculate.evaluate( this.inptMax.getInput() ).value();
		this.inptMax.unhighlight();
		
		this.inptInc.highlight();
		double inc = this.m_calculate.evaluate( this.inptInc.getInput() ).value();
		this.inptInc.unhighlight();
		
		return new DimensionData( this.m_dimensionName , min , max , inc );
	}
	
	private DimensionData m_lastGraphDimensions = null;
	
	/**
	 * temporarily saves the current dimension properties.
	 */
	public void cacheDimensionProperties() {
		this.m_lastGraphDimensions = getDimensionProperties();
	}
	
	/**
	 * @return			if the dimension properties changed since they were last used to
	 * 					render a graph
	 */
	public boolean changedSinceLastGraph() {
		if ( this.m_lastGraphDimensions == null ) {
			return true;
		}
		else {
			return this.m_lastGraphDimensions.equals( getDimensionProperties() );
		}
	}
}
