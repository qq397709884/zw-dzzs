package cn.longicorn.modules.web;

/**
 * Web应答POJO, 允许扩展，暂时未考虑国际化支持
 */
public class SimpleResponse {

	public static String S_OK = "ok";
	public static String S_ERROR = "err";
	public static String S_FAIL = "fail";
	public static String S_TIMEOUT = "timeout";

	/**
	 * 应答状态码，必须
	 */
	private String status;

	/**
	 * 应答文本信息 
	 */
	private String msg;

	/**
	 * 当前操作的对象的ID，比如创建对象时，对象最后的ID等
	 */
	private String oid;

	public SimpleResponse() {
		this.status = S_OK;
		this.msg = "操作成功";
	}

	public SimpleResponse(String msg) {
		this.status = S_OK;
		this.msg = msg;
	}
	
	public SimpleResponse(String status, String msg) {
		this.status = status;
		this.msg = msg;
	}

	public SimpleResponse(String status, String msg, String oid) {
		this(status, msg);
		this.oid = oid;
	}
	
	public SimpleResponse(String status, String msg, Long oid) {
		this(status, msg);
		this.oid = oid.toString();
	}

	public String getStatus() {
		return status;
	}

	public String getMsg() {
		return msg;
	}

	public String getOid() {
		return oid;
	}

}
