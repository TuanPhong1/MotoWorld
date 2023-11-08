package nhom7.fpoly.motoworld.Onboarding;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import nhom7.fpoly.motoworld.MainActivity;
import nhom7.fpoly.motoworld.R;

public class GetStarted extends AppCompatActivity {
    Button startButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        startButton = findViewById(R.id.startButton);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(GetStarted.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}