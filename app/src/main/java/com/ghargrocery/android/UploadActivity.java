package com.ghargrocery.android;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UploadActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText product_name, product_prize, product_weight;
    private Spinner product_category;
    private ImageView product_image_view;
    private Button choose_image,upload_product;
    private Uri image_url;
    private StorageTask uploadTask;
    DatabaseReference databaseRef;
    private List<String> categories;
    StorageReference storageRef;
    DatabaseReference categoryRef;
    private String URL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        
        //initialize
        
        product_name = (EditText) findViewById(R.id.upload_name);
        product_prize = (EditText) findViewById(R.id.upload_prize);
        product_weight = (EditText) findViewById(R.id.upload_weight);
        product_category = (Spinner) findViewById(R.id.upload_category);

        choose_image = (Button) findViewById(R.id.upload_choose_image);
        upload_product = (Button) findViewById(R.id.upload_upload_image);
        categoryRef = FirebaseDatabase.getInstance().getReference("categories");
        product_image_view = (ImageView) findViewById(R.id.upload_image_view);
        categories = new ArrayList<String>();
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        product_category.setAdapter(adapter);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        categoryRef = FirebaseDatabase.getInstance().getReference("categories");
        databaseRef = FirebaseDatabase.getInstance().getReference("products");
        storageRef = FirebaseStorage.getInstance().getReference("products");

        choose_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
        
        upload_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload_product();
            }
        });


        categoryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Set<String> set = new HashSet<String>();
                for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    set.add(postSnapshot.getKey().toString());
                }
                adapter.clear();
                adapter.addAll(set);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"problem occured",Toast.LENGTH_LONG).show();
            }
        });

    }
    private String getFileExtension(Uri image_url) {
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(image_url));
    }

    private void upload_product() {
        if(image_url != null){
            final StorageReference fileReference = storageRef.child(System.currentTimeMillis()+'.'+getFileExtension(image_url));
            uploadTask = fileReference.putFile(image_url).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressBar.setVisibility(View.INVISIBLE);
                    product_name.setText("");
                    product_weight.setText("");
                    product_prize.setText("");
                    product_image_view.setImageResource(R.drawable.product_background);
                    Toast.makeText(getApplicationContext(),"upload failure",Toast.LENGTH_LONG).show();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(),"upload Successful",Toast.LENGTH_LONG).show();
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            URL = uri.toString();
                            String uploadId = databaseRef.push().getKey();
                            Product product  = new Product(product_name.getText().toString().trim(),product_prize.getText().toString().trim(),product_weight.getText().toString().trim(),URL,product_category.getSelectedItem().toString(),uploadId,false);

                            databaseRef.child(uploadId).setValue(product);
                            product_name.setText("");
                            product_weight.setText("");
                            product_prize.setText("");
                            product_image_view.setImageResource(R.drawable.product_background);
                        }
                    });


                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    progressBar.setVisibility(View.VISIBLE);
                }
            });
        }
    }



    private void openFileChooser() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i,PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            image_url = data.getData();

            Glide.with(getApplicationContext()).load(image_url).into(product_image_view);
        }
    }
}