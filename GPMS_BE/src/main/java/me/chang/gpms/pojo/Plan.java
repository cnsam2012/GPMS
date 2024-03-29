package me.chang.gpms.pojo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

@Data
public class Plan {

    private int id;
    private String number;
    private int type;
    private String name;
    private int major_orie_id;
    private int grade;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date start_d;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date end_d;

    private int class_hour;
    private float credit;
    private int percent_in;
    private int percent_ex;
    private boolean is_deleted;
    private String content;
    private String objective;
    private String demand;
    private int score_cal_type;
    private int percent_in_daily_report;
    private int percent_in_weekly_report;
    private int percent_in_monthly_report;
    private int percent_in_summary;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date deadline;
}
