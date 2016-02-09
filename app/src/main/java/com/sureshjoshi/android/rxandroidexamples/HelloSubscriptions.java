package com.sureshjoshi.android.rxandroidexamples;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

public class HelloSubscriptions extends Activity {

    CompositeSubscription mSubscriptions;

    @Bind(R.id.textview_connection)
    TextView mConnectionTextView;

    @Bind(R.id.textview_distinct_connection)
    TextView mDistinctConnectionTextView;

    @Bind(R.id.textview_distinctuntilchanged_connection)
    TextView mDistinctUntilChangedConnectionTextView;

    @Bind(R.id.textview_event)
    TextView mEventTextView;

    @Bind(R.id.textview_distinctuntilchanged_event)
    TextView mDistinctUntilChangedEventTextView;

    @Bind(R.id.textview_distinctuntilchanged_keyselector_event)
    TextView mDistinctUntilChangedKeySelectorTextView;

    @OnClick(R.id.button_clear)
    void clearAllText() {
        mConnectionTextView.setText("");
        mDistinctConnectionTextView.setText("");
        mDistinctUntilChangedConnectionTextView.setText("");
        mEventTextView.setText("");
        mDistinctUntilChangedEventTextView.setText("");
        mDistinctUntilChangedKeySelectorTextView.setText("");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_subscriptions);
        ButterKnife.bind(this);
        mSubscriptions = new CompositeSubscription();

        // Add the Connection State subscription
        mSubscriptions.add(TemperatureSensor.getInstance().getConnectionState()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(state -> {
                    mConnectionTextView.append(state.name() + ", ");
                }));

        // Add the Connection State 'Distinct' subscription
        mSubscriptions.add(TemperatureSensor.getInstance().getConnectionState()
                .observeOn(AndroidSchedulers.mainThread())
                .distinct()
                .subscribe(state -> {
                    mDistinctConnectionTextView.append(state.name() + ", ");
                }));

        // Add the Connection State 'DistinctUntilChanged' subscription
        mSubscriptions.add(TemperatureSensor.getInstance().getConnectionState()
                .observeOn(AndroidSchedulers.mainThread())
                .distinctUntilChanged()
                .subscribe(state -> {
                    mDistinctUntilChangedConnectionTextView.append(state.name() + ", ");
                }));

        // Add the Temperature Status 'DistinctUntilChanged' subscription
        mSubscriptions.add(TemperatureSensor.getInstance().getTemperatureStatusEvent()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(event -> {
                    mEventTextView.append(event.getTemperatureState().name() + ", ");
                }));

        // Add the Temperature Status 'DistinctUntilChanged' subscription
        mSubscriptions.add(TemperatureSensor.getInstance().getTemperatureStatusEvent()
                .observeOn(AndroidSchedulers.mainThread())
                .distinctUntilChanged()
                .subscribe(event -> {
                    mDistinctUntilChangedEventTextView.append(event.getTemperatureState().name() + ", ");
                }));

        // Add the Temperature Status 'DistinctUntilChanged' with correct KeySelector subscription
        mSubscriptions.add(TemperatureSensor.getInstance().getTemperatureStatusEvent()
                .observeOn(AndroidSchedulers.mainThread())
                .distinctUntilChanged(TemperatureSensor.TemperatureStatusEvent::getTemperatureState)
                .subscribe(event -> {
                    mDistinctUntilChangedKeySelectorTextView.append(event.getTemperatureState().name() + ", ");
                }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSubscriptions.unsubscribe();
    }

}
