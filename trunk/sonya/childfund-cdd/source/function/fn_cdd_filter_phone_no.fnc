CREATE OR REPLACE FUNCTION fn_cdd_filter_phone_no
(
    p_phone_no IN VARCHAR2
)
RETURN VARCHAR2
IS
/******************************************************************************
   NAME:       fn_cdd_filter_phone_no
   PURPOSE:    

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        2008-09-18  배영규           1. Created this function.

   NOTES:

   Automatically available Auto Replace Keywords:
      Object Name:     fn_cdd_filter_phone_no
      Sysdate:         2008-09-18
      Date and Time:   2008-09-18, 오후 2:48:39, and 2008-09-18 오후 2:48:39
      Username:         (set in TOAD Options, Procedure Editor)
      Table Name:       (set in the "New PL/SQL Object" dialog)

******************************************************************************/
v_filtered_phone_no VARCHAR2(100);

BEGIN
    v_filtered_phone_no := p_phone_no;
    v_filtered_phone_no := TRIM(v_filtered_phone_no);
    v_filtered_phone_no := REPLACE(v_filtered_phone_no, ' ', '');
    v_filtered_phone_no := REPLACE(v_filtered_phone_no, '-', '');
   
    RETURN v_filtered_phone_no;
   
    EXCEPTION
        WHEN OTHERS THEN
            DBMS_OUTPUT.PUT_LINE(SQLERRM||'ERROR');	
            -- Consider logging the error and then re-raise
            RETURN p_phone_no;
END;


/
