package com.example.newworldphotoeditor;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookActivity;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.UUID;

public class LoginFacebookActivity extends AppCompatActivity {
    private ProfilePictureView friendProfilePicture;
    private TextView txtUsernameFacebook;
    private LoginButton btnLoginFacebook;
    private CallbackManager callbackManager;
    static ShareDialog shareDialog;
    ShareLinkContent shareLinkContent;
    String name;
    Bitmap bitmap;
    public static int Select_Image = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_login_facebook);
        callbackManager = CallbackManager.Factory.create();
        init();
        btnLoginFacebook.setReadPermissions("public_profile");
        onLoginFacebook();
        shareDialog = new ShareDialog(LoginFacebookActivity.this);
    }

    public void onLoginFacebook() {
        btnLoginFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                btnLoginFacebook.setVisibility(View.INVISIBLE);
                txtUsernameFacebook.setVisibility(View.VISIBLE);
                result();
                Intent intent = getIntent();
                File root = Environment.getExternalStorageDirectory();
                String a = intent.getStringExtra("shareImage");

                Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.barak_obama_face);
                Log.e("image", String.valueOf(image));

                SharePhoto photo = new SharePhoto.Builder()
                        .setBitmap(image)
                        .build();
                SharePhotoContent content = new SharePhotoContent.Builder()
                        .addPhoto(photo)
                        .build();
                shareDialog.show(content);
                Log.e("cascascascc", intent.getStringExtra("shareImage"));

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    @Override
    protected void onStart() {
        LoginManager.getInstance().logOut();
        super.onStart();
    }

    private void result() {
        GraphRequest graphRequest = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                Log.e("JSON", response.getJSONObject().toString());
                try {
                    name = object.getString("name");
                    friendProfilePicture.setProfileId(Profile.getCurrentProfile().getId());
                    txtUsernameFacebook.setText(name);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        Bundle params = new Bundle();
        params.putString("fields", "name");
        graphRequest.setParameters(params);
        graphRequest.executeAsync();
    }

    public void init() {
        friendProfilePicture = (ProfilePictureView) findViewById(R.id.friendProfilePicture);
        txtUsernameFacebook = (TextView) findViewById(R.id.txt_username_facebook);
        btnLoginFacebook = (LoginButton) findViewById(R.id.btn_login_facebook);
        onLogout();
    }

    private void onLogout() {
        LoginManager.getInstance().logOut();
        txtUsernameFacebook.setText("");
        friendProfilePicture.setProfileId(null);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
