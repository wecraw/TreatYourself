package com.wecraw.treatyourself.activities;

import android.content.pm.PackageInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.wecraw.treatyourself.BuildConfig;
import com.wecraw.treatyourself.R;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutActivity extends AppCompatActivity {

    //NO XML FOR THIS ACTIVITY STYLE DEFINED IN CLASS

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Element versionElement = new Element();
        versionElement.setTitle("Version "+ BuildConfig.VERSION_NAME);

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.ty_about_logo)
                .setDescription("a Productivity Booster by Will Crawford")
                .addItem(versionElement)
                .addEmail("wecraw.industries@gmail.com")
                .create();

        setContentView(aboutPage);


    }

}
