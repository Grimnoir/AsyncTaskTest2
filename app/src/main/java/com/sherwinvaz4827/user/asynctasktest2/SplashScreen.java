package com.sherwinvaz4827.user.asynctasktest2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SplashScreen extends AppCompatActivity {

    private ProgressBar progressBar;
    private TextView textView;
    private ProgressAsync progressAsync;
    private DatabaseReference mReff;
    private String appversion="1.1";
    private Uri dataUri= Uri.parse("https://github.com/Grimnoir/FirebaseAppVerionCheck");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        progressBar=findViewById(R.id.progressBar);
        textView=findViewById(R.id.progresspercentage);
        mReff= FirebaseDatabase.getInstance().getReference("Version");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        progressBar.setMax(100);
        progressBar.setScaleY(3f);
        progressAsync=new ProgressAsync();
        progressAsync.execute();


    }

    public void progressAnimation(){
        ProgressBarAnimation progressBarAnimation=new ProgressBarAnimation(this, progressBar, textView, 0, 100);
        progressBarAnimation.setDuration(8000);
        progressBar.setAnimation(progressBarAnimation);
    }
    public class ProgressAsync extends AsyncTask<String,Integer,String>
    {



        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {

          mReff.addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                  String databaseversion=dataSnapshot.getValue().toString();
                  if (appversion.equals(databaseversion)) {
                      Toast.makeText(SplashScreen.this, "App upto date", Toast.LENGTH_SHORT).show();
                  } else {
                      AlertDialog alertDialog = new AlertDialog.Builder(SplashScreen.this).setTitle("New Version Available")
                              .setMessage("Please update to new version to continue use").setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
                                  @Override
                                  public void onClick(DialogInterface dialog, int which) {
                                      Intent intent = new Intent("android.intent.action.VIEW");
                                      intent.setData(dataUri); // Uri of the data to be displayed
                                      if (intent.resolveActivity(getPackageManager()) != null) {
                                          startActivity(intent);
                                      }


                                  }
                              }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                                  @Override
                                  public void onClick(DialogInterface dialog, int which) {
                                      dialog.dismiss();
                                      System.exit(0);
                                  }
                              }).create();

                      alertDialog.show();

                  }
              }

              @Override
              public void onCancelled(@NonNull DatabaseError databaseError) {
                  Toast.makeText(SplashScreen.this, "Failed to connect to server", Toast.LENGTH_SHORT).show();
              }
          });

            return "Background Task Completed";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate();
            progressAnimation();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }
}
