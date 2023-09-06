package com.stranger.chat.login_modules.fregment;

import static android.content.ContentValues.TAG;
import static android.text.TextUtils.isEmpty;
import static android.widget.Toast.LENGTH_SHORT;
import static com.stranger.chat.fuctionality.FirebaseDatabaseConnection.mAuth;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.fragment.app.Fragment;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.stranger.chat.R;

import org.jetbrains.annotations.Nullable;

import java.util.concurrent.TimeUnit;

public class LoginFragment extends Fragment {

    MaterialToolbar toolBar;
    AppCompatAutoCompleteTextView tfCountryCode;
    EditText tfPhonenumber;
    Button btContinue;

    ArrayAdapter<String> adapter;
    String countryCode, phonenumber, requestCode;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] countryCodes = {"1", "91", "44", "49", "81",};
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, countryCodes);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolBar = view.findViewById(R.id.toolBar);
        tfCountryCode = view.findViewById(R.id.tfCountryCode);
        tfPhonenumber = view.findViewById(R.id.tfPhonenumber);
        btContinue = view.findViewById(R.id.btContinue);

        tfCountryCode.setAdapter(adapter); // drop down menu (Country code)

        toolBar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.policy) // redirect to policy web page
                Toast.makeText(requireActivity(), "policy web page will be open", LENGTH_SHORT).show();
            else return false;
            return true;
        });

        // to get otp
        btContinue.setOnClickListener(v -> {
            countryCode = tfCountryCode.getText().toString().replaceAll("\\s", "").trim();
            phonenumber = tfPhonenumber.getText().toString().replaceAll("\\s", "").trim();

            requestCode = String.format("+%s %s", countryCode, phonenumber); // it is combination of country code & phonenumber

            if (isEmpty(phonenumber))
                Toast.makeText(getContext(), "Phone Number is required", LENGTH_SHORT).show();
            else if (isEmpty(countryCode))
                Toast.makeText(getContext(), "Country code is required", LENGTH_SHORT).show();
            else {

                PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth).setPhoneNumber(requestCode).setTimeout(60L, TimeUnit.SECONDS).setActivity(requireActivity()).setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
//                                signInWithPhoneAuthCredential(credential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(getContext(), e.getMessage(), LENGTH_SHORT).show();
                        Log.d(TAG, "onVerificationCompleted:" + e.getMessage());
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                        super.onCodeSent(verificationId, token);

                        Bundle bundle = new Bundle();
                        bundle.putString("phonenumber", requestCode);
                        bundle.putString("verificationId", verificationId);

                        requireActivity().getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).replace(R.id.fragment, VerificationFragment.class, bundle).addToBackStack(null).commit();
                    }
                }).build();
                PhoneAuthProvider.verifyPhoneNumber(options);
            }
        });
    }
}

