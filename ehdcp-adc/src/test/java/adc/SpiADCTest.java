package adc;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Dawei on 9/16/2016.
 */
public class SpiADCTest {

    private SpiADC adc = new SpiADC(null);

    @Test
    public void setPowerMode() throws Exception {
        adc.resetControlWord();
        adc.setPowerMode(SpiADC.PowerMode.NORMAL);
        assertEquals(3, adc.getControlHigh());
    }

    @Test
    public void setSequenceMode() throws Exception {
        adc.resetControlWord();
        adc.setSequenceMode(SpiADC.SequenceMode.NONE);
        assertEquals(0, adc.getControlHigh());
        assertEquals(0, adc.getControlLow());
        adc.resetControlWord();
        adc.setSequenceMode(SpiADC.SequenceMode.CONTINUOUS);
        assertEquals(64, adc.getControlHigh());
        assertEquals(-128, adc.getControlLow());
        adc.resetControlWord();
        adc.setSequenceMode(SpiADC.SequenceMode.NON_INTERRUPT);
        assertEquals(64, adc.getControlHigh());
        assertEquals(0, adc.getControlLow());
        adc.resetControlWord();
        adc.setSequenceMode(SpiADC.SequenceMode.SHADOW);
        assertEquals(0, adc.getControlHigh());
        assertEquals(-128, adc.getControlLow());
    }

    @Test
    public void setRange() throws Exception {
        adc.resetControlWord();
        adc.setRange(SpiADC.Range.FULL);
        assertEquals(0, adc.getControlLow());
        adc.resetControlWord();
        adc.setRange(SpiADC.Range.HALF);
        assertEquals(32, adc.getControlLow());
    }

    @Test
    public void setCoding() throws Exception {
        adc.resetControlWord();
        adc.setCoding(SpiADC.Coding.COMPLEMENT);
        assertEquals(0, adc.getControlLow());
        adc.resetControlWord();
        adc.setCoding(SpiADC.Coding.STRAIGHT);
        assertEquals(16, adc.getControlLow());
    }

    @Test
    public void setState() throws Exception {
        adc.resetControlWord();
        adc.setState(SpiADC.State.TRI);
        assertEquals(0, adc.getControlLow());
        adc.resetControlWord();
        adc.setState(SpiADC.State.WEAK);
        assertEquals(64, adc.getControlLow());
    }

    @Test
    public void setDefault() throws Exception {
        adc.resetControlWord();
        adc.setDefault();
        assertEquals((byte)0b10000011, adc.getControlHigh());
        assertEquals((byte)0b00010000, adc.getControlLow());
    }

}