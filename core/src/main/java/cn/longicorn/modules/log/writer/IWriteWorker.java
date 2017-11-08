package cn.longicorn.modules.log.writer;

public interface IWriteWorker extends Runnable {

	public void write();

	public void bindBundle(RecordBundle bundle);

}
