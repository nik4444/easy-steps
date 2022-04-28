package com.easysteps.viewmodel;

import android.content.Context;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.easysteps.helper.Utils;
import com.easysteps.network.ApiResponse;
import com.easysteps.repository.Repository;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by NIKUNJ
 */

public class LoginViewModel extends ViewModel {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<ApiResponse> responseLiveData = new MutableLiveData<>();
    private final Repository repository;

    public LoginViewModel(Repository repository) {
        this.repository = repository;
    }

    public MutableLiveData<ApiResponse> loginResponse() {
        return responseLiveData;
    }

    public void getLogin(Context context, HashMap<String, String> body) {
        if (Utils.checkInternetConnection(context))
            disposables.add(repository.login(body)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .doOnSubscribe((d) -> responseLiveData.setValue(ApiResponse.loading()))
                    .subscribe(
                            result -> responseLiveData.setValue(ApiResponse.success(result)),
                            throwable -> responseLiveData.setValue(ApiResponse.error(throwable))
                    ));
        else Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }
}
