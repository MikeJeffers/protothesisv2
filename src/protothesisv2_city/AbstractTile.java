package protothesisv2_city;

import toxi.geom.Rect;
import toxi.geom.Vec2D;

public abstract class AbstractTile implements Tile {
	private Vec2D pos;
	private TileType type;
	private Zone zone = new EmptyZone();
	
	
	@Override
	public Vec2D getPos() {
		return this.pos;
	}
	
	@Override
	public boolean setPos(Vec2D v) {
		if(this.pos!=null){
			return false;
		}
		//TODO add logic about pos restrictions of game world here
		this.pos = v;
		return true;
	}

	@Override
	public TileType getType() {
		return this.type;
	}

	@Override
	public boolean setType(TileType t) {
		this.type = t;
		return true;
	}

	@Override
	public boolean setZone(Zone z) {
		if(this.type.isZonable(z)){
			this.zone = z;
			return true;
		}
		return false;
	}

	@Override
	public Zone getZoning() {
		return this.zone;
	}

	@Override
	public void step() {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(Main p) {
		int[] color = zone.zoneColor();
		p.fill(color[0], color[1], color[2]);
		p.gfx.point(this.pos.scale(Main.SCALE));
		

	}

	@Override
	public int hashCode() {
		return this.hashCode();

	}

	@Override
	public boolean equals(Object other) {
		if (other != null && this != null) {
			if (other instanceof Tile) {
				Tile o = (Tile) other;
				return o.getPos().equals(this.getPos());
			}
		}
		return other == this;
	}





}
