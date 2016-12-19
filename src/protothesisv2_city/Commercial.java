package protothesisv2_city;

public class Commercial extends AbstractZone{
	
	public static final String TYPE = "R";
	
	private int[] col = new int[]{0,0,255};

	@Override
	public int[] zoneColor() {
		// TODO Auto-generated method stub
		return col;
	}

	@Override
	public String type() {
		// TODO Auto-generated method stub
		return Commercial.TYPE;
	}

}
