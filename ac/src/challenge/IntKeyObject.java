package challenge;

public class IntKeyObject implements Comparable<IntKeyObject> {
	private Object val;
	private Integer key;
	private boolean ascending;
	
	public IntKeyObject(int key, Object val, boolean ascending) {
		this.key = key;
		this.val = val;
		this.ascending = ascending;
	}
	
	public Object getContent() {
		return val;
	}
	public Integer getKey() {
		return key;
	}

	@Override
	public int compareTo(IntKeyObject other) {
		return ascending ? getKey().compareTo(other.getKey()) : other.getKey().compareTo(this.getKey());
	}
	
}