package hanyang.ac.kr.belieme.dataType;

import android.view.View;

public class Setting {
    public static final int VIEW_TYPE_HEADER = 0;
    public static final int VIEW_TYPE_ITEM = 1;

    private String title;
    private View.OnClickListener onClickListener;
    private int viewType;

    public Setting() {
    }

    public Setting(String title, View.OnClickListener onClickListener) {
        this.title = title;
        this.onClickListener = onClickListener;
        viewType = VIEW_TYPE_ITEM;
    }

    public Setting(String title, View.OnClickListener onClickListener, int viewType) {
        this.title = title;
        this.onClickListener = onClickListener;
        this.viewType = viewType;
    }

    public String getTitle() {
        return title;
    }

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
