package mjchao.graph3d.util;

import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;

/**
 * contains a collection of materials for modifying the appearances of graphs
 * 
 * @author mjchao
 *
 */
public class Materials {
	
	final public static MaterialData RED = createMaterialData( Color.RED , Text.Materials.RED );
	final public static MaterialData GREEN = createMaterialData( Color.GREEN , Text.Materials.GREEN );
	final public static MaterialData BLUE = createMaterialData( Color.BLUE , Text.Materials.BLUE );
	final public static MaterialData YELLOW = createMaterialData( Color.YELLOW , Text.Materials.YELLOW );
	final public static MaterialData ORANGE = createMaterialData( Color.ORANGE , Text.Materials.ORANGE );
	final public static MaterialData WHITE = createMaterialData( Color.WHITE , Text.Materials.WHITE );
	final public static MaterialData[] materials = { RED , GREEN , BLUE , YELLOW , ORANGE , WHITE };
	
	public static PhongMaterial createMaterial( Color diffuse , Color specular ) {
		PhongMaterial m = new PhongMaterial();
		m.setDiffuseColor( diffuse );
		m.setSpecularColor( specular );
		return m;
	}
	
	public static MaterialData createMaterialData( Color c , String name ) {
		return new MaterialData( createMaterial( c , c ) , c , name );
	}
	
	public static MaterialData[] getMaterialsList() {
		return materials;
	}

	public static class MaterialData {
		
		final public PhongMaterial m_material;
		final public Color m_color;
		final public String m_name;
		
		public MaterialData( PhongMaterial material , Color c , String name ) {
			this.m_material = material;
			this.m_color = c;
			this.m_name = name;
		}
	}
}
