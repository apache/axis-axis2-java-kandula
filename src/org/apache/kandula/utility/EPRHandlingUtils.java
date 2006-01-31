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
package org.apache.kandula.utility;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import javax.xml.namespace.QName;

import org.apache.axis2.addressing.AddressingConstants;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.addressing.AddressingConstants.Final;
import org.apache.axis2.om.OMElement;
import org.apache.axis2.om.OMNamespace;
import org.apache.axis2.om.impl.llom.factory.OMLinkedListImplFactory;
import org.apache.axis2.soap.SOAPFactory;

/**
 * @author <a href="mailto:thilina@opensource.lk"> Thilina Gunarathne </a>
 */
public class EPRHandlingUtils {

	public static void endpointToOM(EndpointReference epr, OMElement parentEPR,
			SOAPFactory factory) {
		OMNamespace wsAddressing = factory.createOMNamespace(
				AddressingConstants.Submission.WSA_NAMESPACE,
				AddressingConstants.WSA_DEFAULT_PREFIX);
		OMElement addressElement = factory.createOMElement("Address",
				wsAddressing);
		addressElement.setText(epr.getAddress());
		parentEPR.addChild(addressElement);
		Map referenceValues = epr.getAllReferenceParameters();
		if (referenceValues != null) {
			OMElement refPropertyElement = factory.createOMElement(
					"ReferenceParameters", wsAddressing);
			parentEPR.addChild(refPropertyElement);
			Iterator iterator = referenceValues.keySet().iterator();
			while (iterator.hasNext()) {
				QName key = (QName) iterator.next();
				OMElement omElement = (OMElement) referenceValues.get(key);
				refPropertyElement.addChild(omElement);
				if (Final.WSA_NAMESPACE.equals(wsAddressing)) {
					omElement.addAttribute(
							Final.WSA_IS_REFERENCE_PARAMETER_ATTRIBUTE,
							Final.WSA_TYPE_ATTRIBUTE_VALUE, wsAddressing);
				}
			}
		}
	}

	public static EndpointReference endpointFromOM(OMElement eprElement) {
		EndpointReference epr;
		epr = new EndpointReference(eprElement.getFirstChildWithName(
				new QName("Address")).getText());
		HashMap referenceProperties = new HashMap();
		OMElement referencePropertiesElement = eprElement
				.getFirstChildWithName(new QName("ReferenceParameters"));
		Iterator propertyIter = referencePropertiesElement.getChildElements();
		while (propertyIter.hasNext()) {
			OMElement element = (OMElement) propertyIter.next();

			//TODO do we need to detach the OMElement
			referenceProperties.put(element.getQName(), element
					.cloneOMElement());
		}
		//will have to live with Ref parameters for some time :: Till axis2
		// @ing gets stable
		epr.setReferenceParameters(referenceProperties);
		return epr;
	}

	public static void addReferenceProperty(EndpointReference epr, QName key,
			String Value) {
		// We'll have to live with reference parameters for the moment
		// Since Axis2 Addressing does not support ref properties well
		HashMap refProperties;
		if ((refProperties = (HashMap) epr.getAllReferenceParameters()) == null) {
			refProperties = new HashMap();
		}
		OMLinkedListImplFactory factory = new OMLinkedListImplFactory();
		OMElement omElement = factory.createOMElement(key, null);
		omElement.setText(Value);
		refProperties.put(key, omElement);
		epr.setReferenceParameters(refProperties);
	}

	/**
	 * MD5 a random string with localhost/date etc will return 128 bits
	 * construct a string of 18 characters from those bits.
	 * 
	 * @return string
	 */
	public static String getRandomStringOf18Characters() {
		Random myRand = new Random();
		long rand = myRand.nextLong();
		String sid;
		try {
			sid = InetAddress.getLocalHost().toString();
		} catch (UnknownHostException e) {
			sid = Thread.currentThread().getName();
		}
		long time = System.currentTimeMillis();
		StringBuffer sb = new StringBuffer();
		sb.append(sid);
		sb.append(":");
		sb.append(Long.toString(time));
		sb.append(":");
		sb.append(Long.toString(rand));
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			//System.out.println("Error: " + e);
			//todo heve to be properly handle
		}
		md5.update(sb.toString().getBytes());
		byte[] array = md5.digest();
		StringBuffer sb2 = new StringBuffer();
		for (int j = 0; j < array.length; ++j) {
			int b = array[j] & 0xFF;
			sb2.append(Integer.toHexString(b));
		}
		int begin = myRand.nextInt();
		if (begin < 0)
			begin = begin * -1;
		begin = begin % 8;
		return new String(sb2.toString().substring(begin, begin + 18))
				.toUpperCase();
	}
}