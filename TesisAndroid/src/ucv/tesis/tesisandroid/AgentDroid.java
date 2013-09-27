package ucv.tesis.tesisandroid;


import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;



import snmp.SNMPBadValueException;
import snmp.SNMPIPAddress;
import snmp.SNMPObjectIdentifier;
import snmp.SNMPTimeTicks;
import snmp.SNMPTrapSenderInterface;
import snmp.SNMPv1TrapPDU;
import ucv.tesis.tesisandroid.DBOIDHelper.ObjIdent;
import ucv.tesis.tesisandroid.R;
import ucv.tesis.tesisandroid.AgentSNMP.LocalBinder;


import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.*;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.*;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.ToggleButton;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

public class AgentDroid extends Activity {

	TabHost tabHost;
	Intent intService = null;
	AgentSNMP myService;
    boolean isBound = false;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agent_droid);
		this.init();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.agent_droid, menu);
		return true;
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		if (isBound) unbindService(myConnection);
	}
	
	private ServiceConnection myConnection = new ServiceConnection() {

	    public void onServiceConnected(ComponentName className, IBinder service) {
	        LocalBinder binder = (LocalBinder) service;
	        myService = binder.getService();
	        isBound = true;
	        Toast.makeText(getApplicationContext(),
                    "AgentDroid connected to AGENT  ", Toast.LENGTH_LONG).show();
	    }
	    
	    public void onServiceDisconnected(ComponentName arg0) {
	        isBound = false;
	        myService = null;
	        Toast.makeText(getApplicationContext(),
                    "AgentDroid Disconnected to AGENT  ", Toast.LENGTH_LONG).show();
	    }
	    
	};
	
	
	@SuppressLint("NewApi")
	protected void init(){
		
		tabHost = (TabHost) findViewById(R.id.tabhost);
        // invocamos el setup de TabHost
        tabHost.setup();
        // Creamos un TabSpec por cada tab
        TabSpec specs = tabHost.newTabSpec("tag1");
        specs.setContent(R.id.tab1);
        specs.setIndicator("Config Agent");
        tabHost.addTab(specs);
        
        specs = tabHost.newTabSpec("tag2");
        specs.setContent(R.id.tab2);
        specs.setIndicator("Traps v1");
        tabHost.addTab(specs);
        
        specs = tabHost.newTabSpec("tag3");
        specs.setContent(R.id.tab3);
        specs.setIndicator("Traps v2c");
        tabHost.addTab(specs);
        
        specs = tabHost.newTabSpec("tag4");
        specs.setContent(R.id.tab4);
        specs.setIndicator("System Group");
        tabHost.addTab(specs);

        Spinner comboBox = (Spinner) findViewById(R.id.tab2Spinner1);
        ArrayAdapter<?> adapterCombo =  ArrayAdapter.createFromResource(this, R.array.typetraps, 
        								android.R.layout.simple_spinner_item);
        adapterCombo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        comboBox.setAdapter(adapterCombo);
        comboBox.setOnItemSelectedListener(new OnItemSelectedListener(){
        	
        	public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int pos, long id){
        		TextView label = (TextView) findViewById(R.id.tab2textView5);
        		EditText text = (EditText) findViewById(R.id.tab2editText4);
        		
        		if(parentView.getItemAtPosition(pos).toString().equalsIgnoreCase("enterpriseSpecific")){
        			label.setVisibility(View.VISIBLE);
        			text.setVisibility(View.VISIBLE);
        		}else{
        			label.setVisibility(View.GONE);
        			text.setVisibility(View.GONE);
        		}	        		
        	}
        	public void onNothingSelected(AdapterView<?> parentView){;}
        }); 
        
        
        Spinner tab3ComboBoxCommand = (Spinner) findViewById(R.id.tab3ComboBoxCommand);
        adapterCombo =  ArrayAdapter.createFromResource(this, R.array.selectOpcionStrings, 
        								android.R.layout.simple_spinner_item);
        adapterCombo.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        tab3ComboBoxCommand.setAdapter(adapterCombo);
        tab3ComboBoxCommand.setOnItemSelectedListener(new OnItemSelectedListener(){
        	
        	public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int pos, long id){
        		TextView textTrapType = (TextView) findViewById(R.id.tab3TextTrapType);
        		Spinner comboBoxTrapType = (Spinner) findViewById(R.id.tab3ComboBoxTrapType);
        		TextView tab3TextEnterprise = (TextView) findViewById(R.id.tab3TextEnterprise);
        		EditText tab3EditEnterpriseOID = (EditText) findViewById(R.id.tab3EditEnterpriseOID);
        		TextView tab3TextTrapOID = (TextView) findViewById(R.id.tab3TextTrapOID);
        		EditText tab3EditTrapOID = (EditText) findViewById(R.id.tab3EditTrapOID);
        		TextView tab3TextDescription = (TextView) findViewById(R.id.tab3TextDescription);
        		EditText tab3EditDescription = (EditText) findViewById(R.id.tab3EditDescription);
        		TextView tab3TextDataType = (TextView) findViewById(R.id.tab3TextDataType);
        		Spinner tab3ComboBoxDataType = (Spinner) findViewById(R.id.tab3ComboBoxDataType);
        		Button b = (Button) findViewById(R.id.tab3ButtonTrap);
        		
        		if(parentView.getItemAtPosition(pos).toString().equalsIgnoreCase("Inform")){
        			b.setText(R.string.bInform);
        			textTrapType.setVisibility(View.GONE);
        			comboBoxTrapType.setVisibility(View.GONE);
        			tab3TextEnterprise.setVisibility(View.VISIBLE);
        			tab3EditEnterpriseOID.setVisibility(View.VISIBLE);
        			tab3TextTrapOID.setVisibility(View.VISIBLE);
        			tab3EditTrapOID.setVisibility(View.VISIBLE);
        			tab3TextDescription.setVisibility(View.VISIBLE);
        			tab3EditDescription.setVisibility(View.VISIBLE);
        			tab3TextDataType.setVisibility(View.VISIBLE);
        			tab3ComboBoxDataType.setVisibility(View.VISIBLE);
        		}else if(parentView.getItemAtPosition(pos).toString().equalsIgnoreCase("Trap")){
        			b.setText(R.string.bTraps);
        			textTrapType.setVisibility(View.VISIBLE);
        			comboBoxTrapType.setVisibility(View.VISIBLE);
        			tab3TextEnterprise.setVisibility(View.GONE);
        			tab3EditEnterpriseOID.setVisibility(View.GONE);
        			tab3TextTrapOID.setVisibility(View.GONE);
        			tab3EditTrapOID.setVisibility(View.GONE);
        			tab3TextDescription.setVisibility(View.GONE);
        			tab3EditDescription.setVisibility(View.GONE);
        			tab3TextDataType.setVisibility(View.GONE);
        			tab3ComboBoxDataType.setVisibility(View.GONE);
        		}	        		
        	}
        	public void onNothingSelected(AdapterView<?> parentView){;}
        });
        
        Spinner tab3ComboBoxTrapType = (Spinner) findViewById(R.id.tab3ComboBoxTrapType);
        adapterCombo =  ArrayAdapter.createFromResource(this, R.array.typetrapsv2c, 
        								android.R.layout.simple_spinner_item);
        adapterCombo.setDropDownViewResource(android.R.layout.simple_list_item_activated_1);
        tab3ComboBoxTrapType.setAdapter(adapterCombo);
        tab3ComboBoxTrapType.setOnItemSelectedListener(new OnItemSelectedListener(){
        	
        	public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int pos, long id){
        		TextView tab3TextEnterprise = (TextView) findViewById(R.id.tab3TextEnterprise);
        		EditText tab3EditEnterpriseOID = (EditText) findViewById(R.id.tab3EditEnterpriseOID);
        		TextView tab3TextTrapOID = (TextView) findViewById(R.id.tab3TextTrapOID);
        		EditText tab3EditTrapOID = (EditText) findViewById(R.id.tab3EditTrapOID);
        		TextView tab3TextDescription = (TextView) findViewById(R.id.tab3TextDescription);
        		EditText tab3EditDescription = (EditText) findViewById(R.id.tab3EditDescription);
        		TextView tab3TextDataType = (TextView) findViewById(R.id.tab3TextDataType);
        		Spinner tab3ComboBoxDataType = (Spinner) findViewById(R.id.tab3ComboBoxDataType);
        		
        		if(parentView.getItemAtPosition(pos).toString().equalsIgnoreCase("Other")){
        			tab3TextEnterprise.setVisibility(View.VISIBLE);
        			tab3EditEnterpriseOID.setVisibility(View.VISIBLE);
        			tab3TextTrapOID.setVisibility(View.VISIBLE);
        			tab3EditTrapOID.setVisibility(View.VISIBLE);
        			tab3TextDescription.setVisibility(View.VISIBLE);
        			tab3EditDescription.setVisibility(View.VISIBLE);
        			tab3TextDataType.setVisibility(View.VISIBLE);
        			tab3ComboBoxDataType.setVisibility(View.VISIBLE);
        		}else{
        			tab3TextEnterprise.setVisibility(View.GONE);
        			tab3EditEnterpriseOID.setVisibility(View.GONE);
        			tab3TextTrapOID.setVisibility(View.GONE);
        			tab3EditTrapOID.setVisibility(View.GONE);
        			tab3TextDescription.setVisibility(View.GONE);
        			tab3EditDescription.setVisibility(View.GONE);
        			tab3TextDataType.setVisibility(View.GONE);
        			tab3ComboBoxDataType.setVisibility(View.GONE);
        		}		        		
        	}
        	public void onNothingSelected(AdapterView<?> parentView){;}
        });
        
        Spinner tab3ComboBoxDataType = (Spinner) findViewById(R.id.tab3ComboBoxDataType);
        adapterCombo =  ArrayAdapter.createFromResource(this, R.array.datatype, 
        								android.R.layout.simple_spinner_item);
        adapterCombo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tab3ComboBoxDataType.setAdapter(adapterCombo);
        tab3ComboBoxDataType.setOnItemSelectedListener(new OnItemSelectedListener(){
        	
        	public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int pos, long id){
        			        		
        	}
        	public void onNothingSelected(AdapterView<?> parentView){;}
        }); 
        
        getTabHost().setOnTabChangedListener(new OnTabChangeListener() {

        	@Override
        	public void onTabChanged(String tabId) {

        	int i = getTabHost().getCurrentTab();
        	
        	if (i == 3) { // Si cambio al TAB4 entonces refresca...
    	    	try {
    	    		DBOIDHelper database = new DBOIDHelper(getBaseContext());
      	    		TextView txtChanged = (TextView) findViewById(R.id.tab4textView11);
    	    		ObjIdent oid = null;
    	    		oid = database.getOID("1.3.6.1.2.1.1.1.0");
    	    		if(oid != null){
    	    			txtChanged.setText(oid.getValue());
    	    		}
    	    		
    	    		oid = database.getOID("1.3.6.1.2.1.1.2.0");
    	    		txtChanged = (TextView) findViewById(R.id.tab4textView21);
    	    		if(oid != null){
    	    			txtChanged.setText(oid.getValue());
    	    		}
    	    		//  getOID("1.3.6.1.2.1.1.3.0")
    	    	    
    	    	    EditText editTextChanged;
    	    	    editTextChanged = (EditText) findViewById(R.id.tab4editText1);
    	    	    oid = database.getOID("1.3.6.1.2.1.1.4.0");
    	    	    if(oid != null){
    	    	    	editTextChanged.setText(oid.getValue());
    	    		}
    	    	    
    	    	    editTextChanged = (EditText) findViewById(R.id.tab4editText2);
    	    	    oid = database.getOID("1.3.6.1.2.1.1.5.0");
    	    	    if(oid != null){
    	    	    	editTextChanged.setText(oid.getValue());
    	    		}
    	    	    
    	    	    editTextChanged = (EditText) findViewById(R.id.tab4editText3);
    	    	    oid = database.getOID("1.3.6.1.2.1.1.6.0");
    	    	    if(oid != null){
    	    	    	editTextChanged.setText(oid.getValue());
    	    		}
    	    	    //  getOID("1.3.6.1.2.1.1.7.0")
    	    	    oid = database.getOID("1.3.6.1.2.1.1.7.0");
    	    	    int entero=0;
    	    	    String bin = "";
    	    	    if(oid != null){
    	    	    	entero = Integer.parseInt(oid.getValue());
    	    		}
    	    	    while(entero > 0){
    	    	    	bin += "" + (entero % 2); 
    	    	    	entero = entero / 2 ;
    	    	    }
    	    	    
    	    	    int tam = bin.length();
    	    	    CheckBox cb = null;
    	    	    
    	    	    cb = (CheckBox) findViewById(R.id.checkBox1);
    	    	    if (tam >= 1 && bin.charAt(0) == '1' )
    	    	    	cb.setChecked(true);
    	    	    else
    	    	    	cb.setChecked(false);
    	    	    	
    	    	    
    	    	    cb = (CheckBox) findViewById(R.id.checkBox2);
    	    	    if (tam >= 2 && bin.charAt(1) == '1' )
    	    	    	cb.setChecked(true);
    	    	    else
    	    	    	cb.setChecked(false);
    	    	    	
    	    	    
    	    	    cb = (CheckBox) findViewById(R.id.checkBox3);
    	    	    if (tam >= 3 && bin.charAt(2) == '1' )
    	    	    	cb.setChecked(true);
    	    	    else
    	    	    	cb.setChecked(false);
    	    	    	
    	    	    
    	    	    cb = (CheckBox) findViewById(R.id.checkBox4);
    	    	    if (tam >= 4 && bin.charAt(3) == '1' )
    	    	    	cb.setChecked(true);
    	    	    else
    	    	    	cb.setChecked(false);
    	    	    	
    	    	    
    	    	    cb = (CheckBox) findViewById(R.id.checkBox5);
    	    	    if (tam >= 7 && bin.charAt(6) == '1' )
    	    	    	cb.setChecked(true);
    	    	    else
    	    	    	cb.setChecked(false);
    	    	    	
    	    	    oid = database.getOID("1.3.6.1.2.1.1.3.0");
    	    		txtChanged = (TextView) findViewById(R.id.tab4textView31);
    		    	BigInteger timeInit = new BigInteger(oid.getValue());
    		    	Long t = System.currentTimeMillis();
    		    	BigInteger timeNow = new BigInteger(t.toString());    		    	
    		    	
    		    	if(timeInit.compareTo(BigInteger.ONE) == 1){
    					
    					long milliseconds = timeNow.subtract(timeInit).longValue();
    					
					    long seconds = milliseconds / 1000;
					    milliseconds  %= 1000;
					    long minutes = seconds / 60;
					    seconds %= 60;
					    long hours = minutes / 60;
					    minutes %= 60;
					    hours %= 24;
					    String h = ""+hours;
					    String m = ""+minutes;
					    String s = ""+seconds;
					    
					    txtChanged.setText(((h.length()==1)?"0"+h:h)+":"+((m.length()==1)?"0"+m:m)+":"+((s.length()==1)?"0"+s:s)+"."+milliseconds);
    					
    				}else{
    					txtChanged.setText("0 - (00:00:00.00)");
    				}
    				
    			    database.cleanup();
    			    
    		    } catch (Exception e) {
    				e.printStackTrace();
    			}
    	    }// if

        	  }
        	});
        
        
        Button btn1 = (Button) findViewById(R.id.tab2button1); 
        
        btn1.setOnClickListener(new OnClickListener(){         
                                                               
        	@Override                                          
        	public void onClick(View v) { 
        		
        		String[] arr = new String[5];
        		EditText edit = (EditText) findViewById(R.id.tab2editText1);
    		    String ipAddr  = edit.getText().toString();
    		    
    		    edit = (EditText) findViewById(R.id.tab2editText3);
    		    String community  = edit.getText().toString();
    		    
    		    edit = (EditText) findViewById(R.id.tab2editText5);
    		    String entrepriseOID  = edit.getText().toString();
    		    
    		    Spinner list = (Spinner) findViewById(R.id.tab2Spinner1);
    			int genTrap = list.getSelectedItemPosition();
    			
    			edit = (EditText) findViewById(R.id.tab2editText4);
    			String specTrap = edit.getText().toString();
    			
    			arr[0] = ipAddr;
    			arr[1] = community;
    			arr[2] = entrepriseOID;
    			arr[3] = ""+genTrap;
    			arr[4] = specTrap;
    			
    			SenderTrapBackground task = new SenderTrapBackground();
    			task.execute(arr);
	
        	}
        });
        
        Button btn2 = (Button) findViewById(R.id.tab4button1);
        
        btn2.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				try {
					DBOIDHelper database = new DBOIDHelper(getBaseContext());
					
					EditText edit = (EditText) findViewById(R.id.tab4editText1);
				    String str  = edit.getText().toString();
				    database.update("1.3.6.1.2.1.1.4.0", str);
				   
				    edit = (EditText) findViewById(R.id.tab4editText2);
				    str  = edit.getText().toString();
				    database.update("1.3.6.1.2.1.1.5.0", str);
				    
				    edit = (EditText) findViewById(R.id.tab4editText3);
				    str  = edit.getText().toString();
				    database.update("1.3.6.1.2.1.1.6.0", str);
				    
				    //2^(X-1) 
				    int sum = 0;
				    CheckBox cb = null;
				    cb = (CheckBox) findViewById(R.id.checkBox1);
				    if (cb.isChecked())
				    	sum += Math.pow(2, 1-1);
				    
				    cb = (CheckBox) findViewById(R.id.checkBox2);
				    if (cb.isChecked())
				    	sum += Math.pow(2, 2-1);
				    
				    cb = (CheckBox) findViewById(R.id.checkBox3);
				    if (cb.isChecked())
				    	sum += Math.pow(2, 3-1);
				    
				    cb = (CheckBox) findViewById(R.id.checkBox4);
				    if (cb.isChecked())
				    	sum += Math.pow(2, 4-1);
				    
				    cb = (CheckBox) findViewById(R.id.checkBox5);
				    if (cb.isChecked())
				    	sum += Math.pow(2, 7-1);
				    
				    database.update("1.3.6.1.2.1.1.7.0", ""+sum);
				    
				    database.cleanup();
				    
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					Toast toast1 = Toast.makeText(getApplicationContext(),
			                "The changes have been saved correctly", Toast.LENGTH_SHORT);
					toast1.show();
				}
			}

        });
        
        Toast toast1 = Toast.makeText(getApplicationContext(),
                "No Connected", Toast.LENGTH_SHORT);
        
        try {
        	ConnectivityManager connec = null;
            connec =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            
            if (connec != null) {

                NetworkInfo net = connec.getActiveNetworkInfo();
                
                if (net != null && net.getType() == ConnectivityManager.TYPE_WIFI){
                   	WifiManager wManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
                   	WifiInfo wCurrent = wManager.getConnectionInfo();
                   	toast1 = Toast.makeText(getApplicationContext(),
                            "AgentDroid connected to " + wCurrent.getSSID() , Toast.LENGTH_LONG);			
                }else{
        			toast1 = Toast.makeText(getApplicationContext(),
                            "AgentDroid connected to other network type", Toast.LENGTH_LONG);
            	}
    		}
            
		} catch (Exception e) {
			
			e.printStackTrace();
		}
        
        toast1.show();
        
        DBOIDHelper database = new DBOIDHelper(getBaseContext());
        ArrayList<ObjIdent> objIdens = database.getAll();
        
        for (Iterator<ObjIdent> iterator = objIdens.iterator(); iterator.hasNext();) {
			ObjIdent objIdent = (ObjIdent) iterator.next();
			System.out.println(objIdent.getName() + " => " + objIdent.getValue() );  
		}
        
        
		
		
	    /* a init(); se le agrego' el @SuppressLint("NewApi") 
	    porque para el me'todo setOnCheckedChangeListener 
	    era necesario sacar el warning del API minimun 14 */
	
	    if (Build.VERSION.SDK_INT >= 14) { 
	    	
	    	Switch ssrv = (Switch) findViewById(R.id.switchService);
	    	// saber si el servicio esta en ejecucion, con el nombre de la clase en ejecucion
	    	if(isRunning( getBaseContext() , "ucv.tesis.tesisandroid.AgentSNMP")){
	    		ssrv.setChecked(true);
	    		bindService(new Intent(AgentDroid.this, AgentSNMP.class), myConnection, Context.BIND_AUTO_CREATE);
	    	}else{
	    		ssrv.setChecked(false);
	    	}
	    	
	    	ssrv.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		    	@Override
		    	
		    	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		    		
		    		intService = new Intent(AgentDroid.this, AgentSNMP.class);
		    		EditText comRO = (EditText) findViewById(R.id.EditComRO);
            		EditText comRW = (EditText) findViewById(R.id.EditComRW);
            		EditText port = (EditText) findViewById(R.id.EditPort);
            		RadioButton snmp1 = (RadioButton) findViewById(R.id.RadioSNMPv1);
            		boolean version1 = snmp1.isChecked();
            		String str = "";
            		
            		int portSNMP = Integer.parseInt(port.getText().toString());
            		
		    		if(isChecked){
		    			if(portSNMP > 1024 && portSNMP < 65536){
		    				startService(intService);
		    				bindService(intService, myConnection, Context.BIND_AUTO_CREATE);
		    				isBound = true;
		    			
		    				DBOIDHelper database = new DBOIDHelper(getBaseContext());
		    					str = comRO.getText().toString();
			    				database.update("CommunityReadOnly", str);
			    				str = comRW.getText().toString();
			    				database.update("CommunityReadWrite", str);
			    				str = port.getText().toString();
			    				database.update("portSNMP", str);
			    				database.update("snmpVersionOne", (version1==true)?"true":"false");
		    					database.update("1.3.6.1.2.1.1.3.0", ""+System.currentTimeMillis());			
		    				database.cleanup();
		    			}else{
		    				buttonView.setChecked(false);
		    				Builder builder = new Builder(AgentDroid.this);
		    				builder.setTitle("Config Agent");
	    					builder.setMessage("Incorrect Port Number. Must be greater than 1024 and less than 65535.");
	    					builder.setNeutralButton("Ok", null);	
		    				AlertDialog alertDia = builder.create();
		    				alertDia.show();
		    				alertDia.setCancelable(false);
		    			}
		    		}else{
		    			if(isBound){
		    				unbindService(myConnection);
		    				stopService(intService);  
		    				isBound = false;
		    			}
		    		}
		    	}
	    	}); 
	    	
	    }else{
	    	
	    	ToggleButton tgBtt = (ToggleButton) findViewById(R.id.toggleButton1);
	    	if(isRunning( getBaseContext() , "ucv.tesis.tesisandroid.AgentSNMP")){
	    		tgBtt.setChecked(true);
	    		bindService(new Intent(AgentDroid.this, AgentSNMP.class), myConnection, Context.BIND_AUTO_CREATE);
	    	}else{
	    		tgBtt.setChecked(false);
	    	}
	    	tgBtt.setOnCheckedChangeListener(new OnCheckedChangeListener() {
	    		@Override
	    		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
	    			
	    			intService = new Intent(AgentDroid.this, AgentSNMP.class);
	    			EditText comRO = (EditText) findViewById(R.id.EditComRO);
            		EditText comRW = (EditText) findViewById(R.id.EditComRW);
            		EditText port = (EditText) findViewById(R.id.EditPort);
            		RadioButton snmp1 = (RadioButton) findViewById(R.id.RadioSNMPv1);
            		boolean version1 = snmp1.isChecked();
            		String str = "";
            		
            		int portSNMP = Integer.parseInt(port.getText().toString());
            		
	    			if(isChecked){
	    				if(portSNMP > 1024 && portSNMP < 65536){
		    				startService(intService);
		    				bindService(intService, myConnection, Context.BIND_AUTO_CREATE);
		    				isBound = true;
		    			
		    				DBOIDHelper database = new DBOIDHelper(getBaseContext());
		    					str = comRO.getText().toString();
			    				database.update("CommunityReadOnly", str);
			    				str = comRW.getText().toString();
			    				database.update("CommunityReadWrite", str);
			    				str = port.getText().toString();
			    				database.update("portSNMP", str);
			    				database.update("snmpVersionOne", (version1==true)?"true":"false");
		    					database.update("1.3.6.1.2.1.1.3.0", ""+System.currentTimeMillis());			
		    				database.cleanup();
		    			}else{
		    				buttonView.setChecked(false);
		    				Builder builder = new Builder(AgentDroid.this);
		    				builder.setTitle("Config Agent");
	    					builder.setMessage("Incorrect Port Number. Must be greater than 1024 and less than 65535.");
	    					builder.setNeutralButton("Ok", null);	
		    				AlertDialog alertDia = builder.create();
		    				alertDia.show();
		    				alertDia.setCancelable(false);
		    			}
		    		}else{
		    			if(isBound) {
		    				unbindService(myConnection);
		    				stopService(intService);  
		    				isBound = false;
		    			}
		    		}
	    		}
	    	}); 
	    	
	    	  
	    } // if (Build.VERSION.SDK_INT >= 14)
	    
	    EditText editTextChanged;
	    editTextChanged = (EditText) findViewById(R.id.EditComRO);
	    ObjIdent oid = database.getOID("CommunityReadOnly");
	    if(oid != null){
	    	editTextChanged.setText(oid.getValue());
		}
	    editTextChanged = (EditText) findViewById(R.id.EditComRW);
	    oid = database.getOID("CommunityReadWrite");
	    if(oid != null){
	    	editTextChanged.setText(oid.getValue());
		}
	    editTextChanged = (EditText) findViewById(R.id.EditPort);
	    oid = database.getOID("portSNMP");
	    if(oid != null){
	    	editTextChanged.setText(oid.getValue());
		}
	    RadioButton radSNMPv1 = (RadioButton) findViewById(R.id.RadioSNMPv1);
	    RadioButton radSNMPv2c = (RadioButton) findViewById(R.id.RadioSNMPv2c);
	    oid = database.getOID("snmpVersionOne");
	    if(oid != null){
	    	if(oid.getValue().equalsIgnoreCase("true")){
	    		radSNMPv1.setChecked(true);
	    		radSNMPv2c.setChecked(false);
	    	}else{
	    		radSNMPv1.setChecked(false);
	    		radSNMPv2c.setChecked(true);
	    	}
		}
	    
	} // init()

	
	private static boolean isRunning(Context context, String class_name)
	{
		try
		{
			for (RunningServiceInfo service : ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningServices(Integer.MAX_VALUE)) 
			{
				//System.out.println(service.service.getClassName());
				if (service.service.getClassName().equals(class_name))
				{
					return true;
				}
			}
		}
		catch (Exception e)
		{
		}
		return false;
	}

	public TabHost getTabHost(){
		return this.tabHost;
	}

	public class SenderTrapBackground extends AsyncTask<String[], Void, Boolean> {

		
		@Override
		protected Boolean doInBackground(String[]... arg0) {
			
			boolean send=true;
			
			try {
				
				SNMPTrapSenderInterface SNMPSendTrapV1 = new SNMPTrapSenderInterface();
				SNMPv1TrapPDU trap = null;
				String ipAddr = arg0[0][0];
				String community = arg0[0][1];
				String entrepriseOID = arg0[0][2] ;
				String gTrap = arg0[0][3];
				String sTrap = arg0[0][4];
				InetAddress hostAddress = null;
				
				int genTrap = 0;
				int specTrap = 0;
				
				try {
					hostAddress = InetAddress.getByName(ipAddr);
					genTrap = Integer.parseInt(gTrap.equalsIgnoreCase("")? "0":gTrap);
					specTrap = Integer.parseInt(sTrap.equalsIgnoreCase("")? "0":sTrap);
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
				
				if(genTrap == 5) 
					genTrap+=1;
				
				SNMPTimeTicks timestamp = new SNMPTimeTicks(3*6000);
				
				trap = new SNMPv1TrapPDU( new SNMPObjectIdentifier(entrepriseOID), new SNMPIPAddress(ipAddr), genTrap, specTrap, timestamp);
			    
				SNMPSendTrapV1.sendTrap(0, hostAddress, community, trap);
			    
				
			} catch (SocketException e) {
				e.printStackTrace();
				send=false;
			} catch (IOException e) {
				e.printStackTrace();
				send=false;
			} catch (SNMPBadValueException e) {
				e.printStackTrace();
				send=false;
			} 
			System.out.println("Enviado: " + send);
			return send;
		}
		@Override
		protected void onPostExecute(Boolean send){
			
			Builder builder = new Builder(AgentDroid.this);
			
			if(send){ 
				builder.setTitle("Sender Traps v1");
				builder.setMessage("SNMPv1Trap sent successfully");
				builder.setNeutralButton("Ok", null);	
			}else{
				builder.setTitle("Sender Traps v1");
				builder.setMessage("SNMPv1Trap not sent ");
				builder.setNeutralButton("Ok", null);	
			}
			AlertDialog alertDia = builder.create();
			alertDia.show();
			alertDia.setCancelable(false);
		
		}
		
	}
	
}
