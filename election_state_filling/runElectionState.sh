#!/bin/sh

#Clean HDFS out folder
hdfs dfs -rm -R /user/ubuntu/richa/random/filling_date/out
hdfs dfs -rm -R /user/ubuntu/richa/random/filling_date/out_final

#Run PIG Script
pig -f election_zip.pig

#Run pig script to load data to table
pig -f concat_data.pig

        


 
