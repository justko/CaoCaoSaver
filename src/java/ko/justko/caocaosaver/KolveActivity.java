package ko.justko.caocaosaver;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import ko.justko.caocaosaver.block.BlockOperation;
import ko.justko.caocaosaver.block.Horizontal2Block;
import ko.justko.caocaosaver.block.Single1Block;
import ko.justko.caocaosaver.block.Square4Block;
import ko.justko.caocaosaver.block.Vertical2Block;
import ko.justko.caocaosaver.verify.Verify;

public class KolveActivity extends Activity {
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case 0:
                    final AlertDialog.Builder result = new AlertDialog.Builder(KolveActivity.this);
                    Log.d("ko", "调用handle" + saver.getSolved());
                    if (saver.getSolved()) {
                        names = saver.getNames();
                        operations = saver.getOperations();
                        result.setTitle("恭喜");
                        result.setMessage("它在" + names.size()+ "步可解");
                        result.setPositiveButton("查看步骤", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(KolveActivity.this, "正在演示", Toast.LENGTH_SHORT).show();
                                showMePath();
                            }
                        });
                        result.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(KolveActivity.this, "您取消了查看步骤", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        result.setTitle("遗憾");
                        result.setPositiveButton("好的", null);
                        result.setMessage("无解或您已放弃求解");
                    }
                    result.show();
                    break;
                case 1:
                    progressDialog.setMessage("已知:" + message.getData().getString("allStates") +
                            "\n已检测:" + message.getData().getString("tested") +
                            "\n剩余:" + message.getData().getString("remainStates"));
                    break;

            }

        }
    };
    private State state;
    private Saver saver;
    private boolean caocaoHere = false;
    private ProgressDialog progressDialog;
    private ArrayList<String> names;
    private ArrayList<BlockOperation> operations;
    private int heroNum=1;
    private final static String[] heros = new String[]{
            "lvbu", "ganning", "guanyu", "huangzhong","liubei", "machao","zhangfei","zhaoyun",	//8
            "zhugeliang","hetaihou","zhangliao","zoushi","zhangchunhua", "zhangxingcai","diaochan",	//7
            "sunquan", "sunshangxiang","zhenji", "zhouyu","sunce","daqiao","xiaoqiao",		//7
            "guohuanghou"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //
        state = new State();
        //验证等级
        int level= Verify.verify(this);
        if(level==0)heroNum=8;
        else if(level==1)heroNum=15;
        else if(level==2)heroNum=22;
        else if(level==3)heroNum=heros.length;

        progressDialog = new ProgressDialog(KolveActivity.this);
        //添加Block事件
        final ImageView square4 = findViewById(R.id.square4);
        final ImageView horizontal2 = findViewById(R.id.horizontal2);
        final ImageView vertical2 = findViewById(R.id.vertical2);
        final ImageView single1 = findViewById(R.id.single1);
        final GridLayout mainPanel = findViewById(R.id.mainPanel);
        square4.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getActionMasked() == MotionEvent.ACTION_UP) {
                    int[] cr = atColumnRow(view, motionEvent.getX(), motionEvent.getY());
                    if (cr != null) {
                        if (caocaoHere) {
                            AlertDialog.Builder cao = new AlertDialog.Builder(KolveActivity.this);
                            cao.setTitle("警告");
                            cao.setMessage("曹操已经出现!");
                            cao.setPositiveButton("确定", null);
                            cao.show();
                            return true;
                        } else {
                            ImageView imageView = new ImageView(KolveActivity.this);
                            imageView.setImageResource(R.drawable.hv_caocao);
                            GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(GridLayout.spec(cr[1], 2), GridLayout.spec(cr[0], 2));
                            layoutParams.height = (int) (80 * 2 * mainPanel.getResources().getDisplayMetrics().density);
                            layoutParams.width = (int) (80 * 2 * mainPanel.getResources().getDisplayMetrics().density);
                            int id = View.generateViewId();
                            Square4Block square4Block = new Square4Block(Integer.toString(id), cr[0], cr[1]);
                            if (state.add(square4Block)) {
                                imageView.setId(id);
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
                    int[] cr = atColumnRow(v, event.getX(), event.getY());
                    if (cr != null) {
                        Random random = new Random();
                        ImageView imageView = new ImageView(KolveActivity.this);
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
                    int[] cr = atColumnRow(v, event.getX(), event.getY());
                    if (cr != null) {
                        Random random = new Random();
                        ImageView imageView = new ImageView(KolveActivity.this);
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
                    int[] cr = atColumnRow(v, event.getX(), event.getY());
                    if (cr != null) {
                        ImageView imageView = new ImageView(KolveActivity.this);
                        imageView.setImageResource(R.drawable.soldier);
                        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams(GridLayout.spec(cr[1], 1), GridLayout.spec(cr[0], 1));
                        layoutParams.height = (int) (80 * 1 * mainPanel.getResources().getDisplayMetrics().density);
                        layoutParams.width = (int) (80 * 1 * mainPanel.getResources().getDisplayMetrics().density);
                        int id = View.generateViewId();
                        Single1Block single1Block = new Single1Block(Integer.toString(id), cr[0], cr[1]);
                        if (state.add(single1Block)) {
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
                    AlertDialog.Builder cao = new AlertDialog.Builder(KolveActivity.this);
                    cao.setTitle("警告");
                    cao.setMessage("曹操好像已经走了!");
                    cao.setPositiveButton("确定", null);
                    cao.show();
                    return;
                }
                state.reSolve();
                saver = new Saver(state);
                //进度框
                progressDialog.setTitle("请稍候...");
                progressDialog.setMessage("需要检测上万个状态，可能需要5~10分钟！");
                progressDialog.setCancelable(true);
                progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        saver.setTerminate(true);
                    }
                });
                progressDialog.show();
                //计算线程
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        saver.solve(handler);
                        progressDialog.dismiss();
                        handler.sendEmptyMessage(0);
                    }
                }).start();
            }
        });//ok监听结束
        //back事件
        Button back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder cao = new AlertDialog.Builder(KolveActivity.this);
                cao.setTitle("警告");
                cao.setMessage(state.toString());
                cao.setPositiveButton("确定", null);
                cao.show();
            }
        });
    }

    //计算行列
    @Nullable
    private int[] atColumnRow(View view, float x, float y) {
        GridLayout gridLayout = findViewById(R.id.mainPanel);
        float offsetX = view.getX() + x - gridLayout.getX();
        float offsetY = view.getY() + y - gridLayout.getY();
        int column = gridLayout.getColumnCount();
        int row = gridLayout.getRowCount();
        float width = gridLayout.getWidth();
        float height = gridLayout.getHeight();
        if (offsetX < 0 || offsetY < 0) return null;
        int c = (int) (offsetX * column / width);
        int r = (int) (offsetY * row / height);
        if (c >= column || r >= row) return null;
        return new int[]{c, r};
    }

    //演示
    private void showMePath() {
        if (names == null || names.size() == 0) return;
        showMeOne(0, names.size());
    }

    private void showMeOne(final int i, final int all) {
        final float grid = 80 * 1 * findViewById(R.id.mainPanel).getResources().getDisplayMetrics().density;
        ImageView imageView = findViewById(Integer.parseInt(names.get(i)));
        float translationX = imageView.getTranslationX();
        float translationY = imageView.getTranslationY();
        ViewPropertyAnimator viewPropertyAnimator = imageView.animate();
        switch (operations.get(i)) {
            case UP:
                state.move(names.get(i), BlockOperation.UP);
                viewPropertyAnimator.translationY(translationY - grid);
                break;
            case DOWN:
                state.move(names.get(i), BlockOperation.DOWN);
                viewPropertyAnimator.translationY(translationY + grid);
                break;
            case LEFT:
                state.move(names.get(i), BlockOperation.LEFT);
                viewPropertyAnimator.translationX(translationX - grid);
                break;
            default:
                state.move(names.get(i), BlockOperation.RIGHT);
                viewPropertyAnimator.translationX(translationX + grid);
                break;
        }
        viewPropertyAnimator.setDuration(1000);
        viewPropertyAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        viewPropertyAnimator.setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (i < all - 1) showMeOne(i + 1, all);
            }
        });
    }
}
