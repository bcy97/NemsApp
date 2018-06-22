package com.nemsapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.table.TableData;
import com.nemsapp.R;
import com.nemsapp.vo.Data;

import java.util.ArrayList;
import java.util.List;

public class TableActivity extends AppCompatActivity {

    private View view;

    private SmartTable table;


    final Column<String> dataColumn_1 = new Column<>("数据1", "data1");
    final Column<Integer> dataColumn_2 = new Column<>("数据2", "data2");
    final Column<Integer> dataColumn_3 = new Column<>("数据3", "data3");
    final Column<Integer> dataColumn_4 = new Column<>("数据4", "data4");
    final Column<Integer> dataColumn_5 = new Column<>("数据5", "data5");
    final Column<Integer> dataColumn_6 = new Column<>("数据6", "data6");
    final Column<Integer> dataColumn_7 = new Column<>("数据7", "data7");
    final Column<Integer> dataColumn_8 = new Column<>("数据8", "data8");
    final Column<Integer> dataColumn_9 = new Column<>("数据9", "data9");
    final Column<Integer> dataColumn_10 = new Column<>("数据10", "data10");
    final Column<Integer> dataColumn_11 = new Column<>("数据11", "data11");
    final Column<Integer> dataColumn_12 = new Column<>("数据12", "data12");


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.test);
        view = View.inflate(this, R.layout.activity_table, null);
        setContentView(view);

        List<Data> list = new ArrayList<>();
        list.add(new Data(0.1, 3.3, 2.3, 3.3, 4.3, 5.3, 13.3, 5.3, 123.4, 12.2, 123.23, 9.2));
        TableData tableData = new TableData("", list, dataColumn_1, dataColumn_2, dataColumn_3,
                dataColumn_4, dataColumn_5, dataColumn_6, dataColumn_7, dataColumn_8, dataColumn_9, dataColumn_10, dataColumn_11, dataColumn_12);
        table = findViewById(R.id.table);
        table.setTableData(tableData);

    }
}
