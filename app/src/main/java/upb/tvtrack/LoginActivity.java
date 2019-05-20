package upb.tvtrack;

import android.content.Intent;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;

public class LoginActivity extends AppCompatActivity {

    //private static final String HMAC_SHA_256_KEY = "SMDPROIECT";

    EditText email_edittext;
    EditText password_edittext;
    Button login_button;
    Button signup_button;

    //private KeyStore keystore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final UserDatabase udb = UserDatabase.getUserDatabase(this);

        email_edittext = findViewById(R.id.email_edittext);
        password_edittext = findViewById(R.id.password_edittext);
        login_button = findViewById(R.id.login_button);
        signup_button = findViewById(R.id.signup_button);

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

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEmpty(email_edittext) || isEmpty(password_edittext)) {
                    Toast.makeText(LoginActivity.this,
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

                int exists = udb.userDao().existsUser(email_hash, pass_hash);

                if (exists != 0) {
                    Intent intent = new Intent(LoginActivity.this, TVListActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this,
                            "User does not exist!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signup_intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(signup_intent);
            }
        });
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }
}
