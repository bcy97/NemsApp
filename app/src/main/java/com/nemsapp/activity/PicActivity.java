package com.nemsapp.activity;

import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.nemsapp.R;
import com.nemsapp.components.Button;
import com.nemsapp.components.OldLine;
import com.nemsapp.components.Switch;
import com.nemsapp.ui.MainUI;
import com.nemsapp.util.PathParser;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


public class PicActivity extends AppCompatActivity {

    private MainUI mainUI;

    private DrawerLayout drawerLayout;

    private ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainUI = findViewById(R.id.mainUI);
        listView = findViewById(R.id.v4_listview);
        drawerLayout = findViewById(R.id.pic_layout);

        ParseSVG();

        initDate();
//        timer.schedule(task, 0, 1000);
    }

    final Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    update();
                    break;
            }
            super.handleMessage(msg);
        }

        void update() {
            //刷新msg的内容
            List<Button> buttons = mainUI.getButtons();
            for (Button button : buttons) {
                getButtonState(button);
            }
            mainUI.invalidate();
        }
    };
    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
        public void run() {
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }
    };

    private void ParseSVG() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            List<Button> buttons = new ArrayList<>();
            List<OldLine> lines = new ArrayList<>();
            List<Switch> switches = new ArrayList<>();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputStream is = getResources().openRawResource(R.raw.swch);
            Document document = builder.parse(is);
            NodeList svgNodeList = document.getElementsByTagName("path");
            for (int i = 0; i < svgNodeList.getLength(); i++) {
                Element element = (Element) svgNodeList.item(i);
                String path = element.getAttribute("android:pathData");
                Path path_svg = PathParser.createPathFromPathData(path);
                String name = element.getAttribute("android:name");
                if (name.startsWith("button")) {
                    Button button = new Button(this, Float.parseFloat(element.getAttribute("android:strokeWidth")));
                    button.setPath(path_svg);
                    RectF rectF = new RectF();
                    path_svg.computeBounds(rectF, true);
                    Region region = new Region();
                    region.setPath(path_svg, new Region((int) (rectF.left), (int) (rectF.top), (int) (rectF.right), (int) (rectF.bottom)));
                    button.setRegion(region);
                    button.setName(name);
                    button.setId(element.getAttribute("android:id"));
                    getButtonState(button);
                    buttons.add(button);
                } else if (name.startsWith("switch")) {
                    Switch swch = new Switch(this, Float.parseFloat(element.getAttribute("android:strokeWidth")));
                    String pathData = element.getAttribute("android:pathData");
                    int index = pathData.indexOf(' ');
                    String location = pathData.substring(0, index);
                    swch.setLocation(location);
                    swch.setName(name);
                    switches.add(swch);
                } else {
                    OldLine line = new OldLine(this, Float.parseFloat(element.getAttribute("android:strokeWidth")));
//                            Integer.parseInt(element.getAttribute("android:strokeColor")));
                    line.setPath(path_svg);
                    lines.add(line);
                }
            }
            mainUI.setButtons(buttons);
            mainUI.setOldLines(lines);
            mainUI.setSwitches(switches);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getButtonState(final Button button) {
        //开启线程来发起网络请求
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                BufferedReader reader = null;
                try {
                    URL url = new URL("http://10.0.2.2:8080/data/getStData?name=" + button.getId());
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    //下面对获取到的输入流进行读取
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
//                    String line;
//                    while ((line = reader.readLine()) != null) {
//                        response.append(line);
//                    }
                    int state = Integer.parseInt(reader.readLine());
                    button.setOn(state == 1 ? true : false);
                    System.out.println(response.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        if (timer != null) {// 停止timer
            timer.cancel();
            timer = null;
        }
        super.onDestroy();
    }

    private void initDate() {
        final List<String> list = new ArrayList<>();
        list.add("系统图");
        list.add("单元1");
        list.add("单元2");
        list.add("单元3");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(PicActivity.this, "clicked", Toast.LENGTH_SHORT);
            }
        });
//        drawerLayout.openDrawer(Gravity.LEFT);//侧滑打开  不设置则不会默认打开
    }


}
