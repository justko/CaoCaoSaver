package ko.justko.caocaosaver.block;

import ko.justko.caocaosaver.State;

public final class Single1Block extends BlockBase implements Cloneable {
	@Override
	public Single1Block clone() {
		return (Single1Block) super.clone();
	}

	public Single1Block(String name,int x, int y) {
		super(x, y, BlockType.SINGLE1,name);
	}

	@Override
	public boolean moveUp(State form) {
		if (form.isOccupied(x, y - 1))
			return false;
		form.unOccupy(x, y);
		form.occupy(x, y - 1);
		y--;
		return true;
	}

	@Override
	public boolean moveDown(State form) {
		if (form.isOccupied(x, y + 1))
			return false;
		form.unOccupy(x, y);
		form.occupy(x, y + 1);
		y++;
		return true;
	}

	@Override
	public boolean moveLeft(State form) {
		if (form.isOccupied(x - 1, y))
			return false;
		form.unOccupy(x, y);
		form.occupy(x-1, y);
		x--;
		return true;
	}

	@Override
	public boolean moveRight(State form) {
		if (form.isOccupied(x + 1, y))
			return false;
		form.unOccupy(x, y);
		form.occupy(x + 1, y);
		x++;
		return true;
	}

	@Override
	public boolean addTo(int x, int y, State form) {
		if (form.isOccupied(x, y))
			return false;
		form.occupy(x, y);
		this.x = x;
		this.y = y;
		return true;
	}

	@Override
	public String toString() {
		return name + "@(" + x + "," + y + ")";
	}

}
