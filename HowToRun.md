#How to Run

1. Run script from scrips\fetchElectionData.sh to fetch the data from fec website
2. Run script from scripts\loadData.sh to load the data in hdfs
3. Run script from election_map\runElectionMap.sh which will join the dataset, create hbase table and finally load the data in the hbase table for showing the report data on Map
4. Run script from election_state_report\runElectionState.sh which will join datasets and then load the data in another hbase table 
