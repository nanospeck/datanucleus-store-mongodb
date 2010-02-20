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
import org.datanucleus.store.connection.AbstractManagedConnection;
import javax.transaction.xa.XAResource;

public class MongoDBManagedConnection extends AbstractManagedConnection {
    public Object getConnection() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public XAResource getXAResource() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void close() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
