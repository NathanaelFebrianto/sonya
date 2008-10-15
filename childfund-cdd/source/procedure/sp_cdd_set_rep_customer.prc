CREATE OR REPLACE PROCEDURE sp_cdd_set_rep_customer
(
    p_src_system IN VARCHAR2,           -- 원천시스템(예. FMS, HOMEPAGE) 
    p_src_table IN VARCHAR2,            -- 원천고객테이블명(예. TEMP, SUPERM, ACCOUNTM, TH_MEMBER_MASTER, TEMP-SUPERM, TEMP-ACCOUNTM) 
    p_src_cust_id IN VARCHAR2           -- 원천고객ID 
)
IS
/******************************************************************************
   NAME:       sp_cdd_set_rep_customer
   PURPOSE:    

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        2008-09-22   배영규          1. Created this procedure.

   NOTES:

   Automatically available Auto Replace Keywords:
      Object Name:     sp_cdd_set_rep_customer
      Sysdate:         2008-09-22
      Date and Time:   2008-09-22, 오후 2:37:11, and 2008-09-22 오후 2:37:11
      Username:         (set in TOAD Options, Procedure Editor)
      Table Name:       (set in the "New PL/SQL Object" dialog)

******************************************************************************/
v_error_msg VARCHAR2(1000);
v_total_cnt NUMBER;
v_success_cnt NUMBER;
v_error_cnt NUMBER;
v_job_start_dm VARCHAR2(14);

v_rep_superm_cust_id VARCHAR2(11);
v_src_cust_id VARCHAR2(50);
v_score NUMBER;
v_max_score NUMBER;
v_skip_yn VARCHAR2(1);

CURSOR cur IS
    SELECT
        src_cust_id
        ,superm_cust_id
        ,superm_cust_tp
        ,fix_yn
        ,rep_superm_cust_yn
        ,superm_cust_nm_yn      -- 5점
        ,superm_jumin_no_yn     -- 10점
        ,superm_biz_reg_no_yn   -- 10점
        ,superm_cmpy_reg_no_yn  -- 10점
        ,superm_for_reg_no_yn   -- 10점
        ,superm_passport_no_yn  -- 10점
        ,superm_mobile_no_yn    -- 8점
        ,superm_email_yn        -- 8점
        ,superm_addr_yn         -- 9점    
        ,superm_cust_stat       -- 신규(1) or 활동(2) -> 10점
        ,superm_fund_start_dd   -- not null -> 2점
        ,superm_fund_end_dd     -- null -> 2점
        ,superm_reg_dd
        ,superm_upd_dd
    FROM dw_cdd_customer_matching        
    WHERE (src_system = p_src_system AND src_table = p_src_table AND simularity = 1 AND NVL(manual_del_yn, 'N') <> 'Y')
        AND (src_cust_id = p_src_cust_id OR superm_cust_id = p_src_cust_id
        OR src_cust_id IN (
            SELECT src_cust_id FROM dw_cdd_customer_matching 
            WHERE src_system = p_src_system AND src_table = p_src_table AND simularity = 1 AND NVL(manual_del_yn, 'N') <> 'Y' AND superm_cust_id = p_src_cust_id));

           
BEGIN

    v_total_cnt := 0;
    v_success_cnt := 0;
    v_error_cnt := 0;
    v_job_start_dm := TO_CHAR(SYSDATE, 'yyyymmddhh24miss');
    
    v_rep_superm_cust_id := '';
    v_src_cust_id := '';
    v_max_score := 0;
    v_skip_yn := 'N';
    

    FOR srclist IN cur LOOP
        
        v_score := 0;

        BEGIN

            IF (srclist.rep_superm_cust_yn = 'Y') THEN
                v_skip_yn := 'Y';
                EXIT;
            END IF;
            
            DBMS_OUTPUT.PUT_LINE('LOOP == '||v_total_cnt);

            IF (srclist.superm_cust_nm_yn = 'Y') THEN
                v_score := v_score + 5;
            END IF;
            
            IF (srclist.superm_jumin_no_yn = 'Y') THEN
                v_score := v_score + 10;
            END IF;
            
            IF (srclist.superm_biz_reg_no_yn = 'Y') THEN
                v_score := v_score + 10;
            END IF;
            
            IF (srclist.superm_cmpy_reg_no_yn = 'Y') THEN
                v_score := v_score + 10;
            END IF;
            
            IF (srclist.superm_for_reg_no_yn = 'Y') THEN
                v_score := v_score + 10;
            END IF;
            
            IF (srclist.superm_passport_no_yn = 'Y') THEN
                v_score := v_score + 10;
            END IF;
            
            IF (srclist.superm_mobile_no_yn = 'Y') THEN
                v_score := v_score + 8;
            END IF;
            
            IF (srclist.superm_email_yn = 'Y') THEN
                v_score := v_score + 8;
            END IF;

            IF (srclist.superm_addr_yn = 'Y') THEN
                v_score := v_score + 9;
            END IF;
            
            IF (srclist.superm_cust_stat = '1' OR srclist.superm_cust_stat = '2') THEN  -- 신규 or 활동
                v_score := v_score + 10;
            END IF;
            
            IF (srclist.superm_fund_start_dd IS NOT NULL AND srclist.superm_fund_start_dd <> '') THEN
                v_score := v_score + 2;
            END IF;
            
            IF (srclist.superm_fund_end_dd IS NULL OR srclist.superm_fund_end_dd = '') THEN
                v_score := v_score + 2;
            END IF;
            
            IF (v_score > v_max_score) THEN
                v_max_score := v_score;
                v_rep_superm_cust_id := srclist.superm_cust_id;
                v_src_cust_id := srclist.src_cust_id;
            END IF;
            
            v_success_cnt := v_success_cnt + 1;

        EXCEPTION
            WHEN OTHERS THEN
                DBMS_OUTPUT.PUT_LINE(SQLERRM||'ERROR');    
                
                v_error_msg := SQLERRM;                
                v_error_cnt := v_error_cnt + 1;        
        END;
            
        v_total_cnt := v_total_cnt + 1;    
        
    END LOOP;
    
    --DBMS_OUTPUT.PUT_LINE('src_system = '||p_src_system||', src_table = '||p_src_table||', src_cust_id = '||v_src_cust_id||', rep_superm_cust_id = '||v_rep_superm_cust_id); 
    
    IF (TRIM(v_rep_superm_cust_id) IS NOT NULL AND v_skip_yn = 'N') THEN
        UPDATE dw_cdd_customer_matching SET
            rep_superm_cust_yn = 'Y'
        WHERE src_system = p_src_system AND
            src_table = p_src_table AND
            src_cust_id = v_src_cust_id AND
            superm_cust_id = v_rep_superm_cust_id;
            
        COMMIT;
    END IF;


EXCEPTION
	WHEN OTHERS THEN
		DBMS_OUTPUT.PUT_LINE(SQLERRM||'ERROR');	
		
		v_error_msg := SQLERRM;
        
        RAISE;
		
END sp_cdd_set_rep_customer;


/
