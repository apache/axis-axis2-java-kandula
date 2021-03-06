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

import org.apache.axiom.om.OMElement;
import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.kandula.Constants;
import org.apache.kandula.context.impl.ATActivityContext;
import org.apache.kandula.coordinator.at.ATCoordinator;
import org.apache.kandula.faults.AbstractKandulaException;
import org.apache.kandula.participant.at.Vote;
import org.apache.kandula.storage.StorageUtils;

/**
 * @author <a href="mailto:thilina@opensource.lk"> Thilina Gunarathne </a>
 */
public class CoordinatorPortTypeRawXMLSkeleton {
	/**
	 * @param requestElement
	 * @throws AbstractKandulaException
	 */
	public void preparedOperation(OMElement requestElement)
			throws AxisFault {
		OMElement header = MessageContext.getCurrentMessageContext().getEnvelope().getHeader();
		String activityId = header.getFirstChildWithName(
				Constants.TRANSACTION_ID_PARAMETER).getText();
		String enlistmentId = header.getFirstChildWithName(
				Constants.ENLISTMENT_ID_PARAMETER).getText();
		ATCoordinator coordinator = new ATCoordinator();
		try {
			ATActivityContext atContext = (ATActivityContext) StorageUtils.getContext(activityId);
			coordinator.countVote(atContext, Vote.PREPARED, enlistmentId);
		} catch (AbstractKandulaException e) {
			e.printStackTrace();
			AxisFault fault = AxisFault.makeFault(e);
			fault.setFaultCode(e.getFaultCode());
			throw fault;
		}
	}

	/**
	 * @param requestElement
	 * @throws AbstractKandulaException
	 */
	public void abortedOperation(OMElement requestElement)
			throws AxisFault {
		OMElement header = MessageContext.getCurrentMessageContext().getEnvelope().getHeader();
		String activityId = header.getFirstChildWithName(
				Constants.TRANSACTION_ID_PARAMETER).getText();
		String enlistmentId = header.getFirstChildWithName(
				Constants.ENLISTMENT_ID_PARAMETER).getText();
		ATCoordinator coordinator = new ATCoordinator();
		try {
			ATActivityContext atContext = (ATActivityContext) StorageUtils.getContext(activityId);
			coordinator.abortedOperation(atContext, enlistmentId);
		} catch (AbstractKandulaException e) {
			AxisFault fault = AxisFault.makeFault(e);
			fault.setFaultCode(e.getFaultCode());
			throw fault;
		}
	}

	/**
	 * @param requestElement
	 * @throws AbstractKandulaException
	 */
	public void readOnlyOperation(OMElement requestElement)
			throws AxisFault {
		OMElement header = MessageContext.getCurrentMessageContext().getEnvelope().getHeader();
		String activityId = header.getFirstChildWithName(
				Constants.TRANSACTION_ID_PARAMETER).getText();
		String enlistmentId = header.getFirstChildWithName(
				Constants.ENLISTMENT_ID_PARAMETER).getText();
		ATCoordinator coordinator = new ATCoordinator();
		try {
			ATActivityContext atContext = (ATActivityContext) StorageUtils.getContext(activityId);
			coordinator.countVote(atContext, Vote.READ_ONLY, enlistmentId);
		} catch (AbstractKandulaException e) {
			AxisFault fault = AxisFault.makeFault(e);
			fault.setFaultCode(e.getFaultCode());
			throw fault;
		}
	}

	/**
	 * @param requestElement
	 * @throws AbstractKandulaException
	 */
	public void committedOperation(OMElement requestElement)
			throws AxisFault {
		OMElement header = MessageContext.getCurrentMessageContext().getEnvelope().getHeader();
		String activityId = header.getFirstChildWithName(
				Constants.TRANSACTION_ID_PARAMETER).getText();
		String enlistmentId = header.getFirstChildWithName(
				Constants.ENLISTMENT_ID_PARAMETER).getText();
		ATCoordinator coordinator = new ATCoordinator();
		try {
			ATActivityContext atContext = (ATActivityContext) StorageUtils.getContext(activityId);
			coordinator.countParticipantOutcome(atContext, enlistmentId);
		} catch (AbstractKandulaException e) {
			AxisFault fault = AxisFault.makeFault(e);
			fault.setFaultCode(e.getFaultCode());
			throw fault;
		}
	}

	/**
	 * @param requestElement
	 * @throws AbstractKandulaException
	 */
	public void replayOperation(OMElement requestElement) throws AxisFault {
		System.out.println("Visited Replay operation");
	}

}