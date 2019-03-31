package pro.axonomy.www.kyc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import pro.axonomy.www.R;

/**
 * Created by xingyuanding on 3/30/19.
 */

public class KYCLanding extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_kyc_landing_cn);
    }

    public void speedVerify1(View view) {
        Intent kycSpeedIntent = new Intent(this, KYCSpeed.class);
        startActivity(kycSpeedIntent);
    }

    public void manualVerify1(View view) {
        Intent kycManualStep1Intent = new Intent(this, KYCManualStep1.class);
        startActivity(kycManualStep1Intent);
    }

}
