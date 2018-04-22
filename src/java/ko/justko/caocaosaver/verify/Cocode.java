package ko.justko.caocaosaver.verify;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by justko on 3/10/2018.
 */

public class Cocode implements Serializable {
    private String code;
    private String id;

    public Cocode(String code,String id){
        this.code=code;
        this.id=id;
    }

    public int okLevel(){
        Log.d("ko","验证中...code:"+code+",id:"+id);
        if(code.equals(Encryption.encrypt(id+"first"))){
            return 1;
        }else if(code.equals(Encryption.encrypt(id+"second"))){
            return 2;
        }else if(code.equals(Encryption.encrypt(id+"third"))){
            return 3;
        }else{
            return 0;
        }
    }
}
