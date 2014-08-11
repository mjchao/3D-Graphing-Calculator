package mjchao.graph3d.calculate.operators;

import mjchao.graph3d.calculate.BinaryOperator;
import mjchao.graph3d.calculate.Value;

public class Multiply extends BinaryOperator {

	final public static String REPRESENTATION = "*";
	final public static int PRECEDENCE = 2;
	final public static boolean IS_LEFT_ASSOCIATIVE = true;
	
	public Multiply() {
		super( REPRESENTATION , PRECEDENCE , IS_LEFT_ASSOCIATIVE );
	}

	@Override
	public Value operate( Value... args ) {
		Value left = args[ 0 ];
		Value right = args[ 1 ];
		return left.multiply( right );
	}

}
