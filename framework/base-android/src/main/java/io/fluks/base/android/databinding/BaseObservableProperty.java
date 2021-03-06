package io.fluks.base.android.databinding;

import android.databinding.ObservableField;
import android.support.annotation.NonNull;

public class BaseObservableProperty<T> extends ObservableField<T> {

    @SuppressWarnings("ConstantConditions")
    @NonNull
    @Override
    public T get() { return super.get(); }
}
