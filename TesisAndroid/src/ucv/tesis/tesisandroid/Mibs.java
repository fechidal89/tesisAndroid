package ucv.tesis.tesisandroid;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;



import snmp.SNMPBERCodec;
import snmp.SNMPBadValueException;
import snmp.SNMPGetException;
import snmp.SNMPInteger;
import snmp.SNMPObject;
import snmp.SNMPObjectIdentifier;
import snmp.SNMPOctetString;
import snmp.SNMPPDU;
import snmp.SNMPRequestException;
import snmp.SNMPRequestListener;
import snmp.SNMPSequence;
import snmp.SNMPSetException;
import snmp.SNMPVariablePair;

public class Mibs implements SNMPRequestListener {

	
	SNMPOctetString storedSNMPValue;
	
	@Override
	public SNMPSequence processRequest(SNMPPDU pdu, String communityName)
			throws SNMPGetException, SNMPSetException {
		// TODO Auto-generated method stub
	    byte pduType = pdu.getPDUType();
        switch (pduType)
        {
            case SNMPBERCodec.SNMPGETREQUEST:
            {
            	 System.out.println("SNMPGETREQUEST\n");
                break;
            }
            
            case SNMPBERCodec.SNMPGETNEXTREQUEST:
            {
            	 System.out.println("SNMPGETNEXTREQUEST\n");
                break;
            }
            
            case SNMPBERCodec.SNMPSETREQUEST:
            {
            	 System.out.println("SNMPSETREQUEST\n");
                break;
            }
            
            case SNMPBERCodec.SNMPGETRESPONSE:
            {
            	 System.out.println("SNMPGETRESPONSE\n");
                break;
            }
            
            case SNMPBERCodec.SNMPTRAP:
            {
            	 System.out.println("SNMPTRAP\n");
                break;
            }
            
            default:
            {
            	 System.out.println("unknown\n");
                break;
            }
            
            
        }
        
        
        
        SNMPSequence varBindList = pdu.getVarBindList();
        SNMPSequence responseList = new SNMPSequence();
        
        for (int i = 0; i < varBindList.size(); i++)
        {
            SNMPSequence variablePair = (SNMPSequence)varBindList.getSNMPObjectAt(i);
            SNMPObjectIdentifier snmpOID = (SNMPObjectIdentifier)variablePair.getSNMPObjectAt(0);
            SNMPObject snmpValue = (SNMPObject)variablePair.getSNMPObjectAt(1);
            
            System.out.println("       OID:           " + snmpOID + "\n");
            System.out.println("       value:         " + snmpValue + "\n");
            
            
            // check to see if supplied community name is ours; if not, we'll just silently
            /* ignore the request by not returning anything
            if (!communityName.equals(this.communityName))
            {
                continue;
            }*/
            
            // we'll only respond to requests for OIDs 1.3.6.1.2.1.99.0 and 1.3.6.1.2.1.100.0 
            
            // OID 1.3.6.1.2.1.99.0: it's read-only
            if (snmpOID.toString().equals("1.3.6.1.2.1.1.4.0"))
            {
                if (pduType == SNMPBERCodec.SNMPSETREQUEST)
                {
                    // got a set-request for our variable; throw an exception to indicate the 
                    // value is read-only - the SNMPv1AgentInterface will create the appropriate
                    // error message using our supplied error index and status
                    // note that error index starts at 1, not 0, so it's i+1
                    int errorIndex = i+1;
                    int errorStatus = SNMPRequestException.VALUE_READ_ONLY;
                    throw new SNMPSetException("Trying to set a read-only variable!", errorIndex, errorStatus);
                }
                else if (pduType == SNMPBERCodec.SNMPGETREQUEST)
                {
                    // got a get-request for our variable; send back a value - just a string
                    try
                    {
                        SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPOctetString("Fernando Hidalgo"));
                        //SNMPVariablePair newPair = new SNMPVariablePair(snmpOID, new SNMPOctetString("Boo"));
                        responseList.addSNMPObject(newPair);
                    }
                    catch (SNMPBadValueException e)
                    {
                        // won't happen...
                    }
                } 
                
            }
            
            if (snmpOID.toString().equals("1.3.6.1.2.1.1.5.0"))
            {
                if (pduType == SNMPBERCodec.SNMPSETREQUEST)
                {
                    // got a set-request for our variable; supplied value must be a string
                    if (snmpValue instanceof SNMPOctetString)
                    {
                        // assign new value
                        storedSNMPValue = (SNMPOctetString)snmpValue;
                        
                        // return SNMPVariablePair to indicate we've handled this OID
                        try
                        {
                            SNMPVariablePair newPair = new SNMPVariablePair(snmpOID, storedSNMPValue);
                            responseList.addSNMPObject(newPair);
                        }
                        catch (SNMPBadValueException e)
                        {
                            // won't happen...
                        }
                    
                    }
                    else
                    {
                        int errorIndex = i+1;
                        int errorStatus = SNMPRequestException.BAD_VALUE;
                        throw new SNMPSetException("Supplied value must be SNMPOctetString", errorIndex, errorStatus);
                    }
                    
                }
                else if (pduType == SNMPBERCodec.SNMPGETREQUEST)
                {
                    // got a get-request for our variable; send back a value - just a string
                    try
                    {
                        SNMPVariablePair newPair = new SNMPVariablePair(snmpOID, storedSNMPValue);
                        responseList.addSNMPObject(newPair);
                    }
                    catch (SNMPBadValueException e)
                    {
                        // won't happen...
                    }
                } 
                
            } // if 
            
            
            
            /******************************
             *
             *   MIB-II | the Interfaces Group
             * 
             ******************************/
        
            /* ifNumber Integer | Read-Only | Mandatory */
            if (snmpOID.toString().equals("1.3.6.1.2.1.2.1.0"))
            {
                if (pduType == SNMPBERCodec.SNMPSETREQUEST)
                {
                    int errorIndex = i+1;
                    int errorStatus = SNMPRequestException.VALUE_READ_ONLY;
                    throw new SNMPSetException("Trying to set a read-only variable!", errorIndex, errorStatus);
                }
                else if (pduType == SNMPBERCodec.SNMPGETREQUEST)
                {
                    int nIf = 0; // numero de interfaces
                	ArrayList<String> arrIf = new ArrayList<String>();
                	String cmd="";
                	try
                    {
                        Process p=null;
                        try {
							p = Runtime.getRuntime().exec("ls /sys/class/net/");
							BufferedReader in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
							String line;
					        while ((line = in2.readLine()) != null) {  
					        	nIf += 1; 
					        	arrIf.add(line);
					        }

					        /** Crear tabla de if  **/
					        
					        String [] arrEntry = {
					        		"type", /* ifType */
					        		"mtu", /* ifMtu */
					        		"speed", /* IfSpeed */
					        		"address", /* ifPhyAddress */
					        		"operstate", /* ifOperStatus */  
					        		/**  ###FALTA### IfAdminStatus **/
					        		"power/runtime_active_time", /* ifLastChange */
					        		"statistics/rx_bytes", /*ifInOctets*/
					        		"statistics/rx_packets", /*IfInUcastPkts*/
					        		"statistics/multicast", /* ifInNUcastPkts*/
					        		"statistics/rx_dropped", /* ifInDiscards*/
					        		"statistics/rx_errors", /* ifInErrors*/
					        		"statistics/rx_missed", /** ifInUnknownProtos  PREGUNTAR */
					        		"statistics/tx_bytes", /* ifOutOctets */
					        		"statistics/tx_packets", /* ifOutUcastPkts */
					        		/** ###FALTA### ifOutNUcastPkts */
					        		"statistics/tx_dropped", /* ifOutDiscards */
					        		"statistics/tx_errors", /* ifOutErrors */
					        		"tx_queue_len", /* ifOutErrors */
						        	};
					        
					        for (String entry : arrEntry) {
								System.out.println("TABLE ENTRY => "+entry);
						        for (String eth : arrIf) {
						        	cmd="cat /sys/class/net/"+eth+"/"+entry+"\n";
						        	//System.out.println(cmd);
						        	p = Runtime.getRuntime().exec(cmd);
									in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
							        System.out.println("if: \""+eth+"\" => "+in2.readLine()); 
						            cmd="";
						            
							    }
					        }
					        
					        p.destroy();
					        
					    } catch (Exception e) {
							// TODO: handle exception
						}
                        
                        SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPInteger(nIf));
                        responseList.addSNMPObject(newPair);
                        
                    }
                    catch (SNMPBadValueException e)
                    {
                        // won't happen...
                    }
                } 
                    
            }
            
        } // for
        
        System.out.println("\n");
        
        
        // return the created list of variable pairs
        return responseList;
        
    
	}

	@Override
	public SNMPSequence processGetNextRequest(SNMPPDU requestPDU,
			String communityName) throws SNMPGetException {
		// TODO Auto-generated method stub
		return null;
	}

}
