/**********************************************************************
Copyright (c) 2010 Marcel Lanz. All rights reserved.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Contributors :
    ...
***********************************************************************/
package org.datanucleus.store.mongodb;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.datanucleus.ClassLoaderResolver;
import org.datanucleus.OMFContext;
import org.datanucleus.ObjectManager;
import org.datanucleus.PersistenceConfiguration;
import org.datanucleus.metadata.MetaDataListener;
import org.datanucleus.store.AbstractStoreManager;
import org.datanucleus.store.NucleusConnection;

public class MongoDBStoreManager extends AbstractStoreManager
{
    MetaDataListener metadataListener;

    //private HBaseConfiguration hbaseConfig;
    
    private boolean autoCreateTables = true;
    private boolean autoCreateColumns = true;

    private int poolTimeBetweenEvictionRunsMillis; 
    private int poolMinEvictableIdleTimeMillis;
    
    /**
     * Constructor.
     * @param clr ClassLoader resolver
     * @param omfContext ObjectManagerFactory context
     */
    public MongoDBStoreManager(ClassLoaderResolver clr, OMFContext omfContext)
    {
        super("mongodb", clr, omfContext);
                
        // Handler for metadata
        metadataListener = new MongoDBMetaDataListener(this);
        omfContext.getMetaDataManager().registerListener(metadataListener);

        // Handler for persistence process
        persistenceHandler = new MongoDBPersistenceHandler(this);

        PersistenceConfiguration conf = omfContext.getPersistenceConfiguration();
       
        // how often should the evictor run
        poolTimeBetweenEvictionRunsMillis = conf.getIntProperty("datanucleus.connectionPool.timeBetweenEvictionRunsMillis");
        if (poolTimeBetweenEvictionRunsMillis == 0)
        {
            poolTimeBetweenEvictionRunsMillis = 15 * 1000; // default, 15 secs
        }
         
        // how long may a connection sit idle in the pool before it may be evicted
        poolMinEvictableIdleTimeMillis = conf.getIntProperty("datanucleus.connectionPool.minEvictableIdleTimeMillis");
        if (poolMinEvictableIdleTimeMillis == 0)
        {
            poolMinEvictableIdleTimeMillis = 30 * 1000; // default, 30 secs
        }
                
        logConfiguration();
    }

    protected void registerConnectionMgr()
    {
        super.registerConnectionMgr();
        this.connectionMgr.disableConnectionPool();
    }


    /**
     * Release of resources
     */
    public void close()
    {
        omfContext.getMetaDataManager().deregisterListener(metadataListener);
        super.close();
    }

    public NucleusConnection getNucleusConnection(ObjectManager om)
    {
        throw new UnsupportedOperationException();
    }

    /**
     * Accessor for the supported options in string form
     */
    public Collection getSupportedOptions()
    {
        Set set = new HashSet();
        set.add("ApplicationIdentity");
        set.add("TransactionIsolationLevel.read-committed");
        return set;
    }
    
    public int getPoolMinEvictableIdleTimeMillis()
    {
        return poolMinEvictableIdleTimeMillis;
    }
    
    public int getPoolTimeBetweenEvictionRunsMillis()
    {
        return poolTimeBetweenEvictionRunsMillis;
    }
}
