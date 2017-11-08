package cn.longicorn.modules.ui.tree;

public interface TreeNode {
	String getId();

	void setId(String id);

	String getName();

	void setName(String name);

	boolean getIsParent();

	void setIsParent(boolean isParent);

	String getTarget();

	void setTarget(String target);

	String getUrl();

	void setUrl(String url);
}