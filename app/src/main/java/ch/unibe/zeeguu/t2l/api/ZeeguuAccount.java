package ch.unibe.zeeguu.t2l.api;

import android.content.SharedPreferences;

import ch.unibe.zeeguu.t2l.T2L;

/**
 * Created by LeveX on 12/06/2017.
 */

public class ZeeguuAccount {
    private String uuid, password;
    private Integer session = -1;


    public boolean isValid(){
        return (uuid != null) && (password != null);
    }

    public boolean isSessionValid(){
        return session != null && session > 0;
    }

    public void setSession(int session){
        this.session = session;
    }


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getSession() {
        return session;
    }

    @Override
    public String toString(){
        return String.format("@%s | %s  (%s)\n", uuid, password, session);
    }

    public void loadFromPrefs(SharedPreferences prefs) {
        uuid = prefs.getString(T2L.PREF_UUID, null);
        password = prefs.getString(T2L.PREF_PASSWORD, null);
        session = prefs.getInt(T2L.PREF_SESSION, -1);
    }
}
