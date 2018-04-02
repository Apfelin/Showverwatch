package upb.tvtrack;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class LoginScreenActivity extends Activity {

    public static final String EXTRA_MESSAGE = "upb.tvtrack.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
    }

    public void loginPress(View view) {

        Intent intent = new Intent(this, TVListActivity.class);
        EditText email = (EditText) findViewById(R.id.email_text);
        String message = email.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void signupPress(View view) {

        Intent intent = new Intent(this, SignUpActivity.class);
        startActivities(intent);
    }
}
