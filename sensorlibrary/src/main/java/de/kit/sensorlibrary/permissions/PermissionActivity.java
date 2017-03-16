package de.kit.sensorlibrary.permissions;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

/**
 * Created by Robert Zetzsche on 16.03.2017.
 */

public class PermissionActivity extends AppCompatActivity {
    private static int PERMISSION_CODE = 516;
    private Messenger messenger;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        if (getIntent() != null) {
            if (getIntent().hasExtra("permissions")) {
                ActivityCompat.requestPermissions(this, getIntent().getStringArrayExtra("permissions"), PERMISSION_CODE);
            }
            if (getIntent().hasExtra("handler")) {
                messenger = getIntent().getParcelableExtra("handler");
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean granted = true;
        for (int i = 0; i < permissions.length; i++) {
            granted &= grantResults[i] == PackageManager.PERMISSION_GRANTED;
        }
        Message message = Message.obtain();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isGranted", granted);
        message.setData(bundle);
        try {
            messenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        finish();
    }
}
