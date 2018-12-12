package it.pgp.permissionmanagementexample;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Toast;

public class MultiplePermissionsRequestActivity extends Activity {

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 0x11111) {
            if (grantResults.length == 0) { // request cancelled
                Toast.makeText(this, "Storage permissions denied", Toast.LENGTH_SHORT).show();
                finishAffinity();
                return;
            }

            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Storage permissions denied", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            Toast.makeText(this, "Storage permissions granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0x11111:
                Toast.makeText(this, "Nothing to do here, already handled in onRequestPermissionsResult", Toast.LENGTH_SHORT).show();
                break;
            case 0x22222:
                Toast.makeText(this, "System settings permission "+
                        (Settings.System.canWrite(this)?"granted":"denied"), Toast.LENGTH_SHORT).show();
                break;
            case 0x33333:
                Toast.makeText(this, "Overlay permission "+
                        (Settings.canDrawOverlays(this)?"granted":"denied"), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void requestStoragePermissions(View unused) {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                0x11111);
    }

    public void openSystemSettingsPermissionsManagement(View unused) {
        startActivityForResult(new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS),0x22222);
    }

    public void openOverlayPermissionsManagement(View unused) {
        startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION),0x33333);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_permissions_request);
    }
}
