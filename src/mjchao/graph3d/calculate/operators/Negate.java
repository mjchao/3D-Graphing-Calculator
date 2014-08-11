package mjchao.graph3d.calculate.operators;

import mjchao.graph3d.calculate.UnaryPrefixOperator;
import mjchao.graph3d.calculate.Value;

public class Negate extends UnaryPrefixOperator {

	final public static String REPRESENTATION = "-";
	final public static int PRECEDENCE = 3;
	final public static boolean IS_LEFT_ASSOCIATIVE = false;
	
	public Negate() {
		super( REPRESENTATION , PRECEDENCE , IS_LEFT_ASSOCIATIVE );
	}

	@Override
	public Value operate( Value... args ) {
		Value toNegate = args[ 0 ];
		return toNegate.multiply( Value.NEGATIVE_ONE );
	}

}
