package com.example.xyzreader.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.xyzreader.R;
import com.example.xyzreader.ui.fragments.ArticleDetailFragment;

public class ArticleDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        if (savedInstanceState == null) {
            ArticleDetailFragment fragment = ArticleDetailFragment.newInstance(getIntent().getLongExtra(ArticleListActivity.EXTRA_ARTICLE_ID, 0));

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.root_frame_article_detail, fragment)
                    .commit();
        }
    }
}
