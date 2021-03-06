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
package org.apache.kandula.wsat.completion;

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.description.Parameter;
import org.apache.kandula.Status;
import org.apache.kandula.initiator.CompletionCallback;

/**
 * @author <a href="mailto:thilina@opensource.lk"> Thilina Gunarathne </a>
 */

public class CompletionInitiatorPortTypeRawXMLSkeleton {

	public void committedOperation(OMElement requestElement) throws AxisFault {
		Parameter parameter = MessageContext.getCurrentMessageContext()
				.getAxisService().getParameter(
						CompletionInitiatorServiceListener.COMPLETION_CALLBACK);
		if (parameter != null) {
			CompletionCallback callback = (CompletionCallback) parameter
					.getValue();
			callback.setStatus(Status.CoordinatorStatus.STATUS_COMMITTING);
			callback.setComplete(true);
		} else {
			// TODO let's do something
		}
	}

	public void abortedOperation(OMElement requestElement) throws AxisFault {
		Parameter parameter = MessageContext.getCurrentMessageContext()
				.getAxisService().getParameter(
						CompletionInitiatorServiceListener.COMPLETION_CALLBACK);
		if (parameter != null) {
			CompletionCallback callback = (CompletionCallback) parameter
					.getValue();
			callback.setStatus(Status.CoordinatorStatus.STATUS_ABORTING);
			callback.setComplete(true);
		} else {
			// TODO let's do something
		}
	}
}