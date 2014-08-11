package mc.graph3d.calculate.functions;

import mc.graph3d.calculate.Calculate;
import mc.graph3d.calculate.Function;
import mc.graph3d.calculate.Value;

public class Tangent extends Function {

	final public static String REPRESENTATION = "tan";
	final public static int NUM_ARGS = 1;
	
	public Tangent() {
		super( REPRESENTATION , NUM_ARGS );
	}
	
	@Override
	public Value operate( int[] flags , Value... args ) {
		int angleMode = flags[ 0 ];
		return Value.tan( Calculate.getRadAngleFromFlag( angleMode , args[ 0 ] ) );
	}
}