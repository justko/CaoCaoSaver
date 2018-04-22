package ko.justko.caocaosaver;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Random;

import ko.justko.caocaosaver.block.Horizontal2Block;
import ko.justko.caocaosaver.block.Single1Block;
import ko.justko.caocaosaver.block.Square4Block;
import ko.justko.caocaosaver.block.Vertical2Block;
import ko.justko.caocaosaver.verify.Verify;

import static ko.justko.caocaosaver.block.BlockOperation.*;

public class KonstructActivity extends Activity {

    private boolean caocaoHere = false;
    private int caocaoId = 0;
    private int heroNum;
    State state;
    private final static String[] heros = new String[]{
            "lvbu", "ganning", "guanyu", "huangzhong","liubei", "machao","zhangfei","zhaoyun",	//8
            "zhugeliang","hetaihou","zhangliao","zoushi","zhangchunhua", "zhangxingcai","diaochan",	//7
            "sunquan", "sunshangxiang","zhenji", "zhouyu","sunce","daqiao","xiaoqiao"		//7
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konstruct);

        state = new State();
        //验证等级
        int level= Verify.verify(this);
        if(level==0)heroNum=8;
        else if(level==1)heroNum=15;
        else if(level==2)heroNum=22;
        else if(level==3)heroNum=heros.length;

        final ImageView square4 = findViewById(R.id.square4);
        final ImageView horizontal2 = findViewById(R.id.horizontal2);
        final ImageView vertical2 = findViewById(R.id.vertical2);
        final ImageView single1 = findViewById(R.id.single1);
        final GridLayout mainPanel = findViewById(R.id.mainPanel);
        square4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getActionMasked() == MotionEvent.ACTION_UP) {
                    int[] cr = atColumnRow(v.getX()+event.getX(), v.getY()+event.getY(),mainPanel);
                    if (cr != null) {
                        if (caocaoHere) {
                            AlertDialog.Builder cao = new AlertDialog.Builder(KonstructActivity.this);
                            cao.setTitle("警告");
                            cao.setMessage("曹操已经出现!");
                            cao.setPositiveButton("确定", null);
                            cao.show();
                            return true;
                        } else {
                            ImageView imageView = new ImageView(KonstructActivity.this);
                            imageView.setImageResource(R.drawable.hv_caocao);
                            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(GridLayout.spec(cr[1], 2), GridLayout.spec(cr[0], 2));
                            layoutParams.height = (int) (80 * 2 * mainPanel.getResources().getDisplayMetrics().density);
                            layoutParams.width = (int) (80 * 2 * mainPanel.getResources().getDisplayMetrics().density);
                            int id = View.generateViewId();
                            Square4Block square4Block = new Square4Block(Integer.toString(id), cr[0], cr[1]);
                            if (state.add(square4Block)) {
                                imageView.setId(id);
                                imageView.setOnTouchListener(new MoveListener());
                                caocaoId = id;
                                mainPanel.addView(imageView, layoutParams);
                                caocaoHere = true;
                            }
                        }
                    }
                }
                return true;
            }
        });
        horizontal2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getActionMasked() == MotionEvent.ACTION_UP) {
                    int[] cr = atColumnRow(v.getX()+event.getX(), v.getY()+event.getY(),mainPanel);
                    if (cr != null) {
                        Random random = new Random();
                        ImageView imageView = new ImageView(KonstructActivity.this);
                        String selectedHero = heros[random.nextInt(heroNum)];
                        imageView.setImageResource(getResources().getIdentifier(
                                "h_" + selectedHero, "drawable",
                                getApplicationInfo().packageName));
                        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(GridLayout.spec(cr[1], 1), GridLayout.spec(cr[0], 2));
                        layoutParams.height = (int) (80 * 1 * mainPanel.getResources().getDisplayMetrics().density);
                        layoutParams.width = (int) (80 * 2 * mainPanel.getResources().getDisplayMetrics().density);
                        int id = View.generateViewId();
                        Horizontal2Block horizontal2Block = new Horizontal2Block(Integer.toString(id), cr[0], cr[1]);
                        if (state.add(horizontal2Block)) {
                            imageView.setOnTouchListener(new MoveListener());
                            imageView.setId(id);
                            mainPanel.addView(imageView, layoutParams);
                        }
                    }
                }
                return true;
            }
        });
        vertical2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getActionMasked() == MotionEvent.ACTION_UP) {
                    int[] cr = atColumnRow(v.getX()+event.getX(), v.getY()+event.getY(),mainPanel);
                    if (cr != null) {
                        Random random = new Random();
                        ImageView imageView = new ImageView(KonstructActivity.this);
                        String selectedHero = heros[random.nextInt(heroNum)];
                        imageView.setImageResource(getResources().getIdentifier(
                                "v_" + selectedHero, "drawable",
                                getApplicationInfo().packageName));
                        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(GridLayout.spec(cr[1], 2), GridLayout.spec(cr[0], 1));
                        layoutParams.height = (int) (80 * 2 * mainPanel.getResources().getDisplayMetrics().density);
                        layoutParams.width = (int) (80 * 1 * mainPanel.getResources().getDisplayMetrics().density);
                        int id = View.generateViewId();
                        Vertical2Block vertical2Block = new Vertical2Block(Integer.toString(id), cr[0], cr[1]);
                        if (state.add(vertical2Block)) {
                            imageView.setOnTouchListener(new MoveListener());
                            imageView.setId(id);
                            mainPanel.addView(imageView, layoutParams);
                        }
                    }
                }
                return true;
            }
        });
        single1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getActionMasked() == MotionEvent.ACTION_UP) {
                    int[] cr = atColumnRow(v.getX()+event.getX(), v.getY()+event.getY(),mainPanel);
                    if (cr != null) {
                        ImageView imageView = new ImageView(KonstructActivity.this);
                        imageView.setImageResource(R.drawable.soldier);
                        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(GridLayout.spec(cr[1], 1), GridLayout.spec(cr[0], 1));
                        layoutParams.height = (int) (80 * 1 * mainPanel.getResources().getDisplayMetrics().density);
                        layoutParams.width = (int) (80 * 1 * mainPanel.getResources().getDisplayMetrics().density);
                        int id = View.generateViewId();
                        Single1Block single1Block = new Single1Block(Integer.toString(id), cr[0], cr[1]);
                        if (state.add(single1Block)) {
                            imageView.setOnTouchListener(new MoveListener());
                            imageView.setId(id);
                            mainPanel.addView(imageView, layoutParams);
                        }
                    }
                }
                return true;
            }
        });

        final Button ok = findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!caocaoHere) {
                    AlertDialog.Builder cao = new AlertDialog.Builder(KonstructActivity.this);
                    cao.setTitle("警告");
                    cao.setMessage("曹操好像已经走了!");
                    cao.setPositiveButton("确定", null);
                    cao.show();
                } else {
                    float grid = 80 * findViewById(R.id.mainPanel).getResources().getDisplayMetrics().density;
                    ImageView caocao=findViewById(caocaoId);
                    int[] caoPos = atInColumnRow(caocao.getX()+grid / 2, caocao.getY()+grid / 2,mainPanel);
                    Log.d("ko","返回坐标@("+caoPos[0]+","+caoPos[1]+")");
                    if (caoPos != null && caoPos[0] == 1 && caoPos[1] == 3) {
                        AlertDialog.Builder alert=new AlertDialog.Builder(KonstructActivity.this);
                        alert.setTitle("警告");
                        alert.setMessage("这是已经完成的，最好不要给自己这么简单的问题。");
                        alert.setPositiveButton("好的",null);
                        alert.show();
                    }else {
                        /*
                        square4.setOnTouchListener(null);
                        horizontal2.setOnTouchListener(null);
                        vertical2.setOnTouchListener(null);
                        single1.setOnTouchListener(null);
                        */
                        Toast.makeText(KonstructActivity.this, "现在开始吧", Toast.LENGTH_SHORT).show();
                        state.reSolve();
                        ok.setEnabled(false);
                        square4.setVisibility(View.INVISIBLE);
                        horizontal2.setVisibility(View.INVISIBLE);
                        vertical2.setVisibility(View.INVISIBLE);
                        single1.setVisibility(View.INVISIBLE);
                    }
                }
            }
        });//ok监听结束
        //back事件
        Button back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder cao = new AlertDialog.Builder(KonstructActivity.this);
                cao.setTitle("警告");
                cao.setMessage(state.toString());
                cao.setPositiveButton("确定", null);
                cao.show();
            }
        });
    }

    private class MoveListener implements View.OnTouchListener {
        private final float grid = 80 * findViewById(R.id.mainPanel).getResources().getDisplayMetrics().density;
        private long pressTime;
        private float lastX;
        private float lastY;

        @Override
        public boolean onTouch(final View v, MotionEvent event) {
            if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                pressTime = System.currentTimeMillis();
                lastX = event.getX();
                lastY = event.getY();
            }
            if (event.getActionMasked() == MotionEvent.ACTION_UP) {
                if (System.currentTimeMillis() - pressTime < 200)
                    return true;
                float translationX = v.getTranslationX();
                float translationY = v.getTranslationY();
                final ViewPropertyAnimator viewPropertyAnimator = v.animate();
                final Button ok=findViewById(R.id.ok);
                if (v.getId() == caocaoId&&!ok.isEnabled()) {
                    Log.d("ko","检测到曹操移动");
                    viewPropertyAnimator.setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            Log.d("ko","曹操移动后做的事情");
                            super.onAnimationEnd(animation);
                            final float offsetX=v.getX();
                            final float offsetY=v.getY();
                            GridLayout mainPanel=findViewById(R.id.mainPanel);
                            float grid=80 * mainPanel.getResources().getDisplayMetrics().density;
                            int[] caoPos = atColumnRow(offsetX+grid/2, offsetY+grid/2,mainPanel);
                            if(caoPos[0]==1&&caoPos[1]==3){
                                AlertDialog.Builder alert=new AlertDialog.Builder(KonstructActivity.this);
                                alert.setTitle("恭喜");
                                alert.setMessage("恭喜完成！所用步数："+state.getNames().size()+"。");
                                state.reSolve();
                                alert.setPositiveButton("好的",null);
                                alert.show();
                                ok.setEnabled(true);
                                final ImageView square4 = findViewById(R.id.square4);
                                final ImageView horizontal2 = findViewById(R.id.horizontal2);
                                final ImageView vertical2 = findViewById(R.id.vertical2);
                                final ImageView single1 = findViewById(R.id.single1);
                                square4.setVisibility(View.VISIBLE);
                                horizontal2.setVisibility(View.VISIBLE);
                                vertical2.setVisibility(View.VISIBLE);
                                single1.setVisibility(View.VISIBLE);
                                viewPropertyAnimator.setListener(new AnimatorListenerAdapter() {
                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        super.onAnimationEnd(animation);
                                    }
                                });
                            }
                        }
                    });
                }
                viewPropertyAnimator.setDuration(500);
                String name = String.valueOf(v.getId());
                float moveX = event.getX() - lastX;
                float moveY = event.getY() - lastY;
                if (Math.abs(moveX) > Math.abs(moveY)) {
                    if (moveX > 0 && state.move(name, RIGHT))
                        viewPropertyAnimator.translationX(translationX + grid);
                    else if (moveX < 0 && state.move(name, LEFT))
                        viewPropertyAnimator.translationX(translationX - grid);
                } else {
                    if (moveY > 0 && state.move(name, DOWN))
                        viewPropertyAnimator.translationY(translationY + grid);
                    else if (moveY < 0 && state.move(name, UP))
                        viewPropertyAnimator.translationY(translationY - grid);
                }
            }
            return true;
        }
    }

    @Nullable
    private int[] atColumnRow(float x, float y,GridLayout gridLayout) {
        float offsetX =x - gridLayout.getX();
        float offsetY =y - gridLayout.getY();
        int column = gridLayout.getColumnCount();
        int row = gridLayout.getRowCount();
        float width = gridLayout.getWidth();
        float height = gridLayout.getHeight();
        if (offsetX < 0 || offsetY < 0) return null;
        int c = (int) (offsetX * column / width);
        int r = (int) (offsetY * row / height);
        if (c >= column || r >= row) return null;
        Log.d("ko","传入:("+x+","+y+"),Grid坐标:("+
                gridLayout.getX()+","+gridLayout.getY()+"),Grid宽高("
                +width+","+height+")");
        return new int[]{c, r};
    }
    @Nullable
    private int[] atInColumnRow(float offsetX, float offsetY,GridLayout gridLayout) {
        int column = gridLayout.getColumnCount();
        int row = gridLayout.getRowCount();
        float width = gridLayout.getWidth();
        float height = gridLayout.getHeight();
        if (offsetX < 0 || offsetY < 0) return null;
        int c = (int) (offsetX * column / width);
        int r = (int) (offsetY * row / height);
        if (c >= column || r >= row) return null;
        Log.d("ko","传入:("+offsetX+","+offsetY+"),Grid坐标:("+
                gridLayout.getX()+","+gridLayout.getY()+"),Grid宽高("
                +width+","+height+")");
        return new int[]{c, r};
    }
}

