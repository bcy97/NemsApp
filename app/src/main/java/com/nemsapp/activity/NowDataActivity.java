package com.nemsapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.table.TableData;
import com.nemsapp.R;
import com.nemsapp.vo.NowData;

import java.util.ArrayList;
import java.util.List;

public class NowDataActivity extends AppCompatActivity {

    private View view;

    private SmartTable table;

    private ListView listView;


    final Column<String> dataColumn_1 = new Column<>("名称", "data1");
    final Column<Integer> dataColumn_2 = new Column<>("数值", "data2");
    final Column<Integer> dataColumn_3 = new Column<>("单位", "data3");


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.test);
        view = View.inflate(this, R.layout.activity_table, null);
        setContentView(view);

        listView = findViewById(R.id.v4_listview);

        List<NowData> list = new ArrayList<>();
        list.add(new NowData(0.1, 3.3, 2.3));
        list.add(new NowData(0.1, 3.3, 2.3));
        list.add(new NowData(0.1, 3.3, 2.3));
        list.add(new NowData(0.1, 3.3, 2.3));
        list.add(new NowData(0.1, 3.3, 2.3));
        TableData tableData = new TableData("", list, dataColumn_1, dataColumn_2, dataColumn_3);
        table = findViewById(R.id.table);
        table.setTableData(tableData);
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
                Toast.makeText(NowDataActivity.this, "clicked", Toast.LENGTH_SHORT);
            }
        });
//        drawerLayout.openDrawer(Gravity.LEFT);//侧滑打开  不设置则不会默认打开
    }

}
