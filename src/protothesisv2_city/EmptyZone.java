package protothesisv2_city;

public class EmptyZone extends AbstractZone{
	
	public static final String TYPE = "_";
	
	private int[] col = new int[]{55, 55, 55};

	@Override
	public int[] zoneColor() {
		return col;
	}

	@Override
	public String type() {
		// TODO Auto-generated method stub
		return EmptyZone.TYPE;
	}

}
