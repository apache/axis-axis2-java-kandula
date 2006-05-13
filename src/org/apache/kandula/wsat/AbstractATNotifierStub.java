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
package org.apache.kandula.wsat;

import java.io.IOException;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMNamespace;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axiom.soap.SOAPFactory;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.OperationClient;
import org.apache.axis2.client.Options;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.context.ServiceContext;
import org.apache.axis2.context.ServiceGroupContext;
import org.apache.axis2.deployment.DeploymentException;
import org.apache.axis2.description.AxisOperation;
import org.apache.axis2.description.AxisService;
import org.apache.axis2.description.AxisServiceGroup;
import org.apache.kandula.Constants;
import org.apache.kandula.faults.AbstractKandulaException;
import org.apache.kandula.faults.KandulaGeneralException;

/**
 * @author <a href="mailto:thilina@opensource.lk"> Thilina Gunarathne </a>
 */
public abstract class AbstractATNotifierStub {
	protected AxisOperation[] operations;

	protected AxisService service;

	protected ConfigurationContext configurationContext;

	protected ServiceContext serviceContext;

	protected EndpointReference toEPR;

	public AbstractATNotifierStub(String axis2Home, String axis2Xml,
			AxisService service) throws AbstractKandulaException {
		// creating the configuration
		this.service = service;
		try {
			configurationContext = ConfigurationContextFactory
					.createConfigurationContextFromFileSystem(axis2Home,
							axis2Xml);
			configurationContext.getAxisConfiguration().addService(service);
		} catch (DeploymentException e) {
			throw new KandulaGeneralException(e);
		} catch (AxisFault e1) {
			throw new KandulaGeneralException(e1);
		}
		ServiceGroupContext sgc = new ServiceGroupContext(
				this.configurationContext, (AxisServiceGroup) this.service
						.getParent());
		this.serviceContext = new ServiceContext(service, sgc);
	}

	/**
	 * Provides common functionality for stubs to send notification messages
	 * 
	 * @param localName :
	 *            name of the notification message
	 * @param action :
	 *            ws-a action value for the notification message
	 * @param opIndex :
	 *            operation index in the operations array
	 * @param replyToEPR :
	 *            notification messages except termination messages should send
	 *            this
	 * @throws IOException
	 */
	protected void notify(String localName, String action, int opIndex,
			EndpointReference replyToEPR) throws AbstractKandulaException {
		MessageContext messageContext;
		try {
			Options options = new Options();
			messageContext = new MessageContext();
			final OperationClient client = operations[opIndex].createClient(
					serviceContext, options);

			SOAPFactory factory = OMAbstractFactory.getSOAP12Factory();
			SOAPEnvelope env = factory.getDefaultEnvelope();

			OMNamespace wsAT = factory.createOMNamespace(Constants.WS_AT,
					"wsat");
			OMElement request = factory.createOMElement(localName, wsAT);
			env.getBody().addChild(request);
			messageContext.setEnvelope(env);

			options.setTo(this.toEPR);
			if (replyToEPR != null) {
				options.setReplyTo(replyToEPR);
			}
			options.setAction(action);
			// options.setTranportOut(org.apache.axis2.Constants.TRANSPORT_HTTP);
			// System.out.println(operations[opIndex]);
			client.addMessageContext(messageContext);
			/*
			 * hacking till we get fire and forget corretly in Axis2
			 */
			Thread thread = new Thread(new Runnable() {
				public void run() {
					try {
						client.execute(false);
					} catch (AxisFault e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			thread.start();

		} catch (AxisFault e) {
			throw new KandulaGeneralException(e);
		}
	}
}