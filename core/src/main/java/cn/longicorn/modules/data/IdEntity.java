package cn.longicorn.modules.data;

import java.io.Serializable;

public abstract class IdEntity implements Serializable {

	private static final long serialVersionUID = 855914657670040126L;
	
	protected Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
