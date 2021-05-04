package com.example.rent.auth;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.rent.pojo.UserInfoPoJo;
import com.example.rent.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserSetUpFragment extends Fragment {

    private Context context;
    private EditText name_et, email_et, phone_nbr_et,nid_number_et;
    private ImageView image_input_img;
    private Button submit_btn;
    public static final int GALLERY_PICK_CODE_PROFILE = 5;
    private String userId;
    private String downloadUrl;
    private DatabaseReference databaseReference;

    FirebaseAuth firebaseAuth;
    FirebaseStorage firebaseStorage;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public UserSetUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_set_up, container, false);
        image_input_img = view.findViewById(R.id.image_input_img);
        name_et = view.findViewById(R.id.name_et);
        email_et = view.findViewById(R.id.email_et);
        phone_nbr_et = view.findViewById(R.id.phone_nbr_et);
        submit_btn = view.findViewById(R.id.submit_btn);
        nid_number_et = view.findViewById(R.id.nid_number_et);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("UserInfo");
        userId = firebaseAuth.getCurrentUser().getUid();

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sendData();
            }
        });
        image_input_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImageFromGallery();
            }
        });

        return view;
    }

    private void sendData() {

        String name = name_et.getText().toString();
        String email = email_et.getText().toString();
        String phoneNumber = phone_nbr_et.getText().toString();
        String nidNumber = nid_number_et.getText().toString();

        if (!TextUtils.isEmpty(downloadUrl) && !TextUtils.isEmpty(name) && !TextUtils.isEmpty(email)
                && !TextUtils.isEmpty(phoneNumber)) {

            databaseReference.child(userId).setValue(new UserInfoPoJo(userId,name,email,phoneNumber,nidNumber,downloadUrl)).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(context, "Profile Updated", Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        changeFragment(new LoginFragment());
                    }
                    else {
                        Toast.makeText(context, ""+task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        else {
            Toast.makeText(context, "All Field's are required", Toast.LENGTH_SHORT).show();
        }

    }

    private void getImageFromGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_PICK_CODE_PROFILE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GALLERY_PICK_CODE_PROFILE:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();

                    image_input_img.setImageURI(uri);

                    uploadImage(uri);
                }
                break;

        }
    }

    private void uploadImage(Uri uri) {

        final StorageReference filePath = firebaseStorage.getReference().child("Photos").child(userId).child(uri.getLastPathSegment());
        filePath.putFile(uri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    Toast.makeText(context, "" + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }

                // Continue with the task to get the download URL
                return filePath.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    downloadUrl = String.valueOf(downloadUri);

                    Toast.makeText(context, "" + downloadUrl, Toast.LENGTH_SHORT).show();

                } else {
                    // Handle failures
                    // ...
                    Toast.makeText(context, "" + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();

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
