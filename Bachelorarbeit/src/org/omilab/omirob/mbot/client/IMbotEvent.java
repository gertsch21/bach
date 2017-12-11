package org.omilab.omirob.mbot.client;

/**
 * Event Interface for mBot onboard button
 *
 * @author    Martin Kunz <martin.michael.kunz@gmail.com>
 */
public interface IMbotEvent {
    void onButton(boolean pressed);
}
