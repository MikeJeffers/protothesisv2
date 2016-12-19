package protothesisv2_city;

import java.util.List;

import toxi.geom.Vec2D;

public interface World {
	
	public List<Tile> getWorld();
	public void init();
	public Tile getTileBySelection(Vec2D target);
	public Tile[] getNeighborTiles(Tile central);
	public boolean setTileByPos(Vec2D target, TileType t);
	boolean setTile(TileType type, Tile tile);
	public boolean setTileType(TileType t, Tile tile);
	public boolean zoneTileByPos(Vec2D target, Zone z);
	public boolean zoneTile(Tile t, Zone z);
	public void step();
	
	public double getScore();
	
	public void render(Main p);
	

}
