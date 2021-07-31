package com.example.login;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.cardview.widget.CardView;


public class WelActivity extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);

        String username = getIntent().getStringExtra("username");
        TextView text = findViewById(R.id.textView);
        text.setText("Hi, " + username);

        CardView logout = findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(WelActivity.this,
                                          MainActivity.class));
            }
        });
    }
}
