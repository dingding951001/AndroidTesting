package pro.axonomy.www.kyc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import pro.axonomy.www.R;

public class KYCManualStep1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_kyc_step1_cn);
    }

    public void manualVerify2(View view) {
        Intent kycManualStep2Intent = new Intent(this, KYCManualStep2.class);
        startActivity(kycManualStep2Intent);
    }

}
