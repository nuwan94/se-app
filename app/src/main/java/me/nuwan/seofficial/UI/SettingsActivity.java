package me.nuwan.seofficial.UI;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

import me.nuwan.seofficial.Fireabse.FirebaseDB;
import me.nuwan.seofficial.Model.Login;
import me.nuwan.seofficial.Model.User;
import me.nuwan.seofficial.R;

public class SettingsActivity extends AppCompatActivity {

    private StorageReference storageReference;
    private ImageView avatar;
    private ProgressDialog progressDialog;

    private static final int GALLERY_INTENT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        progressDialog = new ProgressDialog(this);
        storageReference = FirebaseStorage.getInstance().getReference().child("Avatars");

        Button selectImageButton = findViewById(R.id.settings_selectImage);
        avatar = findViewById(R.id.settings_avatar);

        Picasso.get().load(User.currentUser.getImg()).into(avatar);

        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            progressDialog.setMessage("Uploading...");
            progressDialog.show();

            final Uri uri = data.getData();

            Picasso.get().load(uri).into(avatar);

            avatar.setDrawingCacheEnabled(true);
            avatar.buildDrawingCache();
            Bitmap bitmap = avatar.getDrawingCache();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
            byte[] compressedAvatar = byteArrayOutputStream.toByteArray();

            final StorageReference uploaderRef = storageReference.child(User.currentUser.getSno());

            uploaderRef.putBytes(compressedAvatar).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    uploaderRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            User.currentUser.setImg(uri.toString());
                            FirebaseDB.getUserReference().child(User.currentUser.getSno()).setValue(User.currentUser);
                            progressDialog.dismiss();
                            Toast.makeText(SettingsActivity.this, "Profile picture updated.", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(SettingsActivity.this, "Error : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
}
