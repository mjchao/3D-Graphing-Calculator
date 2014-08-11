package mjchao.graph3d.graph;

import javafx.geometry.Point3D;
import javafx.scene.paint.PhongMaterial;

/**
 * a line in 3D space
 * 
 * @author mjchao
 *
 */
class Line3D extends Body {

	final public static int DEFAULT_THICKNESS = 5;
	
	public Line3D( Point3D start , Point3D end , PhongMaterial material ) {
		super();
		
		double halfThickness = DEFAULT_THICKNESS/2;
		double startX = start.getX();
		double endX = end.getX();
		double thicknessDeficit = DEFAULT_THICKNESS - Math.abs( startX-endX );
		
		//if the start x is too close to the end x, then
		//the line may appear too thin when viewed from the xz or xy plane. 
		//so we would need to modify the starting and ending x's so that
		//the line has a noticable thickness
		double deltaStartX = 0;
		double deltaEndX = 0;
		if ( thicknessDeficit > 0 ) {
			if ( startX <= endX ) {
				deltaStartX -= thicknessDeficit/2;
				deltaEndX += thicknessDeficit/2;
			}
			else {
				deltaStartX += thicknessDeficit/2;
				deltaEndX -= thicknessDeficit/2;
			}
		}
		
		//we represent a line as a very thin parallelpiped 
		Point3D frontll = start.add( deltaStartX , halfThickness , halfThickness );
		Point3D frontul = start.add( deltaEndX , -halfThickness , halfThickness );
		Point3D frontlr = end.add( deltaStartX , halfThickness , halfThickness );
		Point3D frontur = end.add( deltaEndX , -halfThickness , halfThickness );
	
		Point3D backll = start.add( deltaStartX , halfThickness , -halfThickness );
		Point3D backul = start.add( deltaEndX , -halfThickness , -halfThickness );
		Point3D backlr = end.add( deltaStartX , halfThickness , -halfThickness );
		Point3D backur = end.add( deltaEndX , -halfThickness , -halfThickness );
		
		Quadrilateral3D frontFace = new Quadrilateral3D( frontll , frontlr , frontur , frontul , true , material );
		Quadrilateral3D topFace = new Quadrilateral3D( frontul , frontur , backur , backul , true , material );
		Quadrilateral3D backFace = new Quadrilateral3D( backll , backlr , backur , backul , true , material );
		Quadrilateral3D bottomFace = new Quadrilateral3D( frontll , frontlr , backlr , backll , true , material );
		Quadrilateral3D leftFace = new Quadrilateral3D( frontll , frontul , backul , backll , true , material );
		Quadrilateral3D rightFace = new Quadrilateral3D( frontlr , frontur , backur , backlr , true , material );
		this.getChildren().addAll( frontFace , topFace , bottomFace , leftFace , rightFace , backFace );
	}
}
