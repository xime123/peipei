package com.tshang.peipei.view.fireview;

import java.util.HashMap;

public class ObserveFireView {

    private static ObserveFireView observe;

    private HashMap<String, FireItem> map = new HashMap<String, FireItem>();

    public HashMap<String, FireItem> getMap() {
	return map;
    }

    private ObserveFireView() {
    };

    public static ObserveFireView getInstance() {
	if (null == observe) {
	    observe = new ObserveFireView();
	}
	return observe;
    }

    public void clear() {
	map.clear();
    }

}
