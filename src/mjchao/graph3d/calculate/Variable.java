package mjchao.graph3d.calculate;


/**
 * a token with a variable value that could change. the value is represented by some text, 
 * e.g. "x" or "y" or "z"
 * 
 * @author mjchao
 *
 */
public class Variable extends Value {
	
	/**
	 * creates a variable with the default value of 0. WARNING: Watch out for divide-by-zero
	 * 
	 * @param representation
	 */
	public Variable( String representation ) {
		this( representation , 0 );
	}
	
	/**
	 * creates a variable with the given value
	 * 
	 * @param representation
	 * @param value
	 */
	public Variable( String representation , double value ) {
		super( value );
		setRepresentation( representation );
	}

	/**
	 * sets the value of this variable to the specified value
	 * 
	 * @param newValue	new value of this variable
	 */
	public void set( double newValue ) {
		super.setValue( newValue );
	}
	
	@Override
	public boolean equals( Object o ) {
		if ( o instanceof Variable ) {
			Variable v = ( Variable ) o;
			return this.getRepresentation().equals( v.getRepresentation() );
		}
		else {
			return false;
		}
	}
}
