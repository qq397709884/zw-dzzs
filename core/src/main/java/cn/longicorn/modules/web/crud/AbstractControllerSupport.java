package cn.longicorn.modules.web.crud;

import cn.longicorn.modules.data.ISearchParser;
import cn.longicorn.modules.data.Page;
import cn.longicorn.modules.web.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * DataTables列表视图支持类
 * 因bacthDel有重大漏洞，所以取消
 * @param <T>	列表中显示对象的类型
 */
public abstract class AbstractControllerSupport<T, K> extends BaseController {

	@RequestMapping(value = "getDatas", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public DataTables<T> createDataTables(HttpServletRequest request) {
		ISearchParser<T> searchParser = new DataTablesSearchParser<T>(request, "filter_");
		Page<T> page = searchParser.parse();
		preSearchPage(page);
		page = getStandardManager().searchPage(page);
		String sEcho = request.getParameter("sEcho");
		postSearchPage(page);
		return assembleDataTables(page, StringUtils.isNotBlank(sEcho) ? Integer.parseInt(sEcho) : 0);
	}

	//该方法如果在泛型参数中将Manager的实现类型传递进来的话，可以通过反射直接实现。
	//但是这样在实现Action的时候可能会带来灵活性的减弱，因此不如采用这种简洁的形式
	protected abstract StandardManager<T, K> getStandardManager();

	/**
	 * 如果需要在后台强制增加查询条件和排序，修改页信息等，重载该方法
	 */
	protected void preSearchPage(Page<T> page) {

	}

	/**
	 * Page对象初始化完成，数据填充后进行再处理，比如对List<T>列表中的成员进行计算等
	 */
	protected void postSearchPage(Page<T> page) {

	}

	protected final DataTables<T> assembleDataTables(Page<T> page, int sEcho) {
		DataTables<T> dataTable = new DataTables<T>();
		dataTable.setAaData(page.getResult());
		dataTable.setsEcho(sEcho);
		dataTable.setiTotalRecords(page.getTotalCount());
		dataTable.setiTotalDisplayRecords(page.getTotalCount());
		return dataTable;
	}

}