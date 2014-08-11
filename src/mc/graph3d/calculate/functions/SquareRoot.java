package mc.graph3d.calculate.functions;

import mc.graph3d.calculate.Function;
import mc.graph3d.calculate.Value;

public class SquareRoot extends Function {
	
	final public static String REPRESENTATION = "sqrt";
	final public static int NUM_ARGS = 1;
	
	public SquareRoot() {
		super( REPRESENTATION , NUM_ARGS );
	}

	@Override
	public Value operate( int[] flags , Value... args ) {
		return Value.sqrt( args[ 0 ] );
	}

}
