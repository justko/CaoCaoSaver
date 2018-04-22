package ko.justko.caocaosaver.block;

import java.util.Comparator;

import ko.justko.caocaosaver.State;
public abstract class BlockBase implements Cloneable,Comparable<BlockBase>,Comparator<BlockBase> {
	protected String name;
	protected int x, y;
	protected BlockType blockType;

	public abstract boolean moveUp(State state);

	public abstract boolean moveDown(State state);

	public abstract boolean moveLeft(State state);

	public abstract boolean moveRight(State state);

	public abstract boolean addTo(int x, int y, State state);

	public boolean addTo(State state) {
		return addTo(x, y, state);
	}

	public BlockBase(int x, int y, BlockType blockType, String name) {
		this.x = x;
		this.y = y;
		this.blockType = blockType;
		this.name = name;
	}
	@Override
	public int hashCode(){
		return x*137+y*139+blockType.getOrder()*191;
	}

	public int getX() {
		return x;
	};

	public int getY() {
		return y;
	}

	@Override
	public BlockBase clone() {
		BlockBase blockBase = null;
		try {
			return (BlockBase) super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return blockBase;
	}

	public String getName() {
		return name;
	}
	
	@Override
	public int compareTo(BlockBase o) {
		if(blockType.getOrder()<o.blockType.getOrder())return 1;
		else if(blockType.getOrder()>o.blockType.getOrder())return -1;
		else if(x<o.x)return 1;
		else if(x>o.x)return -1;
		else if(y<o.y)return 1;
		else if(y>o.y)return -1;
		else return 0;
	}

	@Override
	public int compare(BlockBase o1, BlockBase o2) {
		return o1.compareTo(o2);
	}
}