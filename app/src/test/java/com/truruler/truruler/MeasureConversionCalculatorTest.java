package com.truruler.truruler;


import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class MeasureConversionCalculatorTest {
    @Test
    public void ConversionInchToCM() throws Exception {
        assertEquals(25.4, 10*2.54, 0.001);
    }
    @Test
    public void ConversionCMtoInch() throws Exception {
        assertEquals(10, 25.4/2.54, 0.001);
    }
}
