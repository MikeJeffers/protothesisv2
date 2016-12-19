package protothesisv2_city;

import toxi.geom.Vec2D;

public interface Tile {
	
	public Vec2D getPos();
	public boolean setPos(Vec2D v);
	
	public TileType getType();
	public boolean setType(TileType t);
	public boolean setZone(Zone z);
	public Zone getZoning();
	public void step();
	
	public void render(Main p);
	

}
