package ucv.tesis.tesisandroid;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import snmp.SNMPBERCodec;
import snmp.SNMPBadValueException;
import snmp.SNMPCounter32;
import snmp.SNMPGauge32;
import snmp.SNMPGetException;
import snmp.SNMPIPAddress;
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
import snmp.SNMPv2BulkRequestPDU;
import ucv.tesis.tesisandroid.DBOIDHelper.ObjIdent;

public class Mibs implements SNMPRequestListener {

	
	SNMPOctetString storedSNMPValue;
	Context contextActivity = null;
	String communityRO = "public";
	String communityRW = "public";
	
	public Mibs(Context cont, String commRO, String commRW){
		this.contextActivity = cont;
		this.communityRO = commRO;
		this.communityRW = commRW;
	}
	
	/**
	 * 
	 * Para enviar cadenas con tilde setear la variable sendWithoutTittle = false
	 * por defecto sendWithoutTittle = true
	 * 
	 */
	
	@SuppressLint("NewApi")
	@Override
	public SNMPSequence processRequest(SNMPPDU pdu, String communityName)
			throws SNMPGetException, SNMPSetException {
		
		boolean sendWithoutTittle = true;
	    byte pduType = pdu.getPDUType();
        
        SNMPSequence varBindList = pdu.getVarBindList();
        SNMPSequence responseList = new SNMPSequence();
        
        int errorIndex = 0;
    	int errorStatus = 0;
    	
        for (int i = 0; i < varBindList.size(); i++)
        {
            SNMPSequence variablePair = (SNMPSequence)varBindList.getSNMPObjectAt(i);
            SNMPObjectIdentifier snmpOID = (SNMPObjectIdentifier)variablePair.getSNMPObjectAt(0);
            SNMPObject snmpValue = (SNMPObject)variablePair.getSNMPObjectAt(1);
            
            if (pduType == SNMPBERCodec.SNMPSETREQUEST && communityName.equalsIgnoreCase(this.communityRW))
            {
            	
            	/* sysContact DisplayString | Read-Write | Mandatory */
            	if (snmpOID.toString().equals("1.3.6.1.2.1.1.4.0"))
                {
                    if (snmpValue instanceof SNMPOctetString)
                    {
                        storedSNMPValue = (SNMPOctetString)snmpValue;
                        
                        try
                        {
                        	DBOIDHelper database = new DBOIDHelper(this.contextActivity);
                        		database.update(snmpOID.toString(), storedSNMPValue.toString() );
                        	database.cleanup();
                            SNMPVariablePair newPair = new SNMPVariablePair(snmpOID, storedSNMPValue);
                            responseList.addSNMPObject(newPair);
                        }
                        catch (SNMPBadValueException e)
                        {
                        	errorIndex = i+1;
                        	errorStatus = SNMPRequestException.FAILED;
                        }
                    }
                    else
                    {
                        errorIndex = i+1;
                        errorStatus = SNMPRequestException.BAD_VALUE;
                        throw new SNMPSetException("Supplied value must be SNMPOctetString", errorIndex, errorStatus);
                    }
                    
                    continue;
                } // sysContact
            	
            	/* sysName DisplayString | Read-Write | Mandatory */
            	else if (snmpOID.toString().equals("1.3.6.1.2.1.1.5.0"))
                {
                    if (snmpValue instanceof SNMPOctetString)
                    {
                        storedSNMPValue = (SNMPOctetString)snmpValue;
                        
                        try
                        {
                        	DBOIDHelper database = new DBOIDHelper(this.contextActivity);
                        		database.update(snmpOID.toString(), storedSNMPValue.toString() );
                        	database.cleanup();
                            SNMPVariablePair newPair = new SNMPVariablePair(snmpOID, storedSNMPValue);
                            responseList.addSNMPObject(newPair);
                        }
                        catch (SNMPBadValueException e)
                        {
                        	errorIndex = i+1;
                        	errorStatus = SNMPRequestException.FAILED;
                        }
                    }
                    else
                    {
                        errorIndex = i+1;
                        errorStatus = SNMPRequestException.BAD_VALUE;
                        throw new SNMPSetException("Supplied value must be SNMPOctetString", errorIndex, errorStatus);
                    }
                    
                    continue;
                } //sysName
                
                /* sysLocation DisplayString | Read-Write | Mandatory */
            	else if (snmpOID.toString().equals("1.3.6.1.2.1.1.6.0"))
                {
                    if (snmpValue instanceof SNMPOctetString)
                    {
                        storedSNMPValue = (SNMPOctetString)snmpValue;
                        
                        try
                        {
                        	DBOIDHelper database = new DBOIDHelper(this.contextActivity);
                        		database.update(snmpOID.toString(), storedSNMPValue.toString() );
                        	database.cleanup();
                            SNMPVariablePair newPair = new SNMPVariablePair(snmpOID, storedSNMPValue);
                            responseList.addSNMPObject(newPair);
                        }
                        catch (SNMPBadValueException e)
                        {
                        	errorIndex = i+1;
                        	errorStatus = SNMPRequestException.FAILED;
                        }
                    }
                    
                    continue;
                } // sysLocation
            	
                // ifAdminStatus Integer | Read-Only | Mandatory | 1.3.6.1.2.1.2.2.1.7.X -> X es el numero de interfaz
            	else if (snmpOID.toString().startsWith("1.3.6.1.2.1.2.2.1.7."))
                {
                	 if (snmpValue instanceof SNMPInteger)
    	             {
	                     try
                         {
                         	DBOIDHelper database = new DBOIDHelper(this.contextActivity);
                         		database.update(snmpOID.toString(), ""+snmpValue );
                         	database.cleanup();
                            SNMPVariablePair newPair = new SNMPVariablePair(snmpOID, snmpValue);
                            responseList.addSNMPObject(newPair);
                         }
                         catch (SNMPBadValueException e){
                         	errorIndex = i+1;
                         	errorStatus = SNMPRequestException.FAILED;
                         }
                     }
                     else
                     {
                         errorIndex = i+1;
                         errorStatus = SNMPRequestException.BAD_VALUE;
                         throw new SNMPSetException("Supplied value must be SNMPInteger", errorIndex, errorStatus);
                     }
                	 
                	 continue;
                } //ifAdminStatus
            	else{  // Los otros OID son READ-ONLY
                    errorIndex = i+1;
                    errorStatus = SNMPRequestException.VALUE_READ_ONLY;
                    throw new SNMPSetException("Trying to set a read-only variable!", errorIndex, errorStatus);
                }
                
              // COMUNIDAD MALA.   
            } else if (pduType == SNMPBERCodec.SNMPSETREQUEST && !communityName.equalsIgnoreCase(this.communityRW))
            {
            	errorIndex = 0;
            	errorStatus = SNMPRequestException.FAILED;
                throw new SNMPSetException("Trying to set OID value with community name bad.!", errorIndex, errorStatus);
            }// pduType == SNMPBERCodec.SNMPSETREQUEST
            
            
            
            
            
            
            
            
            
            
            
            
            
            if (pduType == SNMPBERCodec.SNMPGETREQUEST)
            {
            	if(!(communityName.equalsIgnoreCase(this.communityRW) || communityName.equalsIgnoreCase(this.communityRO))){
            		errorIndex = 0;
                	errorStatus = SNMPRequestException.FAILED;
                    throw new SNMPSetException("Name community bad!", errorIndex, errorStatus);
                }
            
		            		/************************************
				            *	          						*
				            *     MIB-II | the System Group     *
				            * 									*
				            *************************************/
            	
            if(snmpOID.toString().startsWith("1.3.6.1.2.1.1"))
            {	
            		
            	/* sysDescr  DisplayString | Read-Only | Mandatory */
                if (snmpOID.toString().equals("1.3.6.1.2.1.1.1.0") || snmpOID.toString().equals("1.3.6.1.2.1.1.1"))
                {   
                	
                    try
                    {
                    	DBOIDHelper database = new DBOIDHelper(this.contextActivity);
                    		ObjIdent oid = database.getOID("1.3.6.1.2.1.1.1.0");
                    	database.cleanup();
                    	SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPOctetString(oid.getValue()));
                        responseList.addSNMPObject(newPair);
                    }
                    catch (SNMPBadValueException e)
                    {
                    	errorIndex = i+1;
                    	errorStatus = SNMPRequestException.FAILED;
                    }
                    
                    continue;   
                } // sysDescr
                
                /* sysObjectID   DisplayString | Read-Only | Mandatory */
                if (snmpOID.toString().equals("1.3.6.1.2.1.1.2.0") || snmpOID.toString().equals("1.3.6.1.2.1.1.2"))
                {
                    try
                    {
                    	DBOIDHelper database = new DBOIDHelper(this.contextActivity);
                    		ObjIdent oid = database.getOID("1.3.6.1.2.1.1.2.0");
                    	database.cleanup();
                    	SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPOctetString(oid.getValue()));
                        responseList.addSNMPObject(newPair);
                    }
                    catch (SNMPBadValueException e)
                    {
                    	errorIndex = i+1;
                    	errorStatus = SNMPRequestException.FAILED;
                    }
                     
                    continue;   
                } // sysObjectID
                
                /* sysUpTime Integer | Read-Only | Mandatory */
                if (snmpOID.toString().equals("1.3.6.1.2.1.1.3.0") || snmpOID.toString().equals("1.3.6.1.2.1.1.3"))
                {
                    try
                    {
                    	DBOIDHelper database = new DBOIDHelper(this.contextActivity);
                    		ObjIdent oid = database.getOID("1.3.6.1.2.1.1.3.0");
                    	database.cleanup();
            	    	Long timeInit = Long.valueOf(oid.getValue());
            	    	Long timeNow = System.currentTimeMillis();
            	    	Long date = null;
            			if(timeInit > 0){
            				date = timeNow - timeInit;
            			}
                    	SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPInteger(date));
                        responseList.addSNMPObject(newPair);
                    }
                    catch (SNMPBadValueException e)
                    {
                    	errorIndex = i+1;
                    	errorStatus = SNMPRequestException.FAILED;
                    }
                     
                    continue;   
                } // sysUpTime 
                
                /* sysContact DisplayString | Read-Write | Mandatory */
                if (snmpOID.toString().equals("1.3.6.1.2.1.1.4.0") || snmpOID.toString().equals("1.3.6.1.2.1.1.4"))
                {
                    try
                    {
                    	DBOIDHelper database = new DBOIDHelper(this.contextActivity);
                    		ObjIdent oid = database.getOID("1.3.6.1.2.1.1.4.0");
                    	database.cleanup();
                        SNMPVariablePair newPair = new SNMPVariablePair(snmpOID, new SNMPOctetString(oid.getValue()));
                        responseList.addSNMPObject(newPair);
                    }
                    catch (SNMPBadValueException e)
                    {
                    	errorIndex = i+1;
                    	errorStatus = SNMPRequestException.FAILED;
                    }
                     
                    continue;
                } // sysContact
                
                /* sysName DisplayString | Read-Write | Mandatory */
                if (snmpOID.toString().equals("1.3.6.1.2.1.1.5.0") || snmpOID.toString().equals("1.3.6.1.2.1.1.5"))
                {
                    try
                    {
                    	DBOIDHelper database = new DBOIDHelper(this.contextActivity);
                    		ObjIdent oid = database.getOID("1.3.6.1.2.1.1.5.0");
                    	database.cleanup();
                        SNMPVariablePair newPair = new SNMPVariablePair(snmpOID, new SNMPOctetString(oid.getValue()));
                        responseList.addSNMPObject(newPair);
                    }
                    catch (SNMPBadValueException e)
                    {
                    	errorIndex = i+1;
                    	errorStatus = SNMPRequestException.FAILED;
                    }
                     
                    continue;
                } // sysName 
                
                /* sysLocation DisplayString | Read-Write | Mandatory */
                if (snmpOID.toString().equals("1.3.6.1.2.1.1.6.0") || snmpOID.toString().equals("1.3.6.1.2.1.1.6"))
                {
                    if (pduType == SNMPBERCodec.SNMPGETREQUEST)
                    {
                        try
                        {
                        	DBOIDHelper database = new DBOIDHelper(this.contextActivity);
                        		ObjIdent oid = database.getOID("1.3.6.1.2.1.1.6.0");
                        	database.cleanup();
                            SNMPVariablePair newPair = new SNMPVariablePair(snmpOID, new SNMPOctetString(oid.getValue()));
                            responseList.addSNMPObject(newPair);
                        }
                        catch (SNMPBadValueException e)
                        {
                        	errorIndex = i+1;
                        	errorStatus = SNMPRequestException.FAILED;
                        }
                    } 
                    continue;
                } // sysLocation
                
                /* sysServices   DisplayString | Read-Only | Mandatory */
                if (snmpOID.toString().equals("1.3.6.1.2.1.1.7.0") || snmpOID.toString().equals("1.3.6.1.2.1.1.7"))
                {
                    try
                    {
                    	DBOIDHelper database = new DBOIDHelper(this.contextActivity);
                    		ObjIdent oid = database.getOID("1.3.6.1.2.1.1.7.0");
                    	database.cleanup();
                    	SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPOctetString(oid.getValue()));
                        responseList.addSNMPObject(newPair);
                    }
                    catch (SNMPBadValueException e)
                    {
                    	errorIndex = i+1;
                    	errorStatus = SNMPRequestException.FAILED;
                    }
                     
                    continue;   
                } // sysServices 
                
            }// FIN System
            
				            	/************************************
					            *	          						*
					            *   MIB-II | the Interfaces Group   *
					            * 									*
					            *************************************/

            if(snmpOID.toString().startsWith("1.3.6.1.2.1.2"))
            {
            	// ifNumber Integer | Read-Only | Mandatory 
	            if (snmpOID.toString().equals("1.3.6.1.2.1.2.1.0") || snmpOID.toString().equals("1.3.6.1.2.1.2.1"))
	            {
	                int nIf = 0; // numero de interfaces
	            	ArrayList<String> arrIf = new ArrayList<String>();
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
	                    } catch (Exception e) {
					    	errorIndex = i+1;
	                    	errorStatus = SNMPRequestException.FAILED;
						}
	                    
	                    SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPInteger(nIf));
	                    responseList.addSNMPObject(newPair);
	                }
	                catch (SNMPBadValueException e)
	                {
	                	errorIndex = i+1;
	                	errorStatus = SNMPRequestException.FAILED;
	                }
	                 
	                continue;    
	            } //ifNumber
	            
	            // ifIndex Integer | Read-Only | Mandatory | 1.3.6.1.2.1.2.2.1.1.X -> es el numero de interfaz
	            if (snmpOID.toString().startsWith("1.3.6.1.2.1.2.2.1.1."))
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
						errorIndex = i+1;
	                	errorStatus = SNMPRequestException.FAILED;
					}
					// Pregunto cual interfaz esta solicitando
					try {
						eth =  Integer.parseInt(snmpOID.toString().substring(20, snmpOID.toString().length()));
					} catch (NumberFormatException e) {
						errorIndex = i+1;
	                	errorStatus = SNMPRequestException.FAILED;
					}	
	            	// si el numero de interfaz solicitada esta, busco el valor y lo envio
					if( 1 <= eth && eth <= nIf ){
	                    try
	                    {
	                        SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPInteger(eth));
	                        responseList.addSNMPObject(newPair);
	                    }
	                    catch (SNMPBadValueException e)
	                    {
	                    	errorIndex = i+1;
		                	errorStatus = SNMPRequestException.FAILED;
	                    }
					}else{
						errorIndex = i+1;
	                	errorStatus = SNMPRequestException.VALUE_NOT_AVAILABLE;
	                    throw new SNMPSetException("Value out of range of the number interfaces", errorIndex, errorStatus);						
					}
	 
	                continue;
	            } // ifIndex
	            
	            // ifDescr DisplayString | Read-Only | Mandatory | 1.3.6.1.2.1.2.2.1.2.X -> X es el numero de interfaz
	            if (snmpOID.toString().startsWith("1.3.6.1.2.1.2.2.1.2."))
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
						errorIndex = i+1;
	                	errorStatus = SNMPRequestException.FAILED;
					}
					// Pregunto cual interfaz que esta solicitando
					try {
						eth =  Integer.parseInt(snmpOID.toString().substring(20, snmpOID.toString().length()));
					} catch (NumberFormatException e) {
						errorIndex = i+1;
	                	errorStatus = SNMPRequestException.FAILED;
					}
	            	// si el numero de interfaz solicitada esta busco el valor y lo envio
					if( 1 <= eth && eth <= nIf ){
	                    try{
	                        SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPOctetString(arrIf.get(eth-1).toString()));
	                        responseList.addSNMPObject(newPair);
	                    }catch (SNMPBadValueException e){
	                    	errorIndex = i+1;
		                	errorStatus = SNMPRequestException.FAILED;
	                    }
					}else{
						errorIndex = i+1;
	                    errorStatus = SNMPRequestException.VALUE_NOT_AVAILABLE;
	                    throw new SNMPSetException("Value out of range of the number interfaces", errorIndex, errorStatus);						
					}
	                
					continue;
	            } // ifDescr
	            
	            // ifType Integer | Read-Only | Mandatory | 1.3.6.1.2.1.2.2.1.3.X -> X es el numero de interfaz
	            if (snmpOID.toString().startsWith("1.3.6.1.2.1.2.2.1.3."))
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
						errorIndex = i+1;
	                	errorStatus = SNMPRequestException.FAILED;
					}
					// Pregunto cual interfaz que esta solicitando
					try {
						eth =  Integer.parseInt(snmpOID.toString().substring(20, snmpOID.toString().length()));
					} catch (NumberFormatException e) {
						errorIndex = i+1;
	                	errorStatus = SNMPRequestException.FAILED;
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
	                    catch (SNMPBadValueException e){
	                    	errorIndex = i+1;
		                	errorStatus = SNMPRequestException.FAILED;
	                    }catch (Exception e){
	                    	errorIndex = i+1;
		                	errorStatus = SNMPRequestException.FAILED;
	                    }
					}else{
						errorIndex = i+1;
	                    errorStatus = SNMPRequestException.VALUE_NOT_AVAILABLE;
	                    throw new SNMPSetException("Value out of range of the number interfaces", errorIndex, errorStatus);						
					}
	                
	                continue;
	            } // ifType
	            
	         // ifMtu Integer | Read-Only | Mandatory | 1.3.6.1.2.1.2.2.1.4.X -> X es el numero de interfaz
	            if (snmpOID.toString().startsWith("1.3.6.1.2.1.2.2.1.4."))
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
						errorIndex = i+1;
	                	errorStatus = SNMPRequestException.FAILED;
					}
					// Pregunto cual interfaz que esta solicitando
					try {
						eth =  Integer.parseInt(snmpOID.toString().substring(20, snmpOID.toString().length()));
					}catch (NumberFormatException e) {
						errorIndex = i+1;
	                	errorStatus = SNMPRequestException.FAILED;
					}
					// si el numero de interfaz solicitada esta, busco el valor y lo envio
					if( 1 <= eth && eth <= nIf ){
	                    try
	                    {
	                    	File archivo = new File ("/sys/class/net/"+arrIf.get(eth-1)+"/mtu");
							FileReader fileReader = new FileReader (archivo);
							BufferedReader buffReader = new BufferedReader(fileReader);
							line=buffReader.readLine();
							if( null != fileReader)
								fileReader.close();
							
							mtu = Integer.parseInt(line);
							SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPInteger(mtu));
							responseList.addSNMPObject(newPair);
	                    }catch (Exception e){
	                    	errorIndex = i+1;
		                	errorStatus = SNMPRequestException.FAILED;
	                    }
					}else{
						errorIndex = i+1;
	                    errorStatus = SNMPRequestException.VALUE_NOT_AVAILABLE;
	                    throw new SNMPSetException("Value out of range of the number interfaces", errorIndex, errorStatus);						
					}
					
	                continue;
	            } // ifMtu
	            
	            // ifSpeed Gauge | Read-Only | Mandatory | 1.3.6.1.2.1.2.2.1.5.X -> X es el numero de interfaz
	            if (snmpOID.toString().startsWith("1.3.6.1.2.1.2.2.1.5."))
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
						errorIndex = i+1;
	                	errorStatus = SNMPRequestException.FAILED;
					}
					
					// Pregunto cual interfaz que esta solicitando
					try {
						eth =  Integer.parseInt(snmpOID.toString().substring(20, snmpOID.toString().length()));
					}catch (NumberFormatException e) {
						errorIndex = i+1;
	                	errorStatus = SNMPRequestException.FAILED;
					}
	            		
	            	// si el numero de interfaz solicitada esta, busco el valor y lo envio
					if( 1 <= eth && eth <= nIf ){
	                    try
	                    {
	                    	/*File archivo = new File ("/sys/class/net/"+arrIf.get(eth-1)+"/speed");
							FileReader fileReader = new FileReader (archivo);
							BufferedReader buffReader = new BufferedReader(fileReader);
							line=buffReader.readLine();
							if( null != fileReader)
								fileReader.close();
							if(line == null) speed = 0;
							else speed = Integer.parseInt(line);
	                    	*/
	                        SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPGauge32(speed));
	                        responseList.addSNMPObject(newPair);
	                    }catch (Exception e){
	                    	errorIndex = i+1;
		                	errorStatus = SNMPRequestException.FAILED;
		                }
					}else{
						errorIndex = i+1;
	                    errorStatus = SNMPRequestException.VALUE_NOT_AVAILABLE;
	                    throw new SNMPSetException("Value out of range of the number interfaces", errorIndex, errorStatus);						
					}
	                
					continue;
	            } // ifSpeed
	            
	         // ifPhysAddress PhysAddress | Read-Only | Mandatory | 1.3.6.1.2.1.2.2.1.6.X -> X es el numero de interfaz
	            if (snmpOID.toString().startsWith("1.3.6.1.2.1.2.2.1.6."))
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
						errorIndex = i+1;
	                	errorStatus = SNMPRequestException.FAILED;
					}
					
					// Pregunto cual interfaz que esta solicitando
					try {
						eth =  Integer.parseInt(snmpOID.toString().substring(20, snmpOID.toString().length()));
					}catch (NumberFormatException e) {
						errorIndex = i+1;
	                	errorStatus = SNMPRequestException.FAILED;
					}
	            		
	            	// si el numero de interfaz solicitada esta, busco el valor y lo envio
					if( 1 <= eth && eth <= nIf ){
	                    try
	                    {
	                    	File archivo = new File ("/sys/class/net/"+arrIf.get(eth-1)+"/address");
							FileReader fileReader = new FileReader (archivo);
							BufferedReader buffReader = new BufferedReader(fileReader);
							address=buffReader.readLine();
							if( null != fileReader)
								fileReader.close();
							
	                        SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPOctetString(address));
	                        responseList.addSNMPObject(newPair);
	                    }catch (Exception e){
	                    	errorIndex = i+1;
		                	errorStatus = SNMPRequestException.FAILED;
	                    }
					}else{
						errorIndex = i+1;
	                    errorStatus = SNMPRequestException.VALUE_NOT_AVAILABLE;
	                    throw new SNMPSetException("Value out of range of the number interfaces", errorIndex, errorStatus);						
					}
	               
	                continue;
	            } // ifPhysAddress
	            
	            // ifAdminStatus Integer | Read-Only | Mandatory | 1.3.6.1.2.1.2.2.1.7.X -> X es el numero de interfaz
	            if (snmpOID.toString().startsWith("1.3.6.1.2.1.2.2.1.7."))
	            {
		            int eth=0; // interfaz solicitada
	            	int nIf=0; // numero de interfaces
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
						errorIndex = i+1;
	                	errorStatus = SNMPRequestException.FAILED;
					}
					
					// Pregunto cual interfaz que esta solicitando
					try {
						eth =  Integer.parseInt(snmpOID.toString().substring(20, snmpOID.toString().length()));
					}catch (NumberFormatException e) {
						errorIndex = i+1;
	                	errorStatus = SNMPRequestException.FAILED;
					}
	            		
	            	// si el numero de interfaz solicitada esta, busco el valor y lo envio
					if( 1 <= eth && eth <= nIf ){
	                    try
	                    {
	                    	DBOIDHelper database = new DBOIDHelper(this.contextActivity);
	                    	ObjIdent oid = database.getOID(snmpOID.toString());
	                    	database.cleanup();
	                    	
	                        SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPInteger(Integer.parseInt(oid.getValue())));
	                        responseList.addSNMPObject(newPair);
	                    }catch (Exception e)
	                    {
	                    	errorIndex = i+1;
		                	errorStatus = SNMPRequestException.FAILED;
	                    }
					}else{
						errorIndex = i+1;
	                    errorStatus = SNMPRequestException.VALUE_NOT_AVAILABLE;
	                    throw new SNMPSetException("Value out of range of the number interfaces", errorIndex, errorStatus);						
					}
	                	                	
	                
	                continue;
	            } // ifAdminStatus
	            
	            // ifOperStatus Integer | Read-Only | Mandatory | 1.3.6.1.2.1.2.2.1.8.X -> X es el numero de interfaz
	            if (snmpOID.toString().startsWith("1.3.6.1.2.1.2.2.1.8."))
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
						errorIndex = i+1;
	                	errorStatus = SNMPRequestException.FAILED;
					}
					
					// Pregunto cual interfaz que esta solicitando
					try {
						eth =  Integer.parseInt(snmpOID.toString().substring(20, snmpOID.toString().length()));
					}catch (NumberFormatException e) {
						errorIndex = i+1;
	                	errorStatus = SNMPRequestException.FAILED;
					}
	            		
	            	// si el numero de interfaz solicitada esta, busco el valor y lo envio
					if( 1 <= eth && eth <= nIf ){
	                    try
	                    {
	                    	File archivo = new File ("/sys/class/net/"+arrIf.get(eth-1)+"/operstate");
							FileReader fileReader = new FileReader (archivo);
							BufferedReader buffReader = new BufferedReader(fileReader);
							status=buffReader.readLine();
							if( null != fileReader)
								fileReader.close();
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
	                    }catch (Exception e){
	                    	errorIndex = i+1;
		                	errorStatus = SNMPRequestException.FAILED;
	                    }
					}else{
						errorIndex = i+1;
	                    errorStatus = SNMPRequestException.VALUE_NOT_AVAILABLE;
	                    throw new SNMPSetException("Value out of range of the number interfaces", errorIndex, errorStatus);						
					}
	                 
	                continue;
	            } // ifOperStatus
	            
	            
	            // ifLastChange TimeTicks | Read-Only | Mandatory | 1.3.6.1.2.1.2.2.1.9.X -> X es el numero de interfaz
	            if (snmpOID.toString().startsWith("1.3.6.1.2.1.2.2.1.9."))
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
						errorIndex = i+1;
	                	errorStatus = SNMPRequestException.FAILED;
					}
					
					// Pregunto cual interfaz que esta solicitando
					try {
						eth =  Integer.parseInt(snmpOID.toString().substring(20, snmpOID.toString().length()));
					}catch (NumberFormatException e) {
						errorIndex = i+1;
	                	errorStatus = SNMPRequestException.FAILED;
					}
	            		
	            	// si el numero de interfaz solicitada esta, busco el valor y lo envio
					if( 1 <= eth && eth <= nIf ){
	                    try
	                    {
	                    	File archivo = new File ("/sys/class/net/"+arrIf.get(eth-1)+"/power/runtime_active_time");
							FileReader fileReader = new FileReader (archivo);
							BufferedReader buffReader = new BufferedReader(fileReader);
							time = Integer.parseInt(buffReader.readLine());
							if( null != fileReader)
								fileReader.close();
							
							SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPTimeTicks(time));
	                        responseList.addSNMPObject(newPair);
	                    }
	                    catch (Exception e){
	                    	errorIndex = i+1;
		                	errorStatus = SNMPRequestException.FAILED;
	                    }
					}else{
						errorIndex = i+1;
	                    errorStatus = SNMPRequestException.VALUE_NOT_AVAILABLE;
	                    throw new SNMPSetException("Value out of range of the number interfaces", errorIndex, errorStatus);						
					}
	                
	                continue;
	            } // ifLastChange
	            
	            
	         // ifInOctets Counter32 | Read-Only | Mandatory | 1.3.6.1.2.1.2.2.1.10.X -> X es el numero de interfaz
	            if (snmpOID.toString().startsWith("1.3.6.1.2.1.2.2.1.10."))
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
						errorIndex = i+1;
	                	errorStatus = SNMPRequestException.FAILED;
	                }
					
					// Pregunto cual interfaz que esta solicitando
					try {
						eth =  Integer.parseInt(snmpOID.toString().substring(21, snmpOID.toString().length()));
					}catch (NumberFormatException e) {
						errorIndex = i+1;
	                	errorStatus = SNMPRequestException.FAILED;
					}
	            		
	            	// si el numero de interfaz solicitada esta, busco el valor y lo envio
					if( 1 <= eth && eth <= nIf ){
	                    try
	                    {
	                    	File archivo = new File ("/sys/class/net/"+arrIf.get(eth-1)+"/statistics/rx_bytes");
							FileReader fileReader = new FileReader (archivo);
							BufferedReader buffReader = new BufferedReader(fileReader);
							octets = Long.parseLong(buffReader.readLine());
							if( null != fileReader)
								fileReader.close();
							
							SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(octets));
	                        responseList.addSNMPObject(newPair);
	                    }catch (Exception e)
	                    {
	                    	errorIndex = i+1;
		                	errorStatus = SNMPRequestException.FAILED;
	                    }
					}else{
						errorIndex = i+1;
	                    errorStatus = SNMPRequestException.VALUE_NOT_AVAILABLE;
	                    throw new SNMPSetException("Value out of range of the number interfaces", errorIndex, errorStatus);						
					}
	                	                	
	                 
	                continue;
	            } // ifInOctets
	            
	            // ifInUcastPkts Counter32 | Read-Only | Mandatory | 1.3.6.1.2.1.2.2.1.11.X -> X es el numero de interfaz
	            if (snmpOID.toString().startsWith("1.3.6.1.2.1.2.2.1.11."))
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
						errorIndex = i+1;
	                	errorStatus = SNMPRequestException.FAILED;
					}
					
					// Pregunto cual interfaz que esta solicitando
					try {
						eth =  Integer.parseInt(snmpOID.toString().substring(21, snmpOID.toString().length()));
					}catch (NumberFormatException e) {
						errorIndex = i+1;
	                	errorStatus = SNMPRequestException.FAILED;
					}
	            		
	            	// si el numero de interfaz solicitada esta, busco el valor y lo envio
					if( 1 <= eth && eth <= nIf ){
	                    try
	                    {
	                    	File archivo = new File ("/sys/class/net/"+arrIf.get(eth-1)+"/statistics/rx_packets");
							FileReader fileReader = new FileReader (archivo);
							BufferedReader buffReader = new BufferedReader(fileReader);
							packets = Long.parseLong(buffReader.readLine());
							if( null != fileReader)
								fileReader.close();
							
							
	                        SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(packets));
	                        responseList.addSNMPObject(newPair);
	                    }catch (Exception e)
	                    {
	                    	errorIndex = i+1;
		                	errorStatus = SNMPRequestException.FAILED;
	                    }
					}else{
						errorIndex = i+1;
	                    errorStatus = SNMPRequestException.VALUE_NOT_AVAILABLE;
	                    throw new SNMPSetException("Value out of range of the number interfaces", errorIndex, errorStatus);						
					}
	                
	                continue;
	            } // ifInUcastPkts
	            
	            // ifInNUcastPkts Counter32 | Read-Only | Mandatory | 1.3.6.1.2.1.2.2.1.12.X -> X es el numero de interfaz
	            if (snmpOID.toString().startsWith("1.3.6.1.2.1.2.2.1.12."))
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
						errorIndex = i+1;
	                	errorStatus = SNMPRequestException.FAILED;
					}
					
					// Pregunto cual interfaz que esta solicitando
					try {
						eth =  Integer.parseInt(snmpOID.toString().substring(21, snmpOID.toString().length()));
					}catch (NumberFormatException e) {
						errorIndex = i+1;
	                	errorStatus = SNMPRequestException.FAILED;
					}
	            		
	            	// si el numero de interfaz solicitada esta, busco el valor y lo envio
					if( 1 <= eth && eth <= nIf ){
	                    try
	                    {
	                    	File archivo = new File ("/sys/class/net/"+arrIf.get(eth-1)+"/statistics/multicast");
							FileReader fileReader = new FileReader (archivo);
							BufferedReader buffReader = new BufferedReader(fileReader);
							packets = Long.parseLong(buffReader.readLine());
							if( null != fileReader)
								fileReader.close();
							
	                        SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(packets));
	                        responseList.addSNMPObject(newPair);
	                    }catch (Exception e)
	                    {
	                    	errorIndex = i+1;
		                	errorStatus = SNMPRequestException.FAILED;
	                    }
					}else{
						errorIndex = i+1;
	                    errorStatus = SNMPRequestException.VALUE_NOT_AVAILABLE;
	                    throw new SNMPSetException("Value out of range of the number interfaces", errorIndex, errorStatus);						
					}
	            	          
	                continue;
	            } // ifInNUcastPkts
	            
	         // ifInDiscards Counter32 | Read-Only | Mandatory | 1.3.6.1.2.1.2.2.1.13.X -> X es el numero de interfaz
	            if (snmpOID.toString().startsWith("1.3.6.1.2.1.2.2.1.13."))
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
							errorIndex = i+1;
		                	errorStatus = SNMPRequestException.FAILED;
						}
						
						// Pregunto cual interfaz que esta solicitando
						try {
							eth =  Integer.parseInt(snmpOID.toString().substring(21, snmpOID.toString().length()));
						}catch (NumberFormatException e) {
							errorIndex = i+1;
		                	errorStatus = SNMPRequestException.FAILED;
						}
	                		
	                	// si el numero de interfaz solicitada esta, busco el valor y lo envio
						if( 1 <= eth && eth <= nIf ){
		                    try
		                    {
		                    	File archivo = new File ("/sys/class/net/"+arrIf.get(eth-1)+"/statistics/rx_dropped");
								FileReader fileReader = new FileReader (archivo);
								BufferedReader buffReader = new BufferedReader(fileReader);
								packets = Long.parseLong(buffReader.readLine());
								if( null != fileReader)
									fileReader.close();
								
		                        SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(packets));
		                        responseList.addSNMPObject(newPair);
		                    }catch (Exception e)
		                    {
		                    	errorIndex = i+1;
			                	errorStatus = SNMPRequestException.FAILED;
		                    }
						}else{
							errorIndex = i+1;
		                    errorStatus = SNMPRequestException.VALUE_NOT_AVAILABLE;
		                    throw new SNMPSetException("Value out of range of the number interfaces", errorIndex, errorStatus);						
						}
	                	                	
	                
	                continue;
	            } // ifInDiscards
	            
	            // ifInErrors Counter32 | Read-Only | Mandatory | 1.3.6.1.2.1.2.2.1.14.X -> X es el numero de interfaz
	            if (snmpOID.toString().startsWith("1.3.6.1.2.1.2.2.1.14."))
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
						errorIndex = i+1;
	                	errorStatus = SNMPRequestException.FAILED;
					}
					
					// Pregunto cual interfaz que esta solicitando
					try {
						eth =  Integer.parseInt(snmpOID.toString().substring(21, snmpOID.toString().length()));
					}catch (NumberFormatException e) {
						errorIndex = i+1;
	                	errorStatus = SNMPRequestException.FAILED;
					}
	            		
	            	// si el numero de interfaz solicitada esta, busco el valor y lo envio
					if( 1 <= eth && eth <= nIf ){
	                    try
	                    {
	                    	File archivo = new File ("/sys/class/net/"+arrIf.get(eth-1)+"/statistics/rx_errors");
							FileReader fileReader = new FileReader (archivo);
							BufferedReader buffReader = new BufferedReader(fileReader);
							packets = Long.parseLong(buffReader.readLine());
							if( null != fileReader)
								fileReader.close();
							
	                        SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(packets));
	                        responseList.addSNMPObject(newPair);
	                    }catch (Exception e)
	                    {
	                    	errorIndex = i+1;
		                	errorStatus = SNMPRequestException.FAILED;
	                    }
					}else{
						errorIndex = i+1;
	                    errorStatus = SNMPRequestException.VALUE_NOT_AVAILABLE;
	                    throw new SNMPSetException("Value out of range of the number interfaces", errorIndex, errorStatus);						
					}
					
	                continue;
	            } // ifInErrors
	            
	            // ifInUnknownProtos Counter32 | Read-Only | Mandatory | 1.3.6.1.2.1.2.2.1.15.X -> X es el numero de interfaz
	            if (snmpOID.toString().startsWith("1.3.6.1.2.1.2.2.1.15."))
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
						errorIndex = i+1;
	                	errorStatus = SNMPRequestException.FAILED;
					}
					
					// Pregunto cual interfaz que esta solicitando
					try {
						eth =  Integer.parseInt(snmpOID.toString().substring(21, snmpOID.toString().length()));
					}catch (NumberFormatException e) {
						errorIndex = i+1;
	                	errorStatus = SNMPRequestException.FAILED;
					}
	            		
	            	// si el numero de interfaz solicitada esta, busco el valor y lo envio
					if( 1 <= eth && eth <= nIf ){
	                    try
	                    {
	                    	File archivo = new File ("/sys/class/net/"+arrIf.get(eth-1)+"/statistics/rx_missed_errors");
							FileReader fileReader = new FileReader (archivo);
							BufferedReader buffReader = new BufferedReader(fileReader);
							packets = Long.parseLong(buffReader.readLine());
							if( null != fileReader)
								fileReader.close();
						
							SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(packets));
	                        responseList.addSNMPObject(newPair);
	                    }catch (Exception e)
	                    {
	                    	errorIndex = i+1;
		                	errorStatus = SNMPRequestException.FAILED;
	                    }
					}else{
						errorIndex = i+1;
	                    errorStatus = SNMPRequestException.VALUE_NOT_AVAILABLE;
	                    throw new SNMPSetException("Value out of range of the number interfaces", errorIndex, errorStatus);						
					}          	
	                
	                continue;
	            } // ifInUnknownProtos
	            
	            // ifOutOctets Counter32 | Read-Only | Mandatory | 1.3.6.1.2.1.2.2.1.16.X -> X es el numero de interfaz
	            if (snmpOID.toString().startsWith("1.3.6.1.2.1.2.2.1.16."))
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
						errorIndex = i+1;
	                    errorStatus = SNMPRequestException.FAILED;
					}
					
					// Pregunto cual interfaz que esta solicitando
					try {
						eth =  Integer.parseInt(snmpOID.toString().substring(21, snmpOID.toString().length()));
					}catch (NumberFormatException e) {
						errorIndex = i+1;
	                    errorStatus = SNMPRequestException.FAILED;
					}
	            		
	            	// si el numero de interfaz solicitada esta, busco el valor y lo envio
					if( 1 <= eth && eth <= nIf ){
	                    try
	                    {
	                    	File archivo = new File ("/sys/class/net/"+arrIf.get(eth-1)+"/statistics/tx_bytes");
							FileReader fileReader = new FileReader (archivo);
							BufferedReader buffReader = new BufferedReader(fileReader);
							octets = Long.parseLong(buffReader.readLine());
							if( null != fileReader)
								fileReader.close();
	                    	
	                        SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(octets));
	                        responseList.addSNMPObject(newPair);
	                    }catch (Exception e)
	                    {
	                    	errorIndex = i+1;
		                    errorStatus = SNMPRequestException.FAILED;
	                    }
					}else{
						errorIndex = i+1;
	                    errorStatus = SNMPRequestException.VALUE_NOT_AVAILABLE;
	                    throw new SNMPSetException("Value out of range of the number interfaces", errorIndex, errorStatus);						
					}
	                
	                continue;
	            } // ifOutOctets
	            
	         // ifOutUcastPkts Counter32 | Read-Only | Mandatory | 1.3.6.1.2.1.2.2.1.17.X -> X es el numero de interfaz
	            if (snmpOID.toString().startsWith("1.3.6.1.2.1.2.2.1.17."))
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
						errorIndex = i+1;
	                    errorStatus = SNMPRequestException.FAILED;
					}
					// Pregunto cual interfaz que esta solicitando
					try {
						eth =  Integer.parseInt(snmpOID.toString().substring(21, snmpOID.toString().length()));
					}catch (NumberFormatException e) {
						errorIndex = i+1;
	                    errorStatus = SNMPRequestException.FAILED;
					}
	            	// si el numero de interfaz solicitada esta, busco el valor y lo envio
					if( 1 <= eth && eth <= nIf ){
	                    try
	                    {
	                    	File archivo = new File ("/sys/class/net/"+arrIf.get(eth-1)+"/statistics/tx_packets");
							FileReader fileReader = new FileReader (archivo);
							BufferedReader buffReader = new BufferedReader(fileReader);
							packets = Long.parseLong(buffReader.readLine());
							if( null != fileReader)
								fileReader.close();
							
	                        SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(packets));
	                        responseList.addSNMPObject(newPair);
	                    }catch (Exception e)
	                    {
	                    	errorIndex = i+1;
		                    errorStatus = SNMPRequestException.FAILED;
	                    }
					}else{
						errorIndex = i+1;
	                    errorStatus = SNMPRequestException.VALUE_NOT_AVAILABLE;
	                    throw new SNMPSetException("Value out of range of the number interfaces", errorIndex, errorStatus);						
					}
	            
	                continue;
	            } // ifOutUcastPkts
	            
	            
	         // ifOutNUcastPkts Counter32 | Read-Only | Mandatory | 1.3.6.1.2.1.2.2.1.18.X -> X es el numero de interfaz
	            if (snmpOID.toString().startsWith("1.3.6.1.2.1.2.2.1.18."))
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
							errorIndex = i+1;
		                    errorStatus = SNMPRequestException.FAILED;
						}
						
						// Pregunto cual interfaz que esta solicitando
						try {
							eth =  Integer.parseInt(snmpOID.toString().substring(21, snmpOID.toString().length()));
						}catch (NumberFormatException e) {
							errorIndex = i+1;
		                    errorStatus = SNMPRequestException.FAILED;
						}
	                		
	                	// si el numero de interfaz solicitada esta, busco el valor y lo envio
						if( 1 <= eth && eth <= nIf ){
		                    try
		                    {
		                    	//cmd="cat /sys/class/net/"+arrIf.get(eth-1)+"/statistics/OUTMULTICAST\n";
		                    	packets = 0; //Long.parseLong(in2.readLine());
								
		                        SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(packets));
		                        responseList.addSNMPObject(newPair);
		                    }
		                    catch (SNMPBadValueException e)
		                    {
		                        // won't happen...
		                    }catch (Exception e)
		                    {
		                    	errorIndex = i+1;
			                    errorStatus = SNMPRequestException.FAILED;
		                    }
						}else{
							errorIndex = i+1;
		                    errorStatus = SNMPRequestException.VALUE_NOT_AVAILABLE;
		                    throw new SNMPSetException("Value out of range of the number interfaces", errorIndex, errorStatus);						
						}
	                	                	
	                
	                continue;
	            } // ifOutNUcastPkts
	            
	            // ifOutDiscards Counter32 | Read-Only | Mandatory | 1.3.6.1.2.1.2.2.1.19.X -> X es el numero de interfaz
	            if (snmpOID.toString().startsWith("1.3.6.1.2.1.2.2.1.19."))
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
						errorIndex = i+1;
	                    errorStatus = SNMPRequestException.FAILED;
					}
					
					// Pregunto cual interfaz que esta solicitando
					try {
						eth =  Integer.parseInt(snmpOID.toString().substring(21, snmpOID.toString().length()));
					}catch (NumberFormatException e) {
						errorIndex = i+1;
	                    errorStatus = SNMPRequestException.FAILED;
					}
	            		
	            	// si el numero de interfaz solicitada esta, busco el valor y lo envio
					if( 1 <= eth && eth <= nIf ){
	                    try
	                    {
	                    	File archivo = new File("/sys/class/net/"+arrIf.get(eth-1)+"/statistics/tx_dropped");
							FileReader fileReader = new FileReader (archivo);
							BufferedReader buffReader = new BufferedReader(fileReader);
							packets = Long.parseLong(buffReader.readLine());
							if( null != fileReader)
								fileReader.close();
							
	                        SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(packets));
	                        responseList.addSNMPObject(newPair);
	                    }catch (Exception e)
	                    {
	                    	errorIndex = i+1;
		                    errorStatus = SNMPRequestException.FAILED;
	                    }
					}else{
						errorIndex = i+1;
	                    errorStatus = SNMPRequestException.VALUE_NOT_AVAILABLE;
	                    throw new SNMPSetException("Value out of range of the number interfaces", errorIndex, errorStatus);						
					}
	            	 
	                continue;
	            } // ifOutDiscards
	            
	         // ifOutErrors Counter32 | Read-Only | Mandatory | 1.3.6.1.2.1.2.2.1.20.X -> X es el numero de interfaz
	            if (snmpOID.toString().startsWith("1.3.6.1.2.1.2.2.1.20."))
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
						errorIndex = i+1;
	                    errorStatus = SNMPRequestException.FAILED;
					}
					
					// Pregunto cual interfaz que esta solicitando
					try {
						eth =  Integer.parseInt(snmpOID.toString().substring(21, snmpOID.toString().length()));
					}catch (NumberFormatException e) {
						errorIndex = i+1;
	                    errorStatus = SNMPRequestException.FAILED;
					}
	            		
	            	// si el numero de interfaz solicitada esta, busco el valor y lo envio
					if( 1 <= eth && eth <= nIf ){
	                    try
	                    {
	                    	File archivo = new File("/sys/class/net/"+arrIf.get(eth-1)+"/statistics/tx_errors");
							FileReader fileReader = new FileReader (archivo);
							BufferedReader buffReader = new BufferedReader(fileReader);
							packets = Long.parseLong(buffReader.readLine());
							if( null != fileReader)
								fileReader.close();
							
	                        SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(packets));
	                        responseList.addSNMPObject(newPair);
	                    }catch (Exception e)
	                    {
	                    	errorIndex = i+1;
		                    errorStatus = SNMPRequestException.FAILED;
	                    }
					}else{
						errorIndex = i+1;
	                    errorStatus = SNMPRequestException.VALUE_NOT_AVAILABLE;
	                    throw new SNMPSetException("Value out of range of the number interfaces", errorIndex, errorStatus);						
					}
	                 
	                continue;
	            } // ifOutErrors
	            
	            
	            // ifOutQLen Counter32 | Read-Only | Mandatory | 1.3.6.1.2.1.2.2.1.21.X -> X es el numero de interfaz
	            if (snmpOID.toString().startsWith("1.3.6.1.2.1.2.2.1.21."))
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
						errorIndex = i+1;
	                    errorStatus = SNMPRequestException.FAILED;
					}
					
					// Pregunto cual interfaz que esta solicitando
					try {
						eth =  Integer.parseInt(snmpOID.toString().substring(21, snmpOID.toString().length()));
					}catch (NumberFormatException e) {
						errorIndex = i+1;
	                    errorStatus = SNMPRequestException.FAILED;
					}
	            		
	            	// si el numero de interfaz solicitada esta, busco el valor y lo envio
					if( 1 <= eth && eth <= nIf ){
	                    try
	                    {
	                    	File archivo = new File("/sys/class/net/"+arrIf.get(eth-1)+"/tx_queue_len");
							FileReader fileReader = new FileReader (archivo);
							BufferedReader buffReader = new BufferedReader(fileReader);
							qLen = Long.parseLong(buffReader.readLine());
							if( null != fileReader)
								fileReader.close();
	                        SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPGauge32(qLen));
	                        responseList.addSNMPObject(newPair);
	                    }catch (Exception e)
	                    {
	                    	errorIndex = i+1;
		                    errorStatus = SNMPRequestException.FAILED;
	                    }
					}else{
						errorIndex = i+1;
	                    errorStatus = SNMPRequestException.VALUE_NOT_AVAILABLE;
	                    throw new SNMPSetException("Value out of range of the number interfaces", errorIndex, errorStatus);						
					}
	            	 
	                continue;
	            } // ifOutQLen 
	            
	            
	            // ifSpecific OBJECT IDENTIFIER | Read-Only | Mandatory | 1.3.6.1.2.1.2.2.1.22.X -> X es el numero de interfaz
	            if (snmpOID.toString().startsWith("1.3.6.1.2.1.2.2.1.22."))
	            {
	                int eth=0; // interfaz solicitada
	            	int nIf=0; // numero de interfaces
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
						errorIndex = i+1;
	                    errorStatus = SNMPRequestException.FAILED;
					}
					
					// Pregunto cual interfaz que esta solicitando
					try {
						eth =  Integer.parseInt(snmpOID.toString().substring(21, snmpOID.toString().length()));
					}catch (NumberFormatException e) {
						errorIndex = i+1;
	                    errorStatus = SNMPRequestException.FAILED;
					}
	            		
	            	// si el numero de interfaz solicitada esta, busco el valor y lo envio
					if( 1 <= eth && eth <= nIf ){
	                    try
	                    {
	                    	SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPObjectIdentifier("0.0"));
	                        responseList.addSNMPObject(newPair);
	                    }
	                    catch (SNMPBadValueException e)
	                    {
	                        // won't happen...
	                    }catch (Exception e)
	                    {
	                    	errorIndex = i+1;
		                    errorStatus = SNMPRequestException.FAILED;
	                    }
					}else{
						errorIndex = i+1;
	                    errorStatus = SNMPRequestException.VALUE_NOT_AVAILABLE;
	                    throw new SNMPSetException("Value out of range of the number interfaces", errorIndex, errorStatus);						
					}
	                 
	                continue;
	            } // ifOutQLen
	            
				            
            } // FIN  MIB-II | the Interfaces Group
            
			            	/********************************************
				            *	           NOT IMPLEMENT				*
				            *   MIB-II | the Address Translation Group  *
				            * 											*
				            ********************************************/
 
						            /*****************************
						            *							 *
						            *   MIB-II | the IP Group	 *
						            * 							 *
						            ******************************/
            
            if(snmpOID.toString().startsWith("1.3.6.1.2.1.4"))
            {
            	// ipForwarding | Integer | Read-Write | Mandatory |
                if (snmpOID.toString().equals("1.3.6.1.2.1.4.1.0") || snmpOID.toString().equals("1.3.6.1.2.1.4.1"))
                {	
                	String line = "";
                	ArrayList<String> lines = new ArrayList<String>();
            		try
                    {
                		File archivo = new File("/proc/net/snmp");
            			FileReader fileReader = new FileReader (archivo);
            			BufferedReader buffReader = new BufferedReader(fileReader);
                		while ((line =buffReader.readLine()) != null) {
            				lines.add(line);
            			}
                		if( null != fileReader)
    						fileReader.close();
                		
            			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
            			String value = "Forwarding";
            			arrName= new ArrayList<String>(Arrays.asList(lines.get(0).split(" "))); // IP Names
            			arrValue= new ArrayList<String>(Arrays.asList(lines.get(1).split(" "))); // IP Values
            			int pos = 0;
            			for (String stra : arrName) {
            				if(stra.equalsIgnoreCase(value)) break;
            				else pos++;
            			}
            			
            			long forw = Long.parseLong( arrValue.get(pos));
            			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(forw));
                        responseList.addSNMPObject(newPair);
                     }catch (Exception e)
                     {
                    	 errorIndex = i+1;
    	                 errorStatus = SNMPRequestException.FAILED;
                     }
                 
                    continue;
                } // ipForwarding
                
                
                // ipDefaultTTL | Integer | Read-Write | Mandatory |
                if (snmpOID.toString().equals("1.3.6.1.2.1.4.2.0") || snmpOID.toString().equals("1.3.6.1.2.1.4.2"))
                {
            		String line = "";
                	ArrayList<String> lines = new ArrayList<String>();
            		try
                    {
                		File archivo = new File("/proc/net/snmp");
            			FileReader fileReader = new FileReader (archivo);
            			BufferedReader buffReader = new BufferedReader(fileReader);
                		while ((line =buffReader.readLine()) != null) {
            				lines.add(line);
            			}
                		if( null != fileReader)
    						fileReader.close();
                		
            			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
            			String value = "DefaultTTL";
            			arrName= new ArrayList<String>(Arrays.asList(lines.get(0).split(" "))); // IP Names
            			arrValue= new ArrayList<String>(Arrays.asList(lines.get(1).split(" "))); // IP Values
            			int pos = 0;
            			for (String stra : arrName) {
            				if(stra.equalsIgnoreCase(value)) break;
            				else pos++;
            			}
            			
            			long ttl = Long.parseLong( arrValue.get(pos));
            			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(ttl));
                        responseList.addSNMPObject(newPair);
                    }catch (Exception e)
                    {
                   	 errorIndex = i+1;
                     errorStatus = SNMPRequestException.FAILED;
                    }
                    
                    continue;
                } // ipDefaultTTL
                
                
                // ipInReceives | Counter | Read-Only | Mandatory |
                if (snmpOID.toString().equals("1.3.6.1.2.1.4.3.0")|| snmpOID.toString().equals("1.3.6.1.2.1.4.3"))
                {	
                	String line = "";
                	ArrayList<String> lines = new ArrayList<String>();
            		try
                    {
                		File archivo = new File("/proc/net/snmp");
            			FileReader fileReader = new FileReader (archivo);
            			BufferedReader buffReader = new BufferedReader(fileReader);
                		while ((line =buffReader.readLine()) != null) {
            				lines.add(line);
            			}
                		if( null != fileReader)
    						fileReader.close();
            			
            			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
            			String value = "InReceives";
            			arrName= new ArrayList<String>(Arrays.asList(lines.get(0).split(" "))); // IP Names
            			arrValue= new ArrayList<String>(Arrays.asList(lines.get(1).split(" "))); // IP Values
            			int pos = 0;
            			for (String stra : arrName) {
            				if(stra.equalsIgnoreCase(value)) break;
            				else pos++;
            			}
            			
            			long recv = Long.parseLong( arrValue.get(pos));
            			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(recv));
                        responseList.addSNMPObject(newPair);
                    }catch (Exception e)
                    {
                   	 errorIndex = i+1;
                     errorStatus = SNMPRequestException.FAILED;
                    }
                    
                    continue;
                } // ipInReceives 
                
                
                // ipInHdrErrors | Counter | Read-Only | Mandatory |
                if (snmpOID.toString().equals("1.3.6.1.2.1.4.4.0")|| snmpOID.toString().equals("1.3.6.1.2.1.4.4"))
                {
                	String line = "";
                	ArrayList<String> lines = new ArrayList<String>();
            		try
                    {
                		File archivo = new File("/proc/net/snmp");
            			FileReader fileReader = new FileReader (archivo);
            			BufferedReader buffReader = new BufferedReader(fileReader);
                		while ((line =buffReader.readLine()) != null) {
            				lines.add(line);
            			}
                		if( null != fileReader)
    						fileReader.close();
            			
                		ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
            			String value = "InHdrErrors";
            			arrName= new ArrayList<String>(Arrays.asList(lines.get(0).split(" "))); // IP Names
            			arrValue= new ArrayList<String>(Arrays.asList(lines.get(1).split(" "))); // IP Values
            			int pos = 0;
            			for (String stra : arrName) {
            				if(stra.equalsIgnoreCase(value)) break;
            				else pos++;
            			}
            			
            			long recv = Long.parseLong( arrValue.get(pos));
            			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(recv));
                        responseList.addSNMPObject(newPair);
                    }catch (Exception e)
                    {
                   	 errorIndex = i+1;
                     errorStatus = SNMPRequestException.FAILED;
                    }
                    
                    continue;
                } // ipInHdrErrors 
                
                
                // ipInAddrErrors | Counter | Read-Only | Mandatory |
                if (snmpOID.toString().equals("1.3.6.1.2.1.4.5.0") || snmpOID.toString().equals("1.3.6.1.2.1.4.5"))
                {
                	String line = "";
                	ArrayList<String> lines = new ArrayList<String>();
            		try
                    {
                		File archivo = new File("/proc/net/snmp");
            			FileReader fileReader = new FileReader (archivo);
            			BufferedReader buffReader = new BufferedReader(fileReader);
                		while ((line =buffReader.readLine()) != null) {
            				lines.add(line);
            			}
                		if( null != fileReader)
    						fileReader.close();
                			
            			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
            			String value = "InAddrErrors";
            			arrName= new ArrayList<String>(Arrays.asList(lines.get(0).split(" "))); // IP Names
            			arrValue= new ArrayList<String>(Arrays.asList(lines.get(1).split(" "))); // IP Values
            			int pos = 0;
            			for (String stra : arrName) {
            				if(stra.equalsIgnoreCase(value)) break;
            				else pos++;
            			}
            			
            			long recv = Long.parseLong( arrValue.get(pos));
            			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(recv));
                        responseList.addSNMPObject(newPair);
                    }catch (Exception e)
                    {
                   	 errorIndex = i+1;
                     errorStatus = SNMPRequestException.FAILED;
                    }
                    
                    continue;
                } // ipInAddrErrors
                
                
                // ipForwDatagrams | Counter | Read-Only | Mandatory |
                if (snmpOID.toString().equals("1.3.6.1.2.1.4.6.0") || snmpOID.toString().equals("1.3.6.1.2.1.4.6"))
                {
                	String line = "";
                	ArrayList<String> lines = new ArrayList<String>();
            		try
                    {
                		File archivo = new File("/proc/net/snmp");
            			FileReader fileReader = new FileReader (archivo);
            			BufferedReader buffReader = new BufferedReader(fileReader);
                		while ((line =buffReader.readLine()) != null) {
            				lines.add(line);
            			}
                		if( null != fileReader)
    						fileReader.close();
                			
            			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
            			String value = "ForwDatagrams";
            			arrName= new ArrayList<String>(Arrays.asList(lines.get(0).split(" "))); // IP Names
            			arrValue= new ArrayList<String>(Arrays.asList(lines.get(1).split(" "))); // IP Values
            			int pos = 0;
            			for (String stra : arrName) {
            				if(stra.equalsIgnoreCase(value)) break;
            				else pos++;
            			}
            			
            			long recv = Long.parseLong( arrValue.get(pos));
            			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(recv));
                        responseList.addSNMPObject(newPair);
                    }catch (Exception e)
                    {
                   	 errorIndex = i+1;
                     errorStatus = SNMPRequestException.FAILED;
                    }
                    
                    continue;
                } // ipForwDatagrams 
                
                
                // ipInUnknownProtos | Counter | Read-Only | Mandatory |
                if (snmpOID.toString().equals("1.3.6.1.2.1.4.7.0") || snmpOID.toString().equals("1.3.6.1.2.1.4.7"))
                {
                	String line = "";
                	ArrayList<String> lines = new ArrayList<String>();
            		try
                    {
                		File archivo = new File("/proc/net/snmp");
            			FileReader fileReader = new FileReader (archivo);
            			BufferedReader buffReader = new BufferedReader(fileReader);
                		while ((line =buffReader.readLine()) != null) {
            				lines.add(line);
            			}
                		if( null != fileReader)
    						fileReader.close();
                			
            			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
            			String value = "InUnknownProtos";
            			arrName= new ArrayList<String>(Arrays.asList(lines.get(0).split(" "))); // IP Names
            			arrValue= new ArrayList<String>(Arrays.asList(lines.get(1).split(" "))); // IP Values
            			int pos = 0;
            			for (String stra : arrName) {
            				if(stra.equalsIgnoreCase(value)) break;
            				else pos++;
            			}
            			
            			long recv = Long.parseLong( arrValue.get(pos));
            			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(recv));
                        responseList.addSNMPObject(newPair);
                    }catch (Exception e)
                    {
                   	 errorIndex = i+1;
                     errorStatus = SNMPRequestException.FAILED;
                    }
                
                    continue;
                } // ipInUnknownProtos 
                
                
                // ipInDiscards | Counter | Read-Only | Mandatory |
                if (snmpOID.toString().equals("1.3.6.1.2.1.4.8.0") || snmpOID.toString().equals("1.3.6.1.2.1.4.8"))
                {
                	String line = "";
                	ArrayList<String> lines = new ArrayList<String>();
            		try
                    {
                		File archivo = new File("/proc/net/snmp");
            			FileReader fileReader = new FileReader (archivo);
            			BufferedReader buffReader = new BufferedReader(fileReader);
                		while ((line =buffReader.readLine()) != null) {
            				lines.add(line);
            			}
                		if( null != fileReader)
    						fileReader.close();
                			
            			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
            			String value = "InDiscards";
            			arrName= new ArrayList<String>(Arrays.asList(lines.get(0).split(" "))); // IP Names
            			arrValue= new ArrayList<String>(Arrays.asList(lines.get(1).split(" "))); // IP Values
            			int pos = 0;
            			for (String stra : arrName) {
            				if(stra.equalsIgnoreCase(value)) break;
            				else pos++;
            			}
            			
            			long recv = Long.parseLong( arrValue.get(pos));
            			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(recv));
                        responseList.addSNMPObject(newPair);
                    }catch (Exception e)
                    {
                   	 errorIndex = i+1;
                     errorStatus = SNMPRequestException.FAILED;
                    }
                    
                    continue;
                } // ipInDiscards
                
                
                // ipInDelivers | Counter | Read-Only | Mandatory |
                if (snmpOID.toString().equals("1.3.6.1.2.1.4.9.0") || snmpOID.toString().equals("1.3.6.1.2.1.4.9"))
                {
                	String line = "";
                	ArrayList<String> lines = new ArrayList<String>();
            		try
                    {
                		File archivo = new File("/proc/net/snmp");
            			FileReader fileReader = new FileReader (archivo);
            			BufferedReader buffReader = new BufferedReader(fileReader);
                		while ((line =buffReader.readLine()) != null) {
            				lines.add(line);
            			}
                		if( null != fileReader)
    						fileReader.close();
                			
            			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
            			String value = "InDelivers";
            			arrName= new ArrayList<String>(Arrays.asList(lines.get(0).split(" "))); // IP Names
            			arrValue= new ArrayList<String>(Arrays.asList(lines.get(1).split(" "))); // IP Values
            			int pos = 0;
            			for (String stra : arrName) {
            				if(stra.equalsIgnoreCase(value)) break;
            				else pos++;
            			}
            			
            			long recv = Long.parseLong( arrValue.get(pos));
            			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(recv));
                        responseList.addSNMPObject(newPair);
                    }catch (Exception e)
                    {
                   	 errorIndex = i+1;
                     errorStatus = SNMPRequestException.FAILED;
                    }
                    
                    continue;
                } // ipInDelivers
                
                
                // ipOutRequests | Counter | Read-Only | Mandatory |
                if (snmpOID.toString().equals("1.3.6.1.2.1.4.10.0") || snmpOID.toString().equals("1.3.6.1.2.1.4.10"))
                {
                	String line = "";
                	ArrayList<String> lines = new ArrayList<String>();
            		try
                    {
                		File archivo = new File("/proc/net/snmp");
            			FileReader fileReader = new FileReader (archivo);
            			BufferedReader buffReader = new BufferedReader(fileReader);
                		while ((line =buffReader.readLine()) != null) {
            				lines.add(line);
            			}
                		if( null != fileReader)
    						fileReader.close();
                			
            			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
            			String value = "OutRequests";
            			arrName= new ArrayList<String>(Arrays.asList(lines.get(0).split(" "))); // IP Names
            			arrValue= new ArrayList<String>(Arrays.asList(lines.get(1).split(" "))); // IP Values
            			int pos = 0;
            			for (String stra : arrName) {
            				if(stra.equalsIgnoreCase(value)) break;
            				else pos++;
            			}
            			
            			long recv = Long.parseLong( arrValue.get(pos));
            			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(recv));
                        responseList.addSNMPObject(newPair);
                    }catch (Exception e)
                    {
                   	 errorIndex = i+1;
                     errorStatus = SNMPRequestException.FAILED;
                    }
                    
                    continue;
                } // ipOutRequests
                
                
                // ipOutDiscards | Counter | Read-Only | Mandatory |
                if (snmpOID.toString().equals("1.3.6.1.2.1.4.11.0") || snmpOID.toString().equals("1.3.6.1.2.1.4.11"))
                {
                	String line = "";
                	ArrayList<String> lines = new ArrayList<String>();
            		try
                    {
                		File archivo = new File("/proc/net/snmp");
            			FileReader fileReader = new FileReader (archivo);
            			BufferedReader buffReader = new BufferedReader(fileReader);
                		while ((line =buffReader.readLine()) != null) {
            				lines.add(line);
            			}
                		if( null != fileReader)
    						fileReader.close();
                			
            			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
            			String value = "OutDiscards";
            			arrName= new ArrayList<String>(Arrays.asList(lines.get(0).split(" "))); // IP Names
            			arrValue= new ArrayList<String>(Arrays.asList(lines.get(1).split(" "))); // IP Values
            			int pos = 0;
            			for (String stra : arrName) {
            				if(stra.equalsIgnoreCase(value)) break;
            				else pos++;
            			}
            			
            			long recv = Long.parseLong( arrValue.get(pos));
            			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(recv));
                        responseList.addSNMPObject(newPair);
                    }catch (Exception e)
                    {
                   	 errorIndex = i+1;
                     errorStatus = SNMPRequestException.FAILED;
                    }
                    
                    continue;
                } // ipOutDiscards
                
                
             // ipOutNoRoutes | Counter | Read-Only | Mandatory |
                if (snmpOID.toString().equals("1.3.6.1.2.1.4.12.0") || snmpOID.toString().equals("1.3.6.1.2.1.4.12"))
                {
                	String line = "";
                	ArrayList<String> lines = new ArrayList<String>();
            		try
                    {
                		File archivo = new File("/proc/net/snmp");
            			FileReader fileReader = new FileReader (archivo);
            			BufferedReader buffReader = new BufferedReader(fileReader);
                		while ((line =buffReader.readLine()) != null) {
            				lines.add(line);
            			}
                		if( null != fileReader)
    						fileReader.close();
                			
            			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
            			String value = "OutNoRoutes";
            			arrName= new ArrayList<String>(Arrays.asList(lines.get(0).split(" "))); // IP Names
            			arrValue= new ArrayList<String>(Arrays.asList(lines.get(1).split(" "))); // IP Values
            			int pos = 0;
            			for (String stra : arrName) {
            				if(stra.equalsIgnoreCase(value)) break;
            				else pos++;
            			}
            			
            			long recv = Long.parseLong( arrValue.get(pos));
            			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(recv));
                        responseList.addSNMPObject(newPair);
                    }catch (Exception e)
                    {
                   	 errorIndex = i+1;
                     errorStatus = SNMPRequestException.FAILED;
                    }
                    
                    continue;
                } // ipOutNoRoutes
                
                
                // ipReasmTimeout | Integer | Read-Only | Mandatory |
                if (snmpOID.toString().equals("1.3.6.1.2.1.4.13.0") || snmpOID.toString().equals("1.3.6.1.2.1.4.13"))
                {
                	String line = "";
                	ArrayList<String> lines = new ArrayList<String>();
            		try
                    {
                		File archivo = new File("/proc/net/snmp");
            			FileReader fileReader = new FileReader (archivo);
            			BufferedReader buffReader = new BufferedReader(fileReader);
                		while ((line =buffReader.readLine()) != null) {
            				lines.add(line);
            			}
                		if( null != fileReader)
    						fileReader.close();
                			
            			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
            			String value = "ReasmTimeout";
            			arrName= new ArrayList<String>(Arrays.asList(lines.get(0).split(" "))); // IP Names
            			arrValue= new ArrayList<String>(Arrays.asList(lines.get(1).split(" "))); // IP Values
            			int pos = 0;
            			for (String stra : arrName) {
            				if(stra.equalsIgnoreCase(value)) break;
            				else pos++;
            			}
            			
            			long recv = Long.parseLong( arrValue.get(pos));
            			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPInteger(recv));
                        responseList.addSNMPObject(newPair);
                    }catch (Exception e)
                    {
                   	 errorIndex = i+1;
                     errorStatus = SNMPRequestException.FAILED;
                    }
                    
                    continue;
                } // ipReasmTimeout
                
                
                // ipReasmReqds | Counter | Read-Only | Mandatory |
                if (snmpOID.toString().equals("1.3.6.1.2.1.4.14.0") || snmpOID.toString().equals("1.3.6.1.2.1.4.14"))
                {
                	String line = "";
                	ArrayList<String> lines = new ArrayList<String>();
            		try
                    {
                		File archivo = new File("/proc/net/snmp");
            			FileReader fileReader = new FileReader (archivo);
            			BufferedReader buffReader = new BufferedReader(fileReader);
                		while ((line =buffReader.readLine()) != null) {
            				lines.add(line);
            			}
                		if( null != fileReader)
    						fileReader.close();
                			
            			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
            			String value = "ReasmReqds";
            			arrName= new ArrayList<String>(Arrays.asList(lines.get(0).split(" "))); // IP Names
            			arrValue= new ArrayList<String>(Arrays.asList(lines.get(1).split(" "))); // IP Values
            			int pos = 0;
            			for (String stra : arrName) {
            				if(stra.equalsIgnoreCase(value)) break;
            				else pos++;
            			}
            			
            			long recv = Long.parseLong( arrValue.get(pos));
            			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(recv));
                        responseList.addSNMPObject(newPair);
                    }catch (Exception e)
                    {
                   	 errorIndex = i+1;
                     errorStatus = SNMPRequestException.FAILED;
                    }
                    
                    continue;
                } // ipReasmReqds
                
                // ipReasmOKs | Counter | Read-Only | Mandatory |
                if (snmpOID.toString().equals("1.3.6.1.2.1.4.15.0") || snmpOID.toString().equals("1.3.6.1.2.1.4.15"))
                {
                	String line = "";
                	ArrayList<String> lines = new ArrayList<String>();
            		try
                    {
                		File archivo = new File("/proc/net/snmp");
            			FileReader fileReader = new FileReader (archivo);
            			BufferedReader buffReader = new BufferedReader(fileReader);
                		while ((line =buffReader.readLine()) != null) {
            				lines.add(line);
            			}
                		if( null != fileReader)
    						fileReader.close();
                		
            			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
            			String value = "ReasmOKs";
            			arrName= new ArrayList<String>(Arrays.asList(lines.get(0).split(" "))); // IP Names
            			arrValue= new ArrayList<String>(Arrays.asList(lines.get(1).split(" "))); // IP Values
            			int pos = 0;
            			for (String stra : arrName) {
            				if(stra.equalsIgnoreCase(value)) break;
            				else pos++;
            			}
            			
            			long recv = Long.parseLong( arrValue.get(pos));
            			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(recv));
                        responseList.addSNMPObject(newPair);
                    }catch (Exception e)
                    {
                   	 errorIndex = i+1;
                     errorStatus = SNMPRequestException.FAILED;
                    }
                    
                    continue;
                } // ipReasmOKs
                
                
                // ipReasmFails | Counter | Read-Only | Mandatory |
                if (snmpOID.toString().equals("1.3.6.1.2.1.4.16.0") || snmpOID.toString().equals("1.3.6.1.2.1.4.16"))
                {
                	String line = "";
                	ArrayList<String> lines = new ArrayList<String>();
            		try
                    {
                		File archivo = new File("/proc/net/snmp");
            			FileReader fileReader = new FileReader (archivo);
            			BufferedReader buffReader = new BufferedReader(fileReader);
                		while ((line =buffReader.readLine()) != null) {
            				lines.add(line);
            			}
                		if( null != fileReader)
    						fileReader.close();
                		
            			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
            			String value = "ReasmFails";
            			arrName= new ArrayList<String>(Arrays.asList(lines.get(0).split(" "))); // IP Names
            			arrValue= new ArrayList<String>(Arrays.asList(lines.get(1).split(" "))); // IP Values
            			int pos = 0;
            			for (String stra : arrName) {
            				if(stra.equalsIgnoreCase(value)) break;
            				else pos++;
            			}
            			
            			long recv = Long.parseLong( arrValue.get(pos));
            			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(recv));
                        responseList.addSNMPObject(newPair);
                    }catch (Exception e)
                    {
                   	 errorIndex = i+1;
                     errorStatus = SNMPRequestException.FAILED;
                    }
                    
                    continue;
                } // ipReasmFails
                
                // ipFragOKs | Counter | Read-Only | Mandatory |
                if (snmpOID.toString().equals("1.3.6.1.2.1.4.17.0") || snmpOID.toString().equals("1.3.6.1.2.1.4.17"))
                {
                	String line = "";
                	ArrayList<String> lines = new ArrayList<String>();
            		try
                    {
                		File archivo = new File("/proc/net/snmp");
            			FileReader fileReader = new FileReader (archivo);
            			BufferedReader buffReader = new BufferedReader(fileReader);
                		while ((line =buffReader.readLine()) != null) {
            				lines.add(line);
            			}
                		if( null != fileReader)
    						fileReader.close();
                			
            			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
            			String value = "FragOKs";
            			arrName= new ArrayList<String>(Arrays.asList(lines.get(0).split(" "))); // IP Names
            			arrValue= new ArrayList<String>(Arrays.asList(lines.get(1).split(" "))); // IP Values
            			int pos = 0;
            			for (String stra : arrName) {
            				if(stra.equalsIgnoreCase(value)) break;
            				else pos++;
            			}
            			
            			long recv = Long.parseLong( arrValue.get(pos));
            			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(recv));
                        responseList.addSNMPObject(newPair);
                    }catch (Exception e)
                    {
                   	 errorIndex = i+1;
                     errorStatus = SNMPRequestException.FAILED;
                    }
                    
                    continue;
                } // ipFragOKs
                
                // ipFragFails | Counter | Read-Only | Mandatory |
                if (snmpOID.toString().equals("1.3.6.1.2.1.4.18.0") || snmpOID.toString().equals("1.3.6.1.2.1.4.18"))
                {
                	String line = "";
                	ArrayList<String> lines = new ArrayList<String>();
            		try
                    {
                		File archivo = new File("/proc/net/snmp");
            			FileReader fileReader = new FileReader (archivo);
            			BufferedReader buffReader = new BufferedReader(fileReader);
                		while ((line =buffReader.readLine()) != null) {
            				lines.add(line);
            			}
                		if( null != fileReader)
    						fileReader.close();
                		
            			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
            			String value = "FragFails";
            			arrName= new ArrayList<String>(Arrays.asList(lines.get(0).split(" "))); // IP Names
            			arrValue= new ArrayList<String>(Arrays.asList(lines.get(1).split(" "))); // IP Values
            			int pos = 0;
            			for (String stra : arrName) {
            				if(stra.equalsIgnoreCase(value)) break;
            				else pos++;
            			}
            			
            			long recv = Long.parseLong( arrValue.get(pos));
            			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(recv));
                        responseList.addSNMPObject(newPair);
                    }catch (Exception e)
                    {
                   	 errorIndex = i+1;
                     errorStatus = SNMPRequestException.FAILED;
                    }
                    
                    continue;
                } // ipFragFails
                
                
                // ipFragCreates | Counter | Read-Only | Mandatory |
                if (snmpOID.toString().equals("1.3.6.1.2.1.4.19.0") || snmpOID.toString().equals("1.3.6.1.2.1.4.19"))
                {
                	String line = "";
                	ArrayList<String> lines = new ArrayList<String>();
            		try
                    {
                		File archivo = new File("/proc/net/snmp");
            			FileReader fileReader = new FileReader (archivo);
            			BufferedReader buffReader = new BufferedReader(fileReader);
                		while ((line =buffReader.readLine()) != null) {
            				lines.add(line);
            			}
                		if( null != fileReader)
    						fileReader.close();
                		
            			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
            			String value = "FragCreates";
            			arrName= new ArrayList<String>(Arrays.asList(lines.get(0).split(" "))); // IP Names
            			arrValue= new ArrayList<String>(Arrays.asList(lines.get(1).split(" "))); // IP Values
            			int pos = 0;
            			for (String stra : arrName) {
            				if(stra.equalsIgnoreCase(value)) break;
            				else pos++;
            			}
            			
            			long recv = Long.parseLong( arrValue.get(pos));
            			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(recv));
                        responseList.addSNMPObject(newPair);
                    }catch (Exception e)
                    {
                   	 errorIndex = i+1;
                     errorStatus = SNMPRequestException.FAILED;
                    }
                    
                    continue;
                } // ipFragCreates
                
                
                 /***********************************
                 * 									*
                 *  	the IP Address Table		*
                 *   ipAddrTable 1.3.6.1.2.1.4.20	*
                 *  								*
                 ***********************************/

                if (snmpOID.toString().startsWith(("1.3.6.1.2.1.4.20.1.")))
                {
                	String cmd="ip address show"+"\n";
                	String line = ""; // deafult_ttl
                	Process p=null;
                	ArrayList<String> lines = new ArrayList<String>();
            		BufferedReader in2=null;

                	try
                    {
                		p = Runtime.getRuntime().exec(cmd);
            			in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
            			
            			while ((line =in2.readLine()) != null) {
            				lines.add(line);
            			}

            			String ifIndex;
            			int x = 0;
            			String cadena = "";
            			cadena = lines.get(x);
            			ArrayList<ArrayList<String>> table = new ArrayList<ArrayList<String>>();
            			while ( x < lines.size()) {

            				if(cadena.contains("mtu") && cadena.contains("qdisc") && cadena.contains("state") ){

            					String arrTmp[] = cadena.split(" ");
            					ifIndex = arrTmp[0].substring(0, arrTmp[0].length()-1);
            					
            					do{
            						
            						if(!cadena.contains("inet6") && cadena.contains("inet")){

                    					arrTmp = cadena.split(" ");	
                    					
                    					String arrTmp1[] = arrTmp[5].split("/");
                    					String ipAddr = arrTmp1[0], strMask = arrTmp1[1] ;
                    					int mask = Integer.parseInt(strMask);
                    					
                    						if (mask <= 8){
                    							strMask = (256 - (Math.pow(2,(8 - mask)))) + ".0.0";
                    						}else if (mask <= 16){
                    							strMask =  "255." + (256 - (Math.pow(2,(16 - mask)))) +".0";
                    						}else if (mask <= 24){
                    							strMask =  "255.255." + (256 - (Math.pow(2,(24 - mask))));
                    						}else if (mask <= 32){
                    							strMask = "255.255.255." + (256 - (Math.pow(2,(32 - mask))));
                    						}	
                    						
                    						ArrayList<String> rowTable = new ArrayList<String>();
                    						rowTable.add(ipAddr);
                    						rowTable.add(ifIndex);
                    						rowTable.add(strMask);
                    						rowTable.add("1"); //BroadCast
                    						rowTable.add("65535"); //
                    						
                    						table.add(rowTable);
                    				}
            						x++;
            						if (x >= lines.size()) break; 
            						cadena = lines.get(x);
            					}while(!(cadena.contains("mtu") && cadena.contains("qdisc") && cadena.contains("state")));
            					
            				} // if
            				
            			} // while ( x < lines.size()) {
            			
            			for (int j = 0; j < table.size(); j++) {
            				ArrayList<String> arrElement = table.get(j);
            				
            				// ipAdEntAddr | IpAddress | Read-Only | Mandatory
            				if(snmpOID.toString().equals(("1.3.6.1.2.1.4.20.1.1."+arrElement.get(0)))){

        	                	try
        	                    {
        	                        SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPIPAddress(arrElement.get(0)));
        	                        responseList.addSNMPObject(newPair);
        	                        break;
        	                    }catch (Exception e)
        	                    {
        	                   	 errorIndex = i+1;
        	                     errorStatus = SNMPRequestException.FAILED;
        	                    }
    	        	            
            				}// ipAdEntAddr
            				
            				// ipAdEntIfIndex | Integer | Read-Only | Mandatory
            				if(snmpOID.toString().equals(("1.3.6.1.2.1.4.20.1.2."+arrElement.get(0)))){

            					try
        	                    {
        	                        SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPInteger(Integer.parseInt(arrElement.get(1))));
        	                        responseList.addSNMPObject(newPair);
        	                        break;
        	                    }catch (Exception e)
        	                    {
        	                   	 errorIndex = i+1;
        	                     errorStatus = SNMPRequestException.FAILED;
        	                    }
    	        	            
            				}// ipAdEntIfIndex
            				
            				// ipAdEntNetMask | IpAddress | Read-Only | Mandatory
            				if(snmpOID.toString().equals(("1.3.6.1.2.1.4.20.1.3."+arrElement.get(0)))){

        	                	try
        	                    {
        	                		SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPIPAddress(arrElement.get(2)));
        	                        responseList.addSNMPObject(newPair);
        	                        break;
        	                    }catch (Exception e)
        	                    {
        	                   	 errorIndex = i+1;
        	                     errorStatus = SNMPRequestException.FAILED;
        	                    }
    	        	            
            				}// ipAdEntNetMask
            				
            				// ipAdEntBcastAddr | Integer | Read-Only | Mandatory
            				if(snmpOID.toString().equals(("1.3.6.1.2.1.4.20.1.4."+arrElement.get(0)))){

        	                	try
        	                    {
        	                        SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPInteger(Integer.parseInt(arrElement.get(3))));
        	                        responseList.addSNMPObject(newPair);
        	                        break;
        	                    }catch (Exception e)
        	                    {
        	                   	 errorIndex = i+1;
        	                     errorStatus = SNMPRequestException.FAILED;
        	                    }
    	        	            
            				}// ipAdEntBcastAddr
            				
            				// ipAdEntBcastAddr | Integer | Read-Only | Mandatory
            				if(snmpOID.toString().equals(("1.3.6.1.2.1.4.20.1.5."+arrElement.get(0)))){

            	                try
        	                    {
        	                        SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPInteger(Integer.parseInt(arrElement.get(4))));
        	                        responseList.addSNMPObject(newPair);
        	                        break;
        	                    }catch (Exception e)
        	                    {
        	                   	 errorIndex = i+1;
        	                     errorStatus = SNMPRequestException.FAILED;
        	                    }
    	        	            
            				}// ipAdEntBcastAddr
    						
    					} 
            			
                    }catch (Exception e)
                    {
                   	 errorIndex = i+1;
                     errorStatus = SNMPRequestException.FAILED;
                    }
                	continue;
                }
                
                /***********************************
                 * 									*
                 *  	the IP Routing Table		*
                 *   ipAddrTable 1.3.6.1.2.1.4.21	*
                 *  								*
                 ***********************************/
                             
                if (snmpOID.toString().startsWith(("1.3.6.1.2.1.4.21.1.")))
                {	//  0        1         2      3     4     5     6    7    8    9     10
                	//Iface Destination Gateway Flags RefCnt Use Metric Mask MTU Window IRTT
                	String cmd="";
                	String line = "";
                	ArrayList<String> lines = new ArrayList<String>();
            		try
                    {
                		File archivo = new File("/proc/net/route");
            			FileReader fileReader = new FileReader (archivo);
            			BufferedReader buffReader = new BufferedReader(fileReader);
                		while ((line =buffReader.readLine()) != null) {
            				lines.add(line);
            			}
                		if( null != fileReader)
    						fileReader.close();
                		
            			for(int x = 1 ; x < lines.size() ; x++){
    						String arrTmp[] = lines.get(x).split("\t");
    						String hexValue = arrTmp[1];
    						String ip = "";
    					
    						for(int i1 = hexValue.length(); i1 > 0; i1 = i1 - 2) {
    						    ip = ip + Integer.valueOf(hexValue.substring(i1-2, i1 ), 16) + ".";
    						}
    						ip= (ip.substring(0, ip.length()-1));
    						// ipRouteDest | ipAddress | Read-Write | Mandatory
    						if (snmpOID.toString().equals(("1.3.6.1.2.1.4.21.1.1."+ip)))
    		                {
    							SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPIPAddress(ip));
    	                        responseList.addSNMPObject(newPair);
    	                        
    	                    // ipRouteIfIndex | Integer | Read-Write | Mandatory
    		                }else if (snmpOID.toString().equals(("1.3.6.1.2.1.4.21.1.2."+ip)))
    		                {
    		                	cmd="ls /sys/class/net/";;
    		                	line = "";
    		                	int iFace=1;
    		                	Process p = null;
    		                	BufferedReader in2 = null;
    		                	try
    		                    {
    		                		p = Runtime.getRuntime().exec(cmd);
    		            			in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
    		            			
    		            			while ((line =in2.readLine()) != null) {
    		            				if(line.equals(arrTmp[0])){
    		            					break;
    		            				}
    		            				iFace++;
    		            			}
    		            		}catch (Exception e)
    			                 {
    			                	 e.printStackTrace();
    			                 }
    		                	
    							SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPInteger(iFace));
    	                        responseList.addSNMPObject(newPair);
    	                        
    	                    // ipRouteMetric1 | Integer | Read-Write | Mandatory
    		                }else if (snmpOID.toString().equals(("1.3.6.1.2.1.4.21.1.3."+ip)))
    		                {
    		                	SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPInteger(Integer.parseInt(arrTmp[6])));
    	                        responseList.addSNMPObject(newPair);
    	                    // ipRouteNextHop | ipAddress | Read-Write | Mandatory    
    		                }else if (snmpOID.toString().equals(("1.3.6.1.2.1.4.21.1.7."+ip)))
    		                {
    		                	hexValue = arrTmp[2];
    							ip = "";
    						
    							for(int i1 = hexValue.length(); i1 > 0; i1 = i1 - 2) {
    							    ip = ip + Integer.valueOf(hexValue.substring(i1-2, i1 ), 16) + ".";
    							}
    							ip= (ip.substring(0, ip.length()-1));
    		                	
    							SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPIPAddress(ip));
    	                        responseList.addSNMPObject(newPair);
    	                    // ipRouteMask | ipAddress | Read-Write | Mandatory    
    		                }else if (snmpOID.toString().equals(("1.3.6.1.2.1.4.21.1.11."+ip)))
    		                {
    		                	hexValue = arrTmp[7];
    							ip = "";
    						
    							for(int i1 = hexValue.length(); i1 > 0; i1 = i1 - 2) {
    							    ip = ip + Integer.valueOf(hexValue.substring(i1-2, i1 ), 16) + ".";
    							}
    							ip= (ip.substring(0, ip.length()-1));
    		                	
    							SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPIPAddress(ip));
    	                        responseList.addSNMPObject(newPair);
    		                }
    						
    					}
                        
                     }catch (Exception e)
                     {
                    	 errorIndex = i+1;
                         errorStatus = SNMPRequestException.FAILED;
                     }
                    
                	continue;   
                }
                
                 /**************************************
                 * 									   *
                 *  the IP Address Translation table   *
                 *    ipAddrTable 1.3.6.1.2.1.4.22	   *
                 *  								   *
                 **************************************/
            
                if (snmpOID.toString().startsWith(("1.3.6.1.2.1.4.22.1.")))
                {
                	// ipNetToMediaIfIndex 
                	String cmd="ip address show"+"\n";
                	String line = ""; // deafult_ttl
                	Process p=null;
                	ArrayList<String> lines = new ArrayList<String>();
            		BufferedReader in2=null;

                	try
                    {
                		p = Runtime.getRuntime().exec(cmd);
            			in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
            			
            			while ((line =in2.readLine()) != null) {
            				lines.add(line);
            			}

            			String ifIndex="",cadena="",phyIFace="";
            			int x = 0;
            			cadena = lines.get(x);
            			ArrayList<ArrayList<String>> table = new ArrayList<ArrayList<String>>();
            			
            			while ( x < lines.size()) {

            				if(cadena.contains("mtu") && cadena.contains("qdisc") && cadena.contains("state") ){

            					String arrTmp[] = cadena.split(" ");
            					ifIndex = arrTmp[0].substring(0, arrTmp[0].length()-1);
            					
            					do{
            						
            						if(cadena.contains("link/ether") || cadena.contains("link/loopback")){ /*|| cadena.contains("link/ppp") ||
            						   cadena.contains("link/tunnel6") || cadena.contains("link/sit") ||
            						   cadena.contains("link/loopback") ){ // si y solo si se puede colocar la ip 127.0.0.1*/
            							arrTmp = cadena.split(" ");
            							phyIFace = arrTmp[5];
            						}
            						
            						if(!cadena.contains("inet6") && cadena.contains("inet")){

                    					arrTmp = cadena.split(" ");	
                    					
                    					String arrTmp1[] = arrTmp[5].split("/");
                    					String ipAddr = arrTmp1[0];	
                    						
                    						ArrayList<String> rowTable = new ArrayList<String>();
                    						rowTable.add(ifIndex); // 0
                    						rowTable.add(phyIFace);// 1
                    						rowTable.add(ipAddr);  // 2
                    						rowTable.add("1");     // 3  Other always 
                    						
                    						table.add(rowTable);
                    				}
            						x++;
            						if (x >= lines.size()) break; 
            						cadena = lines.get(x);
            					}while(!(cadena.contains("mtu") && cadena.contains("qdisc") && cadena.contains("state")));
            					

            				} // if
            				
            			} // while ( x < lines.size()) {
            			
            			for (int j = 0; j < table.size(); j++){
            				ArrayList<String> arrElement = table.get(j);
            				
            				// ipNetToMediaIfIndex | INTEGER | read-write | mandatory
            				if(snmpOID.toString().equals(("1.3.6.1.2.1.4.22.1.1."+arrElement.get(2)))){

        	                	try
        	                    {
        	                        SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPInteger(Integer.parseInt(arrElement.get(0))));
        	                        responseList.addSNMPObject(newPair);
        	                        break;
        	                    }
        	                    catch (SNMPBadValueException e)
        	                    {
        	                    	errorIndex = i+1;
        	                        errorStatus = SNMPRequestException.FAILED;
        	                    }
    	        	            
            				}// ipNetToMediaIfIndex
            				
            				// ipNetToMediaPhysAddress  | PhysAddress | read-write | mandatory
            				if(snmpOID.toString().equals(("1.3.6.1.2.1.4.22.1.2."+arrElement.get(2)))){

        	                	try
        	                    {
        	                        SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPOctetString(arrElement.get(1)));
        	                        responseList.addSNMPObject(newPair);
        	                        break;
        	                    }
        	                    catch (SNMPBadValueException e)
        	                    {
        	                    	errorIndex = i+1;
        	                        errorStatus = SNMPRequestException.FAILED;
        	                    }
    	        	            
            				}// ipNetToMediaPhysAddress 
            				
            				// ipNetToMediaNetAddress   | PhysAddress | read-write | mandatory
            				if(snmpOID.toString().equals(("1.3.6.1.2.1.4.22.1.3."+arrElement.get(2)))){

            	                try
        	                    {
        	                        SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()),new SNMPIPAddress(arrElement.get(2))); 
        	                        responseList.addSNMPObject(newPair);
        	                        break;
        	                    }
        	                    catch (SNMPBadValueException e)
        	                    {
        	                    	errorIndex = i+1;
        	                        errorStatus = SNMPRequestException.FAILED;
        	                    }
    	        	            
            				}// ipNetToMediaNetAddress  
            				
            				// ipNetToMediaType    | PhysAddress | read-write | mandatory
            				if(snmpOID.toString().equals(("1.3.6.1.2.1.4.22.1.4."+arrElement.get(2)))){

        	                	try
        	                    {
        	                        SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPInteger(Integer.parseInt(arrElement.get(3))));
        	                        responseList.addSNMPObject(newPair);
        	                        break;
        	                    }
        	                    catch (SNMPBadValueException e)
        	                    {
        	                    	errorIndex = i+1;
        	                        errorStatus = SNMPRequestException.FAILED;
        	                    }
    	        	            
            				}// ipNetToMediaType  
            				
            			} // for (int j = 0; j < table.size(); j++)
            			
            			
                    }catch (Exception e)
                    {
                   	 	errorIndex = i+1;
                        errorStatus = SNMPRequestException.FAILED;
                    }
                	
                	continue;
                }
                
                
            } // MIB-II | the IP Group
            
					             /***************************
					             * 							*
					             * MIB-II |	the ICMP Group  *
					             * 							*
					             ***************************/
            
            if(snmpOID.toString().startsWith("1.3.6.1.2.1.5"))
            { 
	            // icmpInMsgs | Counter | Read-Only | Mandatory 
	            if (snmpOID.toString().equals("1.3.6.1.2.1.5.1.0") || snmpOID.toString().equals("1.3.6.1.2.1.5.1"))
	            {
	            	String line = "";
	            	ArrayList<String> lines = new ArrayList<String>();
	        		try
	                {
	            		File archivo = new File("/proc/net/snmp");
	        			FileReader fileReader = new FileReader (archivo);
	        			BufferedReader buffReader = new BufferedReader(fileReader);
	            		while ((line =buffReader.readLine()) != null) {
	        				lines.add(line);
	        			}
	            		if( null != fileReader)
							fileReader.close();
	            		
	        			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
	        			String value = "InMsgs";
	        			arrName= new ArrayList<String>(Arrays.asList(lines.get(2).split(" "))); // IP Names
	        			arrValue= new ArrayList<String>(Arrays.asList(lines.get(3).split(" "))); // IP Values
	        			int pos = 0;
	        			for (String stra : arrName) {
	        				if(stra.equalsIgnoreCase(value)) break;
	        				else pos++;
	        			}
	        			
	        			long recv = Long.parseLong( arrValue.get(pos));
	        			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(recv));
	                    responseList.addSNMPObject(newPair);
	                }catch (Exception e){
                    	errorIndex = i+1;
	                	errorStatus = SNMPRequestException.FAILED;
                    }
	                
	                continue;
	            } // icmpInMsgs
            
            
	         // icmpInErrors | Counter | Read-Only | Mandatory 
	            if (snmpOID.toString().equals("1.3.6.1.2.1.5.2.0") || snmpOID.toString().equals("1.3.6.1.2.1.5.2"))
	            {
	            	String line = "";
	            	ArrayList<String> lines = new ArrayList<String>();
	        		try
	                {
	            		File archivo = new File("/proc/net/snmp");
	        			FileReader fileReader = new FileReader (archivo);
	        			BufferedReader buffReader = new BufferedReader(fileReader);
	            		while ((line =buffReader.readLine()) != null) {
	        				lines.add(line);
	        			}
	            		if( null != fileReader)
							fileReader.close();
	            		
	        			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
	        			String value = "InErrors";
	        			arrName= new ArrayList<String>(Arrays.asList(lines.get(2).split(" "))); // IP Names
	        			arrValue= new ArrayList<String>(Arrays.asList(lines.get(3).split(" "))); // IP Values
	        			int pos = 0;
	        			for (String stra : arrName) {
	        				if(stra.equalsIgnoreCase(value)) break;
	        				else pos++;
	        			}
	        			
	        			long recv = Long.parseLong( arrValue.get(pos));
	        			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(recv));
	                    responseList.addSNMPObject(newPair);
	                }catch (Exception e){
	                	errorIndex = i+1;
	                	errorStatus = SNMPRequestException.FAILED;
	                }
	                
	                continue;
	            } // icmpInErrors
	            
	            // icmpInDestUnreachs | Counter | Read-Only | Mandatory 
	            if (snmpOID.toString().equals("1.3.6.1.2.1.5.3.0") || snmpOID.toString().equals("1.3.6.1.2.1.5.3"))
	            {
	            	String line = "";
	            	ArrayList<String> lines = new ArrayList<String>();
	        		try
	                {
	            		File archivo = new File("/proc/net/snmp");
	        			FileReader fileReader = new FileReader (archivo);
	        			BufferedReader buffReader = new BufferedReader(fileReader);
	            		while ((line =buffReader.readLine()) != null) {
	        				lines.add(line);
	        			}
	            		if( null != fileReader)
							fileReader.close();
	            		
	        			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
	        			String value = "InDestUnreachs";
	        			arrName= new ArrayList<String>(Arrays.asList(lines.get(2).split(" "))); // IP Names
	        			arrValue= new ArrayList<String>(Arrays.asList(lines.get(3).split(" "))); // IP Values
	        			int pos = 0;
	        			for (String stra : arrName) {
	        				if(stra.equalsIgnoreCase(value)) break;
	        				else pos++;
	        			}
	        			
	        			long recv = Long.parseLong( arrValue.get(pos));
	        			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(recv));
	                    responseList.addSNMPObject(newPair);
	                }catch (Exception e){
	                	errorIndex = i+1;
	                	errorStatus = SNMPRequestException.FAILED;
	                }
	                
	                continue;
	            } // icmpInDestUnreachs
	            
	            
	         // icmpInTimeExcds | Counter | Read-Only | Mandatory 
	            if (snmpOID.toString().equals("1.3.6.1.2.1.5.4.0") || snmpOID.toString().equals("1.3.6.1.2.1.5.4"))
	            {
	            	String line = "";
	            	ArrayList<String> lines = new ArrayList<String>();
	        		try
	                {
	            		File archivo = new File("/proc/net/snmp");
	        			FileReader fileReader = new FileReader (archivo);
	        			BufferedReader buffReader = new BufferedReader(fileReader);
	            		while ((line =buffReader.readLine()) != null) {
	        				lines.add(line);
	        			}
	            		if( null != fileReader)
							fileReader.close();
	            		
            			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
            			String value = "InTimeExcds";
            			arrName= new ArrayList<String>(Arrays.asList(lines.get(2).split(" "))); // IP Names
            			arrValue= new ArrayList<String>(Arrays.asList(lines.get(3).split(" "))); // IP Values
            			int pos = 0;
            			for (String stra : arrName) {
            				if(stra.equalsIgnoreCase(value)) break;
            				else pos++;
            			}
            			
            			long recv = Long.parseLong( arrValue.get(pos));
            			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(recv));
                        responseList.addSNMPObject(newPair);
                    }catch (Exception e){
	                	errorIndex = i+1;
	                	errorStatus = SNMPRequestException.FAILED;
	                }
	                
	                continue;
	            } // icmpInTimeExcds
	            
	            
	            // icmpInParmProbs | Counter | Read-Only | Mandatory 
	            if (snmpOID.toString().equals("1.3.6.1.2.1.5.5.0") || snmpOID.toString().equals("1.3.6.1.2.1.5.5"))
	            {
	            	String line = "";
	            	ArrayList<String> lines = new ArrayList<String>();
	        		try
	                {
	            		File archivo = new File("/proc/net/snmp");
	        			FileReader fileReader = new FileReader (archivo);
	        			BufferedReader buffReader = new BufferedReader(fileReader);
	            		while ((line =buffReader.readLine()) != null) {
	        				lines.add(line);
	        			}
	            		if( null != fileReader)
							fileReader.close();
	            		
	        			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
	        			String value = "InParmProbs";
	        			arrName= new ArrayList<String>(Arrays.asList(lines.get(2).split(" "))); // IP Names
	        			arrValue= new ArrayList<String>(Arrays.asList(lines.get(3).split(" "))); // IP Values
	        			int pos = 0;
	        			for (String stra : arrName) {
	        				if(stra.equalsIgnoreCase(value)) break;
	        				else pos++;
	        			}
	        			
	        			long recv = Long.parseLong( arrValue.get(pos));
	        			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(recv));
	                    responseList.addSNMPObject(newPair);
	                }catch (Exception e){
	                	errorIndex = i+1;
	                	errorStatus = SNMPRequestException.FAILED;
	                }
	                
	                continue;
	            } // icmpInParmProbs
	            
	            
	            // icmpInSrcQuenchs | Counter | Read-Only | Mandatory 
	            if (snmpOID.toString().equals("1.3.6.1.2.1.5.6.0") || snmpOID.toString().equals("1.3.6.1.2.1.5.6"))
	            {
	            	String line = "";
	            	ArrayList<String> lines = new ArrayList<String>();
	        		try
	                {
	            		File archivo = new File("/proc/net/snmp");
	        			FileReader fileReader = new FileReader (archivo);
	        			BufferedReader buffReader = new BufferedReader(fileReader);
	            		while ((line =buffReader.readLine()) != null) {
	        				lines.add(line);
	        			}
	            		if( null != fileReader)
							fileReader.close();
	            		
	        			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
	        			String value = "InSrcQuenchs";
	        			arrName= new ArrayList<String>(Arrays.asList(lines.get(2).split(" "))); // IP Names
	        			arrValue= new ArrayList<String>(Arrays.asList(lines.get(3).split(" "))); // IP Values
	        			int pos = 0;
	        			for (String stra : arrName) {
	        				if(stra.equalsIgnoreCase(value)) break;
	        				else pos++;
	        			}
	        			
	        			long recv = Long.parseLong( arrValue.get(pos));
	        			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(recv));
	                    responseList.addSNMPObject(newPair);
	                }catch (Exception e){
	                	errorIndex = i+1;
	                	errorStatus = SNMPRequestException.FAILED;
	                }
	                
	                continue;
	            } // icmpInSrcQuenchs
	            
	            
	            // icmpInRedirects | Counter | Read-Only | Mandatory 
	            if (snmpOID.toString().equals("1.3.6.1.2.1.5.7.0") || snmpOID.toString().equals("1.3.6.1.2.1.5.7"))
	            {
	            	String line = "";
	            	ArrayList<String> lines = new ArrayList<String>();
	        		try
	                {
	            		File archivo = new File("/proc/net/snmp");
	        			FileReader fileReader = new FileReader (archivo);
	        			BufferedReader buffReader = new BufferedReader(fileReader);
	            		while ((line =buffReader.readLine()) != null) {
	        				lines.add(line);
	        			}
	            		if( null != fileReader)
							fileReader.close();
	            		
	        			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
	        			String value = "InRedirects";
	        			arrName= new ArrayList<String>(Arrays.asList(lines.get(2).split(" "))); // IP Names
	        			arrValue= new ArrayList<String>(Arrays.asList(lines.get(3).split(" "))); // IP Values
	        			int pos = 0;
	        			for (String stra : arrName) {
	        				if(stra.equalsIgnoreCase(value)) break;
	        				else pos++;
	        			}
	        			
	        			long recv = Long.parseLong( arrValue.get(pos));
	        			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(recv));
	                    responseList.addSNMPObject(newPair);
	                }catch (Exception e){
	                	errorIndex = i+1;
	                	errorStatus = SNMPRequestException.FAILED;
	                }
	                
	                continue;
	            } // icmpInRedirects
	            
	            
	            // icmpInEchos | Counter | Read-Only | Mandatory 
	            if (snmpOID.toString().equals("1.3.6.1.2.1.5.8.0") || snmpOID.toString().equals("1.3.6.1.2.1.5.8"))
	            {
	            	String line = "";
	            	ArrayList<String> lines = new ArrayList<String>();
	        		try
	                {
	            		File archivo = new File("/proc/net/snmp");
	        			FileReader fileReader = new FileReader (archivo);
	        			BufferedReader buffReader = new BufferedReader(fileReader);
	            		while ((line =buffReader.readLine()) != null) {
	        				lines.add(line);
	        			}
	            		if( null != fileReader)
							fileReader.close();
	            		
	        			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
	        			String value = "InEchos";
	        			arrName= new ArrayList<String>(Arrays.asList(lines.get(2).split(" "))); // IP Names
	        			arrValue= new ArrayList<String>(Arrays.asList(lines.get(3).split(" "))); // IP Values
	        			int pos = 0;
	        			for (String stra : arrName) {
	        				if(stra.equalsIgnoreCase(value)) break;
	        				else pos++;
	        			}
	        			
	        			long recv = Long.parseLong( arrValue.get(pos));
	        			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(recv));
	                    responseList.addSNMPObject(newPair);
	                }catch (Exception e){
	                	errorIndex = i+1;
	                	errorStatus = SNMPRequestException.FAILED;
	                }
	                
	                continue;
	            } // icmpInEchos
	            
	            
	            // icmpInEchoReps | Counter | Read-Only | Mandatory 
	            if (snmpOID.toString().equals("1.3.6.1.2.1.5.9.0") || snmpOID.toString().equals("1.3.6.1.2.1.5.9"))
	            {
	            	String line = "";
	            	ArrayList<String> lines = new ArrayList<String>();
	        		try
	                {
	            		File archivo = new File("/proc/net/snmp");
	        			FileReader fileReader = new FileReader (archivo);
	        			BufferedReader buffReader = new BufferedReader(fileReader);
	            		while ((line =buffReader.readLine()) != null) {
	        				lines.add(line);
	        			}
	            		if( null != fileReader)
							fileReader.close();
	            		
	        			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
	        			String value = "InEchoReps";
	        			arrName= new ArrayList<String>(Arrays.asList(lines.get(2).split(" "))); // IP Names
	        			arrValue= new ArrayList<String>(Arrays.asList(lines.get(3).split(" "))); // IP Values
	        			int pos = 0;
	        			for (String stra : arrName) {
	        				if(stra.equalsIgnoreCase(value)) break;
	        				else pos++;
	        			}
	        			
	        			long recv = Long.parseLong( arrValue.get(pos));
	        			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(recv));
	                    responseList.addSNMPObject(newPair);
	                }catch (Exception e){
	                	errorIndex = i+1;
	                	errorStatus = SNMPRequestException.FAILED;
	                }
	                
	                continue;
	            } // icmpInEchoReps
	          
	            
	         // icmpInTimestamps | Counter | Read-Only | Mandatory 
	            if (snmpOID.toString().equals("1.3.6.1.2.1.5.10.0") || snmpOID.toString().equals("1.3.6.1.2.1.5.10"))
	            {
	            	String line = "";
	            	ArrayList<String> lines = new ArrayList<String>();
	        		try
	                {
	            		File archivo = new File("/proc/net/snmp");
	        			FileReader fileReader = new FileReader (archivo);
	        			BufferedReader buffReader = new BufferedReader(fileReader);
	            		while ((line =buffReader.readLine()) != null) {
	        				lines.add(line);
	        			}
	            		if( null != fileReader)
							fileReader.close();
	            		
	        			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
	        			String value = "InTimestamps";
	        			arrName= new ArrayList<String>(Arrays.asList(lines.get(2).split(" "))); // IP Names
	        			arrValue= new ArrayList<String>(Arrays.asList(lines.get(3).split(" "))); // IP Values
	        			int pos = 0;
	        			for (String stra : arrName) {
	        				if(stra.equalsIgnoreCase(value)) break;
	        				else pos++;
	        			}
	        			
	        			long recv = Long.parseLong( arrValue.get(pos));
	        			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(recv));
	                    responseList.addSNMPObject(newPair);
	                }catch (Exception e){
	                	errorIndex = i+1;
	                	errorStatus = SNMPRequestException.FAILED;
	                }
	                
	                continue;
	            } // icmpInTimestamps
	            
	            // icmpInTimestampReps | Counter | Read-Only | Mandatory 
	            if (snmpOID.toString().equals("1.3.6.1.2.1.5.11.0") || snmpOID.toString().equals("1.3.6.1.2.1.5.11"))
	            {
	            	String line = "";
	            	ArrayList<String> lines = new ArrayList<String>();
	        		try
	                {
	            		File archivo = new File("/proc/net/snmp");
	        			FileReader fileReader = new FileReader (archivo);
	        			BufferedReader buffReader = new BufferedReader(fileReader);
	            		while ((line =buffReader.readLine()) != null) {
	        				lines.add(line);
	        			}
	            		if( null != fileReader)
							fileReader.close();
	            		
	        			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
	        			String value = "InTimestampReps";
	        			arrName= new ArrayList<String>(Arrays.asList(lines.get(2).split(" "))); // IP Names
	        			arrValue= new ArrayList<String>(Arrays.asList(lines.get(3).split(" "))); // IP Values
	        			int pos = 0;
	        			for (String stra : arrName) {
	        				if(stra.equalsIgnoreCase(value)) break;
	        				else pos++;
	        			}
	        			
	        			long recv = Long.parseLong( arrValue.get(pos));
	        			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(recv));
	                    responseList.addSNMPObject(newPair);
	                }catch (Exception e){
	                	errorIndex = i+1;
	                	errorStatus = SNMPRequestException.FAILED;
	                }
	                
	                continue;
	            } // icmpInTimestampReps
	            
	            
	         // icmpInAddrMasks | Counter | Read-Only | Mandatory 
	            if (snmpOID.toString().equals("1.3.6.1.2.1.5.12.0") || snmpOID.toString().equals("1.3.6.1.2.1.5.12"))
	            {
	            	String line = "";
	            	ArrayList<String> lines = new ArrayList<String>();
	        		try
	                {
	            		File archivo = new File("/proc/net/snmp");
	        			FileReader fileReader = new FileReader (archivo);
	        			BufferedReader buffReader = new BufferedReader(fileReader);
	            		while ((line =buffReader.readLine()) != null) {
	        				lines.add(line);
	        			}
	            		if( null != fileReader)
							fileReader.close();
	            		
	        			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
	        			String value = "InAddrMasks";
	        			arrName= new ArrayList<String>(Arrays.asList(lines.get(2).split(" "))); // IP Names
	        			arrValue= new ArrayList<String>(Arrays.asList(lines.get(3).split(" "))); // IP Values
	        			int pos = 0;
	        			for (String stra : arrName) {
	        				if(stra.equalsIgnoreCase(value)) break;
	        				else pos++;
	        			}
	        			
	        			long recv = Long.parseLong( arrValue.get(pos));
	        			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(recv));
	                    responseList.addSNMPObject(newPair);
	                }catch (Exception e){
	                	errorIndex = i+1;
	                	errorStatus = SNMPRequestException.FAILED;
	                }
	                
	                continue;
	            } // icmpInAddrMasks
	            
	            
	         // icmpInAddrMaskReps | Counter | Read-Only | Mandatory 
	            if (snmpOID.toString().equals("1.3.6.1.2.1.5.13.0") || snmpOID.toString().equals("1.3.6.1.2.1.5.13"))
	            {
	            	String line = "";
	            	ArrayList<String> lines = new ArrayList<String>();
	        		try
	                {
	            		File archivo = new File("/proc/net/snmp");
	        			FileReader fileReader = new FileReader (archivo);
	        			BufferedReader buffReader = new BufferedReader(fileReader);
	            		while ((line =buffReader.readLine()) != null) {
	        				lines.add(line);
	        			}
	            		if( null != fileReader)
							fileReader.close();
	            		
	        			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
	        			String value = "InAddrMaskReps";
	        			arrName= new ArrayList<String>(Arrays.asList(lines.get(2).split(" "))); // IP Names
	        			arrValue= new ArrayList<String>(Arrays.asList(lines.get(3).split(" "))); // IP Values
	        			int pos = 0;
	        			for (String stra : arrName) {
	        				if(stra.equalsIgnoreCase(value)) break;
	        				else pos++;
	        			}
	        			
	        			long recv = Long.parseLong( arrValue.get(pos));
	        			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(recv));
	                    responseList.addSNMPObject(newPair);
	                }catch (Exception e){
	                	errorIndex = i+1;
	                	errorStatus = SNMPRequestException.FAILED;
	                }
	                
	                continue;
	            } // icmpInAddrMaskReps
	            
	            
	            // icmpOutMsgs | Counter | Read-Only | Mandatory 
	            if (snmpOID.toString().equals("1.3.6.1.2.1.5.14.0") || snmpOID.toString().equals("1.3.6.1.2.1.5.14"))
	            {
	            	String line = "";
	            	ArrayList<String> lines = new ArrayList<String>();
	        		try
	                {
	            		File archivo = new File("/proc/net/snmp");
	        			FileReader fileReader = new FileReader (archivo);
	        			BufferedReader buffReader = new BufferedReader(fileReader);
	            		while ((line =buffReader.readLine()) != null) {
	        				lines.add(line);
	        			}
	            		if( null != fileReader)
							fileReader.close();
	            		
	        			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
	        			String value = "OutMsgs";
	        			arrName= new ArrayList<String>(Arrays.asList(lines.get(2).split(" "))); // IP Names
	        			arrValue= new ArrayList<String>(Arrays.asList(lines.get(3).split(" "))); // IP Values
	        			int pos = 0;
	        			for (String stra : arrName) {
	        				if(stra.equalsIgnoreCase(value)) break;
	        				else pos++;
	        			}
	        			
	        			long recv = Long.parseLong( arrValue.get(pos));
	        			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(recv));
	                    responseList.addSNMPObject(newPair);
	                }catch (Exception e){
	                	errorIndex = i+1;
	                	errorStatus = SNMPRequestException.FAILED;
	                }
	                
	                continue;
	            } // icmpOutMsgs
	            
	            
	            // icmpOutErrors | Counter | Read-Only | Mandatory 
	            if (snmpOID.toString().equals("1.3.6.1.2.1.5.15.0") || snmpOID.toString().equals("1.3.6.1.2.1.5.15"))
	            {
	            	String line = "";
	            	ArrayList<String> lines = new ArrayList<String>();
	        		try
	                {
	            		File archivo = new File("/proc/net/snmp");
	        			FileReader fileReader = new FileReader (archivo);
	        			BufferedReader buffReader = new BufferedReader(fileReader);
	            		while ((line =buffReader.readLine()) != null) {
	        				lines.add(line);
	        			}
	            		if( null != fileReader)
							fileReader.close();
	            		
	        			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
	        			String value = "OutErrors";
	        			arrName= new ArrayList<String>(Arrays.asList(lines.get(2).split(" "))); // IP Names
	        			arrValue= new ArrayList<String>(Arrays.asList(lines.get(3).split(" "))); // IP Values
	        			int pos = 0;
	        			for (String stra : arrName) {
	        				if(stra.equalsIgnoreCase(value)) break;
	        				else pos++;
	        			}
	        			
	        			long recv = Long.parseLong( arrValue.get(pos));
	        			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(recv));
	                    responseList.addSNMPObject(newPair);
	                }catch (Exception e){
	                	errorIndex = i+1;
	                	errorStatus = SNMPRequestException.FAILED;
	                }
	                
	                continue;
	            } // icmpOutErrors
	            
	            // icmpOutDestUnreachs | Counter | Read-Only | Mandatory 
	            if (snmpOID.toString().equals("1.3.6.1.2.1.5.16.0") || snmpOID.toString().equals("1.3.6.1.2.1.5.16"))
	            {
	            	String line = "";
	            	ArrayList<String> lines = new ArrayList<String>();
	        		try
	                {
	            		File archivo = new File("/proc/net/snmp");
	        			FileReader fileReader = new FileReader (archivo);
	        			BufferedReader buffReader = new BufferedReader(fileReader);
	            		while ((line =buffReader.readLine()) != null) {
	        				lines.add(line);
	        			}
	            		if( null != fileReader)
							fileReader.close();
	            		
	        			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
	        			String value = "OutDestUnreachs";
	        			arrName= new ArrayList<String>(Arrays.asList(lines.get(2).split(" "))); // IP Names
	        			arrValue= new ArrayList<String>(Arrays.asList(lines.get(3).split(" "))); // IP Values
	        			int pos = 0;
	        			for (String stra : arrName) {
	        				if(stra.equalsIgnoreCase(value)) break;
	        				else pos++;
	        			}
	        			
	        			long recv = Long.parseLong( arrValue.get(pos));
	        			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(recv));
	                    responseList.addSNMPObject(newPair);
	                }catch (Exception e){
	                	errorIndex = i+1;
	                	errorStatus = SNMPRequestException.FAILED;
	                }
	                
	                continue;
	            } // icmpOutDestUnreachs
	            
	            
	            // icmpOutTimeExcds | Counter | Read-Only | Mandatory 
	            if (snmpOID.toString().equals("1.3.6.1.2.1.5.17.0") || snmpOID.toString().equals("1.3.6.1.2.1.5.17"))
	            {
	            	String line = "";
	            	ArrayList<String> lines = new ArrayList<String>();
	        		try
	                {
	            		File archivo = new File("/proc/net/snmp");
	        			FileReader fileReader = new FileReader (archivo);
	        			BufferedReader buffReader = new BufferedReader(fileReader);
	            		while ((line =buffReader.readLine()) != null) {
	        				lines.add(line);
	        			}
	            		if( null != fileReader)
							fileReader.close();
	            		
	        			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
	        			String value = "OutTimeExcds";
	        			arrName= new ArrayList<String>(Arrays.asList(lines.get(2).split(" "))); // IP Names
	        			arrValue= new ArrayList<String>(Arrays.asList(lines.get(3).split(" "))); // IP Values
	        			int pos = 0;
	        			for (String stra : arrName) {
	        				if(stra.equalsIgnoreCase(value)) break;
	        				else pos++;
	        			}
	        			
	        			long recv = Long.parseLong( arrValue.get(pos));
	        			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(recv));
	                    responseList.addSNMPObject(newPair);
	                }catch (Exception e){
	                	errorIndex = i+1;
	                	errorStatus = SNMPRequestException.FAILED;
	                }
	                
	                continue;
	            } // icmpOutTimeExcds
	            
	            
	            // icmpOutParmProbs | Counter | Read-Only | Mandatory 
	            if (snmpOID.toString().equals("1.3.6.1.2.1.5.18.0") || snmpOID.toString().equals("1.3.6.1.2.1.5.18"))
	            {
	            	String line = "";
	            	ArrayList<String> lines = new ArrayList<String>();
	        		try
	                {
	            		File archivo = new File("/proc/net/snmp");
	        			FileReader fileReader = new FileReader (archivo);
	        			BufferedReader buffReader = new BufferedReader(fileReader);
	            		while ((line =buffReader.readLine()) != null) {
	        				lines.add(line);
	        			}
	            		if( null != fileReader)
							fileReader.close();
	            		
	        			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
	        			String value = "OutParmProbs";
	        			arrName= new ArrayList<String>(Arrays.asList(lines.get(2).split(" "))); // IP Names
	        			arrValue= new ArrayList<String>(Arrays.asList(lines.get(3).split(" "))); // IP Values
	        			int pos = 0;
	        			for (String stra : arrName) {
	        				if(stra.equalsIgnoreCase(value)) break;
	        				else pos++;
	        			}
	        			
	        			long recv = Long.parseLong( arrValue.get(pos));
	        			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(recv));
	                    responseList.addSNMPObject(newPair);
	                }catch (Exception e){
	                	errorIndex = i+1;
	                	errorStatus = SNMPRequestException.FAILED;
	                }
	                
	                continue;
	            } // icmpOutParmProbs
	            
	            
	            // icmpOutSrcQuenchs | Counter | Read-Only | Mandatory 
	            if (snmpOID.toString().equals("1.3.6.1.2.1.5.19.0") || snmpOID.toString().equals("1.3.6.1.2.1.5.19"))
	            {
	            	String line = "";
	            	ArrayList<String> lines = new ArrayList<String>();
	        		try
	                {
	            		File archivo = new File("/proc/net/snmp");
	        			FileReader fileReader = new FileReader (archivo);
	        			BufferedReader buffReader = new BufferedReader(fileReader);
	            		while ((line =buffReader.readLine()) != null) {
	        				lines.add(line);
	        			}
	            		if( null != fileReader)
							fileReader.close();
	        		
	        			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
	        			String value = "OutSrcQuenchs";
	        			arrName= new ArrayList<String>(Arrays.asList(lines.get(2).split(" "))); // IP Names
	        			arrValue= new ArrayList<String>(Arrays.asList(lines.get(3).split(" "))); // IP Values
	        			int pos = 0;
	        			for (String stra : arrName) {
	        				if(stra.equalsIgnoreCase(value)) break;
	        				else pos++;
	        			}
	        			
	        			long recv = Long.parseLong( arrValue.get(pos));
	        			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(recv));
	                    responseList.addSNMPObject(newPair);
	                }catch (Exception e){
	                	errorIndex = i+1;
	                	errorStatus = SNMPRequestException.FAILED;
	                }
	            
	                continue;
	            } // icmpOutSrcQuenchs
	            
	            
	            // icmpOutRedirects | Counter | Read-Only | Mandatory 
	            if (snmpOID.toString().equals("1.3.6.1.2.1.5.20.0") || snmpOID.toString().equals("1.3.6.1.2.1.5.20"))
	            {
	            	String line = "";
	            	ArrayList<String> lines = new ArrayList<String>();
	        		try
	                {
	            		File archivo = new File("/proc/net/snmp");
	        			FileReader fileReader = new FileReader (archivo);
	        			BufferedReader buffReader = new BufferedReader(fileReader);
	            		while ((line =buffReader.readLine()) != null) {
	        				lines.add(line);
	        			}
	            		if( null != fileReader)
							fileReader.close();
	        		
	        			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
	        			String value = "OutRedirects";
	        			arrName= new ArrayList<String>(Arrays.asList(lines.get(2).split(" "))); // IP Names
	        			arrValue= new ArrayList<String>(Arrays.asList(lines.get(3).split(" "))); // IP Values
	        			int pos = 0;
	        			for (String stra : arrName) {
	        				if(stra.equalsIgnoreCase(value)) break;
	        				else pos++;
	        			}
	        			
	        			long recv = Long.parseLong( arrValue.get(pos));
	        			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(recv));
	                    responseList.addSNMPObject(newPair);
	                }catch (Exception e){
	                	errorIndex = i+1;
	                	errorStatus = SNMPRequestException.FAILED;
	                }
	                
	                continue;
	            } // icmpOutRedirects
	            
	            
	            // icmpOutEchos | Counter | Read-Only | Mandatory 
	            if (snmpOID.toString().equals("1.3.6.1.2.1.5.21.0") || snmpOID.toString().equals("1.3.6.1.2.1.5.21"))
	            {
	            	String line = "";
	            	ArrayList<String> lines = new ArrayList<String>();
	        		try
	                {
	            		File archivo = new File("/proc/net/snmp");
	        			FileReader fileReader = new FileReader (archivo);
	        			BufferedReader buffReader = new BufferedReader(fileReader);
	            		while ((line =buffReader.readLine()) != null) {
	        				lines.add(line);
	        			}
	            		if( null != fileReader)
							fileReader.close();
	            		
	        			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
	        			String value = "OutEchos";
	        			arrName= new ArrayList<String>(Arrays.asList(lines.get(2).split(" "))); // IP Names
	        			arrValue= new ArrayList<String>(Arrays.asList(lines.get(3).split(" "))); // IP Values
	        			int pos = 0;
	        			for (String stra : arrName) {
	        				if(stra.equalsIgnoreCase(value)) break;
	        				else pos++;
	        			}
	        			
	        			long recv = Long.parseLong( arrValue.get(pos));
	        			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(recv));
	                    responseList.addSNMPObject(newPair);
	                }catch (Exception e){
	                	errorIndex = i+1;
	                	errorStatus = SNMPRequestException.FAILED;
	                }
	                
	                continue;
	            } // icmpOutEchos
	            
	            
	         // icmpOutEchoReps | Counter | Read-Only | Mandatory 
	            if (snmpOID.toString().equals("1.3.6.1.2.1.5.22.0") || snmpOID.toString().equals("1.3.6.1.2.1.5.22"))
	            {
	            	String line = "";
	            	ArrayList<String> lines = new ArrayList<String>();
	        		try
	                {
	            		File archivo = new File("/proc/net/snmp");
	        			FileReader fileReader = new FileReader (archivo);
	        			BufferedReader buffReader = new BufferedReader(fileReader);
	            		while ((line =buffReader.readLine()) != null) {
	        				lines.add(line);
	        			}
	            		if( null != fileReader)
							fileReader.close();
	            		
	            			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
	            			String value = "OutEchoReps";
	            			arrName= new ArrayList<String>(Arrays.asList(lines.get(2).split(" "))); // IP Names
	            			arrValue= new ArrayList<String>(Arrays.asList(lines.get(3).split(" "))); // IP Values
	            			int pos = 0;
	            			for (String stra : arrName) {
	            				if(stra.equalsIgnoreCase(value)) break;
	            				else pos++;
	            			}
	            			
	            			long recv = Long.parseLong( arrValue.get(pos));
	            			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(recv));
	                        responseList.addSNMPObject(newPair);
	                }catch (Exception e){
	                	errorIndex = i+1;
	                	errorStatus = SNMPRequestException.FAILED;
	                }
	                
	                continue;
	            } // icmpOutEchoReps
	            
	            
	            // icmpOutTimestamps | Counter | Read-Only | Mandatory 
	            if (snmpOID.toString().equals("1.3.6.1.2.1.5.23.0") || snmpOID.toString().equals("1.3.6.1.2.1.5.23"))
	            {
	            	String line = "";
	            	ArrayList<String> lines = new ArrayList<String>();
	        		try
	                {
	            		File archivo = new File("/proc/net/snmp");
	        			FileReader fileReader = new FileReader (archivo);
	        			BufferedReader buffReader = new BufferedReader(fileReader);
	            		while ((line =buffReader.readLine()) != null) {
	        				lines.add(line);
	        			}
	            		if( null != fileReader)
							fileReader.close();
	            		
	        			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
	        			String value = "OutTimestamps";
	        			arrName= new ArrayList<String>(Arrays.asList(lines.get(2).split(" "))); // IP Names
	        			arrValue= new ArrayList<String>(Arrays.asList(lines.get(3).split(" "))); // IP Values
	        			int pos = 0;
	        			for (String stra : arrName) {
	        				if(stra.equalsIgnoreCase(value)) break;
	        				else pos++;
	        			}
	        			
	        			long recv = Long.parseLong( arrValue.get(pos));
	        			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(recv));
	                    responseList.addSNMPObject(newPair);
	                }catch (Exception e){
	                	errorIndex = i+1;
	                	errorStatus = SNMPRequestException.FAILED;
	                }
	                
	                continue;
	            } // icmpOutTimestamps
	            
	            
	            // icmpOutTimestampReps | Counter | Read-Only | Mandatory 
	            if (snmpOID.toString().equals("1.3.6.1.2.1.5.24.0") || snmpOID.toString().equals("1.3.6.1.2.1.5.24"))
	            {
	            	String line = "";
	            	ArrayList<String> lines = new ArrayList<String>();
	        		try
	                {
	            		File archivo = new File("/proc/net/snmp");
	        			FileReader fileReader = new FileReader (archivo);
	        			BufferedReader buffReader = new BufferedReader(fileReader);
	            		while ((line =buffReader.readLine()) != null) {
	        				lines.add(line);
	        			}
	            		if( null != fileReader)
							fileReader.close();
	            		
	        			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
	        			String value = "OutTimestampReps";
	        			arrName= new ArrayList<String>(Arrays.asList(lines.get(2).split(" "))); // IP Names
	        			arrValue= new ArrayList<String>(Arrays.asList(lines.get(3).split(" "))); // IP Values
	        			int pos = 0;
	        			for (String stra : arrName) {
	        				if(stra.equalsIgnoreCase(value)) break;
	        				else pos++;
	        			}
	        			
	        			long recv = Long.parseLong( arrValue.get(pos));
	        			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(recv));
	                    responseList.addSNMPObject(newPair);
	                }catch (Exception e){
	                	errorIndex = i+1;
	                	errorStatus = SNMPRequestException.FAILED;
	                }
	                
	                continue;
	            } // icmpOutTimestampReps
	            
	            
	            // icmpOutAddrMasks | Counter | Read-Only | Mandatory 
	            if (snmpOID.toString().equals("1.3.6.1.2.1.5.25.0") || snmpOID.toString().equals("1.3.6.1.2.1.5.25"))
	            {
	            	String line = "";
	            	ArrayList<String> lines = new ArrayList<String>();
	        		try
	                {
	            		File archivo = new File("/proc/net/snmp");
	        			FileReader fileReader = new FileReader (archivo);
	        			BufferedReader buffReader = new BufferedReader(fileReader);
	            		while ((line =buffReader.readLine()) != null) {
	        				lines.add(line);
	        			}
	            		if( null != fileReader)
							fileReader.close();
	            		
	        			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
	        			String value = "OutAddrMasks";
	        			arrName= new ArrayList<String>(Arrays.asList(lines.get(2).split(" "))); // IP Names
	        			arrValue= new ArrayList<String>(Arrays.asList(lines.get(3).split(" "))); // IP Values
	        			int pos = 0;
	        			for (String stra : arrName) {
	        				if(stra.equalsIgnoreCase(value)) break;
	        				else pos++;
	        			}
	        			
	        			long recv = Long.parseLong( arrValue.get(pos));
	        			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(recv));
	                    responseList.addSNMPObject(newPair);
	                }catch (Exception e){
	                	errorIndex = i+1;
	                	errorStatus = SNMPRequestException.FAILED;
	                }
	                
	                continue;
	            } // icmpOutAddrMasks
	            
	            
	            // icmpOutAddrMaskReps | Counter | Read-Only | Mandatory 
	            if (snmpOID.toString().equals("1.3.6.1.2.1.5.26.0") || snmpOID.toString().equals("1.3.6.1.2.1.5.26"))
	            {
	            	String line = "";
	            	ArrayList<String> lines = new ArrayList<String>();
	        		try
	                {
	            		File archivo = new File("/proc/net/snmp");
	        			FileReader fileReader = new FileReader (archivo);
	        			BufferedReader buffReader = new BufferedReader(fileReader);
	            		while ((line =buffReader.readLine()) != null) {
	        				lines.add(line);
	        			}
	            		if( null != fileReader)
							fileReader.close();
	            		
	        			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
	        			String value = "OutAddrMaskReps";
	        			arrName= new ArrayList<String>(Arrays.asList(lines.get(2).split(" "))); // IP Names
	        			arrValue= new ArrayList<String>(Arrays.asList(lines.get(3).split(" "))); // IP Values
	        			int pos = 0;
	        			for (String stra : arrName) {
	        				if(stra.equalsIgnoreCase(value)) break;
	        				else pos++;
	        			}
	        			
	        			long recv = Long.parseLong( arrValue.get(pos));
	        			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(recv));
	                    responseList.addSNMPObject(newPair);
	                }catch (Exception e){
	                	errorIndex = i+1;
	                	errorStatus = SNMPRequestException.FAILED;
	                }
	                
	                continue;
	            } // icmpOutAddrMaskReps
	            
	            
            } // MIB-II |	the ICMP Group
            
            
				             /***************************
				             * 							*
				             * MIB-II |	the TCP Group   *
				             * 							*
				             ***************************/
            
            if(snmpOID.toString().startsWith("1.3.6.1.2.1.6"))
            {
            	
            	 // tcpRtoAlgorithm | Integer | Read-Only | Mandatory 
                if ( snmpOID.toString().equals("1.3.6.1.2.1.6.1.0") || snmpOID.toString().equals("1.3.6.1.2.1.6.1"))
                {
                	String line = "";
                	ArrayList<String> lines = new ArrayList<String>();
            		try
                    {
                		File archivo = new File("/proc/net/snmp");
            			FileReader fileReader = new FileReader (archivo);
            			BufferedReader buffReader = new BufferedReader(fileReader);
                		while ((line =buffReader.readLine()) != null) {
            				lines.add(line);
            			}
                		if( null != fileReader)
    						fileReader.close();

            			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
            			String value = "RtoAlgorithm";
            			arrName= new ArrayList<String>(Arrays.asList(lines.get(6).split(" "))); // IP Names
            			arrValue= new ArrayList<String>(Arrays.asList(lines.get(7).split(" "))); // IP Values
            			int pos = 0;
            			for (String stra : arrName) {
            				if(stra.equalsIgnoreCase(value)) break;
            				else pos++;
            			}
            			
            			int recv = Integer.parseInt(arrValue.get(pos));
            			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPInteger(recv));
                        responseList.addSNMPObject(newPair);
                    }catch (Exception e){
                    	errorIndex = i+1;
                    	errorStatus = SNMPRequestException.FAILED;
                    }
                    
                    continue;
                } // tcpRtoAlgorithm
                
                
                // tcpRtoMin | Integer | Read-Only | Mandatory 
                if (snmpOID.toString().equals("1.3.6.1.2.1.6.2.0") || snmpOID.toString().equals("1.3.6.1.2.1.6.2"))
                {
                	String line = "";
                	ArrayList<String> lines = new ArrayList<String>();
            		try
                    {
                		File archivo = new File("/proc/net/snmp");
            			FileReader fileReader = new FileReader (archivo);
            			BufferedReader buffReader = new BufferedReader(fileReader);
                		while ((line =buffReader.readLine()) != null) {
            				lines.add(line);
            			}
                		if( null != fileReader)
    						fileReader.close();

        				ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
            			String value = "RtoMin";
            			arrName= new ArrayList<String>(Arrays.asList(lines.get(6).split(" "))); // IP Names
            			arrValue= new ArrayList<String>(Arrays.asList(lines.get(7).split(" "))); // IP Values
            			int pos = 0;
            			for (String stra : arrName) {
            				if(stra.equalsIgnoreCase(value)) break;
            				else pos++;
            			}
            			
            			int recv = Integer.parseInt(arrValue.get(pos));
            			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPInteger(recv));
                        responseList.addSNMPObject(newPair);
                    }catch (Exception e){
                    	errorIndex = i+1;
                    	errorStatus = SNMPRequestException.FAILED;
                    }
                    
                    continue;
                } // tcpRtoMin
                
                
                // tcpRtoMax | Integer | Read-Only | Mandatory 
                if (snmpOID.toString().equals("1.3.6.1.2.1.6.3.0") || snmpOID.toString().equals("1.3.6.1.2.1.6.3"))
                {
                	String line = "";
                	ArrayList<String> lines = new ArrayList<String>();
            		try
                    {
                		File archivo = new File("/proc/net/snmp");
            			FileReader fileReader = new FileReader (archivo);
            			BufferedReader buffReader = new BufferedReader(fileReader);
                		while ((line =buffReader.readLine()) != null) {
            				lines.add(line);
            			}
                		if( null != fileReader)
    						fileReader.close();

            			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
            			String value = "RtoMax";
            			arrName= new ArrayList<String>(Arrays.asList(lines.get(6).split(" "))); // IP Names
            			arrValue= new ArrayList<String>(Arrays.asList(lines.get(7).split(" "))); // IP Values
            			int pos = 0;
            			for (String stra : arrName) {
            				if(stra.equalsIgnoreCase(value)) break;
            				else pos++;
            			}
            			
            			int recv = Integer.parseInt(arrValue.get(pos));
            			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPInteger(recv));
                        responseList.addSNMPObject(newPair);
                    }catch (Exception e){
                    	errorIndex = i+1;
                    	errorStatus = SNMPRequestException.FAILED;
                    }
                    
                    continue;
                } // tcpRtoMax
                
                
                // tcpMaxConn | Integer | Read-Only | Mandatory 
                if (snmpOID.toString().equals("1.3.6.1.2.1.6.4.0") || snmpOID.toString().equals("1.3.6.1.2.1.6.4"))
                {
                	String line = "";
                	ArrayList<String> lines = new ArrayList<String>();
            		try
                    {
                		File archivo = new File("/proc/net/snmp");
            			FileReader fileReader = new FileReader (archivo);
            			BufferedReader buffReader = new BufferedReader(fileReader);
                		while ((line =buffReader.readLine()) != null) {
            				lines.add(line);
            			}
                		if( null != fileReader)
    						fileReader.close();


            			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
            			String value = "MaxConn";
            			arrName= new ArrayList<String>(Arrays.asList(lines.get(6).split(" "))); // IP Names
            			arrValue= new ArrayList<String>(Arrays.asList(lines.get(7).split(" "))); // IP Values
            			int pos = 0;
            			for (String stra : arrName) {
            				if(stra.equalsIgnoreCase(value)) break;
            				else pos++;
            			}
            			
            			int recv = Integer.parseInt(arrValue.get(pos));
            			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPInteger(recv));
                        responseList.addSNMPObject(newPair);
                    }catch (Exception e){
                    	errorIndex = i+1;
                    	errorStatus = SNMPRequestException.FAILED;
                    }
                    
                    continue;
                } // tcpMaxConn
                
                
                // tcpActiveOpens | Counter | Read-Only | Mandatory 
                if (snmpOID.toString().equals("1.3.6.1.2.1.6.5.0") || snmpOID.toString().equals("1.3.6.1.2.1.6.5"))
                {
                	String line = "";
                	ArrayList<String> lines = new ArrayList<String>();
            		try
                    {
                		File archivo = new File("/proc/net/snmp");
            			FileReader fileReader = new FileReader (archivo);
            			BufferedReader buffReader = new BufferedReader(fileReader);
                		while ((line =buffReader.readLine()) != null) {
            				lines.add(line);
            			}
                		if( null != fileReader)
    						fileReader.close();

            			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
            			String value = "ActiveOpens";
            			arrName= new ArrayList<String>(Arrays.asList(lines.get(6).split(" "))); // IP Names
            			arrValue= new ArrayList<String>(Arrays.asList(lines.get(7).split(" "))); // IP Values
            			int pos = 0;
            			for (String stra : arrName) {
            				if(stra.equalsIgnoreCase(value)) break;
            				else pos++;
            			}
            			
            			Long recv = Long.parseLong(arrValue.get(pos));
            			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(recv));
                        responseList.addSNMPObject(newPair);
                    }catch (Exception e){
                    	errorIndex = i+1;
                    	errorStatus = SNMPRequestException.FAILED;
                    }
                    
                    continue;
                } // tcpActiveOpens
                
                
                // tcpPassiveOpens | Counter | Read-Only | Mandatory 
                if (snmpOID.toString().equals("1.3.6.1.2.1.6.6.0") || snmpOID.toString().equals("1.3.6.1.2.1.6.6"))
                {
                	String line = "";
                	ArrayList<String> lines = new ArrayList<String>();
            		try
                    {
                		File archivo = new File("/proc/net/snmp");
            			FileReader fileReader = new FileReader (archivo);
            			BufferedReader buffReader = new BufferedReader(fileReader);
                		while ((line =buffReader.readLine()) != null) {
            				lines.add(line);
            			}
                		if( null != fileReader)
    						fileReader.close();

            			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
            			String value = "PassiveOpens";
            			arrName= new ArrayList<String>(Arrays.asList(lines.get(6).split(" "))); // IP Names
            			arrValue= new ArrayList<String>(Arrays.asList(lines.get(7).split(" "))); // IP Values
            			int pos = 0;
            			for (String stra : arrName) {
            				if(stra.equalsIgnoreCase(value)) break;
            				else pos++;
            			}
            			
            			Long recv = Long.parseLong(arrValue.get(pos));
            			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(recv));
                        responseList.addSNMPObject(newPair);
                    }catch (Exception e){
                    	errorIndex = i+1;
                    	errorStatus = SNMPRequestException.FAILED;
                    }
            		
                    continue;
                } // tcpPassiveOpens
                
                // tcpAttemptFails | Counter | Read-Only | Mandatory 
                if (snmpOID.toString().equals("1.3.6.1.2.1.6.7.0") || snmpOID.toString().equals("1.3.6.1.2.1.6.7"))
                {
                	String line = "";
                	ArrayList<String> lines = new ArrayList<String>();
            		try
                    {
                		File archivo = new File("/proc/net/snmp");
            			FileReader fileReader = new FileReader (archivo);
            			BufferedReader buffReader = new BufferedReader(fileReader);
                		while ((line =buffReader.readLine()) != null) {
            				lines.add(line);
            			}
                		if( null != fileReader)
    						fileReader.close();

            			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
            			String value = "AttemptFails";
            			arrName= new ArrayList<String>(Arrays.asList(lines.get(6).split(" "))); // IP Names
            			arrValue= new ArrayList<String>(Arrays.asList(lines.get(7).split(" "))); // IP Values
            			int pos = 0;
            			for (String stra : arrName) {
            				if(stra.equalsIgnoreCase(value)) break;
            				else pos++;
            			}
            			
            			Long recv = Long.parseLong(arrValue.get(pos));
            			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(recv));
                        responseList.addSNMPObject(newPair);
                    }catch (Exception e){
                    	errorIndex = i+1;
                    	errorStatus = SNMPRequestException.FAILED;
                    }
                    
                    continue;
                } // tcpAttemptFails
                
                
                // tcpEstabResets | Counter | Read-Only | Mandatory 
                if (snmpOID.toString().equals("1.3.6.1.2.1.6.8.0") || snmpOID.toString().equals("1.3.6.1.2.1.6.8"))
                {
                	String line = "";
                	ArrayList<String> lines = new ArrayList<String>();
            		try
                    {
                		File archivo = new File("/proc/net/snmp");
            			FileReader fileReader = new FileReader (archivo);
            			BufferedReader buffReader = new BufferedReader(fileReader);
                		while ((line =buffReader.readLine()) != null) {
            				lines.add(line);
            			}
                		if( null != fileReader)
    						fileReader.close();

            			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
            			String value = "EstabResets";
            			arrName= new ArrayList<String>(Arrays.asList(lines.get(6).split(" "))); // IP Names
            			arrValue= new ArrayList<String>(Arrays.asList(lines.get(7).split(" "))); // IP Values
            			int pos = 0;
            			for (String stra : arrName) {
            				if(stra.equalsIgnoreCase(value)) break;
            				else pos++;
            			}
            			
            			Long recv = Long.parseLong(arrValue.get(pos));
            			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(recv));
                        responseList.addSNMPObject(newPair);
                    }catch (Exception e){
                    	errorIndex = i+1;
                    	errorStatus = SNMPRequestException.FAILED;
                    }
                    
                    continue;
                } // tcpEstabResets
            	
                
                // tcpCurrEstab | Counter | Read-Only | Mandatory 
                if (snmpOID.toString().equals("1.3.6.1.2.1.6.9.0") || snmpOID.toString().equals("1.3.6.1.2.1.6.9"))
                {
                	String line = "";
                	ArrayList<String> lines = new ArrayList<String>();
            		try
                    {
                		File archivo = new File("/proc/net/snmp");
            			FileReader fileReader = new FileReader (archivo);
            			BufferedReader buffReader = new BufferedReader(fileReader);
                		while ((line =buffReader.readLine()) != null) {
            				lines.add(line);
            			}
                		if( null != fileReader)
    						fileReader.close();

            			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
            			String value = "CurrEstab";
            			arrName= new ArrayList<String>(Arrays.asList(lines.get(6).split(" "))); // IP Names
            			arrValue= new ArrayList<String>(Arrays.asList(lines.get(7).split(" "))); // IP Values
            			int pos = 0;
            			for (String stra : arrName) {
            				if(stra.equalsIgnoreCase(value)) break;
            				else pos++;
            			}
            			
            			Long recv = Long.parseLong(arrValue.get(pos));
            			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(recv));
                        responseList.addSNMPObject(newPair);
                    }catch (Exception e){
                    	errorIndex = i+1;
                    	errorStatus = SNMPRequestException.FAILED;
                    }
                    
                    continue;
                } // tcpCurrEstab
                
                
                // tcpInSegs | Counter | Read-Only | Mandatory 
                if (snmpOID.toString().equals("1.3.6.1.2.1.6.10.0") || snmpOID.toString().equals("1.3.6.1.2.1.6.10"))
                {
                	String line = "";
                	ArrayList<String> lines = new ArrayList<String>();
            		try
                    {
                		File archivo = new File("/proc/net/snmp");
            			FileReader fileReader = new FileReader (archivo);
            			BufferedReader buffReader = new BufferedReader(fileReader);
                		while ((line =buffReader.readLine()) != null) {
            				lines.add(line);
            			}
                		if( null != fileReader)
    						fileReader.close();

            			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
            			String value = "InSegs";
            			arrName= new ArrayList<String>(Arrays.asList(lines.get(6).split(" "))); // IP Names
            			arrValue= new ArrayList<String>(Arrays.asList(lines.get(7).split(" "))); // IP Values
            			int pos = 0;
            			for (String stra : arrName) {
            				if(stra.equalsIgnoreCase(value)) break;
            				else pos++;
            			}
            			
            			Long recv = Long.parseLong(arrValue.get(pos));
            			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(recv));
                        responseList.addSNMPObject(newPair);
                    }catch (Exception e){
                    	errorIndex = i+1;
                    	errorStatus = SNMPRequestException.FAILED;
                    }
                    
                    continue;
                } // tcpInSegs
                
                
                // tcpOutSegs | Counter | Read-Only | Mandatory 
                if (snmpOID.toString().equals("1.3.6.1.2.1.6.11.0") || snmpOID.toString().equals("1.3.6.1.2.1.6.11"))
                {
                	String line = "";
                	ArrayList<String> lines = new ArrayList<String>();
            		try
                    {
                		File archivo = new File("/proc/net/snmp");
            			FileReader fileReader = new FileReader (archivo);
            			BufferedReader buffReader = new BufferedReader(fileReader);
                		while ((line =buffReader.readLine()) != null) {
            				lines.add(line);
            			}
                		if( null != fileReader)
    						fileReader.close();

            			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
            			String value = "OutSegs";
            			arrName= new ArrayList<String>(Arrays.asList(lines.get(6).split(" "))); // IP Names
            			arrValue= new ArrayList<String>(Arrays.asList(lines.get(7).split(" "))); // IP Values
            			int pos = 0;
            			for (String stra : arrName) {
            				if(stra.equalsIgnoreCase(value)) break;
            				else pos++;
            			}
            			
            			Long recv = Long.parseLong(arrValue.get(pos));
            			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(recv));
                        responseList.addSNMPObject(newPair);
                    }catch (Exception e){
                    	errorIndex = i+1;
                    	errorStatus = SNMPRequestException.FAILED;
                    }
                    
                    continue;
                } // tcpOutSegs
                
                
                // tcpRetransSegs | Counter | Read-Only | Mandatory 
                if (snmpOID.toString().equals("1.3.6.1.2.1.6.12.0") || snmpOID.toString().equals("1.3.6.1.2.1.6.12"))
                {
                	String line = "";
                	ArrayList<String> lines = new ArrayList<String>();
            		try
                    {
                		File archivo = new File("/proc/net/snmp");
            			FileReader fileReader = new FileReader (archivo);
            			BufferedReader buffReader = new BufferedReader(fileReader);
                		while ((line =buffReader.readLine()) != null) {
            				lines.add(line);
            			}
                		if( null != fileReader)
    						fileReader.close();
                		
            			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
            			String value = "RetransSegs";
            			arrName= new ArrayList<String>(Arrays.asList(lines.get(6).split(" "))); // IP Names
            			arrValue= new ArrayList<String>(Arrays.asList(lines.get(7).split(" "))); // IP Values
            			int pos = 0;
            			for (String stra : arrName) {
            				if(stra.equalsIgnoreCase(value)) break;
            				else pos++;
            			}
            			
            			Long recv = Long.parseLong(arrValue.get(pos));
            			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(recv));
                        responseList.addSNMPObject(newPair);
                    }catch (Exception e){
                    	errorIndex = i+1;
                    	errorStatus = SNMPRequestException.FAILED;
                    }
                    
                    continue;
                } // tcpRetransSegs
                
			                 /*********************************
				             * 							      *
				             *   the TCP Connection Table     *
				             * tcpConnTable 1.3.6.1.2.1.6.13  *
				             * 							      *
				             *********************************/
			                
                // tcpConnEntry
                if (snmpOID.toString().startsWith("1.3.6.1.2.1.6.13.1."))
                {
                	// tcpConnState | Integer | Read-Write | Mandatory
                	if (snmpOID.toString().startsWith("1.3.6.1.2.1.6.13.1.1"))
                    {
                		String cmd="netstat\n", line, oidConn="";
                		Process p=null;
                    	ArrayList<String> lines = new ArrayList<String>();
                		BufferedReader in2=null;
                		int stateConn=0;
                    	try
                        {
                    		p = Runtime.getRuntime().exec(cmd);
                			in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
                			while ((line =in2.readLine()) != null) {
                				lines.add(line);
                			}
                			/* Comienzo en 1 para saltar esta linea:
                			 * Proto Recv-Q Send-Q Local_Address Foreign_Address State
                			 */
     		
    						String matNetstat[][] = new String[lines.size()-1][6];

                			for (int j = 1; j < lines.size(); j++) {
    							String arrTmp[] = lines.get(j).split(" ");
    							for (int k = 0, x = 0; k < arrTmp.length; k++) {
    								if(!arrTmp[k].equalsIgnoreCase("")){
    									matNetstat[j-1][x] = arrTmp[k];
    									x++;
    								}
    							}
    						}
                			
                			for (int j = 0; j < matNetstat.length; j++) {
    							
                				if(matNetstat[j][0].equalsIgnoreCase("tcp6")){
                					
                					String arr[] = matNetstat[j][3].split(":");
                					// :::* -> [][][][*]
                					if(arr[arr.length-2].equalsIgnoreCase("") && arr[arr.length-1].equalsIgnoreCase("*")){
                						oidConn = "0.0.0.0.0"; // 0.0.0.0:0
                					}else{ // ::1:25 -> [][][1][25]
                						if(arr[arr.length-2].equalsIgnoreCase("1") && !arr[arr.length-1].equalsIgnoreCase("")){
                							oidConn = "127.0.0.1."+arr[arr.length-1]; // 127.0.0.1:25
                						}else{ // 0.0.0.0:* -> [0.0.0.0][*]
                							if(arr[arr.length-2].equalsIgnoreCase("0.0.0.0") && arr[arr.length-1].equalsIgnoreCase("*")){
                    							oidConn = "0.0.0.0.0"; // 0.0.0.0.0
                							}else{
                								oidConn = arr[arr.length-2]+"."+ arr[arr.length-1];
                							}
                						}
                					}
                					 
    								arr = matNetstat[j][4].split(":");
    								
    								if(arr[arr.length-2].equalsIgnoreCase("") && arr[arr.length-1].equalsIgnoreCase("*")){
                						oidConn = ".0.0.0.0.0"; // 0.0.0.0:0
                					}else{ // ::1:25 -> [][][1][25]
                						if(arr[arr.length-2].equalsIgnoreCase("1") && !arr[arr.length-1].equalsIgnoreCase("")){
                							oidConn = ".127.0.0.1."+arr[arr.length-1]; // 127.0.0.1:25
                						}else{ // 0.0.0.0:* -> [0.0.0.0][*]
                							if(arr[arr.length-2].equalsIgnoreCase("0.0.0.0") && arr[arr.length-1].equalsIgnoreCase("*")){
                    							oidConn = ".0.0.0.0.0"; // 0.0.0.0.0
                							}else{
                								oidConn += "."+arr[arr.length-2]+"."+ arr[arr.length-1];
                							}
                						}
                					}			
    								
                				}else if(matNetstat[j][0].equalsIgnoreCase("tcp")){
                					
                					String arr[] = matNetstat[j][3].split(":");
                					// 0.0.0.0:* -> [0.0.0.0][*]
                					if(arr[arr.length-2].equalsIgnoreCase("0.0.0.0") && arr[arr.length-1].equalsIgnoreCase("*")){
            							oidConn = "0.0.0.0.0"; 
        							}else{
        								oidConn = arr[arr.length-2]+"."+ arr[arr.length-1];
        							}
                					 
    								arr = matNetstat[j][4].split(":");
    								// 0.0.0.0:* -> [0.0.0.0][*]
                					if(arr[arr.length-2].equalsIgnoreCase("0.0.0.0") && arr[arr.length-1].equalsIgnoreCase("*")){
            							oidConn = ".0.0.0.0.0"; 
        							}else{
        								oidConn += "."+arr[arr.length-2]+"."+ arr[arr.length-1];
        							}
                					 								
                				}else if(matNetstat[j][0].equalsIgnoreCase("udp") || matNetstat[j][0].equalsIgnoreCase("udp6")){
                					continue;
                				}
                				
    	            			if(snmpOID.toString().equalsIgnoreCase(("1.3.6.1.2.1.6.13.1.1."+oidConn))){
    	            				
    	            				if(matNetstat[j][5].equalsIgnoreCase("closed")) {
    	            					stateConn=1;
    	            				}else if(matNetstat[j][5].equalsIgnoreCase("listen")) {
    	            					stateConn=2;
    	            				}else if(matNetstat[j][5].equalsIgnoreCase("syn_sent")) {
    	            					stateConn=3;
    	            				}else if(matNetstat[j][5].equalsIgnoreCase("syn_received")) {
    	            					stateConn=4;
    	            				}else if(matNetstat[j][5].equalsIgnoreCase("established")) {
    	            					stateConn=5;
    	            				}else if(matNetstat[j][5].equalsIgnoreCase("fin_wait_1")) {
    	            					stateConn=6;
    	            				}else if(matNetstat[j][5].equalsIgnoreCase("fin_wait_2")) {
    	            					stateConn=7;
    	            				}else if(matNetstat[j][5].equalsIgnoreCase("close_wait")) {
    	            					stateConn=8;
    	            				}else if(matNetstat[j][5].equalsIgnoreCase("last_ack")) {
    	            					stateConn=9;
    	            				}else if(matNetstat[j][5].equalsIgnoreCase("closing")) {
    	            					stateConn=10;
    	            				}else if(matNetstat[j][5].equalsIgnoreCase("time_wait")) {
    	            					stateConn=11;
    	            				}else if(matNetstat[j][5].equalsIgnoreCase("delete_tcb")) {
    	            					stateConn=12;
    	            				}
    										
    	            				try {
    	            					SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPInteger(stateConn));
    	                                responseList.addSNMPObject(newPair);
    	            				}catch (Exception e){
		        		            	errorIndex = i+1;
		        		            	errorStatus = SNMPRequestException.FAILED;
		        		            }
    	            			}
    						}
                			
                        }catch (Exception e){
                        	errorIndex = i+1;
                        	errorStatus = SNMPRequestException.FAILED;
                        }
                	
                    // tcpConnLocalAddress | IPAddress | Read-Only | Mandatory
                    }else if (snmpOID.toString().startsWith("1.3.6.1.2.1.6.13.1.2"))
                    {
                		String cmd="netstat\n", line, oidConn = "";
                		String ipAddress1="", ipAddress2="", portLocal="", portForeign="";
                		Process p=null;
                    	ArrayList<String> lines = new ArrayList<String>();
                		BufferedReader in2=null;
                		
                    	try
                        {
                    		p = Runtime.getRuntime().exec(cmd);
                			in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
                			while ((line =in2.readLine()) != null) {
                				lines.add(line);
                			}
                			/* Comienzo en 1 para saltar esta linea:
                			 * Proto Recv-Q Send-Q Local_Address Foreign_Address State
                			 */
       		
    						String matNetstat[][] = new String[lines.size()-1][6];

                			for (int j = 1; j < lines.size(); j++) {
    							String arrTmp[] = lines.get(j).split(" ");
    							for (int k = 0, x = 0; k < arrTmp.length; k++) {
    								if(!arrTmp[k].equalsIgnoreCase("")){
    									matNetstat[j-1][x] = arrTmp[k];
    									x++;
    								}
    							}
    						}
                			
                			for (int j = 0; j < matNetstat.length; j++) {
    							
                				if(matNetstat[j][0].equalsIgnoreCase("tcp6")){
                					
                					String arr[] = matNetstat[j][3].split(":");
                					// :::* -> [][][][*]
                					if(arr[arr.length-2].equalsIgnoreCase("") && arr[arr.length-1].equalsIgnoreCase("*")){
                						ipAddress1="0.0.0.0"; portLocal="0";
                					}else{ // ::1:25 -> [][][1][25]
                						if(arr[arr.length-2].equalsIgnoreCase("1") && !arr[arr.length-1].equalsIgnoreCase("")){
                							ipAddress1="127.0.0.1"; portLocal=arr[arr.length-1];
                						}else{ // 0.0.0.0:* -> [0.0.0.0][*]
                							if(arr[arr.length-2].equalsIgnoreCase("0.0.0.0") && arr[arr.length-1].equalsIgnoreCase("*")){
                								ipAddress1="0.0.0.0"; portLocal="0";
                							}else{
                								ipAddress1=arr[arr.length-2]; portLocal=arr[arr.length-1];
                							}
                						}
                					}
                					 
    								arr = matNetstat[j][4].split(":");
    								
    								if(arr[arr.length-2].equalsIgnoreCase("") && arr[arr.length-1].equalsIgnoreCase("*")){
                						ipAddress2="0.0.0.0"; portForeign="0";
                					}else{ // ::1:25 -> [][][1][25]
                						if(arr[arr.length-2].equalsIgnoreCase("1") && !arr[arr.length-1].equalsIgnoreCase("")){
                							ipAddress2="127.0.0.1"; portForeign=arr[arr.length-1];
                						}else{ // 0.0.0.0:* -> [0.0.0.0][*]
                							if(arr[arr.length-2].equalsIgnoreCase("0.0.0.0") && arr[arr.length-1].equalsIgnoreCase("*")){
                								ipAddress2="0.0.0.0"; portForeign="0";
                							}else{
                								ipAddress2=arr[arr.length-2]; portForeign=arr[arr.length-1];
                							}
                						}
                					}			
    								
                				}else if(matNetstat[j][0].equalsIgnoreCase("tcp")){
                					
                					String arr[] = matNetstat[j][3].split(":");
                					// 0.0.0.0:* -> [0.0.0.0][*]
                					if(arr[arr.length-2].equalsIgnoreCase("0.0.0.0") && arr[arr.length-1].equalsIgnoreCase("*")){
                						ipAddress1="0.0.0.0"; portLocal="0"; 
        							}else{
        								ipAddress1=arr[arr.length-2]; portLocal=arr[arr.length-1];
        							}
                					 
    								arr = matNetstat[j][4].split(":");
    								// 0.0.0.0:* -> [0.0.0.0][*]
                					if(arr[arr.length-2].equalsIgnoreCase("0.0.0.0") && arr[arr.length-1].equalsIgnoreCase("*")){
                						ipAddress2="0.0.0.0"; portForeign="0";
        							}else{
        								ipAddress2=arr[arr.length-2]; portForeign=arr[arr.length-1];
        							}
                					 								
                				}else if(matNetstat[j][0].equalsIgnoreCase("udp") || matNetstat[j][0].equalsIgnoreCase("udp6")){
                					continue;
                				}
                				
                				oidConn = ipAddress1 + "." + portLocal + "." + ipAddress2 + "." + portForeign; 
                				
    	            			if(snmpOID.toString().equalsIgnoreCase(("1.3.6.1.2.1.6.13.1.2."+oidConn))){            					
    	            				try {
    	            					SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPIPAddress(ipAddress1));
    	                                responseList.addSNMPObject(newPair);
    	            				}catch (Exception e){
		        		            	errorIndex = i+1;
		        		            	errorStatus = SNMPRequestException.FAILED;
		        		            }
    	            			}
    						}
                			
                        }catch (Exception e){
                        	errorIndex = i+1;
                        	errorStatus = SNMPRequestException.FAILED;
                        }
                	
                    // tcpConnLocalPort | Integer | Read-Only | Mandatory
                    }else if (snmpOID.toString().startsWith("1.3.6.1.2.1.6.13.1.3"))
                    {
                		String cmd="netstat\n", line, oidConn = "";
                		String ipAddress1="", ipAddress2="", portLocal="", portForeign="";
                		Process p=null;
                    	ArrayList<String> lines = new ArrayList<String>();
                		BufferedReader in2=null;
                		
                    	try
                        {
                    		p = Runtime.getRuntime().exec(cmd);
                			in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
                			while ((line =in2.readLine()) != null) {
                				lines.add(line);
                			}
                			/* Comienzo en 1 para saltar esta linea:
                			 * Proto Recv-Q Send-Q Local_Address Foreign_Address State
                			 */
      		
    						String matNetstat[][] = new String[lines.size()-1][6];

                			for (int j = 1; j < lines.size(); j++) {
    							String arrTmp[] = lines.get(j).split(" ");
    							for (int k = 0, x = 0; k < arrTmp.length; k++) {
    								if(!arrTmp[k].equalsIgnoreCase("")){
    									matNetstat[j-1][x] = arrTmp[k];
    									x++;
    								}
    							}
    						}
                			
                			for (int j = 0; j < matNetstat.length; j++) {
    							
                				if(matNetstat[j][0].equalsIgnoreCase("tcp6")){
                					
                					String arr[] = matNetstat[j][3].split(":");
                					// :::* -> [][][][*]
                					if(arr[arr.length-2].equalsIgnoreCase("") && arr[arr.length-1].equalsIgnoreCase("*")){
                						ipAddress1="0.0.0.0"; portLocal="0";
                					}else{ // ::1:25 -> [][][1][25]
                						if(arr[arr.length-2].equalsIgnoreCase("1") && !arr[arr.length-1].equalsIgnoreCase("")){
                							ipAddress1="127.0.0.1"; portLocal=arr[arr.length-1];
                						}else{ // 0.0.0.0:* -> [0.0.0.0][*]
                							if(arr[arr.length-2].equalsIgnoreCase("0.0.0.0") && arr[arr.length-1].equalsIgnoreCase("*")){
                								ipAddress1="0.0.0.0"; portLocal="0";
                							}else{
                								ipAddress1=arr[arr.length-2]; portLocal=arr[arr.length-1];
                							}
                						}
                					}
                					 
    								arr = matNetstat[j][4].split(":");
    								
    								if(arr[arr.length-2].equalsIgnoreCase("") && arr[arr.length-1].equalsIgnoreCase("*")){
                						ipAddress2="0.0.0.0"; portForeign="0";
                					}else{ // ::1:25 -> [][][1][25]
                						if(arr[arr.length-2].equalsIgnoreCase("1") && !arr[arr.length-1].equalsIgnoreCase("")){
                							ipAddress2="127.0.0.1"; portForeign=arr[arr.length-1];
                						}else{ // 0.0.0.0:* -> [0.0.0.0][*]
                							if(arr[arr.length-2].equalsIgnoreCase("0.0.0.0") && arr[arr.length-1].equalsIgnoreCase("*")){
                								ipAddress2="0.0.0.0"; portForeign="0";
                							}else{
                								ipAddress2=arr[arr.length-2]; portForeign=arr[arr.length-1];
                							}
                						}
                					}			
    								
                				}else if(matNetstat[j][0].equalsIgnoreCase("tcp")){
                					
                					String arr[] = matNetstat[j][3].split(":");
                					// 0.0.0.0:* -> [0.0.0.0][*]
                					if(arr[arr.length-2].equalsIgnoreCase("0.0.0.0") && arr[arr.length-1].equalsIgnoreCase("*")){
                						ipAddress1="0.0.0.0"; portLocal="0"; 
        							}else{
        								ipAddress1=arr[arr.length-2]; portLocal=arr[arr.length-1];
        							}
                					 
    								arr = matNetstat[j][4].split(":");
    								// 0.0.0.0:* -> [0.0.0.0][*]
                					if(arr[arr.length-2].equalsIgnoreCase("0.0.0.0") && arr[arr.length-1].equalsIgnoreCase("*")){
                						ipAddress2="0.0.0.0"; portForeign="0";
        							}else{
        								ipAddress2=arr[arr.length-2]; portForeign=arr[arr.length-1];
        							}
                					 								
                				}else if(matNetstat[j][0].equalsIgnoreCase("udp") || matNetstat[j][0].equalsIgnoreCase("udp6")){
                					continue;
                				}
                				
                				oidConn = ipAddress1 + "." + portLocal + "." + ipAddress2 + "." + portForeign; 
                				
    	            			if(snmpOID.toString().equalsIgnoreCase(("1.3.6.1.2.1.6.13.1.3."+oidConn))){            					
    	            				try {
    	            					SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPInteger(Integer.parseInt(portLocal)));
    	                                responseList.addSNMPObject(newPair);
    	            				}catch (Exception e){
		        		            	errorIndex = i+1;
		        		            	errorStatus = SNMPRequestException.FAILED;
		        		            }
    	            			}
    						}
                			
                        }catch (Exception e){
                        	errorIndex = i+1;
                        	errorStatus = SNMPRequestException.FAILED;
                        }
                		
                    // tcpConnRemAddress | IPAddress | Read-Only | Mandatory	
                    }else if (snmpOID.toString().startsWith("1.3.6.1.2.1.6.13.1.4"))
                    {
                		String cmd="netstat\n", line, oidConn = "";
                		String ipAddress1="", ipAddress2="", portLocal="", portForeign="";
                		Process p=null;
                    	ArrayList<String> lines = new ArrayList<String>();
                		BufferedReader in2=null;
                		
                    	try
                        {
                    		p = Runtime.getRuntime().exec(cmd);
                			in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
                			while ((line =in2.readLine()) != null) {
                				lines.add(line);
                			}
                			/* Comienzo en 1 para saltar esta linea:
                			 * Proto Recv-Q Send-Q Local_Address Foreign_Address State
                			 */
        		
    						String matNetstat[][] = new String[lines.size()-1][6];

                			for (int j = 1; j < lines.size(); j++) {
    							String arrTmp[] = lines.get(j).split(" ");
    							for (int k = 0, x = 0; k < arrTmp.length; k++) {
    								if(!arrTmp[k].equalsIgnoreCase("")){
    									matNetstat[j-1][x] = arrTmp[k];
    									x++;
    								}
    							}
    						}
                			
                			for (int j = 0; j < matNetstat.length; j++) {
    							
                				if(matNetstat[j][0].equalsIgnoreCase("tcp6")){
                					
                					String arr[] = matNetstat[j][3].split(":");
                					// :::* -> [][][][*]
                					if(arr[arr.length-2].equalsIgnoreCase("") && arr[arr.length-1].equalsIgnoreCase("*")){
                						ipAddress1="0.0.0.0"; portLocal="0";
                					}else{ // ::1:25 -> [][][1][25]
                						if(arr[arr.length-2].equalsIgnoreCase("1") && !arr[arr.length-1].equalsIgnoreCase("")){
                							ipAddress1="127.0.0.1"; portLocal=arr[arr.length-1];
                						}else{ // 0.0.0.0:* -> [0.0.0.0][*]
                							if(arr[arr.length-2].equalsIgnoreCase("0.0.0.0") && arr[arr.length-1].equalsIgnoreCase("*")){
                								ipAddress1="0.0.0.0"; portLocal="0";
                							}else{
                								ipAddress1=arr[arr.length-2]; portLocal=arr[arr.length-1];
                							}
                						}
                					}
                					 
    								arr = matNetstat[j][4].split(":");
    								
    								if(arr[arr.length-2].equalsIgnoreCase("") && arr[arr.length-1].equalsIgnoreCase("*")){
                						ipAddress2="0.0.0.0"; portForeign="0";
                					}else{ // ::1:25 -> [][][1][25]
                						if(arr[arr.length-2].equalsIgnoreCase("1") && !arr[arr.length-1].equalsIgnoreCase("")){
                							ipAddress2="127.0.0.1"; portForeign=arr[arr.length-1];
                						}else{ // 0.0.0.0:* -> [0.0.0.0][*]
                							if(arr[arr.length-2].equalsIgnoreCase("0.0.0.0") && arr[arr.length-1].equalsIgnoreCase("*")){
                								ipAddress2="0.0.0.0"; portForeign="0";
                							}else{
                								ipAddress2=arr[arr.length-2]; portForeign=arr[arr.length-1];
                							}
                						}
                					}			
    								
                				}else if(matNetstat[j][0].equalsIgnoreCase("tcp")){
                					
                					String arr[] = matNetstat[j][3].split(":");
                					// 0.0.0.0:* -> [0.0.0.0][*]
                					if(arr[arr.length-2].equalsIgnoreCase("0.0.0.0") && arr[arr.length-1].equalsIgnoreCase("*")){
                						ipAddress1="0.0.0.0"; portLocal="0"; 
        							}else{
        								ipAddress1=arr[arr.length-2]; portLocal=arr[arr.length-1];
        							}
                					 
    								arr = matNetstat[j][4].split(":");
    								// 0.0.0.0:* -> [0.0.0.0][*]
                					if(arr[arr.length-2].equalsIgnoreCase("0.0.0.0") && arr[arr.length-1].equalsIgnoreCase("*")){
                						ipAddress2="0.0.0.0"; portForeign="0";
        							}else{
        								ipAddress2=arr[arr.length-2]; portForeign=arr[arr.length-1];
        							}
                					 								
                				}else if(matNetstat[j][0].equalsIgnoreCase("udp") || matNetstat[j][0].equalsIgnoreCase("udp6")){
                					continue;
                				}
                				
                				oidConn = ipAddress1 + "." + portLocal + "." + ipAddress2 + "." + portForeign; 
                				
    	            			if(snmpOID.toString().equalsIgnoreCase(("1.3.6.1.2.1.6.13.1.4."+oidConn))){            					
    	            				try {
    	            					SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPIPAddress(ipAddress2));
    	                                responseList.addSNMPObject(newPair);
    	            				}catch (Exception e){
		        		            	errorIndex = i+1;
		        		            	errorStatus = SNMPRequestException.FAILED;
		        		            }
    	            			}
    						}
                			
                        }catch (Exception e){
                        	errorIndex = i+1;
                        	errorStatus = SNMPRequestException.FAILED;
                        }
                		
                    // tcpConnRemPort | Integer | Read-Only | Mandatory	
                    }else if (snmpOID.toString().startsWith("1.3.6.1.2.1.6.13.1.5"))
                    {
                		String cmd="netstat\n", line, oidConn = "";
                		String ipAddress1="", ipAddress2="", portLocal="", portForeign="";
                		Process p=null;
                    	ArrayList<String> lines = new ArrayList<String>();
                		BufferedReader in2=null;
                		
                    	try
                        {
                    		p = Runtime.getRuntime().exec(cmd);
                			in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
                			while ((line =in2.readLine()) != null) {
                				lines.add(line);
                			}
                			/* Comienzo en 1 para saltar esta linea:
                			 * Proto Recv-Q Send-Q Local_Address Foreign_Address State
                			 */
        		
    						String matNetstat[][] = new String[lines.size()-1][6];

                			for (int j = 1; j < lines.size(); j++) {
    							String arrTmp[] = lines.get(j).split(" ");
    							for (int k = 0, x = 0; k < arrTmp.length; k++) {
    								if(!arrTmp[k].equalsIgnoreCase("")){
    									matNetstat[j-1][x] = arrTmp[k];
    									x++;
    								}
    							}
    						}
                			
                			for (int j = 0; j < matNetstat.length; j++) {
    							
                				if(matNetstat[j][0].equalsIgnoreCase("tcp6")){
                					
                					String arr[] = matNetstat[j][3].split(":");
                					// :::* -> [][][][*]
                					if(arr[arr.length-2].equalsIgnoreCase("") && arr[arr.length-1].equalsIgnoreCase("*")){
                						ipAddress1="0.0.0.0"; portLocal="0";
                					}else{ // ::1:25 -> [][][1][25]
                						if(arr[arr.length-2].equalsIgnoreCase("1") && !arr[arr.length-1].equalsIgnoreCase("")){
                							ipAddress1="127.0.0.1"; portLocal=arr[arr.length-1];
                						}else{ // 0.0.0.0:* -> [0.0.0.0][*]
                							if(arr[arr.length-2].equalsIgnoreCase("0.0.0.0") && arr[arr.length-1].equalsIgnoreCase("*")){
                								ipAddress1="0.0.0.0"; portLocal="0";
                							}else{
                								ipAddress1=arr[arr.length-2]; portLocal=arr[arr.length-1];
                							}
                						}
                					}
                					 
    								arr = matNetstat[j][4].split(":");
    								
    								if(arr[arr.length-2].equalsIgnoreCase("") && arr[arr.length-1].equalsIgnoreCase("*")){
                						ipAddress2="0.0.0.0"; portForeign="0";
                					}else{ // ::1:25 -> [][][1][25]
                						if(arr[arr.length-2].equalsIgnoreCase("1") && !arr[arr.length-1].equalsIgnoreCase("")){
                							ipAddress2="127.0.0.1"; portForeign=arr[arr.length-1];
                						}else{ // 0.0.0.0:* -> [0.0.0.0][*]
                							if(arr[arr.length-2].equalsIgnoreCase("0.0.0.0") && arr[arr.length-1].equalsIgnoreCase("*")){
                								ipAddress2="0.0.0.0"; portForeign="0";
                							}else{
                								ipAddress2=arr[arr.length-2]; portForeign=arr[arr.length-1];
                							}
                						}
                					}			
    								
                				}else if(matNetstat[j][0].equalsIgnoreCase("tcp")){
                					
                					String arr[] = matNetstat[j][3].split(":");
                					// 0.0.0.0:* -> [0.0.0.0][*]
                					if(arr[arr.length-2].equalsIgnoreCase("0.0.0.0") && arr[arr.length-1].equalsIgnoreCase("*")){
                						ipAddress1="0.0.0.0"; portLocal="0"; 
        							}else{
        								ipAddress1=arr[arr.length-2]; portLocal=arr[arr.length-1];
        							}
                					 
    								arr = matNetstat[j][4].split(":");
    								// 0.0.0.0:* -> [0.0.0.0][*]
                					if(arr[arr.length-2].equalsIgnoreCase("0.0.0.0") && arr[arr.length-1].equalsIgnoreCase("*")){
                						ipAddress2="0.0.0.0"; portForeign="0";
        							}else{
        								ipAddress2=arr[arr.length-2]; portForeign=arr[arr.length-1];
        							}
                					 								
                				}else if(matNetstat[j][0].equalsIgnoreCase("udp") || matNetstat[j][0].equalsIgnoreCase("udp6")){
                					continue;
                				}
                				
                				oidConn = ipAddress1 + "." + portLocal + "." + ipAddress2 + "." + portForeign; 
                				
    	            			if(snmpOID.toString().equalsIgnoreCase(("1.3.6.1.2.1.6.13.1.5."+oidConn))){            					
    	            				try {
    	            					SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPInteger(Integer.parseInt(portForeign)));
    	                                responseList.addSNMPObject(newPair);
    	            				}catch (Exception e){
		        		            	errorIndex = i+1;
		        		            	errorStatus = SNMPRequestException.FAILED;
		        		            }
    	            			}
    						}
                			
                        }catch (Exception e){
                        	errorIndex = i+1;
                        	errorStatus = SNMPRequestException.FAILED;
                        }
                		
                    }
    	
                	continue;
                }
                
                
                // tcpInErrs | Counter | Read-Only | Mandatory 
                if (snmpOID.toString().equals("1.3.6.1.2.1.6.14.0") || snmpOID.toString().equals("1.3.6.1.2.1.6.14"))
                {
                	String line = "";
                	ArrayList<String> lines = new ArrayList<String>();
            		try
                    {
                		File archivo = new File("/proc/net/snmp");
            			FileReader fileReader = new FileReader (archivo);
            			BufferedReader buffReader = new BufferedReader(fileReader);
                		while ((line =buffReader.readLine()) != null) {
            				lines.add(line);
            			}
                		if( null != fileReader)
    						fileReader.close();

            			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
            			String value = "InErrs";
            			arrName= new ArrayList<String>(Arrays.asList(lines.get(6).split(" "))); // IP Names
            			arrValue= new ArrayList<String>(Arrays.asList(lines.get(7).split(" "))); // IP Values
            			int pos = 0;
            			for (String stra : arrName) {
            				if(stra.equalsIgnoreCase(value)) break;
            				else pos++;
            			}
            			
            			Long recv = Long.parseLong(arrValue.get(pos));
            			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(recv));
                        responseList.addSNMPObject(newPair);
                        
                    }catch (Exception e){
                    	errorIndex = i+1;
                    	errorStatus = SNMPRequestException.FAILED;
                    }
                    
                    continue;
                } // tcpInErrs
                
                
                // tcpOutRsts | Counter | Read-Only | Mandatory 
                if (snmpOID.toString().equals("1.3.6.1.2.1.6.15.0") || snmpOID.toString().equals("1.3.6.1.2.1.6.15"))
                {
                	String line = "";
                	ArrayList<String> lines = new ArrayList<String>();
            		try
                    {
                		File archivo = new File("/proc/net/snmp");
            			FileReader fileReader = new FileReader (archivo);
            			BufferedReader buffReader = new BufferedReader(fileReader);
                		while ((line =buffReader.readLine()) != null) {
            				lines.add(line);
            			}
                		if( null != fileReader)
    						fileReader.close();

            			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
            			String value = "OutRsts";
            			arrName= new ArrayList<String>(Arrays.asList(lines.get(6).split(" "))); // IP Names
            			arrValue= new ArrayList<String>(Arrays.asList(lines.get(7).split(" "))); // IP Values
            			int pos = 0;
            			for (String stra : arrName) {
            				if(stra.equalsIgnoreCase(value)) break;
            				else pos++;
            			}
            			
            			Long recv = Long.parseLong(arrValue.get(pos));
            			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(recv));
                        responseList.addSNMPObject(newPair);
                        
                    }catch (Exception e){
                    	errorIndex = i+1;
                    	errorStatus = SNMPRequestException.FAILED;
                    }
                
                    continue;
                } // tcpOutRsts
                
            }	// MIB-II |	the TCP Group
            
			             /***************************
			             * 							*
			             * MIB-II |	the UDP Group   *
			             * 							*
			             ***************************/
			
			if(snmpOID.toString().startsWith("1.3.6.1.2.1.7"))
			{
	            // udpInDatagrams | Counter | Read-Only | Mandatory 
	            if (snmpOID.toString().equals("1.3.6.1.2.1.7.1.0") || snmpOID.toString().equals("1.3.6.1.2.1.7.1"))
	            {
	            	String line = "";
	            	ArrayList<String> lines = new ArrayList<String>();
		    		try
		            {
		        		File archivo = new File("/proc/net/snmp");
		    			FileReader fileReader = new FileReader (archivo);
		    			BufferedReader buffReader = new BufferedReader(fileReader);
		        		while ((line =buffReader.readLine()) != null) {
		    				lines.add(line);
		    			}
		        		if( null != fileReader)
							fileReader.close();
		
	        			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
	        			String value = "InDatagrams";
	        			arrName= new ArrayList<String>(Arrays.asList(lines.get(8).split(" "))); // IP Names
	        			arrValue= new ArrayList<String>(Arrays.asList(lines.get(9).split(" "))); // IP Values
	        			int pos = 0;
	        			for (String stra : arrName) {
	        				if(stra.equalsIgnoreCase(value)) break;
	        				else pos++;
	        			}
	        			
	        			Long recv = Long.parseLong(arrValue.get(pos));
	        			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(recv));
	                    responseList.addSNMPObject(newPair);
	                    
		            }catch (Exception e){
		            	errorIndex = i+1;
		            	errorStatus = SNMPRequestException.FAILED;
		            }
	        
	                continue;
	            } // udpInDatagrams
	            
	            
	            // udpNoPorts | Counter | Read-Only | Mandatory 
	            if (snmpOID.toString().equals("1.3.6.1.2.1.7.2.0") || snmpOID.toString().equals("1.3.6.1.2.1.7.2"))
	            {
	            	
	            	String line = "";
	            	ArrayList<String> lines = new ArrayList<String>();
		    		try
		            {
		        		File archivo = new File("/proc/net/snmp");
		    			FileReader fileReader = new FileReader (archivo);
		    			BufferedReader buffReader = new BufferedReader(fileReader);
		        		while ((line =buffReader.readLine()) != null) {
		    				lines.add(line);
		    			}
		        		if( null != fileReader)
							fileReader.close();

	        			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
	        			String value = "NoPorts";
	        			arrName= new ArrayList<String>(Arrays.asList(lines.get(8).split(" "))); // IP Names
	        			arrValue= new ArrayList<String>(Arrays.asList(lines.get(9).split(" "))); // IP Values
	        			int pos = 0;
	        			for (String stra : arrName) {
	        				if(stra.equalsIgnoreCase(value)) break;
	        				else pos++;
	        			}
	        			
	        			Long recv = Long.parseLong(arrValue.get(pos));
	        			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(recv));
	                    responseList.addSNMPObject(newPair);
	                    
		            }catch (Exception e){
		            	errorIndex = i+1;
		            	errorStatus = SNMPRequestException.FAILED;
		            }
	                
	                continue;
	            } // udpNoPorts
	            
	            // udpInErrors | Counter | Read-Only | Mandatory 
	            if (snmpOID.toString().equals("1.3.6.1.2.1.7.3.0") || snmpOID.toString().equals("1.3.6.1.2.1.7.3"))
	            {
	            	String line = "";
	            	ArrayList<String> lines = new ArrayList<String>();
		    		try
		            {
		        		File archivo = new File("/proc/net/snmp");
		    			FileReader fileReader = new FileReader (archivo);
		    			BufferedReader buffReader = new BufferedReader(fileReader);
		        		while ((line =buffReader.readLine()) != null) {
		    				lines.add(line);
		    			}
		        		if( null != fileReader)
							fileReader.close();

	        			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
	        			String value = "InErrors";
	        			arrName= new ArrayList<String>(Arrays.asList(lines.get(8).split(" "))); // IP Names
	        			arrValue= new ArrayList<String>(Arrays.asList(lines.get(9).split(" "))); // IP Values
	        			int pos = 0;
	        			for (String stra : arrName) {
	        				if(stra.equalsIgnoreCase(value)) break;
	        				else pos++;
	        			}
	        			
	        			Long recv = Long.parseLong(arrValue.get(pos));
	        			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(recv));
	                    responseList.addSNMPObject(newPair);
		            }catch (Exception e){
		            	errorIndex = i+1;
		            	errorStatus = SNMPRequestException.FAILED;
		            }
	                
	                continue;
	            } // udpInErrors
	            
	            
	            // udpOutDatagrams | Counter | Read-Only | Mandatory 
	            if (snmpOID.toString().equals("1.3.6.1.2.1.7.4.0")  || snmpOID.toString().equals("1.3.6.1.2.1.7.4"))
	            {
	            	String line = "";
	            	ArrayList<String> lines = new ArrayList<String>();
		    		try
		            {
		        		File archivo = new File("/proc/net/snmp");
		    			FileReader fileReader = new FileReader (archivo);
		    			BufferedReader buffReader = new BufferedReader(fileReader);
		        		while ((line =buffReader.readLine()) != null) {
		    				lines.add(line);
		    			}
		        		if( null != fileReader)
							fileReader.close();

	            			ArrayList<String> arrValue = new ArrayList<String>(), arrName = new ArrayList<String>();
	            			String value = "OutDatagrams";
	            			arrName= new ArrayList<String>(Arrays.asList(lines.get(8).split(" "))); // IP Names
	            			arrValue= new ArrayList<String>(Arrays.asList(lines.get(9).split(" "))); // IP Values
	            			int pos = 0;
	            			for (String stra : arrName) {
	            				if(stra.equalsIgnoreCase(value)) break;
	            				else pos++;
	            			}
	            			
	            			Long recv = Long.parseLong(arrValue.get(pos));
	            			SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPCounter32(recv));
	                        responseList.addSNMPObject(newPair);
	                }catch (Exception e){
		            	errorIndex = i+1;
		            	errorStatus = SNMPRequestException.FAILED;
		            }
	                
	                continue;
	            } // udpOutDatagrams
	            
	            
	             /*******************************
	             * 								*
	             *	the UDP Connection Table 	*
	             * udpConnTable 1.3.6.1.2.1.7.5 *
	             * 								*
	             *******************************/
	            // udpConnEntry
	            if (snmpOID.toString().startsWith("1.3.6.1.2.1.7.5.1."))
	            {
	            	// udpLocalAddress | IPAddress | Read-Only | mandatory
		            if (snmpOID.toString().startsWith("1.3.6.1.2.1.7.5.1.1."))
		            {
		        		String cmd="netstat\n", line, oidConn = "";
		        		String ipAddress1="", portLocal="";
		        		Process p=null;
		            	ArrayList<String> lines = new ArrayList<String>();
		        		BufferedReader in2=null;
		        		
		            	try
		                {
		            		p = Runtime.getRuntime().exec(cmd);
		        			in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
		        			while ((line =in2.readLine()) != null) {
		        				lines.add(line);
		        			}
		        			/* Comienzo en 1 para saltar esta linea:
		        			 * Proto Recv-Q Send-Q Local_Address Foreign_Address State
		        			 */
				
							String matNetstat[][] = new String[lines.size()-1][6];
		
		        			for (int j = 1; j < lines.size(); j++) {
								String arrTmp[] = lines.get(j).split(" ");
								for (int k = 0, x = 0; k < arrTmp.length; k++) {
									if(!arrTmp[k].equalsIgnoreCase("")){
										matNetstat[j-1][x] = arrTmp[k];
										x++;
									}
								}
							}
		        			
		        			for (int j = 0; j < matNetstat.length; j++) {
								
		        				if(matNetstat[j][0].equalsIgnoreCase("udp6")){
		        					
		        					String arr[] = matNetstat[j][3].split(":");
		        					// :::161 -> [][][][161]
		        					if(arr[arr.length-2].equalsIgnoreCase("") && !arr[arr.length-1].equalsIgnoreCase("")){
		        						ipAddress1="0.0.0.0"; portLocal=arr[arr.length-1];
		        					}else{ // :::25 -> [][][1][25]
		        						if(arr[arr.length-2].equalsIgnoreCase("1") && !arr[arr.length-1].equalsIgnoreCase("")){
		        							ipAddress1="127.0.0.1"; portLocal=arr[arr.length-1];
		        						}else{ // 0.0.0.0:* -> [0.0.0.0][*]
		        							if(arr[arr.length-2].equalsIgnoreCase("0.0.0.0") && arr[arr.length-1].equalsIgnoreCase("*")){
		        								ipAddress1="0.0.0.0"; portLocal="0";
		        							}else{ //192.168.0.110:161 -> [192.168.0.110][161]
		        								ipAddress1=arr[arr.length-2]; portLocal=arr[arr.length-1];
		        							}
		        						}
		        					}		
									
		        				}else if(matNetstat[j][0].equalsIgnoreCase("udp")){
		        					
		        					String arr[] = matNetstat[j][3].split(":");
		        					// 0.0.0.0:* -> [0.0.0.0][*]
		        					if(arr[arr.length-2].equalsIgnoreCase("0.0.0.0") && arr[arr.length-1].equalsIgnoreCase("*")){
		        						ipAddress1="0.0.0.0"; portLocal="0"; 
									}else{
										ipAddress1=arr[arr.length-2]; portLocal=arr[arr.length-1];
									}
		        					 								
		        				}else if(matNetstat[j][0].equalsIgnoreCase("tcp") || matNetstat[j][0].equalsIgnoreCase("tcp6")){
		        					continue;
		        				}
		        				
		        				oidConn = ipAddress1 + "." + portLocal; 
		        				
		            			if(snmpOID.toString().equalsIgnoreCase(("1.3.6.1.2.1.7.5.1.1."+oidConn))){            					
		            				try {
		            					SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPIPAddress(ipAddress1));
		                                responseList.addSNMPObject(newPair);
		            				}catch (Exception e){
		        		            	errorIndex = i+1;
		        		            	errorStatus = SNMPRequestException.FAILED;
		        		            }
		            			}
							}
		        			
		                }catch (Exception e){
			            	errorIndex = i+1;
			            	errorStatus = SNMPRequestException.FAILED;
			            }
		            	
		            // udpLocalPort | Integer | Read-Only | Mandatory
		            }else if (snmpOID.toString().startsWith("1.3.6.1.2.1.7.5.1.2."))
		            {
		        		String cmd="netstat\n", line, oidConn = "";
		        		String ipAddress1="", portLocal="";
		        		Process p=null;
		            	ArrayList<String> lines = new ArrayList<String>();
		        		BufferedReader in2=null;
		        		
		            	try
		                {
		            		p = Runtime.getRuntime().exec(cmd);
		        			in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
		        			while ((line =in2.readLine()) != null) {
		        				lines.add(line);
		        			}
		        			/* Comienzo en 1 para saltar esta linea:
		        			 * Proto Recv-Q Send-Q Local_Address Foreign_Address State
		        			 */
				
							String matNetstat[][] = new String[lines.size()-1][6];
		
		        			for (int j = 1; j < lines.size(); j++) {
								String arrTmp[] = lines.get(j).split(" ");
								for (int k = 0, x = 0; k < arrTmp.length; k++) {
									if(!arrTmp[k].equalsIgnoreCase("")){
										matNetstat[j-1][x] = arrTmp[k];
										x++;
									}
								}
							}
		        			
		        			for (int j = 0; j < matNetstat.length; j++) {
								
		        				if(matNetstat[j][0].equalsIgnoreCase("udp6")){
		        					
		        					String arr[] = matNetstat[j][3].split(":");
		        					// :::161 -> [][][][161]
		        					if(arr[arr.length-2].equalsIgnoreCase("") && !arr[arr.length-1].equalsIgnoreCase("")){
		        						ipAddress1="0.0.0.0"; portLocal=arr[arr.length-1];
		        					}else{ // ::1:25 -> [][][1][25]
		        						if(arr[arr.length-2].equalsIgnoreCase("1") && !arr[arr.length-1].equalsIgnoreCase("")){
		        							ipAddress1="127.0.0.1"; portLocal=arr[arr.length-1];
		        						}else{ // 0.0.0.0:* -> [0.0.0.0][*]
		        							if(arr[arr.length-2].equalsIgnoreCase("0.0.0.0") && arr[arr.length-1].equalsIgnoreCase("*")){
		        								ipAddress1="0.0.0.0"; portLocal="0";
		        							}else{ //192.168.0.110:161 -> [192.168.0.110][161]
		        								ipAddress1=arr[arr.length-2]; portLocal=arr[arr.length-1];
		        							}
		        						}
		        					}		
									
		        				}else if(matNetstat[j][0].equalsIgnoreCase("udp")){
		        					
		        					String arr[] = matNetstat[j][3].split(":");
		        					// 0.0.0.0:* -> [0.0.0.0][*]
		        					if(arr[arr.length-2].equalsIgnoreCase("0.0.0.0") && arr[arr.length-1].equalsIgnoreCase("*")){
		        						ipAddress1="0.0.0.0"; portLocal="0"; 
									}else{
										ipAddress1=arr[arr.length-2]; portLocal=arr[arr.length-1];
									}
		        					 								
		        				}else if(matNetstat[j][0].equalsIgnoreCase("tcp") || matNetstat[j][0].equalsIgnoreCase("tcp6")){
		        					continue;
		        				}
		        				
		        				oidConn = ipAddress1 + "." + portLocal;  
		        				
		            			if(snmpOID.toString().equalsIgnoreCase(("1.3.6.1.2.1.7.5.1.2."+oidConn))){            					
		            				try {
		            					SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPInteger(Integer.parseInt(portLocal)));
		                                responseList.addSNMPObject(newPair);
		            				}catch (Exception e){
		        		            	errorIndex = i+1;
		        		            	errorStatus = SNMPRequestException.FAILED;
		        		            }	            				
		            			}
							}
		        			
		                }catch (Exception e){
			            	errorIndex = i+1;
			            	errorStatus = SNMPRequestException.FAILED;
			            }
		        	
		            }
		            continue;
	            }// udpConnEntry
	            
				
			}// MIB-II | the UDP Group
			            
								 /****************************************
					             * 										 *
					             * HOST-RESOURCES | hrSWInstalledTable   *
					             * 										 *
					             ****************************************/
			
			if(snmpOID.toString().startsWith("1.3.6.1.2.1.25.6.3"))
			{
				
	            // hrSWInstalledIndex Integer32 | Read-Only | Current | 1.3.6.1.2.1.25.6.3.1.1.X -> X es el numero de SW
	            if (snmpOID.toString().startsWith("1.3.6.1.2.1.25.6.3.1.1."))
	            {
	            	PackageManager pm = contextActivity.getPackageManager();
	            	ArrayList<ApplicationInfo> l = (ArrayList<ApplicationInfo>) pm.getInstalledApplications(ApplicationInfo.FLAG_INSTALLED);
	            	int nSW = 0, nTSW = l.size();
	        	    /*for (Iterator<ApplicationInfo> iterator = l.iterator(); iterator.hasNext();) {
	        	    	ApplicationInfo pInfo = (ApplicationInfo) iterator.next();
	        			
	        		}*/
					
					// Pregunto cual SW que esta solicitando
					try {
						nSW =  Integer.parseInt(snmpOID.toString().substring(23, snmpOID.toString().length()));
					}catch (Exception e){
		            	errorIndex = i+1;
		            	errorStatus = SNMPRequestException.FAILED;
		            }
	            		
	            	// si el numero de SW solicitado esta, busco el valor y lo envio
					if( 1 <= nSW && nSW <= nTSW ){
	                    try
	                    {
	                        SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPInteger(nSW));
	                        responseList.addSNMPObject(newPair);
	                    }catch (Exception e){
			            	errorIndex = i+1;
			            	errorStatus = SNMPRequestException.FAILED;
			            }
					}else{
						errorIndex = i+1;
	                    errorStatus = SNMPRequestException.VALUE_NOT_AVAILABLE;
	                    throw new SNMPSetException("Value out of range of the number Software Installed", errorIndex, errorStatus);						
					}
	                	   
	                continue;
	            } // hrSWInstalledIndex
	            
	            // hrSWInstalledName InternationalDisplayString | Read-Only | Current | 1.3.6.1.2.1.25.6.3.1.2.X -> X es el numero de SW
	            if (snmpOID.toString().startsWith("1.3.6.1.2.1.25.6.3.1.2."))
	            {
	        		PackageManager pm = contextActivity.getPackageManager();
	            	ArrayList<ApplicationInfo> l = (ArrayList<ApplicationInfo>) pm.getInstalledApplications(ApplicationInfo.FLAG_INSTALLED);
	            	int nSW = 0, nTSW = l.size();
	        	    /*for (Iterator<ApplicationInfo> iterator = l.iterator(); iterator.hasNext();) {
	        	    	ApplicationInfo pInfo = (ApplicationInfo) iterator.next();
	        			
	        		}*/
					
					// Pregunto cual SW que esta solicitando
					try {
						nSW =  Integer.parseInt(snmpOID.toString().substring(23, snmpOID.toString().length()));
					}catch (Exception e){
		            	errorIndex = i+1;
		            	errorStatus = SNMPRequestException.FAILED;
		            }
	            		
	            	// si el numero de SW solicitado esta, busco el valor y lo envio
					if( 1 <= nSW && nSW <= nTSW ){
	                    try
	                    {
	                    	String name = "unknown";
	                    	ApplicationInfo ai = l.get(nSW-1);
	                    	
	                    	if(ai != null){
	                    		PackageInfo pi = pm.getPackageInfo(ai.packageName, PackageManager.GET_ACTIVITIES);
		                		name = new String((String) pm.getApplicationLabel(ai));
	                    		if(sendWithoutTittle){
	                    			name = name.replace("á", "a"); name = name.replace("Á", "A");
	                    			name = name.replace("é", "e"); name = name.replace("É", "E");
	                    			name = name.replace("í", "i"); name = name.replace("Í", "I");
	                    			name = name.replace("ó", "o"); name = name.replace("Ó", "O");
	                    			name = name.replace("ú", "u"); name = name.replace("Ú", "U");
	                    			name = name.replace("ñ", "ñ"); name = name.replace("Ñ", "N");
	                    		}
	                    		name += ", " + pi.versionName + ", " + pi.packageName;
		                    }
	                    	SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPOctetString(name));
	                        responseList.addSNMPObject(newPair);
	                    }catch (Exception e){
			            	errorIndex = i+1;
			            	errorStatus = SNMPRequestException.FAILED;
			            }
					}else{
						errorIndex = i+1;
	                    errorStatus = SNMPRequestException.VALUE_NOT_AVAILABLE;
	                    throw new SNMPSetException("Value out of range of the number Software Installed", errorIndex, errorStatus);						
					}
	                	                	
	                
	                continue;
	            } // hrSWInstalledName
	            
	         // hrSWInstalledID ProductID | Read-Only | Current | 1.3.6.1.2.1.25.6.3.1.3.X -> X es el numero de SW
	            if (snmpOID.toString().startsWith("1.3.6.1.2.1.25.6.3.1.3."))
	            {
	            	PackageManager pm = contextActivity.getPackageManager();
	            	ArrayList<ApplicationInfo> l = (ArrayList<ApplicationInfo>) pm.getInstalledApplications(ApplicationInfo.FLAG_INSTALLED);
	            	int nSW = 0, nTSW = l.size();
	        	    /*for (Iterator<ApplicationInfo> iterator = l.iterator(); iterator.hasNext();) {
	        	    	ApplicationInfo pInfo = (ApplicationInfo) iterator.next();
	        			
	        		}*/
					
					// Pregunto cual SW que esta solicitando
					try {
						nSW =  Integer.parseInt(snmpOID.toString().substring(23, snmpOID.toString().length()));
					}catch (Exception e){
		            	errorIndex = i+1;
		            	errorStatus = SNMPRequestException.FAILED;
		            }
	            		
	            	// si el numero de SW solicitado esta, busco el valor y lo envio
					if( 1 <= nSW && nSW <= nTSW ){
	                    try
	                    {
	                    	SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPObjectIdentifier("0.0"));
	                        responseList.addSNMPObject(newPair);
	                    }catch (Exception e){
			            	errorIndex = i+1;
			            	errorStatus = SNMPRequestException.FAILED;
			            }
					}else{
						errorIndex = i+1;
	                    errorStatus = SNMPRequestException.VALUE_NOT_AVAILABLE;
	                    throw new SNMPSetException("Value out of range of the number Software Installed", errorIndex, errorStatus);						
					}
	                	                	
	                 
	                continue;
	            } // hrSWInstalledID
	            
	            
	            // hrSWInstalledType Integer | Read-Only | Current | 1.3.6.1.2.1.25.6.3.1.4.X -> X es el numero de SW
	            if (snmpOID.toString().startsWith("1.3.6.1.2.1.25.6.3.1.4."))
	            {
	            	PackageManager pm = contextActivity.getPackageManager();
	            	ArrayList<ApplicationInfo> l = (ArrayList<ApplicationInfo>) pm.getInstalledApplications(ApplicationInfo.FLAG_INSTALLED);
	            	int nSW = 0, nTSW = l.size();
	        	    /*for (Iterator<ApplicationInfo> iterator = l.iterator(); iterator.hasNext();) {
	        	    	ApplicationInfo pInfo = (ApplicationInfo) iterator.next();
	        			
	        		}*/
					
					// Pregunto cual SW que esta solicitando
					try {
						nSW =  Integer.parseInt(snmpOID.toString().substring(23, snmpOID.toString().length()));
					}catch (Exception e){
		            	errorIndex = i+1;
		            	errorStatus = SNMPRequestException.FAILED;
		            }
	            		
	            	// si el numero de SW solicitado esta, busco el valor y lo envio
					if( 1 <= nSW && nSW <= nTSW ){
	                    try
	                    {
	                    	SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPInteger(4));
	                        responseList.addSNMPObject(newPair);
	                    }catch (Exception e){
			            	errorIndex = i+1;
			            	errorStatus = SNMPRequestException.FAILED;
			            }
					}else{
						errorIndex = i+1;
	                    errorStatus = SNMPRequestException.VALUE_NOT_AVAILABLE;
	                    throw new SNMPSetException("Value out of range of the number Software Installed", errorIndex, errorStatus);						
					}
	                 
	                continue;
	            } // hrSWInstalledType
	            
	            // hrSWInstalledDate DateAndTime | Read-Only | Current | 1.3.6.1.2.1.25.6.3.1.5.X -> X es el numero de SW
	            if (snmpOID.toString().startsWith("1.3.6.1.2.1.25.6.3.1.5."))
	            {
	            	PackageManager pm = contextActivity.getPackageManager();
	            	ArrayList<ApplicationInfo> l = (ArrayList<ApplicationInfo>) pm.getInstalledApplications(ApplicationInfo.FLAG_INSTALLED);
	            	int nSW = 0, nTSW = l.size();
	        	    /*for (Iterator<ApplicationInfo> iterator = l.iterator(); iterator.hasNext();) {
	        	    	ApplicationInfo pInfo = (ApplicationInfo) iterator.next();
	        			
	        		}*/
					
					// Pregunto cual SW que esta solicitando
					try {
						nSW =  Integer.parseInt(snmpOID.toString().substring(23, snmpOID.toString().length()));
					}catch (Exception e){
		            	errorIndex = i+1;
		            	errorStatus = SNMPRequestException.FAILED;
		            }
	            		
	            	// si el numero de SW solicitado esta, busco el valor y lo envio
					if( 1 <= nSW && nSW <= nTSW ){
	                    try
	                    {
	                    	String dateHex = "00:00:01:01:00:00:00:00"; // DEFAULT: January 1, year 0000
	                    	ApplicationInfo ai = l.get(nSW-1);
	                    	Long datetime = Long.valueOf(0);
	                    	if(ai != null){
	                    		PackageInfo pi = pm.getPackageInfo(ai.packageName, PackageManager.GET_ACTIVITIES);
	                    		datetime = pi.lastUpdateTime;
	                    		Calendar cal = Calendar.getInstance();
	                    		cal.setTimeInMillis(datetime);
	                    		int year = cal.get(Calendar.YEAR);
	                    		int month = cal.get(Calendar.MONTH);
	                    		int day = cal.get(Calendar.DATE);
	                    		int hour = cal.get(Calendar.HOUR);
	                    		int minute = cal.get(Calendar.MINUTE);
	                    		int second = cal.get(Calendar.SECOND);
	                    		int millisecond = cal.get(Calendar.MILLISECOND);
	                    		
	                    		String sYear = Integer.toHexString(year);
	                    		if(sYear.length() == 3)
	                    			sYear = "0" + sYear.charAt(0)+ ":" + sYear.substring(1);
	                    		else if (sYear.length() == 4)
	                    			sYear = sYear.charAt(0)+ sYear.charAt(1)+ ":" + sYear.substring(1);
	                    		
	                    		
	                    		dateHex =  sYear +":"+ 
	                    		((Integer.toHexString(month).length()==1)? "0"+Integer.toHexString(month):Integer.toHexString(month)) +":"+
	                    		((Integer.toHexString(day).length()==1)? "0"+Integer.toHexString(day):Integer.toHexString(day)) +":"+
	                    		((Integer.toHexString(hour).length()==1)? "0"+Integer.toHexString(hour):Integer.toHexString(hour)) +":"+
	                    		((Integer.toHexString(minute).length()==1)? "0"+Integer.toHexString(minute):Integer.toHexString(minute)) +":"+
	                    		((Integer.toHexString(second).length()==1)? "0"+Integer.toHexString(second):Integer.toHexString(second)) +":"+
	                    		((Integer.toHexString(millisecond).length()==1)? "0"+Integer.toHexString(millisecond):Integer.toHexString(millisecond));
	                    	}	                    	
	                    	SNMPVariablePair newPair = new SNMPVariablePair(new SNMPObjectIdentifier(snmpOID.toString()), new SNMPOctetString(dateHex));
	                        responseList.addSNMPObject(newPair);
	                    }catch (Exception e){
			            	errorIndex = i+1;
			            	errorStatus = SNMPRequestException.FAILED;
			            }
					}else{
						errorIndex = i+1;
	                    errorStatus = SNMPRequestException.VALUE_NOT_AVAILABLE;
	                    throw new SNMPSetException("Value out of range of the number Software Installed", errorIndex, errorStatus);						
					}
	                	                	
	                
	                continue;
	            } // hrSWInstalledType
				
			}
			
            }//pduType == SNMPBERCodec.SNMPGETREQUEST)

            
        } // for
        
        
        
        for (int i = 0; i < responseList.size(); i++)
        {
            SNMPSequence variablePair = (SNMPSequence)responseList.getSNMPObjectAt(i);
            SNMPObjectIdentifier snmpOID = (SNMPObjectIdentifier)variablePair.getSNMPObjectAt(0);
            SNMPObject snmpValue = (SNMPObject)variablePair.getSNMPObjectAt(1);
            
        }
        
	    return responseList;
        
    
	}

	@Override
	public SNMPSequence processGetNextRequest(SNMPPDU PDU,
			String communityName) throws SNMPGetException{

		SNMPTreeGetNextRequest<String> treeRoot = SNMPTreeGetNextRequest.SNMPTreeLoad(contextActivity);
		SNMPSequence varBindList = PDU.getVarBindList();
		SNMPSequence newList = new SNMPSequence();
		
		for (int i = 0; i < varBindList.size(); i++)
        {
            SNMPSequence variablePair = (SNMPSequence)varBindList.getSNMPObjectAt(i);
            SNMPObjectIdentifier snmpOID = (SNMPObjectIdentifier)variablePair.getSNMPObjectAt(0);
            SNMPObject snmpValue = (SNMPObject)variablePair.getSNMPObjectAt(1);
            SNMPVariablePair newPair = null;
            String nextNode = "";
            try {
            	SNMPTreeGetNextRequest<String> node = treeRoot.findBinSearch(snmpOID.toString());
            	if(node!=null){
        	    	
        	    	nextNode = node.GetNext();
        	    	
        	    }else{
        	    	
            	}
	            newPair = new SNMPVariablePair(new SNMPObjectIdentifier(nextNode), snmpValue);     		
	            newList.addSNMPObject(newPair);
            } catch (Exception e) {
				
			}
			
        }
		
		for (int i = 0; i < newList.size(); i++)
        {
            SNMPSequence variablePair = (SNMPSequence)newList.getSNMPObjectAt(i);
            SNMPObjectIdentifier snmpOID = (SNMPObjectIdentifier)variablePair.getSNMPObjectAt(0);
            SNMPObject snmpValue = (SNMPObject)variablePair.getSNMPObjectAt(1);
            
        }
		
		
        SNMPSequence respList = null;
        SNMPPDU v1PDU = null;
        try {
        	v1PDU = new SNMPPDU(SNMPBERCodec.SNMPGETREQUEST, PDU.getRequestID(), 0, 0, newList);
        	respList = this.processRequest(v1PDU, communityName);
        } catch (SNMPSetException e) {
			
		} catch (SNMPBadValueException e) {
			
		}

        if(respList.size() == 0){
        	respList = newList;
        }
        
        for (int i = 0; i < respList.size(); i++)
        {
            SNMPSequence variablePair = (SNMPSequence)respList.getSNMPObjectAt(i);
            SNMPObjectIdentifier snmpOID = (SNMPObjectIdentifier)variablePair.getSNMPObjectAt(0);
            SNMPObject snmpValue = (SNMPObject)variablePair.getSNMPObjectAt(1);
            
        }
        
		return respList;
	}
	
	
	public SNMPSequence processGetBulkRequest(SNMPv2BulkRequestPDU PDU,
			String communityName) throws SNMPGetException{
		
 
		SNMPSequence newList = PDU.getVarBindList();
		int L = newList.size();
		int N = PDU.getNonRepeaters();
		int maxRep = PDU.getMaxRepetitions();
		int R = L - N;
		SNMPTreeGetNextRequest<String> treeRoot = SNMPTreeGetNextRequest.SNMPTreeLoad(contextActivity);
		
		for (int i = 0 ; i < maxRep ; i++) {	
			for(int j = 0 ; j < R ; j++){
				SNMPSequence variablePair = (SNMPSequence)newList.getSNMPObjectAt(N+j);
				SNMPObjectIdentifier snmpOID = (SNMPObjectIdentifier)variablePair.getSNMPObjectAt(0);
	            SNMPObject snmpValue = (SNMPObject)variablePair.getSNMPObjectAt(1);
	            SNMPVariablePair newPair = null;
	            try {
	            	SNMPTreeGetNextRequest<String> node = treeRoot.findBinSearch(snmpOID.toString());
	            	
		            newPair = new SNMPVariablePair(new SNMPObjectIdentifier(node.GetNext()), snmpValue);     		
		            newList.addSNMPObject(newPair);
				} catch (SNMPBadValueException e) {
					e.printStackTrace();
				} catch (Exception e) {
					
				}			
			}
			
			N+=R; 
		}

		
		SNMPPDU v1PDU = null;
		SNMPSequence respList = null;
		try {
        	v1PDU = new SNMPPDU(SNMPBERCodec.SNMPGETREQUEST, PDU.getRequestID(), 0, 0, newList);
        	respList = this.processRequest(v1PDU, communityName);
		} catch (SNMPSetException e) {
			e.printStackTrace();
		} catch (SNMPBadValueException e) {
			e.printStackTrace();
		}
		
		return respList;
	}

}
