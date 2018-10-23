package com.nemsapp.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nemsapp.R;
import com.nemsapp.adapter.SimpleTreeAdapter;
import com.nemsapp.treelist.Node;
import com.nemsapp.treelist.TreeListViewAdapter;
import com.nemsapp.util.Constants;
import com.nemsapp.vo.GraphLine;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.IOException;
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

import static com.nemsapp.util.Constants.ip;

public class GraphActivity extends AppCompatActivity implements View.OnClickListener {

    private OkHttpClient okHttpClient = new OkHttpClient();

    private LineChart lineChart;
    private TreeListViewAdapter sideBarAdapter;
    private List<Node> sideBarDatas = new ArrayList<>();
    private ListView sideBar;

    private Map<String, List<GraphLine>> pointMap;
    private Map<String, GraphLine> allPoints;

    private TimePickerView pvTime;
    private Button time;
    private Date chooseTime;
    private Button search;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        sideBar = findViewById(R.id.list);

        initSiderBar();

        time = findViewById(R.id.chooseTime);
        time.setOnClickListener(this);
        search = findViewById(R.id.Search);
        search.setOnClickListener(this);

        initCustomTimePicker();

        //设置数据
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            entries.add(new Entry(i, (float) (Math.random()) * 80));
        }
        //一个LineDataSet就是一条线


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
        allPoints = new HashMap<>();

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
                        GraphLine graphLine = new GraphLine(line.attributeValue("name"), line.attributeValue("color"));
                        lineInfos.add(graphLine);
                        allPoints.put(line.attributeValue("name"), graphLine);
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

        String url = "http://" + ip + ":8080/line/getHistoryLineData";

        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");

        Map<String, String> data = new HashMap<>();
        data.put("time", getTime(chooseTime));
//        data.put("unitname", unitname);
        Gson gson = new Gson();

        final List<String> pointNames = new ArrayList<>();

        for (Node node : sideBarDatas) {
            if (node.isLeaf() && node.isChecked()) {
                for (GraphLine graph : pointMap.get(node.getName())) {
                    pointNames.add(graph.getName());
                }
            }
        }
        if (pointNames.size() <= 0) {
            for (GraphLine graph : pointMap.get(0)) {
                pointNames.add(graph.getName());
            }
        }

        data.put("pointName", gson.toJson(pointNames));

        RequestBody requestBody = RequestBody.create(mediaType, gson.toJson(data));

        // 创建request
        final Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        //发起请求
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
                            Map<String, Float[]> data = gson.fromJson(strdata, new TypeToken<HashMap<String, Float[]>>() {
                            }.getType());
                            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
                            for (String pointName : pointNames) {
                                ArrayList<Entry> yVals = new ArrayList<>();
                                for (int i = 0; i < data.get(pointName).length; i++) {
                                    yVals.add(new Entry(data.get(pointName)[i], i));
                                }
                                LineDataSet lineData = new LineDataSet(yVals, pointName);
                                lineData.setCubicIntensity(0.2f);
                                lineData.setDrawFilled(true);  //设置包括的范围区域填充颜色
                                lineData.setDrawCircles(true);  //设置有圆点
                                lineData.setLineWidth(2f);    //设置线的宽度
                                lineData.setCircleSize(5f);   //设置小圆的大小
                                int color = Color.parseColor(allPoints.get(pointName).getColor());
                                lineData.setHighLightColor(color);
                                lineData.setColor(color);    //设置曲线的颜色
                                lineData.setFillColor(color);
                                dataSets.add(lineData);

                            }
                            lineChart.setData(new LineData(dataSets));
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.chooseTime && pvTime != null) {
            // pvTime.setDate(Calendar.getInstance());
            /* pvTime.show(); //show timePicker*/
            pvTime.show(v);//弹出时间选择器，传递参数过去，回调的时候则可以绑定此view
        } else if (v.getId() == R.id.Search) {
            if (chooseTime == null) {
                Toast.makeText(this, "请选择开始时间", Toast.LENGTH_SHORT);
                return;
            } else {
                getData();
            }
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
                if (v.getId() == R.id.chooseTime && pvTime != null) {
                    time.setText("时间：" + getTime(date));
                    chooseTime = date;
                }
                Toast.makeText(GraphActivity.this, getTime(date), Toast.LENGTH_SHORT).show();
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
                .setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("年", "月", "日", "时", "分", "秒")
                .setLineSpacingMultiplier(1.2f)
                .setTextXOffset(0, 0, 0, 40, 0, -40)
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setDividerColor(0xFF24AD9D)
                .build();

    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }
}
