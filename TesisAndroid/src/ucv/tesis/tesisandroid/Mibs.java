package ucv.tesis.tesisandroid;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;



import snmp.SNMPBERCodec;
import snmp.SNMPBadValueException;
import snmp.SNMPCounter32;
import snmp.SNMPGauge32;
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
import snmp.SNMPTimeTicks;
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
					        
					        p = Runtime.getRuntime().exec("cat /proc/sys/net/ipv4/ip_default_ttl"+"\n");
							in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
					        System.out.println("DEFAUL_TTL_IPv4 => "+in2.readLine()); 
				            
					        
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
            
            
            // ifEntry Integer | Read-Only | Mandatory | 1.3.6.1.2.1.2.2.1.1.X -> es el numero de interfaz
            if (snmpOID.toString().startsWith("1.3.6.1.2.1.2.2.1.1."))
            {
                if (pduType == SNMPBERCodec.SNMPSETREQUEST)
                {
                	int errorIndex = i+1;
                    int errorStatus = SNMPRequestException.VALUE_READ_ONLY;
                    throw new SNMPSetException("Trying to set a read-only variable!", errorIndex, errorStatus);
                }
                else if (pduType == SNMPBERCodec.SNMPGETREQUEST)
                {

                	int eth=0; // interfaz solicitada
                	int nIf =0; // numero de interfaces
                 	String cmd="ls /sys/class/net/"; // comando para listar las interfaces en el dispositivo.
					Process p=null;
					// Busco cuantas interfaces existen
					try {
						p = Runtime.getRuntime().exec(cmd);
						BufferedReader in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
						
					    while (in2.readLine() != null) {  
					    	nIf += 1;  					        	
					    }
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
					// Pregunto cual interfaz que esta solicitando
					try {
						eth =  Integer.parseInt(snmpOID.toString().substring(20, snmpOID.toString().length()));
					} catch (NumberFormatException e) {
						// TODO: handle exception
						e.printStackTrace();
					}
                		
                	// si el numero de interfaz solicitada esta busco el valor y lo envio
					if( 1 <= eth && eth <= nIf ){
	                    try
	                    {
	                        SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPInteger(eth));
	                        responseList.addSNMPObject(newPair);
	                    }
	                    catch (SNMPBadValueException e)
	                    {
	                        // won't happen...
	                    }
					}else{
						int errorIndex = i+1;
	                    int errorStatus = SNMPRequestException.VALUE_NOT_AVAILABLE;
	                    throw new SNMPSetException("Valor fuera de rango del número interfaces", errorIndex, errorStatus);						
					}
                	
                	
                } 
                
            } // ifEntry
            
         // ifDescr DisplayString | Read-Only | Mandatory | 1.3.6.1.2.1.2.2.1.2.X -> X es el numero de interfaz
            if (snmpOID.toString().startsWith("1.3.6.1.2.1.2.2.1.2."))
            {
                if (pduType == SNMPBERCodec.SNMPSETREQUEST)
                {
                	int errorIndex = i+1;
                    int errorStatus = SNMPRequestException.VALUE_READ_ONLY;
                    throw new SNMPSetException("Trying to set a read-only variable!", errorIndex, errorStatus);
                }
                else if (pduType == SNMPBERCodec.SNMPGETREQUEST)
                {

                	int eth=0; // interfaz solicitada
                	int nIf =0; // numero de interfaces
                 	String cmd="ls /sys/class/net/"; // comando para listar las interfaces en el dispositivo.
					Process p=null;
					ArrayList<String> arrIf = new ArrayList<String>();
					// Busco cuantas interfaces existen
					try {
						p = Runtime.getRuntime().exec(cmd);
						BufferedReader in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
						String line="";
						
					    while ((line = in2.readLine()) != null) {  
					    	nIf += 1;
					    	arrIf.add(line);
					    }
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
					// Pregunto cual interfaz que esta solicitando
					try {
						eth =  Integer.parseInt(snmpOID.toString().substring(20, snmpOID.toString().length()));
					} catch (NumberFormatException e) {
						// TODO: handle exception
						e.printStackTrace();
					}
                		
                	// si el numero de interfaz solicitada esta busco el valor y lo envio
					if( 1 <= eth && eth <= nIf ){
	                    try
	                    {
	                        SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPOctetString(arrIf.get(eth-1).toString()));
	                        responseList.addSNMPObject(newPair);
	                    }
	                    catch (SNMPBadValueException e)
	                    {
	                        // won't happen...
	                    }
					}else{
						int errorIndex = i+1;
	                    int errorStatus = SNMPRequestException.VALUE_NOT_AVAILABLE;
	                    throw new SNMPSetException("Valor fuera de rango del número interfaces", errorIndex, errorStatus);						
					}
                	                	
                } 
                
            } // ifDescr
            
            
            // ifType Integer | Read-Only | Mandatory | 1.3.6.1.2.1.2.2.1.3.X -> X es el numero de interfaz
            if (snmpOID.toString().startsWith("1.3.6.1.2.1.2.2.1.3."))
            {
                if (pduType == SNMPBERCodec.SNMPSETREQUEST)
                {
                	int errorIndex = i+1;
                    int errorStatus = SNMPRequestException.VALUE_READ_ONLY;
                    throw new SNMPSetException("Trying to set a read-only variable!", errorIndex, errorStatus);
                }
                else if (pduType == SNMPBERCodec.SNMPGETREQUEST)
                {

                	int eth=0; // interfaz solicitada
                	int nIf =0; // numero de interfaces
                 	String cmd="ls /sys/class/net/"; // comando para listar las interfaces en el dispositivo.
					Process p=null;
					ArrayList<String> arrIf = new ArrayList<String>();
					// Busco cuantas interfaces existen
					try {
						p = Runtime.getRuntime().exec(cmd);
						BufferedReader in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
						String line="";
						
					    while ((line = in2.readLine()) != null) {  
					    	nIf += 1;
					    	arrIf.add(line);
					    }
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
					// Pregunto cual interfaz que esta solicitando
					try {
						eth =  Integer.parseInt(snmpOID.toString().substring(20, snmpOID.toString().length()));
					} catch (NumberFormatException e) {
						// TODO: handle exception
						e.printStackTrace();
					}
                		
                	// si el numero de interfaz solicitada esta, busco el valor y lo envio
					if( 1 <= eth && eth <= nIf ){
	                    try
	                    {   
	                    	SNMPVariablePair newPair;
	                    	if(arrIf.get(eth-1).toString().equalsIgnoreCase("lo")||
	                    	   arrIf.get(eth-1).toString().equalsIgnoreCase("lo0")|| // soporte a varias Loopback
	                    	   arrIf.get(eth-1).toString().equalsIgnoreCase("lo1")){
	                        	newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPInteger(24));		                        
	                    	}else{
	                    		newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPInteger(1));		                        		                    		
	                    	}
	                    	responseList.addSNMPObject(newPair);
	                    }
	                    catch (SNMPBadValueException e)
	                    {
	                        // won't happen...
	                    }catch (Exception e)
	                    {
	                    	// TODO: handle exception
							e.printStackTrace();
	                    }
					}else{
						int errorIndex = i+1;
	                    int errorStatus = SNMPRequestException.VALUE_NOT_AVAILABLE;
	                    throw new SNMPSetException("Valor fuera de rango del número interfaces", errorIndex, errorStatus);						
					}
                	                	
                } 
                
            } // ifType
            
            // ifMtu Integer | Read-Only | Mandatory | 1.3.6.1.2.1.2.2.1.4.X -> X es el numero de interfaz
            if (snmpOID.toString().startsWith("1.3.6.1.2.1.2.2.1.4."))
            {
                if (pduType == SNMPBERCodec.SNMPSETREQUEST)
                {
                	int errorIndex = i+1;
                    int errorStatus = SNMPRequestException.VALUE_READ_ONLY;
                    throw new SNMPSetException("Trying to set a read-only variable!", errorIndex, errorStatus);
                }
                else if (pduType == SNMPBERCodec.SNMPGETREQUEST)
                {

                	int eth=0; // interfaz solicitada
                	int nIf=0; // numero de interfaces
                	int mtu=0; // Max Trafer Unit 
                 	String cmd="ls /sys/class/net/", line=""; // comando para listar las interfaces en el dispositivo.
					Process p=null;
					BufferedReader in2=null;
					ArrayList<String> arrIf = new ArrayList<String>();
					// Busco cuantas interfaces existen
					try {
						p = Runtime.getRuntime().exec(cmd);
						in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
						line="";
						
					    while ((line = in2.readLine()) != null) {  
					    	nIf += 1;
					    	arrIf.add(line);					    	
					    }
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
					// Pregunto cual interfaz que esta solicitando
					try {
						eth =  Integer.parseInt(snmpOID.toString().substring(20, snmpOID.toString().length()));
					}catch (NumberFormatException e) {
						// TODO: handle exception
						e.printStackTrace();
					}
                		
                	// si el numero de interfaz solicitada esta, busco el valor y lo envio
					if( 1 <= eth && eth <= nIf ){
	                    try
	                    {
	                    	cmd="cat /sys/class/net/"+arrIf.get(eth-1)+"/mtu\n";
	                    	p = Runtime.getRuntime().exec(cmd);
							in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
							mtu = Integer.parseInt(in2.readLine());
	                    	
	                        SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPInteger(mtu));
	                        responseList.addSNMPObject(newPair);
	                    }
	                    catch (SNMPBadValueException e)
	                    {
	                        // won't happen...
	                    }catch (Exception e)
	                    {
	                    	// TODO: handle exception
							e.printStackTrace();
	                    }
					}else{
						int errorIndex = i+1;
	                    int errorStatus = SNMPRequestException.VALUE_NOT_AVAILABLE;
	                    throw new SNMPSetException("Valor fuera de rango del número interfaces", errorIndex, errorStatus);						
					}
                	                	
                } 
                
            } // ifMtu
            
            
         // ifSpeed Gauge | Read-Only | Mandatory | 1.3.6.1.2.1.2.2.1.5.X -> X es el numero de interfaz
            if (snmpOID.toString().startsWith("1.3.6.1.2.1.2.2.1.5."))
            {
                if (pduType == SNMPBERCodec.SNMPSETREQUEST)
                {
                	int errorIndex = i+1;
                    int errorStatus = SNMPRequestException.VALUE_READ_ONLY;
                    throw new SNMPSetException("Trying to set a read-only variable!", errorIndex, errorStatus);
                }
                else if (pduType == SNMPBERCodec.SNMPGETREQUEST)
                {

                	int eth=0; // interfaz solicitada
                	int nIf=0; // numero de interfaces
                	int speed=0; // bandwidth bits per second
                 	String cmd="ls /sys/class/net/", line=""; // comando para listar las interfaces en el dispositivo.
					Process p=null;
					BufferedReader in2=null;
					ArrayList<String> arrIf = new ArrayList<String>();
					// Busco cuantas interfaces existen
					try {
						p = Runtime.getRuntime().exec(cmd);
						in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
						line="";
						
					    while ((line = in2.readLine()) != null) {  
					    	nIf += 1;
					    	arrIf.add(line);					    	
					    }
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
					// Pregunto cual interfaz que esta solicitando
					try {
						eth =  Integer.parseInt(snmpOID.toString().substring(20, snmpOID.toString().length()));
					}catch (NumberFormatException e) {
						// TODO: handle exception
						e.printStackTrace();
					}
                		
                	// si el numero de interfaz solicitada esta, busco el valor y lo envio
					if( 1 <= eth && eth <= nIf ){
	                    try
	                    {
	                    	cmd="cat /sys/class/net/"+arrIf.get(eth-1)+"/speed\n";
	                    	p = Runtime.getRuntime().exec(cmd);
							in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
							line = in2.readLine();
							if(line == null) speed = 0;
							else speed = Integer.parseInt(line);
	                    	
	                        SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPGauge32(speed));
	                        responseList.addSNMPObject(newPair);
	                    }
	                    catch (SNMPBadValueException e)
	                    {
	                        // won't happen...
	                    }catch (Exception e)
	                    {
	                    	// TODO: handle exception	                    	
							e.printStackTrace();
	                    }
					}else{
						int errorIndex = i+1;
	                    int errorStatus = SNMPRequestException.VALUE_NOT_AVAILABLE;
	                    throw new SNMPSetException("Valor fuera de rango del número interfaces", errorIndex, errorStatus);						
					}
                	                	
                } 
                
            } // ifSpeed
            
            
         // ifPhysAddress PhysAddress | Read-Only | Mandatory | 1.3.6.1.2.1.2.2.1.6.X -> X es el numero de interfaz
            if (snmpOID.toString().startsWith("1.3.6.1.2.1.2.2.1.6."))
            {
                if (pduType == SNMPBERCodec.SNMPSETREQUEST)
                {
                	int errorIndex = i+1;
                    int errorStatus = SNMPRequestException.VALUE_READ_ONLY;
                    throw new SNMPSetException("Trying to set a read-only variable!", errorIndex, errorStatus);
                }
                else if (pduType == SNMPBERCodec.SNMPGETREQUEST)
                {

                	int eth=0; // interfaz solicitada
                	int nIf=0; // numero de interfaces
                	String address=""; // PhysAddress
                 	String cmd="ls /sys/class/net/", line=""; // comando para listar las interfaces en el dispositivo.
					Process p=null;
					BufferedReader in2=null;
					ArrayList<String> arrIf = new ArrayList<String>();
					// Busco cuantas interfaces existen
					try {
						p = Runtime.getRuntime().exec(cmd);
						in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
						line="";
						
					    while ((line = in2.readLine()) != null) {  
					    	nIf += 1;
					    	arrIf.add(line);					    	
					    }
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
					// Pregunto cual interfaz que esta solicitando
					try {
						eth =  Integer.parseInt(snmpOID.toString().substring(20, snmpOID.toString().length()));
					}catch (NumberFormatException e) {
						// TODO: handle exception
						e.printStackTrace();
					}
                		
                	// si el numero de interfaz solicitada esta, busco el valor y lo envio
					if( 1 <= eth && eth <= nIf ){
	                    try
	                    {
	                    	cmd="cat /sys/class/net/"+arrIf.get(eth-1)+"/address\n";
	                    	p = Runtime.getRuntime().exec(cmd);
							in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
							address = in2.readLine();
							
	                        SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPOctetString(address));
	                        responseList.addSNMPObject(newPair);
	                    }
	                    catch (SNMPBadValueException e)
	                    {
	                        // won't happen...
	                    }catch (Exception e)
	                    {
	                    	// TODO: handle exception	                    	
							e.printStackTrace();
	                    }
					}else{
						int errorIndex = i+1;
	                    int errorStatus = SNMPRequestException.VALUE_NOT_AVAILABLE;
	                    throw new SNMPSetException("Valor fuera de rango del número interfaces", errorIndex, errorStatus);						
					}
                	                	
                } 
                
            } // ifPhysAddress
            
            /**
             * 
             *	FALTA ifAdminStatus -> acceso a BD porque es Read-Write
             * 
             */
            
            // ifOperStatus Integer | Read-Only | Mandatory | 1.3.6.1.2.1.2.2.1.8.X -> X es el numero de interfaz
            if (snmpOID.toString().startsWith("1.3.6.1.2.1.2.2.1.8."))
            {
                if (pduType == SNMPBERCodec.SNMPSETREQUEST)
                {
                	int errorIndex = i+1;
                    int errorStatus = SNMPRequestException.VALUE_READ_ONLY;
                    throw new SNMPSetException("Trying to set a read-only variable!", errorIndex, errorStatus);
                }
                else if (pduType == SNMPBERCodec.SNMPGETREQUEST)
                {

                	int eth=0; // interfaz solicitada
                	int nIf=0; // numero de interfaces
                	String status="";
                	int oper=0; // operational state
                 	String cmd="ls /sys/class/net/", line=""; // comando para listar las interfaces en el dispositivo.
					Process p=null;
					BufferedReader in2=null;
					ArrayList<String> arrIf = new ArrayList<String>();
					// Busco cuantas interfaces existen
					try {
						p = Runtime.getRuntime().exec(cmd);
						in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
						line="";
						
					    while ((line = in2.readLine()) != null) {  
					    	nIf += 1;
					    	arrIf.add(line);					    	
					    }
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
					// Pregunto cual interfaz que esta solicitando
					try {
						eth =  Integer.parseInt(snmpOID.toString().substring(20, snmpOID.toString().length()));
					}catch (NumberFormatException e) {
						// TODO: handle exception
						e.printStackTrace();
					}
                		
                	// si el numero de interfaz solicitada esta, busco el valor y lo envio
					if( 1 <= eth && eth <= nIf ){
	                    try
	                    {
	                    	cmd="cat /sys/class/net/"+arrIf.get(eth-1)+"/operstate\n";
	                    	p = Runtime.getRuntime().exec(cmd);
							in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
							status = in2.readLine();
							// el comando devuelve String lo busco segun el RFC2863
							if(status.equalsIgnoreCase("up")){
								oper = 1;
							}else if(status.equalsIgnoreCase("down")){
								oper = 2;
							}else if(status.equalsIgnoreCase("testing")){								
								oper = 3;
							}else if(status.equalsIgnoreCase("unknown")){
								oper = 4;
							}else if(status.equalsIgnoreCase("dormant")){
								oper = 5;
							}else if(status.equalsIgnoreCase("notPresent")){
								oper = 6;
							}else{ // lowerLayerDown
								oper = 7;
							}
							
	                        SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPInteger(oper));
	                        responseList.addSNMPObject(newPair);
	                    }
	                    catch (SNMPBadValueException e)
	                    {
	                        // won't happen...
	                    }catch (Exception e)
	                    {
	                    	// TODO: handle exception	                    	
							e.printStackTrace();
	                    }
					}else{
						int errorIndex = i+1;
	                    int errorStatus = SNMPRequestException.VALUE_NOT_AVAILABLE;
	                    throw new SNMPSetException("Valor fuera de rango del número interfaces", errorIndex, errorStatus);						
					}
                	                	
                } 
                
            } // ifOperStatus
            
            // ifLastChange TimeTicks | Read-Only | Mandatory | 1.3.6.1.2.1.2.2.1.9.X -> X es el numero de interfaz
            if (snmpOID.toString().startsWith("1.3.6.1.2.1.2.2.1.9."))
            {
                if (pduType == SNMPBERCodec.SNMPSETREQUEST)
                {
                	int errorIndex = i+1;
                    int errorStatus = SNMPRequestException.VALUE_READ_ONLY;
                    throw new SNMPSetException("Trying to set a read-only variable!", errorIndex, errorStatus);
                }
                else if (pduType == SNMPBERCodec.SNMPGETREQUEST)
                {

                	int eth=0; // interfaz solicitada
                	int nIf=0; // numero de interfaces
                	int time = 0;
                 	String cmd="ls /sys/class/net/", line=""; // comando para listar las interfaces en el dispositivo.
					Process p=null;
					BufferedReader in2=null;
					ArrayList<String> arrIf = new ArrayList<String>();
					// Busco cuantas interfaces existen
					try {
						p = Runtime.getRuntime().exec(cmd);
						in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
						line="";
						
					    while ((line = in2.readLine()) != null) {  
					    	nIf += 1;
					    	arrIf.add(line);					    	
					    }
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
					// Pregunto cual interfaz que esta solicitando
					try {
						eth =  Integer.parseInt(snmpOID.toString().substring(20, snmpOID.toString().length()));
					}catch (NumberFormatException e) {
						// TODO: handle exception
						e.printStackTrace();
					}
                		
                	// si el numero de interfaz solicitada esta, busco el valor y lo envio
					if( 1 <= eth && eth <= nIf ){
	                    try
	                    {
	                    	cmd="cat /sys/class/net/"+arrIf.get(eth-1)+"/power/runtime_active_time\n";
	                    	p = Runtime.getRuntime().exec(cmd);
							in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
							time = Integer.parseInt(in2.readLine());
							
	                        SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPTimeTicks(time));
	                        responseList.addSNMPObject(newPair);
	                    }
	                    catch (SNMPBadValueException e)
	                    {
	                        // won't happen...
	                    }catch (Exception e)
	                    {
	                    	// TODO: handle exception	                    	
							e.printStackTrace();
	                    }
					}else{
						int errorIndex = i+1;
	                    int errorStatus = SNMPRequestException.VALUE_NOT_AVAILABLE;
	                    throw new SNMPSetException("Valor fuera de rango del número interfaces", errorIndex, errorStatus);						
					}
                	                	
                } 
                
            } // ifLastChange
            

            // ifInOctets Counter32 | Read-Only | Mandatory | 1.3.6.1.2.1.2.2.1.10.X -> X es el numero de interfaz
            if (snmpOID.toString().startsWith("1.3.6.1.2.1.2.2.1.10."))
            {
                if (pduType == SNMPBERCodec.SNMPSETREQUEST)
                {
                	int errorIndex = i+1;
                    int errorStatus = SNMPRequestException.VALUE_READ_ONLY;
                    throw new SNMPSetException("Trying to set a read-only variable!", errorIndex, errorStatus);
                }
                else if (pduType == SNMPBERCodec.SNMPGETREQUEST)
                {

                	int eth=0; // interfaz solicitada
                	int nIf=0; // numero de interfaces
                	long octets = 0; //octets
                 	String cmd="ls /sys/class/net/", line=""; // comando para listar las interfaces en el dispositivo.
					Process p=null;
					BufferedReader in2=null;
					ArrayList<String> arrIf = new ArrayList<String>();
					// Busco cuantas interfaces existen
					try {
						p = Runtime.getRuntime().exec(cmd);
						in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
						line="";
						
					    while ((line = in2.readLine()) != null) {  
					    	nIf += 1;
					    	arrIf.add(line);					    	
					    }
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
					// Pregunto cual interfaz que esta solicitando
					try {
						eth =  Integer.parseInt(snmpOID.toString().substring(21, snmpOID.toString().length()));
					}catch (NumberFormatException e) {
						// TODO: handle exception
						e.printStackTrace();
					}
                		
                	// si el numero de interfaz solicitada esta, busco el valor y lo envio
					if( 1 <= eth && eth <= nIf ){
	                    try
	                    {
	                    	cmd="cat /sys/class/net/"+arrIf.get(eth-1)+"/statistics/rx_bytes\n";
	                    	p = Runtime.getRuntime().exec(cmd);
							in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
							octets = Long.parseLong(in2.readLine());
							
	                        SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(octets));
	                        responseList.addSNMPObject(newPair);
	                    }
	                    catch (SNMPBadValueException e)
	                    {
	                        // won't happen...
	                    }catch (Exception e)
	                    {
	                    	// TODO: handle exception	                    	
							e.printStackTrace();
	                    }
					}else{
						int errorIndex = i+1;
	                    int errorStatus = SNMPRequestException.VALUE_NOT_AVAILABLE;
	                    throw new SNMPSetException("Valor fuera de rango del número interfaces", errorIndex, errorStatus);						
					}
                	                	
                } 
                
            } // ifInOctets
            
            
            // ifInUcastPkts Counter32 | Read-Only | Mandatory | 1.3.6.1.2.1.2.2.1.11.X -> X es el numero de interfaz
            if (snmpOID.toString().startsWith("1.3.6.1.2.1.2.2.1.11."))
            {
                if (pduType == SNMPBERCodec.SNMPSETREQUEST)
                {
                	int errorIndex = i+1;
                    int errorStatus = SNMPRequestException.VALUE_READ_ONLY;
                    throw new SNMPSetException("Trying to set a read-only variable!", errorIndex, errorStatus);
                }
                else if (pduType == SNMPBERCodec.SNMPGETREQUEST)
                {

                	int eth=0; // interfaz solicitada
                	int nIf=0; // numero de interfaces
                	long packets = 0; //packets
                 	String cmd="ls /sys/class/net/", line=""; // comando para listar las interfaces en el dispositivo.
					Process p=null;
					BufferedReader in2=null;
					ArrayList<String> arrIf = new ArrayList<String>();
					// Busco cuantas interfaces existen
					try {
						p = Runtime.getRuntime().exec(cmd);
						in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
						line="";
						
					    while ((line = in2.readLine()) != null) {  
					    	nIf += 1;
					    	arrIf.add(line);					    	
					    }
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
					// Pregunto cual interfaz que esta solicitando
					try {
						eth =  Integer.parseInt(snmpOID.toString().substring(21, snmpOID.toString().length()));
					}catch (NumberFormatException e) {
						// TODO: handle exception
						e.printStackTrace();
					}
                		
                	// si el numero de interfaz solicitada esta, busco el valor y lo envio
					if( 1 <= eth && eth <= nIf ){
	                    try
	                    {
	                    	cmd="cat /sys/class/net/"+arrIf.get(eth-1)+"/statistics/rx_packets\n";
	                    	p = Runtime.getRuntime().exec(cmd);
							in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
							packets = Long.parseLong(in2.readLine());
							
	                        SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(packets));
	                        responseList.addSNMPObject(newPair);
	                    }
	                    catch (SNMPBadValueException e)
	                    {
	                        // won't happen...
	                    }catch (Exception e)
	                    {
	                    	// TODO: handle exception	                    	
							e.printStackTrace();
	                    }
					}else{
						int errorIndex = i+1;
	                    int errorStatus = SNMPRequestException.VALUE_NOT_AVAILABLE;
	                    throw new SNMPSetException("Valor fuera de rango del número interfaces", errorIndex, errorStatus);						
					}
                	                	
                } 
                
            } // ifInUcastPkts
            
            // ifInNUcastPkts Counter32 | Read-Only | Mandatory | 1.3.6.1.2.1.2.2.1.12.X -> X es el numero de interfaz
            if (snmpOID.toString().startsWith("1.3.6.1.2.1.2.2.1.12."))
            {
                if (pduType == SNMPBERCodec.SNMPSETREQUEST)
                {
                	int errorIndex = i+1;
                    int errorStatus = SNMPRequestException.VALUE_READ_ONLY;
                    throw new SNMPSetException("Trying to set a read-only variable!", errorIndex, errorStatus);
                }
                else if (pduType == SNMPBERCodec.SNMPGETREQUEST)
                {

                	int eth=0; // interfaz solicitada
                	int nIf=0; // numero de interfaces
                	long packets = 0; //packets
                 	String cmd="ls /sys/class/net/", line=""; // comando para listar las interfaces en el dispositivo.
					Process p=null;
					BufferedReader in2=null;
					ArrayList<String> arrIf = new ArrayList<String>();
					// Busco cuantas interfaces existen
					try {
						p = Runtime.getRuntime().exec(cmd);
						in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
						line="";
						
					    while ((line = in2.readLine()) != null) {  
					    	nIf += 1;
					    	arrIf.add(line);					    	
					    }
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
					// Pregunto cual interfaz que esta solicitando
					try {
						eth =  Integer.parseInt(snmpOID.toString().substring(21, snmpOID.toString().length()));
					}catch (NumberFormatException e) {
						// TODO: handle exception
						e.printStackTrace();
					}
                		
                	// si el numero de interfaz solicitada esta, busco el valor y lo envio
					if( 1 <= eth && eth <= nIf ){
	                    try
	                    {
	                    	cmd="cat /sys/class/net/"+arrIf.get(eth-1)+"/statistics/multicast\n";
	                    	p = Runtime.getRuntime().exec(cmd);
							in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
							packets = Long.parseLong(in2.readLine());
							
	                        SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(packets));
	                        responseList.addSNMPObject(newPair);
	                    }
	                    catch (SNMPBadValueException e)
	                    {
	                        // won't happen...
	                    }catch (Exception e)
	                    {
	                    	// TODO: handle exception	                    	
							e.printStackTrace();
	                    }
					}else{
						int errorIndex = i+1;
	                    int errorStatus = SNMPRequestException.VALUE_NOT_AVAILABLE;
	                    throw new SNMPSetException("Valor fuera de rango del número interfaces", errorIndex, errorStatus);						
					}
                	                	
                } 
                
            } // ifInNUcastPkts
            
            
            // ifInDiscards Counter32 | Read-Only | Mandatory | 1.3.6.1.2.1.2.2.1.13.X -> X es el numero de interfaz
            if (snmpOID.toString().startsWith("1.3.6.1.2.1.2.2.1.13."))
            {
                if (pduType == SNMPBERCodec.SNMPSETREQUEST)
                {
                	int errorIndex = i+1;
                    int errorStatus = SNMPRequestException.VALUE_READ_ONLY;
                    throw new SNMPSetException("Trying to set a read-only variable!", errorIndex, errorStatus);
                }
                else if (pduType == SNMPBERCodec.SNMPGETREQUEST)
                {

                	int eth=0; // interfaz solicitada
                	int nIf=0; // numero de interfaces
                	long packets = 0; //packets
                 	String cmd="ls /sys/class/net/", line=""; // comando para listar las interfaces en el dispositivo.
					Process p=null;
					BufferedReader in2=null;
					ArrayList<String> arrIf = new ArrayList<String>();
					// Busco cuantas interfaces existen
					try {
						p = Runtime.getRuntime().exec(cmd);
						in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
						line="";
						
					    while ((line = in2.readLine()) != null) {  
					    	nIf += 1;
					    	arrIf.add(line);					    	
					    }
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
					// Pregunto cual interfaz que esta solicitando
					try {
						eth =  Integer.parseInt(snmpOID.toString().substring(21, snmpOID.toString().length()));
					}catch (NumberFormatException e) {
						// TODO: handle exception
						e.printStackTrace();
					}
                		
                	// si el numero de interfaz solicitada esta, busco el valor y lo envio
					if( 1 <= eth && eth <= nIf ){
	                    try
	                    {
	                    	cmd="cat /sys/class/net/"+arrIf.get(eth-1)+"/statistics/rx_dropped\n";
	                    	p = Runtime.getRuntime().exec(cmd);
							in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
							packets = Long.parseLong(in2.readLine());
							
	                        SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(packets));
	                        responseList.addSNMPObject(newPair);
	                    }
	                    catch (SNMPBadValueException e)
	                    {
	                        // won't happen...
	                    }catch (Exception e)
	                    {
	                    	// TODO: handle exception	                    	
							e.printStackTrace();
	                    }
					}else{
						int errorIndex = i+1;
	                    int errorStatus = SNMPRequestException.VALUE_NOT_AVAILABLE;
	                    throw new SNMPSetException("Valor fuera de rango del número interfaces", errorIndex, errorStatus);						
					}
                	                	
                } 
                
            } // ifInDiscards
            
            // ifInErrors Counter32 | Read-Only | Mandatory | 1.3.6.1.2.1.2.2.1.14.X -> X es el numero de interfaz
            if (snmpOID.toString().startsWith("1.3.6.1.2.1.2.2.1.14."))
            {
                if (pduType == SNMPBERCodec.SNMPSETREQUEST)
                {
                	int errorIndex = i+1;
                    int errorStatus = SNMPRequestException.VALUE_READ_ONLY;
                    throw new SNMPSetException("Trying to set a read-only variable!", errorIndex, errorStatus);
                }
                else if (pduType == SNMPBERCodec.SNMPGETREQUEST)
                {

                	int eth=0; // interfaz solicitada
                	int nIf=0; // numero de interfaces
                	long packets = 0; //packets
                 	String cmd="ls /sys/class/net/", line=""; // comando para listar las interfaces en el dispositivo.
					Process p=null;
					BufferedReader in2=null;
					ArrayList<String> arrIf = new ArrayList<String>();
					// Busco cuantas interfaces existen
					try {
						p = Runtime.getRuntime().exec(cmd);
						in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
						line="";
						
					    while ((line = in2.readLine()) != null) {  
					    	nIf += 1;
					    	arrIf.add(line);					    	
					    }
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
					// Pregunto cual interfaz que esta solicitando
					try {
						eth =  Integer.parseInt(snmpOID.toString().substring(21, snmpOID.toString().length()));
					}catch (NumberFormatException e) {
						// TODO: handle exception
						e.printStackTrace();
					}
                		
                	// si el numero de interfaz solicitada esta, busco el valor y lo envio
					if( 1 <= eth && eth <= nIf ){
	                    try
	                    {
	                    	cmd="cat /sys/class/net/"+arrIf.get(eth-1)+"/statistics/rx_errors\n";
	                    	p = Runtime.getRuntime().exec(cmd);
							in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
							packets = Long.parseLong(in2.readLine());
							
	                        SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(packets));
	                        responseList.addSNMPObject(newPair);
	                    }
	                    catch (SNMPBadValueException e)
	                    {
	                        // won't happen...
	                    }catch (Exception e)
	                    {
	                    	// TODO: handle exception	                    	
							e.printStackTrace();
	                    }
					}else{
						int errorIndex = i+1;
	                    int errorStatus = SNMPRequestException.VALUE_NOT_AVAILABLE;
	                    throw new SNMPSetException("Valor fuera de rango del número interfaces", errorIndex, errorStatus);						
					}
                	                	
                } 
                
            } // ifInErrors
            
            
            // ifInUnknownProtos Counter32 | Read-Only | Mandatory | 1.3.6.1.2.1.2.2.1.15.X -> X es el numero de interfaz
            if (snmpOID.toString().startsWith("1.3.6.1.2.1.2.2.1.15."))
            {
                if (pduType == SNMPBERCodec.SNMPSETREQUEST)
                {
                	int errorIndex = i+1;
                    int errorStatus = SNMPRequestException.VALUE_READ_ONLY;
                    throw new SNMPSetException("Trying to set a read-only variable!", errorIndex, errorStatus);
                }
                else if (pduType == SNMPBERCodec.SNMPGETREQUEST)
                {

                	int eth=0; // interfaz solicitada
                	int nIf=0; // numero de interfaces
                	long packets = 0; //packets
                 	String cmd="ls /sys/class/net/", line=""; // comando para listar las interfaces en el dispositivo.
					Process p=null;
					BufferedReader in2=null;
					ArrayList<String> arrIf = new ArrayList<String>();
					// Busco cuantas interfaces existen
					try {
						p = Runtime.getRuntime().exec(cmd);
						in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
						line="";
						
					    while ((line = in2.readLine()) != null) {  
					    	nIf += 1;
					    	arrIf.add(line);					    	
					    }
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
					// Pregunto cual interfaz que esta solicitando
					try {
						eth =  Integer.parseInt(snmpOID.toString().substring(21, snmpOID.toString().length()));
					}catch (NumberFormatException e) {
						// TODO: handle exception
						e.printStackTrace();
					}
                		
                	// si el numero de interfaz solicitada esta, busco el valor y lo envio
					if( 1 <= eth && eth <= nIf ){
	                    try
	                    {
	                    	cmd="cat /sys/class/net/"+arrIf.get(eth-1)+"/statistics/rx_missed\n";
	                    	p = Runtime.getRuntime().exec(cmd);
							in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
							packets = Long.parseLong(in2.readLine());
							
	                        SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(packets));
	                        responseList.addSNMPObject(newPair);
	                    }
	                    catch (SNMPBadValueException e)
	                    {
	                        // won't happen...
	                    }catch (Exception e)
	                    {
	                    	// TODO: handle exception	                    	
							e.printStackTrace();
	                    }
					}else{
						int errorIndex = i+1;
	                    int errorStatus = SNMPRequestException.VALUE_NOT_AVAILABLE;
	                    throw new SNMPSetException("Valor fuera de rango del número interfaces", errorIndex, errorStatus);						
					}
                	                	
                } 
                
            } // ifInUnknownProtos
            
            
            // ifOutOctets Counter32 | Read-Only | Mandatory | 1.3.6.1.2.1.2.2.1.16.X -> X es el numero de interfaz
            if (snmpOID.toString().startsWith("1.3.6.1.2.1.2.2.1.16."))
            {
                if (pduType == SNMPBERCodec.SNMPSETREQUEST)
                {
                	int errorIndex = i+1;
                    int errorStatus = SNMPRequestException.VALUE_READ_ONLY;
                    throw new SNMPSetException("Trying to set a read-only variable!", errorIndex, errorStatus);
                }
                else if (pduType == SNMPBERCodec.SNMPGETREQUEST)
                {

                	int eth=0; // interfaz solicitada
                	int nIf=0; // numero de interfaces
                	long octets = 0; //octets
                 	String cmd="ls /sys/class/net/", line=""; // comando para listar las interfaces en el dispositivo.
					Process p=null;
					BufferedReader in2=null;
					ArrayList<String> arrIf = new ArrayList<String>();
					// Busco cuantas interfaces existen
					try {
						p = Runtime.getRuntime().exec(cmd);
						in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
						line="";
						
					    while ((line = in2.readLine()) != null) {  
					    	nIf += 1;
					    	arrIf.add(line);					    	
					    }
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
					// Pregunto cual interfaz que esta solicitando
					try {
						eth =  Integer.parseInt(snmpOID.toString().substring(21, snmpOID.toString().length()));
					}catch (NumberFormatException e) {
						// TODO: handle exception
						e.printStackTrace();
					}
                		
                	// si el numero de interfaz solicitada esta, busco el valor y lo envio
					if( 1 <= eth && eth <= nIf ){
	                    try
	                    {
	                    	cmd="cat /sys/class/net/"+arrIf.get(eth-1)+"/statistics/tx_bytes\n";
	                    	p = Runtime.getRuntime().exec(cmd);
							in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
							octets = Long.parseLong(in2.readLine());
							
	                        SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(octets));
	                        responseList.addSNMPObject(newPair);
	                    }
	                    catch (SNMPBadValueException e)
	                    {
	                        // won't happen...
	                    }catch (Exception e)
	                    {
	                    	// TODO: handle exception	                    	
							e.printStackTrace();
	                    }
					}else{
						int errorIndex = i+1;
	                    int errorStatus = SNMPRequestException.VALUE_NOT_AVAILABLE;
	                    throw new SNMPSetException("Valor fuera de rango del número interfaces", errorIndex, errorStatus);						
					}
                	                	
                } 
                
            } // ifOutOctets
            
            
         // ifOutUcastPkts Counter32 | Read-Only | Mandatory | 1.3.6.1.2.1.2.2.1.17.X -> X es el numero de interfaz
            if (snmpOID.toString().startsWith("1.3.6.1.2.1.2.2.1.17."))
            {
                if (pduType == SNMPBERCodec.SNMPSETREQUEST)
                {
                	int errorIndex = i+1;
                    int errorStatus = SNMPRequestException.VALUE_READ_ONLY;
                    throw new SNMPSetException("Trying to set a read-only variable!", errorIndex, errorStatus);
                }
                else if (pduType == SNMPBERCodec.SNMPGETREQUEST)
                {

                	int eth=0; // interfaz solicitada
                	int nIf=0; // numero de interfaces
                	long packets = 0; //packets
                 	String cmd="ls /sys/class/net/", line=""; // comando para listar las interfaces en el dispositivo.
					Process p=null;
					BufferedReader in2=null;
					ArrayList<String> arrIf = new ArrayList<String>();
					// Busco cuantas interfaces existen
					try {
						p = Runtime.getRuntime().exec(cmd);
						in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
						line="";
						
					    while ((line = in2.readLine()) != null) {  
					    	nIf += 1;
					    	arrIf.add(line);					    	
					    }
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
					// Pregunto cual interfaz que esta solicitando
					try {
						eth =  Integer.parseInt(snmpOID.toString().substring(21, snmpOID.toString().length()));
					}catch (NumberFormatException e) {
						// TODO: handle exception
						e.printStackTrace();
					}
                		
                	// si el numero de interfaz solicitada esta, busco el valor y lo envio
					if( 1 <= eth && eth <= nIf ){
	                    try
	                    {
	                    	cmd="cat /sys/class/net/"+arrIf.get(eth-1)+"/statistics/tx_packets\n";
	                    	p = Runtime.getRuntime().exec(cmd);
							in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
							packets = Long.parseLong(in2.readLine());
							
	                        SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(packets));
	                        responseList.addSNMPObject(newPair);
	                    }
	                    catch (SNMPBadValueException e)
	                    {
	                        // won't happen...
	                    }catch (Exception e)
	                    {
	                    	// TODO: handle exception	                    	
							e.printStackTrace();
	                    }
					}else{
						int errorIndex = i+1;
	                    int errorStatus = SNMPRequestException.VALUE_NOT_AVAILABLE;
	                    throw new SNMPSetException("Valor fuera de rango del número interfaces", errorIndex, errorStatus);						
					}
                	                	
                } 
                
            } // ifOutUcastPkts
            
            // ifOutNUcastPkts Counter32 | Read-Only | Mandatory | 1.3.6.1.2.1.2.2.1.18.X -> X es el numero de interfaz
            if (snmpOID.toString().startsWith("1.3.6.1.2.1.2.2.1.18."))
            {
                if (pduType == SNMPBERCodec.SNMPSETREQUEST)
                {
                	int errorIndex = i+1;
                    int errorStatus = SNMPRequestException.VALUE_READ_ONLY;
                    throw new SNMPSetException("Trying to set a read-only variable!", errorIndex, errorStatus);
                }
                else if (pduType == SNMPBERCodec.SNMPGETREQUEST)
                {

                	int eth=0; // interfaz solicitada
                	int nIf=0; // numero de interfaces
                	long packets = 0; //packets
                 	String cmd="ls /sys/class/net/", line=""; // comando para listar las interfaces en el dispositivo.
					Process p=null;
					BufferedReader in2=null;
					ArrayList<String> arrIf = new ArrayList<String>();
					// Busco cuantas interfaces existen
					try {
						p = Runtime.getRuntime().exec(cmd);
						in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
						line="";
						
					    while ((line = in2.readLine()) != null) {  
					    	nIf += 1;
					    	arrIf.add(line);					    	
					    }
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
					// Pregunto cual interfaz que esta solicitando
					try {
						eth =  Integer.parseInt(snmpOID.toString().substring(21, snmpOID.toString().length()));
					}catch (NumberFormatException e) {
						// TODO: handle exception
						e.printStackTrace();
					}
                		
                	// si el numero de interfaz solicitada esta, busco el valor y lo envio
					if( 1 <= eth && eth <= nIf ){
	                    try
	                    {
	                    	cmd="cat /sys/class/net/"+arrIf.get(eth-1)+"/statistics/OUTMULTICAST\n";
	                    	p = Runtime.getRuntime().exec(cmd);
							in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
							packets = Long.parseLong(in2.readLine());
							
	                        SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(packets));
	                        responseList.addSNMPObject(newPair);
	                    }
	                    catch (SNMPBadValueException e)
	                    {
	                        // won't happen...
	                    }catch (Exception e)
	                    {
	                    	// TODO: handle exception	                    	
							e.printStackTrace();
	                    }
					}else{
						int errorIndex = i+1;
	                    int errorStatus = SNMPRequestException.VALUE_NOT_AVAILABLE;
	                    throw new SNMPSetException("Valor fuera de rango del número interfaces", errorIndex, errorStatus);						
					}
                	                	
                } 
                
            } // ifOutNUcastPkts
            
            
         // ifOutDiscards Counter32 | Read-Only | Mandatory | 1.3.6.1.2.1.2.2.1.19.X -> X es el numero de interfaz
            if (snmpOID.toString().startsWith("1.3.6.1.2.1.2.2.1.19."))
            {
                if (pduType == SNMPBERCodec.SNMPSETREQUEST)
                {
                	int errorIndex = i+1;
                    int errorStatus = SNMPRequestException.VALUE_READ_ONLY;
                    throw new SNMPSetException("Trying to set a read-only variable!", errorIndex, errorStatus);
                }
                else if (pduType == SNMPBERCodec.SNMPGETREQUEST)
                {

                	int eth=0; // interfaz solicitada
                	int nIf=0; // numero de interfaces
                	long packets = 0; //packets
                 	String cmd="ls /sys/class/net/", line=""; // comando para listar las interfaces en el dispositivo.
					Process p=null;
					BufferedReader in2=null;
					ArrayList<String> arrIf = new ArrayList<String>();
					// Busco cuantas interfaces existen
					try {
						p = Runtime.getRuntime().exec(cmd);
						in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
						line="";
						
					    while ((line = in2.readLine()) != null) {  
					    	nIf += 1;
					    	arrIf.add(line);					    	
					    }
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
					// Pregunto cual interfaz que esta solicitando
					try {
						eth =  Integer.parseInt(snmpOID.toString().substring(21, snmpOID.toString().length()));
					}catch (NumberFormatException e) {
						// TODO: handle exception
						e.printStackTrace();
					}
                		
                	// si el numero de interfaz solicitada esta, busco el valor y lo envio
					if( 1 <= eth && eth <= nIf ){
	                    try
	                    {
	                    	cmd="cat /sys/class/net/"+arrIf.get(eth-1)+"/statistics/tx_dropped\n";
	                    	p = Runtime.getRuntime().exec(cmd);
							in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
							packets = Long.parseLong(in2.readLine());
							
	                        SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(packets));
	                        responseList.addSNMPObject(newPair);
	                    }
	                    catch (SNMPBadValueException e)
	                    {
	                        // won't happen...
	                    }catch (Exception e)
	                    {
	                    	// TODO: handle exception	                    	
							e.printStackTrace();
	                    }
					}else{
						int errorIndex = i+1;
	                    int errorStatus = SNMPRequestException.VALUE_NOT_AVAILABLE;
	                    throw new SNMPSetException("Valor fuera de rango del número interfaces", errorIndex, errorStatus);						
					}
                	                	
                } 
                
            } // ifOutDiscards
            
            // ifOutErrors Counter32 | Read-Only | Mandatory | 1.3.6.1.2.1.2.2.1.20.X -> X es el numero de interfaz
            if (snmpOID.toString().startsWith("1.3.6.1.2.1.2.2.1.20."))
            {
                if (pduType == SNMPBERCodec.SNMPSETREQUEST)
                {
                	int errorIndex = i+1;
                    int errorStatus = SNMPRequestException.VALUE_READ_ONLY;
                    throw new SNMPSetException("Trying to set a read-only variable!", errorIndex, errorStatus);
                }
                else if (pduType == SNMPBERCodec.SNMPGETREQUEST)
                {

                	int eth=0; // interfaz solicitada
                	int nIf=0; // numero de interfaces
                	long packets = 0; //packets
                 	String cmd="ls /sys/class/net/", line=""; // comando para listar las interfaces en el dispositivo.
					Process p=null;
					BufferedReader in2=null;
					ArrayList<String> arrIf = new ArrayList<String>();
					// Busco cuantas interfaces existen
					try {
						p = Runtime.getRuntime().exec(cmd);
						in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
						line="";
						
					    while ((line = in2.readLine()) != null) {  
					    	nIf += 1;
					    	arrIf.add(line);					    	
					    }
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
					// Pregunto cual interfaz que esta solicitando
					try {
						eth =  Integer.parseInt(snmpOID.toString().substring(21, snmpOID.toString().length()));
					}catch (NumberFormatException e) {
						// TODO: handle exception
						e.printStackTrace();
					}
                		
                	// si el numero de interfaz solicitada esta, busco el valor y lo envio
					if( 1 <= eth && eth <= nIf ){
	                    try
	                    {
	                    	cmd="cat /sys/class/net/"+arrIf.get(eth-1)+"/statistics/tx_errors\n";
	                    	p = Runtime.getRuntime().exec(cmd);
							in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
							packets = Long.parseLong(in2.readLine());
							
	                        SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(packets));
	                        responseList.addSNMPObject(newPair);
	                    }
	                    catch (SNMPBadValueException e)
	                    {
	                        // won't happen...
	                    }catch (Exception e)
	                    {
	                    	// TODO: handle exception	                    	
							e.printStackTrace();
	                    }
					}else{
						int errorIndex = i+1;
	                    int errorStatus = SNMPRequestException.VALUE_NOT_AVAILABLE;
	                    throw new SNMPSetException("Valor fuera de rango del número interfaces", errorIndex, errorStatus);						
					}
                	                	
                } 
                
            } // ifOutErrors
            
            
            // ifOutQLen Counter32 | Read-Only | Mandatory | 1.3.6.1.2.1.2.2.1.20.X -> X es el numero de interfaz
            if (snmpOID.toString().startsWith("1.3.6.1.2.1.2.2.1.20."))
            {
                if (pduType == SNMPBERCodec.SNMPSETREQUEST)
                {
                	int errorIndex = i+1;
                    int errorStatus = SNMPRequestException.VALUE_READ_ONLY;
                    throw new SNMPSetException("Trying to set a read-only variable!", errorIndex, errorStatus);
                }
                else if (pduType == SNMPBERCodec.SNMPGETREQUEST)
                {

                	int eth=0; // interfaz solicitada
                	int nIf=0; // numero de interfaces
                	long qLen = 0; //Queue Length
                 	String cmd="ls /sys/class/net/", line=""; // comando para listar las interfaces en el dispositivo.
					Process p=null;
					BufferedReader in2=null;
					ArrayList<String> arrIf = new ArrayList<String>();
					// Busco cuantas interfaces existen
					try {
						p = Runtime.getRuntime().exec(cmd);
						in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
						line="";
						
					    while ((line = in2.readLine()) != null) {  
					    	nIf += 1;
					    	arrIf.add(line);					    	
					    }
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
					
					// Pregunto cual interfaz que esta solicitando
					try {
						eth =  Integer.parseInt(snmpOID.toString().substring(21, snmpOID.toString().length()));
					}catch (NumberFormatException e) {
						// TODO: handle exception
						e.printStackTrace();
					}
                		
                	// si el numero de interfaz solicitada esta, busco el valor y lo envio
					if( 1 <= eth && eth <= nIf ){
	                    try
	                    {
	                    	cmd="cat /sys/class/net/"+arrIf.get(eth-1)+"/tx_queue_len\n";
	                    	p = Runtime.getRuntime().exec(cmd);
							in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
							qLen = Long.parseLong(in2.readLine());
							
	                        SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPGauge32(qLen));
	                        responseList.addSNMPObject(newPair);
	                    }
	                    catch (SNMPBadValueException e)
	                    {
	                        // won't happen...
	                    }catch (Exception e)
	                    {
	                    	// TODO: handle exception	                    	
							e.printStackTrace();
	                    }
					}else{
						int errorIndex = i+1;
	                    int errorStatus = SNMPRequestException.VALUE_NOT_AVAILABLE;
	                    throw new SNMPSetException("Valor fuera de rango del número interfaces", errorIndex, errorStatus);						
					}
                	                	
                } 
                
            } // ifOutQLen
            
            
            /******************************
            *
            *   MIB-II | the Address Translation Group
            * 
            ******************************/
            
            /******************************
            *
            *   MIB-II | the IP Group
            * 
            ******************************/
            
            // ipForwarding | Integer | Read-Write | Mandatory |
            if (snmpOID.toString().equals("1.3.6.1.2.1.4.1.0"))
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
                	String cmd="cat /proc/sys/net/ipv4/ip_forward\n";
                	int forward = 0; // 0 false | 1 true -> forwarding
                	Process p=null;
					BufferedReader in2=null;

                	try
                    {
                		p = Runtime.getRuntime().exec(cmd);
						in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
						forward=Integer.parseInt(in2.readLine());					    	
					    
                        SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPInteger(forward));
                        //SNMPVariablePair newPair = new SNMPVariablePair(snmpOID, new SNMPOctetString("Boo"));
                        responseList.addSNMPObject(newPair);
                    }
                    catch (SNMPBadValueException e)
                    {
                        // won't happen...
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                } 
                
            } // ipForwarding
            
         // ipDefaultTTL | Integer | Read-Write | Mandatory |
            if (snmpOID.toString().equals("1.3.6.1.2.1.4.2.0"))
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
                	String cmd="cat /proc/sys/net/ipv4/ip_default_ttl\n";
                	int ttl = 0; // deafult_ttl
                	Process p=null;
					BufferedReader in2=null;

                	try
                    {
                		p = Runtime.getRuntime().exec(cmd);
						in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
						ttl=Integer.parseInt(in2.readLine());					    	
					    
                        SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPInteger(ttl));
                        //SNMPVariablePair newPair = new SNMPVariablePair(snmpOID, new SNMPOctetString("Boo"));
                        responseList.addSNMPObject(newPair);
                    }
                    catch (SNMPBadValueException e)
                    {
                        // won't happen...
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                } 
                
            } // ipDefaultTTL
            
            
            // PRUEBAAA 
            if (snmpOID.toString().equals("1.3.6.1.2.1.4.3.0"))
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
                	String cmd="cat /proc/net/snmp\n";
                	String line = ""; // deafult_ttl
                	Process p=null;
					BufferedReader in2=null;

                	try
                    {
                		p = Runtime.getRuntime().exec(cmd);
						in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
						
						while ((line =in2.readLine()) != null) {
							System.out.println(line);
						}
						
                        //SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPInteger(ttl));
                        //SNMPVariablePair newPair = new SNMPVariablePair(snmpOID, new SNMPOctetString("Boo"));
                        //responseList.addSNMPObject(newPair);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                } 
                
            } // PRUEBAAA 
            
            
            
            
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
