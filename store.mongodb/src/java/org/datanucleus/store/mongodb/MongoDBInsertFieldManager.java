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



import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.datanucleus.metadata.AbstractClassMetaData;
import org.datanucleus.metadata.AbstractMemberMetaData;
import org.datanucleus.store.fieldmanager.AbstractFieldManager;

public class MongoDBInsertFieldManager extends AbstractFieldManager
{
    private DBObject dbObject;
    private AbstractClassMetaData acmd;

    public MongoDBInsertFieldManager(DBObject dbObject, AbstractClassMetaData acmd) {
        super();
        this.dbObject = dbObject;
        this.acmd = acmd;
    }

    @Override
    public void storeBooleanField(int fieldNumber, boolean value) {
        super.storeBooleanField(fieldNumber, value);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public boolean fetchBooleanField(int fieldNumber) {
        return super.fetchBooleanField(fieldNumber);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void storeCharField(int fieldNumber, char value) {
        super.storeCharField(fieldNumber, value);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public char fetchCharField(int fieldNumber) {
        return super.fetchCharField(fieldNumber);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void storeByteField(int fieldNumber, byte value) {
        super.storeByteField(fieldNumber, value);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public byte fetchByteField(int fieldNumber) {
        return super.fetchByteField(fieldNumber);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void storeShortField(int fieldNumber, short value) {
        super.storeShortField(fieldNumber, value);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public short fetchShortField(int fieldNumber) {
        return super.fetchShortField(fieldNumber);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void storeIntField(int fieldNumber, int value) {
        super.storeIntField(fieldNumber, value);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public int fetchIntField(int fieldNumber) {
        return super.fetchIntField(fieldNumber);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void storeLongField(int fieldNumber, long value) {
        super.storeLongField(fieldNumber, value);
    }

    @Override
    public long fetchLongField(int fieldNumber) {
        return super.fetchLongField(fieldNumber);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void storeFloatField(int fieldNumber, float value) {
        super.storeFloatField(fieldNumber, value);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public float fetchFloatField(int fieldNumber) {
        return super.fetchFloatField(fieldNumber);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void storeDoubleField(int fieldNumber, double value) {
        super.storeDoubleField(fieldNumber, value);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public double fetchDoubleField(int fieldNumber) {
        return super.fetchDoubleField(fieldNumber);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void storeStringField(int fieldNumber, String value) {
        AbstractMemberMetaData ammd = acmd.getMetaDataForManagedMemberAtAbsolutePosition(fieldNumber);
        dbObject.put(ammd.getFullFieldName(), value);

    }

    @Override
    public String fetchStringField(int fieldNumber) {
        AbstractMemberMetaData ammd = acmd.getMetaDataForManagedMemberAtAbsolutePosition(fieldNumber);
        if(ammd != null) {
            String value = (String) dbObject.get(ammd.getFullFieldName());
            return value;
        }
        return null;
    }

    @Override
    public void storeObjectField(int fieldNumber, Object value) {
        super.storeObjectField(fieldNumber, value);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public Object fetchObjectField(int fieldNumber) {
        return super.fetchObjectField(fieldNumber);    //To change body of overridden methods use File | Settings | File Templates.
    }
}
