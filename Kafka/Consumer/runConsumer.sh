#!/bin/sh
java -cp "$(hadoop classpath):/opt/cloudera/parcels/CDH-5.1.2-1.cdh5.1.2.p0.3/lib/pig/pig.jar:/opt/cloudera/parcels/CDH-5.1.2-1.cdh5.1.2.p0.3/lib/hbase/lib/*:/home/ubuntu/richa/kafka_2.8.0-0.8.1.1/libs/*:/opt/cloudera/parcels/CDH-5.1.2-1.cdh5.1.2.p0.3/lib/hbase/lib/*:/home/ubuntu/richa/ExpenseData/java_kafka_consumer/a.jar" com.rg.kafka.ConsumerClass
