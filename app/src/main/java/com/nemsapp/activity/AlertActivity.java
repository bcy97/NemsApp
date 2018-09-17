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
import com.nemsapp.vo.AlertData;
import com.nemsapp.vo.EventInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
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

        List<AlertData> list = new ArrayList<>();
        list.add(new AlertData(0.1, 3.3, 2.3, 3.3, 2.33, 2.3));
        list.add(new AlertData(0.1, 3.3, 2.3, 3.3, 2.33, 2.3));
        list.add(new AlertData(0.1, 3.3, 2.3, 3.3, 2.33, 2.3));
        list.add(new AlertData(0.1, 3.3, 2.3, 3.3, 2.33, 2.3));
        list.add(new AlertData(0.1, 3.3, 2.3, 3.3, 2.33, 2.3));
        list.add(new AlertData(0.1, 3.3, 2.3, 3.3, 2.33, 2.3));
        list.add(new AlertData(0.1, 3.3, 2.3, 3.3, 2.33, 2.3));
        TableData tableData = new TableData("", list, dataColumn_1, dataColumn_2, dataColumn_3,
                dataColumn_4, dataColumn_5, dataColumn_6);
        table = findViewById(R.id.alert_table);
        table.setTableData(tableData);
        table.getConfig().setMinTableWidth(getWindowManager().getDefaultDisplay().getWidth());

    }

    private void getDataByUnitName(final String unitname) {
        String url = "http://" + Constants.ip + ":8080/cumulant/getDataByUnitName";

        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

        final Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(mediaType, unitname))
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
                            tableData = new TableData(unitname, Arrays.asList(data), dataColumn_1, dataColumn_2, dataColumn_3, dataColumn_4, dataColumn_5, dataColumn_6);
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
