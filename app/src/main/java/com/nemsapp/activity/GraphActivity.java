package com.nemsapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.table.TableData;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nemsapp.R;
import com.nemsapp.adapter.SimpleTreeAdapter;
import com.nemsapp.treelist.Node;
import com.nemsapp.treelist.TreeListViewAdapter;
import com.nemsapp.util.CfgData;
import com.nemsapp.util.Constants;
import com.nemsapp.util.Utils;
import com.nemsapp.vo.AcO;
import com.nemsapp.vo.AlertData;
import com.nemsapp.vo.AnO;
import com.nemsapp.vo.EventInfo;
import com.nemsapp.vo.EventLog;
import com.nemsapp.vo.GraphLine;
import com.nemsapp.vo.StO;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.nemsapp.util.Constants.IDACC;
import static com.nemsapp.util.Constants.IDAN;
import static com.nemsapp.util.Constants.IDST;
import static com.nemsapp.util.Constants.ip;

public class GraphActivity extends AppCompatActivity {

    private OkHttpClient okHttpClient = new OkHttpClient();

    private LineChart lineChart;
    private TreeListViewAdapter sideBarAdapter;
    private List<Node> sideBarDatas = new ArrayList<>();
    private ListView sideBar;

    private Map<String, List<GraphLine>> pointMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        //设置数据
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            entries.add(new Entry(i, (float) (Math.random()) * 80));
        }
        //一个LineDataSet就是一条线


        sideBar = findViewById(R.id.list);

        initSiderBar();

    }

    private void init() {

        lineChart = findViewById(R.id.lineChart);
        //显示边界
        lineChart.setDrawBorders(true);

        List<Entry> entries = new ArrayList<>();

        LineDataSet lineDataSet = new LineDataSet(entries, "");
        LineData data = new LineData(lineDataSet);
        lineChart.setData(data);
    }

    private void initSiderBar() {

        int i = 1;

        pointMap = new HashMap<>();

        sideBarDatas.add(new Node(0, "-1", "所有单元"));

        File file = new File(Constants.folderPath + "/baseConfigs/CurveCfg.xml");
        SAXReader reader = new SAXReader();
        Document cfg;
        try {
            cfg = reader.read(file);
            Element root = cfg.getRootElement();
            String query = "//curveCfg/unit";
            List<Element> units = cfg.selectNodes(query);

            //遍历单元
            for (Element unit : units) {
                int unitno = i;
                sideBarDatas.add(new Node(i, 0, unit.attribute("name").getValue()));
                i++;
                List<Element> groups = unit.elements();
                for (Element group : groups) {
                    sideBarDatas.add(new Node(i, unitno, group.attribute("name").getValue()));
                    i++;

                    List<GraphLine> lineInfos = new ArrayList<>();
                    List<Element> lines = group.elements();
                    for (Element line : lines) {
                        lineInfos.add(new GraphLine(line.attributeValue("name"), line.attributeValue("color")));
                        pointMap.put(group.attributeValue("name"), lineInfos);
                    }
                }
            }


        } catch (DocumentException e) {
//            Log.i(TAG, "initSiderBar: cannot read" + file.getName());
            Toast.makeText(this, "配置文件读取错误", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        sideBarAdapter = new SimpleTreeAdapter(sideBar, GraphActivity.this, sideBarDatas, 0, R.mipmap.tree_ex, R.mipmap.tree_ec);
        sideBar.setAdapter(sideBarAdapter);
    }

    private void getData() {

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
                            final Gson gson = new Gson();
                        }

                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
