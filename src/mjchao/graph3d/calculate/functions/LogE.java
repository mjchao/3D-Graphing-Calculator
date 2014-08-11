package mjchao.graph3d.calculate.functions;

import mjchao.graph3d.calculate.Function;
import mjchao.graph3d.calculate.Value;

public class LogE extends Function {

	final public static String REPRESENTATION = "ln";
	final public static int NUM_ARGS = 1;
	
	public LogE() {
		super( REPRESENTATION , NUM_ARGS );
	}
	
	@Override
	public Value operate( int[] flags , Value... args ) {
		return Value.ln( args[ 0 ] );
	}
}
