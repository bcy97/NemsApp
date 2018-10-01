package com.nemsapp.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.nemsapp.R;
import com.nemsapp.adapter.SimpleTreeAdapter;
import com.nemsapp.treelist.Node;
import com.nemsapp.treelist.TreeListViewAdapter;
import com.nemsapp.util.CfgData;
import com.nemsapp.vo.AcO;
import com.nemsapp.vo.AnO;
import com.nemsapp.vo.StO;
import com.nemsapp.vo.UnitInfo;

import java.util.ArrayList;
import java.util.List;

public class GraphActivity extends AppCompatActivity {

    private LineChart lineChart;
    private TreeListViewAdapter siderBarAdapter;
    private List<Node> sideBarDatas = new ArrayList<>();
    private ListView sideBar;


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


        ListView sideBar = findViewById(R.id.list);

        initDatas();

        siderBarAdapter = new SimpleTreeAdapter(sideBar, GraphActivity.this,
                sideBarDatas, 0, R.mipmap.tree_ex, R.mipmap.tree_ec);
        sideBar.setAdapter(siderBarAdapter);

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

    private void initDatas() {
        // id , pid , label , 其他属性
        List<UnitInfo> unitList = CfgData.getInstance().getUnitList();

        int i = 1;

        sideBarDatas.add(new Node(0 + "", "-1", "所有单元"));

        for (UnitInfo unit : unitList) {
            int index = i;
            sideBarDatas.add(new Node(index + "", 0 + "", unit.getName()));
            for (AnO anO : CfgData.getInstance().getAnOByUnitNo(unit.getUnitNo())) {
                i++;
                sideBarDatas.add(new Node(i + "", index + "", anO.getCname()));
            }
            for (StO stO : CfgData.getInstance().getStOByUnitNo(unit.getUnitNo())) {
                i++;
                sideBarDatas.add(new Node(i + "", index + "", stO.getCname()));
            }
            for (AcO acO : CfgData.getInstance().getAcOByUnitNo(unit.getUnitNo())) {
                i++;
                sideBarDatas.add(new Node(i + "", index + "", acO.getCname()));
            }
            i++;
        }

        siderBarAdapter = new SimpleTreeAdapter(sideBar, GraphActivity.this, sideBarDatas, 0, R.mipmap.tree_ex, R.mipmap.tree_ec);
        sideBar.setAdapter(siderBarAdapter);
    }
}
