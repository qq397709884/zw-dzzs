package cn.longicorn.modules.web.crud;

import java.util.List;

/**
 * Datatables Web组件的所需数据的Java描述
 * @param <T>	要呈现的数据列表中元素的类型
 */
public class DataTables<T> {

	/*
	 * An unaltered copy of sEcho sent from the client side. This parameter will change with
	 * each draw (it is basically a draw count) - so it is important that this is implemented. 
	 * Note that it strongly recommended for security reasons that you 'cast' this parameter to
	 * an integer in order to prevent Cross Site Scripting (XSS) attacks.
	 */
	private int sEcho;

	/*
	 * Total records, before filtering (i.e. the total number of records in the database)
	 */
	private long iTotalRecords;

	/*
	 * Total records, after filtering (i.e. the total number of records after filtering has 
	 * been applied - not just the number of records being returned in this result set)
	 */
	private long iTotalDisplayRecords;

	/*
	 * The data in a 2D array.
	 */
	private List<T> aaData;

	public int getsEcho() {
		return sEcho;
	}

	public void setsEcho(int sEcho) {
		this.sEcho = sEcho;
	}

	public List<T> getAaData() {
		return aaData;
	}

	public void setAaData(List<T> aaData) {
		this.aaData = aaData;
	}

	public long getiTotalRecords() {
		return iTotalRecords;
	}

	public void setiTotalRecords(long iTotalRecords) {
		this.iTotalRecords = iTotalRecords;
	}

	public long getiTotalDisplayRecords() {
		return iTotalDisplayRecords;
	}

	public void setiTotalDisplayRecords(long iTotalDisplayRecords) {
		this.iTotalDisplayRecords = iTotalDisplayRecords;
	}

}