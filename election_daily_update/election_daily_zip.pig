SET job.name 'richa_election_state_daily_batch';
load_contribution = LOAD '/user/ubuntu/richa/daily_batch/in/*'  USING PigStorage('|') AS (CMTE_ID:chararray,AMNDT_IND:chararray,RPT_TP:chararray,TRANSACTION_PGI:chararray,
IMAGE_NUM:chararray,TRANSACTION_TP:chararray  ,ENTITY_TP:chararray ,NAME:chararray,CITY:chararray,
STATE:chararray ,ZIP_CODE:chararray,EMPLOYER:chararray ,OCCUPATION:chararray,TRANSACTION_DT:chararray,
TRANSACTION_AMT:double,OTHER_ID:chararray,TRAN_ID:chararray,FILE_NUM:int,
MEMO_CD:chararray ,MEMO_TEXT:chararray,SUB_ID:int);
    
load_committee = LOAD '/user/ubuntu/richa/committee/in/cm14.txt' USING PigStorage('|') AS (CMTE_ID:chararray,CMTE_NM:chararray,TRES_NM:chararray,CMTE_ST1:chararray,CMTE_ST2:chararray,
CMTE_CITY:chararray,CMTE_ST:chararray,CMTE_ZIP:chararray,CMTE_DSGN:chararray,CMTE_TP:chararray,
CMTE_PTY_AFFILIATION:chararray,CMTE_FILING_FREQ:chararray,ORG_TP:chararray,CONNECTED_ORG_NM:chararray,
CAND_ID:chararray);

filter_contribution_pre = filter load_contribution by TRANSACTION_DT IS NOT NULL;

filter_contribution = filter filter_contribution_pre by TRANSACTION_AMT > 0;

filter_committee_pre = filter load_committee by CMTE_PTY_AFFILIATION IS NOT NULL;

filter_committee_null = filter filter_committee_pre by CMTE_TP IS NOT NULL;

filter_committee_party = filter filter_committee_null by (CMTE_PTY_AFFILIATION == 'DEM' OR CMTE_PTY_AFFILIATION == 'REP');

filter_committee = filter filter_committee_party by (CMTE_TP == 'P'  OR CMTE_TP == 'S' OR CMTE_TP == 'H');

merge_data_set = join filter_contribution by CMTE_ID left, filter_committee by CMTE_ID;

data = foreach merge_data_set generate filter_contribution::CMTE_ID as CMTE_ID,
CMTE_NM as CMTE_NM, CMTE_TP as CMTE_TP, ZIP_CODE as ZIP_CODE, TRANSACTION_DT as TRANSACTION_DT,
TRANSACTION_AMT as TRANSACTION_AMT,CITY as CITY, STATE as STATE, CAND_ID as CAND_ID ;

--describe data;
--data: {CMTE_ID,CMTE_NM,CMTE_TP,CITY,TRANSACTION_DT,TRANSACTION_AMT,CITY,STATE}

load_candidate = LOAD '/user/ubuntu/richa/candidate/in/cn14.txt' USING PigStorage('|') AS
(CAND_ID:chararray,CAND_NAME:chararray,CAND_PTY_AFFILIATION:chararray,CAND_ELECTION_YR:int,
CAND_OFFICE_ST:chararray,CAND_OFFICE:chararray,CAND_OFFICE_DISTRICT:chararray,CAND_ICI:chararray,
CAND_STATUS:chararray,CAND_PCC:chararray,CAND_ST1:chararray,CAND_ST2:chararray,CAND_CITY:chararray,
CAND_ST:chararray,CAND_ZIP:chararray);

merge_final_set = join data by CAND_ID left, load_candidate by CAND_ID;

merged_data = foreach merge_final_set generate CMTE_ID as CMTE_ID,
CMTE_NM as CMTE_NM, CMTE_TP as CMTE_TP, STATE as STATE, TRANSACTION_DT as TRANSACTION_DT,
TRANSACTION_AMT as TRANSACTION_AMT,CAND_PTY_AFFILIATION AS CAND_PTY_AFFILIATION;


data_group = GROUP merged_data by (CMTE_TP,CAND_PTY_AFFILIATION,TRANSACTION_DT,STATE);

total_per_day = FOREACH data_group generate group, SUM(merged_data.TRANSACTION_AMT) as total;

describe total_per_day;

ready = FOREACH total_per_day generate flatten(group), total;

STORE ready INTO '/user/ubuntu/richa/daily_batch/state/out' USING PigStorage('|') ;