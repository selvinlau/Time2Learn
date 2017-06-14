package ch.unibe.zeeguu.t2l;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import ch.unibe.zeeguu.t2l.api.ZeeguuAPI;
import ch.unibe.zeeguu.t2l.api.ZeeguuAccount;
import ch.unibe.zeeguu.t2l.api.ZeeguuWord;
import ch.unibe.zeeguu.t2l.net.DataReceiver;

public class LanguageSelected extends AppCompatActivity implements DataReceiver {

    private ZeeguuAccount account;
    private SharedPreferences prefs;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_selected);

        prefs = getSharedPreferences(T2L.USER_PREFS, Context.MODE_PRIVATE);


        account = new ZeeguuAccount();
        account.loadFromPrefs(prefs);

        String language = prefs.getString(T2L.PREF_LANGUAGE, "");

        TextView lang = (TextView) findViewById(R.id.lang_name);
        lang.setText(Languages.codeToLanguage(language));

        ImageView flag = (ImageView) findViewById(R.id.lang_flag);
        flag.setImageDrawable(getDrawable(Languages.codeToFlag(language)));

        if(!prefs.contains(T2L.PREF_BOOKMARKS))
           ZeeguuAPI.getBookmarks(account, this);
    }

    // triggered through activity XML
    public void changeLanguage(View v){
        prefs.edit().putString(T2L.PREF_LANGUAGE, "").commit();

        Intent intent = new Intent(this, T2L.class);
        startActivity(intent);
    }

    @Override
    public void receiveData(Object o) {
        String data = (String) o;
        System.out.println("Loaded data:: " + data);
        prefs.edit().putString(T2L.PREF_BOOKMARKS, data).commit();
    }
}
