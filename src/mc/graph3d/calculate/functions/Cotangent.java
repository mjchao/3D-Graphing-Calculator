package mc.graph3d.calculate.functions;

import mc.graph3d.calculate.Calculate;
import mc.graph3d.calculate.Function;
import mc.graph3d.calculate.Value;

public class Cotangent extends Function {

	final public static String REPRESENTATION = "cot";
	final public static int NUM_ARGS = 1;
	
	public Cotangent() {
		super( REPRESENTATION , NUM_ARGS );
	}
	
	@Override
	public Value operate( int[] flags , Value... args ) {
		int angleMode = flags[ 0 ];
		return Value.cot( Calculate.getRadAngleFromFlag( angleMode , args[ 0 ] ) );
	}
}
