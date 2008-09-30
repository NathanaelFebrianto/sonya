CREATE OR REPLACE PROCEDURE sf_cdd_customer_match_table_3
IS
/******************************************************************************
   NAME:       sf_cdd_customer_match_table_3 
   PURPOSE:    TH_MEMBER_MASTER -> DW_CDD_CUSTOMER_MATCHING ���̺�� �ʱ�����

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
        'FMS' AS src_system,        -- ��õ�ý���
        'ACCOUNTM' AS src_table,    -- ��õ�����̺��
        account_id AS src_cust_id,  -- ��õ��ID
        acctype_dv AS src_cust_tp,  -- ��õ������
        accountname AS cust_nm,     -- ����
        cust_id AS superm_cust_id,  -- �Ŀ��ڹ�ȣ
        NULL AS jumin_no,           -- �ֹε�Ϲ�ȣ
        NULL AS biz_reg_no,         -- ����ڵ�Ϲ�ȣ
        NULL AS cmpy_reg_no,        -- ���ε�Ϲ�ȣ
        NULL AS for_reg_no,         -- �ܱ��ε�Ϲ�ȣ
        NULL AS passport_no,        -- ���ǹ�ȣ
        mophnum AS mobile_no,       -- �޴�����ȣ
        email AS email,             -- �̸���
        zipcode AS zipcode,         -- ����������ּ�_�����ȣ
        address AS addr1,           -- ����������ּ�_�ּ�
        addressdtl AS addr2         -- ����������ּ�_���ּ�        
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
                'BATCH',                -- ȣ��ý���
                '�迵��',               -- ȣ����
                0,                      -- �ּ� ���絵
                srclist.src_system,     -- ��õ�ý���
                srclist.src_table,      -- ��õ���̺��
                srclist.src_cust_id,    -- ��õ��ID
                srclist.src_cust_tp,    -- ��õ������
                srclist.cust_nm,        -- ����
                srclist.superm_cust_id, -- �Ŀ��ڹ�ȣ
                srclist.jumin_no,       -- �ֹε�Ϲ�ȣ
                srclist.biz_reg_no,     -- ����ڵ�Ϲ�ȣ
                srclist.cmpy_reg_no,    -- ���ε�Ϲ�ȣ
                srclist.for_reg_no,     -- �ܱ��ε�Ϲ�ȣ
                srclist.passport_no,    -- ���ǹ�ȣ
                srclist.mobile_no,      -- �޴�����ȣ
                srclist.email,          -- �̸���
                srclist.zipcode,        -- ����������ּ�_�����ȣ
                srclist.addr1,          -- ����������ּ�_�ּ�
                srclist.addr2          -- ����������ּ�_���ּ�     
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
		'�ʱ�����-����Ī���̺����-SUPERM',
		v_job_start_dm,
		TO_CHAR(SYSDATE,'yyyyMMddHH24miSS'),
		v_total_cnt,
		v_success_cnt,
		v_error_cnt,
		'COMPLETED',
		'',
		'�迵��'
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
			'�ʱ�����-����Ī���̺����-SUPERM',
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
		
END sf_cdd_customer_match_table_3;


/
