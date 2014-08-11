package mjchao.graph3d.calculate.functions;

import mjchao.graph3d.calculate.Function;
import mjchao.graph3d.calculate.Value;

public class Log10 extends Function {

	final public static String REPRESENTATION = "log10";
	final public static int NUM_ARGS = 1;
	
	public Log10() {
		super( REPRESENTATION , NUM_ARGS );
	}
	
	@Override
	public Value operate( int[] flags , Value... args ) {
		return Value.log10( args[ 0 ] );
	}
}
