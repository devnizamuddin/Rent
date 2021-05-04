package com.example.rent.screen.createads;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.rent.pojo.HomePoJo;
import com.example.rent.pojo.MyLocation;
import com.example.rent.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

import static android.app.Activity.RESULT_OK;


public class AddNewFragment extends Fragment {


    private Context context;

    //Location

    private FusedLocationProviderClient client;
    private LocationCallback callback;
    private LocationRequest request;
    private String lat;
    private String lon;

    public static final int REQUEST_LOCATION = 1;
    public static final int GALLERY_PICK_CODE_ONE = 1;
    public static final int GALLERY_PICK_CODE_TWO = 2;
    public static final int GALLERY_PICK_CODE_THREE = 3;

    FirebaseAuth firebaseAuth;
    private FirebaseStorage firebaseStorage;
    private DatabaseReference databaseReference;

    ArrayList<String> downloadUrls = new ArrayList<>();
    private String userId;

    EditText description_et, amount_et;
    Spinner area_name_sp;
    ImageView image_one, image_two, image_three, upload_img, add_location_img;
    RadioGroup price_type_rg;
    RadioButton per_night_rb, per_month_rb;
    TextView location_txt;

    ArrayAdapter<CharSequence> arrayAdapter;
    android.app.AlertDialog dialog;


    public AddNewFragment() {
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
        View view = inflater.inflate(R.layout.fragment_add_new, container, false);


        arrayAdapter = ArrayAdapter.
                createFromResource(context, R.array.location_array, android.R.layout.simple_list_item_1);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Spinner
        area_name_sp = view.findViewById(R.id.area_name_sp);
        area_name_sp.setAdapter(arrayAdapter);
        //EditText
        description_et = view.findViewById(R.id.description_et);
        amount_et = view.findViewById(R.id.amount_et);
        //Radio Group
        price_type_rg = view.findViewById(R.id.price_type_rg);
        per_night_rb = view.findViewById(R.id.per_night_rb);
        per_month_rb = view.findViewById(R.id.per_month_rb);
        //image
        image_one = view.findViewById(R.id.image_one);
        image_two = view.findViewById(R.id.image_two);
        image_three = view.findViewById(R.id.image_three);
        //btn
        upload_img = view.findViewById(R.id.upload_img);
        add_location_img = view.findViewById(R.id.add_location_img);
        location_txt = view.findViewById(R.id.location_txt);

        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getCurrentUser().getUid();

        dialog = new SpotsDialog.Builder().setContext(context)
                .setCancelable(false).setTheme(R.style.CustomPhoto).build();

        firebaseStorage = FirebaseStorage.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("House_Ad");


        per_month_rb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //createMonthPicker();
            }
        });

        add_location_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocation();
            }
        });

        image_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getImageFromGallery(GALLERY_PICK_CODE_ONE);
            }
        });

        image_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImageFromGallery(GALLERY_PICK_CODE_TWO);
            }
        });

        image_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImageFromGallery(GALLERY_PICK_CODE_THREE);
            }
        });

        upload_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadAd();
            }
        });


        //Location
        return view;
    }

    private void getLocation() {

        client = LocationServices.getFusedLocationProviderClient(context);
        callback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                for (Location location : locationResult.getLocations()) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    lat = String.valueOf(latitude);
                    lon = String.valueOf(longitude);
                    Toast.makeText(context, "Location Added", Toast.LENGTH_SHORT).show();
                    add_location_img.setImageResource(R.drawable.ic_location_black_24dp);
                    location_txt.setText("Location Added");
                }
            }
        };

        request = new LocationRequest()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setNumUpdates(1);

        if (ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION);
        } else {
        }
        client.requestLocationUpdates(request, callback, null);

    }

    private void uploadAd() {

        String areaName = area_name_sp.getSelectedItem().toString();
        if (areaName.equals("Please select your area")) {
            areaName = null;
        }
        String description = description_et.getText().toString();
        String amount = amount_et.getText().toString();
        int radioButtonId = price_type_rg.getCheckedRadioButtonId();
        RadioButton price_type_rv = getView().findViewById(radioButtonId);
        String priceType = price_type_rv.getText().toString();

        if (!TextUtils.isEmpty(areaName) && !TextUtils.isEmpty(description) && !TextUtils.isEmpty(amount) &&
                !TextUtils.isEmpty(priceType) && downloadUrls.size() > 0) {
            String id = databaseReference.push().getKey();
            HomePoJo homePoJo =
                    new HomePoJo(id, userId, areaName, description, priceType, amount, downloadUrls, new MyLocation(lat, lon));
            databaseReference.child(id).setValue(homePoJo).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if (task.isSuccessful()) {
                        Toast.makeText(context, "Uploaded", Toast.LENGTH_SHORT).show();
                        createAdUploadedView();

                    } else {
                        Toast.makeText(context, "" + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            });

        } else {
            Toast.makeText(context, "All field are required", Toast.LENGTH_SHORT).show();
        }

    }

    private void createAdUploadedView() {

        description_et.setText("");
        amount_et.setText("");
        price_type_rg.clearCheck();
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        image_one.setImageBitmap(null);
        image_two.setImageBitmap(null);
        image_three.setImageBitmap(null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Your ad is uploaded. It will be visible to other user from now");
        builder.setTitle("Success");
        builder.setNegativeButton("close", null);
        builder.show();

    }

    private void getImageFromGallery(final int GALLERY_PICK_CODE) {

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_PICK_CODE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GALLERY_PICK_CODE_ONE:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();

                    image_one.setImageURI(uri);

                    uploadImage(uri);
                }
                break;

            case GALLERY_PICK_CODE_TWO:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();

                    image_two.setImageURI(uri);
                    uploadImage(uri);
                }
                break;
            case GALLERY_PICK_CODE_THREE:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();

                    image_three.setImageURI(uri);
                    uploadImage(uri);
                }
                break;
        }
    }

    private void uploadImage(Uri uri) {

        dialog.show();
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
                    dialog.dismiss();
                    Uri downloadUri = task.getResult();
                    String downloadUrl = String.valueOf(downloadUri);
                    //  String photoId = databaseReference.push().getKey();
                    downloadUrls.add(downloadUrl);
                    Toast.makeText(context, "Image uploaded", Toast.LENGTH_SHORT).show();



                   /* photoDatabaseReference.child(photoId).setValue(imagePoJo).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show();

                            }
                        }
                    });*/
                } else {
                    // Handle failures
                    // ...
                    dialog.dismiss();
                    Toast.makeText(context, "" + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });

    }
}
