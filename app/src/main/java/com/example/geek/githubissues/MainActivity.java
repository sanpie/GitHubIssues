package com.example.geek.githubissues;

import android.app.Activity;
import android.os.AsyncTask;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity {
    private ProgressDialog pDialog;
EditText e1,e2,e3;TextView tt;
    String[] list1;
    int count1=0,count2=0;
    String repository="redcarpet",user="vmg",state="closed";
    String url="https://api.github.com/repos/"+user+"/"+repository+"/issues?state=closed";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tt=(TextView)findViewById(R.id.textview3);
        tt.setText("Titles for :- user: "+user+", repository: "+repository+", state: "+state);
        new GetContacts().execute();

    }
public void search(View v)
{
    e1=(EditText)findViewById(R.id.editText);
    e2=(EditText)findViewById(R.id.editText2);
    e3=(EditText)findViewById(R.id.editText3);
    user=e1.getText().toString();
    repository=e2.getText().toString();
    state=e3.getText().toString();
    new GetContacts().execute();

}
    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url, ServiceHandler.GET);
            Log.d("Response: ", "> " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONArray jsonarray=new JSONArray(jsonStr);
                    // looping through All Contacts
                    list1=new String[jsonarray.length()];
                    count1=jsonarray.length();
                    for (int i = 0; i < jsonarray.length(); i++) {


                        JSONObject c = jsonarray.getJSONObject(i);

                        String name = c.getString("title");
                        list1[i]=name;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            ArrayAdapter adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.activity_listview, list1);
            TextView t1=(TextView)findViewById(R.id.textview1);
            t1.setText(count1+"");
            ListView listView = (ListView) findViewById(R.id.list_view);
            listView.setAdapter(adapter);
        }

    }


}
