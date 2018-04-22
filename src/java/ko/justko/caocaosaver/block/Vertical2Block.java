package ko.justko.caocaosaver.block;

import ko.justko.caocaosaver.State;

public final class Vertical2Block extends BlockBase {

	public Vertical2Block(String name,int x, int y) {
		super(x, y, BlockType.VERTICAL2,name);
	}
	@Override
	public boolean moveUp(State form) {
		if (form.isOccupied(x, y - 1))
			return false;
		form.occupy(x, y - 1);
		form.unOccupy(x, y + 1);
		y--;
		return true;
	}

	@Override
	public boolean moveDown(State form) {
		if (form.isOccupied(x, y + 2))
			return false;
		form.occupy(x, y + 2);
		form.unOccupy(x, y);
		y++;
		return true;
	}

	@Override
	public boolean moveLeft(State form) {
		if (form.isOccupied(x - 1, y) || form.isOccupied(x - 1, y + 1))
			return false;
		form.occupy(x - 1, y);
		form.occupy(x - 1, y + 1);
		form.unOccupy(x, y);
		form.unOccupy(x, y + 1);
		x--;
		return true;
	}

	@Override
	public boolean moveRight(State form) {
		if (form.isOccupied(x + 1, y) || form.isOccupied(x + 1, y + 1))
			return false;
		form.occupy(x + 1, y);
		form.occupy(x + 1, y + 1);
		form.unOccupy(x, y);
		form.unOccupy(x, y + 1);
		x++;
		return true;
	}

	@Override
	public boolean addTo(int x, int y, State form) {
		if (form.isOccupied(x, y) || form.isOccupied(x, y + 1))
			return false;
		form.occupy(x, y);
		form.occupy(x, y + 1);
		this.x = x;
		this.y = y;
		return true;
	}
	@Override
	public String toString(){
		return name+"@("+x+","+y+")";
	}

}
