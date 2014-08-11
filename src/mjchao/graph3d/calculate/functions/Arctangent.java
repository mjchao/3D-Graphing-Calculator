package mjchao.graph3d.calculate.functions;

import mjchao.graph3d.calculate.Calculate;
import mjchao.graph3d.calculate.Function;
import mjchao.graph3d.calculate.Value;

public class Arctangent extends Function {

	final public static String REPRESENTATION = "arctan";
	final public static int NUM_ARGS = 1;
	
	public Arctangent() {
		super( REPRESENTATION , NUM_ARGS );
	}
	
	@Override
	public Value operate( int[] flags , Value... args ) {
		int angleMode = flags[ 0 ];
		return Calculate.getAngleFromRadAngle( angleMode , Value.atan( args[ 0 ] ) );
	}
}
