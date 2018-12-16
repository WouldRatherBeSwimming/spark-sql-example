# spark-sql-example
given the following tables, write a SQL query that finds all the Professors for each Student.

```
|--------------------------------------|
|             Student                  |
|======================================|
| ID  | Name    |Major                 |
|--------------------------------------|
| 01  | Ralph   |Art History           |
| 02  | Robert  |Computer Science      |
| 03  | Anne    |Electrical Engineering|
| 04  | Jane    |Biology               |
|--------------------------------------|

|--------------------------|
|   StudentProfessor       |
|==========================|
| StudentID  | ProfessorID |
|--------------------------|
|     01     |      01     |
|     01     |      03     |
|     01     |      04     |
|     02     |      02     |
|     03     |      01     |
|     03     |      04     |
|--------------------------|

|--------------------------------------|
|             Professor                |
|======================================|
| ID  | Name         |     Class       |
|--------------------------------------|
| 01  | Mr. Smith    | Linear Algebra  |
| 02  | Professor X  | Telekinesis     |
| 03  | Mrs. Smith   | Anatomy         |
| 04  | Einstein     | Relativity      |
|--------------------------------------|
``` 

Answer:

```
SELECT student.name student_name, professor.name professor_name
FROM example.student
INNER JOIN example.studentprofessor ON student.id=studentprofessor.studentid 
INNER JOIN example.professor ON studentprofessor.professorid=professor.id
```

      +------------+--------------+
      |student_name|professor_name|
      +------------+--------------+
      |        Anne|      Einstein|
      |        Anne|     Mr. Smith|
      |      Robert|   Professor X|
      |       Ralph|      Einstein|
      |       Ralph|    Mrs. Smith|
      |       Ralph|     Mr. Smith|
      +------------+--------------+    
