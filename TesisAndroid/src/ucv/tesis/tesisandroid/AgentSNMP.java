package ucv.tesis.tesisandroid;

import snmp.SNMPv1AgentInterface;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;

public class AgentSNMP extends Service {
	
	SNMPv1AgentInterface agent = null;
	Mibs MIB = new Mibs();
	private final IBinder myBinder = new LocalBinder();
	
	
	public AgentSNMP() {}
	
	@Override
	public void onCreate(){
		try {
			this.agent = new SNMPv1AgentInterface(1);
			this.agent.addRequestListener(MIB);
			this.agent.startReceiving();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public void onDestroy(){
		super.onDestroy();
		
		try {
			if (agent != null){ 
				agent.stopReceiving(); 
			}
			Toast.makeText(this, "Servicio Detenido...!", Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	

	@Override
	public IBinder onBind(Intent intent) {
		// TODO: Return the communication channel to the service.
		return this.myBinder;
	}
	
	
	public class LocalBinder extends Binder {
		AgentSNMP getService() {
            return AgentSNMP.this;
		}
	}
}
