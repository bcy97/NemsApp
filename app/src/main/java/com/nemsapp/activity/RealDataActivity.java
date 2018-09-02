package com.nemsapp.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.table.TableData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nemsapp.R;
import com.nemsapp.util.Constants;
import com.nemsapp.util.FileHelper;
import com.nemsapp.vo.AnValue;
import com.nemsapp.vo.FileMD5;
import com.nemsapp.vo.RealData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RealDataActivity extends AppCompatActivity {

    private OkHttpClient okHttpClient = new OkHttpClient();

    private View view;

    private SmartTable table;

    private ListView listView;

    private List<String> unitlist;


    final Column<String> dataColumn_1 = new Column<>("名称", "name");
    final Column<Integer> dataColumn_2 = new Column<>("数值", "data");
    final Column<Integer> dataColumn_3 = new Column<>("单位", "unit");

    List<RealData> realData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.test);
        view = View.inflate(this, R.layout.activity_table, null);
        setContentView(view);

        listView = findViewById(R.id.v4_listview);

        initSideBar();

        List<RealData> list = new ArrayList<>();
        list.add(new RealData("123"));
        list.add(new RealData("12"));
        list.add(new RealData("13"));
        list.add(new RealData("23"));
        TableData tableData = new TableData("", list, dataColumn_1, dataColumn_2, dataColumn_3);
        table = findViewById(R.id.table);
        table.setTableData(tableData);
        table.getConfig().setMinTableWidth(getWindowManager().getDefaultDisplay().getWidth());


    }

    private void initSideBar() {

        //获取点列表
        List<FileMD5> fileMD5List = FileHelper.getConfigFileMD5().get("unitConfigs");

        unitlist = new ArrayList<>();
        for (int i = 0; i < fileMD5List.size(); i++) {
            String name = fileMD5List.get(i).getFileName();
            unitlist.add(name.substring(0, name.length() - 4));
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, unitlist);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(unitlist.get(position));
                getDataByUnitName(unitlist.get(position));
            }
        });
//        drawerLayout.openDrawer(Gravity.LEFT);//侧滑打开  不设置则不会默认打开
    }

    public void getDataByUnitName(final String unitname) {
        String url = "http://" + Constants.ip + ":8080/realdata/getRealData";

        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

        final Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(mediaType, "1#开闭所201柜1#进线"))
                .build();
        final Call call = okHttpClient.newCall(request);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Response response = call.execute();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TableData tableData = null;
                            try {
                                Gson gson = new Gson();
                                String strdata = response.body().string();
                                Map<String, AnValue> data = gson.fromJson(strdata, new TypeToken<HashMap<String, AnValue>>() {
                                }.getType());
                                realData = new ArrayList<>();
                                for (String name : data.keySet()) {
                                    realData.add(new RealData(name, data.get(name).getValid() == 1 ? data.get(name).getValue() : 0, ""));
                                }
                                tableData = new TableData(unitname, realData, dataColumn_1, dataColumn_2, dataColumn_3);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
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
