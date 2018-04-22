package ko.justko.caocaosaver;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

public class KelpActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kelp);
        TextView para=findViewById(R.id.introduction_content_1);
        String s=getString(R.string.instruction_content_1);
        para.setText(Html.fromHtml(s));
    }
}
