package ucv.tesis.tesisandroid;

import ucv.tesis.tesisandroid.R;

import android.os.Bundle;
import android.os.SystemClock;
import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.*;
import android.content.Context;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.view.Menu;
import android.view.View;


public class AgentDroid extends Activity {

	TabHost tabHost;
	
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
			// TODO: handle exception
			e.printStackTrace();
		}
        
        toast1.show();
        
        String str = android.os.Build.MODEL + ", " +
        			 android.os.Build.FINGERPRINT + ", " +
        			 android.os.Build.BOOTLOADER + ", " +
        			 android.os.Build.MANUFACTURER + ", " +
        			 android.os.Build.CPU_ABI ;
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
	}

}
