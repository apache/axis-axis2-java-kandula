package org.apache.kandula.participant.ba;

import org.apache.axis2.AxisFault;
import org.apache.axis2.context.MessageContext;
import org.apache.axis2.handlers.AbstractHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.kandula.Constants;
import org.apache.kandula.Status;
import org.apache.kandula.context.AbstractContext;
import org.apache.kandula.context.impl.ParticipantContext;
import org.apache.kandula.faults.AbstractKandulaException;
import org.apache.kandula.storage.StorageUtils;

public class ParticipantOutFaultHandler extends AbstractHandler {

	/**
	 * Field log
	 */
	private static final Log log = LogFactory.getLog(ParticipantOutFaultHandler.class);

	public InvocationResponse invoke(MessageContext msgContext) throws AxisFault {

		Object property = msgContext.getOperationContext()
				.getProperty(AbstractContext.REQUESTER_ID);
		if (property != null) {
			String reqID = (String) property;
			AbstractContext context = StorageUtils.getContext(msgContext, reqID);
			if (context != null) {
				ParticipantContext participantContext = (ParticipantContext) context;
				if (participantContext.getRegistrationProtocol().equals(Constants.WS_BA_CC)
						|| participantContext.getRegistrationProtocol().equals(Constants.WS_BA_PC)) {
					// Notify the coordinator if the participant is wsba-pc
					BAParticipantTransactionSentCoordinator coordinator = new BAParticipantTransactionSentCoordinator();
					try {
						coordinator.fault(participantContext);
					} catch (AbstractKandulaException e) {
						throw AxisFault.makeFault(e);
					}
				}
			}
		}
		return InvocationResponse.CONTINUE;
	}
}
