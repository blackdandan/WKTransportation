package com.wk.wktransportation.ui.selecttypepage;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.wk.wktransportation.R;
import com.wk.wktransportation.ui.selectpage.SelectActivity;

import static com.wk.wktransportation.ui.selectpage.SelectActivity.INCUBATOR_TYPE;

public class SelectTypePage extends AppCompatActivity {
    public static final int TYPE_CAR = 0;
    public static final int TYPE_TEMP_BOX = 1;
    public static final int TYPE_FREEZE_BOX = 2;
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int type = 0;
            switch (v.getId()){
                case R.id.car:
                    type = TYPE_CAR;
                    break;
                case R.id.temp_box:
                    type = TYPE_TEMP_BOX;
                    break;
                case R.id.freeze_box:
                    type = TYPE_FREEZE_BOX;
                    break;
                default:
                    break;
            }
            Intent intent = new Intent(SelectTypePage.this,SelectActivity.class);
            intent.putExtra(INCUBATOR_TYPE,type);
            startActivity(intent);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_type_page);
        initView();
    }

    private void initView() {
        findViewById(R.id.car).setOnClickListener(onClickListener);
        findViewById(R.id.temp_box).setOnClickListener(onClickListener);
        findViewById(R.id.freeze_box).setOnClickListener(onClickListener);
    }
}
