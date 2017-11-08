package cn.longicorn.modules.data.mongo;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.DatastoreImpl;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.UpdateOperations;
import java.util.List;

/**
 * @since 2014-8-5
 * @author liukehua
 * @param <T>   Entity Type
 * @param <K>   Id Type
 */
public abstract class AbstractDAO<T, K> extends BasicDAO<T, K> {
    public AbstractDAO(Datastore ds) {
        super(ds);
    }

    public AbstractDAO(DatastoreImpl ds) {
        super(ds);
    }

    public AbstractDAO(MongoClient mongoClient, Morphia morphia, String dbName) {
        super(mongoClient, morphia, dbName);
    }

    public void setValue(UpdateOperations<T> opts, String field, Object value) {
        if (value != null) {
            opts.set(field, value);
        } else {
            opts.unset(field);
        }
    }

    public void setList(UpdateOperations<T> opts, String field, List<?> value) {
        if (value != null && !value.isEmpty()) {
            opts.set(field, value);
        } else {
            opts.unset(field);
        }
    }

    public void insert(T entity) {
        this.getDs().insert(entity);
    }

    public void setArray(UpdateOperations<T> opts, String field, Object[] value) {
        if (value != null && value.length > 0) {
            opts.set(field, value);
        } else {
            opts.unset(field);
        }
    }

/*
    public UpdateResults<T> update(T t) {
        UpdateOperations<T> options = this.getDatastore().createUpdateOperations(ReflectionUtils.getSuperClassGenricType(t.getClass(), 0));
        MappedClass mc = this.getDs().getMapper().getMappedClass(t);
        List<MappedField> mfs = mc.getPersistenceFields();
        String key = null;
        Object value = null;
        for(MappedField mf : mfs) {
            if (!mf.hasAnnotation(Id.class)) {
                if (mf.getFieldValue(t) != null) {
                    options.set(mf.getField().getName(), mf.getFieldValue(t));
                } else {
                    //如果
                    options.unset(mf.getField().getName());
                }
            } else {
                key = mf.getField().getName();
                value = mf.getFieldValue(t);
            }
        }

        if (key == null || value == null) throw new UpdateException("主键不能为空");
        Query<T> q = this.createQuery().filter(key, value);

        return this.updateFirst(q, options);

    }
*/

}