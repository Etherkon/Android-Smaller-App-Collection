
package com.ui.uitest;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.SdkSuppress;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.By;
import android.support.test.uiautomator.UiDevice;
import android.support.test.uiautomator.UiObject2;
import android.support.test.uiautomator.Until;
import android.test.suitebuilder.annotation.LargeTest;

import com.ui.uitest.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
@SdkSuppress(minSdkVersion = 23)
@LargeTest

public class UiTest {

    private static final String TARGET_PACKAGE =
            InstrumentationRegistry.getTargetContext().getPackageName();

    private static final int LAUNCH_TIMEOUT = 4000;
    private UiDevice mLaite;

    @Before
    public void startBlueprintActivityFromHomeScreen() {
		
        mLaite = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        mLaite.pressHome();

        final String launcherPackage = getLauncherPackageName();
        assertThat(launcherPackage, notNullValue());
        mLaite.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), LAUNCH_TIMEOUT);

        Context context = InstrumentationRegistry.getContext();
        final Intent intent = context.getPackageManager().getLaunchIntentForPackage(TARGET_PACKAGE);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);   
        context.startActivity(intent);
		
        mLaite.wait(Until.hasObject(By.pkg(TARGET_PACKAGE).depth(0)), LAUNCH_TIMEOUT);
    }

    @Test
    public void checkPreconditions() {
        assertThat(mLaite, notNullValue());
    }

    @Test
    public void findViewPerformActionAndCheckAssertion() {
        final String btnContentDescription = InstrumentationRegistry.getTargetContext()
                .getString(R.string.content_desc_hello_android_testing);
        mLaite.findObject(By.desc(btnContentDescription)).click();

        final String textViewResId = "text_view_rocks";
        UiObject2 androidRocksTextView = mLaite
                .wait(Until.findObject(By.res(TARGET_PACKAGE, textViewResId)), 300 );
        assertThat(androidRocksTextView, notNullValue());

        final String androidTestingRocksText = InstrumentationRegistry.getTargetContext()
                .getString(R.string.android_testing_rocks);
        assertThat(androidRocksTextView.getText(), is(equalTo(androidTestingRocksText)));
    }

    private String getLauncherPackageName() {
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);

        PackageManager pm = InstrumentationRegistry.getContext().getPackageManager();
        ResolveInfo resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfo.activityInfo.packageName;
    }

}
