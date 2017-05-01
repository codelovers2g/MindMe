package com.mindmesolo.mindme.GettingStarted;

/**
 * Created by enest_09 on 9/12/2016.
 */

public interface GreetingAndForwarding {
    void checkPermissions(int permissions);

    void playPauseRecord(boolean b);

    void greetingDeleted(Boolean GreetingSwitch);

    void greetingDataFromFragment(Boolean GreetingSwitch);

    void forwardingDataFromFragment(String ForwardingPhone, Boolean ForwardingSwitch);
}
