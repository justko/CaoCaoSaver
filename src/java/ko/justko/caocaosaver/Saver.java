package ko.justko.caocaosaver;

import android.os.Bundle;
import android.os.Message;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import android.os.Handler;
import android.util.Log;

import ko.justko.caocaosaver.block.BlockBase;
import ko.justko.caocaosaver.block.BlockOperation;

public class Saver {
	private HashSet<State> allStates;
	private LinkedList<State> remainStates;
	private ArrayList<String> names;
	private ArrayList<BlockOperation> operations;
	private boolean terminate;
	private boolean solved;
	private int tested;
	public boolean getSolved(){
		return solved;
	}
	public Saver(State state) {
		allStates=new HashSet<>();
		remainStates=new LinkedList<>();
		allStates.add(state);
		remainStates.add(state);
		names=null;
		operations=null;
		terminate=false;
		solved=false;
		tested=0;

	}
	public void setTerminate(boolean terminate){
		this.terminate=terminate;
	}
	public ArrayList<String> getNames(){
		return names;
	}
	public ArrayList<BlockOperation> getOperations(){
		return operations;
	}
	public boolean solve(Handler handler){
		tested=0;
		while(!terminate&&!remainStates.isEmpty()){
			State aState=remainStates.pollFirst();
			tested++;
			if(tested%1000==0){
				Message message=new Message();
				Bundle bundle=new Bundle();
				message.what=1;
				bundle.putString("tested",Integer.toString(tested));
				bundle.putString("allStates",Integer.toString(allStates.size()));
				bundle.putString("remainStates",Integer.toString(remainStates.size()));
				message.setData(bundle);
				handler.sendMessage(message);
				Log.d("ko","处理:"+tested+",所知:"+allStates.size()+",剩余:"+remainStates.size());
			}
			int x=aState.getBlockBases().get(0).getX();
			int y=aState.getBlockBases().get(0).getY();
			if(x==1&&y==3){
				names=aState.getNames();
				operations=aState.getOperations();
				solved=true;
				return true;
			}
			State attemptState=aState.clone();
			for(BlockBase blockBase:aState.getBlockBases()){
				for(BlockOperation blockOperation:BlockOperation.values()){
					if(attemptState.move(blockBase.getName(), blockOperation)){
						if(!allStates.contains(attemptState)){
							allStates.add(attemptState);
							remainStates.add(attemptState);
						}
						attemptState=aState.clone();
					}
				}
			}
		}
		return false;
	}
}