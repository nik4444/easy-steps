package com.easysteps.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.easysteps.repository.Repository;

import javax.inject.Inject;

/**
 * Created by NIKUNJ
 */
public class ViewModelFactory implements ViewModelProvider.Factory {

    private final Repository repository;

    @Inject
    public ViewModelFactory(Repository repository) {
        this.repository = repository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PaymentMethodsViewModel.class)) {
            return (T) new LoginViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown class name");
    }
}
