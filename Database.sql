data base schemea: (Government Smart Portal):

Enitties:
1: Global admin
2: City admin
3: Departmental admin
4: officers
5: Citizens
6: Department
7: City 

Attributes:

   Not choosed ID as the primary key because if the ID will    
      be changed then how will the ID can be primary key

Global Admin:

GA_ID    - interger 
GA_name  - varchar(20)
GA_email  - varchar(20)  (pk)
GA_password - integer

City Admin:

CA_ID  -  interger 
CA_name  - varchar(20)
CA_email  - varchar(20)  (pk)
CA_password  - integer
CA_age - integer
CA_dob   - date
CA_hiredate  - datetime

Departmental Admin:

DA_ID    - interger 
DA_name  - varchar(20)
DA_email  - varchar(20)   (pk)
DA_password   - integer
DA_dob  - date  
DA_hiredate   datetime

Officers:
OF_ID   -  integer (pk)
OF_email  - varchar(20)
OF_password  - integer
OF_name  - varchar(20)
OF_dob   - date
OF_TotalCasesSolved   -   integer
OF_TotalCasesoending   -   integer
OF_TotalUnsolvedCases  -   integer  
OF_dept   -  varchar(20)
OF_hiredate  -  datetime

Citizens:

CT_ID    - interger (PK)
CT_name  - varchar(20)
CT_email  - varchar(20)
CT_password    - interger 
CT_bod   - date
CT_Cnic  -   interger (PK)

Department:

Dept_ID    -  integer (pk)
Dept_name   -   varchar (20)
Dept_NoOfficers    -  integer
Total_departments    -  integer
Dept_type   -    varchar(20)  

City: 

C_Id   -  integer (pk)
C_name  -  varchar (20)
C_TotalDepts  -   integer
C_totalOfficers  - integer

Officer_Performance:

OF_ID   -  integer (pk)
OF_name  - varchar(20)
OF_TotalCasesSolved   -   integer
OF_TotalCasesoending   -   integer 
OF_TotalUnsolvedCases  -   integer 



NOTE 1: For safety and for criticalness of the global admin and city admin and deparmental adin we can do like ever time they login the login will include the ID also and a new ID will be assigned to them and the authority will be managed. 

NOTE 2: Now when we will add the deparmtent it will store in the and officer will also have the department for finding we will use qureies instead of making seperate depatments.








