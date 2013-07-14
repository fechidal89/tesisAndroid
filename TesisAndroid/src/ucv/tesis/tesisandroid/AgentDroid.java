package ucv.tesis.tesisandroid;

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
        
        String str = Build.MODEL + ", " +
        			 Build.FINGERPRINT + ", " +
        			 Build.BOOTLOADER + ", " +
        			 Build.MANUFACTURER + ", " +
        			 Build.CPU_ABI ;
	    TextView txtChanged = (TextView) findViewById(R.id.tab3textView1);
	    txtChanged.setText(txtChanged.getText() + " "+str );
	    txtChanged = (TextView) findViewById(R.id.tab3textView2);
	    txtChanged.setText(txtChanged.getText() + " NULL" );
	    txtChanged = (TextView) findViewById(R.id.tab3textView3);
	    long milliseconds = SystemClock.uptimeMillis();
	    long seconds = milliseconds / 1000;
	    long minutes = seconds / 60;
	    seconds %= 60;
	    long hours = minutes / 60;
	    minutes %= 60;
	    hours %= 24;
	    txtChanged.setText(txtChanged.getText() +" "+hours+":"+minutes+":"+seconds );
	    txtChanged = (TextView) findViewById(R.id.tab3textView7);
	    
	    txtChanged.setText(txtChanged.getText() +" 72" );
	
	
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
		    	
	    } // endIf(Build.VERSION.SDK_INT >= 14) 
	
	}

	private static boolean isRunning(Context context, String class_name)
	{
		try
		{
			for (RunningServiceInfo service : ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningServices(Integer.MAX_VALUE)) 
			{
					System.out.println(service.service.getClassName());
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
