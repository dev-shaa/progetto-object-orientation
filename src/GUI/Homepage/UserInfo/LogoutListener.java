package GUI.Homepage.UserInfo;

import java.util.EventListener;

/**
 * L'interfaccia per i listener che vogliono essere avvertiti quando viene eseguito il logout.
 */
public interface LogoutListener extends EventListener {

    /**
     * Invocato quando viene eseguito il logout.
     */
    public void onLogout();
}
