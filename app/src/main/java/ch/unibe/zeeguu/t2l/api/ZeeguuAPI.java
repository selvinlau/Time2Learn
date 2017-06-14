package ch.unibe.zeeguu.t2l.api;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

import ch.unibe.zeeguu.t2l.T2L;
import ch.unibe.zeeguu.t2l.net.DataDownloader;
import ch.unibe.zeeguu.t2l.net.DataReceiver;
import ch.unibe.zeeguu.t2l.net.HttpRequest;
import ch.unibe.zeeguu.t2l.net.HttpData;

/**
 * Created by LeveX on 19/05/2017.
 */

public class ZeeguuAPI {
    public static final String API_GET_LANGUAGES = "https://zeeguu.unibe.ch/api/available_languages";
    public static final String API_CREATE_ACCOUNT = "https://zeeguu.unibe.ch/api/add_anon_user";
    public static final String API_LEARNED_LANGUAGE = "https://zeeguu.unibe.ch/api/learned_language/%s?session=%s";
    public static final String API_SIGN_IN = "https://zeeguu.unibe.ch/api/get_anon_session/%s";
    public static final String API_BOOKMARKS_TO_STUDY = "https://zeeguu.unibe.ch/api/bookmarks_to_study/%d?session=%d";

    public static String[] languagesStr2Array(String list) {

        System.out.println("parsing language list: " + list);
        list = list.replace("[", "");
        list = list.replace("]", "");
        list = list.replace("\"", "");
        list = list.replace(" ", "");


        return list.split(",");
    }


    public static void newAccount(ZeeguuAccount account, Context context) {
        SharedPreferences prefs = context.getSharedPreferences(T2L.USER_PREFS, Context.MODE_PRIVATE);

        String id = generateId();
        String pass = generatePass();

        System.out.println("Creating new account with uuid=" + id);
        System.out.println("and password=" + pass);

        HttpData data = new HttpData(API_CREATE_ACCOUNT);
        data.addParameter("uuid", id);
        data.addParameter("password", pass);

        String result = sendHttpRequest(data);

        if (result == null) return;

        Integer session = Integer.parseInt(result);

        SharedPreferences.Editor e = prefs.edit();
        e.putInt(T2L.PREF_SESSION, session);
        e.putString(T2L.PREF_UUID, id);
        e.putString(T2L.PREF_PASSWORD, pass);

        if (e.commit()) {
            account.setUuid(id);
            account.setPassword(pass);
            account.setSession(session);
        }
    }


    public static void signIn(ZeeguuAccount account, Context context){
        SharedPreferences prefs = context.getSharedPreferences(T2L.USER_PREFS, Context.MODE_PRIVATE);

        String url = String.format(API_SIGN_IN, account.getUuid());

        HttpData data = new HttpData(url);
        data.addParameter("password", account.getPassword());

        String result = sendHttpRequest(data);

        if(!data.successResponse()) return;

        int session = Integer.parseInt(result);

        prefs.edit().putInt(T2L.PREF_SESSION, session).commit();
        account.setSession(session);
    }

    public static boolean registerLanguage(String language, ZeeguuAccount account, Context context) {
        SharedPreferences prefs = context.getSharedPreferences(T2L.USER_PREFS, Context.MODE_PRIVATE);
        String url = String.format(API_LEARNED_LANGUAGE, language, account.getSession());


        HttpData data = new HttpData(url);

        sendHttpRequest(data);

        // http response is success
        if (data.successResponse()) {
            // add language to shared prefs
            return prefs.edit().putString(T2L.PREF_LANGUAGE, language).commit();
        }

        // http response is not success
        return false;
    }

    public static void getBookmarks(ZeeguuAccount account, DataReceiver receiver){
        int words = 50;
        String url = String.format(API_BOOKMARKS_TO_STUDY, words, account.getSession());

        System.out.println("URL: " + url);

        new DataDownloader(receiver).execute(url);
    }




    public static String sendHttpRequest(HttpData data){
        String result = "";
        try {
            result = new HttpRequest(data).execute().get();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return result;
    }


    public static String generatePass() {
        return generateRandomString();
    }
    public static String generateId() {
        return generateRandomString();
    }
    public static String generateRandomString(){
        return UUID.randomUUID().toString();
    }
}
