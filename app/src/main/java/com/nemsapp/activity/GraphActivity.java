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

import java.util.ArrayList;
import java.util.List;

public class GraphActivity extends AppCompatActivity {

    private LineChart lineChart;
    private TreeListViewAdapter mAdapter;
    private List<Node> mDatas = new ArrayList<Node>();


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


        ListView mTree = findViewById(R.id.list);

        initDatas();

        mAdapter = new SimpleTreeAdapter(mTree, GraphActivity.this,
                mDatas, 0, R.mipmap.tree_ex, R.mipmap.tree_ec);
        mTree.setAdapter(mAdapter);

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
        mDatas.add(new Node("1", "-1", "文件管理系统"));

        mDatas.add(new Node(2 + "", 1 + "", "游戏"));
        mDatas.add(new Node(3 + "", 1 + "", "文档"));
        mDatas.add(new Node(4 + "", 1 + "", "程序"));
        mDatas.add(new Node(5 + "", 2 + "", "war3"));
        mDatas.add(new Node(6 + "", 2 + "", "刀塔传奇"));

        mDatas.add(new Node(7 + "", 4 + "", "面向对象"));
        mDatas.add(new Node(8 + "", 4 + "", "非面向对象"));

        mDatas.add(new Node(9 + "", 7 + "", "C++"));
        mDatas.add(new Node(10 + "", 7 + "", "JAVA"));
        mDatas.add(new Node(11 + "", 7 + "", "Javascript"));
        mDatas.add(new Node(12 + "", 8 + "", "C"));
        mDatas.add(new Node(13 + "", 12 + "", "C"));
        mDatas.add(new Node(14 + "", 13 + "", "C"));
        mDatas.add(new Node(15 + "", 14 + "", "C"));
        mDatas.add(new Node(16 + "", 15 + "", "C"));
    }
}
