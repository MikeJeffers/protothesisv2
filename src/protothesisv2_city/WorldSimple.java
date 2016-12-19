package protothesisv2_city;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import toxi.geom.Rect;
import toxi.geom.Vec2D;

public class WorldSimple implements World {
	private int size = 10;
	private int scale = 10;
	List<Tile> tiles = new ArrayList<Tile>();
	private TileType basic = new BasicTileType();
	private Random random;

	public WorldSimple(int size, int scale) {
		this.size = size;
		this.scale = scale;
	}

	@Override
	public void init() {
		for (int i = 0; i < size; i++) {
			for(int j =0; j<size; j++){
				Tile t = new BasicTile(new Vec2D(i*scale, j*scale), basic);
				tiles.add(t);
			}
		}

	}

	@Override
	public List<Tile> getWorld() {
		return this.tiles;
	}

	@Override
	public boolean setTile(TileType type, Tile tile) {
		Tile t = findTile(tile);
		return t.setType(type);
	}

	
	@Override
	public boolean zoneTile(Tile tile, Zone z) {
		Tile t = findTile(tile);
		return t.setZone(z);
	}

	@Override
	public void step() {
		for (Tile t : tiles) {
			t.step();
		}
	}

	@Override
	public void render(Main p) {
		
		p.fill(55);
		p.gfx.rect(new Rect(0, 0, size*scale, size*scale));
		for (Tile t : tiles) {
			t.render(p);
		}
	}

	@Override
	public boolean setTileByPos(Vec2D target, TileType type) {
		Tile targetTile = this.getTileBySelection(target);
		if (targetTile != null) {
			targetTile.setType(type);
			return true;
		}
		return false;
	}

	@Override
	public boolean zoneTileByPos(Vec2D target, Zone z) {
		Tile targetTile = this.getTileBySelection(target);
		if (targetTile != null) {
			targetTile.setZone(z);
			return true;
		}
		return false;
	}
	


	@Override
	public boolean setTileType(TileType type, Tile tile) {
		//TODO check null
		Tile t = findTile(tile);
		return t.setType(type);
	}

	
	
	private Tile findTile(Tile toFind){
		if(tiles.contains(toFind)){
			return tiles.get(tiles.indexOf(toFind));
		}
		return null;
	}

	@Override
	public Tile getTileBySelection(Vec2D target) {
		float min = Float.MAX_VALUE;
		Tile targetTile = null;
		for (Tile t : tiles) {
			float d = t.getPos().distanceTo(target);
			if (d < min) {
				min = d;
				targetTile = t;
			}
		}
		return targetTile;
	}

	@Override
	public Tile[] getNeighborTiles(Tile central) {
		int index = 0;
		if(tiles.contains(central)){
			index = tiles.indexOf(central);
		}
		int NW = index-size-1;
		int N = index-size;
		int NE = index-size+1;
		int W = index-1;
		int E = index+1;
		int SW = index+size-1;
		int S = index+size;
		int SE = index+size+1;
		Tile[] neighbors = new Tile[8];
		neighbors[0] = getTileIfIndexInBounds(NW);
		neighbors[1] = getTileIfIndexInBounds(N);
		neighbors[2] = getTileIfIndexInBounds(NE);
		neighbors[3] = getTileIfIndexInBounds(W);
		neighbors[4] = getTileIfIndexInBounds(E);
		neighbors[5] = getTileIfIndexInBounds(SW);
		neighbors[6] = getTileIfIndexInBounds(S);
		neighbors[7] = getTileIfIndexInBounds(SE);
		return neighbors;
	}
	
	private Tile getTileIfIndexInBounds(int index){
		if(index>0 && index<this.tiles.size()){
			return this.tiles.get(index);
		}
		return null;
	}

	@Override
	public double getScore() {
		if(this.random==null){
			this.random = new Random();
		}
		double d = random.nextDouble();
		System.out.print("Random Score is:"+d);
		return d;
	}




}
