CREATE OR REPLACE FUNCTION fn_cdd_exclude_pattern
(
    p_matching_criteria IN VARCHAR2, -- CUST_NM, JUMIN_NO, BIZ_REG_NO, CMPY_REG_NO, FOR_REG_NO, PASSPORT_NO, MOBILE_NO, EMAIL, P_ZIPCODE, P_ADDR1, P_ADDR2
    p_text IN VARCHAR2
)
RETURN VARCHAR2
IS
/******************************************************************************
   NAME:       fn_cdd_exclude_pattern
   PURPOSE:    

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        2008-09-18  배영규           1. Created this function.

   NOTES:

   Automatically available Auto Replace Keywords:
      Object Name:     fn_cdd_exclude_pattern
      Sysdate:         2008-09-18
      Date and Time:   2008-09-18, 오전 11:40:27, and 2008-09-18 오전 11:40:27
      Username:         (set in TOAD Options, Procedure Editor)
      Table Name:       (set in the "New PL/SQL Object" dialog)

******************************************************************************/
v_input_text VARCHAR2(1000);
v_exclude_text VARCHAR2(30);
v_cnt NUMBER(5);

BEGIN
    v_input_text := TRIM(p_text);
    v_exclude_text := '|*|*|*|';
    v_cnt := 0;
    
    /* 값이 null 또는 ''인 경우 */
    IF (v_input_text IS NULL OR v_input_text = '') THEN
        RETURN v_exclude_text;
    END IF;
    
    /* 고객명 */
    IF (p_matching_criteria = 'CUST_NM') THEN
        RETURN v_input_text;
    
    /* 주민등록번호: 제외패턴체크 필요  */
    ELSIF (p_matching_criteria = 'JUMIN_NO') THEN
        IF (length(v_input_text) <> 13) THEN            
            RETURN v_exclude_text;
        END IF;
    
    /* 사업자등록번호 */
    ELSIF (p_matching_criteria = 'BIZ_REG_NO') THEN
        IF (length(v_input_text) <> 10) THEN            
            RETURN v_exclude_text;
        END IF;        
        RETURN v_input_text;
    
    /* 법인등록번호 */
    ELSIF (p_matching_criteria = 'CMPY_REG_NO') THEN
        IF (length(v_input_text) <> 13) THEN            
            RETURN v_exclude_text;
        END IF;
        RETURN v_input_text;
    
    /* 외국인등록번호 */
    ELSIF (p_matching_criteria = 'FOR_REG_NO') THEN
        IF (length(v_input_text) <> 13) THEN            
            RETURN v_exclude_text;
        END IF;
        RETURN v_input_text;
    
    /* 여권번호 */
    ELSIF (p_matching_criteria = 'PASSPORT_NO') THEN
        IF (length(v_input_text) <> 9) THEN            
            RETURN v_exclude_text;
        END IF;
        RETURN v_input_text;
    
    /* 휴대폰번호 */
    ELSIF (p_matching_criteria = 'MOBILE_NO') THEN
        IF (length(v_input_text) < 6) THEN            
            RETURN v_exclude_text;
        END IF;
        RETURN v_input_text;
    
    /* 이메일 */
    ELSIF (p_matching_criteria = 'EMAIL') THEN
        RETURN v_input_text;

    /* 우편번호 */
    ELSIF (p_matching_criteria = 'ZIPCODE') THEN
        IF (length(v_input_text) <> 6) THEN            
            RETURN v_exclude_text;
        END IF;
        RETURN v_input_text;
    
    /* 주소 */
    ELSIF (p_matching_criteria = 'ADDR1') THEN
        RETURN v_input_text;
    
    /* 상세주소 */
    ELSIF (p_matching_criteria = 'ADDR2') THEN
        RETURN v_input_text;

    END IF;
	
    /* 제외패턴 체크 */
    SELECT COUNT(*) INTO v_cnt
    FROM dw_cdd_exclude_pattern
    WHERE matching_criteria = p_matching_criteria 
        AND INSTR(v_input_text, exclude_pattern) > 0;
    
    
    IF (v_cnt > 0) THEN
        RETURN v_exclude_text;
    END IF;
   
    RETURN v_input_text;
   
    EXCEPTION
        WHEN OTHERS THEN
            DBMS_OUTPUT.PUT_LINE(SQLERRM||'ERROR');	
            -- Consider logging the error and then re-raise
            RETURN p_text;
END;


/
