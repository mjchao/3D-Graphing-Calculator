package mc.graph3d.calculate.functions;

import mc.graph3d.calculate.Function;
import mc.graph3d.calculate.Value;

public class LogBase extends Function {

	final public static String REPRESENTATION = "logb";
	final public static int NUM_ARGS = 2;
	
	public LogBase() {
		super( REPRESENTATION , NUM_ARGS );
	}
	
	@Override
	public Value operate( int[] flags , Value... args ) {
		return Value.logBase( args[ 0 ] , args[ 1 ] );
	}
}
