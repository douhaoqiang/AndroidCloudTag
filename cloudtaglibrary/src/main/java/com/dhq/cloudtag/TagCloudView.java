package com.dhq.cloudtag;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by douhaoqiang 2018.05.03
 */
public class TagCloudView<T> extends ViewGroup {

    private static final String TAG = TagCloudView.class.getSimpleName();
    private List<T> tags;

    private LayoutInflater mInflater;
    private TagCloudView.TagListener mTagListener;

    private int sizeWidth;
    private int sizeHeight;

    private int mTagBorderHor; //水平间距
    private int mTagBorderVer; //垂直间距

    private boolean mIsTagCheck = true; //是否可选择 （默认不可选择）
    private boolean mIsSingleCheck = true; //是否单选（默认单选）


    private int totalWidth = 0;
    private int totalHeight = mTagBorderVer;

    private int leftLocation;
    private int rightLocation;
    private int topLocation;
    private int bottomLocation;

    private SparseArray<T> checkedItems = new SparseArray<>();
    private SparseArray<View> checkedViews = new SparseArray<>();

    public TagCloudView(Context context) {
        this(context, null);
    }

    public TagCloudView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagCloudView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mInflater = LayoutInflater.from(context);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.TagCloudView,
                defStyleAttr,
                defStyleAttr
        );

        mTagBorderHor = a.getDimensionPixelSize(R.styleable.TagCloudView_tagHorizontalMargen, getDefaultValue(R.dimen.tag_cloud_hor_margen));
        mTagBorderVer = a.getDimensionPixelSize(R.styleable.TagCloudView_tagVerticalMargen, getDefaultValue(R.dimen.tag_cloud_ver_margen));

        mIsTagCheck = a.getBoolean(R.styleable.TagCloudView_isCanCheck, true);
        mIsSingleCheck = a.getBoolean(R.styleable.TagCloudView_isSingleCheck, true);


        a.recycle();

    }

    private int getDefaultValue(int res) {
        return getContext().getResources().getDimensionPixelOffset(res);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return (!mIsTagCheck) || super.onInterceptTouchEvent(ev);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int childWidth;
        int childHeight;
        for (int i = 0; i < getChildCount(); i++) {
            final T data = tags.get(i);
            View child = getChildAt(i);
            childWidth = child.getMeasuredWidth();
            childHeight = child.getMeasuredHeight();

            if (i == 0) {
                totalWidth = leftLocation + childWidth;
                totalHeight = childHeight + topLocation;
            } else {
                totalWidth += childWidth + mTagBorderHor;
            }

            // + marginLeft 保证最右侧与 ViewGroup 右边距有边界
            if (totalWidth + rightLocation > sizeWidth) {
                totalWidth = leftLocation + childWidth;
                totalHeight += childHeight + mTagBorderVer;
            }

            child.layout(
                    totalWidth - childWidth,
                    totalHeight - childHeight,
                    totalWidth,
                    totalHeight);

        }

    }

    /**
     * 计算 ChildView 宽高
     *
     * @param widthMeasureSpec  宽
     * @param heightMeasureSpec 高
     */
    @SuppressLint("DrawAllocation")
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /**
         * 计算 ViewGroup 上级容器为其推荐的宽高
         */
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
//        sizeHeight = MeasureSpec.getSize(heightMeasureSpec);


        int childWidth;
        int childHeight;
        //开始位置
        leftLocation = getPaddingLeft() + ((MarginLayoutParams) getLayoutParams()).leftMargin;
        rightLocation = getPaddingRight() + ((MarginLayoutParams) getLayoutParams()).rightMargin;
        topLocation = getPaddingTop() + ((MarginLayoutParams) getLayoutParams()).topMargin;
        bottomLocation = getPaddingBottom() + ((MarginLayoutParams) getLayoutParams()).bottomMargin;


        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            this.measureChild(child, widthMeasureSpec, heightMeasureSpec);
            childWidth = child.getMeasuredWidth();
            childHeight = child.getMeasuredHeight();
            if (i == 0) {
                totalWidth = leftLocation + childWidth;
                totalHeight = childHeight + topLocation;
            } else {
                totalWidth += childWidth + mTagBorderHor;
            }

            // + marginLeft 保证最右侧与 ViewGroup 右边距有边界
            if (totalWidth + rightLocation > sizeWidth) {
                totalWidth = leftLocation + childWidth;
                totalHeight += childHeight + mTagBorderVer;

            }
        }
        totalHeight = totalHeight + bottomLocation;

        /**
         * 高度根据设置改变
         * 如果为 MATCH_PARENT 则充满父窗体，否则根据内容自定义高度
         */
        setMeasuredDimension(sizeWidth, totalHeight);

    }


    /**
     * 这是标签列表
     *
     * @param tagList 标签列表内容
     */
    public void setTags(List<T> tagList) {
        this.tags = tagList;
        this.checkedItems.clear();
        this.checkedViews.clear();
        this.removeAllViews();

        if (mTagListener == null) {
            return;
        }

        if (tags != null && tags.size() > 0) {
            for (int i = 0; i < tags.size(); i++) {

                final T data = tags.get(i);
                final View child = mInflater.inflate(mTagListener.getlayResId(), null);

                if (mIsTagCheck) {
                    final int finalI = i;
                    child.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            T t = checkedItems.get(finalI);
                            if (t == null) {
                                if (mIsSingleCheck) {
                                    checkedItems.clear();
                                    checkedViews.clear();
                                }
                                checkedItems.put(finalI, data);
                                checkedViews.put(finalI, child);
                            } else {
                                checkedItems.remove(finalI);
                                checkedViews.remove(finalI);
                            }
                            //选择完刷新界面
                            refreshView();
                        }
                    });
                    T selectItem = checkedItems.get(i);
                    if (selectItem == null) {
                        mTagListener.convertView(data, child, false);
                    } else {
                        mTagListener.convertView(data, child, true);
                    }
                } else {
                    //当不可选择时 返回同一个状态
                    mTagListener.convertView(data, child, false);
                }
                addView(child);
            }
        }
        postInvalidate();
    }

    /**
     * 刷新界面
     */
    private void refreshView() {

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            T data = tags.get(i);
            T selectItem = checkedItems.get(i);
            if (selectItem == null) {
                mTagListener.convertView(data, child, false);
            } else {
                mTagListener.convertView(data, child, true);
            }
        }


    }


    public void setOnTagListener(TagCloudView.TagListener onTagListener) {
        this.mTagListener = onTagListener;
    }


    /**
     * 获取选择项
     *
     * @return 获取选择列表
     */
    public List<T> getCheckedTags() {
        List<T> tags = new ArrayList<>();
        for (int i = 0; i < checkedItems.size(); i++) {
            tags.add(checkedItems.valueAt(i));
        }
        return tags;
    }

    public interface TagListener<T> {

        /**
         * 获取item资源id
         *
         * @return  获取item布局
         */
        int getlayResId();

        /**
         * 绘制界面
         *
         * @param data      单个数据
         * @param tagView   单个布局view
         * @param isChecked 是否被选中
         */
        void convertView(T data, View tagView, boolean isChecked);


    }

}
