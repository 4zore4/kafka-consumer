package com.var.consumer.components;

import com.var.consumer.domain.Stress;
import lombok.extern.slf4j.Slf4j;
import org.influxdb.InfluxDB;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class InfluxComponent {

    @Qualifier("influxdb")
    @Autowired
    private InfluxDB influxDB;

    @Autowired
    private BatchPoints batchPoints;

    @Value("${spring.influx.database}")
    public String database;

    public void insert(String jobId, String name, String mark, BigInteger time_consuming, Long time){
        Point point = Point.measurement("stress_data")
                .tag("jobId",jobId)
                .addField("mark",mark)
                .addField("name",name)
                .addField("time_consuming",time_consuming)
                .time(time, TimeUnit.NANOSECONDS)
                .build();

        influxDB.write(database,"autogen",point);
        log.info(database);
        log.info("写入完成");
    }
    public void batchInsert(List<Stress> stressList){
        for (Stress stress : stressList){
            Point point = Point.measurement("stress_data")
                    .tag("jobId",stress.getJobId())
                    .addField("mark",stress.getMark())
                    .addField("name",stress.getName())
                    .addField("time_consuming",stress.getTime())
                    .time(stress.getDate(), TimeUnit.NANOSECONDS)
                    .build();
            batchPoints.point(point);
        }

//        log.info(batchPoints.toString());
        influxDB.write(batchPoints);
        log.info(database);
        log.info("写入完成");
    }

}
