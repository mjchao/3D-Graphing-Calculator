package mc.graph3d.calculate.operators;

import mc.graph3d.calculate.BinaryOperator;
import mc.graph3d.calculate.Value;

public class Plus extends BinaryOperator {

	final public static String REPRESENTATION = "+";
	final public static int PRECEDENCE = 1;
	final public static boolean IS_LEFT_ASSOCIATIVE = true;
	
	public Plus() {
		super( REPRESENTATION , PRECEDENCE , IS_LEFT_ASSOCIATIVE );
	}
	
	@Override
	public Value operate( Value... args ) {
		Value left = args[ 0 ];
		Value right = args[ 1 ];
		return left.add( right );
	}
}
