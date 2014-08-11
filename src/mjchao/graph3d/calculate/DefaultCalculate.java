package mjchao.graph3d.calculate;

import mjchao.graph3d.calculate.constants.E;
import mjchao.graph3d.calculate.constants.Pi;
import mjchao.graph3d.calculate.functions.Arccosine;
import mjchao.graph3d.calculate.functions.Arcsine;
import mjchao.graph3d.calculate.functions.Arctangent;
import mjchao.graph3d.calculate.functions.Cosecant;
import mjchao.graph3d.calculate.functions.Cosine;
import mjchao.graph3d.calculate.functions.Cotangent;
import mjchao.graph3d.calculate.functions.Log10;
import mjchao.graph3d.calculate.functions.LogBase;
import mjchao.graph3d.calculate.functions.LogE;
import mjchao.graph3d.calculate.functions.Secant;
import mjchao.graph3d.calculate.functions.Sine;
import mjchao.graph3d.calculate.functions.SquareRoot;
import mjchao.graph3d.calculate.functions.Tangent;
import mjchao.graph3d.calculate.operators.Divide;
import mjchao.graph3d.calculate.operators.Exponent;
import mjchao.graph3d.calculate.operators.Minus;
import mjchao.graph3d.calculate.operators.Multiply;
import mjchao.graph3d.calculate.operators.Plus;

/**
 * a default calculations environment that contains the following predefined symbols:
 * <ul>Operators
 * 	<li>Addition = "+"
 * 	<li>Subtraction = "-"
 * 	<li>Multiplication = "*";
 * 	<li>Division = "/"
 * 	<li>Negation = "-" (negative sign)
 *  <li>Exponentiation = "^"
 * </ul> 
 * <ul>Functions
 * 	<li>Sine = "sin"
 * 	<li>Cosine = "cos"
 *  <li>Tangnet = "tan"
 *  <li>Cosecant = "cosec"
 *  <li>Secant = "sec"
 *  <li>Cotangent = "cot"
 *  <li>Arcsine = "arcsin"
 *  <li>Arccosine = "arccos"
 *  <li>Arctangent = "arctan"
 *  <li>Log Base 10 = "log10"
 *  <li>Log Base e = "ln"
 *  <li>Log Arbitrary Base = "logb"
 *  <li>Square Root = "sqrt"
 * </ul>
 * 
 * <ul>Constants
 * 	<li>pi = 3.141592653589793
 *  <li>e = 2.718281828459045
 * </ul>
 * @author mjchao
 *
 */
public class DefaultCalculate extends Calculate {

	//DEBUG
	/*
	final public static void main( String[] args ) {
		DefaultCalculate c = new DefaultCalculate();
		c.setAngleType( Calculate.DEGREE_MODE );
		c.process( "-pi/2" );
		System.out.println( c.evaluate() );
	}//*/
	
	public DefaultCalculate() {
		addDefaultPredefinedTokens();
	}
	
	final private void addDefaultPredefinedTokens() {
		defineOperators( new Plus() , new Minus() , new Multiply() , new Divide() , new Exponent() );
		defineFunctions( new Sine() , new Cosine() , new Tangent() , 
				new Cosecant() , new Secant() , new Cotangent() , 
				new Arcsine() , new Arccosine() , new Arctangent() , 
				new Log10() , new LogE() , new LogBase() ,
				new SquareRoot() );
		defineConstants( new Pi() , new E() );
	}
}
