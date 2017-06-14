package ch.unibe.zeeguu.t2l;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.JsonReader;
import android.widget.RemoteViews;

import org.json.*;
import org.json.simple.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.util.ArrayList;
import java.util.Iterator;

import ch.unibe.zeeguu.t2l.api.ApiParser;
import ch.unibe.zeeguu.t2l.api.ZeeguuAPI;
import ch.unibe.zeeguu.t2l.api.ZeeguuAccount;
import ch.unibe.zeeguu.t2l.api.ZeeguuWord;
import ch.unibe.zeeguu.t2l.net.DataDownloader;
import ch.unibe.zeeguu.t2l.net.DataReceiver;

public class Widget2Learn extends AppWidgetProvider {

    ArrayList<ZeeguuWord> words = new ArrayList<ZeeguuWord>();
    int idx = 0;


    @Override
    public void onEnabled(Context context) {
        System.out.println("onEnabled()");
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        System.out.println("onUpdate()");

        SharedPreferences prefs = context.getSharedPreferences(T2L.USER_PREFS, Context.MODE_PRIVATE);

        if(words.isEmpty()) {
            String bookmark_json = prefs.getString(T2L.PREF_BOOKMARKS, null);

            words = ApiParser.loadBookmarks(bookmark_json);
        }


        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }


    }


    private void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                 int appWidgetId) {

        System.out.println("updateAppWidget");
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.wdg_word_layout);


        if(words.isEmpty())
            return;

        ZeeguuWord word = words.get(idx);

        System.out.println("Loaded word: " + word.getWord_from());

        views.setTextViewText(R.id.txt_word, word.getWord_to());
        views.setTextViewText(R.id.txt_sentence, word.getContext());


        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(word.getUrl()));
        // In widget we are not allowing to use intents as usually. We have to use PendingIntent instead of 'startActivity'
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
        // Here the basic operations the remote view can do.
        views.setOnClickPendingIntent(R.id.img_btn_url, pendingIntent);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }


}