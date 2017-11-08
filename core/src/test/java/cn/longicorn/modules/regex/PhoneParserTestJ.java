package cn.longicorn.modules.regex;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PhoneParserTestJ {

    @Test
    public void testParser() {
        assertEquals(PhoneParser.TeleProvider.TELECOM, PhoneParser.getInstance().parseMobile("17700123456"));
        assertEquals(PhoneParser.TeleProvider.TELECOM, PhoneParser.getInstance().parseMobile("17790123456"));
        assertEquals(PhoneParser.TeleProvider.TELECOM, PhoneParser.getInstance().parseMobile("17300123456"));
        assertEquals(PhoneParser.TeleProvider.TELECOM, PhoneParser.getInstance().parseMobile("17390123456"));
        assertEquals(PhoneParser.TeleProvider.TELECOM, PhoneParser.getInstance().parseMobile("17000123456"));
        assertEquals(PhoneParser.TeleProvider.TELECOM, PhoneParser.getInstance().parseMobile("17010123456"));
        assertEquals(PhoneParser.TeleProvider.TELECOM, PhoneParser.getInstance().parseMobile("17020123456"));
        assertEquals(null, PhoneParser.getInstance().parseMobile("17030123456"));
        assertEquals(PhoneParser.TeleProvider.CMCC, PhoneParser.getInstance().parseMobile("17050123456"));
    }

}
