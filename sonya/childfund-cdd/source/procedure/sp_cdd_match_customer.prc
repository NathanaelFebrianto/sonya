CREATE OR REPLACE PROCEDURE sp_cdd_match_customer
(
    p_caller_system IN VARCHAR2,        -- �ʼ�: ȣ��ý���(��. BATCH, HOMEPAGE-FRONT, HOMEPAGE-ADMIN, CMA, ETL) 
    p_caller IN VARCHAR2,               -- �ʼ�: ȣ����(��. Ȩ������ȸ��ID, Ȩ������������ID, CMA�����ID, ETL�� ��� "ubigent") 
    p_min_simularity IN NUMBER,         -- �ɼ�: �ּ� ���絵(��. ����Ʈ = 0) 
    p_src_system IN VARCHAR2,           -- �ʼ�: ��õ�ý���(��. FMS, HOMEPAGE) 
    p_src_table IN VARCHAR2,            -- �ʼ�: ��õ�����̺��(��. SUPERM, ACCOUNTM, TH_MEMBER_MASTER) 
    p_src_cust_id IN VARCHAR2,          -- �ʼ�: ��õ��ID
    p_src_cust_tp IN VARCHAR2,          -- �ʼ�: ��õ������(1:����, 2:����, 3:��ü, 4:�ܱ���) 
    p_src_cust_nm IN VARCHAR2,          -- �ɼ�: ���� 
    p_src_superm_cust_id IN VARCHAR2,   -- �ɼ�: �Ŀ��ڹ�ȣ
    p_src_jumin_no IN VARCHAR2,         -- �ɼ�: �ֹε�Ϲ�ȣ
    p_src_biz_reg_no IN VARCHAR2,       -- �ɼ�: ����ڵ�Ϲ�ȣ
    p_src_cmpy_reg_no IN VARCHAR2,      -- �ɼ�: ���ε�Ϲ�ȣ
    p_src_for_reg_no IN VARCHAR2,       -- �ɼ�: �ܱ��ε�Ϲ�ȣ
    p_src_passport_no IN VARCHAR2,      -- �ɼ�: ���ǹ�ȣ
    p_src_mobile_no IN VARCHAR2,        -- �ɼ�: �޴�����ȣ
    p_src_email IN VARCHAR2,            -- �ɼ�: �̸���
    p_src_zipcode IN VARCHAR2,          -- �ɼ�: ����������ּ�_�����ȣ
    p_src_addr1 IN VARCHAR2,            -- �ɼ�: ����������ּ�_�ּ�
    p_src_addr2 IN VARCHAR2             -- �ɼ�: ����������ּ�_���ּ�
)
IS
/******************************************************************************
   NAME:       sp_cdd_match_customer
   PURPOSE:    

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        2008-09-09  �迵��           1. Created this procedure.

   NOTES:

   Automatically available Auto Replace Keywords:
      Object Name:     sp_cdd_match_customer
      Sysdate:         2008-09-09
      Date and Time:   2008-09-09, ���� 9:37:23, and 2008-09-09 ���� 9:37:23
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
        cust_id AS superm_cust_id,          -- �Ŀ��ڹ�ȣ
        supertype_dv AS superm_cust_tp,     -- ������
        supername AS superm_cust_nm,        -- ����
        rr_id AS superm_jumin_no,           -- �ֹε�Ϲ�ȣ
        br_id AS superm_biz_reg_no,         -- ����ڵ�Ϲ�ȣ
        NULL AS superm_cmpy_reg_no,         -- ���ε�Ϲ�ȣ
        fr_id AS superm_for_reg_no,         -- �ܱ��ε�Ϲ�ȣ
        NULL AS superm_passport_no,         -- ���ǹ�ȣ
        supermophnum AS superm_mobile_no,   -- �޴�����ȣ
        superemail AS superm_email,         -- �̸���
        superzipcdde_2 AS superm_zipcode,   -- ����������ּ�_�����ȣ
        address_2 AS superm_addr1,          -- ����������ּ�_�ּ�
        addressdtl_2 AS superm_addr2,       -- ����������ּ�_���ּ�
        superstate_dv AS superm_cust_stat,  -- �Ŀ������±���
        supstrt_dt AS superm_fund_start_dd, -- �Ŀ���������
        supend_dt AS superm_fund_end_dd,    -- �Ŀ���������
        fstoper_dt AS superm_reg_dd,        -- ���ʵ������
        lastupdate_dt AS superm_upd_dd      -- ������������                            
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

    /* �Ķ���ͷ� ���� �Է°��� �������� üũ ��, �������� ���� ��� Exception ���� �߻� */
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
                p_caller_system,        -- ȣ��ý���
                p_caller,               -- ȣ����
                p_min_simularity,       -- �ּ� ���絵
                p_src_system,           -- ��õ��: ��õ�ý���
                p_src_table,            -- ��õ��: ��õ���̺��
                p_src_cust_id,          -- ��õ��: ��õ��ID
                p_src_cust_tp,          -- ��õ��: ��õ������
                p_src_cust_nm,          -- ��õ��: ����
                p_src_superm_cust_id,   -- ��õ��: �Ŀ��ڹ�ȣ
                p_src_jumin_no,         -- ��õ��: �ֹε�Ϲ�ȣ
                p_src_biz_reg_no,       -- ��õ��: ����ڵ�Ϲ�ȣ
                p_src_cmpy_reg_no,      -- ��õ��: ���ε�Ϲ�ȣ
                p_src_for_reg_no,       -- ��õ��: �ܱ��ε�Ϲ�ȣ
                p_src_passport_no,      -- ��õ��: ���ǹ�ȣ
                p_src_mobile_no,        -- ��õ��: �޴�����ȣ
                p_src_email,            -- ��õ��: �̸���
                p_src_zipcode,          -- ��õ��: ����������ּ�_�����ȣ
                p_src_addr1,            -- ��õ��: ����������ּ�_�ּ�
                p_src_addr2,            -- ��õ��: ����������ּ�_���ּ�
                srclist.superm_cust_id,         -- �񱳴�� �����Ŀ���: �Ŀ��ڹ�ȣ
                srclist.superm_cust_tp,         -- �񱳴�� �����Ŀ���: ������
                srclist.superm_cust_nm,         -- �񱳴�� �����Ŀ���: ����
                srclist.superm_jumin_no,        -- �񱳴�� �����Ŀ���: �ֹε�Ϲ�ȣ
                srclist.superm_biz_reg_no,      -- �񱳴�� �����Ŀ���: ����ڵ�Ϲ�ȣ
                srclist.superm_cmpy_reg_no,     -- �񱳴�� �����Ŀ���: ���ε�Ϲ�ȣ
                srclist.superm_for_reg_no,      -- �񱳴�� �����Ŀ���: �ܱ��ε�Ϲ�ȣ
                srclist.superm_passport_no,     -- �񱳴�� �����Ŀ���: ���ǹ�ȣ
                srclist.superm_mobile_no,       -- �񱳴�� �����Ŀ���: �޴�����ȣ
                srclist.superm_email,           -- �񱳴�� �����Ŀ���: �̸���
                srclist.superm_zipcode,         -- �񱳴�� �����Ŀ���: ����������ּ�_�����ȣ
                srclist.superm_addr1,           -- �񱳴�� �����Ŀ���: ����������ּ�_�ּ�
                srclist.superm_addr2,           -- �񱳴�� �����Ŀ���: ����������ּ�_���ּ�
                srclist.superm_cust_stat,       -- �񱳴�� �����Ŀ���: �Ŀ������±���
                srclist.superm_fund_start_dd,   -- �񱳴�� �����Ŀ���: �Ŀ���������
                srclist.superm_fund_end_dd,     -- �񱳴�� �����Ŀ���: �Ŀ���������
                srclist.superm_reg_dd,          -- �񱳴�� �����Ŀ���: ���ʵ������
                srclist.superm_upd_dd,          -- �񱳴�� �����Ŀ���: ������������ 
                v_dupl_superm_cust_yn           -- ��Ȯ�ϰ� ��ġ�ϴ�(simularity=1) �Ŀ��� ��������
            );
            
            /* ���� �Ŀ����߿��� �ϳ��� ���絵�� "1"�� �Ŀ��ڰ� �����ϴ� ��� */
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
                    '��Ī�Ǵ� ���� �Ŀ��ڸ���Ʈ ����',
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
     * ���� �Ŀ����߿��� �ϳ��� ���絵�� "1"�� �Ŀ��ڰ� �����ϴ� ���,
     * ��ǥ�Ŀ��ڹ�ȣ�� ������.
     */    
    IF (v_set_rep_cust_need_yn = 'Y') THEN
        sp_cdd_set_rep_customer(p_src_system, p_src_table, p_src_cust_id);
    END IF;
    
    --DBMS_OUTPUT.PUT_LINE('v_set_rep_cust_need_yn = '||p_src_cust_id||','||v_set_rep_cust_need_yn);	
    
    

EXCEPTION

    WHEN user_define_error1 THEN
           RAISE_APPLICATION_ERROR(-20001, '1��° ������ ȣ��ý���(p_caller_system)�� �ʼ��׸��Դϴ�. (��. BATCH, HOMEPAGE-FRONT, HOMEPAGE-ADMIN, CMA, ETL)');
    
    WHEN user_define_error2 THEN
           RAISE_APPLICATION_ERROR(-20002, '2��° ������ ȣ����(p_caller)�� �ʼ��׸��Դϴ�. (��. Ȩ������ȸ��ID, Ȩ������������ID, CMA�����ID, ETL�� ��� "ubigent")');
    
    WHEN user_define_error3 THEN
           RAISE_APPLICATION_ERROR(-20003, '3��° ������ �ּ����絵(p_min_simularity)�� �ִ� 1�� �ѱ� �� �����ϴ�. (��. ����Ʈ = 0)');

    WHEN user_define_error4 THEN
           RAISE_APPLICATION_ERROR(-20004, '4��° ������ ��õ�ý���(p_src_system)�� �ʼ��׸��̸�, ���Ǵ� ���� "FMS", "HOMEPAGE"�� ���˴ϴ�.');

    WHEN user_define_error5 THEN
           RAISE_APPLICATION_ERROR(-20005, '5��° ������ ��õ�����̺��(p_src_table)�� �ʼ��׸��̸�, ���Ǵ� ���� "SUPERM", "ACCOUNTM", "TH_MEMBER_MASTER"�� ���˴ϴ�.');

    WHEN user_define_error6 THEN
           RAISE_APPLICATION_ERROR(-20006, '6��° ������ ��õ��ID(p_src_cust_id)�� �ʼ��׸��Դϴ�.');

    WHEN user_define_error7 THEN
           RAISE_APPLICATION_ERROR(-20007, '7��° ������ ��õ������(p_src_cust_tp)�� �ʼ��׸��̸�, ���Ǵ� ���� "1"-����, "2"-����, "3"-��ü, "4"-�ܱ��θ� ���˴ϴ�.');
                
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
			'��Ī�Ǵ� ���� �Ŀ��ڸ���Ʈ ����',
			v_job_start_dm,
			TO_CHAR(SYSDATE,'yyyyMMddHH24miSS'),
			v_total_cnt,
			v_success_cnt,
			v_error_cnt,
			'FAILED',
			v_error_msg,
			'�迵��'
			);
		COMMIT;
        
        
        RAISE;
		
END sp_cdd_match_customer;


/
