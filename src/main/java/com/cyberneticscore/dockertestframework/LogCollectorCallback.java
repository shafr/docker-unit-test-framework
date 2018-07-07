package com.cyberneticscore.dockertestframework;

import com.github.dockerjava.api.model.Frame;
import com.github.dockerjava.core.command.LogContainerResultCallback;

/**
 * @author Kanstantsin Shautsou
 * @author shafr - Extracted minimal needed code for log feature to work
 */
public class LogCollectorCallback extends LogContainerResultCallback {
    protected final StringBuffer log = new StringBuffer();

    @Override
    public void onNext(Frame frame) {
        log.append(new String(frame.getPayload()));
    }

    @Override
    public String toString() {
        return log.toString();
    }
}