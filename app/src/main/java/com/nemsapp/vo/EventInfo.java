package com.nemsapp.vo;

import java.util.List;

public class EventInfo {

    public EventInfo() {

    }

    public EventInfo(int id, String strTime, String info) {
        this.id = id;
        this.strTime = strTime;
        this.info = info;
    }

    private int id;
    private String strTime;
    private String info;
    private List<EventLog> eventLogs;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStrTime() {
        return strTime;
    }

    public void setStrTime(String strTime) {
        this.strTime = strTime;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setEventLogs(List<EventLog> eventLogs) {
        this.eventLogs = eventLogs;
    }

    public List<EventLog> getEventLogs() {
        return eventLogs;
    }

    public String toEventLogString() {
        if (null == eventLogs || eventLogs.size() == 0)
            return "";
        StringBuffer sb = new StringBuffer();
        for (EventLog log : eventLogs)
            sb.append(log.getName() + ":" + log.getData() + ",");
        if (sb.length() > 0)
            sb.substring(0, sb.length() - 1);
        return sb.toString();
    }
}
