package cn.longicorn.modules.data;

import org.apache.commons.lang3.StringUtils;

public class AntiSQLInjection {

    public static String process(String input) {
        String refuse = "'|and|or|exec|execute|insert|select|delete|update|count|drop|truncate|drop|truncate";
        String[] refuseArray = refuse.split("\\|");
        for (String aRefuseArray : refuseArray) {
            if (StringUtils.indexOfIgnoreCase(input, aRefuseArray) >= 0) {
                return input.substring(0, input.length() - input.indexOf(aRefuseArray) - aRefuseArray.length());
            }
        }
        return input;
    }

}
