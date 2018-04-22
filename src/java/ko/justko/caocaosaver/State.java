package ko.justko.caocaosaver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import ko.justko.caocaosaver.block.BlockBase;
import ko.justko.caocaosaver.block.BlockOperation;

public class State implements Cloneable {
	private int width;
	private int height;
	private boolean[][] occupation;
	private ArrayList<BlockBase> blockBases;
	private ArrayList<String> names;
	private ArrayList<BlockOperation> operations;
	
	public State() {
		height = 5;
		width = 4;
		names=new ArrayList<>();
		operations=new ArrayList<>();
		blockBases = new ArrayList<>();
		occupation = new boolean[height][width];
		for (boolean[] row : occupation) {
			Arrays.fill(row, false);
		}
	}

	public void reSolve(){
		names=new ArrayList<>();
		operations=new ArrayList<>();
	}
	@Override
	public int hashCode(){
		int code=width*191+height*197+blockBases.size()*1327;
		for (int i = 0; i < blockBases.size(); ++i) {
			code=code^blockBases.get(i).hashCode()>>3;
		}
		return code;
	}
	@Override
	public boolean equals(Object object) {
		if (object instanceof State) {
			State state = (State) object;
			if (width == state.width && height == state.height) {
				for (int i = 0; i < blockBases.size(); ++i) {
					if (blockBases.get(i).compareTo(state.blockBases.get(i)) != 0) {
						return false;
					}
				}
				return true;
			}
			return false;
		} else {
			return false;
		}
	}

	public boolean add(BlockBase blockBase) {
		if (blockBase.addTo(this)) {
			blockBases.add(blockBase);
			Collections.sort(blockBases);
			return true;
		} else {
			return false;
		}
	}

	public String getInfo() {
		StringBuilder stringBuilder = new StringBuilder();
		for (BlockBase blockBase : blockBases) {
			stringBuilder.append(blockBase.toString());
			stringBuilder.append('\n');
		}
		return stringBuilder.toString();
	}

	public boolean move(String name, BlockOperation blockOperation) {
		for (BlockBase blockBase : blockBases) {
			if (name.equals(blockBase.getName())) {
				switch (blockOperation) {
				case UP:
					if(blockBase.moveUp(this)){
						names.add(blockBase.getName());
						operations.add(BlockOperation.UP);
						Collections.sort(blockBases);
						return true;
					}
					return false;
				case DOWN:
					if(blockBase.moveDown(this)){
						names.add(blockBase.getName());
						operations.add(BlockOperation.DOWN);
						Collections.sort(blockBases);
						return true;
					}
					return false;
				case LEFT:
					if(blockBase.moveLeft(this)){
						names.add(blockBase.getName());
						operations.add(BlockOperation.LEFT);
						Collections.sort(blockBases);
						return true;
					}
					return false;
				case RIGHT:
					if(blockBase.moveRight(this)){
						names.add(blockBase.getName());
						operations.add(BlockOperation.RIGHT);
						Collections.sort(blockBases);
						return true;
					}
					return false;
				default:
					return false;
				}
			}
		}
		return false;
	}

	@Override
	public State clone() {
		State newForm = null;
		try {
			newForm = (State) super.clone();
		} catch (CloneNotSupportedException e) {
			System.out.println(e.getStackTrace());
		}
		assert (newForm != null);
		newForm.occupation = occupation.clone();
		for (int i = 0; i < height; ++i) {
			newForm.occupation[i] = occupation[i].clone();
		}
		newForm.blockBases = new ArrayList<>();
		for (BlockBase blockBase : blockBases) {
			newForm.blockBases.add(blockBase.clone());
		}
		newForm.names=new ArrayList<>(names);
		newForm.operations=new ArrayList<>(operations);
		return newForm;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public boolean isOccupied(int x, int y) {
		if (x < 0 || x >= width || y < 0 || y >= height)
			return true;
		return occupation[y][x];
	}

	public void occupy(int x, int y) {
		occupation[y][x] = true;
	}

	public void unOccupy(int x, int y) {
		occupation[y][x] = false;
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		for (boolean[] row : occupation) {
			for (boolean i : row)
				if (i)
					stringBuilder.append("●");
				else
					stringBuilder.append("○");
			stringBuilder.append("\n");
		}
		return stringBuilder.toString();
	}
	public ArrayList<String> getNames(){
		return names;
	}
	public ArrayList<BlockOperation> getOperations(){
		return operations;
	}
	public ArrayList<BlockBase> getBlockBases(){
		return blockBases;
	}
}
