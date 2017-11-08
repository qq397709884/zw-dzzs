package cn.longicorn.modules.regex;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 电话号码所属运营商识别
 * TODO 采用默认正则表达式加外部配置的方式提供灵活的运营商号段扩展能力
 * 暂时不采用配置的原因是没有中心化的配置分发平台
 */
public final class PhoneParser {

    public enum TeleProvider {

        CMCC("CMCC", "中国移动", "1(((3[4-9]|5[012789]|47|78|8[23478])\\d{8})|(70[356]\\d{7}))"),
        UNICOM("UNICOM", "中国联通", "1(((3[0-2]|5[56]|45|76|8[56])\\d{8})|(70[9874]\\d{7}))"),
        TELECOM("TELECOM", "中国电信", "1(((33|53|7[37]|8[019])\\d{8})|(70[012]\\d{7}))");

        TeleProvider(String code, String displayName, String phoneRegex) {
            this.code = code;
            this.displayName = displayName;
            this.phoneRegex = phoneRegex;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getCode() {
            return code;
        }

        public String getPhoneRegex() {
            return phoneRegex;
        }

        private String displayName;
        private String code;
        private String phoneRegex;

    }

    //存放不同运营商电话号码规则的正则表达式，key 运营商名字 value 匹配的正则表达式
    private Map<String, String> regexMap = new HashMap<String, String>();

    private PhoneParser() {
        initRegexMapDefault();
    }

    public PhoneParser(Map<String, String> regexMap) {
        setRegexList(regexMap);
    }

    public void setRegexList(Map<String, String> regexMap) {
        this.regexMap = regexMap;
    }

    public TeleProvider parseMobile(String phone) {
        if (phone == null)
            return null;
        Set<String> key = regexMap.keySet();
        for (String s : key) {
            if (phone.matches(regexMap.get(s))) {
                return TeleProvider.valueOf(s);
            }
        }
        return null;
    }

    public boolean isValid(String phone) {
        return parseMobile(phone) != null;
    }

    public boolean isNotValid(String phone) {
        return parseMobile(phone) == null;
    }

    public Map<String, Integer> parseMobiles(String[] phones) {
        Integer cmccCount = 0, unicomCount = 0, telecomCount = 0, unknowCount = 0;
        for (String phone : phones) {
            TeleProvider a = parseMobile(phone);
            if (a == null) {
                unknowCount++;
                continue;
            }
            switch (a) {
                case CMCC:
                    cmccCount++;
                    break;
                case UNICOM:
                    unicomCount++;
                    break;
                case TELECOM:
                    telecomCount++;
                    break;
            }
        }

        Map<String, Integer> retMap = new HashMap<String, Integer>();
        retMap.put(TeleProvider.CMCC.getCode(), cmccCount);
        retMap.put(TeleProvider.UNICOM.getCode(), unicomCount);
        retMap.put(TeleProvider.TELECOM.getCode(), telecomCount);
        retMap.put("UNKNOW", unknowCount);
        return retMap;
    }

    public String getDisplayName(String code) {
        TeleProvider a = null;
        try {
            a = TeleProvider.valueOf(code);
        } catch (Exception e) {
            //do nothing
        }
        return a == null ? "" : a.getDisplayName();
    }

    private void initRegexMapDefault() {
        TeleProvider[] agents = TeleProvider.values();
        for (TeleProvider agent : agents) {
            regexMap.put(agent.getCode(), agent.getPhoneRegex());
        }
    }

    private static PhoneParser parser;

    public synchronized static PhoneParser getInstance() {
        if (parser == null) {
            parser = new PhoneParser();
        }
        return parser;
    }

}