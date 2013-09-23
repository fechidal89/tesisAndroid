package ucv.tesis.tesisandroid;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;


import ucv.tesis.tesisandroid.DBOIDHelper.ObjIdent;
import ucv.tesis.tesisandroid.R;
import ucv.tesis.tesisandroid.AgentSNMP.LocalBinder;


import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
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
import android.widget.Switch;
import android.widget.TabHost;
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
                    "AgentDroid DISCONNECTED to AGENT  ", Toast.LENGTH_LONG).show();
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
        specs.setIndicator("Sender Traps");
        tabHost.addTab(specs);
        
        
        specs = tabHost.newTabSpec("tag3");
        specs.setContent(R.id.tab3);
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
        			label.setVisibility(View.INVISIBLE);
        			text.setVisibility(View.INVISIBLE);
        		}	        		
        	}
        	public void onNothingSelected(AdapterView<?> parentView){;}
        }); 
        
        Button btn2 = (Button) findViewById(R.id.tab3button1);
        
        btn2.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				try {
					DBOIDHelper database = new DBOIDHelper(getBaseContext());
					
					EditText edit = (EditText) findViewById(R.id.tab3editText1);
				    String str  = edit.getText().toString();
				    database.update("1.3.6.1.2.1.1.4.0", str);
				   
				    edit = (EditText) findViewById(R.id.tab3editText2);
				    str  = edit.getText().toString();
				    database.update("1.3.6.1.2.1.1.5.0", str);
				    
				    edit = (EditText) findViewById(R.id.tab3editText3);
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
        ObjIdent oid = null;
        DBOIDHelper database = new DBOIDHelper(getBaseContext());
        ArrayList<ObjIdent> objIdens = database.getAll();
        
        for (Iterator<ObjIdent> iterator = objIdens.iterator(); iterator.hasNext();) {
			ObjIdent objIdent = (ObjIdent) iterator.next();
			System.out.println(objIdent.getName() + " => " + objIdent.getValue() );  
		}
        
        TextView txtChanged = (TextView) findViewById(R.id.tab3textView11);
        
		oid = database.getOID("1.3.6.1.2.1.1.1.0");
		if(oid != null){
			txtChanged.setText(oid.getValue());
		}
		
		oid = database.getOID("1.3.6.1.2.1.1.2.0");
		txtChanged = (TextView) findViewById(R.id.tab3textView21);
		if(oid != null){
			txtChanged.setText(oid.getValue());
		}
		//  getOID("1.3.6.1.2.1.1.3.0")
	    try {
	    	Date date = new Date(SystemClock.uptimeMillis());
		    DateFormat formatter = new SimpleDateFormat("HH:mm:ss.SS");
		    String dateFormatted = formatter.format(date);
		    txtChanged = (TextView) findViewById(R.id.tab3textView31);
		    txtChanged.setText(dateFormatted );
		} catch (Exception e) {
			e.printStackTrace();
		}		    
	    
	    EditText editTextChanged;
	    editTextChanged = (EditText) findViewById(R.id.tab3editText1);
	    oid = database.getOID("1.3.6.1.2.1.1.4.0");
	    if(oid != null){
	    	editTextChanged.setText(oid.getValue());
		}
	    
	    editTextChanged = (EditText) findViewById(R.id.tab3editText2);
	    oid = database.getOID("1.3.6.1.2.1.1.5.0");
	    if(oid != null){
	    	editTextChanged.setText(oid.getValue());
		}
	    
	    editTextChanged = (EditText) findViewById(R.id.tab3editText3);
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
	    	
	    
	    database.cleanup();

		
		
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
		    		if(isChecked){
	    				startService(intService);
	    				bindService(intService, myConnection, Context.BIND_AUTO_CREATE);
	    				isBound = true;

		    		}else{
		    			if(isBound) {
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
		    		if(isChecked){
	    				startService(intService);
	    				bindService(intService, myConnection, Context.BIND_AUTO_CREATE);
	    				isBound = true;

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


}
