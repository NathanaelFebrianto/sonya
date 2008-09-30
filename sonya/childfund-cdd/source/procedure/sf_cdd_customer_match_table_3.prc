CREATE OR REPLACE PROCEDURE sf_cdd_customer_match_table_3
IS
/******************************************************************************
   NAME:       sf_cdd_customer_match_table_3 
   PURPOSE:    TH_MEMBER_MASTER -> DW_CDD_CUSTOMER_MATCHING 테이블로 초기적재

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        2008-09-09  배영규           1. Created this procedure.

   NOTES:

   Automatically available Auto Replace Keywords:
      Object Name:     sf_cdd_customer_match_table_3 
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
        'FMS' AS src_system,        -- 원천시스템
        'ACCOUNTM' AS src_table,    -- 원천고객테이블명
        account_id AS src_cust_id,  -- 원천고객ID
        acctype_dv AS src_cust_tp,  -- 원천고객구분
        accountname AS cust_nm,     -- 고객명
        cust_id AS superm_cust_id,  -- 후원자번호
        NULL AS jumin_no,           -- 주민등록번호
        NULL AS biz_reg_no,         -- 사업자등록번호
        NULL AS cmpy_reg_no,        -- 법인등록번호
        NULL AS for_reg_no,         -- 외국인등록번호
        NULL AS passport_no,        -- 여권번호
        mophnum AS mobile_no,       -- 휴대폰번호
        email AS email,             -- 이메일
        zipcode AS zipcode,         -- 우편수령지주소_우편번호
        address AS addr1,           -- 우편수령지주소_주소
        addressdtl AS addr2         -- 우편수령지주소_상세주소        
    FROM accountm@FMS;
    --WHERE 
        --fstoper_dt <= '19911231' OR TRIM(fstoper_dt) IS NULL;
        --fstoper_dt BETWEEN '19920101' AND '19951231'
        --fstoper_dt >= '20080901';


BEGIN

    v_total_cnt := 0;
    v_success_cnt := 0;
    v_error_cnt := 0;
    v_job_start_dm := TO_CHAR(SYSDATE, 'yyyymmddhh24miss');    

    FOR srclist IN cur LOOP
        
        BEGIN
            /* call sub-routine procedure */
            sp_cdd_match_customer(
                'BATCH',                -- 호출시스템
                '배영규',               -- 호출자
                0,                      -- 최소 유사도
                srclist.src_system,     -- 원천시스템
                srclist.src_table,      -- 원천테이블명
                srclist.src_cust_id,    -- 원천고객ID
                srclist.src_cust_tp,    -- 원천고객구분
                srclist.cust_nm,        -- 고객명
                srclist.superm_cust_id, -- 후원자번호
                srclist.jumin_no,       -- 주민등록번호
                srclist.biz_reg_no,     -- 사업자등록번호
                srclist.cmpy_reg_no,    -- 법인등록번호
                srclist.for_reg_no,     -- 외국인등록번호
                srclist.passport_no,    -- 여권번호
                srclist.mobile_no,      -- 휴대폰번호
                srclist.email,          -- 이메일
                srclist.zipcode,        -- 우편수령지주소_우편번호
                srclist.addr1,          -- 우편수령지주소_주소
                srclist.addr2          -- 우편수령지주소_상세주소     
            );
            
            v_success_cnt := v_success_cnt + 1;
            
            COMMIT;
        
        END;
            
        v_total_cnt := v_total_cnt + 1;    
        
    END LOOP;

    INSERT INTO dw_etl_job_hist (
        job_no,
        job_id,
        job_nm,
        job_strt_dm,
        job_end_dm,
        tot_src_cnt,
        succ_cnt,
        error_cnt,
        job_stat,
        job_log,
        executor
		)	
	VALUES (
		v_job_start_dm, 
		'sf_cdd_customer_match_table_3', 
		'초기적재-고객매칭테이블생성-SUPERM',
		v_job_start_dm,
		TO_CHAR(SYSDATE,'yyyyMMddHH24miSS'),
		v_total_cnt,
		v_success_cnt,
		v_error_cnt,
		'COMPLETED',
		'',
		'배영규'
		);
	COMMIT;

EXCEPTION
	WHEN OTHERS THEN
		DBMS_OUTPUT.PUT_LINE(SQLERRM||'ERROR');	
		
		v_error_msg := SQLERRM;

		INSERT INTO dw_etl_job_hist (
			job_no,
			job_id,
			job_nm,
			job_strt_dm,
			job_end_dm,
			tot_src_cnt,
			succ_cnt,
			error_cnt,
			job_stat,
			job_log,
			executor
			)	
		VALUES (
			v_job_start_dm, 
			'sf_cdd_customer_match_table_3', 
			'초기적재-고객매칭테이블생성-SUPERM',
			v_job_start_dm,
			TO_CHAR(SYSDATE,'yyyyMMddHH24miSS'),
			v_total_cnt,
			v_success_cnt,
			v_error_cnt,
			'FAILED',
			v_error_msg,
			'배영규'
			);
		COMMIT;
		
END sf_cdd_customer_match_table_3;


/
