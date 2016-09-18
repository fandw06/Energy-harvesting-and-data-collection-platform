package main;

import adc.SpiADC;
import com.pi4j.io.spi.SpiChannel;
import com.pi4j.io.spi.SpiDevice;
import com.pi4j.io.spi.SpiFactory;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Collector {

    private SpiDevice spi;
    private SpiADC adc;
    private static final int INTERVAL = 1000;

    private String serverIP;
    private String dbName;
    private InfluxDB influxDB;

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
                writeInfluxDB(value);
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

    public void setServer(String serverIP) {
        this.serverIP = serverIP;
        influxDB = InfluxDBFactory.connect("http://" + serverIP +":8086", "root", "root");
    }

    public void writeInfluxDB(final double[] data) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> fields = new HashMap<String, Object>();
                for (int i = 0; i< data.length; i++) {
                    fields.put("ch" + i, data[i]);
                }
                Point point = Point.measurement("ad")
                        .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                        .fields(fields)
                        .build();

                influxDB.write("collector", "autogen", point);
            }
        }).start();

    }

    public static void main(String[] args) {
        Collector c = new Collector();
        c.setServer("128.143.24.101");
        c.run();
    }
}
