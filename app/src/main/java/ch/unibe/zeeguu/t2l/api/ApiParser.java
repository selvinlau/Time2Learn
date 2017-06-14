package ch.unibe.zeeguu.t2l.api;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by LeveX on 13/06/2017.
 */
public class ApiParser {
    public static ArrayList<ZeeguuWord> loadBookmarks(String bookmark_json) {
        ArrayList<ZeeguuWord> words = new ArrayList<>();


        if(bookmark_json == null || bookmark_json.equals(""))
            return words;

        System.out.println("loadBookmarks: " + bookmark_json);
        JSONParser parser = new JSONParser();

        try {
            JSONArray arr = (JSONArray) parser.parse(bookmark_json);
            Iterator<JSONObject> it = arr.iterator();

            JSONObject obj;
            while(it.hasNext()){
                obj = it.next();
                ZeeguuWord word = new ZeeguuWord();

                word.setId((Long) obj.get("id"));
                word.setContext((String) obj.get("context"));
                word.setUrl((String) obj.get("url"));
                word.setWord_to((String) obj.get("to"));
                word.setWord_from((String) obj.get("from"));

                words.add(word);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println(Arrays.toString(words.toArray()));
        return words;
    }
}
