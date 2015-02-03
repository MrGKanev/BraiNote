package org.brainote.android.utils;

import android.test.InstrumentationTestCase;

import org.junit.Test;

import java.io.IOException;

import org.brainote.OmniNotes;
import org.brainote.utils.ConnectionManager;
import org.brainote.utils.GeocodeHelper;

public class GeocodeHelperTest extends InstrumentationTestCase {

    private final Double LAT = 43.799328;
    private final Double LON = 11.171552;

    @Test
    public void testGetAddressFromCoordinates() throws IOException {
        if (ConnectionManager.internetAvailable(OmniNotes.getAppContext())) {
            String address = GeocodeHelper.getAddressFromCoordinates(OmniNotes.getAppContext(), LAT, LON);
            assertTrue(address.length() > 0);
        }
    }
}