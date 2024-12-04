BULK INSERT tst
   FROM 'd:\1.txt'
   WITH 
      (
         FIELDTERMINATOR = ',',
         ROWTERMINATOR = '\n'
      )

