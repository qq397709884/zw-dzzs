package cn.longicorn.modules.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MailParser {

	public static boolean check(String email) {
		 String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
		 Pattern regex = Pattern.compile(check);  
		 Matcher matcher = regex.matcher(email);  
		 return matcher.matches();
	}
}