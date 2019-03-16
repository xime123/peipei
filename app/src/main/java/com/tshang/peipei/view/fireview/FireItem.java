package com.tshang.peipei.view.fireview;

import android.os.CountDownTimer;

public class FireItem {
    private String id;
    private Long time;
    private CountDownTimer counTimer;

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public Long getTime() {
	return time;
    }

    public void setTime(Long time) {
	this.time = time;
    }

    public CountDownTimer getCounTimer() {
	return counTimer;
    }

    public void setCounTimer(CountDownTimer counTimer) {
	this.counTimer = counTimer;
    }
}
