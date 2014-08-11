package mjchao.graph3d.calculate.functions;

import mjchao.graph3d.calculate.Calculate;
import mjchao.graph3d.calculate.Function;
import mjchao.graph3d.calculate.Value;

public class Arccosine extends Function {

	final public static String REPRESENTATION = "arccos";
	final public static int NUM_ARGS = 1;
	
	public Arccosine() {
		super( REPRESENTATION , NUM_ARGS );
	}
	
	@Override
	public Value operate( int[] flags , Value... args ) {
		int angleMode = flags[ 0 ];
		return Calculate.getAngleFromRadAngle( angleMode , Value.acos( args[ 0 ] ) );
	}
}
