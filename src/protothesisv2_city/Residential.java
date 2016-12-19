package protothesisv2_city;

public class Residential extends AbstractZone{
	
	public static final String TYPE = "R";
	
	private int[] col = new int[]{0, 255, 0};

	@Override
	public int[] zoneColor() {
		// TODO Auto-generated method stub
		return col;
	}

	@Override
	public String type() {
		// TODO Auto-generated method stub
		return Residential.TYPE;
	}


}
