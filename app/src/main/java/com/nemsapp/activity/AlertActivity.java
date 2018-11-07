package com.nemsapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.format.draw.MultiLineDrawFormat;
import com.bin.david.form.data.table.TableData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nemsapp.R;
import com.nemsapp.util.CfgData;
import com.nemsapp.util.Constants;
import com.nemsapp.util.Utils;
import com.nemsapp.vo.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.nemsapp.util.Constants.*;


public class AlertActivity extends AppCompatActivity {

    private OkHttpClient okHttpClient = new OkHttpClient();

    private SmartTable table;

    private EventInfo[] data;

    final Column<String> dataColumn_1 = new Column<>("时间", "time");
    final Column<String> dataColumn_2 = new Column<>("监控单元", "unit");
    final Column<String> dataColumn_3 = new Column<>("事件名称", "event");
    final Column<String> dataColumn_4 = new Column<>("事件内容", "info");
    final Column<String> dataColumn_5 = new Column<>("相关数据", "more");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        table = findViewById(R.id.alert_table);

        getAlert();

        table.getConfig().setMinTableWidth(getWindowManager().getDefaultDisplay().getWidth())
                .setShowXSequence(false);

    }

    private void getAlert() {

        String url = "http://" + ip + ":8080/alert/getAlert";

        // 创建request
        final Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        //同步请求
        final Call call = okHttpClient.newCall(request);

        //发起请求
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
                            final Gson gson = new Gson();
                            final ArrayList<AlertData> alertDatas = new ArrayList<>();
                            data = gson.fromJson(strdata, new TypeToken<EventInfo[]>() {
                            }.getType());

                            for (int i = 0; i < data.length; i++) {
                                AlertData alertData = new AlertData();
                                int id = data[i].getId();
                                switch (Utils.getTypeInId(id)) {
                                    case IDAN:
                                        AnO anO = CfgData.getInstance().getAnO(id);
                                        alertData.setEvent(anO.getCname());
                                        break;
                                    case IDACC:
                                        AcO acO = CfgData.getInstance().getAcO(id);
                                        alertData.setEvent(acO.getCname());
                                        break;
                                    case IDST:
                                        StO stO = CfgData.getInstance().getStO(id);
                                        alertData.setEvent(stO.getCname());
                                }
                                alertData.setUnit(CfgData.getInstance().getUnitInfoByNo(Utils.getUnitNoInId(id)).getName());
                                alertData.setTime(data[i].getStrTime().substring(0, data[i].getStrTime().length() - 4));
                                alertData.setInfo(data[i].getInfo());
                                if (data[i].getEventLogs() != null && data[i].getEventLogs().size() > 0) {
                                    alertData.setMore("查看详情");
                                } else {
                                    alertData.setMore("无更多信息");
                                }
                                alertDatas.add(alertData);
                            }

                            tableData = new TableData("即时警报", alertDatas, dataColumn_1, dataColumn_2, dataColumn_3, dataColumn_4, dataColumn_5);
                            tableData.setOnItemClickListener(new TableData.OnItemClickListener() {
                                @Override
                                public void onClick(Column column, String value, Object o, int col, int row) {
                                    if (value != null && value.equals("查看详情")) {
                                        List<EventLog> eventLogs = data[row].getEventLogs();
                                        Intent intent = new Intent(AlertActivity.this, EventLogActivity.class);

                                        //传递具体事件信息
                                        intent.putExtra("data", gson.toJson(eventLogs));

                                        //传递表头
                                        String title = alertDatas.get(row).getEvent() + " " + alertDatas.get(row).getInfo() + " 时间：" + alertDatas.get(row).getTime();
                                        intent.putExtra("title", title);
                                        startActivity(intent);
                                    }
                                }
                            });
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
