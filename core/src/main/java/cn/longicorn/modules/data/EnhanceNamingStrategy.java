package cn.longicorn.modules.data;

public class EnhanceNamingStrategy implements INamingStrategy {

	@Override
	public String columnToProperty(String columnName) {
		StringBuilder property = new StringBuilder();
		char[] cs = columnName.toCharArray();
		for (int i = 0; i < cs.length; i++) {
			if (cs[i] == '_') {
				if ((cs.length - 1) > i) {
					cs[i + 1] = Character.toUpperCase(cs[i + 1]);
				}
			} else {
				property.append(cs[i]);
			}
		}
		return property.toString();
	}

	@Override
	public String PropertyToColumn(String property) {
		StringBuilder column = new StringBuilder();
		char[] cs = property.toCharArray();
		for (char c : cs) {
			if (c != Character.toLowerCase(c)) {
				column.append("_").append(Character.toLowerCase(c));
			} else {
				column.append(c);
			}
		}
		return column.toString();
	}

}
