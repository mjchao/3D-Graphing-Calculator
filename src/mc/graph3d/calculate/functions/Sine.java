package mc.graph3d.calculate.functions;

import mc.graph3d.calculate.Calculate;
import mc.graph3d.calculate.Function;
import mc.graph3d.calculate.Value;

public class Sine extends Function {
	
	final public static String REPRESENTATION = "sin";
	final public static int NUM_ARGUMENTS = 1;
	
	public Sine() {
		super( REPRESENTATION , NUM_ARGUMENTS );
	}

	@Override
	public Value operate( int[] flags , Value... args ) {
		int angleMode = flags[ 0 ];
		return Value.sin( Calculate.getRadAngleFromFlag( angleMode , args[ 0 ] ) );
	}
	
}
