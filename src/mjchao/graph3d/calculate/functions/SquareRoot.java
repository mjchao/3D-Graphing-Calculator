package mjchao.graph3d.calculate.functions;

import mjchao.graph3d.calculate.Function;
import mjchao.graph3d.calculate.Value;

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
