package ch.unibe.zeeguu.t2l;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ch.unibe.zeeguu.t2l.api.ZeeguuAPI;
import ch.unibe.zeeguu.t2l.api.ZeeguuAccount;
import ch.unibe.zeeguu.t2l.net.DataReceiver;

public class ArticleReceiver implements DataReceiver {

    private SharedPreferences prefs;
    private Context context;

    public ArticleReceiver(Context context){
        this.context = context;
    }
    @Override
    public void receiveData(Object o) {
        prefs = context.getSharedPreferences(T2L.USER_PREFS, Context.MODE_PRIVATE);
        String data = (String) o;
        System.out.println("Loaded data:: " + data);
        System.out.println(T2L.PREF_ARTICLES);
        prefs.edit().putString(T2L.PREF_ARTICLES, data).commit();
    }
}
