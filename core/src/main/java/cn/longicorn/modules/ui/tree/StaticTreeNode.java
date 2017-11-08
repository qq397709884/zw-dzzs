package cn.longicorn.modules.ui.tree;

public class StaticTreeNode extends SimpleTreeNode {

	private String pId;
	private boolean open;

	public String getpId() {
		return pId;
	}

	public void setpId(String pId) {
		this.pId = pId;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}

}