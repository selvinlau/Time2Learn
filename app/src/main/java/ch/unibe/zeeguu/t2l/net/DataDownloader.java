package ch.unibe.zeeguu.t2l.net;

import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by LeveX on 19/05/2017.
 */

public class DataDownloader extends AsyncTask<String, Void, Void> {
    private DataReceiver receipient;
    private String result;

    public DataDownloader(DataReceiver receipient){
        this.receipient = receipient;
    }


    @Override
    protected Void doInBackground(String... params) {


        // load JSON.db into a string
        result = loadURL(params[0]);

        return null;
    }

    @Override
    protected void onPostExecute(Void t){
        // notify parent class
        receipient.receiveData(result);
    }


    /**
     * Loads a URL as a string
     * @param address URL from which data is to be retrieved
     * @return String with content from the connection
     */
    private String loadURL(String address){
        String out = "";

        try {
            URL url = new URL(address);
            URLConnection con = url.openConnection();
            InputStream in = con.getInputStream();
            String encoding = con.getContentEncoding();
            encoding = encoding == null ? "UTF-8" : encoding;

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            byte[] buf = new byte[8192];
            int len = 0;
            while ((len = in.read(buf)) != -1) {
                baos.write(buf, 0, len);
            }
            out = new String(baos.toByteArray(), encoding);

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return out;
    }




}
