package com.easysteps.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.easysteps.network.ApiResponse;
import com.easysteps.repository.Repository;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by NIKUNJ
 */

public class PaymentMethodsViewModel extends ViewModel {

    private final CompositeDisposable disposables = new CompositeDisposable();
    private final MutableLiveData<ApiResponse> responseLiveData = new MutableLiveData<>();
    private final Repository repository;

    public PaymentMethodsViewModel(Repository repository) {
        this.repository = repository;
    }

    public MutableLiveData<ApiResponse> paymentMethodResponse() {
        return responseLiveData;
    }

//    public void getPaymentData(Context context) {
//        if (Utils.checkInternetConnection(context))
//            disposables.add(repository.login()
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .doOnSubscribe((d) -> responseLiveData.setValue(ApiResponse.loading()))
//                    .subscribe(
//                            result -> responseLiveData.setValue(ApiResponse.success(result)),
//                            throwable -> responseLiveData.setValue(ApiResponse.error(throwable))
//                    ));
//        else Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
//    }

    @Override
    protected void onCleared() {
        disposables.clear();
    }
}
