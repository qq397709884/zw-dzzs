package cn.longicorn.modules.ui.tree;

public class SimpleTreeNode implements TreeNode {
	private String id;
	private boolean isParent;			//是否为父节点
	private String name;				//节点名称
	private String target;				//目标_blank或_self或frame名称
	private String url;					//url链接

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean getIsParent() {
		return isParent;
	}

	public void setIsParent(boolean isParent) {
		this.isParent = isParent;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}