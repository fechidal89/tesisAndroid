package ucv.tesis.tesisandroid;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

public class SNMPTreeGetNextRequest<T> implements Iterable<SNMPTreeGetNextRequest<String>> {

	
		String data;
	    boolean tableOrEntry;
	    SNMPTreeGetNextRequest<String> parent;
	    SNMPTreeGetNextRequest<String> nNext;
	    List<SNMPTreeGetNextRequest<String>> children;
	    private List<SNMPTreeGetNextRequest<String>> elementsIndex;
	    
	    public SNMPTreeGetNextRequest(String data) {
	        this.data = data;
	        this.tableOrEntry = true;
	        this.children = new LinkedList<SNMPTreeGetNextRequest<String>>();
	    }
	    public SNMPTreeGetNextRequest(SNMPTreeGetNextRequest<String> node) {
	        this.data = node.data;
	        this.tableOrEntry = node.tableOrEntry;
	        this.parent = node.parent;
	        this.nNext = node.nNext;
	        this.children = node.children;
	        this.elementsIndex = node.elementsIndex;	        
	    }

	   /* public SNMPTreeGetNextRequest<String> addNode(String child) {
	    	this.tableOrEntry = false;
	    	SNMPTreeGetNextRequest<String> childNode = new SNMPTreeGetNextRequest<String>(child);
	        childNode.parent = this;
	        childNode.nNext = null;
	        if(this.children.size()>0)
	        	this.children.get(this.children.size()-1).nNext = childNode;
	        this.children.add(childNode);
	        return childNode;
	    }*/
	    
	    public SNMPTreeGetNextRequest<String> addNode(String child, boolean TOrE) {
	    	this.tableOrEntry = TOrE;
	    	SNMPTreeGetNextRequest<String> childNode = new SNMPTreeGetNextRequest<String>(child);
	        childNode.parent = (SNMPTreeGetNextRequest<String>) this;
	        childNode.tableOrEntry = TOrE;
	        childNode.nNext = null;
	        if(this.children.size()>0)
	        	this.children.get(this.children.size()-1).nNext = childNode;
	        this.children.add(childNode);
	        return childNode;
	    }
	    
	    public void addLeaf(String child) {
	    	this.tableOrEntry = true;
	    	SNMPTreeGetNextRequest<String> childNode = new SNMPTreeGetNextRequest<String>(child);
	        childNode.parent = (SNMPTreeGetNextRequest<String>) this;
	        //childNode.nNext = null;
	        childNode.tableOrEntry = false;
	        if(this.children.size()>0)
	        	this.children.get(this.children.size()-1).nNext = childNode;
	        /*
	        if(this.nNext == null){
	        	SNMPTreeGetNextRequest<String> node = this.parent;
	        	if(node != null){
		        	while(node.nNext == null && node.parent != null){
		        		node = node.parent;
		        	}
		        	childNode.nNext = node.nNext;
		        }else{
		        	childNode.nNext = null;
		        }
	        }else{
	        	childNode.nNext = this.nNext;
	        }*/
	        
	        this.children.add(childNode);
	    }
	    public boolean isRoot() {
            return parent == null;
	    }
	
	    public boolean isLeaf() {
	            return children.size() == 0;
	    }

	    public int getLevel() {
            if (this.isRoot())
                    return 0;
            else
                    return parent.getLevel() + 1;
	    }
	
	    private void registerChildForSearch(SNMPTreeGetNextRequest<String> node) {
	            elementsIndex.add(node);
	            if (parent != null)
	                    parent.registerChildForSearch(node);
	    }
	
	    public SNMPTreeGetNextRequest<String> findTreeNode(Comparable<String> cmp) {
	            for (SNMPTreeGetNextRequest<String> element : this.elementsIndex) {
	            		String elData = element.data;
	                    if (cmp.compareTo(elData) == 0)
	                            return element;
	            }
	            return null;
	    }
	
	    public final SNMPTreeGetNextRequest<String> findBinSearch(String oid){
	    	
	    	List<SNMPTreeGetNextRequest<String>> L =  this.children;
	    	int izq = 0;
	    	int der = L.size()-1;
	    	int center = (izq+der)/2;
	    	//int level = 1;
	    	
	    	if(oid.equalsIgnoreCase(this.data)){
	    		//
	    		return (SNMPTreeGetNextRequest<String>) this;
	    	}
	    	
	    	while(true){
	    		izq = 0;
	    		der = L.size()-1;
		    	center = (izq+der)/2;
		    	/*
		    	for (Iterator<SNMPTreeGetNextRequest<String>> iterator = L.iterator(); iterator.hasNext();) {
					SNMPTreeGetNextRequest<String> snmpTreeGetNextRequest = (SNMPTreeGetNextRequest<String>) iterator.next();
					
				}
		    	
		    	*/
		    	while((izq<= der) && !(oid.contains(L.get(center).data))){
		    		
	    			
		    		if(compareOID(oid,L.get(center).data) < 0 ){
		    			
		    			der = center - 1;
		    		}else{
		    			
		    			izq = center + 1;
		    		} 
		    		
		    		center = (izq+der)/2;
		    	}
		    	
		    	if(izq>der)
		    		return null;
		    	else if(L.get(center).data.equalsIgnoreCase(oid)){
		    		break;
		    	}else{
		    		L = L.get(center).children;
		    		//level++;
		    		
		    	}
	    	}
	    	
		    return  L.get(center);
	    	
	    }
	    
	    public final int compareOID(String principal, String segundario){
	    	
	    	principal = principal.replace(".", " ");
	    	segundario = segundario.replace(".", " ");
	    	String[] arrA = principal.split(" ");
	    	String[] arrB = segundario.split(" ");
	    	int tamMin = (arrA.length > arrB.length )? arrB.length : arrA.length;
	    	if(tamMin > 15) tamMin = 15; // caso tcp y udp con las ips y puertos, solo hasta el puerto origen
	    	int rest = 0;
	    	for (int i = 0; i < tamMin; i++) {
	    		rest += Integer.parseInt(arrA[i]) - Integer.parseInt(arrB[i]); 
	    	}
	    	
	    	return rest;
	    }
	    
	    
	    public String GetNext(){
	    	
	    	if(this.tableOrEntry){
	    		if(this.children != null && this.children.size() > 0){
	    			SNMPTreeGetNextRequest<String> node = this.children.get(0);
	    			
	    			while(node.tableOrEntry){
	    				if(node.children != null && node.children.size() > 0){
	    	    			node = node.children.get(0);
	    				}
	    			}
	    			
	    			return node.data;
	    		}	    			
	    	}else{
	    		
	    		if(this.nNext != null){
	    			SNMPTreeGetNextRequest<String> node = this.nNext;
	    			while(node.tableOrEntry){
	    				if(node.children != null && node.children.size() > 0){
	    	    			node = node.children.get(0);
	    				}else{ // no tiene hijos pero tiene siguiente caso de los Entry del Grupo At
	    					if(node.nNext != null){
	    						
	    						node = node.nNext;
	    					}
	    				}
	    			}
	    			return node.data;
	    		}else{
	    			
	    			return this.data;
	    		}
	    	}
			return (String)"";
	    	
	    	
	    }
	    
	    
	    @Override
	    public String toString() {
	            return data != null ? data : "[data null]";
	    }
	
	    @Override
	    public Iterator<SNMPTreeGetNextRequest<String>> iterator() {
	    		SNMPTreeGetNextRequestIter<String> iter = new SNMPTreeGetNextRequestIter<String>((SNMPTreeGetNextRequest<String>) this);
	            return iter;
	    }
	    
	    @SuppressLint("InlinedApi")
		public final static SNMPTreeGetNextRequest<String> SNMPTreeLoad(Context contAct )
	    {
	    	SNMPTreeGetNextRequest<String> root = new SNMPTreeGetNextRequest<String>("1.3.6.1.2.1");
	    	{
	    		/**
	    		 * SYSTEM
	    		 */	    		
	    		SNMPTreeGetNextRequest<String> system = root.addNode("1.3.6.1.2.1.1", true);
	    		system.addLeaf("1.3.6.1.2.1.1.1"); //sysDescr
	    		system.addLeaf("1.3.6.1.2.1.1.2"); //sysObjectID
	    		system.addLeaf("1.3.6.1.2.1.1.3"); //sysUpTime
	    		system.addLeaf("1.3.6.1.2.1.1.4"); //sysContact
	    		system.addLeaf("1.3.6.1.2.1.1.5"); //sysName
	    		system.addLeaf("1.3.6.1.2.1.1.6"); //sysLocation
	    		system.addLeaf("1.3.6.1.2.1.1.7"); //sysServices
	    		
	    		/**
	    		 * INTERFACES
	    		 */
	    		SNMPTreeGetNextRequest<String> interfaces = root.addNode("1.3.6.1.2.1.2", true);
	    		interfaces.addLeaf("1.3.6.1.2.1.2.1"); //ifNumber
	    		SNMPTreeGetNextRequest<String> iftable = interfaces.addNode("1.3.6.1.2.1.2.2", true); //ifTable
	    		{
    				int nIf = 0; // numero de interface
                    Process p=null;
                    try {
						p = Runtime.getRuntime().exec("ls /sys/class/net/");
						BufferedReader in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
						String line;
				        while ((line = in2.readLine()) != null) {  
				        	nIf += 1; 						        	
				        }
                    }catch(Exception e){
                    	e.printStackTrace();
                    }
                    
	    			SNMPTreeGetNextRequest<String> ifEntry = iftable.addNode("1.3.6.1.2.1.2.2.1", true); // ifEntry
	    			{
	    				/**ifIndex*/
						SNMPTreeGetNextRequest<String> ifIndex = ifEntry.addNode("1.3.6.1.2.1.2.2.1.1", true);
	    				for(int iface = 1 ; iface <= nIf ; iface++ ){
							ifIndex.addLeaf("1.3.6.1.2.1.2.2.1.1."+iface);
						}
						/**ifDescr*/
						SNMPTreeGetNextRequest<String> ifDescr = ifEntry.addNode("1.3.6.1.2.1.2.2.1.2", true);
	    				for(int iface = 1 ; iface <= nIf ; iface++ ){
							ifDescr.addLeaf("1.3.6.1.2.1.2.2.1.2."+iface);
						}
	    				/**ifType*/
	    				SNMPTreeGetNextRequest<String> ifType = ifEntry.addNode("1.3.6.1.2.1.2.2.1.3", true);
	    				for(int iface = 1 ; iface <= nIf ; iface++ ){
	    					ifType.addLeaf("1.3.6.1.2.1.2.2.1.3."+iface);
						}
	    				/**ifMtu*/
	    				SNMPTreeGetNextRequest<String> ifMtu = ifEntry.addNode("1.3.6.1.2.1.2.2.1.4", true);
	    				for(int iface = 1 ; iface <= nIf ; iface++ ){
	    					ifMtu.addLeaf("1.3.6.1.2.1.2.2.1.4."+iface);
						}
	    				/**ifSpeed*/
	    				SNMPTreeGetNextRequest<String> ifSpeed = ifEntry.addNode("1.3.6.1.2.1.2.2.1.5", true);
	    				for(int iface = 1 ; iface <= nIf ; iface++ ){
	    					ifSpeed.addLeaf("1.3.6.1.2.1.2.2.1.5."+iface);
	    				} 
	    				/**ifPhysAddress*/
	    				SNMPTreeGetNextRequest<String> ifPhysAddress = ifEntry.addNode("1.3.6.1.2.1.2.2.1.6", true);
	    				for(int iface = 1 ; iface <= nIf ; iface++ ){
	    					ifPhysAddress.addLeaf("1.3.6.1.2.1.2.2.1.6."+iface);
	    				}
	    				/**ifAdminStatus*/
	    				SNMPTreeGetNextRequest<String> ifAdminStatus = ifEntry.addNode("1.3.6.1.2.1.2.2.1.7", true);
	    				for(int iface = 1 ; iface <= nIf ; iface++ ){
	    					ifAdminStatus.addLeaf("1.3.6.1.2.1.2.2.1.7."+iface);
	    				} 
	    				/**ifOperStatus*/
	    				SNMPTreeGetNextRequest<String> ifOperStatus = ifEntry.addNode("1.3.6.1.2.1.2.2.1.8", true);
	    				for(int iface = 1 ; iface <= nIf ; iface++ ){
	    					ifOperStatus.addLeaf("1.3.6.1.2.1.2.2.1.8."+iface);
	    				} 
	    				/**ifLastChange*/
	    				SNMPTreeGetNextRequest<String> ifLastChange = ifEntry.addNode("1.3.6.1.2.1.2.2.1.9", true);
	    				for(int iface = 1 ; iface <= nIf ; iface++ ){
	    					ifLastChange.addLeaf("1.3.6.1.2.1.2.2.1.9."+iface);
	    				}
	    				/**ifInOctets*/
	    				SNMPTreeGetNextRequest<String> ifInOctets = ifEntry.addNode("1.3.6.1.2.1.2.2.1.10", true);
	    				for(int iface = 1 ; iface <= nIf ; iface++ ){
	    					ifInOctets.addLeaf("1.3.6.1.2.1.2.2.1.10."+iface);
	    				}
	    				/**ifInUcastPkts*/
	    				SNMPTreeGetNextRequest<String> ifInUcastPkts = ifEntry.addNode("1.3.6.1.2.1.2.2.1.11", true);
	    				for(int iface = 1 ; iface <= nIf ; iface++ ){
	    					ifInUcastPkts.addLeaf("1.3.6.1.2.1.2.2.1.11."+iface);
	    				}
	    				/**ifInNUcastPkts*/
	    				SNMPTreeGetNextRequest<String> ifInNUcastPkts = ifEntry.addNode("1.3.6.1.2.1.2.2.1.12", true);
	    				for(int iface = 1 ; iface <= nIf ; iface++ ){
	    					ifInNUcastPkts.addLeaf("1.3.6.1.2.1.2.2.1.12."+iface);
	    				} 
	    				/**ifInDiscards*/
	    				SNMPTreeGetNextRequest<String> ifInDiscards = ifEntry.addNode("1.3.6.1.2.1.2.2.1.13", true);
	    				for(int iface = 1 ; iface <= nIf ; iface++ ){
	    					ifInDiscards.addLeaf("1.3.6.1.2.1.2.2.1.13."+iface);
	    				}
	    				/**ifInErrors*/
	    				SNMPTreeGetNextRequest<String> ifInErrors = ifEntry.addNode("1.3.6.1.2.1.2.2.1.14", true);
	    				for(int iface = 1 ; iface <= nIf ; iface++ ){
	    					ifInErrors.addLeaf("1.3.6.1.2.1.2.2.1.14."+iface);
	    				}
	    				/**ifInUnknownProtos*/
	    				SNMPTreeGetNextRequest<String> ifInUnknownProtos = ifEntry.addNode("1.3.6.1.2.1.2.2.1.15", true);
	    				for(int iface = 1 ; iface <= nIf ; iface++ ){
	    					ifInUnknownProtos.addLeaf("1.3.6.1.2.1.2.2.1.15."+iface);
	    				}
	    				/**ifOutOctets*/
	    				SNMPTreeGetNextRequest<String> ifOutOctets = ifEntry.addNode("1.3.6.1.2.1.2.2.1.16", true);
	    				for(int iface = 1 ; iface <= nIf ; iface++ ){
	    					ifOutOctets.addLeaf("1.3.6.1.2.1.2.2.1.16."+iface);
	    				}
	    				/**ifOutUcastPkts*/
	    				SNMPTreeGetNextRequest<String> ifOutUcastPkts = ifEntry.addNode("1.3.6.1.2.1.2.2.1.17", true);
	    				for(int iface = 1 ; iface <= nIf ; iface++ ){
	    					ifOutUcastPkts.addLeaf("1.3.6.1.2.1.2.2.1.17."+iface);
	    				}
	    				/**ifOutNUcastPkts*/
	    				SNMPTreeGetNextRequest<String> ifOutNUcastPkts = ifEntry.addNode("1.3.6.1.2.1.2.2.1.18", true);
	    				for(int iface = 1 ; iface <= nIf ; iface++ ){
	    					ifOutNUcastPkts.addLeaf("1.3.6.1.2.1.2.2.1.18."+iface);
	    				}
	    				/**ifOutDiscards*/
	    				SNMPTreeGetNextRequest<String> ifOutDiscards = ifEntry.addNode("1.3.6.1.2.1.2.2.1.19", true);
	    				for(int iface = 1 ; iface <= nIf ; iface++ ){
	    					ifOutDiscards.addLeaf("1.3.6.1.2.1.2.2.1.19."+iface);
	    				} 
	    				/**ifOutErrors*/
	    				SNMPTreeGetNextRequest<String> ifOutErrors = ifEntry.addNode("1.3.6.1.2.1.2.2.1.20", true);
	    				for(int iface = 1 ; iface <= nIf ; iface++ ){
	    					ifOutErrors.addLeaf("1.3.6.1.2.1.2.2.1.20."+iface);
	    				} 
	    				/**ifOutQLen*/
	    				SNMPTreeGetNextRequest<String> ifOutQLen = ifEntry.addNode("1.3.6.1.2.1.2.2.1.21", true);
	    				for(int iface = 1 ; iface <= nIf ; iface++ ){
	    					ifOutQLen.addLeaf("1.3.6.1.2.1.2.2.1.21."+iface);
	    				}
	    				/**ifSpecific*/
	    				SNMPTreeGetNextRequest<String> ifSpecific = ifEntry.addNode("1.3.6.1.2.1.2.2.1.22", true);
	    				for(int iface = 1 ; iface <= nIf ; iface++ ){
	    					ifSpecific.addLeaf("1.3.6.1.2.1.2.2.1.22."+iface);
	    				}
	    			}// ifEntry	    			
	    			
	    		}//ifTable
	    		
	    		/**
	    		 * AT
	    		 */
	    		SNMPTreeGetNextRequest<String> at = root.addNode("1.3.6.1.2.1.3", true);
	    		SNMPTreeGetNextRequest<String> atTable = at.addNode("1.3.6.1.2.1.3.1", true); //atTable
	    		{
	    			SNMPTreeGetNextRequest<String> atEntry = atTable.addNode("1.3.6.1.2.1.3.1.1", true); //atEntry
	    			{
	    				SNMPTreeGetNextRequest<String> atIfIndex = atEntry.addNode("1.3.6.1.2.1.3.1.1.1", true); //atIfIndex
	    				SNMPTreeGetNextRequest<String> atPhysAddress = atEntry.addNode("1.3.6.1.2.1.3.1.1.2", true); //atPhysAddress
	    				SNMPTreeGetNextRequest<String> atNetAddress = atEntry.addNode("1.3.6.1.2.1.3.1.1.3", true); //atNetAddress
	    			}
	    		}
	    		
	    		/**
	    		 * IP
	    		 */
	    		SNMPTreeGetNextRequest<String> ip = root.addNode("1.3.6.1.2.1.4", true);
	    		{
	    			ip.addLeaf("1.3.6.1.2.1.4.1");// ipForwarding
	    			ip.addLeaf("1.3.6.1.2.1.4.2");// ipDefaultTTL
					ip.addLeaf("1.3.6.1.2.1.4.3");// ipInReceives
					ip.addLeaf("1.3.6.1.2.1.4.4");// ipInHdrErrors
					ip.addLeaf("1.3.6.1.2.1.4.5");// ipInAddrErrors
					ip.addLeaf("1.3.6.1.2.1.4.6");// ipForwDatagrams
					ip.addLeaf("1.3.6.1.2.1.4.7");// ipInUnknownProtos
					ip.addLeaf("1.3.6.1.2.1.4.8");// ipInDiscards
					ip.addLeaf("1.3.6.1.2.1.4.9");// ipInDelivers
					ip.addLeaf("1.3.6.1.2.1.4.10");// ipOutRequests
					ip.addLeaf("1.3.6.1.2.1.4.11");// ipOutDiscards
					ip.addLeaf("1.3.6.1.2.1.4.12");// ipOutNoRoutes
					ip.addLeaf("1.3.6.1.2.1.4.13");// ipReasmTimeout
					ip.addLeaf("1.3.6.1.2.1.4.14");// ipReasmReqds
					ip.addLeaf("1.3.6.1.2.1.4.15");// ipReasmOKs
					ip.addLeaf("1.3.6.1.2.1.4.16");// ipReasmFails
					ip.addLeaf("1.3.6.1.2.1.4.17");// ipFragOKs
					ip.addLeaf("1.3.6.1.2.1.4.18");// ipFragFails
					ip.addLeaf("1.3.6.1.2.1.4.19");// ipFragCreates
	    			
					SNMPTreeGetNextRequest<String> ipAddrTable = ip.addNode("1.3.6.1.2.1.4.20", true); // ipAddrTable
					
					SNMPTreeGetNextRequest<String> ipAddrEntry = ipAddrTable.addNode("1.3.6.1.2.1.4.20.1", true); //ipAddrEntry
					{
						String cmd="ip address show"+"\n";
		            	String line = "";
		            	Process p=null;
		            	ArrayList<String> lines = new ArrayList<String>();
		        		BufferedReader in2=null;
		        		ArrayList<String> table = new ArrayList<String>();
		            	try
		                {
		            		p = Runtime.getRuntime().exec(cmd);
		        			in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
		        			
		        			while ((line =in2.readLine()) != null) {
		        				lines.add(line);
		        			}
		        			
		        			int x = 0;
		        			String cadena = "";
		        			cadena = lines.get(x);
		        			
		        			while ( x < lines.size()) {

		        				if(cadena.contains("mtu") && cadena.contains("qdisc") && cadena.contains("state") ){

		        					String arrTmp[] = cadena.split(" ");
		        					do{
		        						if(!cadena.contains("inet6") && cadena.contains("inet")){
		                					arrTmp = cadena.split(" ");
		                					String arrTmp1[] = arrTmp[5].split("/");
		                					String ipAddr = arrTmp1[0];
		                					table.add(ipAddr);
		        						}
		        						x++;
		        						if (x >= lines.size()) break; 
		        						cadena = lines.get(x);
		        					}while(!(cadena.contains("mtu") && cadena.contains("qdisc") && cadena.contains("state")));
		        				} // if
		        			} // while ( x < lines.size()) 
		                }catch(Exception e){
		                	e.printStackTrace();
		                }
		            	
						/** ipAdEntAddr*/
		            	SNMPTreeGetNextRequest<String> ipAdEntAddr = ipAddrEntry.addNode("1.3.6.1.2.1.4.20.1.1", true);
		            	for (Iterator<String> iterator = table.iterator(); iterator.hasNext();) {
							String str = (String) iterator.next();
							ipAdEntAddr.addLeaf("1.3.6.1.2.1.4.20.1.1."+str);
						}
		            	/** ipAdEntIfIndex*/
		            	SNMPTreeGetNextRequest<String> ipAdEntIfIndex = ipAddrEntry.addNode("1.3.6.1.2.1.4.20.1.2", true);
		            	for (Iterator<String> iterator = table.iterator(); iterator.hasNext();) {
							String str = (String) iterator.next();
							ipAdEntIfIndex.addLeaf("1.3.6.1.2.1.4.20.1.2."+str);
						}
		            	/** ipAdEntNetMask*/
		            	SNMPTreeGetNextRequest<String> ipAdEntNetMask = ipAddrEntry.addNode("1.3.6.1.2.1.4.20.1.3", true);
		            	for (Iterator<String> iterator = table.iterator(); iterator.hasNext();) {
							String str = (String) iterator.next();
							ipAdEntNetMask.addLeaf("1.3.6.1.2.1.4.20.1.3."+str);
						}
		            	/** ipAdEntBcastAddr*/
		            	SNMPTreeGetNextRequest<String> ipAdEntBcastAddr = ipAddrEntry.addNode("1.3.6.1.2.1.4.20.1.4", true);
		            	for (Iterator<String> iterator = table.iterator(); iterator.hasNext();) {
							String str = (String) iterator.next();
							ipAdEntBcastAddr.addLeaf("1.3.6.1.2.1.4.20.1.4."+str);
						}
		            	/** ipAdEntReasmMaxSize*/
		            	SNMPTreeGetNextRequest<String> ipAdEntReasmMaxSize = ipAddrEntry.addNode("1.3.6.1.2.1.4.20.1.5", true);
		            	for (Iterator<String> iterator = table.iterator(); iterator.hasNext();) {
							String str = (String) iterator.next();
							ipAdEntReasmMaxSize.addLeaf("1.3.6.1.2.1.4.20.1.5."+str);
						}
	            	
					}
					
					SNMPTreeGetNextRequest<String> ipRouteTable = ip.addNode("1.3.6.1.2.1.4.21", true); // ipRouteTable
					{
						SNMPTreeGetNextRequest<String> ipRouteEntry = ipRouteTable.addNode("1.3.6.1.2.1.4.21.1", true); // ipRouteEntry
						{
							String cmd="cat /proc/net/route\n";
		                	String line = "";
		                	Process p=null;
		                	ArrayList<String> lines = new ArrayList<String>();
		            		BufferedReader in2=null;
		            		ArrayList<String> table = new ArrayList<String>();
		                	try
		                    {
		                		p = Runtime.getRuntime().exec(cmd);
		            			in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
		            			
		            			while ((line =in2.readLine()) != null) {
		            				lines.add(line);
		            			}
		            			
		            			for(int x = 1 ; x < lines.size() ; x++){
									String arrTmp[] = lines.get(x).split("\t");
									String hexValue = arrTmp[1];
									String ipAddr = "";
								
									for(int i1 = hexValue.length(); i1 > 0; i1 = i1 - 2) {
										ipAddr = ipAddr + Integer.valueOf(hexValue.substring(i1-2, i1 ), 16) + ".";
									}
									ipAddr= (ipAddr.substring(0, ipAddr.length()-1));
									table.add(ipAddr);
		            			}
		                    }catch(Exception e){
		                    	e.printStackTrace();
		                    }
		                	
		                	/**ipRouteDest*/
			            	SNMPTreeGetNextRequest<String> ipRouteDest = ipRouteEntry.addNode("1.3.6.1.2.1.4.21.1.1", true);
			            	for (Iterator<String> iterator = table.iterator(); iterator.hasNext();) {
								String str = (String) iterator.next();
								ipRouteDest.addLeaf("1.3.6.1.2.1.4.21.1.1."+str);
							}
			            	/**ipRouteIfIndex*/
			            	SNMPTreeGetNextRequest<String> ipRouteIfIndex = ipRouteEntry.addNode("1.3.6.1.2.1.4.21.1.2", true);
			            	for (Iterator<String> iterator = table.iterator(); iterator.hasNext();) {
								String str = (String) iterator.next();
								ipRouteIfIndex.addLeaf("1.3.6.1.2.1.4.21.1.2."+str);
							}
			            	/**ipRouteMetric1*/
			            	SNMPTreeGetNextRequest<String> ipRouteMetric1 = ipRouteEntry.addNode("1.3.6.1.2.1.4.21.1.3", true);
			            	for (Iterator<String> iterator = table.iterator(); iterator.hasNext();) {
								String str = (String) iterator.next();
								ipRouteMetric1.addLeaf("1.3.6.1.2.1.4.21.1.3."+str);
							}
			            	/**ipRouteNextHop*/
			            	SNMPTreeGetNextRequest<String> ipRouteNextHop = ipRouteEntry.addNode("1.3.6.1.2.1.4.21.1.7", true);
			            	for (Iterator<String> iterator = table.iterator(); iterator.hasNext();) {
								String str = (String) iterator.next();
								ipRouteNextHop.addLeaf("1.3.6.1.2.1.4.21.1.7."+str);
							}
			            	/**ipRouteMask*/
			            	SNMPTreeGetNextRequest<String> ipRouteMask = ipRouteEntry.addNode("1.3.6.1.2.1.4.21.1.11", true);
			            	for (Iterator<String> iterator = table.iterator(); iterator.hasNext();) {
								String str = (String) iterator.next();
								ipRouteMask.addLeaf("1.3.6.1.2.1.4.21.1.11."+str);
							}
						}
					}
					
					SNMPTreeGetNextRequest<String> ipNetToMediaTable = ip.addNode("1.3.6.1.2.1.4.22", true); // ipNetToMediaTable
					
					SNMPTreeGetNextRequest<String> ipNetToMediaEntry = ipNetToMediaTable.addNode("1.3.6.1.2.1.4.22.1", true); // ipNetToMediaTable
					{
						String cmd="ip address show"+"\n";
		            	String line = "";
		            	Process p=null;
		            	ArrayList<String> lines = new ArrayList<String>();
		        		BufferedReader in2=null;
		        		ArrayList<String> table = new ArrayList<String>();
		            	try
		                {
		            		p = Runtime.getRuntime().exec(cmd);
		        			in2 = new BufferedReader(new InputStreamReader(p.getInputStream()));
		        			
		        			while ((line =in2.readLine()) != null) {
		        				lines.add(line);
		        			}
		        			
		        			int x = 0;
		        			String cadena = "";
		        			cadena = lines.get(x);
		        			
		        			while ( x < lines.size()) {

		        				if(cadena.contains("mtu") && cadena.contains("qdisc") && cadena.contains("state") ){

		        					String arrTmp[] = cadena.split(" ");
		        					do{
		        						if(!cadena.contains("inet6") && cadena.contains("inet")){
		                					arrTmp = cadena.split(" ");
		                					String arrTmp1[] = arrTmp[5].split("/");
		                					String ipAddr = arrTmp1[0];
		                					table.add(ipAddr);
		        						}
		        						x++;
		        						if (x >= lines.size()) break; 
		        						cadena = lines.get(x);
		        					}while(!(cadena.contains("mtu") && cadena.contains("qdisc") && cadena.contains("state")));
		        				} // if
		        			} // while ( x < lines.size()) 
		                }catch(Exception e){
		                	e.printStackTrace();
		                }
		            	
		            	/**ipNetToMediaIfIndex*/
		            	SNMPTreeGetNextRequest<String> ipNetToMediaIfIndex = ipNetToMediaEntry.addNode("1.3.6.1.2.1.4.22.1.1", true);
		            	for (Iterator<String> iterator = table.iterator(); iterator.hasNext();) {
							String str = (String) iterator.next();
							ipNetToMediaIfIndex.addLeaf("1.3.6.1.2.1.4.22.1.1."+str);
						}
		            	/**ipNetToMediaPhysAddress*/
		            	SNMPTreeGetNextRequest<String> ipNetToMediaPhysAddress = ipNetToMediaEntry.addNode("1.3.6.1.2.1.4.22.1.2", true);
		            	for (Iterator<String> iterator = table.iterator(); iterator.hasNext();) {
							String str = (String) iterator.next();
							ipNetToMediaPhysAddress.addLeaf("1.3.6.1.2.1.4.22.1.2."+str);
						}
		            	/**ipNetToMediaNetAddress*/
		            	SNMPTreeGetNextRequest<String> ipNetToMediaNetAddress = ipNetToMediaEntry.addNode("1.3.6.1.2.1.4.22.1.3", true);
		            	for (Iterator<String> iterator = table.iterator(); iterator.hasNext();) {
							String str = (String) iterator.next();
							ipNetToMediaNetAddress.addLeaf("1.3.6.1.2.1.4.22.1.3."+str);
						}
		            	/**ipNetToMediaType*/
		            	SNMPTreeGetNextRequest<String> ipNetToMediaType = ipNetToMediaEntry.addNode("1.3.6.1.2.1.4.22.1.4", true);
		            	for (Iterator<String> iterator = table.iterator(); iterator.hasNext();) {
							String str = (String) iterator.next();
							ipNetToMediaType.addLeaf("1.3.6.1.2.1.4.22.1.4."+str);
						}
						
					}
					
	    			//ip.addLeaf("1.3.6.1.2.1.4.23");// ipRoutingDiscards no implementado por ser adicional y no encontrarse.
	    			
	    		} // ip
	    		
	    		/**
	    		 * ICMP
	    		 */
	    		SNMPTreeGetNextRequest<String> icmp = root.addNode("1.3.6.1.2.1.5", true);
	    		{
	    			icmp.addLeaf("1.3.6.1.2.1.5.1");// icmpInMsgs
	    			icmp.addLeaf("1.3.6.1.2.1.5.2");// icmpInErrors
					icmp.addLeaf("1.3.6.1.2.1.5.3");// icmpInDestUnreachs
					icmp.addLeaf("1.3.6.1.2.1.5.4");// icmpInTimeExcds
					icmp.addLeaf("1.3.6.1.2.1.5.5");// icmpInParmProbs
					icmp.addLeaf("1.3.6.1.2.1.5.6");// icmpInSrcQuenchs
					icmp.addLeaf("1.3.6.1.2.1.5.7");// icmpInRedirects
					icmp.addLeaf("1.3.6.1.2.1.5.8");// icmpInEchos
					icmp.addLeaf("1.3.6.1.2.1.5.9");// icmpInEchoReps
					icmp.addLeaf("1.3.6.1.2.1.5.10");// icmpInTimestamps
					icmp.addLeaf("1.3.6.1.2.1.5.11");// icmpInTimestampReps
					icmp.addLeaf("1.3.6.1.2.1.5.12");// icmpInAddrMasks
					icmp.addLeaf("1.3.6.1.2.1.5.13");// icmpInAddrMaskReps
					icmp.addLeaf("1.3.6.1.2.1.5.14");// icmpOutMsgs
					icmp.addLeaf("1.3.6.1.2.1.5.15");// icmpOutErrors
					icmp.addLeaf("1.3.6.1.2.1.5.16");// icmpOutDestUnreachs
					icmp.addLeaf("1.3.6.1.2.1.5.17");// icmpOutTimeExcds
					icmp.addLeaf("1.3.6.1.2.1.5.18");// icmpOutParmProbs
					icmp.addLeaf("1.3.6.1.2.1.5.19");// icmpOutSrcQuenchs
					icmp.addLeaf("1.3.6.1.2.1.5.20");// icmpOutRedirects
					icmp.addLeaf("1.3.6.1.2.1.5.21");// icmpOutEchos
					icmp.addLeaf("1.3.6.1.2.1.5.22");// icmpOutEchoReps
					icmp.addLeaf("1.3.6.1.2.1.5.23");// icmpOutTimestamps
					icmp.addLeaf("1.3.6.1.2.1.5.24");// icmpOutTimestampReps
					icmp.addLeaf("1.3.6.1.2.1.5.25");// icmpOutAddrMasks
					icmp.addLeaf("1.3.6.1.2.1.5.26");// icmpOutAddrMaskReps
	    		}
	    	
	    		/**
	    		 * TCP
	    		 */
	    		SNMPTreeGetNextRequest<String> tcp = root.addNode("1.3.6.1.2.1.6", true);
	    		{
	    			tcp.addLeaf("1.3.6.1.2.1.6.1");// tcpRtoAlgorithm
	    			tcp.addLeaf("1.3.6.1.2.1.6.2");// tcpRtoMin
	    			tcp.addLeaf("1.3.6.1.2.1.6.3");// tcpRtoMax
	    			tcp.addLeaf("1.3.6.1.2.1.6.4");// tcpMaxConn
					tcp.addLeaf("1.3.6.1.2.1.6.5");// tcpActiveOpens
	    			tcp.addLeaf("1.3.6.1.2.1.6.6");// tcpPassiveOpens
					tcp.addLeaf("1.3.6.1.2.1.6.7");// tcpAttemptFails
	    			tcp.addLeaf("1.3.6.1.2.1.6.8");// tcpEstabResets
	    			tcp.addLeaf("1.3.6.1.2.1.6.9");// tcpCurrEstab
	    			tcp.addLeaf("1.3.6.1.2.1.6.10");// tcpInSegs
	    			tcp.addLeaf("1.3.6.1.2.1.6.11");// tcpOutSegs
	    			tcp.addLeaf("1.3.6.1.2.1.6.12");// tcpRetransSegs
	    			
	    			SNMPTreeGetNextRequest<String> tcpConnTable = tcp.addNode("1.3.6.1.2.1.6.13", true); // tcpConnTable
	    			
	    			SNMPTreeGetNextRequest<String> tcpConnEntry = tcpConnTable.addNode("1.3.6.1.2.1.6.13.1", true);
	    			{
	            		String cmd="netstat\n", line, oidConn="";
	            		Process p=null;
	                	ArrayList<String> lines = new ArrayList<String>();
	            		BufferedReader in2=null;
	            		int stateConn=0;
	            		ArrayList<String> table = new ArrayList<String>();
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
	            				} // endif
	            				
		            			table.add(oidConn);
		            			
							} // for
	            			
	            			java.util.Collections.sort(table);	            			
	                    }
	            		catch (Exception e)
	   	                {
	   	                	 e.printStackTrace();
	   	                }
	                	
	                	/**tcpConnState*/
		            	SNMPTreeGetNextRequest<String> tcpConnState = tcpConnEntry.addNode("1.3.6.1.2.1.6.13.1.1", true);
		            	for (Iterator<String> iterator = table.iterator(); iterator.hasNext();) {
							String str = (String) iterator.next();
							tcpConnState.addLeaf("1.3.6.1.2.1.6.13.1.1."+str);
						}
		            	/**tcpConnLocalAddress*/
		            	SNMPTreeGetNextRequest<String> tcpConnLocalAddress = tcpConnEntry.addNode("1.3.6.1.2.1.6.13.1.2", true);
		            	for (Iterator<String> iterator = table.iterator(); iterator.hasNext();) {
							String str = (String) iterator.next();
							tcpConnLocalAddress.addLeaf("1.3.6.1.2.1.6.13.1.2."+str);
						}
		            	/**tcpConnLocalPort*/
		            	SNMPTreeGetNextRequest<String> tcpConnLocalPort = tcpConnEntry.addNode("1.3.6.1.2.1.6.13.1.3", true);
		            	for (Iterator<String> iterator = table.iterator(); iterator.hasNext();) {
							String str = (String) iterator.next();
							tcpConnLocalPort.addLeaf("1.3.6.1.2.1.6.13.1.3."+str);
						}
		            	/**tcpConnRemAddress*/
		            	SNMPTreeGetNextRequest<String> tcpConnRemAddress = tcpConnEntry.addNode("1.3.6.1.2.1.6.13.1.4", true);
		            	for (Iterator<String> iterator = table.iterator(); iterator.hasNext();) {
							String str = (String) iterator.next();
							tcpConnRemAddress.addLeaf("1.3.6.1.2.1.6.13.1.4."+str);
						}
		            	/**tcpConnRemPort*/
		            	SNMPTreeGetNextRequest<String> tcpConnRemPort = tcpConnEntry.addNode("1.3.6.1.2.1.6.13.1.5", true);
		            	for (Iterator<String> iterator = table.iterator(); iterator.hasNext();) {
							String str = (String) iterator.next();
							tcpConnRemPort.addLeaf("1.3.6.1.2.1.6.13.1.5."+str);
						}
	    				
	    			}
	    			
	    			tcp.addLeaf("1.3.6.1.2.1.6.14");// tcpInErrs
	    			tcp.addLeaf("1.3.6.1.2.1.6.15");// tcpOutRsts
	    		}
	    	
	    		/**
	    		 * UDP
	    		 */
	    		SNMPTreeGetNextRequest<String> udp = root.addNode("1.3.6.1.2.1.7", true);
	    		{
	    			udp.addLeaf("1.3.6.1.2.1.7.1");// udpInDatagrams
					udp.addLeaf("1.3.6.1.2.1.7.2");// udpNoPorts
					udp.addLeaf("1.3.6.1.2.1.7.3");// udpInErrors
					udp.addLeaf("1.3.6.1.2.1.7.4");// udpOutDatagrams
					SNMPTreeGetNextRequest<String> udpTable = udp.addNode("1.3.6.1.2.1.7.5", true);// udpTable
					
					SNMPTreeGetNextRequest<String> udpEntry = udpTable.addNode("1.3.6.1.2.1.7.5.1", true);
					{
				  		String cmd="netstat\n", line, oidConn = "";
		        		String ipAddress1="", portLocal="";
		        		Process p=null;
		            	ArrayList<String> lines = new ArrayList<String>();
		        		BufferedReader in2=null;
		        		ArrayList<String> table = new ArrayList<String>();
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
		        			
		        				table.add(oidConn);
							}
		        			
		                }
		        		catch (Exception e)
		                {
		                	 e.printStackTrace();
		                }
		            	
		            	/**udpLocalAddress*/
		            	SNMPTreeGetNextRequest<String> udpLocalAddress = udpEntry.addNode("1.3.6.1.2.1.7.5.1.1", true);
		            	for (Iterator<String> iterator = table.iterator(); iterator.hasNext();) {
							String str = (String) iterator.next();
							udpLocalAddress.addLeaf("1.3.6.1.2.1.7.5.1.1."+str);
						}
		            	/**udpLocalPort*/
		            	SNMPTreeGetNextRequest<String> udpLocalPort = udpEntry.addNode("1.3.6.1.2.1.7.5.1.2", true);
		            	for (Iterator<String> iterator = table.iterator(); iterator.hasNext();) {
							String str = (String) iterator.next();
							udpLocalPort.addLeaf("1.3.6.1.2.1.7.5.1.2."+str);
						}
		            	
					}//udpEntry
					
	    		}

				/**
	    		 * HOST-RESOURSES-MIB hrSWInstalled 1.3.6.1.2.1.25.6
	    		 */
				SNMPTreeGetNextRequest<String> hostResources = root.addNode("1.3.6.1.2.1.25", true);
	    		{
	    			// hrSWInstalled 1.3.6.1.2.1.25.6
	    			SNMPTreeGetNextRequest<String> hrSWInstalled = hostResources.addNode("1.3.6.1.2.1.25.6", true);
	    			{
	    				// hrSWInstalledTable 1.3.6.1.2.1.25.6.3
	    				SNMPTreeGetNextRequest<String> hrSWInstalledTable = hrSWInstalled.addNode("1.3.6.1.2.1.25.6.3", true);
		    			{
		    				// hrSWInstalledEntry 1.3.6.1.2.1.25.6.3.1
		    				SNMPTreeGetNextRequest<String> hrSWInstalledEntry = hrSWInstalledTable.addNode("1.3.6.1.2.1.25.6.3.1", true);
			    			{
			    				PackageManager pm = contAct.getPackageManager();
			                	ArrayList<ApplicationInfo> l = (ArrayList<ApplicationInfo>) pm.getInstalledApplications(ApplicationInfo.FLAG_INSTALLED);
			                	int nSW = 0, nTSW = l.size();
			            	   
			    				// hrSWInstalledIndex 1.3.6.1.2.1.25.6.3.1.1
			    				SNMPTreeGetNextRequest<String> hrSWInstalledIndex = hrSWInstalledEntry.addNode("1.3.6.1.2.1.25.6.3.1.1", true);
				    			{
				    				for(nSW = 1 ; nSW <= nTSW ; nSW++){
				    					hrSWInstalledIndex.addLeaf("1.3.6.1.2.1.25.6.3.1.1."+nSW);
				    				}
				    			}
				    			
				    			// hrSWInstalledName 1.3.6.1.2.1.25.6.3.1.2
			    				SNMPTreeGetNextRequest<String> hrSWInstalledName = hrSWInstalledEntry.addNode("1.3.6.1.2.1.25.6.3.1.2", true);
				    			{
				    				for(nSW = 1 ; nSW <= nTSW ; nSW++){
				    					hrSWInstalledName.addLeaf("1.3.6.1.2.1.25.6.3.1.2."+nSW);
				    				}
				    			}
				    			
				    			// hrSWInstalledID 1.3.6.1.2.1.25.6.3.1.3
			    				SNMPTreeGetNextRequest<String> hrSWInstalledID = hrSWInstalledEntry.addNode("1.3.6.1.2.1.25.6.3.1.3", true);
				    			{
				    				for(nSW = 1 ; nSW <= nTSW ; nSW++){
				    					hrSWInstalledID.addLeaf("1.3.6.1.2.1.25.6.3.1.3."+nSW);
				    				}
				    			}
				    			
				    			// hrSWInstalledType 1.3.6.1.2.1.25.6.3.1.4
			    				SNMPTreeGetNextRequest<String> hrSWInstalledType = hrSWInstalledEntry.addNode("1.3.6.1.2.1.25.6.3.1.4", true);
				    			{
				    				for(nSW = 1 ; nSW <= nTSW ; nSW++){
				    					hrSWInstalledType.addLeaf("1.3.6.1.2.1.25.6.3.1.4."+nSW);
				    				}
				    			}
				    			
				    			// hrSWInstalledDate 1.3.6.1.2.1.25.6.3.1.5
			    				SNMPTreeGetNextRequest<String> hrSWInstalledDate = hrSWInstalledEntry.addNode("1.3.6.1.2.1.25.6.3.1.5", true);
				    			{
				    				for(nSW = 1 ; nSW <= nTSW ; nSW++){
				    					hrSWInstalledDate.addLeaf("1.3.6.1.2.1.25.6.3.1.5."+nSW);
				    				}
				    			}
			    			}
		    			}		    				
	    				
	    			}
	    		}
	    		             
	    		root.addLeaf("1.3.6.1.2.1.26");

	    	} // root
	    	
	    	for (SNMPTreeGetNextRequest<String> node : root) {
                //String indent = createIndent(node.getLevel());
                if(node.nNext == null){
                	SNMPTreeGetNextRequest<String> padre = node.parent;
                	if(padre != null){
	                    if(padre.nNext == null && padre.parent != null){
	                    	while( padre != null && padre.nNext == null){
	                    		padre = padre.parent;
	                    	}
	                    	if(padre != null){	          
	                    		node.nNext = padre.nNext;
	                    	}
	                    }else{
	                    	node.nNext = padre.nNext;	                    	
	                    }
                	}
                }
                
	    	}
	    	
	    	return root;	    	
	    	
	    }
	    
		private static String createIndent(int depth) {
	        StringBuilder sb = new StringBuilder();
	        for (int i = 0; i < depth; i++) {
	                sb.append("_|");
	        }
	        return sb.toString();
		}
	    
}
