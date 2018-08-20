package com.wk.wktransportation.ui.selectpage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.wk.wktransportation.R;
import com.wk.wktransportation.rxbus.Event;
import com.wk.wktransportation.ui.BaseActivity;
import com.wk.wktransportation.widget.SelectDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * @discription 描述这个类的作用
 * <p>
 * **********************************
 * @auth @yangzehui
 * @email @784818984@qq.com
 * @date @2018/8/20 20:12
 * **********************************
 **/
public class SelectActivity extends BaseActivity implements SelectDialog.SelectDialogListener{

    private static final String TAG = "SelectActivity";
    private static final int INCUBATOR_TYPE_CAR = 0;
    private static final int INCUBATOR_TYPE_BOX = 1;
    private static final int INCUBATOR_TYPE_FREEZE_BOX = 2;

    public static final String INCUBATOR_TYPE = "incubator_type";
    /**
     * 选择车
     */
    private View mCarSelectView;
    /**
     * 开始时间选择器
     */
    private View mStartTimeSelectView;
    /**
     * 结束时间选择器
     */
    private View mEndTimeSelectView;
    /**
     * 客户选择器
     */
    private View mCustomerSelectView;
    private TextView mTxvCar;
    private TextView mTxvStartTime;
    private TextView mTxvEndTime;
    private TextView mTxvCustomer;
    private Button mCarSelectBtn;
    private Button mStartDateSelectBtn;
    private Button mStartTimeSelectBtn;
    private Button mEndDateSelectBtn;
    private Button mEndTimeSelectBtn;
    private Button mCustomerSelectBtn;

    private SelectDialog mSelectDialog;

    private View.OnClickListener onCarSelectListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mSelectDialog.show();
        }
    };
    @Override
    public void onNotification(Event event) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        initView();
        initIncubator();
        initListener();
    }

    private void initListener() {
        mCarSelectBtn.setOnClickListener(onCarSelectListener);
    }

    private void initView() {
        ViewGroup rootView = findViewById(R.id.select_root_view);
        mCarSelectView = LayoutInflater.from(this).inflate(R.layout.one_btn_select_view,null);
        mStartTimeSelectView = LayoutInflater.from(this).inflate(R.layout.tow_btn_select_view,null);
        mEndTimeSelectView = LayoutInflater.from(this).inflate(R.layout.tow_btn_select_view,null);
        mCustomerSelectView = LayoutInflater.from(this).inflate(R.layout.one_btn_select_view,null);
        rootView.addView(mCarSelectView);
        rootView.addView(mStartTimeSelectView);
        rootView.addView(mEndTimeSelectView);
        rootView.addView(mCustomerSelectView);

        mTxvCar = mCarSelectView.findViewById(R.id.tv_name);

        mTxvStartTime = mStartTimeSelectView.findViewById(R.id.tv_name);
        mTxvStartTime.setText("开始时间");
        mTxvEndTime = mEndTimeSelectView.findViewById(R.id.tv_name);
        mTxvEndTime.setText("到达时间");
        mTxvCustomer = mCustomerSelectView.findViewById(R.id.tv_name);
        mTxvCustomer.setText("客户名称");

        mCarSelectBtn = mCarSelectView.findViewById(R.id.btn_select);

        mStartDateSelectBtn = mStartTimeSelectView.findViewById(R.id.btn_select_one);
        mStartDateSelectBtn.setText("开始日期");
        mStartTimeSelectBtn = mStartTimeSelectView.findViewById(R.id.btn_select_two);
        mStartTimeSelectBtn.setText("开始时间");
        mEndDateSelectBtn = mEndTimeSelectView.findViewById(R.id.btn_select_one);
        mEndDateSelectBtn.setText("结束日期");
        mEndTimeSelectBtn = mEndTimeSelectView.findViewById(R.id.btn_select_two);
        mEndTimeSelectBtn.setText("结束时间");
        mCustomerSelectBtn = mCustomerSelectView.findViewById(R.id.btn_select);
        mCustomerSelectBtn.setText("客户名称");
        mSelectDialog = new SelectDialog(this);
        mSelectDialog.setOnQueryBtnClickListener(this);
    }
    private void initIncubator(){
        int type = getIntent().getIntExtra(INCUBATOR_TYPE,INCUBATOR_TYPE_CAR);
        if (type == INCUBATOR_TYPE_CAR){
            mTxvCar.setText("车牌号");
            mCarSelectBtn.setText("请选择车牌号码");
        }
        if (type == INCUBATOR_TYPE_BOX){
            mTxvCar.setText("保温箱号");
            mCarSelectBtn.setText("请选择保温箱");
        }
        if (type == INCUBATOR_TYPE_FREEZE_BOX){
            mTxvCar.setText("冷藏箱号");
            mCarSelectBtn.setText("请选择冷藏箱");
        }
    }

    @Override
    public List<String> onQuery(String keyword) {
        List<String > strings = new ArrayList<>();
        strings.add("1");
        strings.add("2");
        strings.add("3");
        strings.add("4");
        return strings;
    }

    @Override
    public void onItemClick(String item) {
        Log.d(TAG, "do==== SelectActivity onItemClick:"+item);
    }
}
