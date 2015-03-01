package com.example.boka;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends Activity implements OnClickListener{

	private Button scanBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);		
		setContentView(R.layout.activity_main);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		scanBtn = (Button)findViewById(R.id.scan_button);
		scanBtn.setOnClickListener(this);
		EditText e = (EditText) findViewById(R.id.find_it_query); 
		e.setGravity(Gravity.CENTER_HORIZONTAL);
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
		    	 EditText mEdit = (EditText)findViewById(R.id.find_it_query);
		    	 
		    	 if(mEdit.getText().toString().trim().length() == 10 || mEdit.getText().toString().trim().length() == 13) {
					 
		    		 Intent myIntent = new Intent(MainActivity.this, PromptActivity.class);
			    	 myIntent.putExtra("isbn", mEdit.getText().toString());   	 
			    	 MainActivity.this.startActivity(myIntent);
				 } else {
					 Toast toast = Toast.makeText(getApplicationContext(), "Not a valid ISBN-10 or ISBN-13.", Toast.LENGTH_SHORT); 
					 toast.show();
				 }
		     }
		 });
			
	 }

	@Override
	public void onClick(View v) {
		if(v.getId()==R.id.scan_button){
			IntentIntegrator scanIntegrator = new IntentIntegrator(this);
			scanIntegrator.initiateScan();
		}
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
		if (scanningResult != null) {
			 String scanContent = scanningResult.getContents();
			 if(scanContent.trim().length() == 10 || scanContent.trim().length() == 13) {
			 
				 Intent myIntent = new Intent(MainActivity.this, PromptActivity.class);
		    	 EditText mEdit = (EditText)findViewById(R.id.find_it_query);	    	 
		    	 myIntent.putExtra("isbn", scanContent);
		    	 MainActivity.this.startActivity(myIntent);
			 } else {
				 Toast toast = Toast.makeText(getApplicationContext(), "Not a valid ISBN-10 or ISBN-13.", Toast.LENGTH_SHORT);
				 toast.show();
				 
			 }
		} else{
	    Toast toast = Toast.makeText(getApplicationContext(), 
	            "No scan data received!", Toast.LENGTH_SHORT);
	        toast.show();
	    }
	}

}
