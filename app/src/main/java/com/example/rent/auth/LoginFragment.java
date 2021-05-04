package com.example.rent.auth;


import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.rent.MainActivity;
import com.example.rent.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import dmax.dialog.SpotsDialog;


public class LoginFragment extends Fragment {


    TextInputEditText email_et, password_et;
    ImageView login_iv, forget_password_iv, sign_up_text_iv;
    FirebaseAuth firebaseAuth;
    private Context context;
    AlertDialog dialog;



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        email_et = view.findViewById(R.id.email_et);
        password_et = view.findViewById(R.id.password_et);
        login_iv = view.findViewById(R.id.login_iv);
        forget_password_iv = view.findViewById(R.id.forget_password_iv);
        sign_up_text_iv = view.findViewById(R.id.sign_up_text_iv);
        dialog = new SpotsDialog.Builder().setContext(context)
                .setCancelable(false).setTheme(R.style.CustomPhoto).build();

        firebaseAuth = FirebaseAuth.getInstance();

        login_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });


        sign_up_text_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(new SignUpFragment());
            }
        });
        forget_password_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeFragment(new ForgetPasswordFragment());
            }
        });

        return view;
    }

    private void loginUser() {

        dialog.show();
        String email = email_et.getText().toString();
        String password = password_et.getText().toString();

        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    if (firebaseAuth.getCurrentUser().isEmailVerified()){
                        dialog.dismiss();
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);


                    }
                    else {
                        dialog.dismiss();
                        Toast.makeText(context, "Please verify your email", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    dialog.dismiss();
                    Toast.makeText(getActivity(), "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    void changeFragment(Fragment fragment) {

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_layout, fragment);
        fragmentTransaction.commit();

    }

}
