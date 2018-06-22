package com.nemsapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.table.TableData;
import com.nemsapp.R;
import com.nemsapp.vo.AlertData;
import com.nemsapp.vo.NowData;

import java.util.ArrayList;
import java.util.List;

public class AlertActivity extends AppCompatActivity {

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
}
