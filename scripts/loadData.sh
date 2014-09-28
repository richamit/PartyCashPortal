#!/bin/sh
hdfs dfs -mkdir -p /user/ubuntu/richa/contribution/in
hdfs dfs -mkdir -p /user/ubuntu/richa/committee/in
hdfs dfs -mkdir -p /user/ubuntu/richa/candidate/in
hdfs dfs -mkdir -p /user/ubuntu/richa/zipCode/in

hdfs dfs -put /home/ubuntu/richa/ContributionData/data/*.txt /user/ubuntu/richa/contribution/in
hdfs dfs -put /home/ubuntu/richa/CommitteData/data/*.txt /user/ubuntu/richa/committee/in
hdfs dfs -put /home/ubuntu/richa/CandidateData/data/*.txt /user/ubuntu/richa/candidate/in
hdfs dfs -put /home/ubuntu/richa/zipCode/*.csv /user/ubuntu/richa/zipCode/in
