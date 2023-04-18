package com.example.beproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.example.beproject.models.User;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class AuthenticationActivity extends AppCompatActivity {
    EditText PersonName, EmailAddress, Password;
    Button signUpButton;
    TextView alreadyUser;
    ImageView eye;
    AwesomeValidation mAwesomeValidation;
    Activity activity;
    SignInButton login_with_google;
    ActivityResultLauncher<Integer> launcher;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        this.getWindow().setStatusBarColor(this.getResources().getColor(R.color.white));
        FirebaseAuth mAuth;
        activity = this;
        initViews();



        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(activity, MainActivity.class));
            finish();
        }


        alreadyUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity, LoginActivity.class));
                finish();

            }
        });







        mAwesomeValidation =new AwesomeValidation(ValidationStyle.BASIC);
        AwesomeValidation.disableAutoFocusOnFirstFailure();
        mAwesomeValidation.addValidation(activity, R.id.PersonName, RegexTemplate.NOT_EMPTY, R.string.err_name);
        mAwesomeValidation.addValidation(activity, R.id.EmailAddress, android.util.Patterns.EMAIL_ADDRESS, R.string.err_email);
        mAwesomeValidation.addValidation(activity, R.id.Password,RegexTemplate.NOT_EMPTY, R.string.err_password);


        signUpButton.setOnClickListener(v->{
            //Click handle on sign up
            if(mAwesomeValidation.validate()){
                String fullName = PersonName.getText().toString();
                String email = EmailAddress.getText().toString();
                String password = Password.getText().toString();
                signUpWithEmailAndPassword(fullName,email,password);
            }
        });



        // Configure Google Sign In
        login_with_google = findViewById(R.id.login_with_google);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)

                .requestIdToken("997102662062-d5rjhoh4vfj3s5l2kq8nsq33fupmvi8b.apps.googleusercontent.com")
                .requestEmail()
                .build();
        GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(this,gso);
        googleSignInClient.signOut();

        GoogleSignInAccount googleSignInAccount =GoogleSignIn.getLastSignedInAccount(this);
        ActivityResultLauncher<Intent> activityResultLauncher=registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());

                handleSignInTask(task);
            }
        });
        login_with_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent= googleSignInClient.getSignInIntent();
                activityResultLauncher.launch(signInIntent);
            }
        });


        //end of onCreate
    }
    private void handleSignInTask(Task<GoogleSignInAccount> task){

        try{

            GoogleSignInAccount account = task.getResult(ApiException.class);
            // final String getFullname = account.getDisplayName();
            //  final String getEmail = account.getEmail();
            //  final Uri getPhotoUrl= account.getPhotoUrl();
            startActivity(new Intent(AuthenticationActivity.this,MainActivity.class));
            Toast.makeText(activity, "Signed In successfully", Toast.LENGTH_SHORT).show();
            finish();



        } catch(ApiException e){
            e.printStackTrace();
            Toast.makeText(activity, "SignIn failed", Toast.LENGTH_SHORT).show();
        }


    }



    private void signUpWithEmailAndPassword(String fullName, String email, String password) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    FirebaseUser firebaseUser= task.getResult().getUser();
                    User user=new User(fullName,email,password);
                    saveUserDetailsInFireStore(user,firebaseUser);

                } else {

                    showToast(Objects.requireNonNull(task.getException()).getMessage());
                }
            }
        });
    }

    private void saveUserDetailsInFireStore(User user, FirebaseUser firebaseUser) {
        FirebaseFirestore.getInstance().collection("USERS").document(Objects.requireNonNull(firebaseUser).getUid()).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    startActivity(new Intent(activity, MainActivity.class));
                    Toast.makeText(activity, "Account created successfully", Toast.LENGTH_SHORT).show();

                    finish();
                } else {
                    showToast(task.getException().getMessage());
                }
            }
        });
    }

    private void showToast(String message){
        Toast.makeText(activity, ""+message, Toast.LENGTH_SHORT).show();
    }



    private void initViews() {
        PersonName = findViewById(R.id.PersonName);
        EmailAddress = findViewById(R.id.EmailAddress);
        Password = findViewById(R.id.Password);
        signUpButton = findViewById(R.id.signUpButton);
        alreadyUser = findViewById(R.id.alreadyUser);

    }
}
