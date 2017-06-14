package ch.unibe.zeeguu.t2l;

import java.util.ArrayList;
import java.util.Iterator;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import ch.unibe.zeeguu.t2l.R;
import ch.unibe.zeeguu.t2l.api.ApiParser;
import ch.unibe.zeeguu.t2l.api.ZeeguuAPI;
import ch.unibe.zeeguu.t2l.api.ZeeguuAccount;
import ch.unibe.zeeguu.t2l.net.DataReceiver;

/**
 * If you are familiar with Adapter of ListView,this is the same as adapter
 * with few changes
 */
public class ListProvider implements RemoteViewsFactory {
    private ArrayList<ListItem> listItemList = new ArrayList<ListItem>();
    private Context context = null;
    private int appWidgetId;
    SharedPreferences prefs;

    public ListProvider(Context context, Intent intent) {
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);

        prefs = context.getSharedPreferences(T2L.USER_PREFS, Context.MODE_PRIVATE);
        populateListItem();
    }

    private void populateListItem() {
        String articles_json = prefs.getString(T2L.PREF_ARTICLES, null);

        listItemList = ApiParser.loadArticles(articles_json);
    }


    @Override
    public int getCount() {
        return listItemList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*
     *Similar to getView of Adapter where instead of View
     *we return RemoteViews
     *
     */
    @Override
    public RemoteViews getViewAt(int position) {
        final RemoteViews remoteView = new RemoteViews(
                context.getPackageName(), R.layout.article_list_item);

        ListItem listItem = listItemList.get(position);
        remoteView.setTextViewText(R.id.article_title, listItem.title);
        remoteView.setTextViewText(R.id.article_text, listItem.summary);


        Bundle extras = new Bundle();
        extras.putInt(WidgetProvider.GET_EXTRA, position);
        extras.putString(WidgetProvider.GET_EXTRA, listItemList.get(position).url);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        remoteView.setOnClickFillInIntent(R.id.widget_item, fillInIntent);

        return remoteView;
    }
    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
    }

    @Override
    public void onDestroy() {
    }

}
