package ko.justko.caocaosaver;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcel;
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
import java.io.ObjectOutputStream;
import java.security.Permission;

import ko.justko.caocaosaver.verify.Cocode;
import ko.justko.caocaosaver.verify.Encryption;
import ko.justko.caocaosaver.verify.Verify;


public class KboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kbout);
        final Button av = findViewById(R.id.activate);
        if(checkCallingOrSelfPermission("android.permission.READ_PHONE_STATE")== PackageManager.PERMISSION_GRANTED) {
            Log.d("ko","第一次进入if");
            TelephonyManager telephonyManager = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
            String id = telephonyManager.getDeviceId();
            TextView idContent = findViewById(R.id.idContent);
            idContent.setText(id);
        }else {
            Log.d("ko","开始请求权限");
            av.setEnabled(false);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE},100);
        }

        //激活按钮

        int level=Verify.verify(this);
        if(level>0){
            Log.d("ko","About从文件验证成功Level:"+level);
            av.setEnabled(false);
            av.setText("已激活"+level);
        }
        av.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView id=findViewById(R.id.idContent);
                EditText code=findViewById(R.id.codeInput);
                String idContent=id.getText().toString();
                String codeContent=code.getText().toString();
                Cocode cocode=new Cocode(codeContent,idContent);

                int level=cocode.okLevel();
                if(level>0){
                    try{
                        FileOutputStream fileOutputStream=openFileOutput("code",MODE_PRIVATE);
                        ObjectOutputStream objectOutputStream=new ObjectOutputStream(fileOutputStream);
                        objectOutputStream.writeObject(cocode);
                        fileOutputStream.close();
                        objectOutputStream.close();
                    }catch (IOException e){
                        Log.e("ko",e.getMessage());
                        Toast.makeText(KboutActivity.this,"保存验证状态失败",Toast.LENGTH_SHORT).show();
                    }
                    av.setEnabled(false);
                    av.setText("已激活"+level);
                }else {
                    Toast.makeText(KboutActivity.this,"验证错误",Toast.LENGTH_SHORT).show();
                }
            }
        });


        TextView know=findViewById(R.id.know);
        know.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(KboutActivity.this,KctivateActivity.class);
                startActivity(intent);
            }
        });

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case 100:
                if(grantResults[0]==0) {
                    if(checkCallingOrSelfPermission("android.permission.READ_PHONE_STATE")== PackageManager.PERMISSION_GRANTED) {
                        TelephonyManager telephonyManager = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
                        String id = telephonyManager.getDeviceId();
                        TextView idContent = findViewById(R.id.idContent);
                        idContent.setText(id);
                        findViewById(R.id.activate).setEnabled(true);
                    }
                }else{
                    Toast.makeText(this, "获取权限失败", Toast.LENGTH_SHORT).show();
                    findViewById(R.id.activate).setEnabled(false);
                }
                break;
        }
    }
}
