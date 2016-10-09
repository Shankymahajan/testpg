package com.example.uddishverma.pg_app_beta;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by UddishVerma on 03/09/16.
 */
public class SignUpFrag extends Fragment {

    EditText userFirstName,userLastName, signupEmail, signupPassword, signupRepassword;
    Button btnSignup, btnGoogle, btnFacebook;
//    ImageView eye;

    ProgressDialog progressDialog;

    SweetAlertDialog pDialog;

    FirebaseAuth firebaseAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_signup, container, false);

        progressDialog = new ProgressDialog(getContext());

        Firebase.goOnline();
        Firebase.setAndroidContext(getContext());

        firebaseAuth = FirebaseAuth.getInstance();

        userFirstName = (EditText) view.findViewById(R.id.edittext_firstusername);
        userLastName = (EditText) view.findViewById(R.id.edittext_lastusername);
        signupEmail = (EditText) view.findViewById(R.id.edittext_email);
        signupPassword = (EditText) view.findViewById(R.id.edittext_password);
        signupRepassword = (EditText) view.findViewById(R.id.edittext_repassword);
        btnSignup = (Button) view.findViewById(R.id.btn_signup);
//        eye = (ImageView) view.findViewById(R.id.eye);
//        btnGoogle = (Button) view.findViewById(R.id.btn_google);
//        btnFacebook = (Button) view.findViewById(R.id.btn_fb);


        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUserFunction();
            }
        });
//
//        eye.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                switch (event.getAction()) {
//                    case MotionEvent.ACTION_DOWN:
//                        signupPassword.setInputType(InputType.TYPE_CLASS_TEXT);
//                        break;
//                    case MotionEvent.ACTION_UP:
//                        signupPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//                        break;
//                }
//                return true;
//            }
//        });

//        btnGoogle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
//                        .setTitleText("Oops...")
//                        .setContentText("Please Go To The Login Page!")
//                        .show();
//            }
//        });
//
//        btnFacebook.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new SweetAlertDialog(getContext(), SweetAlertDialog.ERROR_TYPE)
//                        .setTitleText("Oops...")
//                        .setContentText("Please Go To The Login Page!")
//                        .show();
//            }
//        });

        return view;
    }

    private void registerUserFunction() {

        String userFirstNameCheck = userFirstName.getText().toString().trim();
        String userLastNameCheck = userLastName.getText().toString().trim();
        String emailCheck = signupEmail.getText().toString().trim();
        String passwordCheck = signupPassword.getText().toString().trim();
        String repeatPasswordCheck = signupRepassword.getText().toString().trim();


        /**
         * Check the condition on username ,email ,password and re entered password.
         */

        //Checking for the null fields
        if (userFirstNameCheck.isEmpty() ) {
            userFirstName.setError("at least 3 characters");
            //Stopping further execution
            return;
        }

        if (userLastNameCheck.isEmpty() ){
           userLastName.setError("at least 3 characters");
            //Stopping further execution
            return;
        }

        if (emailCheck.isEmpty()
                || !Patterns.EMAIL_ADDRESS.matcher(emailCheck).matches()){
            signupEmail.setError("enter a valid email address");
            return;
        }


        if (passwordCheck.isEmpty()) {
            signupPassword.setError("between 4 and 15 alphanumeric characters");
            return;
        }

        if (repeatPasswordCheck.isEmpty()) {
            signupRepassword.setError("Both passwords should be same");
            return;
        }


        progressDialog.setMessage("Registering User...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(emailCheck, passwordCheck)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()

                            .setDisplayName(userFirstName.getText().toString() + " " + userLastName.getText().toString()).build();
                            user.updateProfile(profileUpdate)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            //start the activity which you want to open after the login in successful
                                            progressDialog.dismiss();
                                            Toast.makeText(getContext(), "Welcome!", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(getContext(), MainActivity.class));
                                        }
                                    });
                        } else
                            Toast.makeText(getContext(), "Could Not Register... Please Try Again!", Toast.LENGTH_SHORT).show();

                    }

                });
    }
}
