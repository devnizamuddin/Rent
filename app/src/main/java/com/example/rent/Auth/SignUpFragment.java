package com.example.rent.Auth;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.rent.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import dmax.dialog.SpotsDialog;


public class SignUpFragment extends Fragment {


    ImageView sign_up_btn_iv;
    TextInputEditText email_et,password_et,confirm_password_et;

    FirebaseAuth firebaseAuth;

    private Context context;
    AlertDialog dialog;

    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_sign_up, container, false);

        email_et = view.findViewById(R.id.email_et);
        password_et = view.findViewById(R.id.password_et);
        confirm_password_et = view.findViewById(R.id.confirm_password_et);
        sign_up_btn_iv = view.findViewById(R.id.sign_up_btn_iv);

        dialog = new SpotsDialog.Builder().setContext(context)
                .setCancelable(false).setTheme(R.style.CustomPhoto).build();
        firebaseAuth = FirebaseAuth.getInstance();

        sign_up_btn_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUpNewUser();
            }
        });

        return view;
    }

    private void signUpNewUser() {

        String email = email_et.getText().toString();
        String password = password_et.getText().toString();
        String confirmPassword = confirm_password_et.getText().toString();

        if ( !TextUtils.isEmpty(email) && !TextUtils.isEmpty(email)&& !TextUtils.isEmpty(email)){

            //Nothing is empty

            if (password.equals(confirmPassword)){

                // password matched
                dialog.show();

                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            //signUP Successful
                            dialog.dismiss();
                            FirebaseUser firebaseUser = task.getResult().getUser();
                            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()){
                                        //Verification Email Send
                                        Toast.makeText(context, "Registered Successfully"
                                                +" please verify your email", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            changeFragment(new UserSetUpFragment());
                        } else {
                            dialog.dismiss();
                            Toast.makeText(getActivity(), "" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
            else {
                Toast.makeText(context, "Password not matched", Toast.LENGTH_SHORT).show();
            }


        }
        else {
            Toast.makeText(context, "All Field Are Required", Toast.LENGTH_SHORT).show();
        }



    }

    void changeFragment(Fragment fragment){

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_layout,fragment);
        fragmentTransaction.commit();


    }

}
