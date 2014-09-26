#!/bin/sh

#Clean HDFS out folder
hdfs dfs -rm -R /user/ubuntu/richa/out/election_zip/
hdfs dfs -rm -R /user/ubuntu/richa/out/election_zip_final/

#Run PIG Script
pig -f election_zip.pig

#Drop, create HBASE table
java -cp "$(hadoop classpath):/opt/cloudera/parcels/CDH-5.1.2-1.cdh5.1.2.p0.3/lib/pig/pig.jar:/opt/cloudera/parcels/CDH-5.1.2-1.cdh5.1.2.p0.3/lib/hbase/lib/*:/home/ubuntu/richa/ElectionCampaignContribution/javaCode/electionContribution.jar" com.rg.hbase.HbaseHandler

#Run pig script to load data to table
pig -f concat_data.pig

        


 
