package com.ruike.itf.utils;

import java.util.Comparator;

/**
 * @ClassName MapKeyComparator
 * @Description TODO
 * @Author wenzhang.yu@hand-china.com
 * @Date 2021/3/3 17:05
 * @Version 1.0
 **/
public class MapKeyComparator implements Comparator<String> {

    @Override
    public int compare(String str1, String str2) {

        return str1.compareTo(str2);
    }
}