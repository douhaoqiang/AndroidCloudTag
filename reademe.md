# 云便签使用
## 第一步 在布局文件中引用

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">


    <com.dhq.cloudtag.TagCloudView
        android:id="@+id/tag_view"
        android:layout_width="match_parent"
        android:layout_height="wrap_content" />


    <com.dhq.cloudtag.TagCloudView
        android:id="@+id/tag_mul_view"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:layout_marginTop="20dp"
        app:isSingleCheck="false" />


</LinearLayout>
```
## 第二步 在代码中设置item布局和显示内容 这样可以自己随意修改 item布局和显示样式

```java
public class MainActivity extends AppCompatActivity {

    private TagCloudView<String> tagView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tagView = findViewById(R.id.tag_view);

        //设置item布局  和 选择状态变化
        TagCloudView.TagListener<String> tagListener = new TagCloudView.TagListener<String>() {
            @Override
            public int getlayResId() {
                return R.layout.item_tag_view;
            }

            @Override
            public void convertView(String data, View tagView, boolean isChecked) {
                TextView tag = tagView.findViewById(R.id.tag_content);
                tag.setText(data);
                if (isChecked) {
                    tag.setBackgroundResource(R.drawable.round_red_stork_bg);
                } else {
                    tag.setBackgroundResource(R.drawable.round_black_stork_bg);
                }
            }
        };

        tagView.setOnTagListener(tagListener);

        tagView.setTags(getTags());

    }


    private List<String> getTags(){

        List<String> tagLists=new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            tagLists.add("标签"+i);
        }

        return tagLists;
    }

}
```

** 属性说明

```xml
<resources>

    <declare-styleable name="TagCloudView">

        <!-- 垂直间距 -->
        <attr name="tagVerticalMargen" format="dimension" />
        <!-- 水平间距 -->
        <attr name="tagHorizontalMargen" format="dimension" />
        <!-- 是否可点击 -->
        <attr name="isCanCheck" format="boolean" />
        <!-- 是否单选 true 单选 false 多选  默认单选 -->
        <attr name="isSingleCheck" format="boolean" />

    </declare-styleable>

</resources>
```

    | 名称 | 作用 |
    | :------: | :------: |
    |   设计尺寸     |    为UI给的效果图尺寸    |
    |   真实尺寸     |    为手机宽度尺寸（dp）    |
    |   输出路径     |    适配文件的输出位置    |