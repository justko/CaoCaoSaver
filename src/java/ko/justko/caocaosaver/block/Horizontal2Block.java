package ko.justko.caocaosaver.block;

import ko.justko.caocaosaver.State;


public final class Horizontal2Block extends BlockBase implements Cloneable {

	@Override
	public Horizontal2Block clone() {
		return (Horizontal2Block) super.clone();
	}

	public Horizontal2Block(String name,int x, int y) {
		super(x, y, BlockType.HORIZONTAL2,name);
	}


	@Override
	public boolean moveUp(State form) {
		if (form.isOccupied(x, y - 1) || form.isOccupied(x + 1, y - 1))
			return false;
		form.unOccupy(x, y);
		form.unOccupy(x + 1, y);
		form.occupy(x, y - 1);
		form.occupy(x + 1, y - 1);
		y--;
		return true;
	}

	@Override
	public boolean moveDown(State form) {
		if (form.isOccupied(x, y + 1) || form.isOccupied(x + 1, y + 1))
			return false;
		form.unOccupy(x, y);
		form.unOccupy(x + 1, y);
		form.occupy(x, y + 1);
		form.occupy(x + 1, y + 1);
		y++;
		return true;
	}

	@Override
	public boolean moveLeft(State form) {
		if (form.isOccupied(x - 1, y))
			return false;
		form.occupy(x - 1, y);
		form.unOccupy(x + 1, y);
		x--;
		return true;
	}

	@Override
	public boolean moveRight(State form) {
		if (form.isOccupied(x + 2, y)) {
			return false;
		}
		form.occupy(x + 2, y);
		form.unOccupy(x, y);
		x++;
		return true;
	}

	@Override
	public boolean addTo(int x, int y, State form) {
		if (form.isOccupied(x, y) || form.isOccupied(x + 1, y))
			return false;
		form.occupy(x, y);
		form.occupy(x + 1, y);
		this.x = x;
		this.y = y;
		return true;
	}
	@Override
	public String toString(){
		return name+"@("+x+","+y+")";
	}
}
