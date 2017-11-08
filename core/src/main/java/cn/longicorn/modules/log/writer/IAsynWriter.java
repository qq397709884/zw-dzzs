package cn.longicorn.modules.log.writer;

import java.io.Serializable;

public interface IAsynWriter {
	
	public void write(Serializable content);

	public void stop();
	
	public void setName(String name);

	public String getName();
	
}
