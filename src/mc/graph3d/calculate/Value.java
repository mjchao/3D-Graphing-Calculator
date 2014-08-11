package mc.graph3d.calculate;

/**
 * a token with a specific double value
 * 
 * @author mjchao
 *
 */
public class Value extends Token {

	final public static Value NEGATIVE_ONE = new Value( -1 );
	final public static Value PI = new Value( Math.PI );
	
	private double m_value;
	
	public Value( int value ) {
		this( (double) value );
	}
	
	public Value( float value ) {
		this( (double) value );
	}
	
	public Value( double value ) {
		super( String.valueOf( value ) );
		this.m_value = value;
	}
	
	public Value add( Value addend ) {
		return new Value( this.m_value + addend.value() ) ;
	}
	
	public Value subtract( Value subtrahend ) {
		return new Value( this.m_value - subtrahend.value() );
	}
	
	public Value multiply( Value multiplier ) {
		return new Value( this.m_value * multiplier.value() );
	}
	
	public Value divide( Value divisor ) {
		return new Value( this.m_value / divisor.value() );
	}
	
	public Value mod( Value divisor ) {
		return new Value( this.m_value % divisor.value() );
	}
	
	public Value pow( Value exponent ) {
		return new Value( Math.pow( this.m_value , exponent.value() ) );
	}
	
	public double value() {
		return this.m_value;
	}
	
	/**
	 * changes the double value of this token. be careful when using this, as values
	 * should technically be immutable, but this functionality is needed for variable values
	 * 
	 * @param v			the new value
	 */
	protected void setValue( double v ) {
		this.m_value = v;
	}
	
	public static Value convertDegToRad( Value degrees ) {
		return new Value( Math.toRadians( degrees.value() ) );
	}
	
	public static Value convertRadToDeg( Value radians ) {
		return new Value( Math.toDegrees( radians.value() ) );
	}
	
	public static Value sin( Value a ) {
		return new Value( Math.sin( a.value() ) );
	}
	
	public static Value cos( Value a ) {
		return new Value( Math.cos( a.value() ) );
	}
	
	public static Value tan( Value a ) {
		return new Value( Math.tan( a.value() ) );
	}
	
	public static Value cosec( Value a ) {
		return new Value( 1/Math.sin( a.value() ) );
	}
	
	public static Value sec( Value a ) {
		return new Value( 1/Math.cos( a.value() ) );
	}
	
	public static Value cot( Value a ) {
		return new Value( 1/Math.tan( a.value() ) );
	}
	
	public static Value asin( Value a ) {
		return new Value( Math.asin( a.value() ) );
	}
	
	public static Value acos( Value a ) {
		return new Value( Math.acos( a.value() ) );
	}
	
	public static Value atan( Value a ) {
		return new Value( Math.atan( a.value() ) );
	}
	
	public static Value log10( Value a ) {
		return new Value( Math.log10( a.value() ) );
	}
	
	public static Value ln( Value a ) {
		return new Value( Math.log( a.value() ) );
	}
	
	public static Value logBase( Value a , Value base ) {
		return new Value( Math.log( a.value() ) / Math.log( base.value() ) );
	}
	
	public static Value sqrt( Value a ) {
		return new Value( Math.sqrt( a.value() ) );
	}
}
