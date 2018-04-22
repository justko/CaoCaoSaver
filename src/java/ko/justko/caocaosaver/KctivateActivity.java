package ko.justko.caocaosaver;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class KctivateActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kctivate);
        Button openStore=findViewById(R.id.openStore);
        openStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String str = "market://details?id=" + "ko.justko.caocaosaver";
                    Intent localIntent = new Intent(Intent.ACTION_VIEW);
                    localIntent.setData(Uri.parse(str));
                    startActivity(localIntent);
                } catch (Exception e) {
                    // 打开应用商店失败 可能是没有手机没有安装应用市场
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "打开应用商店失败", Toast.LENGTH_SHORT).show();
                    // 调用系统浏览器进入商城
                    String url = "http://app.mi.com/detail/163525?ref=search";
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                }
            }
        });
    }
}
