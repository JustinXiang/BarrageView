package read.it.myapplication;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;

import read.it.myapplication.widgets.Barrage;
import read.it.myapplication.widgets.BarrageView;

public class MainActivity extends AppCompatActivity implements BarrageView.PostPirset {

    private BarrageView barrageview;
    private Runnable runnable;
    private boolean start=false;
    ArrayList<Barrage> date=new ArrayList<>(); //弹幕数据
    private Handler handler=new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findId();
        init();
    }

    private void findId() {
        barrageview = findViewById(R.id.barrageview);
    }
    private void init(){
        barrageview.setContext(this);
        barrageview.setPirset(this);
        start=true;
        startDanMu();
    }
    //弹幕点击接口。
    @Override
    public void postPirset(int post_id, int pirset) {

    }


    private void startDanMu(){
        // danMuControl.addPicturDanMu(bean.account_avatar, bean.content.get(0).content, bean.is_praise);
        runnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    date.add(new Barrage("http://img.mp.itc.cn/upload/20170306/aa50c412890a43fc96f364b7fe66e6e3_th.jpeg", "哈哈哈哈",0,i));
                }
                if(barrageview!=null) {
                    barrageview.setDateList(date);
                    if(start) {
                        handler.postDelayed(this, 10000);
                    }
                }
            }
        };
        runnable.run();
    }
}
