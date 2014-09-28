#!/bin/sh
/home/ubuntu/richa/kafka_2.8.0-0.8.1.1/bin/kafka-topics.sh --zookeeper localhost:2181 --create --replication-factor  1 --partition 1 --topic contribution
java -cp "$(hadoop classpath):/opt/cloudera/parcels/CDH-5.1.2-1.cdh5.1.2.p0.3/lib/pig/pig.jar:/opt/cloudera/parcels/CDH-5.1.2-1.cdh5.1.2.p0.3/lib/hbase/lib/*:/home/ubuntu/richa/kafka_2.8.0-0.8.1.1/libs/*:/home/ubuntu/richa/Simulator/producer.jar" com.rg.kafke.handler.SimulateContribution
