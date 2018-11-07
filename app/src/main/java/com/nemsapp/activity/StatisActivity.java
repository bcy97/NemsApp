package com.nemsapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.table.TableData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nemsapp.R;
import com.nemsapp.adapter.SimpleTreeAdapter;
import com.nemsapp.treelist.Node;
import com.nemsapp.treelist.OnTreeNodeClickListener;
import com.nemsapp.treelist.TreeListViewAdapter;
import com.nemsapp.util.CfgData;
import com.nemsapp.util.Utils;
import com.nemsapp.vo.*;

import okhttp3.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.nemsapp.util.Constants.*;

public class StatisActivity extends AppCompatActivity implements View.OnClickListener {

    private TimePickerView pvTime;
    private Button sTime;
    private Button eTime;
    private Button search;


    private SmartTable table;

    //设置起始时间
    private Date startTime;
    private Date endTime;

    //设置要查询的点名
    private List<String> unitnames;

    final Column<String> dataColumn_1 = new Column<>("时间", "time");
    final Column<Integer> dataColumn_2 = new Column<>("监控单元", "unit");
    final Column<Integer> dataColumn_3 = new Column<>("事件名称", "event");
    final Column<Integer> dataColumn_4 = new Column<>("事件内容", "info");
    final Column<Integer> dataColumn_5 = new Column<>("相关数据", "more");

    //侧边栏数据
    private ListView sideBar;
    private TreeListViewAdapter siderBarAdapter;
    private List<Node> sideBarDatas = new ArrayList<>();

    private OkHttpClient okHttpClient = new OkHttpClient();

    //返回的数据
    private EventInfo[] data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statis);

        //隐藏actionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        //初始化时间选择器
        initCustomTimePicker();

        sTime = findViewById(R.id.btn_Stime);
        sTime.setOnClickListener(this);

        eTime = findViewById(R.id.btn_Etime);
        eTime.setOnClickListener(this);

        search = findViewById(R.id.btn_Search);
        search.setOnClickListener(this);

        //初始化表格
        table = findViewById(R.id.static_table);
        table.getConfig().setMinTableWidth(getWindowManager().getDefaultDisplay().getWidth())
                .setShowXSequence(false);

        //初始化侧边栏
        sideBar = findViewById(R.id.static_data);
        initSideBar();

        //初始化默认参数，获取默认数据
        unitnames = new ArrayList<>();
        unitnames.add(sideBarDatas.get(1).getName());
        getEventInfoByUnitName(unitnames);

    }

    private void initSideBar() {

        //获取所有单元列表
        List<UnitInfo> unitList = CfgData.getInstance().getUnitList();

        sideBarDatas.add(new Node(0, -1, "所有单元"));

        for (UnitInfo unit : unitList) {
            sideBarDatas.add(new Node(unit.getUnitNo(), 0, unit.getName()));
        }

        siderBarAdapter = new SimpleTreeAdapter(sideBar, StatisActivity.this, sideBarDatas, 0, R.mipmap.tree_ex, R.mipmap.tree_ec);
        sideBar.setAdapter(siderBarAdapter);

//        drawerLayout.openDrawer(Gravity.LEFT);//侧滑打开  不设置则不会默认打开
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_Stime && pvTime != null) {
            // pvTime.setDate(Calendar.getInstance());
            /* pvTime.show(); //show timePicker*/
            pvTime.show(v);//弹出时间选择器，传递参数过去，回调的时候则可以绑定此view
        } else if (v.getId() == R.id.btn_Etime && pvTime != null) {
            pvTime.show(v);
        } else if (v.getId() == R.id.btn_Search) {
            if (startTime == null) {
                Toast.makeText(this, "请选择开始时间", Toast.LENGTH_SHORT);
                return;
            }
            if (endTime == null) {
                Toast.makeText(this, "请选择结束时间", Toast.LENGTH_SHORT);
                return;
            }
            unitnames = new ArrayList<>();

            for (Node node : sideBarDatas) {
                if (node.isLeaf() && node.isChecked()) {
                    unitnames.add(node.getName());
                }
            }
            if (unitnames.size() <= 0) {
                unitnames.add(sideBarDatas.get(1).getName());
            }

            getEventInfoByUnitNameAndTime(startTime, endTime, unitnames);
        }
    }

    private void initCustomTimePicker() {

        /**
         * @description
         *
         * 注意事项：
         * 1.自定义布局中，id为 optionspicker 或者 timepicker 的布局以及其子控件必须要有，否则会报空指针.
         * 具体可参考demo 里面的两个自定义layout布局。
         * 2.因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
         * setRangDate方法控制起始终止时间(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
         */
        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(2014, 1, 23);
        Calendar endDate = Calendar.getInstance();
        endDate.set(2027, 2, 28);
        //时间选择器 ，自定义布局
        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                if (v.getId() == R.id.btn_Stime && pvTime != null) {
                    sTime.setText("开始时间：" + getTime(date));
                    startTime = date;
                } else if (v.getId() == R.id.btn_Etime && pvTime != null) {
                    eTime.setText("结束时间：" + getTime(date));
                    endTime = date;
                }
                Toast.makeText(StatisActivity.this, getTime(date), Toast.LENGTH_SHORT).show();
            }
        })
                .setDate(selectedDate)
                .setRangDate(startDate, endDate)
                .setLayoutRes(R.layout.time_picker, new CustomListener() {

                    @Override
                    public void customLayout(View v) {
                        final TextView tvSubmit = v.findViewById(R.id.tv_finish);
                        ImageView ivCancel = v.findViewById(R.id.iv_cancel);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvTime.returnData();
                                pvTime.dismiss();
                            }
                        });
                        ivCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvTime.dismiss();
                            }
                        });
                    }
                })
                .setContentTextSize(18)
                .setType(new boolean[]{true, true, true, true, true, false})
                .setLabel("年", "月", "日", "时", "分", "秒")
                .setLineSpacingMultiplier(1.2f)
                .setTextXOffset(0, 0, 0, 40, 0, -40)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(0xFF24AD9D)
                .build();

    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }


    private void getEventInfoByUnitName(final List<String> unitnames) {

        String url = "http://" + ip + ":8080/eventInfo/getInfo";

        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

        final Gson gson = new Gson();
        Map<String, String> param = new HashMap<>();
        Date now = new Date();
        param.put("stime", getTime(new Date(now.getTime() - 86400000L)));
        param.put("etime", getTime(now));
        param.put("unitname", gson.toJson(unitnames));
        param.put("type", 0 + "");

        RequestBody requestBody = RequestBody.create(mediaType, gson.toJson(param));

        // 创建request
        final Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
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
                    System.out.println(strdata);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TableData tableData = null;
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

                            tableData = new TableData("查询统计", alertDatas, dataColumn_1, dataColumn_2, dataColumn_3, dataColumn_4, dataColumn_5);
                            tableData.setOnItemClickListener(new TableData.OnItemClickListener() {
                                @Override
                                public void onClick(Column column, String value, Object o, int col, int row) {
                                    if (value != null && value.equals("查看详情")) {
                                        List<EventLog> eventLogs = data[row].getEventLogs();
                                        Intent intent = new Intent(StatisActivity.this, EventLogActivity.class);
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

    private void getEventInfoByUnitNameAndTime(final Date stime, Date etime, final List<String> unitnames) {

        String url = "http://" + ip + ":8080/eventInfo/getInfo";

        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

        final Gson gson = new Gson();
        Map<String, String> param = new HashMap<>();
        param.put("stime", getTime(stime));
        param.put("etime", getTime(etime));
        param.put("unitname", gson.toJson(unitnames));
        param.put("type", 0 + "");

        RequestBody requestBody = RequestBody.create(mediaType, gson.toJson(param));

        // 创建request
        final Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
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
                    System.out.println(strdata);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TableData tableData = null;
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

                            tableData = new TableData("查询统计", alertDatas, dataColumn_1, dataColumn_2, dataColumn_3, dataColumn_4, dataColumn_5);
                            tableData.setOnItemClickListener(new TableData.OnItemClickListener() {
                                @Override
                                public void onClick(Column column, String value, Object o, int col, int row) {
                                    if (value != null && value.equals("查看详情")) {
                                        List<EventLog> eventLogs = data[row].getEventLogs();
                                        Intent intent = new Intent(StatisActivity.this, EventLogActivity.class);
                                        intent.putExtra("data", gson.toJson(eventLogs));
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

