package com.uph.uph;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONObject;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;
import cz.msebera.android.httpclient.params.BasicHttpParams;
import cz.msebera.android.httpclient.params.HttpConnectionParams;
import cz.msebera.android.httpclient.params.HttpParams;
import cz.msebera.android.httpclient.util.EntityUtils;

import java.util.ArrayList;

public class ServerRequests {

    ProgressDialog progressDialog;
    public static  final int CONNECTION_TIMEOUT = 15000;
    public static final String SERVER_ADDRESS = "http://uph.comxa.com/";

    public ServerRequests(Context context) {

        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing");
        progressDialog.setMessage("Please wait...");

    }

    public void storeDataInBataInBackground(Contact contact , GetUserCallback callback) {

        progressDialog.show();
        new StoreDataAsyncTask(contact , callback).execute();
    }

    public void fetchDataInBackground(Contact contact , GetUserCallback callback) {

        progressDialog.show();
        new FetchDataAsyncTask(contact, callback).execute();
    }

    public class StoreDataAsyncTask extends AsyncTask<Void , Void , Void> {

        Contact contact;
        GetUserCallback  callback;

        public StoreDataAsyncTask(Contact contact , GetUserCallback callback) {

            this.contact = contact;
            this.callback = callback;

        }

        @Override
        protected Void doInBackground(Void... voids) {

            ArrayList<NameValuePair> data_to_send = new ArrayList<>();
            data_to_send.add(new BasicNameValuePair("Name" , contact.name));
            data_to_send.add(new BasicNameValuePair("Email" , contact.email));
            data_to_send.add(new BasicNameValuePair("Username" , contact.username));
            data_to_send.add(new BasicNameValuePair("Password", contact.password));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams , CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "Register.php");

            try {

                post.setEntity(new UrlEncodedFormEntity(data_to_send));
                client.execute(post);

            }catch(Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            progressDialog.dismiss();
            callback.done(null);

            super.onPostExecute(aVoid);
        }
    }

    public class FetchDataAsyncTask extends AsyncTask<Void , Void , Contact> {

        Contact contact;
        GetUserCallback callback;

        public  FetchDataAsyncTask(Contact contact , GetUserCallback callback) {

            this.contact = contact;
            this.callback = callback;

        }

        @Override
        protected Contact doInBackground(Void... voids) {

            ArrayList<NameValuePair> data_to_send = new ArrayList<>();
            data_to_send.add(new BasicNameValuePair("Username" , contact.username));
            data_to_send.add(new BasicNameValuePair("Password", contact.password));

            HttpParams httpRequestParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
            HttpConnectionParams.setSoTimeout(httpRequestParams , CONNECTION_TIMEOUT);

            HttpClient client = new DefaultHttpClient(httpRequestParams);
            HttpPost post = new HttpPost(SERVER_ADDRESS + "FetchUserData.php");

            Contact returnedcontact = null;
            try {

                post.setEntity(new UrlEncodedFormEntity(data_to_send));
                HttpResponse httpResponse = client.execute(post);

                HttpEntity entity = httpResponse.getEntity();
                String result = EntityUtils.toString(entity);

                JSONObject jsonObject = new JSONObject(result);

                if(jsonObject.length() == 0) {

                    returnedcontact = null;

                }else {

                    String name , email;
                    name = null;
                    email = null;

                    if(jsonObject.has("name"))
                        name = jsonObject.getString("name");
                    if(jsonObject.has("email"))
                        email = jsonObject.getString("email");

                    returnedcontact = new Contact(name , email , contact.username , contact.password);


                }

            }catch(Exception e) {
                e.printStackTrace();
            }
            return returnedcontact;
        }

        @Override
        protected void onPostExecute(Contact returnedcontact) {

            progressDialog.dismiss();
            callback.done(returnedcontact);

            super.onPostExecute(returnedcontact);
        }

    }
}
