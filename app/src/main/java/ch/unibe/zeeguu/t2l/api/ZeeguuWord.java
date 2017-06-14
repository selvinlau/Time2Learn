package ch.unibe.zeeguu.t2l.api;

/**
 * Created by LeveX on 13/06/2017.
 */

public class ZeeguuWord {
    private Long id;
    private String word_from;
    private String word_to;
    private String context;
    private String url;



    public String getWord_from() {
        return word_from;
    }

    public void setWord_from(String word_from) {
        this.word_from = word_from;
    }

    public String getWord_to() {
        return word_to;
    }

    public void setWord_to(String word_to) {
        this.word_to = word_to;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString(){
        return String.format("%d) %s >> %s     |     %s   (%s)\n", id, word_from, word_to, context, url);
    }
}
