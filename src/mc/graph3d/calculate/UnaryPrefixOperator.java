package mc.graph3d.calculate;

/**
 * represents a unary operator that comes before (prefix) a value
 * 
 * @author mjchao
 *
 */
abstract public class UnaryPrefixOperator extends Operator {

	public UnaryPrefixOperator( String representation , int precedence , boolean leftAssociative ) {
		super( representation , 1 ,  precedence , leftAssociative );
	}

}
