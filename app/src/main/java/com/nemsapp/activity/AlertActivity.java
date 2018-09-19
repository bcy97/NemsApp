package com.nemsapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.table.TableData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nemsapp.R;
import com.nemsapp.util.Constants;
import com.nemsapp.vo.EventInfo;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class AlertActivity extends AppCompatActivity {

    private OkHttpClient okHttpClient = new OkHttpClient();

    private SmartTable table;

    final Column<String> dataColumn_1 = new Column<>("序号", "id");
    final Column<Integer> dataColumn_2 = new Column<>("时间", "time");
    final Column<Integer> dataColumn_3 = new Column<>("监控单元", "unit");
    final Column<Integer> dataColumn_4 = new Column<>("事件名称", "event");
    final Column<Integer> dataColumn_5 = new Column<>("事件内容", "content");
    final Column<Integer> dataColumn_6 = new Column<>("相关数据", "data");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        getAlert();

        table.getConfig().setMinTableWidth(getWindowManager().getDefaultDisplay().getWidth());

    }

    private void getAlert() {
        String url = "http://" + Constants.ip + ":8080/alert/getAlert";

        final Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        final Call call = okHttpClient.newCall(request);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Response response = call.execute();
                    final String strdata = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TableData tableData = null;
                            Gson gson = new Gson();
                            EventInfo[] data = gson.fromJson(strdata, new TypeToken<EventInfo[]>() {
                            }.getType());
                            tableData = new TableData("即时警报", Arrays.asList(data), dataColumn_1, dataColumn_2, dataColumn_3, dataColumn_4, dataColumn_5, dataColumn_6);
                            table.setTableData(tableData);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
