package ucv.tesis.tesisandroid;

import ucv.tesis.tesisandroid.R;

import android.os.Bundle;
import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.content.Context;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;
import android.view.Menu;

public class AgentDroid extends Activity {

	TabHost tabHost;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agent_droid);
		 tabHost = (TabHost) findViewById(R.id.tabhost);
	        System.out.println(tabHost.getId());
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

	        Toast toast1 = Toast.makeText(getApplicationContext(),
	                "No Connected", Toast.LENGTH_SHORT);
	        
	        
	        try {
	        	ConnectivityManager connec = null;
	            connec =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
	            
	            if (connec != null) {

	                NetworkInfo net = connec.getActiveNetworkInfo();
	                
	                if (net != null && net.getType() == ConnectivityManager.TYPE_WIFI) 
	                	toast1 = Toast.makeText(getApplicationContext(),
	                            "AgentDroid by WIFI", Toast.LENGTH_SHORT);			
	                else
	        			toast1 = Toast.makeText(getApplicationContext(),
	                            "AgentDroid connected by other network", Toast.LENGTH_SHORT);
	        			
	    		}
	            
			} catch (Exception e) {
				// TODO: handle exception
				for (int i = 0; i < 100; i++) {

					System.out.println("Error en apertura de conexion!!!");
						
				}
			}
	        
	        toast1.show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.agent_droid, menu);
		return true;
	}

}
