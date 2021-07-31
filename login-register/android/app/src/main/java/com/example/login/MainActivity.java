package com.example.login;


import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.example.login.tan.Tanserver;
import java.io.IOException;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        /* your host  */
        Tanserver tan = new Tanserver("tanserver.org", 2579);

        CardView login    = findViewById(R.id.login);
        CardView register = findViewById(R.id.register);

        login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                String username = ((EditText)findViewById(R.id.username)).getText().toString();
                String password = ((EditText)findViewById(R.id.password)).getText().toString();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Missing username or password.", Toast.LENGTH_SHORT).show();
                    return;
                }

                JSONObject jsonObject = new JSONObject();

                try {
                    jsonObject.put("username", username);
                    jsonObject.put("password", password);
                } catch (JSONException err) {
                    err.printStackTrace();
                }

                tan.getJSON("login", jsonObject.toString(), new Tanserver.Callback() {

                    @Override
                    public void onSuccess(String jsonString) {
                        /*
                         * {"status":0}: Login success.
                         * {"status":-1}: User could not be found.
                         */
                        try {
                            if (new JSONObject(jsonString).getInt("status") == 0) {

                                startActivity(new Intent(MainActivity.this, WelActivity.class)
                                              .putExtra("username", username));
                            } else {

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "User could not be found.", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } catch (JSONException err) {
                            err.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(IOException err) {
                        err.printStackTrace();
                    }
                });
            }
        });

        register.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                String username = ((EditText)findViewById(R.id.username)).getText().toString();
                String password = ((EditText)findViewById(R.id.password)).getText().toString();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Missing username or password.", Toast.LENGTH_SHORT).show();
                    return;
                }

                JSONObject jsonObject = new JSONObject();

                try {
                    jsonObject.put("username", username);
                    jsonObject.put("password", password);
                } catch (JSONException e) {
                }

                tan.getJSON("register", jsonObject.toString(), new Tanserver.Callback() {

                    @Override
                    public void onSuccess(String jsonString) {
                        /*
                         * {"status":0}: Registration success! Now you can log in.
                         * {"status":-1}: This username already exists.
                         */
                        try {
                            String  message;

                            if (new JSONObject(jsonString).getInt("status") == 0)
                                message = "Registration success! Now you can log in.";
                            else
                                message = "This username already exists.";

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (JSONException err) {
                            err.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(IOException err) {
                        err.printStackTrace();
                    }
                });
            }
        });
    }
}
