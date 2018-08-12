package com.nemsapp.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.nemsapp.R;
import com.nemsapp.vo.NowData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class HistoryDataActivity extends AppCompatActivity implements View.OnClickListener {

    private TimePickerView pvTime;
    private Button sTime;
    private Button eTime;

    private ListView listView;

    private SmartTable table;

    final Column<String> dataColumn_1 = new Column<>("名称", "data1");
    final Column<Integer> dataColumn_2 = new Column<>("数值", "data2");
    final Column<Integer> dataColumn_3 = new Column<>("单位", "data3");

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
        listView = findViewById(R.id.history_data);

        List<NowData> list = new ArrayList<>();
        list.add(new NowData(0.1, 3.3, 2.3));
        list.add(new NowData(0.1, 3.3, 2.3));
        list.add(new NowData(0.1, 3.3, 2.3));
        list.add(new NowData(0.1, 3.3, 2.3));
        list.add(new NowData(0.1, 3.3, 2.3));
        TableData tableData = new TableData("", list, dataColumn_1, dataColumn_2, dataColumn_3);
        table = findViewById(R.id.table);
        table.setTableData(tableData);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, getWindow().getAttributes().height - 50);
        table.setLayoutParams(layoutParams);
        table.getConfig().setMinTableWidth(getWindowManager().getDefaultDisplay().getWidth());


        initDate();
    }

    private void initDate() {
        final List<String> list = new ArrayList<>();
        list.add("所有单元");
        list.add("单元1");
        list.add("单元2");
        list.add("单元3");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(HistoryDataActivity.this, "clicked", Toast.LENGTH_SHORT);
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
                } else if (v.getId() == R.id.btn_Etime && pvTime != null) {
                    eTime.setText("结束时间：" + getTime(date));
                }
                Toast.makeText(HistoryDataActivity.this, getTime(date), Toast.LENGTH_SHORT).show();
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


}
