CREATE OR REPLACE PROCEDURE sp_cdd_match_customer
(
    p_caller_system IN VARCHAR2,        -- 필수: 호출시스템(예. BATCH, HOMEPAGE-FRONT, HOMEPAGE-ADMIN, CMA, ETL) 
    p_caller IN VARCHAR2,               -- 필수: 호출자(예. 홈페이지회원ID, 홈페이지관리자ID, CMA사용자ID, ETL인 경우 "ubigent") 
    p_min_simularity IN NUMBER,         -- 옵션: 최소 유사도(예. 디폴트 = 0) 
    p_src_system IN VARCHAR2,           -- 필수: 원천시스템(예. FMS, HOMEPAGE) 
    p_src_table IN VARCHAR2,            -- 필수: 원천고객테이블명(예. SUPERM, ACCOUNTM, TH_MEMBER_MASTER) 
    p_src_cust_id IN VARCHAR2,          -- 필수: 원천고객ID
    p_src_cust_tp IN VARCHAR2,          -- 필수: 원천고객구분(1:개인, 2:법인, 3:단체, 4:외국인) 
    p_src_cust_nm IN VARCHAR2,          -- 옵션: 고객명 
    p_src_superm_cust_id IN VARCHAR2,   -- 옵션: 후원자번호
    p_src_jumin_no IN VARCHAR2,         -- 옵션: 주민등록번호
    p_src_biz_reg_no IN VARCHAR2,       -- 옵션: 사업자등록번호
    p_src_cmpy_reg_no IN VARCHAR2,      -- 옵션: 법인등록번호
    p_src_for_reg_no IN VARCHAR2,       -- 옵션: 외국인등록번호
    p_src_passport_no IN VARCHAR2,      -- 옵션: 여권번호
    p_src_mobile_no IN VARCHAR2,        -- 옵션: 휴대폰번호
    p_src_email IN VARCHAR2,            -- 옵션: 이메일
    p_src_zipcode IN VARCHAR2,          -- 옵션: 우편수령지주소_우편번호
    p_src_addr1 IN VARCHAR2,            -- 옵션: 우편수령지주소_주소
    p_src_addr2 IN VARCHAR2             -- 옵션: 우편수령지주소_상세주소
)
IS
/******************************************************************************
   NAME:       sp_cdd_match_customer
   PURPOSE:    

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        2008-09-09  배영규           1. Created this procedure.

   NOTES:

   Automatically available Auto Replace Keywords:
      Object Name:     sp_cdd_match_customer
      Sysdate:         2008-09-09
      Date and Time:   2008-09-09, 오후 9:37:23, and 2008-09-09 오후 9:37:23
      Username:         (set in TOAD Options, Procedure Editor)
      Table Name:       (set in the "New PL/SQL Object" dialog)

******************************************************************************/
user_define_error1 EXCEPTION;
user_define_error2 EXCEPTION;
user_define_error3 EXCEPTION;
user_define_error4 EXCEPTION;
user_define_error5 EXCEPTION;
user_define_error6 EXCEPTION;
user_define_error7 EXCEPTION;

v_error_msg VARCHAR2(1000);
v_total_cnt NUMBER;
v_success_cnt NUMBER;
v_error_cnt NUMBER;
v_job_start_dm VARCHAR2(14);

v_dupl_superm_cust_yn VARCHAR2(1);
v_set_rep_cust_need_yn VARCHAR2(1);


CURSOR cur IS
    SELECT
        cust_id AS superm_cust_id,          -- 후원자번호
        supertype_dv AS superm_cust_tp,     -- 고객구분
        supername AS superm_cust_nm,        -- 고객명
        rr_id AS superm_jumin_no,           -- 주민등록번호
        br_id AS superm_biz_reg_no,         -- 사업자등록번호
        NULL AS superm_cmpy_reg_no,         -- 법인등록번호
        fr_id AS superm_for_reg_no,         -- 외국인등록번호
        NULL AS superm_passport_no,         -- 여권번호
        supermophnum AS superm_mobile_no,   -- 휴대폰번호
        superemail AS superm_email,         -- 이메일
        superzipcdde_2 AS superm_zipcode,   -- 우편수령지주소_우편번호
        address_2 AS superm_addr1,          -- 우편수령지주소_주소
        addressdtl_2 AS superm_addr2,       -- 우편수령지주소_상세주소
        superstate_dv AS superm_cust_stat,  -- 후원고객상태구분
        supstrt_dt AS superm_fund_start_dd, -- 후원시작일자
        supend_dt AS superm_fund_end_dd,    -- 후원종료일자
        fstoper_dt AS superm_reg_dd,        -- 최초등록일자
        lastupdate_dt AS superm_upd_dd      -- 최종수정일자                            
    FROM superm
    WHERE
        TRIM(supername) = TRIM(p_src_cust_nm)
        OR cust_id = p_src_superm_cust_id
        OR rr_id = fn_cdd_exclude_pattern('JUMIN_NO', p_src_jumin_no)
        OR br_id = fn_cdd_exclude_pattern('BIZ_REG_NO', p_src_biz_reg_no)
        OR fr_id = fn_cdd_exclude_pattern('FOR_REG_NO', p_src_for_reg_no)
        OR fn_cdd_filter_phone_no(supermophnum) = fn_cdd_exclude_pattern('MOBILE_NO', fn_cdd_filter_phone_no(p_src_mobile_no))
        OR TRIM(superemail) = TRIM(p_src_email)
        OR TRIM(superzipcdde_2)||''||TRIM(address_2)||''||TRIM(addressdtl_2)
           = TRIM(p_src_zipcode)||''||TRIM(p_src_addr1)||''||TRIM(p_src_addr2);
        
        /*
        TRIM(supername) = fn_cdd_exclude_pattern('CUST_NM', p_src_cust_nm)
        OR cust_id = p_superm_cust_id
        OR TRIM(rr_id) = fn_cdd_exclude_pattern('JUMIN_NO', p_src_jumin_no)
        OR TRIM(br_id) = fn_cdd_exclude_pattern('BIZ_REG_NO', p_src_biz_reg_no)
        OR TRIM(fr_id) = fn_cdd_exclude_pattern('FOR_REG_NO', p_src_for_reg_no)
        OR fn_cdd_filter_phone_no(supermophnum) = fn_cdd_exclude_pattern('MOBILE_NO', fn_cdd_filter_phone_no(p_src_mobile_no))
        OR TRIM(superemail) = fn_cdd_exclude_pattern('EMAIL', p_src_email)
        OR TRIM(superzipcdde_2)||''||TRIM(address_2)||''||TRIM(addressdtl_2)
           = fn_cdd_exclude_pattern('ZIPCODE', p_src_zipcode)||''
             ||fn_cdd_exclude_pattern('ADDR1', p_src_addr1)||''
             ||fn_cdd_exclude_pattern('ADDR2', p_src_addr2);
        */
BEGIN

    /* 파라미터로 받은 입력값이 적합한지 체크 후, 적합하지 않은 경우 Exception 강제 발생 */
    IF (p_caller_system IS NULL) THEN
        RAISE user_define_error1;
    END IF;
    
    IF (p_caller IS NULL) THEN
        RAISE user_define_error2;
    END IF;    
        
    IF (p_min_simularity > 1) THEN
        RAISE user_define_error3;
    END IF;
    
    IF (p_src_system IS NULL OR NOT(p_src_system = 'FMS' OR p_src_system = 'HOMEPAGE')) THEN
        RAISE user_define_error4;
    END IF;
    
    IF (p_src_table IS NULL OR NOT(p_src_table = 'SUPERM' OR p_src_table = 'ACCOUNTM' OR p_src_table = 'TH_MEMBER_MASTER')) THEN
        RAISE user_define_error5;
    END IF;
    
    IF (p_src_cust_id IS NULL) THEN
        RAISE user_define_error6;
    END IF;
    
    IF (p_src_cust_tp IS NULL OR NOT(p_src_cust_tp = '1' OR p_src_cust_tp = '2' OR p_src_cust_tp = '3' OR p_src_cust_tp = '4')) THEN
        RAISE user_define_error7;
    END IF;

        
    v_total_cnt := 0;
    v_success_cnt := 0;
    v_error_cnt := 0;
    v_job_start_dm := TO_CHAR(SYSDATE, 'yyyymmddhh24miss');
    
    v_dupl_superm_cust_yn := 'N';
    v_set_rep_cust_need_yn := 'N';

    FOR srclist IN cur LOOP
        
        BEGIN
            
            IF (p_src_table = 'SUPERM' AND p_src_cust_id = srclist.superm_cust_id) THEN
                GOTO end_loop;
            END IF;
                /* call sub-routine procedure */
            sp_cdd_match_customer_by_rule(
                p_caller_system,        -- 호출시스템
                p_caller,               -- 호출자
                p_min_simularity,       -- 최소 유사도
                p_src_system,           -- 원천고객: 원천시스템
                p_src_table,            -- 원천고객: 원천테이블명
                p_src_cust_id,          -- 원천고객: 원천고객ID
                p_src_cust_tp,          -- 원천고객: 원천고객구분
                p_src_cust_nm,          -- 원천고객: 고객명
                p_src_superm_cust_id,   -- 원천고객: 후원자번호
                p_src_jumin_no,         -- 원천고객: 주민등록번호
                p_src_biz_reg_no,       -- 원천고객: 사업자등록번호
                p_src_cmpy_reg_no,      -- 원천고객: 법인등록번호
                p_src_for_reg_no,       -- 원천고객: 외국인등록번호
                p_src_passport_no,      -- 원천고객: 여권번호
                p_src_mobile_no,        -- 원천고객: 휴대폰번호
                p_src_email,            -- 원천고객: 이메일
                p_src_zipcode,          -- 원천고객: 우편수령지주소_우편번호
                p_src_addr1,            -- 원천고객: 우편수령지주소_주소
                p_src_addr2,            -- 원천고객: 우편수령지주소_상세주소
                srclist.superm_cust_id,         -- 비교대상 유사후원자: 후원자번호
                srclist.superm_cust_tp,         -- 비교대상 유사후원자: 고객구분
                srclist.superm_cust_nm,         -- 비교대상 유사후원자: 고객명
                srclist.superm_jumin_no,        -- 비교대상 유사후원자: 주민등록번호
                srclist.superm_biz_reg_no,      -- 비교대상 유사후원자: 사업자등록번호
                srclist.superm_cmpy_reg_no,     -- 비교대상 유사후원자: 법인등록번호
                srclist.superm_for_reg_no,      -- 비교대상 유사후원자: 외국인등록번호
                srclist.superm_passport_no,     -- 비교대상 유사후원자: 여권번호
                srclist.superm_mobile_no,       -- 비교대상 유사후원자: 휴대폰번호
                srclist.superm_email,           -- 비교대상 유사후원자: 이메일
                srclist.superm_zipcode,         -- 비교대상 유사후원자: 우편수령지주소_우편번호
                srclist.superm_addr1,           -- 비교대상 유사후원자: 우편수령지주소_주소
                srclist.superm_addr2,           -- 비교대상 유사후원자: 우편수령지주소_상세주소
                srclist.superm_cust_stat,       -- 비교대상 유사후원자: 후원고객상태구분
                srclist.superm_fund_start_dd,   -- 비교대상 유사후원자: 후원시작일자
                srclist.superm_fund_end_dd,     -- 비교대상 유사후원자: 후원종료일자
                srclist.superm_reg_dd,          -- 비교대상 유사후원자: 최초등록일자
                srclist.superm_upd_dd,          -- 비교대상 유사후원자: 최종수정일자 
                v_dupl_superm_cust_yn           -- 정확하게 일치하는(simularity=1) 후원자 존재유무
            );
            
            /* 유사 후원자중에서 하나라도 유사도가 "1"인 후원자가 존재하는 경우 */
            IF (v_dupl_superm_cust_yn = 'Y') THEN
                v_set_rep_cust_need_yn := v_dupl_superm_cust_yn;
            END IF;
            
            v_success_cnt := v_success_cnt + 1;
            
            COMMIT;

        EXCEPTION
            WHEN OTHERS THEN
                DBMS_OUTPUT.PUT_LINE(SQLERRM||'ERROR');    
                
                v_error_msg := SQLERRM;                
                v_error_cnt := v_error_cnt + 1; 

                
                INSERT INTO dw_etl_job_error (
                    job_no,
                    job_id,
                    seq,
                    job_nm,
                    job_exe_dt,
                    src_data,
                    error_log
                    )    
                VALUES (
                    v_job_start_dm, 
                    'sp_cdd_match_customer['||p_src_system||'|'||p_src_table||'|'||p_src_cust_id||'|'||srclist.superm_cust_id||']', 
                    v_error_cnt,
                    '매칭되는 유사 후원자리스트 생성',
                    TO_CHAR(SYSDATE,'yyyyMMdd'),
                    'SUPERM_CUST_ID = '||srclist.superm_cust_id,
                    v_error_msg
                    );
                COMMIT;                
                
                RAISE;
        
        END;
        
        <<end_loop>>
            
        v_total_cnt := v_total_cnt + 1;    
        
    END LOOP;
    
    /* 
     * 유사 후원자중에서 하나라도 유사도가 "1"인 후원자가 존재하는 경우,
     * 대표후원자번호를 지정함.
     */    
    IF (v_set_rep_cust_need_yn = 'Y') THEN
        sp_cdd_set_rep_customer(p_src_system, p_src_table, p_src_cust_id);
    END IF;
    
    --DBMS_OUTPUT.PUT_LINE('v_set_rep_cust_need_yn = '||p_src_cust_id||','||v_set_rep_cust_need_yn);	
    
    

EXCEPTION

    WHEN user_define_error1 THEN
           RAISE_APPLICATION_ERROR(-20001, '1번째 인자인 호출시스템(p_caller_system)은 필수항목입니다. (예. BATCH, HOMEPAGE-FRONT, HOMEPAGE-ADMIN, CMA, ETL)');
    
    WHEN user_define_error2 THEN
           RAISE_APPLICATION_ERROR(-20002, '2번째 인자인 호출자(p_caller)는 필수항목입니다. (예. 홈페이지회원ID, 홈페이지관리자ID, CMA사용자ID, ETL인 경우 "ubigent")');
    
    WHEN user_define_error3 THEN
           RAISE_APPLICATION_ERROR(-20003, '3번째 인자인 최소유사도(p_min_simularity)는 최대 1을 넘길 수 없습니다. (예. 디폴트 = 0)');

    WHEN user_define_error4 THEN
           RAISE_APPLICATION_ERROR(-20004, '4번째 인자인 원천시스템(p_src_system)은 필수항목이며, 허용되는 값은 "FMS", "HOMEPAGE"만 허용됩니다.');

    WHEN user_define_error5 THEN
           RAISE_APPLICATION_ERROR(-20005, '5번째 인자인 원천고객테이블명(p_src_table)은 필수항목이며, 허용되는 값은 "SUPERM", "ACCOUNTM", "TH_MEMBER_MASTER"만 허용됩니다.');

    WHEN user_define_error6 THEN
           RAISE_APPLICATION_ERROR(-20006, '6번째 인자인 원천고객ID(p_src_cust_id)는 필수항목입니다.');

    WHEN user_define_error7 THEN
           RAISE_APPLICATION_ERROR(-20007, '7번째 인자인 원천고객구분(p_src_cust_tp)은 필수항목이며, 허용되는 값은 "1"-개인, "2"-법인, "3"-단체, "4"-외국인만 허용됩니다.');
                
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
			'sp_cdd_match_customer['||p_src_system||'|'||p_src_table||'|'||p_src_cust_id||']', 
			'매칭되는 유사 후원자리스트 생성',
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
        
        
        RAISE;
		
END sp_cdd_match_customer;


/
