package id.miehasiswa.game.catchthebutterfly;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.SeekBar;

/**
 * Created by Ghiffari azmy on 08/04/2016.
 */
public class SettingActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);

        SharedPreferences sp = getSharedPreferences("id.miehasiswa.game.catchthebutterfly", MODE_PRIVATE);

        SeekBar sBVolume = (SeekBar) findViewById(R.id.volume_bar);
        sBVolume.setProgress(sp.getInt("volume", 100));

        CheckBox cBMute = (CheckBox) findViewById(R.id.cBMute);
        cBMute.setChecked(sp.getBoolean("mute", false));
    }

    public void bApply(View v) {
        SharedPreferences.Editor ed = getSharedPreferences("id.miehasiswa.game.catchthebutterfly", MODE_PRIVATE).edit();
        SeekBar sBVolume = (SeekBar) findViewById(R.id.volume_bar);
        CheckBox cBMute = (CheckBox) findViewById(R.id.cBMute);

        ed.putInt("volume", sBVolume.getProgress());
        ed.putBoolean("mute", cBMute.isChecked());
        ed.commit();

        startActivity(new Intent(SettingActivity.this, MainActivity.class));
        finish();
    }

    public void bDefault(View v) {
        SeekBar sBVolume = (SeekBar) findViewById(R.id.volume_bar);
        CheckBox cBMute = (CheckBox) findViewById(R.id.cBMute);

        sBVolume.setProgress(100);
        cBMute.setChecked(false);
    }

    @Override
    public void onBackPressed() {
        final Button bApply = (Button) findViewById(R.id.bApply);
        SeekBar sBVolume = (SeekBar) findViewById(R.id.volume_bar);
        CheckBox cBMute = (CheckBox) findViewById(R.id.cBMute);
        SharedPreferences sp = getSharedPreferences("id.miehasiswa.game.catchthebutterfly", MODE_PRIVATE);

        if (sp.getInt("volume", 100) != sBVolume.getProgress() || sp.getBoolean("mute", false) != cBMute.isChecked()) {
            AlertDialog.Builder back = new AlertDialog.Builder(SettingActivity.this);
            back.setTitle("Unsaved Changes");
            back.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    bApply.performClick();
                }
            });
            back.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(SettingActivity.this, MainActivity.class));
                    finish();
                }
            });
            back.show();
        } else {
            startActivity(new Intent(SettingActivity.this, MainActivity.class));
            finish();
        }
    }
}
