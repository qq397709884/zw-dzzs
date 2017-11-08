package cn.longicorn.modules.sp;

public class SpCode implements Comparable<SpCode> {

	private String code = "";
	private int codeExLen = 0;

	public SpCode(String code, int codeExLen) {
		// Delete the additional flag tagged by the third party applications
		int pos = code.indexOf('-');
		if (pos > -1) {
			this.code = code.substring(0, pos);
		} else {
			this.code = code;
		}

		this.codeExLen = codeExLen;
	}

	public String code() {
		return code;
	}

	public int exLen() {
		return codeExLen;
	}

	@Override
	public int compareTo(SpCode spcode) {
		if ((code.length() + exLen()) < (spcode.code().length() + spcode.exLen())) {
			return -1;
		}
		if ((code.length() + exLen()) > (spcode.code().length() + spcode.exLen())) {
			return 1;
		}

		int iMin = code.length() < spcode.code().length() ? code.length() : spcode.code.length();
		if (iMin == 0) {
			return 1;
		} else {
			String src = code.substring(0, iMin);
			String des = spcode.code().substring(0, iMin);
			return src.compareTo(des);
		}
	}

}
