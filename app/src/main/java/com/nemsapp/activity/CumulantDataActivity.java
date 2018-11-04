package com.nemsapp.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.nemsapp.util.CfgData;
import com.nemsapp.util.Constants;
import com.nemsapp.vo.Cumulant;
import com.nemsapp.vo.UnitInfo;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CumulantDataActivity extends AppCompatActivity implements View.OnClickListener {

    private OkHttpClient okHttpClient = new OkHttpClient();

    private TimePickerView pvTime;
    private Button sTime;
    private Button eTime;
    private Button search;

    private Date startTime;
    private Date endTime;

    private String unitName;

    private ListView listView;

    private SmartTable table;

    private List<String> unitlist;

    final Column<String> dataColumn_1 = new Column<>("名称", "name");
    final Column<Double> dataColumn_2 = new Column<>("当月", "thisMonth");
    final Column<Double> dataColumn_3 = new Column<>("当日", "today");
    final Column<Double> dataColumn_4 = new Column<>("上月", "lastMonth");
    final Column<Double> dataColumn_5 = new Column<>("上日", "lastday");
    final Column<Double> dataColumn_6 = new Column<>("统计值", "statis");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_data);

        //隐藏actionBar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        initCustomTimePicker();

        sTime = findViewById(R.id.btn_Stime);
        sTime.setOnClickListener(this);

        eTime = findViewById(R.id.btn_Etime);
        eTime.setOnClickListener(this);

        search = findViewById(R.id.btn_Search);
        search.setOnClickListener(this);

        listView = findViewById(R.id.history_data);

        initSideBar();


        table = findViewById(R.id.table);
        table.getConfig().setMinTableWidth(getWindowManager().getDefaultDisplay().getWidth())
                .setShowXSequence(false);

        getDataByUnitName(unitlist.get(0));

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
                    System.out.println(strdata);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TableData tableData = null;
                            Gson gson = new Gson();
                            List<Cumulant> data = gson.fromJson(strdata, new TypeToken<ArrayList<Cumulant>>() {
                            }.getType());
                            tableData = new TableData(unitname, data, dataColumn_1, dataColumn_2, dataColumn_3, dataColumn_4, dataColumn_5, dataColumn_6);
                            table.setTableData(tableData);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void getDataByUnitNameAndTime(final Date stime, final Date etime, final String unitname) {
        String url = "http://" + Constants.ip + ":8080/cumulant/getDataByUnitNameAndTime";

        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

        Map<String, String> data = new HashMap<>();
        data.put("stime", getTime(stime));
        data.put("etime", getTime(etime));
        data.put("unitname", unitname);
        Gson gson = new Gson();

        RequestBody requestBody = RequestBody.create(mediaType, gson.toJson(data));

        final Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        final Call call = okHttpClient.newCall(request);
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
                            Gson gson = new Gson();
                            List<Cumulant> data = gson.fromJson(strdata, new TypeToken<ArrayList<Cumulant>>() {
                            }.getType());

                            for (Cumulant cumulant : data) {
                                //修改当月的小数位数
                                BigDecimal bigDecimal = new BigDecimal(cumulant.getThisMonth());
                                cumulant.setThisMonth(bigDecimal.setScale(1, RoundingMode.UP).doubleValue());
                                //修改当日小数位数
                                bigDecimal = new BigDecimal(cumulant.getToday());
                                cumulant.setToday(bigDecimal.setScale(1, RoundingMode.UP).doubleValue());
                                //修改上月小数位数
                                bigDecimal = new BigDecimal(cumulant.getLastMonth());
                                cumulant.setLastMonth(bigDecimal.setScale(1, RoundingMode.UP).doubleValue());
                                //修改上日小数位数
                                bigDecimal = new BigDecimal(cumulant.getLastday());
                                cumulant.setLastday(bigDecimal.setScale(1, RoundingMode.UP).doubleValue());
                                //修改统计值小数位数
                                bigDecimal = new BigDecimal(cumulant.getStatis());
                                cumulant.setStatis(bigDecimal.setScale(1, RoundingMode.UP).doubleValue());
                                System.out.println(cumulant);
                            }

                            TableData tableData = new TableData(unitname, data, dataColumn_1, dataColumn_2, dataColumn_3, dataColumn_4, dataColumn_5, dataColumn_6);
                            table.setTableData(tableData);

                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void initSideBar() {

        //获取点列表
        List<UnitInfo> pointList = CfgData.getInstance().getUnitList();

        unitlist = new ArrayList<>();
        for (int i = 0; i < pointList.size(); i++) {
            String name = pointList.get(i).getName();
            unitlist.add(name);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, unitlist);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                unitName = unitlist.get(position);
                getDataByUnitName(unitName);
            }
        });
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
            if (unitName == null || unitName.equals("")) {
                unitName = unitlist.get(0);
            }
            getDataByUnitNameAndTime(startTime, endTime, unitName);
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
        final Calendar startDate = Calendar.getInstance();
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
                Toast.makeText(CumulantDataActivity.this, getTime(date), Toast.LENGTH_SHORT).show();
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
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return format.format(date);
    }


}
