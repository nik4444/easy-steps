package com.easysteps.model;

import android.animation.ObjectAnimator;
import android.widget.LinearLayout;

public class BtnAnimDetail {

    LinearLayout back_ll_lout;
    ObjectAnimator objectOfAnimator;


    public BtnAnimDetail(LinearLayout back_ll_lout, ObjectAnimator objectOfAnimator) {
        this.back_ll_lout = back_ll_lout;
        this.objectOfAnimator = objectOfAnimator;
    }

    public LinearLayout getBack_ll_lout() {
        return back_ll_lout;
    }

    public void setBack_ll_lout(LinearLayout back_ll_lout) {
        this.back_ll_lout = back_ll_lout;
    }

    public ObjectAnimator getObjectOfAnimator() {
        return objectOfAnimator;
    }

    public void setObjectOfAnimator(ObjectAnimator objectOfAnimator) {
        this.objectOfAnimator = objectOfAnimator;
    }
}
