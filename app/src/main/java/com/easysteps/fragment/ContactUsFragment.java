package com.easysteps.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.easysteps.R;
import com.easysteps.activity.MainActivity;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;


public class ContactUsFragment extends Fragment {

    TextView txt_cancel;
    LinearLayout rl_contact_us;
    private TextView txt_contact_to;
    private EditText edt_subject;
    private ImageView img_send;

    public ContactUsFragment() {
    }

    public static ContactUsFragment newInstance(String param1, String param2) {
        ContactUsFragment fragment = new ContactUsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact_us, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        txt_cancel = view.findViewById(R.id.txt_cancel);
        txt_contact_to = (TextView) view.findViewById(R.id.txt_contact_to);
        edt_subject = (EditText) view.findViewById(R.id.edt_subject);
        rl_contact_us = view.findViewById(R.id.rl_contact_us);
        img_send = (ImageView) view.findViewById(R.id.img_send);

        txt_contact_to.setText(getActivity().getString(R.string.text_mail));

        txt_cancel.setOnClickListener(v -> {
            FragmentManager manager = getFragmentManager();
            manager.popBackStackImmediate();
        });

        img_send.setOnClickListener(view1 -> {
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        KeyboardVisibilityEvent.setEventListener(
                getActivity(),
                isOpen -> {
                    if (isOpen) {
                        MainActivity.card_navigation.setVisibility(View.GONE);
                    } else {
                        MainActivity.card_navigation.setVisibility(View.VISIBLE);
                    }
                });

    }

}