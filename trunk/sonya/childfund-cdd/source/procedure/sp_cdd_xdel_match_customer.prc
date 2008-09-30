CREATE OR REPLACE PROCEDURE sp_cdd_xdel_match_customer
(
    p_src_system IN VARCHAR2,           -- �ʼ�: ��õ�ý���(��. FMS, HOMEPAGE) 
    p_src_table IN VARCHAR2,            -- �ʼ�: ��õ�����̺��(��. TEMP, SUPERM, ACCOUNTM, TH_MEMBER_MASTER, TEMP-SUPERM, TEMP-ACCOUNTM) 
    p_src_cust_id IN VARCHAR2,          -- �ʼ�: ��õ��ID 
    p_superm_cust_id IN VARCHAR2,       -- �ʼ�: ��Ī�Ŀ��ڹ�ȣ
    --p_manual_del_yn IN VARCHAR2,      -- �ʼ�: ��������('Y' �Ǵ� 'N') 
    p_remark IN VARCHAR2,               -- ���
    p_manual_user_id IN VARCHAR2        -- �ʼ�: ���۾� �����ID    
)
IS
/******************************************************************************
   NAME:       sp_cdd_xdel_match_customer
   PURPOSE:    

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        2008-09-25  �迵��           1. Created this procedure.

   NOTES:

   Automatically available Auto Replace Keywords:
      Object Name:     sp_cdd_xdel_match_customer
      Sysdate:         2008-09-25
      Date and Time:   2008-09-25, ���� 6:35:00, and 2008-09-25 ���� 6:35:00
      Username:         (set in TOAD Options, Procedure Editor)
      Table Name:       (set in the "New PL/SQL Object" dialog)

******************************************************************************/
user_define_error1 EXCEPTION;
user_define_error2 EXCEPTION;
user_define_error3 EXCEPTION;
user_define_error4 EXCEPTION;
--user_define_error5 EXCEPTION;
user_define_error6 EXCEPTION;

v_error_msg VARCHAR2(1000);
v_job_start_dm VARCHAR2(14);

BEGIN

    /* �Ķ���ͷ� ���� �Է°��� �������� üũ ��, �������� ���� ��� Exception ���� �߻� */
    IF (p_src_system IS NULL OR NOT(p_src_system = 'FMS' OR p_src_system = 'HOMEPAGE')) THEN
        RAISE user_define_error1;
    END IF;
    
    IF (p_src_table IS NULL OR NOT(p_src_table = 'SUPERM' OR p_src_table = 'ACCOUNTM' OR p_src_table = 'TH_MEMBER_MASTER' OR p_src_table = 'TEMP_SUPERM' OR p_src_table = 'TEMP_ACCOUNTM')) THEN
        RAISE user_define_error2;
    END IF;
    
    IF (p_src_cust_id IS NULL) THEN
        RAISE user_define_error3;
    END IF;
    
    IF (p_superm_cust_id IS NULL) THEN
        RAISE user_define_error4;
    END IF;
    
    /*
    IF (p_manual_del_yn IS NULL OR NOT(p_manual_del_yn = 'Y' OR p_manual_del_yn = 'N')) THEN
        RAISE user_define_error5;
    END IF;
    */

    IF (p_manual_user_id IS NULL) THEN
        RAISE user_define_error6;
    END IF;

    v_job_start_dm := TO_CHAR(SYSDATE, 'yyyymmddhh24miss');
    
    UPDATE dw_cdd_customer_matching SET
        fix_yn = 'Y',
        manual_yn = 'Y',
        --manual_del_yn = p_manual_del_yn,
        manual_del_yn = 'Y',
        remark = p_remark,
        manual_reg_user_id = p_manual_user_id,
        manual_upd_user_id = p_manual_user_id,
        manual_upd_dt = TO_CHAR(SYSDATE, 'yyyymmddhh24miss'),
        manual_del_dt = TO_CHAR(SYSDATE, 'yyyymmddhh24miss')
    WHERE src_system = p_src_system AND
        src_table = p_src_table AND
        src_cust_id = p_src_cust_id AND
        superm_cust_id = p_superm_cust_id;
    
    IF (p_src_system = 'FMS' AND p_src_table = 'SUPERM') THEN
    UPDATE dw_cdd_customer_matching SET
        fix_yn = 'Y'
        ,manual_yn = 'Y'
        --,manual_del_yn = p_manual_del_yn
        ,manual_del_yn = 'Y'
        --,remark = p_remark
        --,manual_reg_user_id = p_manual_user_id
        ,manual_upd_user_id = p_manual_user_id
        ,manual_upd_dt = TO_CHAR(SYSDATE, 'yyyymmddhh24miss')
        ,manual_del_dt = TO_CHAR(SYSDATE, 'yyyymmddhh24miss')
    WHERE src_system = p_src_system AND
        src_table = p_src_table AND
        src_cust_id = p_superm_cust_id AND
        superm_cust_id = p_src_cust_id;
    END IF;
    
    COMMIT;        

EXCEPTION
    WHEN user_define_error1 THEN
           RAISE_APPLICATION_ERROR(-20201, '1��° ������ ��õ�ý���(p_src_system)�� �ʼ��׸��̸�, ���Ǵ� ���� "FMS", "HOMEPAGE"�� ���˴ϴ�.');

    WHEN user_define_error2 THEN
           RAISE_APPLICATION_ERROR(-20202, '2��° ������ ��õ�����̺��(p_src_table)�� �ʼ��׸��̸�, ���Ǵ� ���� "SUPERM", "ACCOUNTM", "TH_MEMBER_MASTER", "TEMP-SUPERM", "TEMP-ACCOUNTM"�� ���˴ϴ�.');

    WHEN user_define_error3 THEN
           RAISE_APPLICATION_ERROR(-20203, '3��° ������ ��õ��ID(p_src_cust_id)�� �ʼ��׸��Դϴ�.');
    
    WHEN user_define_error4 THEN
           RAISE_APPLICATION_ERROR(-20204, '4��° ������ ��Ī�Ŀ��ڹ�ȣ(p_superm_cust_id)�� �ʼ��׸��Դϴ�.');

    /*
    WHEN user_define_error5 THEN
           RAISE_APPLICATION_ERROR(-20205, '5��° ������ ��������(p_manual_del_yn)�� �ʼ��׸��̸�, "Y" �Ǵ� "N"�� ���˴ϴ�.');
    */
    
    WHEN user_define_error6 THEN
           RAISE_APPLICATION_ERROR(-20206, '9��° ������ ���۾� �����ID(p_manual_user_id)�� �ʼ��׸��Դϴ�.');

    WHEN OTHERS THEN
        DBMS_OUTPUT.PUT_LINE(SQLERRM||'ERROR');    
        
        v_error_msg := SQLERRM;
        
        RAISE;

END sp_cdd_xdel_match_customer;



/
