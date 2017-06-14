package ch.unibe.zeeguu.t2l;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.ArrayList;

import ch.unibe.zeeguu.t2l.api.ZeeguuAPI;

import ch.unibe.zeeguu.t2l.R;
import ch.unibe.zeeguu.t2l.api.ZeeguuAccount;

public class WidgetProvider extends AppWidgetProvider {
	public static final String GET_EXTRA = "www.rug.nl.time2learnarticle.GET_EXTRA";
	public static final String TOAST_ACTION = "www.rug.nl.time2learnarticle.TOAST_ACTION";
	public String articleURL;
	SharedPreferences prefs;

	@Override
	public void onReceive(Context context, Intent intent) {
		AppWidgetManager mgr = AppWidgetManager.getInstance(context);
		if (intent.getAction().equals(TOAST_ACTION)) {
			int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
					AppWidgetManager.INVALID_APPWIDGET_ID);
			//int viewIndex = intent.getIntExtra(GET_EXTRA, 0);
			articleURL = intent.getStringExtra(GET_EXTRA);

			Intent intentURL = new Intent(Intent.ACTION_VIEW,
					Uri.parse(articleURL));
			intentURL.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intentURL);

			//Toast.makeText(context, "Touched view " + viewIndex + articleURL, Toast.LENGTH_SHORT).show();
		}
		super.onReceive(context, intent);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {

		final int N = appWidgetIds.length;



		for (int i = 0; i < N; ++i) {

			ZeeguuAccount account = new ZeeguuAccount();
			prefs = context.getSharedPreferences(T2L.USER_PREFS, Context.MODE_PRIVATE);

			account.loadFromPrefs(prefs);


			Intent intent = new Intent(context, WidgetService.class);
			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);

			intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
			RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
			rv.setRemoteAdapter(appWidgetIds[i], R.id.listViewWidget, intent);

			rv.setEmptyView(R.id.listViewWidget, R.id.empty_view);


			Intent toastIntent = new Intent(context, WidgetProvider.class);

			toastIntent.setAction(WidgetProvider.TOAST_ACTION);
			toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
			intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
			PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent,
					PendingIntent.FLAG_UPDATE_CURRENT);
			rv.setPendingIntentTemplate(R.id.listViewWidget, toastPendingIntent);


			appWidgetManager.updateAppWidget(appWidgetIds[i], rv);

		}
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

}
