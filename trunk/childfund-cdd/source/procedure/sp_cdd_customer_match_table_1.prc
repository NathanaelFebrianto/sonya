CREATE OR REPLACE PROCEDURE sp_cdd_customer_match_table_1 
(
    p_base_dt IN VARCHAR2    -- 기준일자
) 
IS
/******************************************************************************
   NAME:       sp_cdd_customer_match_table_1
   PURPOSE:    SUPERM -> DW_CDD_CUSTOMER_MATCHING 테이블로 적재

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        2008-09-09  배영규           1. Created this procedure.

   NOTES:

   Automatically available Auto Replace Keywords:
      Object Name:     sp_cdd_customer_match_table_1
      Sysdate:         2008-09-09
      Date and Time:   2008-09-09, 오후 9:37:23, and 2008-09-09 오후 9:37:23
      Username:         (set in TOAD Options, Procedure Editor)
      Table Name:       (set in the "New PL/SQL Object" dialog)

******************************************************************************/
v_error_msg VARCHAR2(1000);
v_total_cnt NUMBER;
v_success_cnt NUMBER;
v_error_cnt NUMBER;
v_job_start_dm VARCHAR2(14);

CURSOR cur IS
    SELECT 
        'FMS' AS src_system         -- 원천시스템
        ,'SUPERM' AS src_table      -- 원천고객테이블명
        ,cust_id AS src_cust_id     -- 원천고객ID
        ,CASE                       -- 원천고객구분
         WHEN (supertype_dv = '1' OR supertype_dv = '2' OR supertype_dv = '3' OR supertype_dv = '4') THEN
            supertype_dv
         WHEN ((supertype_dv IS NULL) AND (SUBSTR(cust_id,5,1) = '1' OR SUBSTR(cust_id,5,1) = '2' OR SUBSTR(cust_id,5,1) = '3' OR SUBSTR(cust_id,5,1) = '4')) THEN
            SUBSTR(cust_id,5,1)
         ELSE
            '1'
         END AS src_cust_tp
        ,supername AS cust_nm       -- 고객명
        ,cust_id AS superm_cust_id  -- 후원자번호
        ,rr_id AS jumin_no          -- 주민등록번호
        ,br_id AS biz_reg_no        -- 사업자등록번호
        ,NULL AS cmpy_reg_no        -- 법인등록번호
        ,fr_id AS for_reg_no        -- 외국인등록번호
        ,NULL AS passport_no        -- 여권번호
        ,supermophnum AS mobile_no  -- 휴대폰번호
        ,superemail AS email        -- 이메일
        ,superzipcdde_2 AS zipcode  -- 우편수령지주소_우편번호
        ,address_2 AS addr1         -- 우편수령지주소_주소
        ,addressdtl_2 AS addr2      -- 우편수령지주소_상세주소        
    FROM superm@FMS
    --WHERE 
        --fstoper_dt >= p_base_dt OR lastupdate_dt >= p_base    
    /*
    -- 에러가 발생한 rownum부터 실행하고자 하는 경우
    FROM (SELECT ROWNUM rowno, a.* FROM superm@FMS a) 
        WHERE rowno > 383500 
    */
    ; 


BEGIN

    v_total_cnt := 0;
    v_success_cnt := 0;
    v_error_cnt := 0;
    v_job_start_dm := TO_CHAR(SYSDATE, 'yyyymmddhh24miss');    

    FOR srclist IN cur LOOP
        
        BEGIN
            /* call sub-routine procedure */
            sp_cdd_match_customer(
                'BATCH'                 -- 호출시스템
                ,'배영규'               -- 호출자
                ,0                      -- 최소 유사도
                ,srclist.src_system     -- 원천시스템
                ,srclist.src_table      -- 원천테이블명
                ,srclist.src_cust_id    -- 원천고객ID
                ,srclist.src_cust_tp    -- 원천고객구분
                ,srclist.cust_nm        -- 고객명
                ,srclist.superm_cust_id -- 후원자번호
                ,srclist.jumin_no       -- 주민등록번호
                ,srclist.biz_reg_no     -- 사업자등록번호
                ,srclist.cmpy_reg_no    -- 법인등록번호
                ,srclist.for_reg_no     -- 외국인등록번호
                ,srclist.passport_no    -- 여권번호
                ,srclist.mobile_no      -- 휴대폰번호
                ,srclist.email          -- 이메일
                ,srclist.zipcode        -- 우편수령지주소_우편번호
                ,srclist.addr1          -- 우편수령지주소_주소
                ,srclist.addr2          -- 우편수령지주소_상세주소     
            );
            
            v_success_cnt := v_success_cnt + 1;
            
            COMMIT;
        
        END;
            
        v_total_cnt := v_total_cnt + 1; 
        
        IF (v_total_cnt = 1) THEN
            INSERT INTO dw_etl_job_hist (
                job_no
                ,job_id
                ,job_nm
                ,job_strt_dm
                --,job_end_dm
                --,tot_src_cnt
                ,succ_cnt
                ,error_cnt
                ,job_stat
                ,job_log
                ,executor
                )	
            VALUES (
                v_job_start_dm 
                ,'sp_cdd_customer_match_table_1' 
                ,'고객매칭테이블생성 Batch적재-SUPERM'
                ,v_job_start_dm
                --,TO_CHAR(SYSDATE,'yyyyMMddHH24miSS')
                --,v_total_cnt
                ,v_success_cnt
                ,v_error_cnt
                ,'START'
                ,''
                ,'배영규'
                );
            
            COMMIT;
        
        ELSIF (v_total_cnt > 1) THEN
            UPDATE dw_etl_job_hist SET
                succ_cnt = v_success_cnt
                ,job_stat = 'WORKING'
            WHERE        
                job_no = v_job_start_dm AND job_id = 'sp_cdd_customer_match_table_1';
            
            COMMIT;
        END IF;
        
    END LOOP;

    /*
    INSERT INTO dw_etl_job_hist (
        job_no
        ,job_id
        ,job_nm
        ,job_strt_dm
        ,job_end_dm
        ,tot_src_cnt
        ,succ_cnt
        ,error_cnt
        ,job_stat
        ,job_log
        ,executor
		)	
	VALUES (
		v_job_start_dm 
		,'sp_cdd_customer_match_table_1' 
		,'고객매칭테이블생성 Batch적재-SUPERM'
		,v_job_start_dm
		,TO_CHAR(SYSDATE,'yyyyMMddHH24miSS')
		,v_total_cnt
		,v_success_cnt
		,v_error_cnt
		,'COMPLETED'
		,''
		,'배영규'
		);
    */    

    UPDATE dw_etl_job_hist SET
        job_end_dm = TO_CHAR(SYSDATE,'yyyyMMddHH24miSS')
        ,tot_src_cnt = v_total_cnt
        ,succ_cnt = v_success_cnt
        ,job_stat = 'COMPLETED'
    WHERE        
        job_no = v_job_start_dm AND job_id = 'sp_cdd_customer_match_table_1';
    
	COMMIT;
    

EXCEPTION
	WHEN OTHERS THEN
		DBMS_OUTPUT.PUT_LINE(SQLERRM||'ERROR');	
		
		v_error_msg := SQLERRM;
        
        IF (v_total_cnt < 1) THEN
            INSERT INTO dw_etl_job_hist (
                job_no
                ,job_id
                ,job_nm
                ,job_strt_dm
                ,job_end_dm
                ,tot_src_cnt
                ,succ_cnt
                ,error_cnt
                ,job_stat
                ,job_log
                ,executor
                )	
            VALUES (
                v_job_start_dm 
                ,'sp_cdd_customer_match_table_1' 
                ,'고객매칭테이블생성 Batch적재-SUPERM'
                ,v_job_start_dm
                ,TO_CHAR(SYSDATE,'yyyyMMddHH24miSS')
                ,v_total_cnt
                ,v_success_cnt
                ,v_error_cnt
                ,'FAILED'
                ,v_error_msg
                ,'배영규'
                );
        ELSE
            UPDATE dw_etl_job_hist SET
                job_end_dm = TO_CHAR(SYSDATE,'yyyyMMddHH24miSS')
                ,tot_src_cnt = v_total_cnt
                ,succ_cnt = v_success_cnt
                ,job_stat = 'FAILED'
                ,job_log = v_error_msg
            WHERE        
                job_no = v_job_start_dm AND job_id = 'sp_cdd_customer_match_table_1';
        END IF;
        
		COMMIT;
		
END sp_cdd_customer_match_table_1;


/
