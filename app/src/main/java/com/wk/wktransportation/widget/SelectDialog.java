package com.wk.wktransportation.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.wk.wktransportation.R;
import com.wk.wktransportation.util.ThreadPool;
import com.wk.wktransportation.util.ThreadTool;

import java.util.ArrayList;
import java.util.List;

/**
 * @discription 描述这个类的作用
 * <p>
 * **********************************
 * @auth @yangzehui
 * @email @784818984@qq.com
 * @date @2018/8/20 20:28
 * **********************************
 **/
public class SelectDialog extends Dialog{
    private static final String TAG = "SelectDialog";
    private SelectDialogListener onQueryBtnClickListener = null;
    private Button queryBtn;
    private ListView resultListView;
    private EditText keywordEditText;
    private List<String> queryResultList;
    private AdapterView.OnItemClickListener onListItemClickListener;
    public SelectDialog(@NonNull Context context) {
        super(context);
        init(context);
    }

    public SelectDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init(context);

    }

    protected SelectDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }
    private void init(final Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.select_dialog,null);
        setContentView(view);
        queryBtn = view.findViewById(R.id.btn_query);
        keywordEditText = view.findViewById(R.id.edit_keyword);
        queryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThreadTool.SINGLE_SERVICE.submit(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "do==== SelectDialog run:1");
                        queryResultList.clear();
                        List<String > queryResult = new ArrayList<>();
                        if (onQueryBtnClickListener!=null){
                            queryResult = onQueryBtnClickListener.onQuery(keywordEditText.getText().toString());
                        }
                        queryResultList = queryResult;
                        final List<String> finalQueryResult = queryResult;
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Log.d(TAG, "do==== SelectDialog run:2");
                                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context,
                                        android.R.layout.simple_list_item_1,
                                        (String[]) finalQueryResult.toArray());
                                resultListView.setAdapter(arrayAdapter);
                            }
                        });
//                        ThreadPool.MAIN_WORKER.schedule(new Runnable() {
//                            @Override
//                            public void run() {
//
//                            }
//                        });
                    }
                });
            }
        });
        resultListView = view.findViewById(R.id.list_query);
        BaseAdapter adapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1);
        resultListView.setAdapter(adapter);
        resultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (queryResultList!=null){
                    onQueryBtnClickListener.onItemClick(queryResultList.get(position));
                }else {
                    Log.e(TAG, "do====SelectDialog.onItemClick.:  ");
                }

            }
        });
    }
    public void setOnQueryBtnClickListener(SelectDialogListener listener){
        this.onQueryBtnClickListener = listener;
    }
    public interface SelectDialogListener {
        List<String> onQuery(String keyword);
        void onItemClick(String item);
    }
}
