SET job.name 'richa_map_zip';
load_contribution = LOAD '/user/ubuntu/richa/contribution/in/*'  USING PigStorage('|') AS (CMTE_ID:chararray,AMNDT_IND:chararray,RPT_TP:chararray,TRANSACTION_PGI:chararray,
IMAGE_NUM:chararray,TRANSACTION_TP:chararray  ,ENTITY_TP:chararray ,NAME:chararray,CITY:chararray,
STATE:chararray ,ZIP_CODE:chararray,EMPLOYER:chararray ,OCCUPATION:chararray,TRANSACTION_DT:chararray,
TRANSACTION_AMT:int,OTHER_ID:chararray,TRAN_ID:chararray,FILE_NUM:int,
MEMO_CD:chararray ,MEMO_TEXT:chararray,SUB_ID:int);

filter_cont_A = filter load_contribution by TRANSACTION_DT IS NOT NULL;

filter_cont_B = filter filter_cont_A by TRANSACTION_AMT > 0;

filter_cont_C = filter filter_cont_B by ZIP_CODE IS NOT NULL;

filter_contribution = FOREACH filter_cont_C generate CMTE_ID, SUBSTRING(ZIP_CODE, 0, 5) as ZIP_CODE,
		      TRANSACTION_DT as TRANSACTION_DT, TRANSACTION_AMT as TRANSACTION_AMT,CITY as CITY, STATE as STATE;


A = LOAD '/user/ubuntu/richa/out/zip_clean/part*' USING PigStorage('|') AS  (zip:chararray, city:chararray, state:chararray, lat:chararray, longi:chararray, time:int, dst:int);
B = FOREACH A generate zip as zip, lat as lat, longi as longi, city as city;

C = join filter_contribution by ZIP_CODE left, B by zip;

D = FOREACH C generate filter_contribution::CMTE_ID as CMTE_ID, filter_contribution::TRANSACTION_DT as 
    TRANSACTION_DT, B::lat as lat, B::longi as longi, filter_contribution::ZIP_CODE as zip;
    
--D: {CMTE_ID: chararray,TRANSACTION_DT: chararray,lat: chararray,longi: chararray,zip: chararray}

load_committee = LOAD '/user/ubuntu/richa/committee/in/*' USING PigStorage('|') AS (CMTE_ID:chararray,CMTE_NM:chararray,TRES_NM:chararray,CMTE_ST1:chararray,CMTE_ST2:chararray,
CMTE_CITY:chararray,CMTE_ST:chararray,CMTE_ZIP:chararray,CMTE_DSGN:chararray,CMTE_TP:chararray,
CMTE_PTY_AFFILIATION:chararray,CMTE_FILING_FREQ:chararray,ORG_TP:chararray,CONNECTED_ORG_NM:chararray,
CAND_ID:chararray);


filter_committee_A = filter load_committee by CMTE_PTY_AFFILIATION IS NOT NULL;

filter_committee_null = filter filter_committee_A by CMTE_TP IS NOT NULL ;

filter_committee_party = filter filter_committee_null by (CMTE_PTY_AFFILIATION == 'DEM' OR CMTE_PTY_AFFILIATION == 'REP');

filter_committee = filter filter_committee_party by (CMTE_TP == 'P'  OR CMTE_TP == 'S' OR CMTE_TP == 'H');

merge_data_set = join D by CMTE_ID left, filter_committee by CMTE_ID;

after_merge = FOREACH merge_data_set GENERATE D::CMTE_ID as CMTE_ID, D::TRANSACTION_DT as TRANSACTION_DT,
              D::lat as lat, D::longi as longi, filter_committee::CMTE_TP as CMTE_TP, filter_committee::CMTE_PTY_AFFILIATION
              as CMTE_PARTY, filter_committee::CAND_ID as CAND_ID;
              
filter_by_cand_id = filter after_merge by CAND_ID IS NOT NULL;

load_candidate = LOAD '/user/ubuntu/richa/candidate/in/*' USING PigStorage('|') AS
(CAND_ID:chararray,CAND_NAME:chararray,CAND_PTY_AFFILIATION:chararray,CAND_ELECTION_YR:int,
CAND_OFFICE_ST:chararray,CAND_OFFICE:chararray,CAND_OFFICE_DISTRICT:chararray,CAND_ICI:chararray,
CAND_STATUS:chararray,CAND_PCC:chararray,CAND_ST1:chararray,CAND_ST2:chararray,CAND_CITY:chararray,
CAND_ST:chararray,CAND_ZIP:chararray);

merge_final_set = join filter_by_cand_id by CAND_ID left, load_candidate by CAND_ID;

 
needed_data = FOREACH merge_final_set GENERATE filter_by_cand_id::TRANSACTION_DT as dat,
              CONCAT(CONCAT(filter_by_cand_id::lat,','), filter_by_cand_id::longi) as latlong,
              filter_by_cand_id::CMTE_TP as CMTE_TP,
              filter_by_cand_id::CMTE_PARTY as CMTE_PARTY;

data_group = GROUP needed_data by (CMTE_TP,CMTE_PARTY,dat);

 

uniqzip  = foreach data_group {
           latlong    = needed_data.latlong;
           uniq_latlong = distinct latlong;            
           generate group, uniq_latlong;
};

Register '/home/ubuntu/richa/ElectionCampaignContribution/javaCode/electionContribution.jar'
ready = FOREACH uniqzip generate flatten(group), com.rg.processor.BagToString(uniq_latlong);
STORE ready INTO '/user/ubuntu/richa/out/map_latlong/' USING PigStorage('|') ;
 


