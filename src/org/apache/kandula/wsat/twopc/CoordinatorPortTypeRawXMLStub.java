/*
 * Copyright  2004 The Apache Software Foundation.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.apache.kandula.wsat.twopc;

import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.description.OutOnlyOperationDescription;
import org.apache.kandula.Constants;
import org.apache.kandula.faults.AbstractKandulaException;
import org.apache.kandula.faults.KandulaGeneralException;
import org.apache.kandula.wsat.AbstractATNotifierStub;
/**
 * @author <a href="mailto:thilina@opensource.lk"> Thilina Gunarathne </a>
 */
public class CoordinatorPortTypeRawXMLStub extends AbstractATNotifierStub {
    public static final String AXIS2_HOME = ".";

    static {

        //creating the Service
        _service = new org.apache.axis2.description.ServiceDescription(
                new javax.xml.namespace.QName(Constants.WS_AT,
                        "CoordinatorPortType"));

        //creating the operations
        org.apache.axis2.description.OperationDescription operation;
        operations = new org.apache.axis2.description.OperationDescription[5];

        operation = new OutOnlyOperationDescription();
        operation.setName(new javax.xml.namespace.QName(Constants.WS_AT,
                "PreparedOperation"));
        operations[0] = operation;
        _service.addOperation(operation);

        operation = new OutOnlyOperationDescription();
        operation.setName(new javax.xml.namespace.QName(Constants.WS_AT,
                "AbortedOperation"));
        operations[1] = operation;
        _service.addOperation(operation);
        operation = new OutOnlyOperationDescription();
        operation.setName(new javax.xml.namespace.QName(Constants.WS_AT,
                "ReadOnlyOperation"));
        operations[2] = operation;
        _service.addOperation(operation);

        operation = new OutOnlyOperationDescription();
        operation.setName(new javax.xml.namespace.QName(Constants.WS_AT,
                "CommittedOperation"));
        operations[3] = operation;
        _service.addOperation(operation);
        operation = new OutOnlyOperationDescription();
        operation.setName(new javax.xml.namespace.QName(Constants.WS_AT,
                "ReplayOperation"));
        operations[4] = operation;
        _service.addOperation(operation);
    }

    /**
     * Constructor
     */
    public CoordinatorPortTypeRawXMLStub(String axis2Home,
            EndpointReference targetEndpoint) throws AbstractKandulaException {
        this.toEPR = targetEndpoint;
        try {
            //creating the configuration
            _configurationContext = new org.apache.axis2.context.ConfigurationContextFactory()
                    .buildClientConfigurationContext(axis2Home);
        _configurationContext.getAxisConfiguration().addService(_service);
        } catch (Exception e) {
            throw new KandulaGeneralException(e);
        }
        _serviceContext = _service.getParent().getServiceGroupContext(
                _configurationContext).getServiceContext(
                _service.getName().getLocalPart());
    }

    public void preparedOperation() throws AbstractKandulaException {
        // must send reply to epr
        this.notify("Prepared", Constants.WS_AT_PREPARED, 0, null);
    }

    public void abortedOperation() throws AbstractKandulaException {
        this.notify("Aborted", Constants.WS_AT_ABORTED, 1, null);
    }

    public void readOnlyOperation() throws AbstractKandulaException {
        this.notify("ReadOnly", Constants.WS_AT_READONLY, 2, null);

    }

    public void committedOperation() throws AbstractKandulaException {
        this.notify("Committed", Constants.WS_AT_COMMITTED, 3, null);

    }

    public void replayOperation() throws AbstractKandulaException {
        //must send reply to epr
        this.notify("Replay", Constants.WS_AT_REPLAY, 4, null);
    }

}