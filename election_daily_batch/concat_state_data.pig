SET job.name 'richa_election_zip_load_hbase';

data = LOAD '/user/ubuntu/richa/daily_batch/state/out/part-r-*' USING PigStorage('|') AS (type:chararray,party:chararray,dat:chararray,state:chararray, amount:double);
data1 = FOREACH data generate CONCAT(CONCAT(type,' '), party) as part1, CONCAT(CONCAT(dat,' '), state) as part2, amount as amount;
data2 = FOREACH data1 generate CONCAT(CONCAT(part1,' '), part2) as hkey, amount as amount;
data3 = FILTER data2 by hkey IS NOT NULL;

STORE data3 INTO '/user/ubuntu/richa/daily_batch/state/out/out_final' USING PigStorage('|') ;

