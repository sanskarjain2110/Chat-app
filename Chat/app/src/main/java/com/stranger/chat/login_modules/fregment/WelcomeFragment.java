package com.stranger.chat.login_modules.fregment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.stranger.chat.R;

import org.jetbrains.annotations.Nullable;

public class WelcomeFragment extends Fragment {
    Button btPolicy, btContinue, btTransferAccount;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_welcome, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btPolicy = view.findViewById(R.id.btPolicy);
        btContinue = view.findViewById(R.id.btContinue);
        btTransferAccount = view.findViewById(R.id.btTransferAccount);

        btPolicy.setOnClickListener(v -> {
            // redirect to policy web page
        });

        btContinue.setOnClickListener(v ->
                requireActivity()
                        .getSupportFragmentManager()
                        .beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.fragment, LoginFragment.class, null)
                        .addToBackStack(null).commit()
        );

        btTransferAccount.setOnClickListener(v -> {
            // redirect to account transfer page
        });
    }
}
