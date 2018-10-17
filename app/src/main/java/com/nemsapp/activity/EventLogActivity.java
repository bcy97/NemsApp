package com.nemsapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.table.TableData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nemsapp.R;
import com.nemsapp.vo.EventLog;

import java.util.List;

public class EventLogActivity extends AppCompatActivity {

    private SmartTable table;

    final Column<String> dataColumn_1 = new Column<>("名称", "name");
    final Column<String> dataColumn_2 = new Column<>("类型", "type");
    final Column<Double> dataColumn_3 = new Column<>("数据", "data");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_log);

        table = findViewById(R.id.log_table);
        table.getConfig().setMinTableWidth(getWindowManager().getDefaultDisplay().getWidth())
                .setShowXSequence(false);

        Gson gson = new Gson();

        List<EventLog> logs = gson.fromJson(getIntent().getStringExtra("data"), new TypeToken<List<EventLog>>() {
        }.getType());

        TableData tableData = new TableData("test", logs, dataColumn_1, dataColumn_2, dataColumn_3);
        table.setTableData(tableData);
    }
}
