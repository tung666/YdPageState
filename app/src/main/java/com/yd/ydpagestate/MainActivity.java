package com.yd.ydpagestate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yd.commonlibrary.pagestate.YdPageStateManager;
import com.yd.commonlibrary.pagestate.listener.OnEmptyRetryListener;
import com.yd.commonlibrary.pagestate.listener.OnErrorRetryListener;

public class MainActivity extends AppCompatActivity {

    private YdPageStateManager ydPageStateManager;
    private RelativeLayout relativeLayout;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ydPageStateManager = YdPageStateManager.generate(this, R.id.rlay_parent);
        relativeLayout = (RelativeLayout) findViewById(R.id.rlay_parent);
        textView = (TextView) findViewById(R.id.textView);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.content:
                switchState(0);
                break;
            case R.id.loading:
                switchState(1);
                break;
            case R.id.empty:
                switchState(2);
                break;
            case R.id.error:
                switchState(3);
                break;
        }
        return true;
    }

    private void switchState(int type) {
        switch (type) {
            case 0:
                ydPageStateManager.showContent();
                break;
            case 1:
                ydPageStateManager.showLoading();
                break;
            case 2:
                ydPageStateManager.showEmpty(getResources().getDrawable(R.mipmap.monkey_nodata),
                        getString(R.string.ydPageState_empty_title), getString(R.string.ydPageState_empty_details), new OnEmptyRetryListener() {
                            @Override
                            public void onEmptyRetry(View view) {
                                ydPageStateManager.showLoading();
                            }
                        });
                break;
            case 3:
                //设置加载错误页显示
                ydPageStateManager.showError(getResources().getDrawable(R.mipmap.monkey_cry),
                        getString(R.string.ydPageState_error_title), getString(R.string.ydPageState_error_details),
                        getString(R.string.ydPageState_retry), new OnErrorRetryListener() {
                            @Override
                            public void onErrorRetry(View view) {
                                ydPageStateManager.showLoading();
                            }
                        });
                break;
        }
    }
}
