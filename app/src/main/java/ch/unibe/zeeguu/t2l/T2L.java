package ch.unibe.zeeguu.t2l;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;


import ch.unibe.zeeguu.t2l.adapters.LanguageAdapter;
import ch.unibe.zeeguu.t2l.api.ZeeguuAPI;
import ch.unibe.zeeguu.t2l.api.ZeeguuAccount;
import ch.unibe.zeeguu.t2l.net.DataDownloader;
import ch.unibe.zeeguu.t2l.net.DataReceiver;

public class T2L extends AppCompatActivity implements DataReceiver {
    String[] languages;
    GridView gridView;


    private ZeeguuAccount account = new ZeeguuAccount();
    private String language = "";


    public static final String USER_PREFS = "user_prefs";

    public static final String PREF_BOOKMARKS = "pref_bookmarks";
    public static final String PREF_LANGUAGE = "pref_lang";
    public static final String PREF_UUID = "pref_uuid";
    public static final String PREF_PASSWORD = "pref_password";
    public static final String PREF_SESSION = "pref_session";

    SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.language_grid);

        // load gridview view into identiifer
        gridView = (GridView) findViewById(R.id.lang_grid);

        // load shared preferenfces
        prefs = getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);

        language = prefs.getString(PREF_LANGUAGE, "");

        account.loadFromPrefs(prefs);

        System.out.printf("Username: %s\nPassword: %s\n", account.getUuid(), account.getPassword());

        if(language != ""){
            languageSelect(language);
        }

        // get languages from the server
        // this will trigger receivedData() that will populate the gridview
        new DataDownloader(this).execute(ZeeguuAPI.API_GET_LANGUAGES);

    }

    private void populateLanguages() {
        LanguageAdapter langAdapter = new LanguageAdapter(this, languages);
        gridView.setAdapter(langAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                languageSelect(languages[position]);
            }
        });
    }

    private void languageSelect(String language) {
        this.language = language;
        boolean success = prefs.edit().putString(PREF_LANGUAGE, language).commit();

        // if cannot commit language to prefs do nothing (allows user to select again).
        if (!success) return;

        // check for account
        if (!account.isValid()) {
            ZeeguuAPI.newAccount(account, this);
        }

        // check for active session
        if (account.getSession() != null) {
            ZeeguuAPI.signIn(account, this);
        }

        System.out.println("Session: "+  account.getSession());

        // register language
        success = ZeeguuAPI.registerLanguage(language, account, this);

        if(success) {
            Intent intent = new Intent(this, LanguageSelected.class);
            startActivity(intent);
        }
    }



    @Override
    public void receiveData(Object data) {
        languages = ZeeguuAPI.languagesStr2Array((String) data);

        populateLanguages();
    }


}
