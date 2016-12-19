package protothesisv2_city;

import toxi.geom.Rect;
import toxi.geom.Vec2D;

public class BasicTile extends AbstractTile{
	
	
	public BasicTile(Vec2D center, TileType type){
		this.setPos(center);
		this.setType(type);
	}

	@Override
	public void render(Main p) {
		p.stroke(255);
		Vec2D center = this.getPos();
		super.render(p);
		p.gfx.rect(new Rect(center.x, center.y, Main.SCALE, Main.SCALE ));
		
		
	}
	

}
