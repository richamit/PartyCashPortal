data4 = LOAD '/user/ubuntu/richa/daily_batch/state/out/out_final/part-m-*' USING PigStorage('|') AS (htkey:chararray, amount:double);

STORE data4 INTO 'hbase://election_zip'
        USING org.apache.pig.backend.hadoop.hbase.HBaseStorage('cf1:val');
