CREATE DATABASE TestDB COLLATE Estonian_CS_AS
USE TestDB
CREATE TABLE TestPermTab (PrimaryKey int PRIMARY KEY, TextCol text )


CREATE DATABASE TestDB COLLATE Estonian_CS_AS
USE TestDB
CREATE TABLE TestPermTab (PrimaryKey int PRIMARY KEY, TextCol text )

CREATE DATABASE TestDB COLLATE Estonian_CS_AS
USE TestDB
CREATE TABLE TestPermTab (PrimaryKey int PRIMARY KEY, TextCol text )

CREATE DATABASE TestDB COLLATE Estonian_CS_AS
USE TestDB
CREATE TABLE TestPermTab (PrimaryKey int PRIMARY KEY, TextCol text )

EXEC('INSERT INTO '+@idtab+' VALUES ('''+@id+''', '''+@att+''')')


Specify a parameter name using an at sign (@) as the first character. The parameter name must conform to the rules for identifiers. Parameters are local to the procedure; the same parameter names can be used in other procedures. By default, parameters can take the place only of constants; they cannot be used in place of table names, column names, or the names of other database objects. For more information, see EXECUTE. 



BULK INSERT Northwind.dbo.[Order Details]
   FROM 'f:\orders\lineitem.tbl'
   WITH 
      (
         FIELDTERMINATOR = '|',
         ROWTERMINATOR = '|\n'
      )



************************

CREATE PROCEDURE [dbo].[insert2Table] 

@aSub char(8),
@bSub char(17),
@startDate char(50),
@dur decimal,
@pulse decimal,
@tableName sysname

AS
--DECLARE @tst table(aSub char(8), bSub char(17), startDate datetime, dur decimal (18,0), pulse decimal (18,0))
Exec ( 'Insert into ' + @tableName + ' values ( ''' + @aSub + ''', ''' + @bSub + ''', ''' + @startDate + ''', ''' + @dur + ''',''' + @pulse + ''') ' )
GO

************************