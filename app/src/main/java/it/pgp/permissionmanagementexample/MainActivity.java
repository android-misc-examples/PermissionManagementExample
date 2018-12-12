package it.pgp.permissionmanagementexample;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends Activity {

    public void requestDangerousPermissions() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                0x11111);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0x11111) {
            if (grantResults.length == 0) { // request cancelled
                Toast.makeText(this, "Permissions denied", Toast.LENGTH_SHORT).show();
                finishAffinity();
                return;
            }

            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Permissions denied", Toast.LENGTH_SHORT).show();
                    finishAffinity();
                    return;
                }
            }

            Toast.makeText(this, "Permissions granted", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this,MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView t = findViewById(R.id.textView);
        Button b = findViewById(R.id.button);

        b.setOnClickListener(v-> requestDangerousPermissions());

        try {
            File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
            StringBuilder ss = new StringBuilder();
            for (File ff : f.listFiles()) {
                ss.append(ff.getAbsolutePath());
                ss.append("\n");
            }
            t.setText(ss.toString());
        }
        catch (Exception e) {
            e.printStackTrace();
            t.setText("Cannot list dir content, please enable write storage permissions");
        }
    }
}
