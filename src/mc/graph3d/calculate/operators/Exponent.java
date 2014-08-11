package mc.graph3d.calculate.operators;

import mc.graph3d.calculate.BinaryOperator;
import mc.graph3d.calculate.Value;

public class Exponent extends BinaryOperator {

	final public static String REPRESENTATION = "^";
	final public static int PRECEDENCE = 3;
	final public static boolean IS_LEFT_ASSOCIATIVE = false;
	
	public Exponent() {
		super( REPRESENTATION , PRECEDENCE , IS_LEFT_ASSOCIATIVE );
	}

	@Override
	public Value operate( Value... args ) {
		Value left = args[ 0 ];
		Value right = args[ 1 ];
		return left.pow( right );
	}
}
