package ko.justko.caocaosaver.block;

public enum BlockType {
	SQUARE4(4),SINGLE1(1),HORIZONTAL2(3),VERTICAL2(2);
	public int getOrder(){
		return order;
	}
	BlockType(int order){
		this.order=order;
	}
	private final int order;
	
	
}
