package read.it.myapplication.widgets;

import java.io.Serializable;

/**
 * @des:弹幕模型
 * @author: flx
 * @version: 1
 * @date: 2018/1/23 15:54
 * @see {@link }
 */
public class Barrage implements Serializable {
    private String url;//弹幕头像图片
    private String content;//弹幕文字内容
    private int is_pirset;//弹幕是否被点击
    private int comment_id;//弹幕id

    public Barrage(String url, String content, int is_pirset,int comment_id) {
        this.url = url;
        this.content = content;
        this.is_pirset=is_pirset;
        this.comment_id=comment_id;
    }

    public int getComment_id() {
        return comment_id;
    }

    public void setComment_id(int comment_id) {
        this.comment_id = comment_id;
    }

    public int getIs_pirset() {
        return is_pirset;
    }

    public void setIs_pirset(int is_pirset) {
        this.is_pirset = is_pirset;
    }

    public String getBarrageUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
