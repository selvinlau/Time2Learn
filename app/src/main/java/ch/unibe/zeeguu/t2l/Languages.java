package ch.unibe.zeeguu.t2l;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by gupta on 07/06/2017.
 */

public class Languages {
    private static Map<String, String> lang_names = new HashMap<String, String>();
    private static Map<String, Integer> lang_flag = new HashMap<String, Integer>();

    static{
        lang_names.put("de", "German");
        lang_names.put("es", "Spanish");
        lang_names.put("fr", "French");
        lang_names.put("nl", "Dutch");

        lang_flag.put("de", R.drawable.de);
        lang_flag.put("nl", R.drawable.nl);
        lang_flag.put("es", R.drawable.es);
        lang_flag.put("fr", R.drawable.fr);
    }

    public static String codeToLanguage(String code){
        return lang_names.get(code);
    }

    public static int codeToFlag(String code) {
        return lang_flag.get(code);
    }
}
