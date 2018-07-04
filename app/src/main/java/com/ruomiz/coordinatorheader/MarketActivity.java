package com.ruomiz.coordinatorheader;

import android.graphics.Color;
import android.icu.util.Measure;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;

import java.util.Arrays;

/**
 * Created by Ruomiz on 2018/6/12.
 * Ruomiz try best
 * 仿华为应用市场
 */

public class MarketActivity extends AppCompatActivity {

    private final String TAG = MarketActivity.class.getSimpleName();
    private ConvenientBanner mBanner;

    private final String[] images = {
            "http://bpic.588ku.com/back_pic/04/44/12/025853ace56fce9.jpg!r850/fw/800",
            "http://bpic.588ku.com/back_pic/00/05/15/245625f4ffbaf09.jpg!r850/fw/800",
            "http://bpic.588ku.com/back_pic/04/33/57/25584561477469d.jpg!r850/fw/800",
            "http://bpic.588ku.com/back_pic/00/01/53/53560403f8148a8.jpg!r850/fw/800"
    };

    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private TextView mTab;
    private SearchBar mSearchBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);
        mBanner = findViewById(R.id.convenientBanner);
        mToolbar = findViewById(R.id.toolbar);
        mAppBarLayout = findViewById(R.id.appbar);
        mTab = findViewById(R.id.tab);
        mSearchBar = findViewById(R.id.searchBar);
        init();
    }

    private void init() {
        initBanner();
        initHeader();
    }

    private void initBanner() {
        mBanner.setPages(new CBViewHolderCreator() {
            @Override
            public Holder createHolder(View itemView) {
                return new NetWorkImageHoler(itemView);
            }

            @Override
            public int getLayoutId() {
                return R.layout.item_bannerimage;
            }
        }, Arrays.asList(images));
    }


    private void initHeader() {
        mSearchBar.setSearchIcon(R.drawable.search)
                .setDuration(300)
                .setSearchTextHint("请输入搜索关键字");

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    mTab.setTextColor(Color.argb(255, 255, 255, 255));
                    mTab.setBackgroundColor(Color.argb(0, 255, 255, 255));
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    mTab.setTextColor(Color.argb(255, 0, 0, 0));
                    mTab.setBackgroundColor(Color.argb(255, 255, 255, 255));
                } else {
                    float percent = (Math.abs(verticalOffset) * 100 / appBarLayout.getTotalScrollRange()) * 0.01f;
                    int alpha = (int) Math.floor(percent * 255);
                    if (Math.abs(verticalOffset) * 4 > appBarLayout.getTotalScrollRange()) {
                        mTab.setTextColor(Color.argb(alpha, 0, 0, 0));
                    } else {
                        mTab.setTextColor(Color.argb(255 - alpha * 4, 255, 255, 255));
                    }
                    mTab.setBackgroundColor(Color.argb(alpha, 255, 255, 255));
                    Log.d(TAG, "verticalOffset：" + verticalOffset);

                    if (Math.abs(verticalOffset) > appBarLayout.getTotalScrollRange() * 0.6) {
                        if (mSearchBar.getCurrentState() == SearchBar.State.OPEN) {
                            mSearchBar.closeAnimation();
                            Log.d(TAG, "是时候隐藏了");
                        }
                    } else {
                        if (mSearchBar.getCurrentState() == SearchBar.State.CLOSED) {
                            Log.d(TAG, "是时候显示了");
                            mSearchBar.startAnimation();
                        }
                    }

                }
            }
        });
    }

    private static class NetWorkImageHoler extends Holder<String> {

        private ImageView mIvBanner;

        private NetWorkImageHoler(View itemView) {
            super(itemView);
        }

        @Override
        protected void initView(View itemView) {
            mIvBanner = itemView.findViewById(R.id.iv_banner);
        }

        @Override
        public void updateUI(String data) {
            Glide.with(mIvBanner.getContext()).load(data).centerCrop().into(mIvBanner);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBanner.startTurning(5000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mBanner.stopTurning();
    }

    private void translucentStatusbar() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION  //该参数指布局能延伸到navigationbar，我们场景中不应加这个参数
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT); //设置navigationbar颜色为透明
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

}
