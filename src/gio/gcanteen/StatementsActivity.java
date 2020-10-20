package gio.gcanteen;

import gio.gcanteen.model.AppContext;
import gio.gcanteen.model.Statement;

import java.util.Collection;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class StatementsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statements_loading);
        this.reloadStatements();
    }
    
    public void reloadStatements() {
    	GetStatementsTask getStatementsTask = new GetStatementsTask();
		getStatementsTask.execute();
    }
    
    private class GetStatementsTask extends AsyncTask<Void, Void, Void> {

    	private Exception exceptionThrown = null;
    	private boolean logged_in = true;

    	@Override
    	protected Void doInBackground(Void... voids) {
    		try {
    			AppContext.getAppContext().getModelProxy().loadStatements();
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
    			Log.e("gCanteen", "exception", this.exceptionThrown);
    		} else {
    			if (this.logged_in) {
    				Collection<Statement> statements = AppContext.getAppContext().getModelProxy().getStatements();
    		        setContentView(R.layout.activity_statements);
    		        ListView statementsListView = (ListView) findViewById(R.id.statements_list_view);
    		        ArrayAdapter<Statement> adapter = new ArrayAdapter<Statement>(
    		        		AppContext.getAppContext().getContext(),
    		        		R.layout.activity_statements_text_view,
    		        		statements.toArray(new Statement[0]));
    		        statementsListView.setAdapter(adapter);
    			} else {
    				// TODO
    				//this.text.append("\n" + getString(R.string.not_logged_in));
    			}
    		}
    	}
    }

}
