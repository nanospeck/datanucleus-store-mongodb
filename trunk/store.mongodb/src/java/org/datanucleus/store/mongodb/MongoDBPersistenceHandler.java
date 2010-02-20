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

import com.mongodb.*;
import org.datanucleus.ObjectManager;
import org.datanucleus.StateManager;
import org.datanucleus.exceptions.NucleusDataStoreException;
import org.datanucleus.metadata.AbstractClassMetaData;
import org.datanucleus.store.*;
import org.datanucleus.util.Localiser;

import java.net.UnknownHostException;

public class MongoDBPersistenceHandler extends AbstractPersistenceHandler {
    /**
     * Localiser for messages.
     */
    protected static final Localiser LOCALISER = Localiser.getInstance(
            "org.datanucleus.store.mongodb.Localisation", MongoDBStoreManager.class.getClassLoader());

    protected final MongoDBStoreManager storeMgr;

    MongoDBPersistenceHandler(StoreManager storeMgr) {
        this.storeMgr = (MongoDBStoreManager) storeMgr;
    }

    public void close() {
        // TODO Auto-generated method stub

    }

    public void insertObject(StateManager sm) {
        storeMgr.assertReadOnlyForUpdateOfObject(sm);
        AbstractClassMetaData acmd = sm.getClassMetaData();

        Mongo mongo = null;
        try {
            mongo = new Mongo("localhost");
        } catch (UnknownHostException e) {
            throw new NucleusDataStoreException("unable to connect to mongodb", e);
        }

        DB db = mongo.getDB("DataNucleus");
        DBCollection collection = db.getCollection(acmd.getName());
        BasicDBObject dbObject = new BasicDBObject();


        MongoDBInsertFieldManager fieldManager = new MongoDBInsertFieldManager(dbObject, acmd);
        sm.provideFields(acmd.getAllMemberPositions(), fieldManager);

        collection.insert(dbObject);

        // put back the datastore id to the POJO
        ObjectId id = (ObjectId) dbObject.get("_id");
        Class<?> pkType = acmd.getMetaDataForManagedMemberAtAbsolutePosition(acmd.getPKMemberPositions()[0]).getType();

        if (pkType.equals(String.class)) {
            sm.setPostStoreNewObjectId(id.toString());
            Integer pkIdPos = acmd.getPKMemberPositions()[0];
            sm.replaceField(pkIdPos, id.toString(), true);
        } else {
            throw new IllegalStateException("Primary key for type" + sm.getClassMetaData().getName() +
                    "is of unexpected type " + pkType.getName() +
                    "must be String.class");
        }

        // StateManager objSM = sm.getObjectManager().findStateManager(sm.getObject());
        /*
        if (!storeMgr.managesClass(sm.getClassMetaData().getFullClassName())) {
            storeMgr.addClass(sm.getClassMetaData().getFullClassName(), sm.getExecutionContext().getClassLoaderResolver());
        }

        */

        /*
        // Check existence of the object since HBase doesn't enforce application identity
        try {
            locateObject(sm);
            throw new NucleusUserException(LOCALISER.msg("HBase.Insert.ObjectWithIdAlreadyExists"));
            //TODO add JVM ID of object
//            throw new NucleusUserException(LOCALISER.msg("HBase.Insert.ObjectWithIdAlreadyExists",
            //              StringUtils.toJVMIDString(sm.getObject()), sm.getInternalObjectId()));
        }
        catch (NucleusObjectNotFoundException onfe) {
            // Do nothing since object with this id doesn't exist
        }

        HBaseManagedConnection mconn = (HBaseManagedConnection) storeMgr.getConnection(sm.getExecutionContext());
        try {
            AbstractClassMetaData acmd = sm.getClassMetaData();
            HTable table = mconn.getHTable(HBaseUtils.getTableName(acmd));
            Put put = newPut(sm);
            Delete delete = newDelete(sm);
            MongoDBInsertFieldManager fm = new MongoDBInsertFieldManager(acmd, put, delete);
            sm.provideFields(acmd.getAllMemberPositions(), fm);
            table.put(put);
            table.close();
        }
        catch (IOException e) {
            throw new NucleusDataStoreException(e.getMessage(), e);
        }
        finally {
            mconn.release();
        }

        */

    }

    public void fetchObject(StateManager sm, int[] fieldNumbers) {
        AbstractClassMetaData acmd = sm.getClassMetaData();

        Mongo mongo = null;
        try {
            mongo = new Mongo("localhost");
        } catch (UnknownHostException e) {
            throw new NucleusDataStoreException("unable to connect to mongodb", e);
        }

        DB db = mongo.getDB("DataNucleus");
        DBCollection collection = db.getCollection(acmd.getName());
        Object pkValue = sm.provideField(sm.getClassMetaData().getPKMemberPositions()[0]);

        if (pkValue != null) {
            ObjectId objectId = new ObjectId((String) pkValue);
            DBObject foundObject = collection.findOne(objectId);

            if (foundObject != null) {
                MongoDBInsertFieldManager fieldManager = new MongoDBInsertFieldManager(foundObject, acmd);
                sm.replaceFields(fieldNumbers, fieldManager);
            }
        }
    }

    public Object findObject(ObjectManager om, Object id) {
        return null;
    }

    public void updateObject(StateManager sm, int[] fieldNumbers) {
        storeMgr.assertReadOnlyForUpdateOfObject(sm);
    }

    public void deleteObject(StateManager sm) {
        // Check if read-only so update not permitted
        storeMgr.assertReadOnlyForUpdateOfObject(sm);

        AbstractClassMetaData acmd = sm.getClassMetaData();

        Mongo mongo = null;
        try {
            mongo = new Mongo("localhost");
        } catch (UnknownHostException e) {
            throw new NucleusDataStoreException("unable to connect to mongodb", e);
        }

        DB db = mongo.getDB("DataNucleus");
        DBCollection collection = db.getCollection(acmd.getName());
        Object pkValue = sm.provideField(sm.getClassMetaData().getPKMemberPositions()[0]);

        if (pkValue != null) {
            ObjectId objectId = new ObjectId((String) pkValue);
            DBObject foundObject = collection.findOne(objectId);

            if (foundObject != null) {
                collection.remove(foundObject);
            }
        }
    }

    public void locateObject(StateManager sm) {

        /*
        HBaseManagedConnection mconn = (HBaseManagedConnection) storeMgr.getConnection(sm.getExecutionContext());
        try {
            AbstractClassMetaData acmd = sm.getClassMetaData();
            HTable table = mconn.getHTable(HBaseUtils.getTableName(acmd));
            if (!exists(sm, table)) {
                throw new NucleusObjectNotFoundException();
            }
            table.close();
        }
        catch (IOException e) {
            throw new NucleusDataStoreException(e.getMessage(), e);
        }
        finally {
            mconn.release();
        }

        */
    }
}