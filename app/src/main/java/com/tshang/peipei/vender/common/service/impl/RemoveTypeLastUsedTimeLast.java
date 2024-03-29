package com.tshang.peipei.vender.common.service.impl;

import com.tshang.peipei.vender.common.entity.CacheObject;
import com.tshang.peipei.vender.common.service.CacheFullRemoveType;

/**
 * Remove type when cache is full.<br/>
 * when cache is full, compare last used time of object in cache, if time is bigger remove it first.<br/>
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2011-12-26
 */
public class RemoveTypeLastUsedTimeLast<T> implements CacheFullRemoveType<T> {

    private static final long serialVersionUID = 1L;

    @Override
    public int compare(CacheObject<T> obj1, CacheObject<T> obj2) {
        return (obj2.getLastUsedTime() > obj1.getLastUsedTime()) ? 1 : ((obj2.getLastUsedTime() == obj1
                .getLastUsedTime()) ? 0 : -1);
    }
}
