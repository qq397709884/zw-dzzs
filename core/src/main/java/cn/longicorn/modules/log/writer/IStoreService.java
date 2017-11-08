package cn.longicorn.modules.log.writer;

import java.io.Serializable;

public interface IStoreService {

	public String getEntityClassName();

	public void save(Serializable[] contents);

	public void save(Serializable content);

}
