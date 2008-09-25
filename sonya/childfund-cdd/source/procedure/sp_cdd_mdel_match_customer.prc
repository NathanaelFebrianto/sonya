CREATE OR REPLACE PROCEDURE sp_cdd_mdel_match_customer IS
tmpVar NUMBER;
/******************************************************************************
   NAME:       sp_cdd_mdel_match_customer
   PURPOSE:    

   REVISIONS:
   Ver        Date        Author           Description
   ---------  ----------  ---------------  ------------------------------------
   1.0        2008-09-25          1. Created this procedure.

   NOTES:

   Automatically available Auto Replace Keywords:
      Object Name:     sp_cdd_mdel_match_customer
      Sysdate:         2008-09-25
      Date and Time:   2008-09-25, 오후 6:37:43, and 2008-09-25 오후 6:37:43
      Username:         (set in TOAD Options, Procedure Editor)
      Table Name:       (set in the "New PL/SQL Object" dialog)

******************************************************************************/
BEGIN
   tmpVar := 0;
   EXCEPTION
     WHEN NO_DATA_FOUND THEN
       NULL;
     WHEN OTHERS THEN
       -- Consider logging the error and then re-raise
       RAISE;
END sp_cdd_mdel_match_customer;



/
