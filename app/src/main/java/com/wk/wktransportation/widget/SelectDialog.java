package com.wk.wktransportation.widget;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
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
    private Button clickedView;
    private AdapterView.OnItemClickListener onListItemClickListener;
    public SelectDialog(@NonNull Context context) {
        super(context);
        init(context);
    }

    public void setClickedView(Button clickedView) {
        this.clickedView = clickedView;
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
                new AsyncTask<Void, Void, List<String>>() {
                    @Override
                    protected List<String> doInBackground(Void... voids) {
                        List<String > queryResult = onQueryBtnClickListener.onQuery(clickedView,keywordEditText.getText().toString());
                        queryResultList = queryResult;
                        return queryResult;
                    }

                    @Override
                    protected void onPostExecute(List<String> strings) {
                        super.onPostExecute(strings);
                        Log.d(TAG, "do==== SelectDialog run:2");
                        String[] result = new String[strings.size()];
                        strings.toArray(result);
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context,
                                android.R.layout.simple_list_item_1,
                                 result);
                        resultListView.setAdapter(arrayAdapter);
                    }
                }.execute();
            }
        });
        resultListView = view.findViewById(R.id.list_query);
        BaseAdapter adapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1);
        resultListView.setAdapter(adapter);
        resultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (queryResultList!=null){
                    onQueryBtnClickListener.onItemClick(clickedView,queryResultList.get(position));
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
        List<String> onQuery(Button view,String keyword);
        void onItemClick(Button button,String item);
    }
}
