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
package org.apache.kandula.wscoor;

import java.io.IOException;

import javax.xml.namespace.QName;

import org.apache.axis2.addressing.AnyContentType;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.clientapi.MessageSender;
import org.apache.axis2.om.OMAbstractFactory;
import org.apache.axis2.om.OMElement;
import org.apache.axis2.om.OMNamespace;
import org.apache.kandula.Constants;
import org.apache.kandula.utility.KandulaListener;

/**
 * @author <a href="mailto:thilina@opensource.lk"> Thilina Gunarathne </a>
 */

public class ActivationCoordinatorPortTypeRawXMLStub extends
        org.apache.axis2.clientapi.Stub {

    public static final String AXIS2_HOME = ".";

    protected static org.apache.axis2.description.OperationDescription[] operations;

    static {

        //creating the Service
        _service = new org.apache.axis2.description.ServiceDescription(
                new javax.xml.namespace.QName(
                        "http://schemas.xmlsoap.org/ws/2003/09/wscoor",
                        "ActivationCoordinatorPortType"));

        //creating the operations
        org.apache.axis2.description.OperationDescription operationDesc;
        operations = new org.apache.axis2.description.OperationDescription[1];

        operationDesc = new org.apache.axis2.description.OperationDescription();
        operationDesc.setName(new javax.xml.namespace.QName(
                "http://schemas.xmlsoap.org/ws/2003/09/wscoor",
                "CreateCoordinationContextOperation"));
        operations[0] = operationDesc;
        _service.addOperation(operationDesc);

    }

    /**
     * Constructor
     */
    public ActivationCoordinatorPortTypeRawXMLStub(String axis2Home,
            EndpointReference targetEndpoint) throws java.lang.Exception {
        this.toEPR = targetEndpoint;
        //creating the configuration
        _configurationContext = new org.apache.axis2.context.ConfigurationContextFactory()
                .buildClientConfigurationContext(axis2Home);

        _configurationContext.getAxisConfiguration().addService(_service);
        _serviceContext = _configurationContext.createServiceContext(_service
                .getName());

    }

    private org.apache.axis2.soap.SOAPEnvelope createSOAPEnvelope(
            String coordinationType) {
        org.apache.axis2.soap.SOAPEnvelope env = super.createEnvelope();
        org.apache.axis2.soap.SOAPFactory factory = OMAbstractFactory
                .getSOAP12Factory();
        OMNamespace wsCoor = factory.createOMNamespace(Constants.WS_COOR,
                "wscoor");
        OMElement request = factory.createOMElement(
                "CreateCoordinationContext", wsCoor);
        OMElement coorType = factory
                .createOMElement("CoordinationType", wsCoor);
        coorType.setText(coordinationType);
        request.addChild(coorType);
        env.getBody().addChild(request);
        return env;
    }

    public void createCoordinationContextOperation(String coordinationType,
            String id) throws IOException {

        QName serviceName = new QName("ActivationRequesterPortType");
        QName operationName = new QName(Constants.WS_COOR,
                "CreateCoordinationContextOperation");
        KandulaListener listener = KandulaListener.getInstance();
        listener.addService(serviceName, operationName,
                ActivationRequesterPortTypeRawXMLSkeleton.class.getName());
        listener.start();

        MessageSender messageSender = new MessageSender(_serviceContext);
        org.apache.axis2.context.MessageContext messageContext = getMessageContext();
        EndpointReference replyToEpr = new EndpointReference(listener.getHost()
                + serviceName.getLocalPart());
        AnyContentType refProperties = new AnyContentType();
        refProperties.addReferenceValue(new QName(
                "http://ws.apache.org/kandula", "id"), id);
        replyToEpr.setReferenceProperties(refProperties);
        //  messageSender.
        messageSender.setReplyTo(replyToEpr);
        messageSender.setTo(this.toEPR);
        messageSender.setSoapAction("CreateCoordinationContextOperation");
        //_call.setWsaAction("CreateCoordinationContextOperation");
        org.apache.axis2.soap.SOAPEnvelope env = createSOAPEnvelope(coordinationType);
        messageContext.setEnvelope(env);
        messageSender
                .setSenderTransport(org.apache.axis2.Constants.TRANSPORT_HTTP);
        messageSender.send(operations[0], messageContext);

    }
}