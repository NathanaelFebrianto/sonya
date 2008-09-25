CREATE OR REPLACE FUNCTION fn_cdd_get_rep_customer
(
    p_src_system IN VARCHAR2,           -- ��õ�ý���(��. FMS, HOMEPAGE) 
    p_src_table IN VARCHAR2,            -- ��õ�����̺��(��. SUPERM, ACCOUNTM, TH_MEMBER_MASTER) 
    p_src_cust_id IN VARCHAR2           -- ��õ��ID 
)
RETURN VARCHAR2
IS
/******************************************************************************
   NAME:       fn_cdd_get_rep_customer
   PURPOSE:    

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        2008-09-24  �迵��           1. Created this function.

   NOTES:

   Automatically available Auto Replace Keywords:
      Object Name:     fn_cdd_get_rep_customer
      Sysdate:         2008-09-24
      Date and Time:   2008-09-24, ���� 11:17:35, and 2008-09-24 ���� 11:17:35
      Username:         (set in TOAD Options, Procedure Editor)
      Table Name:       (set in the "New PL/SQL Object" dialog)

******************************************************************************/
user_define_error EXCEPTION;
v_superm_cust_id VARCHAR2(11);

BEGIN
    
    IF (p_src_system = 'FMS' AND p_src_table = 'SUPERM') THEN
        SELECT superm_cust_id into v_superm_cust_id FROM dw_cdd_customer_matching
        WHERE (src_system = p_src_system AND src_table = p_src_table AND rep_superm_cust_yn = 'Y' AND simularity = 1)
            AND (src_cust_id = p_src_cust_id OR superm_cust_id = p_src_cust_id
            OR src_cust_id IN (
                SELECT src_cust_id FROM dw_cdd_customer_matching 
                WHERE superm_cust_id = p_src_cust_id));
    
    ELSIF (p_src_system = 'FMS' AND p_src_table = 'ACCOUNTM') THEN
        SELECT superm_cust_id into v_superm_cust_id FROM dw_cdd_customer_matching
            WHERE src_system = p_src_system AND src_table = p_src_table AND src_cust_id = p_src_cust_id AND rep_superm_cust_yn = 'Y' AND simularity = 1;
    
    ELSIF (p_src_system = 'HOMEPAGE' AND p_src_table = 'TH_MEMBER_MASTER') THEN
        SELECT superm_cust_id into v_superm_cust_id FROM dw_cdd_customer_matching
            WHERE src_system = p_src_system AND src_table = p_src_table AND src_cust_id = p_src_cust_id AND rep_superm_cust_yn = 'Y' AND simularity = 1;     

    ELSE
        RAISE user_define_error; 
    
    END IF;
    
    RETURN v_superm_cust_id;

    EXCEPTION
        WHEN user_define_error THEN
           RAISE_APPLICATION_ERROR(-20101, '��õ�ý��۸�� ��õ���̺���� �������� �ʽ��ϴ�. �ٽ� Ȯ���Ͻñ� �ٶ��ϴ�. [p_src_system = '||p_src_system||'] or [p_src_table = '||p_src_table||'] ��õ�ý���(p_src_system)�� "FMS", "HOMEPAGE"�� ���Ǹ�, ��õ�����̺��(p_src_table)�� "SUPERM", "ACCOUNTM", "TH_MEMBER_MASTER"�� ���˴ϴ�.');
        WHEN NO_DATA_FOUND THEN
            RETURN NULL;
        WHEN OTHERS THEN
            -- Consider logging the error and then re-raise
            DBMS_OUTPUT.PUT_LINE(SQLERRM||'ERROR');
            RAISE;


END fn_cdd_get_rep_customer;



/
