package read.it.myapplication.widgets;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import java.util.ArrayList;
import java.util.List;

import read.it.myapplication.R;

/**
 * @des:图文弹幕
 * @author: flx
 * @version: 1
 * @date: 2018/1/23 15:52
 * @see {@link }
 */
public class BarrageView extends FrameLayout {
    private ArrayList<Barrage> startdate = new ArrayList<>(); //开始时的数据
    private int nowIndex = 0; //date的下标
    //private Bitmap nowBitmap; //当前图片
    int width;    //控件宽
    int height;  //控件高
    float scale;    //像素密度
    FrameLayout frameLayout;
    LayoutParams tvParams;
    Context context;

    private ArrayList<Barrage> endDate = new ArrayList<>(); //结束后的数据

    static boolean IS_START = false;    //判断是否开始

    long alltime; //视频总时长
    long starttime; //开始时间

    //    LinearLayout layout;

    public void setContext(Context context) {
        this.context = context;
    }

    private boolean ishead = false;

    /**
     * 弹幕点赞接口
     */
    public interface PostPirset{
        void postPirset(int post_id,int pirset);
    }

    private PostPirset pirset;

    /**
     * 是否是点赞过得弹幕
     * @param pirset
     */
    public void setPirset(PostPirset pirset){
        this.pirset=pirset;
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            final Barrage barrage = (Barrage) msg.getData().getSerializable("barrage");
            final LinearLayout layout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.barrageview_item, null);
            layout.setLayoutParams(tvParams);
            //随机获得Y值
            layout.setY(getRamdomY());
            layout.setX(width + layout.getWidth());

            final ImageView iv = (ImageView) layout.findViewById(R.id.yidianzan);

            //设置文字
            TextView textView = (TextView) layout.findViewById(R.id.barrageview_item_tv);
            textView.setText(barrage.getContent());
            final View bg = layout.findViewById(R.id.lll);
            if (barrage.getIs_pirset() == 1) {
                ishead = true;
                bg.setBackgroundResource(R.drawable.corners_danmu1);
                iv.setVisibility(VISIBLE);
            } else {
                ishead = false;
                bg.setBackgroundResource(R.drawable.corners_danmu);
                iv.setVisibility(INVISIBLE);
            }

            //View view =layout.findViewById(R.id.dianzan);

            layout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!ishead) {
                        bg.setBackgroundResource(R.drawable.corners_danmu1);
                        iv.setVisibility(VISIBLE);
                        ishead = !ishead;
                        barrage.setIs_pirset(1);
                        if(pirset!=null){
                            pirset.postPirset(barrage.getComment_id(),barrage.getIs_pirset());
                        }
                    } else {
                        bg.setBackgroundResource(R.drawable.corners_danmu);
                        iv.setVisibility(INVISIBLE);
                        ishead = !ishead;
                        barrage.setIs_pirset(0);
                        if(pirset!=null){
                            pirset.postPirset(barrage.getComment_id(),barrage.getIs_pirset());
                        }
                    }
                    //ToastUtil.showToast(barrage.getContent());
                }
            });

            //设置图片
            final ImageView ngNormalCircleImageView = (ImageView) layout.findViewById(R.id.barrageview_item_img);
            Glide.with(context).load(barrage.getBarrageUrl()).asBitmap().centerCrop().into(new BitmapImageViewTarget(ngNormalCircleImageView) {
                @Override
                protected void setResource(Bitmap resource) {
                    RoundedBitmapDrawable circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                    circularBitmapDrawable.setCircular(true);
                    ngNormalCircleImageView.setImageDrawable(circularBitmapDrawable);
                }
            });
            /*ngNormalCircleImageView
            if (nowBitmap != null) {
                ngNormalCircleImageView.setImageBitmap(nowBitmap);
            }*/

            frameLayout.addView(layout);

            final ObjectAnimator anim = ObjectAnimator.ofFloat(layout, "translationX", -width);
            anim.setDuration(8000);

            //释放资源
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    anim.cancel();
                    layout.clearAnimation();
                    frameLayout.removeView(layout);
                    endDate.add(barrage);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            anim.start();
        }
    };


    int lastY;//上一次出现的Y值

    /**
     * 获得随机的Y轴的值
     *
     * @return
     */
    private float getRamdomY() {
        int tempY;
        int rY;
        int result = 0;
        // height * 2 / 4 - 25
        //首先随机选择一条道路
        int nowY = (int) (Math.random() * 3);
        switch (nowY) {
            case 0:
                nowY = avoidTheSameY(nowY, lastY);
                //第一条
                tempY = height / 4 - 50;
                rY = (int) (Math.random() * height / 4);
                if (rY >= height / 8) {
                    result = tempY + rY;
                } else {
                    result = tempY - rY + 100;
                }
                lastY = nowY;
                break;
            case 1:
                nowY = avoidTheSameY(nowY, lastY);
                //第二条
                tempY = height / 2 - 50;
                rY = (int) (Math.random() * height / 4);
                if (rY >= height / 8) {
                    result = tempY + rY;
                } else {
                    result = tempY - rY;
                }
                lastY = nowY;
                break;
            case 2:
                nowY = avoidTheSameY(nowY, lastY);
                //第三条
                tempY = height * 3 / 4 - 50;
                rY = (int) (Math.random() * height / 4);
                if (rY >= height / 8) {
                    result = tempY + rY - 100;
                } else {
                    result = tempY - rY;
                }
                lastY = nowY;
                break;
        }
        return result;
    }

    /**
     * 避免Y重合的方法
     *
     * @param lastY
     * @return
     */
    private int avoidTheSameY(int nowY, int lastY) {
        if (nowY == lastY) {
            nowY++;
        }
        if (nowY == 4) {
            nowY = 0;
        }
        return nowY;
    }


    public BarrageView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = getWidth(); //宽度
        height = getHeight();   //高度
        init();
    }

    private void init() {
        setTime(600000000);    //设置初始时长，改完记得删

        starttime = System.currentTimeMillis();

        scale = this.getResources().getDisplayMetrics().density;
        //获得自身实例
        frameLayout = (FrameLayout) findViewById(R.id.barrageview);
        tvParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        if (IS_START) {
            //开始动画线程
            startBarrageView();
            IS_START = false;
        }
    }

    public void startBarrageView() {
        //开启线程发送弹幕
        new Thread() {
            @Override
            public void run() {
                while ((System.currentTimeMillis() - starttime < alltime)
                        && (nowIndex <= startdate.size() - 1)
                        ) {
                    try {
                        //nowBitmap = getBitmapFromUrl(date.get(nowIndex).getBarrageUrl());
                        Message message = new Message();
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("barrage", startdate.get(nowIndex));
                        nowIndex++;
                        message.setData(bundle);
                        handler.sendMessage(message);
                        Thread.sleep((long) (Math.random() * 3000) + 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                return;
            }
        }.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    /**
     * 设置弹幕开始时候的数据
     *
     * @param date1
     */
    public void setDateList(ArrayList<Barrage> date1) {
        startdate = date1;
        IS_START = true;
    }

    /**
     * 获取弹幕结束后的数据
     *
     * @return
     */
    public List<Barrage> getDateList() {
        if (endDate.size() == startdate.size()) {
            return endDate;
        } else {
            int size = startdate.size() - endDate.size();
            for (int i = startdate.size() - size; i < startdate.size(); i++) {
                endDate.add(startdate.get(i));
            }
            return endDate;
        }
    }

    //设置视频总时长
    public void setTime(long time) {
        alltime = time;
    }
}
