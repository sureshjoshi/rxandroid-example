package com.sureshjoshi.android.rxandroidexamples;

import android.os.Handler;

import com.jakewharton.rxrelay.BehaviorRelay;
import com.jakewharton.rxrelay.PublishRelay;

import rx.Observable;

public class TemperatureSensor {

    private int mCounter = 0;

    private Handler mHandler = new Handler();
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            ++mCounter;

            // Hard-coding some state values for connection
            if (mCounter < 3) {
                mConnectionRelay.call(ConnectionState.DISCONNECTED);
            } else if (mCounter < 6) {
                mConnectionRelay.call(ConnectionState.CONNECTING);
            } else if (mCounter < 13) {
                mConnectionRelay.call(ConnectionState.CONNECTED);
            } else {
                mConnectionRelay.call(ConnectionState.DISCONNECTED);
                mCounter = 0;
            }

            // Create new Event with random data
            mEventRelay.call(new TemperatureStatusEvent());
            mEventCountRelay.call(mEventCountRelay.getValue() + 1);

            mHandler.postDelayed(mRunnable, 1000);
        }
    };

    private BehaviorRelay<ConnectionState> mConnectionRelay;
    private BehaviorRelay<Integer> mEventCountRelay;
    private PublishRelay<TemperatureStatusEvent> mEventRelay;

    private TemperatureSensor() {
        mConnectionRelay = BehaviorRelay.create();
        mEventCountRelay = BehaviorRelay.create();
        mEventRelay = PublishRelay.create();

        // Pre-load the Behaviour relays
        mConnectionRelay.call(ConnectionState.DISCONNECTED);
        mEventCountRelay.call(0);

        mHandler.postDelayed(mRunnable, 1000);
    }

    private static class TemperatureSensorHolder {
        private static final TemperatureSensor INSTANCE = new TemperatureSensor();
    }

    public static TemperatureSensor getInstance() {
        return TemperatureSensorHolder.INSTANCE;
    }

    public Observable<ConnectionState> getConnectionState() {
        return mConnectionRelay.asObservable();
    }

    public Observable<TemperatureStatusEvent> getTemperatureStatusEvent() {
        return mEventRelay.asObservable();
    }

    public Observable<Integer> getNumberOfTemperatureStatusEvents() {
        return mEventCountRelay.asObservable();
    }

    public enum ConnectionState {
        DISCONNECTED,
        CONNECTING,
        CONNECTED
    }

    public static class TemperatureStatusEvent {

        public enum TemperatureStatus {
            SAFE,
            WARNING,
            DANGER,
            CRITICAL
        }

        private TemperatureStatus mTemperatureState;
        private int mTemperature;

        public TemperatureStatusEvent() {
            mTemperature = (int) (Math.random() * 100);
            if (mTemperature < 50) {
                mTemperatureState = TemperatureStatus.SAFE;
            } else if (mTemperature < 75) {
                mTemperatureState = TemperatureStatus.WARNING;
            } else if (mTemperature < 90) {
                mTemperatureState = TemperatureStatus.DANGER;
            } else {
                mTemperatureState = TemperatureStatus.CRITICAL;
            }
        }

        public TemperatureStatus getTemperatureState() {
            return mTemperatureState;
        }

        public int getTemperature() {
            return mTemperature;
        }
    }
}
