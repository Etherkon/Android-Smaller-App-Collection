package com.service.test;

import android.content.Intent;
import android.os.IBinder;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.MediumTest;
import android.support.test.rule.ServiceTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeoutException;

import static org.hamcrest.CoreMatchers.any;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


@MediumTest
@RunWith(AndroidJUnit4.class)
public class ServiceTest {
    @Rule
    public final ServiceTestRule mServiceRule = new ServiceTestRule();

    @Test
    public void testWithBoundService() throws TimeoutException {
		
        Intent serviceIntent = new Intent(InstrumentationRegistry.getTargetContext(), LocalService.class);
        serviceIntent.putExtra(LocalService.SEED_KEY, 42L);
		
        IBinder binder = mServiceRule.bindService(serviceIntent);
        LocalService service = ((LocalService.LocalBinder) binder).getService();
		
        assertThat(service.getRandomInt(), is(any(Integer.class)));
    }
}
