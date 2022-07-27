package com.dynamic.util;

import android.text.TextUtils;

import com.dynamic.model.DMCategory;
import com.dynamic.model.DMContent;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DMBaseSorting {

    public List<DMCategory<DMContent>> arraySortCategory(List<DMCategory<DMContent>> list, boolean isOrderByAsc) {
        Collections.sort(list, new Comparator<DMCategory<DMContent>>() {
            @Override
            public int compare(DMCategory item, DMCategory item2) {
                Date value = getDate(item.getCreatedAt());
                Date value2 = getDate(item2.getCreatedAt());
                return isOrderByAsc ? value.compareTo(value2) : value2.compareTo(value);
            }
        });
        Collections.sort(list, new Comparator<DMCategory<DMContent>>() {
            @Override
            public int compare(DMCategory item, DMCategory item2) {
                Integer value = item.getRanking();
                Integer value2 = item2.getRanking();
                return value.compareTo(value2);
            }
        });
        return list;
    }

    public List<DMContent> arraySortContent(List<DMContent> list) {
        Collections.sort(list, new Comparator<DMContent>() {
            @Override
            public int compare(DMContent item, DMContent item2) {
                Integer value = item.getRanking();
                Integer value2 = item2.getRanking();
                return value.compareTo(value2);
            }
        });
        return list;
    }

    public Date getDate(String inputDate) {
        try {
            if(TextUtils.isEmpty(inputDate)) return new Date();
            return getDateFormat().parse(inputDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Date();
    }

    private SimpleDateFormat simpleDateFormat;

    private SimpleDateFormat getDateFormat() {
        if(simpleDateFormat == null) simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        return simpleDateFormat;
    }
}
