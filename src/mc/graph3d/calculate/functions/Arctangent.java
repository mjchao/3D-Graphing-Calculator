package mc.graph3d.calculate.functions;

import mc.graph3d.calculate.Calculate;
import mc.graph3d.calculate.Function;
import mc.graph3d.calculate.Value;

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
