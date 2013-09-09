package ucv.tesis.tesisandroid;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class SNMPTreeGetNextRequest<T> implements Iterable<SNMPTreeGetNextRequest<T>> {

	
	    T data;
	    SNMPTreeGetNextRequest<T> parent;
	    SNMPTreeGetNextRequest<T> nNext;
	    List<SNMPTreeGetNextRequest<T>> children;
	    private List<SNMPTreeGetNextRequest<T>> elementsIndex;
	    
	    public SNMPTreeGetNextRequest(T data) {
	        this.data = data;
	        this.children = new LinkedList<SNMPTreeGetNextRequest<T>>();
	    }

	    public SNMPTreeGetNextRequest<T> addNode(T child) {
	    	SNMPTreeGetNextRequest<T> childNode = new SNMPTreeGetNextRequest<T>(child);
	        childNode.parent = this;
	        childNode.nNext = null;
	        if(this.children.size()>0)
	        	this.children.get(this.children.size()-1).nNext = childNode;
	        this.children.add(childNode);
	        return childNode;
	    }
	    
	    public void addLeaf(T child) {
	    	SNMPTreeGetNextRequest<T> childNode = new SNMPTreeGetNextRequest<T>(child);
	        childNode.parent = this;
	        childNode.nNext = null;
	        if(this.children.size()>0)
	        	this.children.get(this.children.size()-1).nNext = childNode;
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
	
	    private void registerChildForSearch(SNMPTreeGetNextRequest<T> node) {
	            elementsIndex.add(node);
	            if (parent != null)
	                    parent.registerChildForSearch(node);
	    }
	
	    public SNMPTreeGetNextRequest<T> findTreeNode(Comparable<T> cmp) {
	            for (SNMPTreeGetNextRequest<T> element : this.elementsIndex) {
	                    T elData = element.data;
	                    if (cmp.compareTo(elData) == 0)
	                            return element;
	            }
	
	            return null;
	    }
	
	    @Override
	    public String toString() {
	            return data != null ? data.toString() : "[data null]";
	    }
	
	    @Override
	    public Iterator<SNMPTreeGetNextRequest<T>> iterator() {
	    		SNMPTreeGetNextRequestIter<T> iter = new SNMPTreeGetNextRequestIter<T>(this);
	            return iter;
	    }
	    
	    public static SNMPTreeGetNextRequest<String> SNMPTreeLoad()
	    {
	    	SNMPTreeGetNextRequest<String> root = new SNMPTreeGetNextRequest<String>("1.3.6.1.2.1");
	    	{
	    		/**
	    		 * SYSTEM
	    		 */	    		
	    		SNMPTreeGetNextRequest<String> system = root.addNode("1.3.6.1.2.1.1");
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
	    		SNMPTreeGetNextRequest<String> interfaces = root.addNode("1.3.6.1.2.1.2");
	    		interfaces.addLeaf("1.3.6.1.2.1.2.1"); //ifNumber
	    		SNMPTreeGetNextRequest<String> iftable = interfaces.addNode("1.3.6.1.2.1.2.2"); //ifTable
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
                    
	    			SNMPTreeGetNextRequest<String> ifEntry = iftable.addNode("1.3.6.1.2.1.2.2.1"); // ifEntry
	    			{
	    				/**ifIndex*/
						SNMPTreeGetNextRequest<String> ifIndex = ifEntry.addNode("1.3.6.1.2.1.2.2.1.1");
	    				for(int iface = 1 ; iface <= nIf ; iface++ ){
							ifIndex.addLeaf("1.3.6.1.2.1.2.2.1.1."+iface);
						}
						/**ifDescr*/
						SNMPTreeGetNextRequest<String> ifDescr = ifEntry.addNode("1.3.6.1.2.1.2.2.1.2");
	    				for(int iface = 1 ; iface <= nIf ; iface++ ){
							ifDescr.addLeaf("1.3.6.1.2.1.2.2.1.2."+iface);
						}
	    				/**ifType*/
	    				SNMPTreeGetNextRequest<String> ifType = ifEntry.addNode("1.3.6.1.2.1.2.2.1.3");
	    				for(int iface = 1 ; iface <= nIf ; iface++ ){
	    					ifType.addLeaf("1.3.6.1.2.1.2.2.1.3."+iface);
						}
	    				/**ifMtu*/
	    				SNMPTreeGetNextRequest<String> ifMtu = ifEntry.addNode("1.3.6.1.2.1.2.2.1.4");
	    				for(int iface = 1 ; iface <= nIf ; iface++ ){
	    					ifMtu.addLeaf("1.3.6.1.2.1.2.2.1.4."+iface);
						}
	    				/**ifSpeed*/
	    				SNMPTreeGetNextRequest<String> ifSpeed = ifEntry.addNode("1.3.6.1.2.1.2.2.1.5");
	    				for(int iface = 1 ; iface <= nIf ; iface++ ){
	    					ifSpeed.addLeaf("1.3.6.1.2.1.2.2.1.5."+iface);
	    				} 
	    				/**ifPhysAddress*/
	    				SNMPTreeGetNextRequest<String> ifPhysAddress = ifEntry.addNode("1.3.6.1.2.1.2.2.1.6");
	    				for(int iface = 1 ; iface <= nIf ; iface++ ){
	    					ifPhysAddress.addLeaf("1.3.6.1.2.1.2.2.1.6."+iface);
	    				}
	    				/**ifAdminStatus*/
	    				SNMPTreeGetNextRequest<String> ifAdminStatus = ifEntry.addNode("1.3.6.1.2.1.2.2.1.7");
	    				for(int iface = 1 ; iface <= nIf ; iface++ ){
	    					ifAdminStatus.addLeaf("1.3.6.1.2.1.2.2.1.7."+iface);
	    				} 
	    				/**ifOperStatus*/
	    				SNMPTreeGetNextRequest<String> ifOperStatus = ifEntry.addNode("1.3.6.1.2.1.2.2.1.8");
	    				for(int iface = 1 ; iface <= nIf ; iface++ ){
	    					ifOperStatus.addLeaf("1.3.6.1.2.1.2.2.1.8."+iface);
	    				} 
	    				/**ifLastChange*/
	    				SNMPTreeGetNextRequest<String> ifLastChange = ifEntry.addNode("1.3.6.1.2.1.2.2.1.9");
	    				for(int iface = 1 ; iface <= nIf ; iface++ ){
	    					ifLastChange.addLeaf("1.3.6.1.2.1.2.2.1.9."+iface);
	    				}
	    				/**ifInOctets*/
	    				SNMPTreeGetNextRequest<String> ifInOctets = ifEntry.addNode("1.3.6.1.2.1.2.2.1.10");
	    				for(int iface = 1 ; iface <= nIf ; iface++ ){
	    					ifInOctets.addLeaf("1.3.6.1.2.1.2.2.1.10."+iface);
	    				}
	    				/**ifInUcastPkts*/
	    				SNMPTreeGetNextRequest<String> ifInUcastPkts = ifEntry.addNode("1.3.6.1.2.1.2.2.1.11");
	    				for(int iface = 1 ; iface <= nIf ; iface++ ){
	    					ifInUcastPkts.addLeaf("1.3.6.1.2.1.2.2.1.11."+iface);
	    				}
	    				/**ifInNUcastPkts*/
	    				SNMPTreeGetNextRequest<String> ifInNUcastPkts = ifEntry.addNode("1.3.6.1.2.1.2.2.1.12");
	    				for(int iface = 1 ; iface <= nIf ; iface++ ){
	    					ifInNUcastPkts.addLeaf("1.3.6.1.2.1.2.2.1.12."+iface);
	    				} 
	    				/**ifInDiscards*/
	    				SNMPTreeGetNextRequest<String> ifInDiscards = ifEntry.addNode("1.3.6.1.2.1.2.2.1.13");
	    				for(int iface = 1 ; iface <= nIf ; iface++ ){
	    					ifInDiscards.addLeaf("1.3.6.1.2.1.2.2.1.13."+iface);
	    				}
	    				/**ifInErrors*/
	    				SNMPTreeGetNextRequest<String> ifInErrors = ifEntry.addNode("1.3.6.1.2.1.2.2.1.14");
	    				for(int iface = 1 ; iface <= nIf ; iface++ ){
	    					ifInErrors.addLeaf("1.3.6.1.2.1.2.2.1.14."+iface);
	    				}
	    				/**ifInUnknownProtos*/
	    				SNMPTreeGetNextRequest<String> ifInUnknownProtos = ifEntry.addNode("1.3.6.1.2.1.2.2.1.15");
	    				for(int iface = 1 ; iface <= nIf ; iface++ ){
	    					ifInUnknownProtos.addLeaf("1.3.6.1.2.1.2.2.1.15."+iface);
	    				}
	    				/**ifOutUcastPkts*/
	    				SNMPTreeGetNextRequest<String> ifOutUcastPkts = ifEntry.addNode("1.3.6.1.2.1.2.2.1.17");
	    				for(int iface = 1 ; iface <= nIf ; iface++ ){
	    					ifOutUcastPkts.addLeaf("1.3.6.1.2.1.2.2.1.17."+iface);
	    				}
	    				/**ifOutNUcastPkts*/
	    				SNMPTreeGetNextRequest<String> ifOutNUcastPkts = ifEntry.addNode("1.3.6.1.2.1.2.2.1.18");
	    				for(int iface = 1 ; iface <= nIf ; iface++ ){
	    					ifOutNUcastPkts.addLeaf("1.3.6.1.2.1.2.2.1.18."+iface);
	    				}
	    				/**ifOutDiscards*/
	    				SNMPTreeGetNextRequest<String> ifOutDiscards = ifEntry.addNode("1.3.6.1.2.1.2.2.1.19");
	    				for(int iface = 1 ; iface <= nIf ; iface++ ){
	    					ifOutDiscards.addLeaf("1.3.6.1.2.1.2.2.1.19."+iface);
	    				} 
	    				/**ifOutErrors*/
	    				SNMPTreeGetNextRequest<String> ifOutErrors = ifEntry.addNode("1.3.6.1.2.1.2.2.1.20");
	    				for(int iface = 1 ; iface <= nIf ; iface++ ){
	    					ifOutErrors.addLeaf("1.3.6.1.2.1.2.2.1.20."+iface);
	    				} 
	    				/**ifOutQLen*/
	    				SNMPTreeGetNextRequest<String> ifOutQLen = ifEntry.addNode("1.3.6.1.2.1.2.2.1.21");
	    				for(int iface = 1 ; iface <= nIf ; iface++ ){
	    					ifOutQLen.addLeaf("1.3.6.1.2.1.2.2.1.21."+iface);
	    				}
	    				/**FALTA IFSPECIFIC*/
	    				 
	    			}// ifEntry	    			
	    			
	    		}//ifTable
	    		
	    		/**
	    		 * AT
	    		 */
	    		SNMPTreeGetNextRequest<String> at = root.addNode("1.3.6.1.2.1.3");
	    		SNMPTreeGetNextRequest<String> atTable = at.addNode("1.3.6.1.2.1.3.1"); //atTable
	    		{
	    			SNMPTreeGetNextRequest<String> atEntry = atTable.addNode("1.3.6.1.2.1.3.1.1"); //atEntry
	    			{
	    				atEntry.addLeaf("1.3.6.1.2.1.3.1.1.1"); //atIfIndex
	    				atEntry.addLeaf("1.3.6.1.2.1.3.1.1.2"); //atPhysAddress
	    				atEntry.addLeaf("1.3.6.1.2.1.3.1.1.3"); //atNetAddress
	    			}
	    		}
	    		
	    		/**
	    		 * IP
	    		 */
	    		SNMPTreeGetNextRequest<String> ip = root.addNode("1.3.6.1.2.1.4");
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
	    			
					SNMPTreeGetNextRequest<String> ipAddrTable = ip.addNode("1.3.6.1.2.1.4.20"); // ipAddrTable
					
					SNMPTreeGetNextRequest<String> ipAddrEntry = ipAddrTable.addNode("1.3.6.1.2.1.4.20.1"); //ipAddrEntry
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
		            	SNMPTreeGetNextRequest<String> ipAdEntAddr = ipAddrEntry.addNode("1.3.6.1.2.1.4.20.1.1");
		            	for (Iterator<String> iterator = table.iterator(); iterator.hasNext();) {
							String str = (String) iterator.next();
							ipAdEntAddr.addLeaf("1.3.6.1.2.1.4.20.1.1."+str);
						}
		            	/** ipAdEntIfIndex*/
		            	SNMPTreeGetNextRequest<String> ipAdEntIfIndex = ipAddrEntry.addNode("1.3.6.1.2.1.4.20.1.2");
		            	for (Iterator<String> iterator = table.iterator(); iterator.hasNext();) {
							String str = (String) iterator.next();
							ipAdEntIfIndex.addLeaf("1.3.6.1.2.1.4.20.1.2."+str);
						}
		            	/** ipAdEntNetMask*/
		            	SNMPTreeGetNextRequest<String> ipAdEntNetMask = ipAddrEntry.addNode("1.3.6.1.2.1.4.20.1.3");
		            	for (Iterator<String> iterator = table.iterator(); iterator.hasNext();) {
							String str = (String) iterator.next();
							ipAdEntNetMask.addLeaf("1.3.6.1.2.1.4.20.1.3."+str);
						}
		            	/** ipAdEntBcastAddr*/
		            	SNMPTreeGetNextRequest<String> ipAdEntBcastAddr = ipAddrEntry.addNode("1.3.6.1.2.1.4.20.1.4");
		            	for (Iterator<String> iterator = table.iterator(); iterator.hasNext();) {
							String str = (String) iterator.next();
							ipAdEntBcastAddr.addLeaf("1.3.6.1.2.1.4.20.1.4."+str);
						}
		            	/** ipAdEntReasmMaxSize*/
		            	SNMPTreeGetNextRequest<String> ipAdEntReasmMaxSize = ipAddrEntry.addNode("1.3.6.1.2.1.4.20.1.5");
		            	for (Iterator<String> iterator = table.iterator(); iterator.hasNext();) {
							String str = (String) iterator.next();
							ipAdEntReasmMaxSize.addLeaf("1.3.6.1.2.1.4.20.1.5."+str);
						}
	            	
					}
					
					SNMPTreeGetNextRequest<String> ipRouteTable = ip.addNode("1.3.6.1.2.1.4.21"); // ipRouteTable
					{
						SNMPTreeGetNextRequest<String> ipRouteEntry = ipRouteTable.addNode("1.3.6.1.2.1.4.21.1"); // ipRouteEntry
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
			            	SNMPTreeGetNextRequest<String> ipRouteDest = ipRouteEntry.addNode("1.3.6.1.2.1.4.21.1.1");
			            	for (Iterator<String> iterator = table.iterator(); iterator.hasNext();) {
								String str = (String) iterator.next();
								ipRouteDest.addLeaf("1.3.6.1.2.1.4.21.1.1."+str);
							}
			            	/**ipRouteIfIndex*/
			            	SNMPTreeGetNextRequest<String> ipRouteIfIndex = ipRouteEntry.addNode("1.3.6.1.2.1.4.21.1.2");
			            	for (Iterator<String> iterator = table.iterator(); iterator.hasNext();) {
								String str = (String) iterator.next();
								ipRouteIfIndex.addLeaf("1.3.6.1.2.1.4.21.1.2."+str);
							}
			            	/**ipRouteMetric1*/
			            	SNMPTreeGetNextRequest<String> ipRouteMetric1 = ipRouteEntry.addNode("1.3.6.1.2.1.4.21.1.3");
			            	for (Iterator<String> iterator = table.iterator(); iterator.hasNext();) {
								String str = (String) iterator.next();
								ipRouteMetric1.addLeaf("1.3.6.1.2.1.4.21.1.3."+str);
							}
			            	/**ipRouteNextHop*/
			            	SNMPTreeGetNextRequest<String> ipRouteNextHop = ipRouteEntry.addNode("1.3.6.1.2.1.4.21.1.4");
			            	for (Iterator<String> iterator = table.iterator(); iterator.hasNext();) {
								String str = (String) iterator.next();
								ipRouteNextHop.addLeaf("1.3.6.1.2.1.4.21.1.4."+str);
							}
			            	/**ipRouteMask*/
			            	SNMPTreeGetNextRequest<String> ipRouteMask = ipRouteEntry.addNode("1.3.6.1.2.1.4.21.1.5");
			            	for (Iterator<String> iterator = table.iterator(); iterator.hasNext();) {
								String str = (String) iterator.next();
								ipRouteMask.addLeaf("1.3.6.1.2.1.4.21.1.5."+str);
							}
						}
					}
					
					SNMPTreeGetNextRequest<String> ipNetToMediaTable = ip.addNode("1.3.6.1.2.1.4.22"); // ipNetToMediaTable
					
					SNMPTreeGetNextRequest<String> ipNetToMediaEntry = ipNetToMediaTable.addNode("1.3.6.1.2.1.4.22.1"); // ipNetToMediaTable
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
		            	SNMPTreeGetNextRequest<String> ipNetToMediaIfIndex = ipNetToMediaEntry.addNode("1.3.6.1.2.1.4.22.1.1");
		            	for (Iterator<String> iterator = table.iterator(); iterator.hasNext();) {
							String str = (String) iterator.next();
							ipNetToMediaIfIndex.addLeaf("1.3.6.1.2.1.4.22.1.1."+str);
						}
		            	/**ipNetToMediaPhysAddress*/
		            	SNMPTreeGetNextRequest<String> ipNetToMediaPhysAddress = ipNetToMediaEntry.addNode("1.3.6.1.2.1.4.22.1.2");
		            	for (Iterator<String> iterator = table.iterator(); iterator.hasNext();) {
							String str = (String) iterator.next();
							ipNetToMediaPhysAddress.addLeaf("1.3.6.1.2.1.4.22.1.2."+str);
						}
		            	/**ipNetToMediaNetAddress*/
		            	SNMPTreeGetNextRequest<String> ipNetToMediaNetAddress = ipNetToMediaEntry.addNode("1.3.6.1.2.1.4.22.1.3");
		            	for (Iterator<String> iterator = table.iterator(); iterator.hasNext();) {
							String str = (String) iterator.next();
							ipNetToMediaNetAddress.addLeaf("1.3.6.1.2.1.4.22.1.3."+str);
						}
		            	/**ipNetToMediaType*/
		            	SNMPTreeGetNextRequest<String> ipNetToMediaType = ipNetToMediaEntry.addNode("1.3.6.1.2.1.4.22.1.4");
		            	for (Iterator<String> iterator = table.iterator(); iterator.hasNext();) {
							String str = (String) iterator.next();
							ipNetToMediaType.addLeaf("1.3.6.1.2.1.4.22.1.4."+str);
						}
						
					}
					
	    			ip.addLeaf("1.3.6.1.2.1.4.23");// ipRoutingDiscards
	    			
	    		} // ip
	    		
	    		/**
	    		 * ICMP
	    		 */
	    		SNMPTreeGetNextRequest<String> icmp = root.addNode("1.3.6.1.2.1.5");
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
	    	
	    	
	    	} // root
	    	
	    	return root;	    	
	    	
	    }
	    
}
