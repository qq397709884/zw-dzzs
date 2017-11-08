package cn.longicorn.modules.data.mongo;

import com.mongodb.MongoClient;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.springframework.beans.factory.config.AbstractFactoryBean;

import java.util.ArrayList;
import java.util.List;

/**
 * A morphia Datastore factory for spring
 * @since 2.0.0.SNAPSHOT
 * Usage:
 * <bean id="datastore" class="cn.longicorn.modules.data.mongo.DatastoreFactory">
 *   	 <property name="mongoClient" ref="mongoClient"/>
 *       <property name="morphia" ref="morphia"/>
 *       <property name="dbName" value="oap"/>
 * </bean>
 */
public class DatastoreFactory extends AbstractFactoryBean<Datastore> {

    private Morphia morphia;
    private MongoClient mongoClient;
    private String dbName;
    private List<String> packages = new ArrayList<String>();

    private boolean ensureIndexes = true;
    private boolean ensureCaps = true;

    @Override
    protected Datastore createInstance() throws Exception {
        Datastore ds = morphia.createDatastore(mongoClient, dbName);
        for (String p : packages) {
            morphia.mapPackage(p);
        }
        try {
            if (ensureIndexes) {
                ds.ensureIndexes();
            }
        } catch (com.mongodb.MongoException ignore) {

        }
        if (ensureCaps) {
            ds.ensureCaps();
        }
        return ds;
    }

    @Override
    public Class<?> getObjectType() {
        return Datastore.class;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        if (mongoClient == null) {
            throw new IllegalStateException("mongoClient is not set");
        }
        if (morphia == null) {
            throw new IllegalStateException("morphia is not set");
        }
    }

    public void setMorphia(Morphia morphia) {
        this.morphia = morphia;
    }

    public void setMongoClient(MongoClient mongoClient) {
        this.mongoClient = mongoClient;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public void setEnsureIndexes(boolean ensureIndexes) {
        this.ensureIndexes = ensureIndexes;
    }

    public void setEnsureCaps(boolean ensureCaps) {
        this.ensureCaps = ensureCaps;
    }

    public void setPackages(List<String> packages) {
        this.packages = packages;
    }

}