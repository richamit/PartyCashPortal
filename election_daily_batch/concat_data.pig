 data = LOAD '/user/ubuntu/richa/daily_batch/map/out/part-r-00000' USING PigStorage('|') AS (type:chararray,party:chararray,dat:chararray,latlong:chararray);
 data1 = FOREACH data generate CONCAT(CONCAT(type,party), dat) as con, latlong;
 
 STORE data1 INTO 'hbase://map_latlong_2014'
        USING org.apache.pig.backend.hadoop.hbase.HBaseStorage('cf1:latlong');