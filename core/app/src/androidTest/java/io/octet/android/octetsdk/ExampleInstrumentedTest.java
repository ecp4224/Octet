package io.octet.android.octetsdk;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import io.octet.android.octetsdk.merchant.EthereumLink;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("io.octet.android.octetsdk", appContext.getPackageName());
    }

    @Test
    public void testLink() {
        Context appContext = InstrumentationRegistry.getTargetContext();

        EthereumLink link = EthereumLink.requestLink(appContext);

        link.defaultAccount(new ResultCallback<AccountInfo>() {
            @Override
            public void completed(AccountInfo result) {

            }
        });

        AccountInfo info = link.defaultAccount();
    }
}
