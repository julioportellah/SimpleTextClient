package com.tesisfragments.juliusdevelopment.simpletextclient;

import android.content.ContentResolver;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/*
* Application that sends a string message to a server when the send button is pressed
* Base by Lak J Comspace
* */
public class MainActivity extends ActionBarActivity {

    private Socket client;
    private PrintWriter printWriter;
    private EditText editText;
    private Button button;
    private String message;
    private ImageView imageView,imageView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String folderMain="SimpleTextClient";
        File f=new File(Environment.getExternalStorageDirectory()+"/"+folderMain,"Images");
        if(!f.exists()){
            f.mkdirs();
        }
        imageView=(ImageView)findViewById(R.id.imageView);
        imageView2=(ImageView)findViewById(R.id.imageView2);
        //imageView.setBackgroundColor(Color.BLACK);
        try{
            /*Bitmap bmp= BitmapFactory.decodeFile(photoPath);
        imageView.setImageBitmap(bmp);*/
            //Prueba con un string individual
            String photoPath=Environment.getExternalStorageDirectory().toString()+"/"+folderMain+"/Images/"+"TestImage4.jpg";
            //Drawable d=Drawable.createFromPath(photoPath);
            //imageView.setImageDrawable(d);
            Uri.fromFile(new File(photoPath));
            imageView.setImageURI(Uri.parse(new File(photoPath).toString()));
            //imageView.setImageURI(Uri.fromFile(new File(photoPath)));
        }catch (Exception e){
            e.printStackTrace();
        }
        //Prueba con varios archivos
        String path=Environment.getExternalStorageDirectory().toString()+"/"+folderMain+"/Images/";
        Log.d("Files","Path"+path);
        File fp=new File(path);
        File file[]=fp.listFiles();
        Log.d("Files","Size"+file.length);
        for(int i=0;i<file.length;i++){
            Log.d("Files","FileName:"+file[i].getName());
        }
        imageView.setImageURI(Uri.fromFile(file[2]));
        Uri localImageUri=Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE+"://"+
        getResources().getResourcePackageName(R.drawable.si)+"/"+
        getResources().getResourceTypeName(R.drawable.si)+"/"+
        getResources().getResourceEntryName(R.drawable.si));
        imageView2.setImageURI(localImageUri);
        editText=(EditText)findViewById(R.id.messageField);
        button=(Button)findViewById(R.id.button1);

        button.setOnClickListener(new View.OnClickListener(){
          public void onClick(View v){
              message=editText.getText().toString();
              editText.setText("");
              SendMessage sendMessageTask=new SendMessage();
              sendMessageTask.execute();
          }
        });

    }

    private class SendMessage extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {
            try{
                client=new Socket("192.168.1.35",4444);
                printWriter=new PrintWriter(client.getOutputStream(),true);
                printWriter.write(message);

                printWriter.flush();
                printWriter.close();
                client.close();
            }catch (UnknownHostException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
