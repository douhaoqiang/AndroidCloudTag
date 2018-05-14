package com.dhq.cloudtag;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private TagCloudView<String> tagView;
    private TagCloudView<String> tagMulView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tagView = findViewById(R.id.tag_view);
        tagMulView = findViewById(R.id.tag_mul_view);

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
        tagMulView.setOnTagListener(tagListener);

        tagView.setTags(getTags());
        tagMulView.setTags(getTags());

    }


    private List<String> getTags(){

        List<String> tagLists=new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            tagLists.add("标签"+i);
        }

        return tagLists;
    }

}
