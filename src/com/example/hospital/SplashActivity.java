package com.example.hospital;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class SplashActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        
        Thread splashThread = new Thread() 
        {
            @Override
            public void run()
            {
               try 
               {
                  int waited = 0;
                  while (waited < 5000)
                  {
                     sleep(100);
                     Log.d("Thread", Integer.toString(waited));
                     waited += 100;
                  }
               } 
               catch (InterruptedException e) 
               {
                  // do nothing
               } 
               finally
               {
            	   
                  finish();
                  Intent i = new Intent(getApplicationContext(),MainActivity.class);
             
                  startActivity(i);
               }
            }
         };
         
         splashThread.start();
         
    }
    
}