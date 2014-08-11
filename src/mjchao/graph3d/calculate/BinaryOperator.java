package mjchao.graph3d.calculate;

/**
 * represents an operator that takes two values, a left and right operand
 * 
 * @author mjchao
 *
 */
abstract public class BinaryOperator extends Operator {

	public BinaryOperator( String representation , int precedence , boolean leftAssociative) {
		super( representation,  2 , precedence, leftAssociative);
	}

}
