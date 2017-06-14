package ch.unibe.zeeguu.t2l.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import ch.unibe.zeeguu.t2l.Languages;
import ch.unibe.zeeguu.t2l.R;

/**
 * Created by gupta on 07/06/2017.
 */

public class LanguageAdapter extends BaseAdapter {
    private final Context context;
    private final String[] languages;

    public LanguageAdapter(Context context, String[] langs){
        this.context = context;
        this.languages = langs;
    }

    @Override
    public int getCount() {
        return languages.length;
    }

    @Override
    public Object getItem(int position) {
        return languages[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            final LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.gridcell_language, null);
        }

        String lang_name = Languages.codeToLanguage(languages[position]);
        int flag_id = Languages.codeToFlag(languages[position]);

        TextView name = (TextView) convertView.findViewById(R.id.lang_name);
        name.setText(lang_name);

        ImageView img = (ImageView) convertView.findViewById(R.id.lang_flag);
        img.setImageDrawable(context.getDrawable(flag_id));

        return convertView;
    }
}
