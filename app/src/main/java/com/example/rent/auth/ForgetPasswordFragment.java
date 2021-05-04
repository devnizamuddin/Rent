package com.example.rent.auth;


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

import com.example.rent.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;


public class ForgetPasswordFragment extends Fragment {

    TextInputEditText email_et;
    ImageView send_btn_iv;

    FirebaseAuth firebaseAuth;

    public ForgetPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_forget_password, container, false);

        email_et = view.findViewById(R.id.email_et);
        send_btn_iv = view.findViewById(R.id.send_btn_iv);

        firebaseAuth = FirebaseAuth.getInstance();

        send_btn_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendPasswordRestRequest();
            }
        });

        return view;
    }

    private void sendPasswordRestRequest() {

        String email = email_et.getText().toString();

        if (!email.isEmpty()){
            //email not null
            firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        email_et.setText("");
                        changeFragment(new LoginFragment());
                        Toast.makeText(getContext(), "A password rest link send in your email", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(getContext(), ""+task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else {
            // email null
            Toast.makeText(getActivity(), "Please Enter your email first", Toast.LENGTH_SHORT).show();
        }


    }

    void changeFragment(Fragment fragment){

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_layout,fragment);
        fragmentTransaction.commit();

    }

}
