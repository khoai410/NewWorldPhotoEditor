package com.example.newworldphotoeditor;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONObject;

import java.util.Arrays;

public class LoginFacebookActivity extends AppCompatActivity {
    private ProfilePictureView friendProfilePicture;
    private TextView txtUsernameFacebook;
    private LoginButton btnLoginFacebook;
    private CallbackManager callbackManager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager=CallbackManager.Factory.create();

        setContentView(R.layout.activity_login_facebook);
        init();
        btnLoginFacebook.setReadPermissions(Arrays.asList("public_profile","email"));
        onLoginFacebook();
    }
    public void onLoginFacebook(){
        btnLoginFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                result();
btnLoginFacebook.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    private void result() {
        GraphRequest graphRequest=GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                Log.e("JSON",response.getJSONObject().toString());
            }
        });
        Bundle params=new Bundle();
        params.putString("Halu","name");
        graphRequest.setParameters(params);
        graphRequest.executeAsync();
    }

    public void init(){
        friendProfilePicture = (ProfilePictureView) findViewById(R.id.friendProfilePicture);
        txtUsernameFacebook = (TextView) findViewById(R.id.txt_username_facebook);
        btnLoginFacebook = (LoginButton) findViewById(R.id.btn_login_facebook);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
