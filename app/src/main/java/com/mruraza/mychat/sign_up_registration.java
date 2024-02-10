package com.mruraza.mychat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class sign_up_registration extends AppCompatActivity {

    TextView login_button;
    Button signup_button ;
    EditText user_name,email_address,password_first_time,password_re_enter;
   CircleImageView profile_pic;
   FirebaseAuth auth;
   Uri iamgeuri;
   String IMAGEURI;

    String emailPatternn = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    FirebaseDatabase database;
    FirebaseStorage  storage;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_registration);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Tapai ko account bandai xa hai\n");
        progressDialog.setCancelable(false);
        getSupportActionBar().hide();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        auth=FirebaseAuth.getInstance();
        login_button = findViewById(R.id.textview_for_login_inregistration);
        signup_button = findViewById(R.id.button_for_signup_inregistration);
        user_name = findViewById(R.id.eeenter_user_name);
        email_address = findViewById(R.id.eeeemail_enter);
        password_first_time = findViewById(R.id.pppassword_enter);
        password_re_enter = findViewById(R.id.pppassword_reenter);
        profile_pic =  findViewById(R.id.profilerg);


            login_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(sign_up_registration.this,login.class);
                    startActivity(intent);
                    finish();
                }
            });

            profile_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,"Select Picture"),10);
                }
            });

                signup_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String re_username = user_name.getText().toString();
                        String re_email = email_address.getText().toString();
                        String password1 = password_first_time.getText().toString();
                        String password2nd = password_re_enter.getText().toString();
                        String status = "Chowk tira kta ho chowk tira mw 🙂";

                        if(TextUtils.isEmpty(re_username)){
                            progressDialog.dismiss();
                            user_name.setError("Please Enter UserName");
                            Toast.makeText(sign_up_registration.this, "UserName Can't be Empty!", Toast.LENGTH_SHORT).show();
                        }

                        else if(TextUtils.isEmpty(re_email)){
                            progressDialog.dismiss();
                            user_name.setError("Please Enter Email");
                            Toast.makeText(sign_up_registration.this, "Email Can't be Empty!", Toast.LENGTH_SHORT).show();
                        }

                        else if(TextUtils.isEmpty(password1)){
                            progressDialog.dismiss();
                            user_name.setError("Please Enter Password");
                            Toast.makeText(sign_up_registration.this, "Password Can't be Empty!", Toast.LENGTH_SHORT).show();
                        }

                       else if(TextUtils.isEmpty(password2nd)){
                            progressDialog.dismiss();
                            user_name.setError("Please Enter password");
                            Toast.makeText(sign_up_registration.this, "Password Can't be Empty!", Toast.LENGTH_SHORT).show();
                        }
                       else if(!re_email.matches(emailPatternn))
                        {
                            progressDialog.dismiss();
                            email_address.setError("Please Enter Valid Email address");
                            Toast.makeText(sign_up_registration.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                        } else if (password1.length()<6) {
                            progressDialog.dismiss();
                           password_first_time.setError("Too short Password");
                            password_first_time.setError("Password must me minimum of 6 character");
                        } else if (!password1.matches(password2nd)) {
                            progressDialog.dismiss();
                           password_re_enter.setError("2nd Password doesnt match with first");
                            Toast.makeText(sign_up_registration.this, "Password doesn't match ", Toast.LENGTH_SHORT).show();
                        }
                       else {
                           auth.createUserWithEmailAndPassword(re_email,password2nd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                               @Override
                               public void onComplete(@NonNull Task<AuthResult> task) {
                                   if(task.isSuccessful()){
                                       String id = task.getResult().getUser().getUid();
                                       DatabaseReference reference = database.getReference().child("User").child(id);
                                       StorageReference storageReference = storage.getReference().child("Upload").child(id);

                                       if(iamgeuri!=null)
                                       {
                                           storageReference.putFile(iamgeuri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                               @Override
                                               public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                                   if(task.isSuccessful())
                                                   {
                                                       storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                           @Override
                                                           public void onSuccess(Uri uri) {
                                                               IMAGEURI=uri.toString();
                                                               User user = new User(IMAGEURI,re_username,re_email,password1,id,status);
                                                               reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                   @Override
                                                                   public void onComplete(@NonNull Task<Void> task) {
                                                                       if(task.isSuccessful()){

                                                                           Dialog loadingdialog;
                                                                           loadingdialog = new Dialog(sign_up_registration.this);
                                                                           loadingdialog.setContentView(R.layout.progressbar);
                                                                           loadingdialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.roundedbtn));
                                                                           loadingdialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                                                                           loadingdialog.setCancelable(false);


                                                                           progressDialog.show();
                                                                           Intent intent = new Intent(sign_up_registration.this,MainActivity.class);
                                                                           startActivity(intent);
                                                                           finish();
                                                                       }else {
                                                                           Toast.makeText(sign_up_registration.this, "Error on Creating the user", Toast.LENGTH_SHORT).show();
                                                                       }
                                                                   }
                                                               });
                                                           }
                                                       });
                                                   }
                                               }
                                           });
                                       }else {
                                           String status =  "Chowk tira kta ho chowk tira mw 🙂 ";
                                           IMAGEURI = "https://firebasestorage.googleapis.com/v0/b/mychat-f44b4.appspot.com/o/default_man.png?alt=media&token=9126bc03-c4fb-4588-a898-ea1ce9a7baa5";
                                           User user = new User(IMAGEURI,re_username,re_email,password1,id,status);
                                           reference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                               @Override
                                               public void onComplete(@NonNull Task<Void> task) {
                                                   if(task.isSuccessful()){
                                                       progressDialog.show();
                                                       Intent intent = new Intent(sign_up_registration.this,MainActivity.class);
                                                       startActivity(intent);
                                                       finish();
                                                   }else {
                                                       Toast.makeText(sign_up_registration.this, "Error on Creating the user", Toast.LENGTH_SHORT).show();
                                                   }
                                               }
                                           });
                                       }
                                   }else {
                                       Toast.makeText(sign_up_registration.this, task.getException().getMessage()   , Toast.LENGTH_SHORT).show();
                                   }
                               }
                           });
                        }

                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==10)
        {
            if(data!=null)
            {
            iamgeuri = data.getData();
            profile_pic.setImageURI(iamgeuri);
            }
        }
    }
}