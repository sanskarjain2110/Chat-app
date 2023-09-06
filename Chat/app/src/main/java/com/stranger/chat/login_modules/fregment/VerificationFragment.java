package com.stranger.chat.login_modules.fregment;

import static android.text.TextUtils.isEmpty;
import static android.widget.Toast.LENGTH_SHORT;
import static com.stranger.chat.fuctionality.FirebaseDatabaseConnection.currentUser;
import static com.stranger.chat.fuctionality.FirebaseDatabaseConnection.mAuth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.stranger.chat.R;
import com.stranger.chat.fuctionality.Routes;

import org.jetbrains.annotations.Nullable;

public class VerificationFragment extends Fragment {

    TextView tvTitle;
    EditText tfOtp;
    Button btWrongNumber, btResendCode, btVerify;

    Bundle bundle;
    String phonenumber, verificationId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = getArguments();
        if (bundle != null) {
            phonenumber = bundle.getString("phonenumber");
            verificationId = bundle.getString("verificationId");
        } else requireActivity().finish();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_verification, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvTitle = view.findViewById(R.id.tvTitle);
        tfOtp = view.findViewById(R.id.tfOtp);
        btWrongNumber = view.findViewById(R.id.btWrongNumber);
        btResendCode = view.findViewById(R.id.btResendCode);
        btVerify = view.findViewById(R.id.btVerify);

        tvTitle.setText(String.format("Enter the code we sent to %s", phonenumber)); // shows number

        btVerify.setOnClickListener(v -> {
            String otp = tfOtp.getText().toString();

            if (isEmpty(otp)) {
                Toast.makeText(getContext(), "Please fill fields", LENGTH_SHORT).show();
            } else {
                signInWithPhoneAuthCredential(PhoneAuthProvider.getCredential(verificationId, otp));
            }
        });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(requireActivity(), task -> {
            if (task.isSuccessful()) {
                currentUser = mAuth.getCurrentUser();
                new Routes(getContext(), requireActivity()).loginModuleRoutes();
            } else Toast.makeText(getContext(), "otp verified failed", LENGTH_SHORT).show();

        });
    }
}
