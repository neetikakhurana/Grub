package com.lecture.nitika.grub;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONObject;


public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener{

    private static final int RC_SIGN_IN = 976;
    View imgview;
    EditText email,pwd;
    Button login;
    private LoginButton btn_login;
    private static final String TAG="LoginActivity";
    private Context mcontext = null;
    private CallbackManager callbackManager = null;
    private GoogleApiClient mGoogleApiClient = null;
    private SignInButton btn_googleLogin = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        mcontext = this;
        init();

        googleSignIn();
        /*Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.vegetables);
        Bitmap blurredBitmap = BlurBuilder.blur( getApplicationContext(), largeIcon );
        view.setBackground( new BitmapDrawable( getResources(), blurredBitmap ) );
        TextView c1 = (TextView)findViewById(R.id.caption1);
        TextView c2 = (TextView)findViewById(R.id.caption2);

        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Cookit-Regular.ttf");

        c1.setTypeface(custom_font);
        c2.setTypeface(custom_font);*/
    }

    /**
     * @author: chetan
     * @description: Configure sign-in to request the user's ID, email address, and basic
     * profile. ID and basic profile are included in DEFAULT_SIGN_IN.
     */
    private void googleSignIn() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }


    /***
     * @author: tchetan1
     * @description: happens only once in oncreate()
     * so write all the things which has to happen only once
     */
    private void init() {
        imgview=findViewById(R.id.image);
        email=(EditText)findViewById(R.id.email);
        pwd=(EditText)findViewById(R.id.pwd);
        login=(Button)findViewById(R.id.loginButton);
        FacebookSdk.sdkInitialize(getApplicationContext());
        btn_login = (LoginButton) findViewById(R.id.btn_login);
        email.setOnClickListener(this);
        login.setOnClickListener(this);

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.vegetables);
        Bitmap blurredBitmap = BlurBuilder.blur( getApplicationContext(), largeIcon );
        imgview.setBackground( new BitmapDrawable( getResources(), blurredBitmap ) );

        //btn_login.setReadPermissions("email");
        callbackManager = CallbackManager.Factory.create();
        btn_login.registerCallback(callbackManager,fbCallback);

        //google sign in
        btn_googleLogin = (SignInButton) findViewById(R.id.btn_googleLogin);
        //btn_googleLogin.setSize(SignInButton.SIZE_STANDARD);
        btn_googleLogin.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.email:
                email.getText().clear();
                break;
            case R.id.loginButton:
                if(email.getText().toString().equalsIgnoreCase("") && pwd.getText().toString().equalsIgnoreCase("")){
                    email.setError("Please enter username/email");//it gives user to info message //use any one //
                    pwd.setError("Please enter password");
                }
                else if(pwd.getText().toString().equalsIgnoreCase("")){
                    pwd.setError("Please enter password");
                }
                else if(email.getText().toString().equalsIgnoreCase("")){
                    email.setError("Please enter username/email");//it gives user to info message //use any one //

                }
                else{
                    Log.i(TAG,"going to next");
                    Intent i = new Intent(this, MainPageActivity.class);
                    i.putExtra("username", email.getText().toString());
                    startActivity(i);
                }
                break;
            case R.id.btn_googleLogin:
                signInWithGoogle();
                break;
        }
    }

    private void signInWithGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    FacebookCallback<LoginResult> fbCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Log.d(TAG,"in success");
            GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject object, GraphResponse response) {
                    try{
                        Log.d(TAG,"object info "+object.toString());

                        String login_id = object.getString("id");
                        String login_name = object.getString("name");
                        String login_email = object.getString("email");

                        //loading the user information to the shared preferences
                        SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(mcontext);

                        if(login_id!= null) {
                            spref.edit().putString(Constants.user_id, login_id).apply();
                        }
                        if(login_name!= null) {
                            spref.edit().putString(Constants.user_name, login_name).apply();
                        }
                        if(login_email!= null) {
                            spref.edit().putString(Constants.user_email, login_email).apply();
                        }

                        spref.edit().putBoolean(Constants.isLoggedIn,true).apply();
                        spref.edit().putString(Constants.loggedWith,Constants.Facebook);

                        startNextActivity();

                    }catch(Exception ex){
                        Log.d(TAG,"Exception in fbcallback oncompleted method: "+ex.getLocalizedMessage());
                    }

                }
            });

            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,email,gender");
            request.setParameters(parameters);
            request.executeAsync();
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException error) {

        }
    } ;

    /***
     * Starts the next activity
     */
    private void startNextActivity() {

        //starting the next activity
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(mcontext,MainPageActivity.class);
                startActivity(intent);
                LoginActivity.this.finish(); // finishing this activity
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }

    }

    private void handleSignInResult(GoogleSignInResult result) {

        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.d(TAG,acct.getDisplayName());

            String login_id = acct.getId();
            String login_name = acct.getDisplayName()+" "+acct.getFamilyName();
            String login_email =acct.getEmail();

            //loading the user information to the shared preferences
            SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(mcontext);

            if(login_id!= null) {
                spref.edit().putString(Constants.user_id, login_id).apply();
            }
            if(login_name!= null) {
                spref.edit().putString(Constants.user_name, login_name).apply();
            }
            if(login_email!= null) {
                spref.edit().putString(Constants.user_email, login_email).apply();
            }

            spref.edit().putBoolean(Constants.isLoggedIn,true).apply();
            spref.edit().putString(Constants.loggedWith,Constants.Google);

            startNextActivity();

        } else {
            // Signed out, show unauthenticated UI
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }
}
