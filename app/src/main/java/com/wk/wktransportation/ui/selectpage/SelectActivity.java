package com.wk.wktransportation.ui.selectpage;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.wk.wktransportation.R;
import com.wk.wktransportation.entity.CarInfo;
import com.wk.wktransportation.entity.Customer;
import com.wk.wktransportation.entity.Tempertumer;
import com.wk.wktransportation.net.RestfulClient;
import com.wk.wktransportation.rxbus.Event;
import com.wk.wktransportation.ui.BaseActivity;
import com.wk.wktransportation.util.ThreadPool;
import com.wk.wktransportation.util.ThreadTool;
import com.wk.wktransportation.widget.SelectDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    private Button queryBtn;
    private TextView mTxvCar;
    private TextView mTxvStartTime;
    private TextView mTxvEndTime;
    private TextView mTxvCustomer;
    private Button mCarSelectBtn;
    private Button mStartDateSelectBtn;
    private Button mEndDateSelectBtn;
    private Button mCustomerSelectBtn;

    private SelectDialog mSelectDialog;

    private View.OnClickListener onCarSelectListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           showDialog(v,"搜索车牌号");
        }
    };
    private View.OnClickListener onStartDataClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showTimePicker(v);
        }
    };

    private View.OnClickListener onEndDateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showTimePicker(v);
        }
    };

    private View.OnClickListener onSelectCustomerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showDialog(v,"搜索客户名称");
        }
    };
    private View.OnClickListener onQueryBtnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final String carNumber = mCarSelectBtn.getText().toString();
            final String startTime = mStartDateSelectBtn.getText().toString();
            final String endTime = mEndDateSelectBtn.getText().toString();
            new AsyncTask<Void, Void, List<Tempertumer>>() {
                @Override
                protected List<Tempertumer> doInBackground(Void... voids) {
                    List<Tempertumer> list = RestfulClient.getInstance().selectTempertumer(endTime,startTime,carNumber);
                    return list;
                }

                @Override
                protected void onPostExecute(List<Tempertumer> tempertumers) {
                    super.onPostExecute(tempertumers);
                    Intent intent = new Intent();
                }
            }.execute();
        }
    };
    private void showDialog(View view,String string){
        mSelectDialog.setClickedView((Button) view);
        mSelectDialog.show();
        mSelectDialog.setTitle(string);
    }
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
        mStartDateSelectBtn.setOnClickListener(onStartDataClickListener);
        mEndDateSelectBtn.setOnClickListener(onEndDateClickListener);
        mCustomerSelectBtn.setOnClickListener(onSelectCustomerClickListener);
        queryBtn.setOnClickListener(onQueryBtnClickListener);
    }

    private void initView() {
        ViewGroup rootView = findViewById(R.id.select_root_view);
        mCarSelectView = LayoutInflater.from(this).inflate(R.layout.one_btn_select_view,null);
        mStartTimeSelectView = LayoutInflater.from(this).inflate(R.layout.one_btn_select_view,null);
        mEndTimeSelectView = LayoutInflater.from(this).inflate(R.layout.one_btn_select_view,null);
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

        mStartDateSelectBtn = mStartTimeSelectView.findViewById(R.id.btn_select);
        mStartDateSelectBtn.setText("开始日期");
        mEndDateSelectBtn = mEndTimeSelectView.findViewById(R.id.btn_select);
        mEndDateSelectBtn.setText("结束日期");
        mCustomerSelectBtn = mCustomerSelectView.findViewById(R.id.btn_select);
        mCustomerSelectBtn.setText("客户名称");
        queryBtn = findViewById(R.id.btn_start_query);
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
    private void showTimePicker(final View button){
        TimePickerView pickerView = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                ((Button)button).setText(getTime(date));
            }
        }).build();
        pickerView.setDate(Calendar.getInstance());
        pickerView.show();
    }
    /**
     * @describe 时间显示样式
     * @params Date
     * @return 时间
     **/
    private String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//时间显示样式，可选
        return format.format(date);
    }

    @Override
    public List<String> onQuery(Button view,String keyword) {
        if (view.getText().equals("请选择车牌号码")){
            List<CarInfo> carInfos = RestfulClient.getInstance().getCarInfo(keyword);
            List<String> carNames = new ArrayList<>();
            for (CarInfo carInfo:carInfos){
                carNames.add(carInfo.getCarnumber());
            }
            return carNames;
        }

        if (view.getText().equals("客户名称")){
            List<Customer> customers = RestfulClient.getInstance().getCustomer(keyword);
            List<String> customerNames = new ArrayList<>();
            for (Customer customer:customers){
                customerNames.add(customer.getHospitalname());
            }
            return customerNames;
        }


        return null;
    }

    @Override
    public void onItemClick(Button button,String item) {
        button.setText(item);
        mSelectDialog.dismiss();
    }
}
