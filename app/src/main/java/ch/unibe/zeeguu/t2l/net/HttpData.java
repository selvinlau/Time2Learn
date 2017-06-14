package ch.unibe.zeeguu.t2l.net;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gupta on 06/06/2017.
 */

public class HttpData {
    private String url;
    private String requestMethod = "POST";
    private Map<String, String> parameters = new HashMap<String, String>();
    private int response;

    public HttpData(String url){
        this.url = url;
    }

    public void addParameter(String key, String value){
        parameters.put(key, value);
    }


    public void setResponse(int response){ this.response=response; }

    public String getUrl(){
        return url;
    }


    public String getParameters(){
        String params = "";

        for(Map.Entry<String, String> e : parameters.entrySet()){
            params += e.getKey() + "=" + e.getValue() + "&";
        }

        return params;
    }

    public int getResponse(){
        return response;
    }

    public boolean successResponse() {
        return response == 200;
    }

    public String getRequestMethod() {
        return requestMethod;
    }

    public void setRequestMethod(String requestMethod) {
        if(requestMethod.equals("POST") || requestMethod.equals("GET"))
            this.requestMethod = requestMethod;
        else
            System.err.println("Attempt to add invalid request method to HttpData.");
    }
}
