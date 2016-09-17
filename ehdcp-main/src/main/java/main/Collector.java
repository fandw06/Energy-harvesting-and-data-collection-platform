package main;

import adc.SpiADC;
import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiDevice;
import com.pi4j.io.spi.SpiFactory;
import java.io.IOException;

public class Collector {

    private SpiDevice spi;
    private SpiADC adc;
    private static final int INTERVAL = 1000;

    public Collector() {
        try {
            // Initialize a spi device.
            spi = SpiFactory.getInstance(SpiChannel.CS0,
                    SpiDevice.DEFAULT_SPI_SPEED, // default spi speed 1 MHz
                    SpiDevice.DEFAULT_SPI_MODE); // default spi mode 0
            // Initialize a spiadc device.
            adc = new SpiADC(spi);
            adc.setDefault();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        System.out.println("--------------------------------------Data Collector--------------------------------------");
        for (int i = 0; i < 16; i++) {
            System.out.print(String.format(" | %04d ", i));
        }
        System.out.println();
        while(true) {
            try {
                double value[] = adc.readAll();
                for (int i = 0; i < 16; i++) {
                    System.out.print(String.format(" | %1.3f", value[i]));
                }
                System.out.print("\r");
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        Collector c = new Collector();
        c.run();
    }
}
