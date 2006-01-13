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
package org.apache.kandula.coordinator;

import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;

import org.apache.axis.AxisFault;
import org.apache.axis.message.addressing.EndpointReference;
import org.apache.axis.types.URI;
import org.apache.axis.types.URI.MalformedURIException;
import org.apache.kandula.utils.AddressingHeaders;
import org.apache.kandula.utils.Service;
import org.apache.kandula.utils.TCPSnifferHelper;
import org.apache.kandula.wscoor.ActivationPortTypeRPCBindingStub;
import org.apache.kandula.wscoor.CreateCoordinationContextResponseType;
import org.apache.kandula.wscoor.CreateCoordinationContextType;

/**
 * @author Dasarath Weeratunge
 * 
 * Created on Jun 14, 2004
 */
public class ActivationStub extends ActivationPortTypeRPCBindingStub {

	public ActivationStub(EndpointReference epr) throws AxisFault,
			MalformedURLException {
		super(new URL(TCPSnifferHelper.redirect(epr.getAddress().toString())),
				new Service());
		AddressingHeaders headers = new AddressingHeaders(epr, null);
		((Service) service).setAddressingHeaders(headers);
	}

	public CoordinationContext createCoordinationContext(String coordinationType)
			throws RemoteException, MalformedURIException {
		CreateCoordinationContextType params = new CreateCoordinationContextType();
		params.setCoordinationType(new URI(coordinationType));
		CreateCoordinationContextResponseType response = createCoordinationContextOperation(params);
		return new CoordinationContext(response.getCoordinationContext());
	}
}