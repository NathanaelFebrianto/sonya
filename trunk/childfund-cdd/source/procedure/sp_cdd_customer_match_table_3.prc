CREATE OR REPLACE PROCEDURE sp_cdd_customer_match_table_3
(
    p_base_dt IN VARCHAR2    -- ��������
) 
IS
/******************************************************************************
   NAME:       sp_cdd_customer_match_table_3 
   PURPOSE:    TH_MEMBER_MASTER-> DW_CDD_CUSTOMER_MATCHING ���̺�� ����

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        2008-09-09  �迵��           1. Created this procedure.

   NOTES:

   Automatically available Auto Replace Keywords:
      Object Name:     sf_cdd_customer_match_table_3  
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

CURSOR cur IS
    SELECT 
        'HOMEPAGE' AS src_system            -- ��õ�ý���
        ,'TH_MEMBER_MASTER' AS src_table    -- ��õ�����̺��
        ,id AS src_cust_id                  -- ��õ��ID
        ,'1' AS src_cust_tp                 -- ��õ������
        ,name AS cust_nm                    -- ����
        ,NULL AS superm_cust_id             -- �Ŀ��ڹ�ȣ
        ,number1||number2 AS jumin_no       -- �ֹε�Ϲ�ȣ
        ,NULL AS biz_reg_no                 -- ����ڵ�Ϲ�ȣ
        ,NULL AS cmpy_reg_no                -- ���ε�Ϲ�ȣ
        ,NULL AS for_reg_no                 -- �ܱ��ε�Ϲ�ȣ
        ,NULL AS passport_no                -- ���ǹ�ȣ
        ,phone1||phone2||phone3 AS mobile_no-- �޴�����ȣ
        ,email AS email                     -- �̸���
        ,replace(post, '-', '') AS zipcode  -- ����������ּ�_�����ȣ
        ,address AS addr1                   -- ����������ּ�_�ּ�
        ,address_dtl AS addr2               -- ����������ּ�_���ּ�        
    FROM temp_hmp_old_member
    WHERE 
        id IS NOT NULL
        --AND fstoper_dt >= p_base_dt OR lastupdate_dt >= p_base_dt;

    /*
    -- ������ �߻��� rownum���� �����ϰ��� �ϴ� ���
    FROM (SELECT ROWNUM rowno, a.* FROM temp_hmp_old_member a) 
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
                'BATCH'                 -- ȣ��ý���
                ,'�迵��'               -- ȣ����
                ,0                      -- �ּ� ���絵
                ,srclist.src_system     -- ��õ�ý���
                ,srclist.src_table      -- ��õ���̺��
                ,srclist.src_cust_id    -- ��õ��ID
                ,srclist.src_cust_tp    -- ��õ������
                ,srclist.cust_nm        -- ����
                ,srclist.superm_cust_id -- �Ŀ��ڹ�ȣ
                ,srclist.jumin_no       -- �ֹε�Ϲ�ȣ
                ,srclist.biz_reg_no     -- ����ڵ�Ϲ�ȣ
                ,srclist.cmpy_reg_no    -- ���ε�Ϲ�ȣ
                ,srclist.for_reg_no     -- �ܱ��ε�Ϲ�ȣ
                ,srclist.passport_no    -- ���ǹ�ȣ
                ,srclist.mobile_no      -- �޴�����ȣ
                ,srclist.email          -- �̸���
                ,srclist.zipcode        -- ����������ּ�_�����ȣ
                ,srclist.addr1          -- ����������ּ�_�ּ�
                ,srclist.addr2          -- ����������ּ�_���ּ�     
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
                ,'sp_cdd_customer_match_table_3' 
                ,'����Ī���̺���� Batch����-TH_MEMBER_MASTER'
                ,v_job_start_dm
                --,TO_CHAR(SYSDATE,'yyyyMMddHH24miSS')
                --,v_total_cnt
                ,v_success_cnt
                ,v_error_cnt
                ,'START'
                ,''
                ,'�迵��'
                );
            
            COMMIT;
        
        ELSIF (v_total_cnt > 1) THEN
            UPDATE dw_etl_job_hist SET
                succ_cnt = v_success_cnt
                ,job_stat = 'WORKING'
            WHERE        
                job_no = v_job_start_dm AND job_id = 'sp_cdd_customer_match_table_3';
            
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
		,'sp_cdd_customer_match_table_3' 
		,'����Ī���̺���� Batch����-TH_MEMBER_MASTER'
		,v_job_start_dm
		,TO_CHAR(SYSDATE,'yyyyMMddHH24miSS')
		,v_total_cnt
		,v_success_cnt
		,v_error_cnt
		,'COMPLETED'
		,''
		,'�迵��'
		);
    */
    
    UPDATE dw_etl_job_hist SET
        job_end_dm = TO_CHAR(SYSDATE,'yyyyMMddHH24miSS')
        ,tot_src_cnt = v_total_cnt
        ,succ_cnt = v_success_cnt
        ,job_stat = 'COMPLETED'
    WHERE        
        job_no = v_job_start_dm AND job_id = 'sp_cdd_customer_match_table_3';    
    
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
                ,'sp_cdd_customer_match_table_3' 
                ,'����Ī���̺���� Batch����-TH_MEMBER_MASTER'
                ,v_job_start_dm
                ,TO_CHAR(SYSDATE,'yyyyMMddHH24miSS')
                ,v_total_cnt
                ,v_success_cnt
                ,v_error_cnt
                ,'FAILED'
                ,v_error_msg
                ,'�迵��'
                );
        ELSE
            UPDATE dw_etl_job_hist SET
                job_end_dm = TO_CHAR(SYSDATE,'yyyyMMddHH24miSS')
                ,tot_src_cnt = v_total_cnt
                ,succ_cnt = v_success_cnt
                ,job_stat = 'FAILED'
                ,job_log = v_error_msg
            WHERE        
                job_no = v_job_start_dm AND job_id = 'sp_cdd_customer_match_table_3';
        END IF;
        
		COMMIT;
		
END sp_cdd_customer_match_table_3;


/
