#!/bin/sh

#Clean HDFS out folder
hdfs dfs -rm -R /user/ubuntu/richa/daily_batch/in
hdfs dfs -rm -R /user/ubuntu/richa/daily_batch/state/out
hdfs dfs -rm -R /user/ubuntu/richa/daily_batch/state/out_final

hdfs dfs -mkdir -p /user/ubuntu/richa/daily_batch/in
cd /home/ubuntu/richa/ExpenseData/java_kafka_consumer/
hdfs dfs -put *.out /user/ubuntu/richa/daily_batch/in
rm  /home/ubuntu/richa/ExpenseData/java_kafka_consumer/*.out

#Run PIG Script
pig -f /home/ubuntu/richa/ElectionCampaignContribution/election_daily_batch/election_daily_zip.pig

#Run pig script to load data to table
pig -f /home/ubuntu/richa/ElectionCampaignContribution/election_daily_batch/concat_state_data.pig

pig -f /home/ubuntu/richa/ElectionCampaignContribution/election_daily_batch/final_data.pig


#run Election Map Batch
#Clean HDFS out folder
hdfs dfs -rm -R /user/ubuntu/richa/daily_batch/map/out

#Run PIG Script
pig -f /home/ubuntu/richa/ElectionCampaignContribution/election_daily_batch/map_zip.pig

#Run pig script to load data to table
pig -f /home/ubuntu/richa/ElectionCampaignContribution/election_daily_batch/concat_data.pig


        


 
