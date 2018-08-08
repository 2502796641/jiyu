package com.guang.jiyu.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.TextView;

import com.guang.jiyu.R;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by admin on 2018/7/20.
 */

public class TestActivity extends BaseActivity {

    @BindView(R.id.tv_test)
    TextView tvTest;

    private boolean isOpening = true;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.activity_test);
    }

    @OnClick(R.id.tv_test)
    public void onViewClicked() {
        if(isOpening){
            isOpening = false;
            tvTest.setEllipsize(null);
            tvTest.setSingleLine(false);
/*            ViewGroup.LayoutParams params = tvTest.getLayoutParams();
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            tvTest.setLayoutParams(params);*/
            //tvTest.setEllipsize(null);
        }else{
            isOpening = true;
            tvTest.setEllipsize(TextUtils.TruncateAt.END);
            tvTest.setLines(4);
/*            ViewGroup.LayoutParams params = tvTest.getLayoutParams();
            params.height = 200;
            tvTest.setLayoutParams(params);*/
            //tvTest.setEllipsize(TextUtils.TruncateAt.END);
        }
    }
}
