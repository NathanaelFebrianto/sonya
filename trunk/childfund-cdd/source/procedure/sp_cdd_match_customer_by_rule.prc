CREATE OR REPLACE PROCEDURE sp_cdd_match_customer_by_rule
(
    p_caller_system IN VARCHAR2,        -- ȣ��ý���(��. BATCH, HOMEPAGE-FRONT, HOMEPAGE-ADMIN, CMA, ETL) 
    p_caller IN VARCHAR2,               -- ȣ����(��. Ȩ������ȸ��ID, Ȩ������������ID, CMA�����ID, ETL�� ��� "ubigent") 
    p_min_simularity IN NUMBER,         -- �ּ� ���絵 
    p_src_system IN VARCHAR2,           -- ��õ��: ��õ�ý���(��. FMS, HOMEPAGE) 
    p_src_table IN VARCHAR2,            -- ��õ��: ��õ�����̺��(��. TEMP, SUPERM, ACCOUNTM, TH_MEMBER_MASTER, TEMP-SUPERM, TEMP-ACCOUNTM) 
    p_src_cust_id IN VARCHAR2,          -- ��õ��: ��õ��ID
    p_src_cust_tp IN VARCHAR2,          -- ��õ��: ��õ������
    p_src_cust_nm IN VARCHAR2,          -- ��õ��: ����
    p_src_superm_cust_id IN VARCHAR2,   -- ��õ��: �Ŀ��ڹ�ȣ
    p_src_jumin_no IN VARCHAR2,         -- ��õ��: �ֹε�Ϲ�ȣ
    p_src_biz_reg_no IN VARCHAR2,       -- ��õ��: ����ڵ�Ϲ�ȣ
    p_src_cmpy_reg_no IN VARCHAR2,      -- ��õ��: ���ε�Ϲ�ȣ
    p_src_for_reg_no IN VARCHAR2,       -- ��õ��: �ܱ��ε�Ϲ�ȣ
    p_src_passport_no IN VARCHAR2,      -- ��õ��: ���ǹ�ȣ
    p_src_mobile_no IN VARCHAR2,        -- ��õ��: �޴�����ȣ
    p_src_email IN VARCHAR2,            -- ��õ��: �̸���
    p_src_zipcode IN VARCHAR2,          -- ��õ��: ����������ּ�_�����ȣ
    p_src_addr1 IN VARCHAR2,            -- ��õ��: ����������ּ�_�ּ�
    p_src_addr2 IN VARCHAR2,            -- ��õ��: ����������ּ�_���ּ�
    p_superm_cust_id IN VARCHAR2,       -- �񱳴�� �����Ŀ���: �Ŀ��ڹ�ȣ
    p_superm_cust_tp IN VARCHAR2,       -- �񱳴�� �����Ŀ���: ������
    p_superm_cust_nm IN VARCHAR2,       -- �񱳴�� �����Ŀ���: ����
    p_superm_jumin_no IN VARCHAR2,      -- �񱳴�� �����Ŀ���: �ֹε�Ϲ�ȣ
    p_superm_biz_reg_no IN VARCHAR2,    -- �񱳴�� �����Ŀ���: ����ڵ�Ϲ�ȣ
    p_superm_cmpy_reg_no IN VARCHAR2,   -- �񱳴�� �����Ŀ���: ���ε�Ϲ�ȣ
    p_superm_for_reg_no IN VARCHAR2,    -- �񱳴�� �����Ŀ���: �ܱ��ε�Ϲ�ȣ
    p_superm_passport_no IN VARCHAR2,   -- �񱳴�� �����Ŀ���: ���ǹ�ȣ
    p_superm_mobile_no IN VARCHAR2,     -- �񱳴�� �����Ŀ���: �޴�����ȣ
    p_superm_email IN VARCHAR2,         -- �񱳴�� �����Ŀ���: �̸���
    p_superm_zipcode IN VARCHAR2,       -- �񱳴�� �����Ŀ���: ����������ּ�_�����ȣ
    p_superm_addr1 IN VARCHAR2,         -- �񱳴�� �����Ŀ���: ����������ּ�_�ּ�
    p_superm_addr2 IN VARCHAR2,         -- �񱳴�� �����Ŀ���: ����������ּ�_���ּ�
    p_superm_cust_stat IN VARCHAR2,     -- �񱳴�� �����Ŀ���: �Ŀ������±���
    p_superm_fund_start_dd IN VARCHAR2, -- �񱳴�� �����Ŀ���: �Ŀ���������
    p_superm_fund_end_dd IN VARCHAR2,   -- �񱳴�� �����Ŀ���: �Ŀ���������
    p_superm_reg_dd IN VARCHAR2,        -- �񱳴�� �����Ŀ���: ���ʵ������
    p_superm_upd_dd IN VARCHAR2,        -- �񱳴�� �����Ŀ���: ������������ 
    p_dupl_superm_cust_yn OUT VARCHAR2  -- ��Ȯ�ϰ� ��ġ�ϴ�(simularity=1) �Ŀ��� ��������
)
IS
/******************************************************************************
   NAME:       sp_cdd_match_customer_by_rule
   PURPOSE:    

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        2008-09-09  �迵��           1. Created this procedure.

   NOTES:

   Automatically available Auto Replace Keywords:
      Object Name:     sp_cdd_match_customer_by_rule
      Sysdate:         2008-09-09
      Date and Time:   2008-09-09, ���� 9:37:23, and 2008-09-09 ���� 9:37:23
      Username:         (set in TOAD Options, Procedure Editor)
      Table Name:       (set in the "New PL/SQL Object" dialog)

******************************************************************************/
v_error_msg VARCHAR2(1000);
v_total_cnt NUMBER;
v_success_cnt NUMBER;
v_error_cnt NUMBER;
v_job_start_dm VARCHAR2(14);

/* �۾� Flag ���� */
v_matching_table_exist_cnt NUMBER;
v_exclude_text VARCHAR2(30);

/* ��õ��  ���� */
v_src_cust_nm VARCHAR2(200);
v_src_cust_id VARCHAR2(20);
v_src_superm_cust_id VARCHAR2(11);
v_src_jumin_no VARCHAR2(13);
v_src_biz_reg_no VARCHAR2(10);
v_src_cmpy_reg_no VARCHAR2(13);
v_src_for_reg_no VARCHAR2(13);
v_src_passport_no VARCHAR2(9);
v_src_mobile_no VARCHAR2(100);
v_src_email VARCHAR2(100);
v_source_address VARCHAR2(1000);    -- ��õ ����������ּ�(�����ȣ+�ּ�+���ּ�)

/* �����Ŀ��� ���� */
v_superm_cust_nm VARCHAR2(100);
v_superm_cust_id VARCHAR2(11);
v_superm_jumin_no VARCHAR2(13);
v_superm_biz_reg_no VARCHAR2(10);
v_superm_cmpy_reg_no VARCHAR2(13);
v_superm_for_reg_no VARCHAR2(13);
v_superm_passport_no VARCHAR2(9);
v_superm_mobile_no VARCHAR2(50);
v_superm_email VARCHAR2(100);
v_superm_address VARCHAR2(1000);    -- �񱳴�� ���� �Ŀ��� ����������ּ�(�����ȣ+�ּ�+���ּ�)

v_superm_cust_nm_yn VARCHAR2(1);
v_superm_jumin_no_yn VARCHAR2(1);
v_superm_biz_reg_no_yn VARCHAR2(1);
v_superm_cmpy_reg_no_yn VARCHAR2(1);
v_superm_for_reg_no_yn VARCHAR2(1);
v_superm_passport_no_yn VARCHAR2(1);
v_superm_mobile_no_yn VARCHAR2(1);
v_superm_email_yn VARCHAR2(1);
v_superm_addr_yn VARCHAR2(1);

/* ��Ī ���� �ؽ�Ʈ */
x_cust_nm_yn VARCHAR2(1);
x_superm_cust_id_yn VARCHAR2(1);
x_jumin_no_yn VARCHAR2(1);
x_biz_reg_no_yn VARCHAR2(1);
x_cmpy_reg_no_yn VARCHAR2(1);
x_for_reg_no_yn VARCHAR2(1);
x_passport_no_yn VARCHAR2(1);
x_mobile_no_yn VARCHAR2(1);
x_email_yn VARCHAR2(1);
x_addr_yn VARCHAR2(1);

y_cust_nm_yn VARCHAR2(1);
y_superm_cust_id_yn VARCHAR2(1);
y_jumin_no_yn VARCHAR2(1);
y_biz_reg_no_yn VARCHAR2(1);
y_cmpy_reg_no_yn VARCHAR2(1);
y_for_reg_no_yn VARCHAR2(1);
y_passport_no_yn VARCHAR2(1);
y_mobile_no_yn VARCHAR2(1);
y_email_yn VARCHAR2(1);
y_addr_yn VARCHAR2(1);

x_matching_pattern VARCHAR2(50);
y_matching_pattern VARCHAR2(50);


CURSOR cur IS
    /* Rule ���� */
    SELECT 
        rule_id
        ,cust_tp
        ,priority
        ,simularity
        ,cust_nm_yn
        ,superm_cust_id_yn
        ,jumin_no_yn
        ,biz_reg_no_yn
        ,cmpy_reg_no_yn
        ,for_reg_no_yn
        ,passport_no_yn
        ,mobile_no_yn
        ,email_yn
        ,addr_yn      
    FROM dw_cdd_duplicate_check_rule
    WHERE cust_tp = p_src_cust_tp AND simularity >= NVL(p_min_simularity, 0)
    ORDER BY priority ASC;   

BEGIN

    v_total_cnt := 0;
    v_success_cnt := 0;
    v_error_cnt := 0;
    v_job_start_dm := TO_CHAR(SYSDATE, 'yyyymmddhh24miss');
    
    v_exclude_text := '|*|*|*|';

    /* ��õ�� ���� */
    v_src_cust_nm := TRIM(p_src_cust_nm);
    v_src_superm_cust_id := p_src_superm_cust_id;
    v_src_jumin_no := TRIM(p_src_jumin_no);
    v_src_biz_reg_no := TRIM(p_src_biz_reg_no);
    v_src_cmpy_reg_no := TRIM(p_src_cmpy_reg_no);
    v_src_for_reg_no := TRIM(p_src_for_reg_no);
    v_src_passport_no := TRIM(p_src_passport_no);
    v_src_mobile_no := fn_cdd_filter_phone_no(p_src_mobile_no);
    v_src_email := TRIM(p_src_email);    
    v_source_address := TRIM(p_src_zipcode)||''||TRIM(p_src_addr1)||''||TRIM(p_src_addr2);
    
    /* �����Ŀ��� ���� ���� üũ */
    v_superm_cust_nm := fn_cdd_exclude_pattern('CUST_NM', p_superm_cust_nm);
    v_superm_cust_id := p_superm_cust_id;
    v_superm_jumin_no := fn_cdd_exclude_pattern('JUMIN_NO', p_superm_jumin_no);
    v_superm_biz_reg_no := fn_cdd_exclude_pattern('BIZ_REG_NO', p_superm_biz_reg_no);
    v_superm_cmpy_reg_no := fn_cdd_exclude_pattern('CMPY_REG_NO', p_superm_cmpy_reg_no);
    v_superm_for_reg_no := fn_cdd_exclude_pattern('FOR_REG_NO', p_superm_for_reg_no);
    v_superm_passport_no := fn_cdd_exclude_pattern('PASSPORT_NO', p_superm_passport_no);
    v_superm_mobile_no := fn_cdd_exclude_pattern('MOBILE_NO', fn_cdd_filter_phone_no(p_superm_mobile_no));
    v_superm_email := fn_cdd_exclude_pattern('EMAIL', p_superm_email);
    v_superm_address := fn_cdd_exclude_pattern('ZIPCODE', p_superm_zipcode)||''
        ||fn_cdd_exclude_pattern('ADDR1', p_superm_addr1)||''
        ||fn_cdd_exclude_pattern('ADDR2', p_superm_addr2);    
    
    /* �����Ŀ��� ���� ��ȿ���� üũ */
    IF (v_superm_cust_nm <> v_exclude_text) THEN
        v_superm_cust_nm_yn := 'Y';
    END IF;    
    IF (v_superm_jumin_no <> v_exclude_text) THEN
        v_superm_jumin_no_yn := 'Y';
    END IF;    
    IF (v_superm_biz_reg_no <> v_exclude_text) THEN
        v_superm_biz_reg_no_yn := 'Y';
    END IF;
    IF (v_superm_cmpy_reg_no <> v_exclude_text) THEN
        v_superm_cmpy_reg_no_yn := 'Y';
    END IF;  
    IF (v_superm_for_reg_no <> v_exclude_text) THEN
        v_superm_cust_nm_yn := 'Y';
    END IF;
    IF (v_superm_passport_no <> v_exclude_text) THEN
        v_superm_passport_no_yn := 'Y';
    END IF; 
    IF (v_superm_mobile_no <> v_exclude_text) THEN
        v_superm_mobile_no_yn := 'Y';
    END IF;    
    IF (v_superm_email <> v_exclude_text) THEN
        v_superm_email_yn := 'Y';
    END IF;    
    IF (v_superm_address <> v_exclude_text) THEN
        v_superm_addr_yn := 'Y';
    END IF;
    
    
    FOR srclist IN cur LOOP
        
        BEGIN
            x_cust_nm_yn := '';
            x_superm_cust_id_yn := '';
            x_jumin_no_yn := '';
            x_biz_reg_no_yn := '';
            x_cmpy_reg_no_yn := '';
            x_for_reg_no_yn := '';
            x_passport_no_yn := '';
            x_mobile_no_yn := '';
            x_email_yn := '';
            x_addr_yn := '';

            y_cust_nm_yn := '';
            y_superm_cust_id_yn := '';
            y_jumin_no_yn := '';
            y_biz_reg_no_yn := '';
            y_cmpy_reg_no_yn := '';
            y_for_reg_no_yn := '';
            y_passport_no_yn := '';
            y_mobile_no_yn := '';
            y_email_yn := '';
            y_addr_yn := '';

            x_matching_pattern := '';
            y_matching_pattern := '';
            v_matching_table_exist_cnt := 0;
            
            /* Detect the duplicate customer by rule */
            IF (srclist.cust_nm_yn = 'Y') THEN
                x_cust_nm_yn := 'A';
                IF (v_src_cust_nm = v_superm_cust_nm) THEN                
                    y_cust_nm_yn := 'A';
                END IF;
            END IF;
            
            IF (srclist.superm_cust_id_yn = 'Y') THEN
                x_superm_cust_id_yn := 'B';
                IF (v_src_superm_cust_id = v_superm_cust_id) THEN
                    y_superm_cust_id_yn := 'B';
                END IF;
            END IF;
                
            IF (srclist.jumin_no_yn = 'Y') THEN
                x_jumin_no_yn := 'C';
                IF (v_src_jumin_no = v_superm_jumin_no) THEN
                    y_jumin_no_yn := 'C';
                END IF;
            END IF;
            
            IF (srclist.biz_reg_no_yn = 'Y') THEN
                x_biz_reg_no_yn := 'D';
                IF (v_src_biz_reg_no = v_superm_biz_reg_no) THEN
                    y_biz_reg_no_yn := 'D';
                END IF;
            END IF;
            
            IF (srclist.cmpy_reg_no_yn = 'Y') THEN
                x_cmpy_reg_no_yn := 'E';
                IF (v_src_cmpy_reg_no = v_superm_cmpy_reg_no) THEN
                    y_cmpy_reg_no_yn := 'E';
                END IF;
            END IF;
            
            IF (srclist.for_reg_no_yn = 'Y') THEN
                x_for_reg_no_yn := 'F';
                IF (v_src_for_reg_no = v_superm_for_reg_no) THEN
                    y_for_reg_no_yn := 'F';
                END IF;
            END IF;
            
            IF (srclist.passport_no_yn = 'Y') THEN
                x_passport_no_yn := 'G';
                IF (v_src_passport_no = v_superm_passport_no) THEN
                    y_passport_no_yn := 'G';
                END IF;
            END IF;
            
            IF (srclist.mobile_no_yn = 'Y') THEN
                x_mobile_no_yn := 'H';
                IF (v_src_mobile_no = v_superm_mobile_no) THEN
                    y_mobile_no_yn := 'H';
                END IF;
            END IF;
            
            IF (srclist.email_yn = 'Y') THEN
                x_email_yn := 'I';
                IF (v_src_email = v_superm_email) THEN
                    y_email_yn := 'I';
                END IF;
            END IF;
            
            IF (srclist.addr_yn = 'Y') THEN    
                x_addr_yn := 'J';
                IF (v_source_address = v_superm_address) THEN
                    y_addr_yn := 'J';
                END IF;
            END IF;
            
            /* Check if match the rule */
            x_matching_pattern := x_cust_nm_yn||x_superm_cust_id_yn||x_jumin_no_yn||
                                  x_biz_reg_no_yn||x_cmpy_reg_no_yn||x_for_reg_no_yn||
                                  x_passport_no_yn||x_mobile_no_yn||x_email_yn||x_addr_yn;
            y_matching_pattern := y_cust_nm_yn||y_superm_cust_id_yn||y_jumin_no_yn||
                                  y_biz_reg_no_yn||y_cmpy_reg_no_yn||y_for_reg_no_yn||
                                  y_passport_no_yn||y_mobile_no_yn||y_email_yn||y_addr_yn;

                                 
            IF (x_matching_pattern = y_matching_pattern) THEN
            
                /* ����Ī���̺� �����ϴ� ���� �Ŀ������� üũ */
                SELECT COUNT(*) INTO v_matching_table_exist_cnt
                FROM dw_cdd_customer_matching
                WHERE src_system = p_src_system AND
                      src_table = p_src_table AND
                      src_cust_id = p_src_cust_id AND
                      superm_cust_id = p_superm_cust_id;
            
                IF (v_matching_table_exist_cnt = 0) THEN
                    INSERT INTO dw_cdd_customer_matching (
                        src_system
                        ,src_table
                        ,src_cust_id
                        ,superm_cust_id
                        ,src_cust_tp
                        ,superm_cust_tp
                        ,rule_id
                        ,rule_priority
                        ,simularity
                        ,superm_cust_nm_yn
                        ,superm_jumin_no_yn
                        ,superm_biz_reg_no_yn
                        ,superm_cmpy_reg_no_yn
                        ,superm_for_reg_no_yn
                        ,superm_passport_no_yn
                        ,superm_mobile_no_yn
                        ,superm_email_yn
                        ,superm_addr_yn
                        ,superm_cust_stat
                        ,superm_fund_start_dd
                        ,superm_fund_end_dd
                        ,superm_reg_dd
                        ,superm_upd_dd
                        ,auto_yn
                        ,auto_caller_system
                        ,auto_caller
                        ,auto_reg_dt
                        ,auto_upd_dt
                     )
                     VAlUES (
                        p_src_system
                        ,p_src_table
                        ,p_src_cust_id
                        ,p_superm_cust_id
                        ,p_src_cust_tp
                        ,p_superm_cust_tp
                        ,srclist.rule_id
                        ,srclist.priority
                        ,srclist.simularity
                        ,v_superm_cust_nm_yn
                        ,v_superm_jumin_no_yn
                        ,v_superm_biz_reg_no_yn
                        ,v_superm_cmpy_reg_no_yn
                        ,v_superm_for_reg_no_yn
                        ,v_superm_passport_no_yn
                        ,v_superm_mobile_no_yn
                        ,v_superm_email_yn
                        ,v_superm_addr_yn
                        ,p_superm_cust_stat
                        ,p_superm_fund_start_dd
                        ,p_superm_fund_end_dd
                        ,p_superm_reg_dd
                        ,p_superm_upd_dd                        
                        ,'Y'
                        ,p_caller_system
                        ,p_caller
                        ,TO_CHAR(SYSDATE, 'yyyymmddhh24miss')
                        ,TO_CHAR(SYSDATE, 'yyyymmddhh24miss')
                     );
                ELSE
                    UPDATE dw_cdd_customer_matching SET 
                        src_cust_tp = p_src_cust_tp
                        ,superm_cust_tp = p_superm_cust_tp
                        ,rule_id = srclist.rule_id
                        ,rule_priority = srclist.priority
                        ,simularity = srclist.simularity 
                        ,superm_cust_nm_yn = v_superm_cust_nm_yn
                        ,superm_jumin_no_yn = v_superm_jumin_no_yn
                        ,superm_biz_reg_no_yn = v_superm_biz_reg_no_yn
                        ,superm_cmpy_reg_no_yn = v_superm_cmpy_reg_no_yn
                        ,superm_for_reg_no_yn = v_superm_for_reg_no_yn
                        ,superm_passport_no_yn = v_superm_passport_no_yn
                        ,superm_mobile_no_yn = v_superm_mobile_no_yn
                        ,superm_email_yn = v_superm_email_yn
                        ,superm_addr_yn = v_superm_addr_yn
                        ,superm_cust_stat = p_superm_cust_stat
                        ,superm_fund_start_dd = p_superm_fund_start_dd
                        ,superm_fund_end_dd = p_superm_fund_end_dd
                        ,superm_reg_dd = p_superm_reg_dd
                        ,superm_upd_dd = p_superm_upd_dd
                        ,auto_yn = 'Y'
                        ,auto_caller_system = p_caller_system
                        ,auto_caller = p_caller
                        ,auto_upd_dt = TO_CHAR(SYSDATE, 'yyyymmddhh24miss')                   
                     WHERE src_system = p_src_system AND
                           src_table = p_src_table AND
                           src_cust_id = p_src_cust_id AND
                           superm_cust_id = p_superm_cust_id AND
                           (fix_yn IS NULL OR fix_yn <> 'Y');
                END IF;
                
                IF (srclist.simularity = 1) THEN
                    p_dupl_superm_cust_yn := 'Y';
                END IF;
                /* Loop�� �������� */
                EXIT;
                
            END IF;             
                   
            v_success_cnt := v_success_cnt + 1;
            
            COMMIT;

        EXCEPTION
            WHEN OTHERS THEN
                DBMS_OUTPUT.PUT_LINE(SQLERRM||'ERROR');    
                
                v_error_msg := SQLERRM;                
                v_error_cnt := v_error_cnt + 1; 

                
                INSERT INTO dw_etl_job_error (
                    job_no
                    ,job_id
                    ,seq
                    ,job_nm
                    ,job_exe_dt
                    ,src_data
                    ,error_log
                    )    
                VALUES (
                    v_job_start_dm 
                    ,'sp_cdd_match_customer_by_rule['||p_src_system||'|'||p_src_table||'|'||p_src_cust_id||'|'||p_superm_cust_id||'|'||srclist.rule_id||']' 
                    ,v_error_cnt
                    ,'Rule�� ���� ��Ī�Ǵ� ���������Ʈ ����'
                    ,TO_CHAR(SYSDATE,'yyyyMMdd')
                    ,'Rule ID = '||srclist.rule_id
                    ,v_error_msg
                    );
                COMMIT;                
                
                RAISE;
       
        END;
            
        v_total_cnt := v_total_cnt + 1;    
        
    END LOOP;


EXCEPTION
	WHEN OTHERS THEN
		DBMS_OUTPUT.PUT_LINE(SQLERRM||'ERROR');	
		
		v_error_msg := SQLERRM;
        
        RAISE;

END sp_cdd_match_customer_by_rule;


/
