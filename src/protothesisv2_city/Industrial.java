package protothesisv2_city;

public class Industrial extends AbstractZone{
	
	public static final String TYPE = "I";

	private int[] col = new int[]{255,255,0};

	@Override
	public int[] zoneColor() {
		// TODO Auto-generated method stub
		return col;
	}

	@Override
	public String type() {
		return Industrial.TYPE;
	}

}
