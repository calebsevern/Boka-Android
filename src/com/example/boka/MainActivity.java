package com.example.boka;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		
		doStuff();
		return true;
	}
	
	 protected void doStuff() {
		 
		 Button button= (Button) findViewById(R.id.find_it_button);
		 button.setOnClickListener(new View.OnClickListener() {
		     @Override
		     public void onClick(View v) {
		    	 Intent myIntent = new Intent(MainActivity.this, PromptActivity.class);
		    	 EditText mEdit = (EditText)findViewById(R.id.find_it_query);
		    	 /*Fetch f = new Fetch();
		    	 f.isbn = mEdit.getText().toString();
		    	 new Fetch().execute();*/
		    	 myIntent.putExtra("isbn", mEdit.getText().toString());
		    	 MainActivity.this.startActivity(myIntent);
		     }
		 });
			
	 }

}
