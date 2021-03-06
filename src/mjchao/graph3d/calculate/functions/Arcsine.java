package mjchao.graph3d.calculate.functions;

import mjchao.graph3d.calculate.Calculate;
import mjchao.graph3d.calculate.Function;
import mjchao.graph3d.calculate.Value;

public class Arcsine extends Function {

	final public static String REPRESENTATION = "arcsin";
	final public static int NUM_ARGS = 1;
	
	public Arcsine() {
		super( REPRESENTATION , NUM_ARGS );
	}
	
	@Override
	public Value operate( int[] flags , Value... args ) {
		int angleMode = flags[ 0 ];
		return Calculate.getAngleFromRadAngle( angleMode , Value.asin( args[ 0 ] ) );
	}
}
