package cn.longicorn.modules.sp;

import java.util.ArrayList;
import java.util.List;

/**
 * SP的接入帐号信息
 */
public class SpAccount {

	/* 帐号名 */
	private String accountCode;

	/* spcode的前缀  */
	private String spcodeBase;

	private Boolean isFixedExtLength = true;

	private int extLength;

	public SpAccount(String accountCode, String spcodeBase, boolean isFixedExtLength, int extLength) {
		this.accountCode = accountCode;
		this.spcodeBase = spcodeBase;
		this.isFixedExtLength = isFixedExtLength;
		this.extLength = extLength;
	}

	/**
	 * 根据帐号构建所有的SpCode，将可变的扩展位全部展开
	 */
	public List<SpCode> buildAllSpCode() {
		List<SpCode> list = new ArrayList<>();
		if (isFixedExtLength) {
			list.add(new SpCode(spcodeBase, extLength));
		} else {
			for (int i = 0; i <= extLength; i++) {
				list.add(new SpCode(spcodeBase, i));
			}
		}
		return list;
	}

	public String accountCode() {
		return accountCode;
	}

	public String spcodeBase() {
		return spcodeBase;
	}

	public boolean isFixedExtLength() {
		return isFixedExtLength;
	}

	public int extLength() {
		return extLength;
	}

}
