package mjchao.graph3d.calculate;

/**
 * a constant value, e.g. "pi" or "e". we will treat constants as variables with unchanging
 * values
 * 
 * @author mjchao
 *
 */
public class Constant extends Variable {

	public Constant( String representation , double value  ) {
		super( representation , value );
	}
	
	/**
	 * do not use this method. a constant's value cannot be changed
	 */
	@Override
	protected void setValue( double v ) {
		
		//do nothing - a constant's value cannot be changed
	}

}
