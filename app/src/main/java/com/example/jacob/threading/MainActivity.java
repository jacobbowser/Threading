package com.example.jacob.threading;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity {

    private Button myWrite;
    private Button myLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
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

    /*********************************************************
     * calls the do In background in the thread class so that
     * the write is in the background
     *
     * @param view
     *********************************************************/
    public void fileWrite(View view) {
        Threads thread = new Threads();
        thread.doInBackground(this.getFilesDir());
    }

    public void loadFile(View view) {
        Threads thread = new Threads();
        thread.doInBackground();
    }

    /*********************************************************
     * The load function will load the file that was previously
     * written.
     ********************************************************/
    public void load() {
        List<String> nums = new ArrayList<String>();
        try {
            FileInputStream fis = openFileInput("numbers.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                nums.add(line);
                Thread.sleep(250);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nums);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(itemsAdapter);
    }

    /************************************************************************
     * The clear file function sets a empty array into the array adapter,
     * therefore clearing the text
     * @param view
     ***********************************************************************/
    public void clearFile(View view) {
        List<String> empty = new ArrayList<String>();
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, empty);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
    }

    /**********************************************************************************
     * The threads class will make a thread in the background to run the write and
     * load functions
     *********************************************************************************/
    private class Threads extends AsyncTask<File, Void, Void> {

        /***************************************************************
         * writes the values 1 - 10 into a file
         * @param fileDir
         **************************************************************/
        public void fileWrite(File fileDir) {
            File file = new File(fileDir, "numbers.txt");

            try {
                FileWriter writer = new FileWriter(file);
                for (int i = 1; i <= 10; i++) {
                    writer.append(i + "\n");
                    Thread.sleep(250);
                }
                writer.flush();
                writer.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        /***************************************************************
         * runs the load and write functions in the background
         * @param params
         * @return
         **************************************************************/
        @Override
        protected Void doInBackground(File... params) {
            if(params.length != 0)
                fileWrite(params[0]);
            else
                load();
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

}


