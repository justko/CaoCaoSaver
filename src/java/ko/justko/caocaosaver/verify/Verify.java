package ko.justko.caocaosaver.verify;

import java.io.FileInputStream;
import android.content.Context;
import android.Manifest;
import android.app.Activity;
import android.content.ContextWrapper;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.Permission;

/**
 * Created by justko on 3/10/2018.
 */

public class Verify {
    public static int verify(Context context){
        try {
            FileInputStream fileInputStream = context.openFileInput("code");
            ObjectInputStream objectInputStream=new ObjectInputStream(fileInputStream);
            Cocode cocode=(Cocode)objectInputStream.readObject();
            fileInputStream.close();
            objectInputStream.close();
            return cocode.okLevel();
        }catch (ClassNotFoundException | IOException |NullPointerException e){
            Log.e("ko","查看验证文件错误"+e.getMessage());
            return 0;
        }
    }
}
