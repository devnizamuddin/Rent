package com.example.rent.Navigation;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.example.rent.MainActivity;
import com.example.rent.PoJo.UserInfoPoJo;
import com.example.rent.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment implements PopupMenu.OnMenuItemClickListener {


    private Context context;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private String userId;

    private TextView name_tv, email_tv, phone_tv;
    private ImageView popup_menu;
    private Button change_photo_btn, edit_btn;
    UserInfoPoJo userInfoPoJo;
    public static final int GALLERY_PICK_CODE_PROFILE = 1;

    CircleImageView profile_img;

    FirebaseStorage firebaseStorage;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public ProfileFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        profile_img = view.findViewById(R.id.profile_image);
        change_photo_btn = view.findViewById(R.id.change_photo_btn);
        edit_btn = view.findViewById(R.id.edit_btn);
        name_tv = view.findViewById(R.id.name_tv);
        email_tv = view.findViewById(R.id.email_tv);
        phone_tv = view.findViewById(R.id.phone_tv);
        popup_menu = view.findViewById(R.id.popup_menu);

        databaseReference = FirebaseDatabase.getInstance().getReference("UserInfo");
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();

        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editProfileInformation();


            }
        });

        change_photo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeProfilePicture();
            }
        });

        setLayoutAsUser();


        popup_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopup(view);
            }
        });

        return view;
    }

    private void changeProfilePicture() {
        getImageFromGallery();
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
                    String downloadUrl = String.valueOf(downloadUri);
                    databaseReference.child(userId).child("profileImageUrl").setValue(downloadUrl).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()){
                                Toast.makeText(context, "Profile picture updated", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    // Handle failures
                    // ...
                    Toast.makeText(context, "" + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void editProfileInformation() {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.change_proifile_info, null);
        builder.setView(dialogView);
        final EditText name_et = dialogView.findViewById(R.id.name_et);
        final EditText email_et = dialogView.findViewById(R.id.email_et);
        final EditText phone_et = dialogView.findViewById(R.id.phone_et);
        final Button submit_btn = dialogView.findViewById(R.id.submit_btn);
        final Button cancel_btn = dialogView.findViewById(R.id.cancel_btn);

        name_et.setText(userInfoPoJo.getName());
        email_et.setText(userInfoPoJo.getEmail());
        phone_et.setText(userInfoPoJo.getPhoneNumber());

        final AlertDialog alertDialog = builder.show();

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = name_et.getText().toString();
                String email = email_et.getText().toString();
                String phone = phone_et.getText().toString();
                String nidNumber = userInfoPoJo.getNidNumber();
                String image = userInfoPoJo.getProfileImageUrl();

                UserInfoPoJo userInfoPoJo1 = new UserInfoPoJo(userId,name,email,phone,nidNumber,image);

                databaseReference.child(userId).setValue(userInfoPoJo1).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()){

                            Toast.makeText(context, "Profile Updated", Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                        }
                        else {
                            Toast.makeText(context, ""+task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

            }
        });
    }

    private void setLayoutAsUser() {

        databaseReference.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    userInfoPoJo = dataSnapshot.getValue(UserInfoPoJo.class);
                    setUpLayout(userInfoPoJo);
                } else {
                    Toast.makeText(context, "Not exist", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setUpLayout(UserInfoPoJo userInfoPoJo) {

        name_tv.setText(userInfoPoJo.getName());
        phone_tv.setText(userInfoPoJo.getPhoneNumber());
        email_tv.setText(userInfoPoJo.getEmail());
        Uri uri = Uri.parse(userInfoPoJo.getProfileImageUrl());
        Picasso.get().load(uri).into(profile_img);

    }

    private void showPopup(View view) {


        PopupMenu popupMenu = new PopupMenu(context, view);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.show();

    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {

        switch (menuItem.getItemId()) {

            case R.id.logout:
                firebaseAuth.signOut();
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
                break;
            case R.id.help:
                break;
            case R.id.about_us:
                break;


        }

        return false;
    }
    void changeFragment(Fragment fragment) {

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.test_container, fragment);
        fragmentTransaction.commit();

    }
}
