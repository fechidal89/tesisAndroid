package ucv.tesis.tesisandroid;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class AgentDroid extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agent_droid);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.agent_droid, menu);
		return true;
	}

}
