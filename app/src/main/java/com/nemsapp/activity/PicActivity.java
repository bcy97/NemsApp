package com.nemsapp.activity;

import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.nemsapp.R;
import com.nemsapp.components.Button;
import com.nemsapp.components.Line;
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
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import win.smartown.android.library.tableLayout.TableAdapter;
import win.smartown.android.library.tableLayout.TableLayout;

public class PicActivity extends AppCompatActivity {

    private MainUI mainUI;
    private List<Content> contentList;
    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainUI = (MainUI) findViewById(R.id.mainUI);
        ParseSVG();
        tableLayout = (TableLayout) findViewById(R.id.main_table);
        initContent();
        timer.schedule(task, 0, 1000);

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
            List<Line> lines = new ArrayList<>();
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
                    Line line = new Line(this, Float.parseFloat(element.getAttribute("android:strokeWidth")));
//                            Integer.parseInt(element.getAttribute("android:strokeColor")));
                    line.setPath(path_svg);
                    lines.add(line);
                }
            }
            mainUI.setButtons(buttons);
            mainUI.setLines(lines);
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
                System.out.println("get");
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

    public static class Content {

        private String 姓名;
        private String 语文;
        private String 数学;
        private String 英语;
        private String 物理;
        private String 化学;
        private String 生物;

        public Content(String 姓名, String 语文, String 数学, String 英语, String 物理, String 化学, String 生物) {
            this.姓名 = 姓名;
            this.语文 = 语文;
            this.数学 = 数学;
            this.英语 = 英语;
            this.物理 = 物理;
            this.化学 = 化学;
            this.生物 = 生物;
        }

        public String[] toArray() {
            return new String[]{姓名, 语文, 数学, 英语, 物理, 化学, 生物};
        }

    }

    private String newRandomNumber() {
        return (new Random().nextInt(50) + 50) + "";
    }

    private void initContent() {
        contentList = new ArrayList<>();
        contentList.add(new Content("姓名", "语文", "数学", "英语", "物理", "化学", "生物"));
        contentList.add(new Content("张三", newRandomNumber(), newRandomNumber(), newRandomNumber(), newRandomNumber(), newRandomNumber(), newRandomNumber()));
        contentList.add(new Content("李四", newRandomNumber(), newRandomNumber(), newRandomNumber(), newRandomNumber(), newRandomNumber(), newRandomNumber()));
        contentList.add(new Content("王二", newRandomNumber(), newRandomNumber(), newRandomNumber(), newRandomNumber(), newRandomNumber(), newRandomNumber()));
        contentList.add(new Content("王尼玛", newRandomNumber(), newRandomNumber(), newRandomNumber(), newRandomNumber(), newRandomNumber(), newRandomNumber()));
        contentList.add(new Content("张全蛋", newRandomNumber(), newRandomNumber(), newRandomNumber(), newRandomNumber(), newRandomNumber(), newRandomNumber()));
        contentList.add(new Content("赵铁柱", newRandomNumber(), newRandomNumber(), newRandomNumber(), newRandomNumber(), newRandomNumber(), newRandomNumber()));
    }

    //将第一行作为标题
    private void firstRowAsTitle() {
        //fields是表格中要显示的数据对应到Content类中的成员变量名，其定义顺序要与表格中显示的相同
        final String[] fields = {"姓名", "语文", "数学", "英语", "物理", "化学", "生物"};
        tableLayout.setAdapter(new TableAdapter() {
            @Override
            public int getColumnCount() {
                return fields.length;
            }

            @Override
            public String[] getColumnContent(int position) {
                int rowCount = contentList.size();
                String contents[] = new String[rowCount];
                try {
                    Field field = Content.class.getDeclaredField(fields[position]);
                    field.setAccessible(true);
                    for (int i = 0; i < rowCount; i++) {
                        contents[i] = (String) field.get(contentList.get(i));
                    }
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                return contents;
            }
        });
    }
}
