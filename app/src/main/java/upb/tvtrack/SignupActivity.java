package upb.tvtrack;

import android.content.Intent;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "SMD DEBUG TAG";
    //private static final String HMAC_SHA_256_KEY = "SMDPROIECT";

    EditText email_edittext;
    EditText password_edittext;
    EditText pass_confirm_edittext;
    Button signup_confirm_button;

    //private KeyStore keystore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        final UserDatabase udb = UserDatabase.getUserDatabase(this);

        email_edittext = findViewById(R.id.signup_email_edittext);
        password_edittext = findViewById(R.id.signup_password1_edittext);
        pass_confirm_edittext = findViewById(R.id.signup_password2_edittext);
        signup_confirm_button = findViewById(R.id.signup_confirm_button);

        /*try {
            keystore = KeyStore.getInstance("AndroidKeyStore");
            keystore.load(null);

            if (!keystore.containsAlias(HMAC_SHA_256_KEY)) {
                KeyGenerator kpgen = KeyGenerator.getInstance("HmacSHA256", "AndroidKeyStore");
                KeyGenParameterSpec kgenspec = new KeyGenParameterSpec
                        .Builder(HMAC_SHA_256_KEY, KeyProperties.PURPOSE_SIGN | KeyProperties.PURPOSE_VERIFY)
                        .build();
                kpgen.init(kgenspec);
                kpgen.generateKey();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        signup_confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG, "email edittext: " + email_edittext.getText().toString());
                Log.d(TAG, "signup pass 1: " + password_edittext.getText().toString());
                Log.d(TAG, "signup pass 2: " + pass_confirm_edittext.getText().toString());

                if (isEmpty(email_edittext) || isEmpty(password_edittext) ||
                        isEmpty(pass_confirm_edittext) || (!password_edittext.getText().toString().equals(
                                pass_confirm_edittext.getText().toString()))) {
                    Toast.makeText(SignupActivity.this,
                            "You need to specify some data",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                byte[] email_hash = null;
                byte[] pass_hash = null;

                /*try {
                    Mac mac = Mac.getInstance("HmacSHA256");
                    mac.init(keystore.getKey(HMAC_SHA_256_KEY, null));
                    email_hash = mac.doFinal(email_edittext.getText().toString().getBytes());
                    pass_hash = mac.doFinal(password_edittext.getText().toString().getBytes());
                } catch (Exception e) {
                    e.printStackTrace();
                }*/

                MessageDigest digest;
                try
                {
                    digest = MessageDigest.getInstance("MD5");
                    digest.update(email_edittext.getText().toString().getBytes(Charset.forName("US-ASCII")),0,
                            email_edittext.getText().toString().length());
                    email_hash = digest.digest();
                    digest.update(password_edittext.getText().toString().getBytes(Charset.forName("US-ASCII")),0,
                            password_edittext.getText().toString().length());
                    pass_hash = digest.digest();
                }
                catch (NoSuchAlgorithmException e)
                {
                    e.printStackTrace();
                }

                UserEntity user = new UserEntity();
                user.setEmail_hash(email_hash);
                user.setPassword_hash(pass_hash);
                udb.userDao().insertAll(user);

                Toast.makeText(SignupActivity.this,
                        "New user has been created!",
                        Toast.LENGTH_SHORT).show();

                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        UserDatabase.destroyInstance();
        super.onDestroy();
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }
}
