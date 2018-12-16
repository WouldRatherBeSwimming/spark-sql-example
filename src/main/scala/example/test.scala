package example

/*
 * 
 * 
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
 */


////////// Java HTTP Imports 
import java.net.URL;
import java.net.HttpURLConnection
import scala.collection.JavaConverters._
import scala.collection.mutable._

////////// Spark Imports 
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.storage.StorageLevel
import org.apache.spark.sql.{Row, Column, SparkSession}
import org.apache.spark.sql.SaveMode


case class student(id:Integer, name:String, major:String)
case class professor(id:Integer, name:String, course:String)
case class studentProfessor(studentID:Integer, professorID:Integer)

object test {
    
  def main(args : Array[String]) {

    // handle command line args
    val DB   = args(0)
    
    // setup spark
    var sparkConf = new SparkConf().setAppName("query and code example")
    val sc = new SparkContext(sparkConf) 
    val spark = SparkSession.builder.config(sparkConf).getOrCreate()
    import spark.implicits._


    // generate sample data 
    var studentList = List(student(1,"Ralph","Art History"), 
                           student(2,"Robert", "Computer Science"),
                           student(3, "Anne", "Electrical Engineering"), 
                           student(4, "Jane", "Biology"))

    var studentProfessorList = List(studentProfessor(1,1),
                                    studentProfessor(1,3),
                                    studentProfessor(1,4),
                                    studentProfessor(2,2), 
                                    studentProfessor(3,1),
                                    studentProfessor(3,4))
                                    
    var professorList = List(professor(1, "Mr. Smith", "Linear Algebra"),
                             professor(2, "Professor X", "Telekinesis"), 
                             professor(3, "Mrs. Smith", "Anatomy"),
                             professor(4, "Einstein", "Relativity"))  
    
    var studentDF = studentList.toDF()
    var studentProfessorDF = studentProfessorList.toDF()
    var professorDF = professorList.toDF()
    
    // write to hive tables to utilize Hue interface while writing query 
    //(for example purposes only -- during runtime once query built could stick with df's / tempviews rather than writing to hive) 
    studentDF.write.mode(SaveMode.Overwrite).saveAsTable("%s.student".format(DB))
    studentProfessorDF.write.mode(SaveMode.Overwrite).saveAsTable("%s.studentProfessor".format(DB))
    professorDF.write.mode(SaveMode.Overwrite).saveAsTable("%s.professor".format(DB))
    
    //write a SQL query that finds all the Professors for each Student.
    var query = spark.sql("SELECT student.name student_name, professor.name professor_name FROM example.student INNER JOIN example.studentprofessor ON student.id=studentprofessor.studentid INNER JOIN example.professor ON studentprofessor.professorid=professor.id")
    query.show()
    /*
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
     */
       
  }
}
