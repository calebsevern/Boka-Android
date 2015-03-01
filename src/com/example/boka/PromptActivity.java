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
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.Toast;

public class PromptActivity extends Activity {
	
	ProgressDialog dialog = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
				
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prompt);
	}

	@Override
	protected void onStart() {
		super.onStart();

		getActionBar().setTitle("Book Search");   
		dialog = ProgressDialog.show(PromptActivity.this, "", 
                "Consulting the oracles...", true);
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.prompt, menu);
		doStuff();
		return true;
	}
	
	 protected void doStuff() {
		 
		 	/*
		 	 * 	"Search Again" button listener
		 	 */
		 
			 Button button= (Button) findViewById(R.id.search_again);
			 button.setOnClickListener(new View.OnClickListener() {
			     @Override
			     public void onClick(View v) {
		    		 Intent myIntent = new Intent(PromptActivity.this, MainActivity.class);
			    	 PromptActivity.this.startActivity(myIntent);
			     }
			 });
		 
		 	
		 	StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy); 
			//final Handler handler = new Handler();
			
			final Context c = getApplicationContext();
			
			Thread t = new Thread(new Runnable() { public void run() { 
			        
			    	try {
						
						Intent intent = getIntent();
						String value = intent.getStringExtra("isbn");
						getActionBar().setTitle(value);   
						
						tweetPrice(value);
						//Toast.makeText(getApplicationContext(), value, Toast.LENGTH_LONG).show();
						
						//0538497815
				        URL yahoo = new URL("http://severn.me/projects/Boka/lookup.php?isbn=" + value);
				        URLConnection yc = yahoo.openConnection();
				        yc.setConnectTimeout(10000);
				        BufferedReader in = new BufferedReader(
				                                new InputStreamReader(
				                                yc.getInputStream()));
				        String inputLine = in.readLine();
				        JSONArray links = new JSONArray(inputLine);

						//Toast.makeText(getApplicationContext(), links.length() + " result(s) found.", Toast.LENGTH_LONG).show();
				        for(int i=0; i<links.length(); i++) {
				        	String json = (String) links.get(i);
				        	final JSONObject temp = new JSONObject(json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1));
				        	//System.out.println("Magnet: " + temp.getString("magnet"));
				        	
				        	LinearLayout layout = (LinearLayout) findViewById(R.id.buttons_here);
			                Button btnTag = new Button(c);
			                LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			                params.setMargins(0, 0, 0, 0);
			                btnTag.setLayoutParams(params);
			                btnTag.setText(temp.getString("title"));
			                btnTag.setId(i);
			                
			                addButton(btnTag, temp.getString("magnet"));
				        }
				        dialog.dismiss();
				        in.close();
					} catch(Exception e) {
						StringWriter sw = new StringWriter();
						PrintWriter pw = new PrintWriter(sw);
						System.out.println("SHIT");
						e.printStackTrace();
					} 
			        
			}});
			t.start();
			//runOnUiThread(t);
			//r.run();
			//handler.postDelayed(r, 0);
	 }
	 
	 protected void tweetPrice(final String isbn) {
		 Thread t = new Thread(new Runnable() { public void run() { 
		        
		    	try {
					System.out.println("http://severn.me/projects/Boka/tweet.php?isbn=" + isbn);
			        URL yahoo = new URL("http://severn.me/projects/Boka/tweet.php?isbn=" + isbn);
			        URLConnection yc = yahoo.openConnection();
			        yc.setConnectTimeout(10000);
			        BufferedReader in = new BufferedReader(
			                                new InputStreamReader(
			                                yc.getInputStream()));
			        String input = in.readLine();
			        System.out.println("Response: " + input);
			        in.close();
			        System.out.println("TWEETED");
				} catch(Exception e) {
					StringWriter sw = new StringWriter();
					System.out.println("NOT TWEETED");
					e.printStackTrace();
				} 
		        
		}});
		t.start();
	 }
	 
	 protected void addButton(final Button b, final String magnet) {
		 Thread t = new Thread(new Runnable() { public void run() { 
		        
					
		        	LinearLayout layout = (LinearLayout) findViewById(R.id.buttons_here);
	                layout.addView(b);
	                b.setOnClickListener(new View.OnClickListener() {
	                    @Override
	                    public void onClick(View v) {
	                    	Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(magnet));
							startActivity(browserIntent);
	                    }
	                });
			   }
		        
		});
		runOnUiThread(t);
	 }

}
