package com.example.boka;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.Toast;

public class PromptActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
				
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prompt);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.prompt, menu);
		
		doStuff();
		return true;
	}
	
	 protected void doStuff() {
		 	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy); 
			final Handler handler = new Handler();

			final Context c = getApplicationContext();
			final Runnable r = new Runnable() {
			    public void run() {
			        
			    	
			    	try {
						
						Intent intent = getIntent();
						String value = intent.getStringExtra("isbn");
						Toast.makeText(getApplicationContext(), value, Toast.LENGTH_LONG).show();
						
						//0538497815
				        URL yahoo = new URL("http://severn.me/projects/Boka/lookup.php?isbn=" + value);
				        URLConnection yc = yahoo.openConnection();
				        BufferedReader in = new BufferedReader(
				                                new InputStreamReader(
				                                yc.getInputStream()));
				        String inputLine = in.readLine();
				        JSONArray links = new JSONArray(inputLine);
				        for(int i=0; i<links.length(); i++) {
				        	String json = (String) links.get(i);
				        	final JSONObject temp = new JSONObject(json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1));
				        	System.out.println("Magnet: " + temp.getString("magnet"));
				        	//String magnet = l.getString("magnet");
				        	//System.out.println("Magnet: " + magnet);
				        	
				        	LinearLayout layout = (LinearLayout) findViewById(R.id.buttons_here);
			                Button btnTag = new Button(c);
			                btnTag.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			                btnTag.setText(temp.getString("title"));
			                btnTag.setId(i);
			                layout.addView(btnTag);
			                //((Button) findViewById(i)).setOnClickListener(this);
			                btnTag.setOnClickListener(new View.OnClickListener() {
			                    @Override
			                    public void onClick(View v) {
			                    	try {
										//Toast.makeText(getApplicationContext(), temp.getString("title"), Toast.LENGTH_LONG).show();
										Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(temp.getString("magnet")));
										startActivity(browserIntent);
									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
			                    }
			                });
				        }
				        
				        in.close();
					} catch(Exception e) {
						StringWriter sw = new StringWriter();
						PrintWriter pw = new PrintWriter(sw);
						e.printStackTrace(pw);
						sw.toString();
						System.out.println("nope: " + sw);
					} 
			        
			        
			        
			    }
			};

			handler.postDelayed(r, 0);
			
			
	 }

}
