package gio.gcanteen;

import gio.gcanteen.model.AppContext;
import gio.gcanteen.model.ModelProxy;
import gio.gcanteen.model.Statement;

import java.util.Collection;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppContext.newAppContext(this.getApplicationContext());
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    public void loginClicked(View view) {
    	AppContext appContext = AppContext.getAppContext();
    	TextView textView = (TextView) findViewById(R.id.res_text_view);
    	textView.setText("Button pressed!");
		
    	if (appContext.getNetworkUtils().testConnectivity()) {
    		textView.append("\nConnection available!");
    		ExecuteLoginTask executeLoginTask = new ExecuteLoginTask(appContext.getModelProxy(), textView);
    		executeLoginTask.execute();
    	} else {
    		textView.append("\nNo connection available...");
    	}
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
    		case R.id.action_settings:
    			Intent intent = new Intent(this, SettingsActivity.class);
    			startActivity(intent);
    			return true;
    		default:
    			return super.onOptionsItemSelected(item);
    	}
    }
    
    private class GetStatementsTask extends AsyncTask<Void, Void, Void> {
    	private TextView text;
		private ModelProxy modelProxy;

    	private Exception exceptionThrown = null;
    	private boolean logged_in = true;

		public GetStatementsTask(ModelProxy modelProxy, TextView text) {
			this.modelProxy = modelProxy;
    		this.text = text;
    	}

    	@Override
    	protected Void doInBackground(Void... voids) {
    		try {
    			this.modelProxy.loadStatements();
    		} catch (UnauthorizedException e) {
    			this.logged_in = false;
			} catch (Exception e) {
				this.exceptionThrown = e;
			}
    		return null;
    	}
    	
    	@Override
    	protected void onPostExecute(Void void_) {
    		if (this.exceptionThrown != null) {
    			this.text.append("\n" + getString(R.string.something_bad_happened));
    			this.text.append(this.exceptionThrown.toString());
    			Log.e("gCanteen", "exception", this.exceptionThrown);
    		} else {
    			if (this.logged_in) {
    				Collection<Statement> statements = this.modelProxy.getStatements();
    				for (Statement statement : statements) {
    					this.text.append("\n" + statement.toFormattedString());
    				}
    			} else {
    				this.text.append("\n" + getString(R.string.not_logged_in));
    			}
    		}
    	}
    }
    
    private class ExecuteLoginTask extends AsyncTask<Void, Void, Void> {
    	private TextView text;
		private ModelProxy modelProxy;

    	private Exception exceptionThrown = null;
    	private boolean logged_in = true;
    	
    	private boolean compatible_version;

		public ExecuteLoginTask(ModelProxy modelProxy, TextView text) {
			this.modelProxy = modelProxy;
    		this.text = text;
    	}

    	@Override
    	protected Void doInBackground(Void... voids) {
    		try {
				this.compatible_version = this.modelProxy.checkVersion();
    		} catch (UnauthorizedException e) {
    			this.logged_in = false;
			} catch (Exception e) {
				this.exceptionThrown = e;
			}
    		return null;
    	}
    	
    	@Override
    	protected void onPostExecute(Void void_) {
    		if (this.exceptionThrown != null) {
    			this.text.append("\n" + getString(R.string.something_bad_happened));
    			this.text.append(this.exceptionThrown.toString());
    			Log.e("gCanteen", "exception", this.exceptionThrown);
    		} else {
    			if (this.logged_in) {
    				this.text.append("\n" + getString(R.string.login_successful));
    				if (this.compatible_version) {
    					this.text.append("\n" + getString(R.string.protocol_compatible));
    		    		GetStatementsTask getStatementsTask = new GetStatementsTask(this.modelProxy, this.text);
    		    		getStatementsTask.execute();
    				} else {
    					this.text.append("\n" + getString(R.string.protocol_not_compatible));
    				}
    			} else {
    				this.text.append("\n" + getString(R.string.login_failed));
    			}
    		}
    	}
    }
    
}
