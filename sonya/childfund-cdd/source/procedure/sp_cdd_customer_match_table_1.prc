CREATE OR REPLACE PROCEDURE sp_cdd_customer_match_table_1 
(
    p_base_dt IN VARCHAR2    -- ��������
) 
IS
/******************************************************************************
   NAME:       sp_cdd_customer_match_table_1
   PURPOSE:    SUPERM -> DW_CDD_CUSTOMER_MATCHING ���̺�� ����

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        2008-09-09  �迵��           1. Created this procedure.

   NOTES:

   Automatically available Auto Replace Keywords:
      Object Name:     sp_cdd_customer_match_table_1
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
        'FMS' as src_system,        -- ��õ�ý���
        'SUPERM' as src_table,      -- ��õ�����̺��
        cust_id as src_cust_id,     -- ��õ��ID
        supertype_dv as src_cust_tp,-- ��õ������
        supername as cust_nm,       -- ����
        cust_id as superm_cust_id,  -- �Ŀ��ڹ�ȣ
        rr_id as jumin_no,          -- �ֹε�Ϲ�ȣ
        br_id as biz_reg_no,        -- ����ڵ�Ϲ�ȣ
        null as cmpy_reg_no,        -- ���ε�Ϲ�ȣ
        fr_id as for_reg_no,        -- �ܱ��ε�Ϲ�ȣ
        null as passport_no,        -- ���ǹ�ȣ
        supermophnum as mobile_no,  -- �޴�����ȣ
        superemail as email,        -- �̸���
        superzipcdde_2 as zipcode,  -- ����������ּ�_�����ȣ
        address_2 as addr1,         -- ����������ּ�_�ּ�
        addressdtl_2 as addr2       -- ����������ּ�_���ּ�        
    FROM superm@FMS
    WHERE 
        fstoper_dt >= p_base_dt OR lastupdate_dt >= p_base_dt;


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
		'sp_cdd_customer_match_table_1', 
		'����Ī���̺���� Batch����-SUPERM',
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
			'sp_cdd_customer_match_table_1', 
			'����Ī���̺���� Batch����-SUPERM',
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
		
END sp_cdd_customer_match_table_1;


/
