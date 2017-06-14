package ch.unibe.zeeguu.t2l.net;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.apache.commons.io.IOUtils;

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;


public class HttpRequest extends AsyncTask<HttpData, Void, String> {
    private HttpData data;


    public HttpRequest(HttpData data) {
        this.data = data;
    }

    @Override
    protected void onPreExecute(){
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(HttpData... params) {
        return sendForm();
    }

    @Override
    protected void onPostExecute(String result){
        super.onPostExecute(result);
    }


    private String sendForm(){
        String body = "";
        HttpsURLConnection con = null;
        try{
            String url = data.getUrl();

            System.out.println("URL: " + data.getUrl());
            URL obj = new URL(url);
            con = (HttpsURLConnection) obj.openConnection();

            con.setRequestMethod(data.getRequestMethod());
            con.setDoInput(true);
            con.setDoOutput(true);


            // Set POST parameters
            OutputStream os = con.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(data.getParameters());
            writer.flush();
            writer.close();
            os.close();



            data.setResponse(con.getResponseCode());

            String encoding = con.getContentEncoding();
            encoding = encoding == null ? "UTF-8" : encoding;
            if(con.getResponseCode() == 200)
                body = IOUtils.toString(con.getInputStream(), encoding);

        }
        catch(Exception e){
            e.printStackTrace();
        }

        System.out.println("response: \n" + body);
        return body;
    }
}
