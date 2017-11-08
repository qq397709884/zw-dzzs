package cn.longicorn.modules.ui.tree;

import java.util.List;

public class ComplexTreeNode implements TreeNode {
	private String id;
	private boolean isParent;						//是否为父节点
	private String name;							//节点名称
	private boolean checked;						//默认值false
	private List<ComplexTreeNode> children;			//标准Json数据格式
	private boolean chkDisabled;					//单选和复选是否禁用
	private String click;							//单击
	private boolean halfCheck;						//强制节点复选和单选的半勾选状态
	private String icon;							//图标
	private String iconClose;						//关闭图标
	private String iconOpen;						//打开关闭
	private String iconSkin;						//节点自定义图标的 className
	private boolean nocheck;						//是否隐藏checkbox/radio，默认False
	private boolean open;							//图标展开折叠状态，默认False
	private String target;							//目标_blank或_self或frame名称
	private String url;								//url链接

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

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public List<ComplexTreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<ComplexTreeNode> children) {
		this.children = children;
	}

	public void setParent(boolean isParent) {
		this.isParent = isParent;
	}

	public boolean isChkDisabled() {
		return chkDisabled;
	}

	public void setChkDisabled(boolean chkDisabled) {
		this.chkDisabled = chkDisabled;
	}

	public String getClick() {
		return click;
	}

	public void setClick(String click) {
		this.click = click;
	}

	public boolean isHalfCheck() {
		return halfCheck;
	}

	public void setHalfCheck(boolean halfCheck) {
		this.halfCheck = halfCheck;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getIconClose() {
		return iconClose;
	}

	public void setIconClose(String iconClose) {
		this.iconClose = iconClose;
	}

	public String getIconOpen() {
		return iconOpen;
	}

	public void setIconOpen(String iconOpen) {
		this.iconOpen = iconOpen;
	}

	public String getIconSkin() {
		return iconSkin;
	}

	public void setIconSkin(String iconSkin) {
		this.iconSkin = iconSkin;
	}

	public boolean isNocheck() {
		return nocheck;
	}

	public void setNocheck(boolean nocheck) {
		this.nocheck = nocheck;
	}

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}
}
