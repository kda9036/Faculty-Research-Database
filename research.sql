DROP DATABASE IF EXISTS  research; 

CREATE DATABASE research;
USE research;

DROP TABLE IF EXISTS  faculty;
CREATE TABLE faculty(
	faculty_ID INT UNSIGNED NOT NULL AUTO_INCREMENT,
	lastName VARCHAR(45) NOT NULL,
	firstName VARCHAR(45) NOT NULL,
	buildingNum INT NULL,
	officeNum INT NULL,
	email VARCHAR(254) NULL,
    	user_password VARCHAR(1024) DEFAULT NULL,  # will store hashed
	PRIMARY KEY (faculty_ID),
    	CONSTRAINT faculty_email UNIQUE (email)
)ENGINE=InnoDB DEFAULT CHARSET= utf8mb4;
  
DROP TABLE IF EXISTS  student;
CREATE TABLE student(
	student_ID INT UNSIGNED NOT NULL AUTO_INCREMENT,
	lastName VARCHAR(45) NOT NULL,
	firstName VARCHAR(45) NOT NULL,
	email VARCHAR(254) NOT NULL,
	word1 VARCHAR(45) NOT NULL,
	word2 VARCHAR(45) NULL,
	word3 VARCHAR(45) NULL,
	PRIMARY KEY (student_ID),
    	CONSTRAINT student_email UNIQUE (email)
)ENGINE=InnoDB DEFAULT CHARSET= utf8mb4;

DROP TABLE IF EXISTS  public;
CREATE TABLE public(
	public_ID INT UNSIGNED NOT NULL AUTO_INCREMENT,
	publicName VARCHAR(45) NOT NULL,
	email VARCHAR(254) NOT NULL,
	word1 VARCHAR(45) NOT NULL,
	word2 VARCHAR(45) NULL,
	word3 VARCHAR(45) NULL,
	PRIMARY KEY (public_ID)
)ENGINE=InnoDB DEFAULT CHARSET= utf8mb4;

DROP TABLE IF EXISTS abstract;
CREATE TABLE abstract(
	abstract_ID INT UNSIGNED NOT NULL AUTO_INCREMENT,
	abstract_type VARCHAR(10) NOT NULL, # book vs speech
	title VARCHAR(250) NOT NULL,
    	about MEDIUMTEXT NOT NULL,
	PRIMARY KEY (abstract_ID)
)ENGINE=InnoDB DEFAULT CHARSET= utf8mb4;

DROP TABLE IF EXISTS faculty_abstract;
CREATE TABLE faculty_abstract(
	faculty_ID INT UNSIGNED NOT NULL,
	abstract_ID INT UNSIGNED NOT NULL,
	PRIMARY KEY (faculty_ID, abstract_ID),
	CONSTRAINT faculty_abstract_facultyID_FK FOREIGN KEY (faculty_ID) 
      REFERENCES faculty(faculty_ID)
            ON DELETE CASCADE
            ON UPDATE CASCADE,
	CONSTRAINT faculty_abstract_abstractID_FK FOREIGN KEY (abstract_ID) 
      REFERENCES abstract(abstract_ID)
            ON DELETE CASCADE
            ON UPDATE CASCADE
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO faculty (lastName, firstName, buildingNum, officeNum, email) 
	VALUES ("Habermas", "Jim", 70, 2673, "jim.habermas@rit.edu");

INSERT INTO faculty (lastName, firstName, buildingNum, officeNum, email) 
	VALUES ("Defenbaugh", "George", 70, 2600, "george.defenbaugh@rit.edu");

INSERT INTO faculty (lastName, firstName, buildingNum, officeNum, email) 
	VALUES ("Smedley", "Richard", Null, Null, Null);

INSERT INTO faculty (lastName, firstName, buildingNum, officeNum, email) 
	VALUES ("Ericson", "Barbara", 70, 2700, "barbara.ericson@rit.edu");

INSERT INTO student (lastName, firstName, email, word1, word2, word3)
            VALUES ("Appleton", "Kelly", "kda9036@rit.edu", "SQL", "Web", "Java");

INSERT INTO public (publicName, email, word1, word2, word3)
             VALUES ("Kodak", "kodak@kodak.com", "Java", "C#", NULL);            

INSERT INTO abstract (abstract_type, title, about)
             VALUES ("book", "Learn C and C++ by Samples", "This book, Learn C and C++ by Samples written by James R. Habermas, is a companion to A First Book Ansi C++ by Gary Bronson.  It is the authors firm belief that one can never have too many samples.  If a textbook is to be useful, it needs primary support through an instructor and/or more samples.  This textbook contains a wealth of useful C & C++ samples that are fashioned to further demonstrate the topics outlined in the text."); 

INSERT INTO abstract (abstract_type, title, about)
             VALUES ("book", "C through Design", "C through Design  -   George E. Defenbaugh, Richard Smedley, 1988.  This book presents standard C, i.e., code that compiles cleanly with a compiler that meets the ANSI C standard.  This book has over 90 example programs that illustrate the topics of each chapters.  In addition complete working programs are developed fully, from design to program output.  This book is filled with Antibugging Notes (the stress traps to be avoided), and Quick Notes, that emphasize important points to be remembered.");

INSERT INTO abstract (abstract_type, title, about)
             VALUES ("book", "Introduction to Computing and Programming in PYTHON - A Multimedia Approach", "Introduction to Computing and Programming in PYTHON - A Multimedia Approach by Barbara Ericson.  The programming language used in this book is Python.  Python has been described as executable pseudo-code.  I have found that both computer science majors and non majors can learn Python.  Since Python is actually used for communications tasks (e.g., Web site Development), it's relevant language for an in introductory computing course.  The specific dialect of Python used in this book is Jython.  Jython is Python.  The differences between Python (normally implemented in C) and Jython (which is implemented in Java) are akin to the differences between any two language implementations (e.g., Microsoft vs. GNU C++ implementations).");
             
INSERT INTO faculty_abstract VALUES (1, 1);

INSERT INTO faculty_abstract VALUES (2, 2);

INSERT INTO faculty_abstract VALUES (3, 2);

INSERT INTO faculty_abstract VALUES (4, 3);

