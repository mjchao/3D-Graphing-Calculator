package mjchao.graph3d.graph;
import javafx.geometry.Point3D;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;


/**
 * a quadrilateral in 3D space. the points should be provided in counter clockwise order
 * 
 * @author mjchao
 *
 */
class Quadrilateral3D extends Body {
	
	public Quadrilateral3D( Point3D p1 , Point3D p2 , Point3D p3 , Point3D p4 , boolean colorCulledFace , PhongMaterial material ) {
		
		TriangleMesh t1 = new TriangleMesh();
		t1.getPoints().setAll(
			(float)p1.getX() , (float)p1.getY() , (float)p1.getZ() ,
			(float)p2.getX() , (float)p2.getY() , (float)p2.getZ() ,
			(float)p3.getX() , (float)p3.getY() , (float)p3.getZ()
		);
		t1.getTexCoords().setAll(
			1, 1, // idx t0
			1, 0, // idx t1
			0, 1 // idx t2
		);
		t1.getFaces().setAll(
			0 , 1 , 2 , 0 , 1 , 2
		);
		MeshView tri1 = new MeshView( t1 );
		tri1.setMaterial( material );
		this.addComponent( tri1 );
		
		if ( colorCulledFace ) {
			TriangleMesh t1Back = new TriangleMesh();
			t1Back.getPoints().setAll(
				(float)p3.getX() , (float)p3.getY() , (float)p3.getZ() ,
				(float)p2.getX() , (float)p2.getY() , (float)p2.getZ() ,
				(float)p1.getX() , (float)p1.getY() , (float)p1.getZ()
			);
			t1Back.getTexCoords().setAll(
				1, 1, // idx t0
				1, 0, // idx t1
				0, 1 // idx t2
			);
			t1Back.getFaces().setAll(
				0 , 1 , 2 , 0 , 1 , 2
			);
			MeshView tri1Back = new MeshView( t1Back );
			tri1Back.setMaterial( material );
			this.addComponent( tri1Back );
		}
		
		TriangleMesh t2 = new TriangleMesh();
		t2.getPoints().setAll(
			(float)p3.getX() , (float)p3.getY() , (float)p3.getZ() ,
			(float)p4.getX() , (float)p4.getY() , (float) p4.getZ() , 
			(float)p1.getX() , (float)p1.getY() , (float)p1.getZ()
		);
		t2.getTexCoords().setAll(
				1, 1, // idx t0
				1, 0, // idx t1
				0, 1 // idx t2
		);
		t2.getFaces().setAll(
			0 , 1 , 2 , 0 , 1 , 2
		);
		MeshView tri2 = new MeshView( t2 );
		tri2.setMaterial( material );
		this.addComponent( tri2 );
		
		if ( colorCulledFace ) {
			TriangleMesh t2Back = new TriangleMesh();
			t2Back.getPoints().setAll(
				(float)p1.getX() , (float)p1.getY() , (float)p1.getZ() ,
				(float)p4.getX() , (float)p4.getY() , (float) p4.getZ() ,
				(float)p3.getX() , (float)p3.getY() , (float)p3.getZ()
			);
			t2Back.getTexCoords().setAll(
					1, 1, // idx t0
					1, 0, // idx t1
					0, 1 // idx t2
			);
			t2Back.getFaces().setAll(
				0 , 1 , 2 , 0 , 1 , 2
			);
			MeshView tri2Back = new MeshView( t2Back );
			tri2Back.setMaterial( material );
			this.addComponent( tri2Back );
		}
	}
}
